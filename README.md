# 番茄钟多实现对照学习项目

> 同一个需求用多套技术栈实现，对照学习 Android / Flutter 的概念映射，对标某 IoT 公司（NAS 私有云客户端）的技术栈。

---

## ⚠️ 重要：三个工程已经分化

最初三套代码（legacy / jetpack / flutter）功能是一致的。但学习过程中**先重点打磨了 jetpack 版**，给它加了 NAS 文件管理、传输、网络层、Hilt 等模块，导致它和另外两个工程**已经不一样了**。

所以**不要再沿用"三套一模一样"的旧认知**。当前真实状态：

| 工程 | 目录 | 技术栈 | 当前范围（真实） |
|------|------|--------|------------------|
| ① 传统 Java（基线） | `legacy-android/` | Java + XML + Fragment + SQLite | 番茄钟 + 统计/历史 + 4 级导航演示 + 设置/关于。**最贴近原型，无文件/传输/网络** |
| ② Jetpack MVVM（最完整） | `jetpack-android/` | Kotlin + Compose + Room + Hilt + 网络层 | 番茄钟 + **文件 + 传输 + 设置(DataStore)** + 4 级导航演示。**独有文件/传输模块与网络层** |
| ③ Flutter（跨端模块） | `flutter_module/` | Dart + Riverpod + sqflite | 番茄钟 + 历史 + 4 级导航演示 + MethodChannel。**无文件/传输** |
| ④ 原生壳（Flutter 子项目接入） | `native-android/` | Kotlin + Compose + Hilt + `project(":flutter")` | 经 Add-to-App 把 Flutter 作为 Gradle 子项目接入的原生 App，含 MethodChannel 通信 |

**各工程的详细、准确地图请直接看对应目录的 README：**
- [`legacy-android/README.md`](legacy-android/README.md)
- [`jetpack-android/README.md`](jetpack-android/README.md)
- [`flutter_module/README.md`](flutter_module/README.md)
- [`native-android/README.md`](native-android/README.md)

---

## 一、三套实现的概念对照（范式层面，不是逐文件清单）

> 下面这些表是**三种技术范式的概念对照**。因为三个工程已分化，具体的文件名/类结构要以各工程 README 为准（例如计时状态在 jetpack 里是 `TimerViewModel`，在 legacy 里是 `TimerFragment` 的成员变量，在 flutter 里是 `TimerNotifier`）。

### 入口
| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 应用入口 | `MainActivity.onCreate()` | `MainActivity.onCreate()` → `setContent{}` | `main()` → `runApp()` |
| 加载 UI | `setContentView(R.layout.xxx)` | `AppRoot()` Composable | `PomodoroApp()` Widget |

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
| 存状态 | Fragment 成员变量 | `TimerUiState` data class | `TimerState` class |
| 改状态 | `remainingSeconds--` | `_uiState.value = state.copy(...)` | `state = state.copyWith(...)` |
| 更新 UI | `tvTime.setText(...)` | StateFlow 自动通知 Compose 重组 | Riverpod 自动通知 Widget 重绘 |
| 订阅状态 | `findViewById` + 手动调 | `collectAsState()` | `ref.watch(provider)` |

### 计时器
| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 定时执行 | `Handler.postDelayed(runnable, 1000)` | `launch { while(true) { delay(1000) } }` | `Timer.periodic(Duration(seconds:1))` |
| 停止 | `handler.removeCallbacks(runnable)` | `timerJob?.cancel()` | `_timer?.cancel()` |
| 清理 | `Fragment.onDestroy()` | `ViewModel.onCleared()` | `ref.onDispose()` |

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
| 列表控件 | `ListView` / `RecyclerView` | `LazyColumn` | `ListView.builder` |
| 适配器 | `SimpleAdapter` / `ArrayAdapter` | `items() {}` | `itemBuilder: (ctx, i) {}` |
| 空状态 | `TextView visibility=gone/visible` | `if (records.isEmpty())` | 三元运算符 `? :` |

