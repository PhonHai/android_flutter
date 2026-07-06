# PomodoroNative — 项目代码地图

> **最后更新**: 2026-07-06 · git commit: 5fe7667 · 三套对照学习 + 4 级导航 + 首页 Tab 改造 + Hilt+Retrofit+DataStore 架构升级 + 10 Skills 体系
>
> 项目类型: **三套独立 Android App（对照学习）+ Flutter 模块 (Add-to-App)**
>
> 技术栈: 
> - `legacy-android/`: Java + XML + SQLiteOpenHelper + Navigation Component
> - `jetpack-android/`: Kotlin + Jetpack Compose + MVVM + Room + Hilt + Retrofit + DataStore + Navigation Compose
> - `native-android/`: Kotlin + Jetpack Compose + Flutter AAR (Add-to-App) + Navigation Compose
> - `flutter_module/`: Dart + Flutter + Riverpod + sqflite

---

<a id="sec-meta"></a>
## 1. 元信息

| 属性 | 值 |
|------|-----|
| 项目名 | PomodoroNative（三套对照学习） |
| 子项目 | `legacy-android/` / `jetpack-android/` / `native-android/` / `flutter_module/` |
| 构建工具 | 各项目独立 Gradle wrapper |
| compileSdk | 36 |
| minSdk | 24 |
| applicationId | `com.pomodoro.legacy` / `com.pomodoro.jetpack` / `com.pomodoro.app` |

---

<a id="sec-dir"></a>
## 2. 目录结构

