<!-- applies_to: flutter-hybrid — Flutter Add-to-App 混合架构专用 -->

# Flutter Add-to-App 混合架构规则

> 适用：`native-android`（壳工程）+ `flutter_module`（Flutter 模块）的交互部分。
> 本项目是 Add-to-App 模式：Flutter 模块编译为 AAR，嵌入原生 Android 壳工程。

---

## §engine — FlutterEngine 生命周期

### 核心类
| 类 | 职责 | 所在文件 |
|---|------|---------|
| `PomodoroApplication` | Application 层预热引擎 + 注册 Channel | `native-android/.../PomodoroApplication.kt` |
| `FlutterEngine` | Dart VM 实例（一个引擎 = 一个 Dart Isolate） | Flutter SDK |
| `FlutterEngineCache` | 全局引擎缓存（`LruCache<String, FlutterEngine>`） | Flutter SDK |
| `FlutterContainerActivity` | 壳 Activity，用 `FlutterFragment` 加载缓存引擎 | `native-android/.../FlutterContainerActivity.kt` |
| `FlutterFragment` | Fragment 子类，嵌入 Flutter UI | Flutter SDK |

### 引擎管理规则
- **必须在 `Application.onCreate` 预热**：创建成本 ~150-200ms，提前创建避免白屏
- **必须缓存**：`FlutterEngineCache.getInstance().put(ENGINE_ID, engine)`
- **必须执行 Dart 入口**：`dartExecutor.executeDartEntrypoint(createDefault())`
- **ENGINE_ID = `"pomodoro_engine"`**：`PomodoroApplication` 和 `FlutterContainerActivity` 必须一致

### 严禁
- ❌ 在 Activity.onCreate 里新建 FlutterEngine（每次打开白屏 200ms）
- ❌ 不调用 `executeDartEntrypoint`（引擎创建但 Dart 不运行，白屏）
- ❌ `shouldAttachEngineToActivity(true)`（引擎随 Activity 销毁，无法复用）

### `FlutterFragment` vs `FlutterActivity`
- 本项目用 `FlutterFragment`：可嵌套在原生 UI 中，更灵活
- `FlutterActivity`：全屏 Flutter，引擎内建，不灵活
- **不要改成 `FlutterActivity`**——会丢失混合架构能力

### 为什么用 `AppCompatActivity` 而非 `ComponentActivity`
- `FlutterFragment` 是 Fragment 子类，需要 `FragmentManager`
- `AppCompatActivity` 继承 `FragmentActivity`，支持 `FragmentManager`
- `ComponentActivity` 不支持传统 Fragment 生命周期
- **不要改 `FlutterContainerActivity` 的父类**

---

## §channel — MethodChannel 通信

### 通信模型
```
Flutter 侧                     MethodChannel            原生侧
────────────                  ─────────────            ────────
channel.invokeMethod(         ──异步消息──→            setMethodCallHandler {
  'saveRecord', {args})        (二进制编码)              call.method → when 匹配
                                ←──返回结果──            result.success() / .error()
```

### Channel 契约（两端必须完全一致）

| 方法名 | Flutter 调用 | 原生处理 | 参数 | 返回 |
|--------|-------------|---------|------|------|
| `saveRecord` | `_channel.invokeMethod<bool>('saveRecord', {...})` | `when` 分支匹配 | `startTime: String`, `durationMinutes: Int`, `endTime: String` | `Bool` |
| `getRecords` | `_channel.invokeListMethod('getRecords')` | `when` 分支匹配 | 无 | `List<Map>` |

**Channel name**：`"com.pomodoro/history"`（两端必须完全一致）

### 改动规则
- 新增方法：原生侧加 `when` 分支 + Flutter 侧加 `static Future` 方法
- 改方法名：**两端同步改**，否则 `MissingPluginException`
- 改参数 key：**两端同步改**，否则原生侧 `call.argument<T>()` 返回 null
- 原生侧取参数后**必须做 null 检查**：`call.argument<String>("startTime") ?: return`

### 异常处理策略
| 异常 | 含义 | 处理 |
|------|------|------|
| `PlatformException` | 原生端执行出错 | Flutter 侧 `catch` → 返回 false |
| `MissingPluginException` | 原生端未实现 | Flutter 侧 `catch` → 降级到 sqflite |
| 原生侧 `result.notImplemented()` | 未知方法名 | Flutter 侧抛 `MissingPluginException` |

### 当前 TODO（来自 progress.json）
- `saveRecord` / `getRecords` 原生侧为 TODO 占位，当前返回 `true` / 空列表
- Week 2 计划：原生侧接入 Room，Flutter 侧不改（通信契约不变）

---

## §build — AAR 构建与依赖

### Flutter 模块编译
```bash
cd flutter_module
flutter build aar
# 产物：flutter_module/build/host/outputs/repo/
```

### native-android 引用 AAR
- `build.gradle.kts` 中配置 Flutter AAR 仓库路径
- 改 `flutter_module` 代码后需重新 `flutter build aar`，native-android 才能拿到最新
- **不要把 AAR 产物提交到 git**（已在 `.gitignore` 中排除）

### 依赖关系
```
flutter_module (Dart)
    ↓ flutter build aar
native-android (Kotlin) ← 引用 AAR
    ↑ 独立
jetpack-android (Kotlin) ← 不依赖 Flutter
    ↑ 独立
legacy-android (Java) ← 不依赖 Flutter
```

---

## §arch-boundary — 架构边界（什么放原生，什么放 Flutter）

| 职责 | 放哪 | 原因 |
|------|------|------|
| 计时器 UI + 逻辑 | Flutter 模块 | Add-to-App 核心演示 |
| 底部 Tab 容器 | 原生 Compose | 原生主导页面结构 |
| 四级导航页面 | 原生 Compose | 导航演示在原生侧 |
| 历史记录列表 | 原生（`PomodoroListActivity`） | 原生 UI 展示 |
| 数据持久化 | Flutter sqflite（当前）/ 原生 Room（Week 2） | 演进中 |
| MethodChannel | 两端各一份 | 通信桥梁 |

### 边界规则
- **不要把原生导航页搬到 Flutter 里**——四级导航是原生侧的演示
- **不要把计时器搬到原生侧**——native-android 的核心就是 Flutter 计时器
- Flutter 模块只负责计时器 + 历史页，其他页面都在原生

---

## §cross-ref — 跨文件引用

- 引擎预热逻辑 → `native-android/.../PomodoroApplication.kt`
- MethodChannel 原生侧 → `native-android/.../MethodChannelHandler.kt`
- MethodChannel Flutter 侧 → `flutter_module/lib/channels/method_channels.dart`
- Flutter 容器 → `native-android/.../FlutterContainerActivity.kt`
- 混合架构总览 → `.agent/project-map.md` §9 混合架构专题
- 通用规则 → `conventions/universal.md`

---

## 改前检查清单（混合架构专用）

```
□ MethodChannel 改动? → 原生 + Flutter 两端同步
□ Channel name 一致? → "com.pomodoro/history"
□ 方法名两端匹配? → call.method == invokeMethod 第一参数
□ 参数 key 两端一致? → call.argument<T>("key") == Map key
□ FlutterEngine 预热? → Application.onCreate 中创建 + 缓存
□ 用 withCachedEngine? → 不要每次新建引擎
□ shouldAttachEngineToActivity(false)? → 引擎生命周期独立
□ FlutterContainerActivity 用 AppCompatActivity? → 支持 FragmentManager
□ AAR 重新编译了? → 改 flutter_module 后 flutter build aar
□ 走 confirm-first 输出方案?
```
