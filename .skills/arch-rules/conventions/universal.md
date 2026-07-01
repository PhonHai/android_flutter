<!-- applies_to: universal — 所有 Android 子项目都适用 -->

# 通用 Android 架构规则

> 适用：本项目的三套 Android 子项目（legacy / jetpack / native）+ Flutter 模块。

---

## 1. 签名与权限

- 本项目三套 App 均为**普通应用**，release 签名，无 sharedUserId
- applicationId 各自独立：`com.pomodoro.legacy` / `com.pomodoro.jetpack` / `com.pomodoro.app`
- 改 Manifest 权限前，确认三套 App 是否需要同步加
- **千万不要**给学习项目加 sharedUserId 或系统签名

## 2. 主线程与 ANR

- **任何 IO 不能在主线程**：磁盘、网络、数据库、同步 Binder
- `jetpack-android` / `native-android`：优先 `Dispatchers.IO`
- `legacy-android`：用 `HandlerThread` 或 `AsyncTask`，保持老代码风格
- ANR 阈值：**前台 5 秒 / 后台 200 秒**（手机标准）
- `flutter_module`：Dart 单线程，IO 用 `async/await`，不会阻塞 UI 线程

## 3. MethodChannel / IPC 通信

| 改动 | 是否兼容 | 说明 |
|------|---------|------|
| 新增方法 | ✅ 兼容 | Flutter 侧用新方法名调用 |
| 删除方法 | ❌ 不兼容 | Flutter 侧 `invokeMethod` 会抛 `MissingPluginException` |
| 改方法名 | ❌ 不兼容 | 两端必须同步改 |
| 改参数 key | ❌ 不兼容 | 两端必须同步改 |

**Channel name**：`com.pomodoro/history`（两端必须完全一致）

命名规范：
- 原生侧：`MethodChannelHandler.kt` → `when (call.method) { "saveRecord" -> ... }`
- Flutter 侧：`method_channels.dart` → `_channel.invokeMethod('saveRecord', {...})`

## 4. 资源与多语言

- 字符串放 `res/values/strings.xml`，**不硬编码**
- `legacy-android`：XML 布局 `android:text="@string/xxx"`
- `jetpack-android` / `native-android`：Compose `Text(stringResource(R.string.xxx))`
- 颜色用 `res/values/colors.xml`（`native-android` 已有 `colors.xml`）
- DPI 资源不要删

## 5. 性能通用

- **FlutterEngine 预热**：`PomodoroApplication.onCreate` 中创建 + 缓存，避免 Activity 白屏
- 滑动卡顿：`onDraw` / `Canvas` 创建对象、`onBindViewHolder` 重活
- 启动慢：`onCreate` / `Application.onCreate` 同步加载
- 内存泄漏：静态持有 Context、未反注册 Listener、Handler 持有 Activity
- `legacy-android` 的 `Handler.removeCallbacks` 在 `onDestroy` 必须调用

## 6. 安全红线（不可违反）

- ❌ 禁止 `Runtime.exec` 执行 shell
- ❌ 禁止硬编码密钥/密码到代码
- ❌ 禁止 `android:exported="true"` + 无权限校验的组件
- ❌ 禁止在 MethodChannel 中传递敏感数据不做校验
- ✅ MethodChannel 的 `call.argument<T>()` 取值后做 null 检查

## 7. 构建

- 三套项目各自独立 Gradle wrapper，互不依赖
- `compileSdk: 36`，`minSdk: 24`（三套一致）
- 改 `build.gradle` / `build.gradle.kts` 前看 `.agent/project-map.md` 确认子项目
- `native-android` 的 `build.gradle.kts` 中有 Flutter AAR 依赖配置，改动需谨慎
- 依赖变化要同步到 `.agent/project-map.md`

---

## 改前检查清单

```
□ 确认改的是哪套子项目？（legacy / jetpack / native / flutter_module）
□ 是否需要三套同步修改？
□ IO 操作在主线程? → 移到 IO 线程
□ MethodChannel 改动两端同步? → 改原生也改 Flutter
□ 字符串硬编码? → 移到 strings.xml
□ 走 confirm-first 输出方案了?
```