```
.
├── legacy-android/                  # 传统 Java + XML（无 Jetpack）
│   ├── app/src/main/java/com/pomodoro/legacy/
│   │   ├── data/DatabaseHelper.java
│   │   ├── model/PomodoroRecord.java
│   │   └── ui/
│   │       ├── MainActivity.java             # AppCompatActivity + NavHostFragment
│   │       ├── TabContainerFragment.java      # 首页 4 Tab 容器
│   │       ├── TimerFragment.java             # Tab1: 番茄钟（Handler 计时）
│   │       ├── home/HomeFragment.java         # 第 2 级：导航演示首页
│   │       ├── list/ListFragment.java         # 第 3 级：列表页
│   │       ├── detail/DetailFragment.java     # 第 4 级：详情页
│   │       ├── comment/CommentFragment.java   # 第 5 级：总结页
│   │       ├── stats/StatsFragment.java       # Tab2: 统计（占位）
│   │       ├── settings/SettingsFragment.java # Tab3: 设置（占位）
│   │       └── about/AboutFragment.java       # Tab4: 关于（占位）
│   └── app/src/main/res/
│       ├── layout/ (fragment_*.xml)           # 10 个 XML 布局
│       └── navigation/nav_graph.xml           # Navigation Component 导航图
│
├── jetpack-android/                 # Kotlin + Compose + MVVM + Hilt + Retrofit（自含计时器）
│   └── app/src/main/java/com/pomodoro/jetpack/
│       ├── PomodoroApplication.kt      # @HiltAndroidApp 入口
│       ├── data/
│       │   ├── PomodoroDao.kt           # Room DAO
│       │   ├── PomodoroDatabase.kt      # Room Database
│       │   ├── SettingsDataStore.kt     # DataStore 键值存储（替代 SP）
│       │   └── repository/
│       │       ├── TimerRepository.kt   # 番茄钟记录 Repository
│       │       └── FileRepository.kt    # NAS 文件 Repository
│       ├── di/
│       │   └── AppModule.kt            # Hilt DI 模块（OkHttp/Retrofit/Room/DAO）
│       ├── model/PomodoroEntity.kt      # Room @Entity
│       ├── network/
│       │   ├── NasApiService.kt         # Retrofit API 接口
│       │   ├── NasFileResponse.kt       # 网络响应模型
│       │   ├── OkHttpClientProvider.kt  # OkHttp 配置 + Mock 拦截器
│       │   ├── MockNasInterceptor.kt    # NAS Mock 数据拦截器
│       │   └── Result.kt                # sealed class 网络请求封装
│       ├── viewmodel/
│       │   ├── TimerViewModel.kt        # @HiltViewModel + Repository + StateFlow
│       │   ├── TimerUiState.kt          # data class 不可变状态
│       │   ├── HistoryViewModel.kt      # 历史记录 ViewModel（Flow + stateIn）
│       │   ├── FileViewModel.kt         # NAS 文件列表 ViewModel
│       │   ├── SettingsViewModel.kt     # 设置 ViewModel（DataStore）
│       │   └── TransferViewModel.kt     # 传输列表 ViewModel（Flow 进度）
│       └── ui/
│           ├── MainActivity.kt          # @AndroidEntryPoint + setContent { AppNavHost() }
│           ├── AppNavHost.kt            # Navigation Compose 路由表
│           ├── main/MainTabScreen.kt    # 首页 4 Tab (番茄钟/文件/设置/传输)
│           ├── main/FileListScreen.kt   # Tab2: NAS 文件列表
│           ├── timer/TimerScreen.kt     # Tab1: 番茄钟 (Canvas 环形 + Coroutines delay)
│           ├── settings/SettingsScreen.kt # Tab3: 设置（DataStore 读写）
│           ├── transfer/TransferListScreen.kt # Tab4: 传输列表（Flow 进度）
│           ├── home/HomeScreen.kt       # 第 2 级：导航演示首页
│           ├── list/ListScreen.kt       # 第 3 级：列表页
│           ├── detail/DetailScreen.kt   # 第 4 级：详情页
│           ├── comment/CommentScreen.kt # 第 5 级：总结页
│           └── history/HistoryScreen.kt # 历史记录页（单独入口）
│
├── native-android/                  # Flutter Add-to-App 壳（混合架构）
│   └── app/src/main/java/com/pomodoro/app/
│       ├── MainActivity.kt              # LAUNCHER Activity，首页 4 Tab
│       ├── PomodoroApplication.kt       # Application + FlutterEngine 预热+缓存
│       ├── FlutterContainerActivity.kt  # FlutterFragment 容器（加载 Flutter 模块）
│       ├── PomodoroListActivity.kt      # 历史列表（原生 Compose）
│       ├── MethodChannelHandler.kt      # MethodChannel 原生侧处理
│       └── ui/
│           ├── AppNavHost.kt            # Navigation Compose 路由表
│           ├── main/MainTabScreen.kt    # 首页 4 Tab 容器
│           ├── home/HomeScreen.kt       # 第 2 级：导航演示首页
│           ├── list/ListScreen.kt       # 第 3 级：列表页
│           ├── detail/DetailScreen.kt   # 第 4 级：详情页
│           └── comment/CommentScreen.kt # 第 5 级：总结页
│
└── flutter_module/                  # Flutter 计时器模块（AAR 打包）
    └── lib/
        ├── main.dart                     # ProviderScope → PomodoroApp
        ├── app.dart                      # MaterialApp + 路由
        ├── timer/
        │   ├── timer_notifier.dart       # Riverpod Notifier
        │   ├── timer_state.dart          # TimerState (copyWith)
        │   └── timer_widget.dart         # ConsumerWidget (CustomPaint 环形)
        ├── channels/method_channels.dart # MethodChannel (com.pomodoro/history)
        ├── history/
        │   ├── history_db.dart           # sqflite DAO
        │   └── history_page.dart         # 历史页
        ├── models/pomodoro_record.dart   # 记录实体
        └── navigations/                  # 4 级导航（Flutter 侧演示）
            ├── home/home_page.dart
            ├── list/list_page.dart
            ├── detail/detail_page.dart
            └── comment/comment_page.dart
│
├── .trae/skills/                    # TRAE 原生 Skills 工作流（10 个 Skill）
│   ├── project-memory/              # 项目记忆（explore/sync）+ scripts/
│   ├── code-locate/                 # 代码定位 + references/
│   ├── arch-rules/                  # 架构约束 + conventions/（universal/mobile/flutter-hybrid）
│   ├── confirm-first/               # 改前确认 + references/
│   ├── lazy-build/                  # 懒人阶梯（lite/full/ultra）
│   ├── coding-rules/                # 代码纪律 + references/
│   ├── verify-loop/                 # 闭环验证 + references/
│   ├── git-commit/                  # Conventional Commits 规范提交
│   ├── jetpack-compose-mvvm/        # Compose + MVVM 最佳实践
│   └── flutter-best-practices/      # Flutter 架构最佳实践
└── .skills/                         # 原 workbuddy Skills 目录（保留兼容）
```

