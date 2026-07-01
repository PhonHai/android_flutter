---
name: flutter-best-practices
description: Flutter 架构最佳实践。基于 Flutter 官方 Architecture Recommendations。改 Dart / Widget / Riverpod / sqflite / MethodChannel 代码时自动加载。强制 UDF、不可变数据、分层架构。
applies_to: flutter
user-invocable: false
---

# Flutter 架构最佳实践

> **来源**：[Flutter 官方 Architecture Recommendations](https://docs.flutter.dev/app-architecture/recommendations)
>
> **适用**：`flutter_module/`、以及 `native-android` 中与 Flutter 交互的部分。

---

## 1. 关注点分离（最核心原则）

```
┌─────────────────────────────────────────────────┐
│                   UI 层                          │
│  ┌──────────────┐    ┌────────────────────┐      │
│  │   Widget      │ ←→ │   ViewModel        │      │
│  │  (只看不写)    │    │  (ChangeNotifier /  │      │
│  │              │    │   Riverpod Notifier) │      │
│  └──────────────┘    └────────┬───────────┘      │
├─────────────────────────────────┼────────────────┤
│                  数据层          │                │
│  ┌──────────────┐    ┌──────────▼───────────┐   │
│  │  Repository   │ ←→ │   Service (API/DB)    │   │
│  │  (抽象接口)    │    │   (具体实现)          │   │
│  └──────────────┘    └──────────────────────┘   │
└─────────────────────────────────────────────────┘
```

### 铁的纪律
- **Widget 里不放业务逻辑**：只有 `if` 判断显隐、动画、简单路由
- **Repository 是数据的唯一来源**（Single Source of Truth）
- **数据只从数据层流向 UI 层**，不反向

---

## 2. 不可变数据模型（MUST）

### ✅ 所有字段 `final` + `copyWith`
```dart
class TimerState {
  final int remainingSeconds;
  final bool isRunning;
  final int sessions;

  const TimerState({
    this.remainingSeconds = 1500,
    this.isRunning = false,
    this.sessions = 0,
  });

  TimerState copyWith({
    int? remainingSeconds,
    bool? isRunning,
    int? sessions,
  }) {
    return TimerState(
      remainingSeconds: remainingSeconds ?? this.remainingSeconds,
      isRunning: isRunning ?? this.isRunning,
      sessions: sessions ?? this.sessions,
    );
  }
}
```

> ⚠️ **本项目**：`timer/timer_state.dart` 的 `TimerState` 用了 `copyWith`，是正确的。`PomodoroRecord` 也是 `final` 字段，方向对。

### ❌ 禁止
```dart
// ❌ 可变字段
class BadState {
  int remainingSeconds = 1500;   // 不是 final，外部可以改
}

// ❌ 没有 copyWith
class BadState {
  final int remainingSeconds;
  // 外部只能 new BadState(...) 全部字段重写
}
```

### 进阶：用 freezed（推荐，本项目暂未引入）
```dart
// freezed 自动生成 copyWith / == / hashCode / toString
@freezed
class TimerState with _$TimerState {
  const factory TimerState({
    @Default(1500) int remainingSeconds,
    @Default(false) bool isRunning,
    @Default(0) int sessions,
  }) = _TimerState;
}
```

---

## 3. Repository 模式（数据层隔离）

### ✅ 标准写法
```dart
// 抽象接口（可切换实现）
abstract class PomodoroRepository {
  Future<void> saveRecord(PomodoroRecord record);
  Future<List<PomodoroRecord>> getRecords();
}

// 具体实现
class SqlitePomodoroRepository implements PomodoroRepository {
  final HistoryDb _db;

  SqlitePomodoroRepository(this._db);

  @override
  Future<void> saveRecord(PomodoroRecord record) async {
    await _db.insert(record.toMap());
  }

  @override
  Future<List<PomodoroRecord>> getRecords() async {
    final rows = await _db.queryAll();
    return rows.map(PomodoroRecord.fromMap).toList();
  }
}
```

> ⚠️ **本项目**：`history/history_db.dart` 直接在 Widget/Notifier 层操作数据库，建议未来引入 Repository 层隔离。

### 为什么需要 Repository？
- 测试时可以换 Fake 实现（不依赖真实数据库）
- 切换数据源（sqflite → MethodChannel → Room）时只改 Repository 实现
- Widget 不知道数据来自哪里（数据库？网络？缓存？）

---

## 4. 单向数据流（UDF）在 Flutter 中

```
           State 向下流（read）
  ┌──────────────────────────────┐
  ↓                              │
┌──────┐                   ┌──────────┐
│Widget│                   │ViewModel │
│(read)│                   │(write)   │
└──────┘                   └──────────┘
  │                              ↑
  └──────────────────────────────┘
           Event 向上传（callback）
```

### Riverpod 版（本项目用法）
```dart
// ✅ TimerNotifier 管理状态，Widget 只读
class TimerNotifier extends Notifier<TimerState> {
  @override
  TimerState build() => const TimerState();

  void toggle() {
    state = state.copyWith(isRunning: !state.isRunning);
  }
}

// Widget 消费
class TimerWidget extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final timerState = ref.watch(timerNotifierProvider);
    // 只读 timerState，不直接改
    return Text('${timerState.remainingSeconds}');
  }
}
```

> ⚠️ **本项目**：`timer/timer_notifier.dart` + `timer/timer_widget.dart` 的方向是正确的：Notifier 写 State，Widget 只读。

### ChangeNotifier 版（另一个推荐方案）
```dart
class TimerViewModel extends ChangeNotifier {
  TimerState _state = const TimerState();
  TimerState get state => _state;

  void toggle() {
    _state = _state.copyWith(isRunning: !_state.isRunning);
    notifyListeners();
  }
}
```

---

## 5. Widget 层规范

### ✅ Widget 里只能有什么
```dart
class MyWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        // ✅ if 判断显隐
        if (isLoading) const CircularProgressIndicator(),

        // ✅ 简单路由
        TextButton(
          onPressed: () => Navigator.push(context, MaterialPageRoute(...)),
          child: const Text('Go'),
        ),

        // ✅ 动画
        AnimatedOpacity(opacity: visible ? 1.0 : 0.0, ...),
      ],
    );
  }
}
```

### ❌ Widget 里不能有什么
```dart
// ❌ 业务逻辑
if (state.remainingSeconds <= 0 && !state.isRunning) {
  _db.insert(...);  // 错！应该通过 ViewModel/Notifier
}

// ❌ 网络请求
http.get('https://...').then(...); // 错！应该放 Repository

// ❌ 复杂计算
final stats = records.fold(0, (sum, r) => sum + r.duration); // 错！
```

---

## 6. 依赖注入（DI）

### ✅ Provider / Riverpod
```dart
// 声明
final pomodoroRepositoryProvider = Provider<PomodoroRepository>((ref) {
  return SqlitePomodoroRepository(HistoryDb());
});

// 使用
class TimerNotifier extends Notifier<TimerState> {
  @override
  TimerState build() {
    final repo = ref.read(pomodoroRepositoryProvider);
    // ...
  }
}
```

> ⚠️ **本项目**：当前没有用 DI 容器，`HistoryDb` 是直接实例化的。对于小项目可以接受，但未来如果有多个数据源建议引入。

### ❌ 禁止
```dart
// ❌ 全局单例访问
final db = HistoryDb.instance;  // 测试时无法替换
```

---

## 7. 导航规范

### Flutter 官方推荐：[go_router](https://pub.dev/packages/go_router)
```dart
final router = GoRouter(
  routes: [
    GoRoute(
      path: '/',
      builder: (context, state) => const HomePage(),
      routes: [
        GoRoute(
          path: 'detail/:itemId',
          builder: (context, state) => DetailPage(
            itemId: state.pathParameters['itemId']!,
          ),
        ),
      ],
    ),
  ],
);
```

> ⚠️ **本项目**：当前用的是 `Navigator.push` + 构造函数传参。对于学习项目够用，但生产项目建议用 `go_router`。

---

## 8. 命名约定

| 组件 | 命名 | 示例 |
|------|------|------|
| ViewModel | `XxxViewModel` / `XxxNotifier` | `TimerNotifier` |
| Widget（页面） | `XxxPage` / `XxxScreen` | `HomePage` |
| Repository | `XxxRepository` | `PomodoroRepository` |
| Service | `XxxService` / `XxxApiService` | `HistoryApiService` |
| Model | 名词 | `PomodoroRecord` |
| 共享 Widget | 放 `ui/core/` 目录（不叫 `widgets/`） | — |

> ⚠️ **本项目**：命名上已经接近规范（`TimerNotifier`、`PomodoroRecord`、`HistoryDb`），页面命名可以统一为 `XxxPage`（现在混用了 `_page` 和 `_screen`）。

---

## 9. 测试金字塔

```
        /\
       /E2E\         少量端到端测试
      /──────\
     /集成测试\       中等数量
    /──────────\
   /  单元测试  \     最多
  ────────────────
```

### 必须测
- **Repository → 单元测试**（用 Fake 实现）
- **ViewModel/Notifier → 单元测试**（mock Repository）
- **Widget → Widget Test**（用 `pumpWidget`）

> ⚠️ **本项目**：当前只有 `test/widget_test.dart` 一个测试。建议至少为 `TimerNotifier` 和 `HistoryDb` 加单元测试。

---

## 10. 本项目对照检查

| 检查项 | 当前状态 | 建议 |
|--------|:--:|------|
| 不可变数据模型 | ✅ `copyWith` | — |
| UDF 模式 | ✅ Riverpod Notifier | — |
| Repository 模式 | ❌ 直接操作 DB | 引入 Repository 层 |
| DI 容器 | ⚠️ 直接实例化 | 小项目可接受 |
| Widget 不放逻辑 | ✅ | — |
| 导航 | ⚠️ Navigator.push | 生产项目用 go_router |
| 命名约定 | ⚠️ 混用 _page/_screen | 统一为 XxxPage |
| 单元测试 | ❌ 几乎没有 | 为 Notifier 加测试 |
| `flutter_lints` | ❓ 检查 pubspec | 应该引入 |

---

## 参考链接

- [Flutter Architecture Recommendations](https://docs.flutter.dev/app-architecture/recommendations)
- [Flutter App Architecture Guide](https://docs.flutter.dev/app-architecture/guide)
- [State Management Options](https://docs.flutter.dev/data-and-backend/state-mgmt/options)
- [Compass App (官方示例)](https://github.com/flutter/samples/tree/main/compass_app)
- [go_router](https://pub.dev/packages/go_router)
- [flutter_lints](https://pub.dev/packages/flutter_lints)
