# jetpack-android —— Jetpack MVVM 版（最完整工程）

## 项目定位
三套实现中**功能最完整、最贴近真实「NAS 私有云客户端」**的一个。在学习过程中逐步扩展了 NAS 文件管理、传输、网络层、Hilt 依赖注入，**已明显领先于 legacy / flutter 两个工程**。
可作为面试讲「现代 Android 架构」的主战场。

## 技术栈
- 语言：Kotlin
- UI：Jetpack Compose（声明式）
- 架构：MVVM + `StateFlow` + Repository
- 异步：Coroutines + Flow
- 持久化：Room（`PomodoroEntity` / `PomodoroDao` / `PomodoroDatabase`）+ DataStore（`SettingsDataStore`）
- 网络：自建 Retrofit-like 网络层（`OkHttpClientProvider` + `NasApiService` + `MockNasInterceptor` + sealed `Result`）
- DI：Hilt（`@HiltViewModel` + `AppModule`）

## 功能地图（当前真实状态）
底部 4 个 Tab（`MainTabScreen`，Compose `NavigationBar`）：
| Tab | Screen | 说明 |
|-----|--------|------|
| 番茄钟 | `TimerScreen` | 倒计时（`SavedStateHandle` + `Channel` 增强） |
| 文件 | `FileListScreen` | **独有**：NAS 文件列表，走 Repository + 网络层 |
| 设置 | `SettingsScreen` | **独有**：DataStore 持久化 |
| 传输 | `TransferListScreen` | **独有**：Flow 驱动的上传/下载进度 |

独立页面（`AppNavHost` 路由，从番茄钟进入 4 级导航演示）：
`HomeScreen` → `ListScreen` → `DetailScreen/{itemId}` → `CommentScreen/{itemId}`

另有 `HistoryScreen`（番茄钟历史，`Room` + `Flow` 自动刷新）。

> 对照 legacy：多了「**文件 / 传输 / 设置(DataStore)**」三大模块与整套**网络层 + Hilt**；这些是 jetpack 独有的，legacy / flutter 都没有。

## 实际文件树（`app/src/main/java/com/pomodoro/jetpack/`）
```
├── PomodoroApplication.kt
├── model/
│   └── PomodoroEntity.kt
├── data/
│   ├── PomodoroDao.kt
│   ├── PomodoroDatabase.kt
│   ├── SettingsDataStore.kt
│   └── repository/
│       ├── FileRepository.kt          # 文件业务（独有）
│       └── TimerRepository.kt
├── di/
│   └── AppModule.kt                   # Hilt 注入（独有）
├── network/                           # 网络层（独有）
│   ├── MockNasInterceptor.kt
│   ├── NasApiService.kt
│   ├── NasFileResponse.kt
│   ├── OkHttpClientProvider.kt
│   └── Result.kt
├── ui/
│   ├── MainActivity.kt
│   ├── AppNavHost.kt
│   ├── timer/TimerScreen.kt
│   ├── history/HistoryScreen.kt
│   ├── home/HomeScreen.kt
│   ├── list/ListScreen.kt
│   ├── detail/DetailScreen.kt
│   ├── comment/CommentScreen.kt
│   ├── settings/SettingsScreen.kt
│   └── main/
│       ├── MainTabScreen.kt           # 底部 4 Tab
│       └── FileListScreen.kt          # NAS 文件（独有）
└── viewmodel/
    ├── TimerUiState.kt
    ├── TimerViewModel.kt
    ├── HistoryViewModel.kt
    ├── FileViewModel.kt               # 文件 VM（独有）
    ├── TransferViewModel.kt           # 传输 VM（独有）
    └── SettingsViewModel.kt           # 设置 VM（独有）
```

## 如何运行
Android Studio 打开 `jetpack-android/` → Sync Gradle → Run → 选模拟器。

## ⚠️ 待清理
- 工程根目录误放了 4 个面试 md（`Android Jetpack ms八股（150题）.md` 等），与顶层目录重复且文件名损坏（"面"→"ms"），建议移回顶层或删除。
- `app/build/`、`app/.gradle/`、`.kotlin/` 均为生成物，无需纳入版本管理。