---

<a id="sec-entry"></a>
## 3. 核心入口

### legacy-android（传统 Java）
| 入口 | 文件 | 说明 |
|------|------|------|
| `MainActivity` | `legacy-android/.../MainActivity.java` | AppCompatActivity，装载 NavHostFragment |
| startDestination | `nav_graph.xml` | `tabContainerFragment`（底部 4 Tab 首页） |
| 计时器 | `TimerFragment.java` | `Handler + Runnable.postDelayed()` 每秒 tick |
| Tab 管理器 | `TabContainerFragment.java` | `ChildFragmentManager` + `hide/show` |
| 导航方案 | `nav_graph.xml` | `Navigation Component XML 版` |

### jetpack-android（现代 MVVM + Hilt + Retrofit）
| 入口 | 文件 | 说明 |
|------|------|------|
| `PomodoroApplication` | `PomodoroApplication.kt` | `@HiltAndroidApp`，创建 Application 级 DI 容器 |
| `MainActivity` | `jetpack-android/.../MainActivity.kt` | `@AndroidEntryPoint`，`setContent { AppNavHost() }` |
| startDestination | `AppNavHost.kt` | `"main"`（底部 4 Tab 首页） |
| 计时器 | `TimerViewModel.kt` | `@HiltViewModel` + `TimerRepository` + `StateFlow` |
| 文件列表 | `FileViewModel.kt` | `@HiltViewModel` + `FileRepository` → Retrofit → Mock NAS |
| 设置 | `SettingsViewModel.kt` | `@HiltViewModel` + `SettingsDataStore`（DataStore Flow） |
| 传输列表 | `TransferViewModel.kt` | Flow 模拟上传/下载进度 |
| Tab 管理器 | `MainTabScreen.kt` | `mutableStateOf` + `when` 分支切换 |
| 导航方案 | `AppNavHost.kt` | `Navigation Compose 代码版` |
| DI 模块 | `di/AppModule.kt` | `@Provides` OkHttp/Retrofit/NasApiService/Room/DAO |
| 网络层 | `network/` | Retrofit + OkHttp + MockNasInterceptor + sealed Result |

### native-android（Flutter Add-to-App 混合）
| 入口 | 文件 | 说明 |
|------|------|------|
| `MainActivity` | `native-android/.../MainActivity.kt` | **LAUNCHER**，ComponentActivity，首页 4 Tab |
| `PomodoroApplication` | `native-android/.../PomodoroApplication.kt` | `FlutterEngine` 预热 → 缓存 `"pomodoro_engine"` |
| 计时器 | **Flutter 模块** | 通过 `FlutterContainerActivity` 加载 AAR 里的 Flutter 计时器 |
| Tab 管理器 | `MainTabScreen.kt` | `mutableIntStateOf` + `when`，Tab1 跳 Flutter |
| 导航方案 | `AppNavHost.kt` | `Navigation Compose 代码版`（同 jetpack） |
| 混合通信 | `MethodChannelHandler.kt` | Flutter ↔ Native (com.pomodoro/history) |

### flutter_module（Flutter 模块参考）
| 入口 | 文件 | 说明 |
|------|------|------|
| `main()` | `flutter_module/lib/main.dart` | `ProviderScope → PomodoroApp` |
| 计时器 | `timer/timer_notifier.dart` | `Riverpod Notifier` + `Timer.periodic` |
| 部署方式 | — | 编译为 AAR，嵌入 `native-android` |

