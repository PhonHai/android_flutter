---
name: "flutter-best-practices"
description: "Flutter 架构最佳实践，基于 Flutter 官方 Architecture Recommendations。改 Dart/Widget/Riverpod/sqflite/MethodChannel 代码时自动加载。强制 UDF、不可变数据、分层架构。"
---

# Flutter 架构最佳实践

> **来源**：[Flutter 官方 Architecture Recommendations](https://docs.flutter.dev/app-architecture/recommendations)
>
> **适用**：`flutter_module/`、以及 `native-android` 中与 Flutter 交互的部分。

**ROLE**：Flutter 官方架构最佳实践——分层架构、UDF、不可变数据、MVVM。

**WHEN**：写/改 `.dart` 文件（Widget / ViewModel / Repository / Model）时自动加载。

**CORE RULES**：
- UI 层只管显示，不放业务逻辑
- 数据只向下流（UDF），事件只向上传
- 数据模型不可变（`final` 字段 + `copyWith`）
- Repository 模式隔离数据源
- 依赖注入（Provider / Riverpod）不全局访问

---

## 1. 关注点分离

```
┌─────────────────────────────────────────────────┐
│                   UI 层                          │
│  ┌──────────────┐    ┌────────────────────┐      │
│  │   Widget      │ ←→ │   ViewModel        │      │
│  │  (只看不写)    │    │  (Riverpod Notifier)│      │
│  └──────────────┘    └────────┬───────────┘      │
├─────────────────────────────────┼────────────────┤
│                  数据层          │                │
│  ┌──────────────┐    ┌──────────▼───────────┐   │
│  │  Repository   │ ←→ │   Service (API/DB)    │   │
│  └──────────────┘    └──────────────────────┘   │
└─────────────────────────────────────────────────┘
```

### 铁的纪律
- **Widget 里不放业务逻辑**
- **Repository 是数据的唯一来源**
- **数据只从数据层流向 UI 层**

---

## 2. 不可变数据模型（MUST）

```dart
// ✅ 所有字段 final + copyWith
class TimerState {
  final int remainingSeconds;
  final bool isRunning;
  final int sessions;

  const TimerState({
    this.remainingSeconds = 1500,
    this.isRunning = false,
    this.sessions = 0,
  });

  TimerState copyWith({int? remainingSeconds, bool? isRunning, int? sessions}) {
    return TimerState(
      remainingSeconds: remainingSeconds ?? this.remainingSeconds,
      isRunning: isRunning ?? this.isRunning,
      sessions: sessions ?? this.sessions,
    );
  }
}
```

```dart
// ❌ 禁止
class BadState {
  int remainingSeconds = 1500;  // 不是 final，外部可改
}
```

---

## 3. Repository 模式

```dart
// 抽象接口
abstract class PomodoroRepository {
  Future<void> saveRecord(PomodoroRecord record);
  Future<List<PomodoroRecord>> getRecords();
}

// 具体实现
class SqlitePomodoroRepository implements PomodoroRepository {
  final HistoryDb _db;
  SqlitePomodoroRepository(this._db);
  // ...
}
```

---

## 4. 单向数据流（UDF）— Riverpod 版

```dart
// ✅ TimerNotifier 管理状态，Widget 只读
class TimerNotifier extends Notifier<TimerState> {
  @override
  TimerState build() => const TimerState();

  void toggle() {
    state = state.copyWith(isRunning: !state.isRunning);
  }
}

class TimerWidget extends ConsumerWidget {
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final timerState = ref.watch(timerNotifierProvider);
    return Text('${timerState.remainingSeconds}');
  }
}
```

---

## 5. Widget 层规范

### ✅ Widget 里只能有什么
- `if` 判断显隐
- 简单路由
- 动画

### ❌ Widget 里不能有什么
- 业务逻辑（数据库操作、网络请求）
- 复杂计算

---

## 6. 依赖注入（DI）

```dart
// ✅ Provider / Riverpod
final pomodoroRepositoryProvider = Provider<PomodoroRepository>((ref) {
  return SqlitePomodoroRepository(HistoryDb());
});

// ❌ 禁止全局单例
final db = HistoryDb.instance;
```

---

## 7. 导航规范

Flutter 官方推荐：[go_router](https://pub.dev/packages/go_router)。当前项目用 `Navigator.push`，小项目可接受。

---

## 8. 命名约定

| 组件 | 命名 | 示例 |
|------|------|------|
| ViewModel | `XxxViewModel` / `XxxNotifier` | `TimerNotifier` |
| Widget（页面） | `XxxPage` / `XxxScreen` | `HomePage` |
| Repository | `XxxRepository` | `PomodoroRepository` |
| Service | `XxxService` | `HistoryApiService` |
| Model | 名词 | `PomodoroRecord` |

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

必须测：Repository（单元测试）、ViewModel/Notifier（单元测试）、Widget（Widget Test）。

---

## 参考链接

- [Flutter Architecture Recommendations](https://docs.flutter.dev/app-architecture/recommendations)
- [Flutter App Architecture Guide](https://docs.flutter.dev/app-architecture/guide)
- [State Management Options](https://docs.flutter.dev/data-and-backend/state-mgmt/options)
- [Compass App (官方示例)](https://github.com/flutter/samples/tree/main/compass_app)
- [go_router](https://pub.dev/packages/go_router)