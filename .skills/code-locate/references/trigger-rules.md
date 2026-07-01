# 触发判断决策树（机械可判）

> 本文件给 agent 一个**不依赖主观判断**的决策流程。弱模型靠走决策树即可正确判断"要不要查索引"。

---

## 决策树

```
用户输入
  │
  ├─ 包含代码文件后缀? (.kt/.java/.aidl/.xml/.gradle/.kts/.sh)
  │   ├─ 是 → ✅ 触发查索引
  │   └─ 否 ↓
  │
  ├─ 包含方法签名? (ClassName.method() / ClassName::method / 带参数类型)
  │   ├─ 是 → ✅ 触发查索引
  │   └─ 否 ↓
  │
  ├─ 包含反引号包裹的标识符? (`SomeClass`)
  │   ├─ 是 → ✅ 触发查索引
  │   └─ 否 ↓
  │
  ├─ 包含全限定类名? (com.xxx.Yyy / android.car.Zzz)
  │   ├─ 是 → ✅ 触发查索引
  │   └─ 否 ↓
  │
  ├─ 包含行号引用? (File.kt:42 / 第 N 行)
  │   ├─ 是 → ✅ 触发查索引
  │   └─ 否 ↓
  │
  ├─ 包含纯 CamelCase 词但没有代码格式? (如 "Launcher 那个数据加载很慢")
  │   ├─ config.locate_mode == strict → ❌ 不触发，反问确认
  │   └─ config.locate_mode == loose → ⚠️ 建议查索引，但先问用户
  │   └─ 否 ↓
  │
  └─ 纯中文描述? ("蓝牙连不上"、"启动慢")
      └─ ❌ 不触发，反问用户确认范围
```

---

## 触发特征速查表

| 特征 | 示例 | 触发 |
|------|------|------|
| 文件后缀 | `BluetoothController.kt` | ✅ |
| 文件后缀+行号 | `MainActivity.java:42` | ✅ |
| AIDL | `ICarService.aidl` | ✅ |
| Gradle | `build.gradle.kts` | ✅ |
| 方法签名(括号) | `Launcher.onCreate()` | ✅ |
| 方法签名(双冒号) | `BluetoothController::connect()` | ✅ |
| 带参数类型 | `foo(view: View)` | ✅ |
| 单函数调用 | `loadData()` | ✅ |
| 反引号 | `` `BluetoothController` `` | ✅ |
| 全限定名 | `com.example.Launcher` | ✅ |
| 行号 | "Launcher.java 第 87 行" | ✅ |

## 不触发示例

| 输入 | 原因 | agent 行为 |
|------|------|-----------|
| "蓝牙连不上" | 纯中文 | 反问：配对/连接/传输哪个阶段? |
| "Launcher 启动慢" | 纯中文 | 反问：是指 `LauncherModel.loadWorkspace` 还是整体流程? |
| "WiFi 模块怎么实现的" | 纯中文 | 反问：要我看哪个类? 或先跑 explore 收录 WiFi 相关类 |
| "那个数据加载很慢" | 纯中文 | 反问：你说的数据加载是哪个方法? |

## 边界情况（混合输入）

| 输入 | 判断 | 行为 |
|------|------|------|
| "蓝牙连不上，看下 `BluetoothController.kt:87`" | 有后缀+行号 → 触发 | 查索引→定位→读 87 附近 |
| "Launcher 那个数据加载很慢" | 有 CamelCase 无代码格式 | strict: 反问; loose: 建议查 `LauncherModel.loadWorkspace` |
| "看看 `BluetoothController` 的连接逻辑" | 有反引号 → 触发 | 查索引→定位→读连接方法 |

---

## 反问模板（不触发时用）

弱模型不知道怎么反问时，套这 3 个模板：

**模板 1：列举可能性**
```
"<问题>" 有几种可能：
1. <阶段1> → <类名1>
2. <阶段2> → <类名2>
3. <阶段3> → <类名3>
你遇到的是哪个？
```

**模板 2：确认方法名**
```
你说的 <功能> 是 `ClassName.method()` 这个方法吗？
还是在说 <更广的范围>？
```

**模板 3：建议先收录**
```
这个问题涉及的代码可能还没收录到 `.agent/project-map.md`。
要不要我跑一下 project-memory sync，把这个模块加进索引？
```

---

## 设计依据

为什么用决策树而非"靠理解"：
- 弱模型（国产小模型）的"理解"不可靠，容易误判
- 决策树是**机械匹配**，模型只需查表，不依赖推理能力
- 这也是 harness engineering 的"约束解空间让 agent 更高效"原则

为什么 strict 模式默认反问：
- 反问的成本（1 轮对话）远低于误判后 grep 全项目的成本（10k+ token）
- 弱模型宁可多问，不要乱扫