---

<a id="sec-flow"></a>
## 4. 核心业务流程

### 4.1 legacy-android 路由链路

```
App 启动 → NavHostFragment (startDestination: tabContainerFragment)
  └── TabContainerFragment
        ├── [Tab 1] TimerFragment 番茄钟（Handler 倒计时）
        │     │  点击「进入 4 级导航演示」
        │     │  → navigate(R.id.homeFragment)  ← 全屏替换 Tab 容器
        │     │
        │     └──→ HomeFragment（第 2 级）
        │           → action_home_to_list → ListFragment（第 3 级）
        │             → action_list_to_detail → DetailFragment（第 4 级，传 Bundle(itemId)）
        │               → action_detail_to_comment → CommentFragment（第 5 级）
        │                   ├── 返回详情页 → popBackStack()
        │                   └── 回到首页 → popBackStack(R.id.tabContainerFragment, false)
        ├── [Tab 2] StatsFragment（占位）
        ├── [Tab 3] SettingsFragment（占位）
        └── [Tab 4] AboutFragment（占位）
```

### 4.2 jetpack-android 路由链路

```
App 启动 → PomodoroApplication (@HiltAndroidApp DI 容器初始化)
  → MainActivity (@AndroidEntryPoint)
    └── AppNavHost (startDestination: "main")
          └── MainTabScreen (hiltViewModel 注入 TimerViewModel)
                ├── [Tab 1] TimerScreen 番茄钟（Canvas 环形进度+Coroutines delay）
                │     │  点击「进入 4 级导航演示」
                │     │  → navigate("home")  ← 全屏替换 MainTabScreen
                │     │
                │     └──→ HomeScreen（第 2 级）
                │           → "list" → ListScreen（第 3 级）
                │             → "detail/{itemId}" → DetailScreen（第 4 级）
                │               → "comment/{itemId}" → CommentScreen（第 5 级）
                │                   ├── 返回详情页 → popBackStack()
                │                   └── 回到首页 → popBackStack("main", false)
                ├── [Tab 2] FileListScreen（NAS 文件列表，Retrofit+Repository+sealed Result）
                ├── [Tab 3] SettingsScreen（DataStore 设置页，Flow 自动更新）
                └── [Tab 4] TransferListScreen（传输列表，Flow 进度模拟）
```

### 4.3 native-android 路由链路

```
App 启动 → Application.onCreate() 预热 FlutterEngine → MainActivity
  └── AppNavHost (startDestination: "main")
        └── MainTabScreen
              ├── [Tab 1] FlutterTabContent（按钮+架构说明）
              │     ├── 「启动 Flutter 番茄钟」→ startActivity(FlutterContainerActivity)
              │     │     → FlutterFragment.withCachedEngine("pomodoro_engine")
              │     │       → Flutter 模块渲染 TimerWidget（Riverpod 倒计时）
              │     │       → 完成后 MethodChannel 回传记录
              │     │       → finish() 返回 Tab 容器
              │     │
              │     └── 「进入 4 级导航演示」→ navigate("home")  ← 全屏替换
              │           → HomeScreen（第 2 级）
              │             → "list" → ListScreen（第 3 级）
              │               → "detail/{itemId}" → DetailScreen（第 4 级）
              │                 → "comment/{itemId}" → CommentScreen（第 5 级）
              │                     ├── 返回详情页 → popBackStack()
              │                     └── 回到首页 → popBackStack("main", false)
              ├── [Tab 2] PlaceholderContent（占位）
              ├── [Tab 3] PlaceholderContent（占位）
              └── [Tab 4] PlaceholderContent（占位）
```

### 4.4 关键区别：Tab 1 番茄钟的实现

