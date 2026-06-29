# 番茄钟三套代码对照学习指南

> 同一个番茄钟 App，三套实现方式。逐文件对照看，快速建立「传统 Java → Jetpack MVVM → Flutter」的技术映射。

---

## 一、三套代码在哪

| 版本 | 目录 | 技术栈 | 特点 |
|------|------|--------|------|
| **① 传统 Java** | `legacy-android/` | Java + XML + SQLiteOpenHelper | 零 Jetpack 依赖，纯 Android Framework API |
| **② Jetpack MVVM** | `jetpack-android/` | Kotlin + Compose + Room + Coroutines | 现代安卓架构，对标绿联云 JD 要求 |
| **③ Flutter** | `flutter_module/` | Dart + Riverpod + sqflite | Add-to-App 模块，对标绿联云混合架构 |

---

## 二、文件对照表（核心！）

### 入口

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 应用入口 | `MainActivity.onCreate()` | `MainActivity.onCreate()` → `setContent{}` | `main()` → `runApp()` |
| 加载 UI | `setContentView(R.layout.activity_main)` | `AppRoot()` Composable | `PomodoroApp()` Widget |

### UI 布局

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 垂直排列 | `<LinearLayout vertical>` | `Column {}` | `Column(children: [])` |
| 显示文字 | `<TextView text="25:00"/>` | `Text("25:00")` | `Text("25:00")` |
| 按钮 | `<Button onClick="start"/>` | `Button(onClick = { vm.start() })` | `ElevatedButton(onPressed: () {})` |
| 层叠 | `<FrameLayout>` | `Box {}` | `Stack(children: [])` |
| 圆环绘制 | 自定义 View `onDraw(Canvas)` | `Canvas { drawArc() }` | `CustomPaint(painter:)` |

### 状态管理

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 存状态 | Activity 成员变量 | `TimerUiState` data class | `TimerState` class |
| 改状态 | `remainingSeconds--` | `_uiState.value = state.copy(...)` | `state = state.copyWith(...)` |
| 更新 UI | `tvTime.setText(...)` | StateFlow 自动通知 Compose 重组 | Riverpod 自动通知 Widget 重绘 |
| 订阅状态 | `findViewById` + 手动调 | `collectAsState()` | `ref.watch(provider)` |

### 计时器

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 定时执行 | `Handler.postDelayed(runnable, 1000)` | `launch { while(true) { delay(1000) } }` | `Timer.periodic(Duration(seconds:1))` |
| 停止 | `handler.removeCallbacks(runnable)` | `timerJob?.cancel()` | `_timer?.cancel()` |
| 清理 | `Activity.onDestroy()` | `ViewModel.onCleared()` | `ref.onDispose()` |

### 数据库

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 建表 | `SQLiteOpenHelper.onCreate` 手写 SQL | `@Entity` + `@Database` 注解 | `openDatabase(onCreate: SQL)` |
| 插入 | `ContentValues` + `db.insert()` | `@Insert suspend fun insert()` | `db.insert(table, record.toMap())` |
| 查询 | `Cursor` + `moveToNext` 遍历 | `@Query` + `Flow<List>` | `db.query()` 返回 `List<Map>` |
| 自动更新 | ❌ 手动重新查 | ✅ Flow 自动通知 | ❌ 手动重新查 |

### 列表

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 列表控件 | `ListView` | `LazyColumn` | `ListView.builder` |
| 适配器 | `SimpleAdapter` / `ArrayAdapter` | `items() {}` | `itemBuilder: (ctx, i) {}` |
| 空状态 | `TextView visibility=gone/visible` | `if (records.isEmpty())` | 三元运算符 `? :` |

### 导航

| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 页面跳转 | `Intent` + `startActivity` | `navController.navigate()` / state 切换 | `Navigator.push` / 原生 Activity |
| 返回 | `finish()` | `onBack: () -> Unit` | `Navigator.pop` / `finish()` |

---

## 三、推荐学习路径

### 第 1 步：先读传统 Java 版（你的舒适区）

