---
name: "arch-rules"
description: "项目架构约束：三套 Android 对照（legacy Java / jetpack Compose / native Flutter 混合）+ 手机端。改 MethodChannel/FlutterEngine/NavHost/Room/Manifest/Gradle 时自动加载。"
---

# 项目架构规则（PomodoroNative）

> **项目定位**：三套 Android App 对照学习 + Flutter 模块（Add-to-App），手机端，普通应用签名。

**项目特征**：三套 Android 对照学习（legacy Java / jetpack Compose / native Flutter 混合）+ Flutter 模块（Add-to-App），手机端。

**WHEN**：改 MethodChannel / FlutterEngine / NavHost / Room / Manifest / Gradle / nav_graph / MethodChannelHandler / FlutterContainerActivity 时自动加载。

**CONVENTIONS**：`universal.md`（线程/ANR/IPC/权限/资源/构建）、`mobile.md`（三套 Android 对照架构 + 手机端规则）、`flutter-hybrid.md`（Flutter Add-to-App 混合架构规则）。

---

## 必须遵守的通用原则（所有子项目）

### 1. 主线程规则
- **任何 IO 操作都不能在主线程**（磁盘、网络、数据库、同步 Binder 调用）
- `jetpack-android` / `native-android`：优先 `kotlinx.coroutines` 的 `Dispatchers.IO`
- `legacy-android`：用 `HandlerThread` 或 `AsyncTask`，保持一致风格
- `flutter_module`：Dart 单线程模型，IO 用 `async/await` + `Future`

### 2. IPC 通信（MethodChannel / AIDL）
- **MethodChannel**（native ↔ flutter）：channel name 两端必须完全一致
- 新增方法时在 `MethodChannelHandler.kt` 的 `when` 分支加 case，**不要改老方法名**
- Flutter 侧 `invokeMethod` 的方法名必须和原生 `call.method` 匹配

### 3. 资源与多语言
- 字符串**必须**放 `res/values/strings.xml`，不硬编码
- `legacy-android` 的 XML 布局引用 `@string/xxx`
- `jetpack-android` / `native-android` 的 Compose 用 `stringResource(R.string.xxx)`
- 颜色用 `res/values/colors.xml`，不硬编码

### 4. 性能
- FlutterEngine 创建成本 ~150-200ms，必须在 `Application.onCreate` 预热
- `FlutterContainerActivity` 用 `withCachedEngine` 取缓存引擎
- `legacy-android` 的 `Handler.postDelayed` 在 `onDestroy` 必须 `removeCallbacks`
- `jetpack-android` 的 Coroutines 在 `onCleared` 必须 `cancel`

---

## 模块级规则入口

| 当前任务 | 读哪个 convention |
|---------|------------------|
| 改 Manifest / 权限 / Gradle 构建 | universal.md |
| 改 MethodChannel / FlutterEngine | flutter-hybrid.md |
| 改 NavHost / nav_graph / 导航 | mobile.md |
| 改 Room / SQLite / sqflite | mobile.md |
| 改 Timer / 计时器逻辑 | mobile.md |
| 改 Compose UI / Fragment | mobile.md |
| 三套代码同步修改 | mobile.md |

---

## 三套代码对照修改原则（最重要）

1. 用 `code-locate` 定位目标代码在哪些子项目中存在
2. 确认是**只改一套**还是**三套同步**
3. 三套同步：以 `jetpack-android`（现代版）为基准，`legacy-android` 用等价 Java 写法，`native-android` 用等价 Kotlin + Compose 写法
4. 走 `confirm-first` 输出方案，列出每套要改的文件

---

## 改前必做

1. 用 `code-locate` 定位目标代码
2. 读 5-10 行上下文确认现有风格
3. 看 `.agent/project-map.md` 确认当前子项目的架构类型
4. 确认是否需要三套同步修改
5. 走 `confirm-first` 输出方案

---

## 注意

- 这是**学习项目**，修改时**不要删除教学注释**
- 三套代码刻意保持功能一致但实现不同，**不要统一为一套**
- `native-android` 的计时器在 Flutter 模块中，不在原生侧——这是架构设计
- 真正动手前，必须走 `confirm-first` 输出方案