```
legacy-android → TimerFragment ← 自含 Java Handler 倒计时
jetpack-android → TimerScreen   ← 自含 Kotlin Coroutines 倒计时
native-android  → Flutter 模块    ← 启动独立 Activity 加载 Flutter AAR
                  （AAR 由 flutter_module/ 编译产出）
```

native-android 因计时器实现在 Flutter 模块中（AAR），无法像其他两套那样把计时器视图直接嵌入 Tab 1。采用的方式是：Tab 1 → 按钮 → `startActivity(FlutterContainerActivity)` → `FlutterFragment` 渲染 Flutter UI。

---

<a id="sec-tab-arch"></a>
## 5. 首页 Tab 架构对比

### legacy-android（FragmentTransaction 手动管理）

```
MainActivity
  └── NavHostFragment (startDestination: tabContainerFragment)
        └── TabContainerFragment
              ├── ChildFragmentManager
              │     ├── TimerFragment ────── show (default)
              │     ├── StatsFragment ────── hide
              │     ├── SettingsFragment ─── hide
              │     └── AboutFragment ────── hide
              └── 底部 LinearLayout (4 个 TextView)
```

- 切换策略：`FragmentTransaction.hide/show`，不重建
- 生命周期：ChildFragmentManager 隔离于 NavController
- 导航关系：四级导航时 NavController 全屏替换 TabContainerFragment

### jetpack-android（Compose + Hilt + mutableStateOf）

```
PomodoroApplication (@HiltAndroidApp)
  └── MainActivity (@AndroidEntryPoint)
        └── AppNavHost (startDestination: "main")
              └── MainTabScreen
                    ├── var selectedTab by remember { mutableStateOf(0) }
                    ├── Scaffold(bottomBar = NavigationBar { ... })
                    └── when(selectedTab) {
                          0 → TimerScreen (hiltViewModel, Canvas + Coroutines)
                          1 → FileListScreen (hiltViewModel, Retrofit + sealed Result)
                          2 → SettingsScreen (hiltViewModel, DataStore Flow)
                          3 → TransferListScreen (hiltViewModel, Flow 进度)
                        }
```

### native-android（Compose + Flutter AAR）

```
MainActivity
  └── AppNavHost (startDestination: "main")
        └── MainTabScreen
              ├── var selectedTab by remember { mutableIntStateOf(0) }
              ├── Scaffold(bottomBar = NavigationBar { ... })
              └── when(selectedTab) {
                    0 → FlutterTabContent(按钮 → FlutterContainerActivity)
                    1 → PlaceholderContent
                    2 → PlaceholderContent
                    3 → PlaceholderContent
                  }
```

---

<a id="sec-state"></a>
## 6. 状态管理与计时器实现对照

| 要素 | legacy-android | jetpack-android | native-android | flutter_module |
|------|---------------|-----------------|----------------|----------------|
| 语言 | Java | Kotlin | Kotlin | Dart |
| UI 层 | XML + findViewByID | Compose @Composable | Compose + Flutter Fragment | Widget tree |
| 状态持有 | TimerFragment 成员变量（mutable） | `@HiltViewModel` + `StateFlow<TimerUiState>` | Flutter 模块内部（不在原生侧） | `TimerNotifier` + `TimerState` |
| 依赖注入 | 无（手动 new） | **Hilt**（`@HiltAndroidApp` + `@HiltViewModel` + `AppModule`） | 无 | Riverpod `ProviderScope` |
| 不可变性 | 直接修改成员 | `data class.copy()` | Flutter 端 `copyWith()` | `copyWith()` |
| UI 更新 | `tvTime.setText()` 手动 | Compose 自动重组 | Flutter Widget 自动重建 | Widget 自动重建 |
| 倒计时 | `Handler.postDelayed` | `Coroutines delay` | `Timer.periodic`（在 Flutter 内） | `Timer.periodic` |
| 停止回收 | `handler.removeCallbacks()` | `coroutineScope.cancel()` | `_timer?.cancel()`（Flutter 内） | `_timer?.cancel()` |

---

<a id="sec-data"></a>
## 7. 数据层对照

