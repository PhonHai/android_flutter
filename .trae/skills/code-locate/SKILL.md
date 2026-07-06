---
name: "code-locate"
description: "代码定位。输入含 .kt/.java/类名/方法签名时自动查 .agent/project-map.md 索引，精准 Read，不重新扫全项目。纯中文描述反问用户确认范围。"
---

# 代码定位规范

**ROLE**：读 `.agent/project-map.md` 索引 → 输出定位结果（文件路径 + 锚点 ID），不维护 project-map。

**WHEN**：用户输入含代码文件名/类名/方法签名/AIDL 接口/行号引用时自动触发。纯中文描述反问用户确认范围。

> **核心立场**：`.agent/project-map.md` 存在的意义就是"快速找到"，不允许 agent 退化成"重新扫全项目"。

---

## 触发判断（机械可判）

### ✅ 触发查索引（任一特征）
- 代码文件后缀：`BluetoothController.kt`、`MainActivity.java:42`、`.aidl`
- 方法签名：`Launcher.onCreate()`、`BluetoothController::connect()`
- 反引号包裹：`` `BluetoothController` ``
- 全限定名：`com.example.feature.launcher.Launcher`
- 行号引用：`Launcher.java:87`

### ❌ 不触发（直接理解 + 反问）
- 纯中文描述："蓝牙连不上""Launcher 启动慢"

### 判断优先级
`.kt/.java` 后缀 > 方法签名 > 反引号 > 纯 CamelCase > 纯中文

---

## 触发时的强制行为

### Step 1：读 `.agent/project-map.md` 索引区
只读 `#sec-entry`（核心入口）、`#sec-flow`（业务流程）、`#sec-ipc`（IPC 接口），目标 < 8k token。

### Step 2：索引里找匹配项
精确匹配优先，模糊匹配次之，大小写不敏感。

### Step 3：Read + offset/limit 精准读目标文件
找到 `BluetoothController.kt:87` → Read offset=70, limit=30。**不要** Read 整个文件再 grep。

### Step 4：回答
基于读到的内容回答，贴 1-5 行关键代码片段。

---

## 不触发时的行为

纯中文描述 → 反问确认范围，不要自动 grep 全项目。

---

## 何时退化到 grep（3 种情况）

1. `.agent/project-map.md` 不存在
2. 索引里没这个类
3. 用户明确说"搜全项目""grep 一下"

退化为精准 grep + head 限制，找到后建议跑 sync 更新索引。

---

## 反例（agent 禁止）

- ❌ 纯中文也强行 `grep -rn "bluetooth" .`
- ❌ 有代码标识符也退化成 `find . -name "*Bluetooth*"`
- ❌ Read 整个 2000 行文件再 grep
- ❌ 找到后不说"建议跑 sync 更新索引"

---

## 与其他 skill 的关系

```
用户输入
   ↓
[code-locate] 判断触发
   ↓
触发? → 查索引 → Read 精准行 → 回答
不触发? → 反问用户确认范围
```