### 导航
| 功能 | ① 传统 Java | ② Jetpack | ③ Flutter |
|------|------------|-----------|-----------|
| 页面跳转 | `Intent` + `startActivity` / NavController | `navController.navigate()` | `Navigator.push` / 原生 Activity |
| 返回 | `finish()` / 返回栈 pop | `onBack: () -> Unit` | `Navigator.pop` / `finish()` |

---

## 二、推荐学习路径（按分化后的现实）

### 第 1 步：先读 legacy（你的舒适区，最贴近原型）
打开 [`legacy-android/README.md`](legacy-android/README.md)，按里面的文件树读：
`model/PomodoroRecord.java` → `data/DatabaseHelper.java` → `ui/TimerFragment.java` → 其余 Fragment。
每行代码你都能看懂，建立「这个功能传统怎么实现」的基线。

### 第 2 步：读 jetpack，对照传统 Java
打开 [`jetpack-android/README.md`](jetpack-android/README.md)。注意它比 legacy **多了文件/传输/网络层/Hilt**，
重点理解：**为什么 ViewModel 不直接操作 UI？** —— 状态和 UI 分离，UI 自动订阅状态变化。
独有模块（`FileRepository` / `NasApiService` / `TransferListScreen` / Hilt）是面试讲"现代架构 + 网络层"的加分项。

### 第 3 步：读 flutter，对照 jetpack
打开 [`flutter_module/README.md`](flutter_module/README.md)。Flutter 和 Jetpack 概念几乎一一对应：
- `Notifier<TimerState>` = `ViewModel + StateFlow`
- `ref.watch(provider)` = `collectAsState()`
- `Timer.periodic` = `launch { delay() }`

### 第 4 步：读 native-android，理解混合架构
打开 [`native-android/README.md`](native-android/README.md)。看原生壳如何用 `FlutterContainerActivity` + `MethodChannelHandler` 承载 Flutter 模块。

---

## 三、面试核心话术

### 对比三套架构
> "我用同一个需求写了多套实现：
> - 传统 Java 版用 Handler + SQLiteOpenHelper，状态散落在 Fragment 里
> - Jetpack 版用 ViewModel + StateFlow + Room + Hilt，状态和 UI 分离，还扩展了 NAS 文件管理 / 传输 / 网络层
> - Flutter 版用 Riverpod + sqflite，概念和 Jetpack MVVM 几乎一样
>
> 通过对比我发现：**Flutter 的 Riverpod Notifier 就是 Android 的 ViewModel**，
> `ref.watch()` 就是 `collectAsState()`，`copyWith()` 就是 `data class copy()`。
> 三套架构的核心思想是一样的：状态管理 + 声明式 UI + 数据层分离。"

### 对标绿联云 / 目标公司
> "目标公司用原生 + Flutter 混合架构，Flutter 模块通过 MethodChannel 调原生能力。
> 我的 `native-android` 就是原生壳，负责导航、文件、传输；`flutter_module` 是被嵌入的跨端模块。
> 同时我还写了纯 Compose 的 `jetpack-android` 理解原生 MVVM，写了 `legacy-android` 理解架构演进，
> 确保对每一层都有深度理解。"

---

## 四、每套代码怎么跑

### ① 传统 Java 版
```bash
# 用 AS 打开 legacy-android/ → Sync Gradle → Run → 选模拟器
```

### ② Jetpack 版
```bash
# 用 AS 打开 jetpack-android/ → Sync Gradle → Run → 选模拟器
```

### ③ Flutter 版
```bash
# 方式 1（作为模块）: 先把 flutter_module 编译为 aar/jar，由 native-android 依赖；在 native-android 里 Run App 后 cd flutter_module && flutter attach
# 方式 2（独立跑）:    cd flutter_module && flutter run
```

### ④ 原生壳（Flutter 子项目接入）
```bash
# 用 AS 打开 native-android/（settings.gradle 已通过 include_flutter.groovy 挂上 :flutter 子项目）→ Sync Gradle → Run → 选模拟器
# 调试 Flutter 侧：cd flutter_module && flutter attach
```

---

## 五、配套资料
顶层目录的 md/pdf 是面试知识点汇总（Jetpack / Kotlin / 高频题 / 源码解析），与代码工程分开存放。