| 要素 | legacy-android | jetpack-android | native-android | flutter_module |
|------|---------------|-----------------|----------------|----------------|
| 数据库 | SQLiteOpenHelper | Room `@Database` + `@Dao` | sqflite（Flutter 侧） | sqflite `openDatabase()` |
| 实体 | POJO | `@Entity` data class | `PomodoroRecord` class（Dart） | Dart class |
| 查询 | `db.rawQuery()` | `@Query("SELECT * FROM ...")` | `db.query()`（Flutter 内） | `db.query()` |
| 异步 | 不推荐主线程 | `Flow<List<...>>` 自动更新 | `Future<List<Map>>` | `Future<List<Map>>` |
| 原生侧存储 | 无（全部 Java SQLite） | Room 原生 | MethodChannel 占位 | sqflite 在 Flutter 侧 |
| 键值存储 | `SharedPreferences` | **DataStore**（Flow 读 + suspend 写） | 无 | 无 |
| 网络层 | 无 | **Retrofit + OkHttp + Mock 拦截器** | 无 | 无 |
| Repository | 无 | **TimerRepository / FileRepository**（Hilt @Singleton） | 无 | 无 |
| 请求封装 | 无 | **sealed class Result**（Success/Error/Loading） | 无 | 无 |

---

<a id="sec-nav-compare"></a>
## 8. 导航方案对照

### 传参方式
| 方案 | 传参方式 | 特点 |
|------|---------|------|
| 传统 Java Fragment | `Bundle(args).putString("itemId", id)` | 运行时取，无类型检查 |
| Compose NavHost | `navController.navigate("detail/$itemId")` | 字符串路由，运行时解析 |
| Flutter Navigator | `DetailPage(itemId: itemId)` | 构造函数传参，编译期类型检查 |
| Intent（native） | `Intent.putExtra("key", value)` | Activity 间传参，标准模式 |

### 返回栈管理
| 操作 | legacy-android | jetpack/native-android | flutter_module |
|------|---------------|----------------------|----------------|
| 返回上一页 | `navController.popBackStack()` | `navController.popBackStack()` | `Navigator.pop(context)` |
| 返回到指定页 | `popBackStack(R.id.xxx, false)` | `popBackStack("route", false)` | `Navigator.popUntil()` |
| 系统返回键 | `defaultNavHost="true"` 自动 | `BackHandler { }` 手动处理 | `WillPopScope` |

### 四级导航路由对照
| 等级 | legacy-android | jetpack/native-android |
|------|---------------|----------------------|
| Tab 容器 | `tabContainerFragment` (R.id) | `"main"` (字符串) |
| 第 2 级 | `homeFragment` | `"home"` |
| 第 3 级 | `listFragment` | `"list"` |
| 第 4 级 | `detailFragment/{itemId}` | `"detail/{itemId}"` |
| 第 5 级 | `commentFragment/{itemId}` | `"comment/{itemId}"` |
| 回到首页 | `popBackStack(R.id.tabContainerFragment, false)` | `popBackStack("main", false)` |

---

<a id="sec-hybrid"></a>
## 9. 混合架构专题（native-android + flutter_module）

### 架构分层
```
┌─────────────────────────────────────────────┐
│               native-android                  │
│  ┌──────────────────────────────────────┐    │
│  │  MainActivity (Compose 首页 4 Tab)    │    │
│  │  └── AppNavHost (原生导航)             │    │
│  ├──────────────────────────────────────┤    │
│  │  FlutterContainerActivity            │    │
│  │  └── FlutterFragment                 │    │
│  │        └── Flutter Module (AAR)      │    │
│  │              ├── TimerWidget         │    │
│  │              └── MethodChannel  ──┐  │    │
│  ├──────────────────────────────────────┤    │
│  │  PomodoroApplication                 │    │
│  │  └── FlutterEngineCache              │    │
│  │        └── "pomodoro_engine"         │    │
│  ├──────────────────────────────────────┤    │
│  │  PomodoroListActivity (原生列表)      │    │
│  └──────────────────────────────────────┘    │
├─────────────────────────────────────────────┤
│  MethodChannel: com.pomodoro/history         │
│  saveRecord(startTime, durationMinutes, endTime) → bool │
│  getRecords() → List<Map>                    │
└─────────────────────────────────────────────┘
```

