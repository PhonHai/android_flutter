# native-android —— 原生 Android 壳（Add-to-App 宿主）

## 项目定位
通过 **Flutter Add-to-App** 接入 `flutter_module` 的**原生 Kotlin 宿主工程**。`settings.gradle` 用 `include_flutter.groovy` 把 `flutter_module` 挂为 Gradle 子项目 `:flutter`，`app/build.gradle.kts` 里通过 `project(":flutter")` 引用。它本身也是一套完整的 App（`MainTab` 多页面），用 `FlutterContainerActivity` 加载、用 `MethodChannel` 与原生通信。
对标目标公司「原生 + Flutter 混合架构」：原生壳负责导航、设备能力、设置等核心功能，Flutter 作为子项目被嵌入。

## 技术栈
- 语言：Kotlin
- UI：Jetpack Compose（声明式）
- 架构：MVVM + Hilt
- 关键：**Flutter Add-to-App 子项目集成（`project(":flutter")`）** + `FlutterEngine` / `MethodChannel` 集成（`build.gradle.kts` 为 Kotlin DSL）

> 📦 **Flutter 集成方式**：`flutter_module` 作为 Gradle 子项目 `:flutter` 被 `native-android` 直接 `include`（`settings.gradle` → `include_flutter.groovy`），`app/build.gradle.kts` 中 `debugImplementation(project(":flutter"))` / `releaseImplementation(project(":flutter"))` 引用。**是子项目源码依赖，不是预编译的 jar/aar 文件依赖。**

## 功能地图（当前真实状态）

### 4 个 Tab（底部导航）
| Tab | 名称 | 实现 | 说明 |
|-----|------|------|------|
| 0 | 番茄钟 | 原生 Compose | 入口页：启动 Flutter 番茄钟 / 进入 4 级导航演示 |
| 1 | 统计 | 占位页 | PlaceholderContent |
| 2 | 设置 | 原生 Compose | **设备能力入口**：权限/存储/WiFi/蓝牙 |
| 3 | 关于 | 占位页 | PlaceholderContent |

### 核心组件
- **`MainActivity`** → `MainTabScreen`（底部 4 Tab，均为**原生 Compose** 页面）
- **`FlutterContainerActivity`**：加载并展示 `flutter_module`（用 `FlutterFragment` + 缓存引擎）；同时注册**设备能力 MethodChannel**（`com.pomodoro/device`）
- **`PomodoroListActivity`**：原生写的番茄钟历史列表
- **`DeviceCapabilities`**：设备能力共享层（权限/存储/WiFi/蓝牙），原生设置页和 Flutter MethodChannel **共用同一份逻辑**
- **`DeviceChannelHandler`**：设备能力 MethodChannel 处理器，用 `ActivityResultLauncher` 桥接权限请求/SAF 选目录/蓝牙开启确认
- **`MethodChannelHandler`**：处理 Flutter 发来的番茄钟历史相关调用（如保存历史）
- **`AppNavHost`**：原生导航图

> 注意：这里的「番茄钟/统计/设置/关于」Tab 是**原生实现**的，和 `jetpack-android` 的 Tab 名字巧合相同，但代码完全独立（本工程是混合架构的壳，jetpack-android 是纯 Compose 完整 App）。

### 设备能力架构（新增亮点）

```
┌─────────────────────────────────────────────────┐
│              DeviceCapabilities (object)          │
│         权限 / 存储 / WiFi / 蓝牙 能力实现         │
└──────────┬──────────────────┬────────────────────┘
           │                  │
     ┌─────┴──────┐    ┌─────┴──────┐
     │ 原生设置页  │    │ Flutter 侧 │
     │SettingsScreen│   │DeviceChannel│
     │(Compose 直接调)│  │(MethodChannel│
     │              │   │ 转发到同一实现)│
     └─────────────┘    └────────────┘
```

**设计意图**：能力实现只写一遍（`DeviceCapabilities`），消费方有两个——原生 Compose 设置页直接调、Flutter 模块通过 MethodChannel 转发调。避免「原生写一遍、Flutter 再写一遍」的重复（DRY）。

## 实际文件树（`app/src/main/` 以内）
```
app/src/main/
├── AndroidManifest.xml
├── java/com/pomodoro/app/
│   ├── MainActivity.kt
│   ├── PomodoroApplication.kt          # Application：预热 FlutterEngine
│   ├── MethodChannelHandler.kt         # 番茄钟历史 MethodChannel（com.pomodoro/history）
│   ├── DeviceCapabilities.kt           # 设备能力共享层（权限/存储/WiFi/蓝牙）
│   ├── DeviceChannelHandler.kt         # 设备能力 MethodChannel（com.pomodoro/device）
│   ├── PomodoroListActivity.kt         # 原生历史列表
│   ├── FlutterContainerActivity.kt     # 加载 Flutter 模块 + 注册设备通道
│   └── ui/
│       ├── AppNavHost.kt
│       ├── main/
│       │   ├── MainTabScreen.kt        # 底部 4 Tab（番茄钟/统计/设置/关于）
│       │   └── SettingsScreen.kt       # 设置页：设备能力入口（权限/存储/WiFi/蓝牙）
│       ├── home/HomeScreen.kt
│       ├── list/ListScreen.kt
│       ├── detail/DetailScreen.kt
│       └── comment/CommentScreen.kt
└── res/                                # layout / values(colors,themes,strings)
```
（构建脚本：`build.gradle.kts`、`settings.gradle`、`gradle/wrapper/`）

## 如何运行
Android Studio 打开 `native-android/` → Sync Gradle → Run → 模拟器；
再把 Flutter 模块挂上：`cd ../flutter_module && flutter attach`。