```
legacy-android/app/src/main/java/com/pomodoro/legacy/
├── model/PomodoroRecord.java       ← 先看这个：数据模型
├── data/DatabaseHelper.java        ← 再看这个：数据库
├── ui/MainActivity.java            ← 重点看这个：计时逻辑 + UI 操作
└── ui/HistoryActivity.java         ← 最后看这个：列表
```

每行代码你都能看懂，建立「这个功能传统怎么实现」的基线。

### 第 2 步：读 Jetpack 版，对照传统 Java

```
jetpack-android/app/src/main/java/com/pomodoro/jetpack/
├── model/PomodoroEntity.kt         ← 对照 PomodoroRecord.java
├── data/PomodoroDao.kt             ← 对照 DatabaseHelper.java
├── data/PomodoroDatabase.kt        ← 对照 SQLiteOpenHelper
├── viewmodel/TimerUiState.kt       ← 新概念：状态封装
├── viewmodel/TimerViewModel.kt     ← 对照 MainActivity.java 的计时逻辑
├── ui/timer/TimerScreen.kt         ← 对照 activity_main.xml + findViewById
└── ui/history/HistoryScreen.kt     ← 对照 HistoryActivity.java
```

每个文件注释都有【传统 Java 思维 → Jetpack 映射】对照表。
重点理解：**为什么 ViewModel 不直接操作 UI？** → 因为状态和 UI 分离，UI 自动订阅状态变化。

### 第 3 步：读 Flutter 版，对照 Jetpack

```
flutter_module/lib/
├── main.dart                        ← 对照 MainActivity.kt onCreate
├── app.dart                         ← 对照 TimerScreen.kt 外壳
├── timer/timer_notifier.dart        ← 对照 TimerViewModel.kt
├── timer/timer_state.dart           ← 对照 TimerUiState.kt
├── timer/timer_widget.dart          ← 对照 TimerScreen.kt
├── channels/method_channels.dart    ← 新概念：跨端通信
├── history/history_db.dart          ← 对照 PomodoroDao.kt
├── history/history_page.dart        ← 对照 HistoryScreen.kt
└── models/pomodoro_record.dart      ← 对照 PomodoroEntity.kt
```

Flutter 和 Jetpack 的概念几乎一一对应：
- `Notifier<TimerState>` = `ViewModel + StateFlow`
- `ref.watch(provider)` = `collectAsState()`
- `Timer.periodic` = `launch { delay() }`

---

## 四、面试核心话术

### 对比三套架构

> "我用同一个番茄钟 App 写了三套实现：
> - 传统 Java 版用 Handler + SQLiteOpenHelper，状态散落在 Activity 里
> - Jetpack 版用 ViewModel + StateFlow + Room，状态和 UI 分离
> - Flutter 版用 Riverpod + sqflite，概念和 Jetpack MVVM 几乎一样
>
> 通过对比我发现：**Flutter 的 Riverpod Notifier 就是 Android 的 ViewModel**，
> `ref.watch()` 就是 `collectAsState()`，`copyWith()` 就是 `data class copy()`。
> 三套架构的核心思想是一样的：状态管理 + 声明式 UI + 数据层分离。"

### 对标绿联云

> "绿联云用原生 + Flutter 混合架构，Flutter 模块通过 MethodChannel 调原生能力。
> 我的项目里 Flutter 模块负责计时 UI，原生壳负责导航和数据持久化，
> 正是这个架构的缩影。同时我还写了 Jetpack 版理解原生 MVVM，
> 写了传统 Java版理解架构演进，确保对每一层都有深度理解。"

---

## 五、每套代码怎么跑

### ① 传统 Java 版
```bash
# 用 AS 打开 legacy-android/ 目录
# New Project → Import → 选 legacy-android/
# Run → 选模拟器
```

### ② Jetpack 版
```bash
# 用 AS 打开 jetpack-android/ 目录
# Sync Gradle → Run → 选模拟器
```

### ③ Flutter 版
```bash
# 方式 1: AS 打开 native-android/ Run App，然后 cd flutter_module && flutter attach
# 方式 2: cd flutter_module && flutter run -d emulator-5554 test_main.dart（独立跑）
```
