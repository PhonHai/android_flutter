---
name: code-locate
description: `.agent/project-map.md` 索引使用规范（自动加载）。判断触发：用户输入带代码文件后缀(.kt/.java/.aidl)、方法签名(ClassName.method())、反引号包裹的类名、全限定名、行号引用时，必须先查 `.agent/project-map.md` 索引定位，再精准 Read。纯中文描述不触发，反问用户确认范围。这是 agent 默认行为，后台自动执行。
applies_to: universal
user-invocable: false
---

# 代码定位规范（自动）

> **核心立场**：`.agent/project-map.md` 存在的意义就是"快速找到"，不允许 agent 退化成"重新扫全项目"。
> **但不是所有查询都查索引**——只对有"代码味"的查询查，纯中文描述不查。

---

## 触发判断（机械可判，不靠主观）

详细决策树见 `references/trigger-rules.md`。这里给速查：

### ✅ 触发查索引（任一特征）
- 代码文件后缀：`BluetoothController.kt`、`MainActivity.java:42`、`.aidl`、`build.gradle.kts`
- 方法签名：`Launcher.onCreate()`、`BluetoothController::connect()`、`foo()`
- 反引号包裹：`` `BluetoothController` ``
- 全限定名：`com.example.feature.launcher.Launcher`
- 行号引用：`Launcher.java:87`

### ❌ 不触发（直接理解 + 反问）
- 纯中文描述："蓝牙连不上""Launcher 启动慢""WiFi 模块怎么实现的"

### 判断优先级
`.kt/.java` 后缀 > 方法签名 > 反引号 > 纯 CamelCase > 纯中文

---

## 触发时的强制行为

### Step 1：读 `.agent/project-map.md` 索引区（只读必要锚点）
用 Read + offset/limit，只读这些锚点区块：
- `#sec-entry`（核心入口）
- `#sec-flow`（业务流程）
- `#sec-ipc`（IPC 接口）

**不要读**：`#sec-meta`、`#sec-modules`（太粗）、`#sec-permission`、`#sec-build`、`#sec-changelog`
**目标**：每次读 `.agent/project-map.md` 消耗 < 8k token。

### Step 2：索引里找匹配项
- 精确匹配优先（类名完全相同）
- 模糊匹配次之（子串包含）
- 大小写不敏感

### Step 3：Read + offset/limit 精准读目标文件
找到 `BluetoothController.kt:87` → Read offset=70, limit=30（读 70-100 行）
**不要** Read 整个文件再 grep。

### Step 4：回答
基于读到的内容回答，贴 1-5 行关键代码片段。

---

## 不触发时的行为

纯中文描述 → **不要**自动 grep 全项目，**不要**猜索引：
1. 基于上下文理解用户意图
2. 反问确认范围：
   ```
   "蓝牙连不上" 有几种可能：
   1. 配对阶段失败 → BluetoothPairingController
   2. 连接阶段失败 → BluetoothController.connect()
   3. 传输数据断连 → BluetoothDataChannel
   你遇到的是哪个？
   ```

> **弱模型保护**：config 里 `locate_mode: strict` 时，纯中文也倾向反问，避免误判。

---

## 何时退化到 grep（3 种情况）

1. `.agent/project-map.md` 不存在（还没跑 project-memory explore）
2. 索引里没这个类（没收录）
3. 用户明确说"搜全项目""grep 一下"

退化时克制：
```bash
# ✅ 限定范围 + 限制结果
find ./app/src/main -name "BluetoothController*" 2>/dev/null
grep -rn "class BluetoothController" --include="*.kt" ./feature/bluetooth | head -5
# ❌ 全项目 grep（浪费 token）
```
找到后建议跑 sync 更新索引。

---

## 反例（agent 禁止）

- ❌ 纯中文也强行 `grep -rn "bluetooth" .`（浪费 10k token）
- ❌ 有代码标识符也退化成 `find . -name "*Bluetooth*"`（明明索引里有）
- ❌ Read 整个 2000 行文件再 grep
- ❌ 找到后不说"建议跑 sync 更新索引"

---

## 与其他 skill 的关系

```
用户输入
   ↓
[code-locate] 判断触发 ← 本 skill
   ↓
触发? → 查 `.agent/project-map.md` 索引 → Read 精准行 → 回答
不触发? → 反问用户确认范围
   ↓
用户要修改?
[confirm-first] 输出方案 + 等确认
[coding-rules] 风格检查 → Edit
[verify-loop] 验证
[project-memory sync] 更新索引
```