### 关键参数与原因
| 参数 | 值 | 原因 |
|------|-----|------|
| `shouldAttachEngineToActivity` | `false` | 引擎生命周期不依附于 Activity，切页面不重建 |
| `withCachedEngine("pomodoro_engine")` | 应用级单例 | Application.onCreate 时预热，打开 Flutter 秒开 |
| `FlutterFragment` vs `FlutterActivity` | `FlutterFragment` | 可嵌套在原生 UI 中，更灵活 |

### 三套应用本质差异
| 对比维度 | legacy / jetpack | native |
|---------|-----------------|--------|
| 计时器代码在哪里 | 同一个 App 进程内 | 在 Flutter 模块 AAR 里 |
| Tab 1 能否直接显示计时器 | ✅ 可以 | ❌ Flutter 渲染需要独立 Fragment |
| 多了一个 Activity | 无 | `FlutterContainerActivity` |
| MethodChannel | 不需要 | 需要（Flutter ↔ Native 通信） |
| FlutterEngine | 不需要 | 需要 Application 预热 |
| 定位 | 纯 Android 技术展示 | 混合架构技术展示 |

---

<a id="sec-stats"></a>
## 10. 代码统计

| 子项目 | 语言 | 文件数 | 说明 |
|--------|------|--------|------|
| legacy-android | Java + XML | 12 Java + 10 XML | 传统 Java 版，含底部 4 Tab + 5 级导航 |
| jetpack-android | Kotlin | ~28 | Compose + MVVM + Room + Hilt + Retrofit + DataStore |
| native-android | Kotlin | ~12 | Flutter Add-to-App 壳 + 4 级导航 |
| flutter_module | Dart | ~12 | Flutter + Riverpod + sqflite（AAR 打包） |

---

<a id="sec-changelog"></a>
## 11. 变更日志

| 日期 | 类型 | 描述 |
|------|------|------|
| 2026-07-06 | 文档 | 更新项目地图：反映 Hilt+Retrofit+DataStore 架构升级 |
| 2026-07-05 | 功能 | jetpack-android 集成 Hilt DI + Retrofit 网络层 + DataStore + Repository 模式，新增文件/设置/传输 3 个页面 |
| 2026-07-04 | 迁移 | 10 个 Skill 从 workbuddy .skills/ 迁移至 TRAE 原生 .trae/skills/ 格式（SKILL.md + frontmatter） |
| 2026-06-28 | 初始化 | 创建 flutter_module + native-android，Flutter Add-to-App MVP |
| 2026-06-28 | 新增 | 创建 legacy-android（Java/XML）和 jetpack-android（Compose/MVVM）两套对照代码 |
| 2026-06-28 | 功能 | 三套项目各实现 4 级导航（Navigation Component / NavHost / Navigator.push） |
| 2026-06-28 | 重构 | legacy-android 第 4 级改为总结页（避免番茄钟循环） |
| 2026-06-29 | 重构 | legacy-android 首页改造：TabContainerFragment 底部 4 Tab 切换 |
| 2026-06-29 | 功能 | jetpack-android 同步改造：MainTabScreen + NavigationBar 底部 4 Tab |
| 2026-06-29 | 新增 | native-android 同步改造：MainTabScreen + 4 级导航，切换 LAUNCHER 到 MainActivity |
| 2026-06-29 | 修复 | 四级导航「返回首页」崩溃：popBackStack 后 Fragment already added |
| 2026-06-29 | 修复 | 列表页被 Toolbar 盖住：activity_main.xml 加 fitsSystemWindows |
| 2026-06-29 | 文档 | 首次完整编写本项目代码地图 |
