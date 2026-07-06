---
name: "coding-rules"
description: "强制代码纪律：Kotlin Google Style + Java 阿里手册；最小改动只改用户要求的；注释去 AI 味。任何 Edit/Write .kt/.java/.kts 自动加载。"
---

# Android 代码纪律（强约束） + 编辑 SOP

> **agent 编码时的"宪法"**。任何 Kotlin/Java 代码生成与修改前必须先读本文件。

**ROLE**：决定"怎么写代码"——风格 + 最小改动 + 去 AI 味。

**WHEN**：任何准备 Edit/Write `.kt`/`.java`/`.kts` 时自动加载。

---

## 🚨 强制编辑 SOP

```
Step 0: 判断是否需要用户确认
  ├── 纯重构/改 bug/改实现 → 先出方案，等用户点头（confirm-first）
  └── 用户已明确指定改法 → 可跳过

Step 1: Edit/Write 代码 → 最小改动，去 AI 味

Step 2: 🚨 立即跑构建验证（verify-loop，不可跳过）
  ├── 跑 build_cmd
  ├── 读取完整输出
  ├── 失败 → 修复 → 重跑（最多 2 次）
  └── 成功 → 对照原始需求检查 → 交付
```

---

## 约束 1：代码风格

### Kotlin → Google Kotlin Style
1. **缩进**：4 空格，禁 Tab
2. **命名**：类 UpperCamelCase；函数/属性 lowerCamelCase；常量 UPPER_SNAKE_CASE
3. **行长**：软上限 100，硬上限 120
4. **Import**：字母序、不分组、禁通配
5. **Lambda**：`it` 仅单参数且含义明确
6. **数据类**：字段统一放类顶部，优先 `val`
7. **命名参数**：>2 参数的方法调用强制命名参数
8. **避免 `!!`**：用 `?:` 或显式判空

### Java → 阿里 Java 手册
1. **缩进**：4 空格
2. **命名**：类 UpperCamelCase（名词）；方法 lowerCamelCase（动词）；常量 UPPER_SNAKE_CASE
3. **注释（强制）**：所有类有类注释；公有方法有 Javadoc
4. **OOP**：重写加 `@Override`；禁止实例访问静态成员
5. **并发**：禁 `Executors.newFixedThreadPool`，用 `new ThreadPoolExecutor(...)`
6. **异常**：禁 catch 后不处理；`finally` 不 return；抛异常带 cause

---

## 约束 2：最小改动范围

### 严禁
- 改不相关的注释/代码逻辑/import
- 改格式/缩进/换行/空行
- 改变量/方法/类名
- 重构（提方法/合并/设计模式）
- 修其他 bug / 补 TODO / 加日志
- 改 build.gradle 依赖 / manifest 权限

### 允许
- 改用户明确要求的行/方法/类
- 改直接相关代码（A 调 B，改 A 必须 B 配合）
- 必要的 import（因新引用）
- 必要的 Javadoc（方法签名变了）

### 改前 6 问
```
□ 用户明确说要改这块吗?
□ 这是直接相关的代码吗?
□ 我有没有顺手改其他地方?
□ 我有没有改注释(非必要)?
□ 我有没有改格式/缩进?
□ 我有没有加用户没要求的代码?
```
**任何一题答 No/不确定 → 不要做。**

---

## 约束 3：注释只写代码本身（严禁 AI 味）

### 严禁（7 类 AI 味）
| 类型 | 反例 |
|------|------|
| 讨论过程 | "经过讨论决定用 A" |
| 重构历史 | "重构自原来的 X 方法" |
| 用户上下文 | "用户要求优化这里" |
| AI 思考 | "我加了 null 检查" |
| 主观情绪 | "这里很 tricky" |
| 未来规划 | "未来可能改成 Y" |
| 第一人称 | 任何"我/我们/AI" |

### 应该写
- what：代码做什么
- why：纯技术原因
- 约束：使用限制
- KDoc/Javadoc：公共 API 文档
- 魔法数字：`// 5 秒超时`

---

## 输出前自检

```
风格(Kotlin): □4空格 □命名 □import序无通配 □lambda参数 □命名参数
风格(Java):   □Javadoc □@Override □静态类名访问 □无Executors
改动范围:     □只动要求的 □没动注释 □没动import □没动格式 □没重构
去AI味:       □无对话痕迹 □无历史 □无AI思考 □无主观 □无未来
```
**任一不过 → 不要提交。**