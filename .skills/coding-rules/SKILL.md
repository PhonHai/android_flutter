---
name: coding-rules
description: 强制 Android 代码纪律，三大不可违反约束。(1) 代码风格：Kotlin 遵循 Google Kotlin Style，Java 遵循阿里 Java 手册；(2) 最小改动范围：只改用户明确要求的，严禁动无关注释/逻辑/格式/重构；(3) 注释去 AI 味：注释只写代码本身，严禁对话/历史/AI思考痕迹。任何准备 Write/Edit .kt/.java/.kts 的任务自动加载。
applies_to: universal
user-invocable: false
---

# Android 代码纪律（强约束） + 编辑 SOP

> **agent 编码时的"宪法"**。任何 Kotlin/Java 代码生成与修改前必须先读本文件。
> 详细规则见 `references/`：
> - `minimal-change.md` — 最小改动检查清单（抗 agent 自作主张）
> - `no-ai-smell.md` — 注释去 AI 味（严禁讨论过程/决策/AI思考）

---

## 🚨 强制编辑 SOP（最高优先级，先读这个再读下面的）

**以下 SOP 不是建议，是强制流程。任何 Edit/Write 代码的操作必须走完完整链条，不可跳过任何一步。**

```
Step 0: 判断是否需要用户确认
  ├── 纯重构/改 bug/改实现 → 先出方案，等用户点头（confirm-first）
  └── 用户已明确指定改法 → 可跳过，直接编辑

Step 1: Edit/Write 代码
  └── 最小改动（约束 2），不去 AI 味（约束 3）

Step 2: 🚨 立即跑构建验证（verify-loop，不可跳过）
  ├── 跑 build_cmd（默认 ./gradlew assembleDebug）
  ├── 读取完整输出（不只看最后一行）
  ├── 失败 → 修复 → 重跑（最多 2 次）
  └── 成功 → 对照原始需求检查 → 交付
```

**你必须在回复中显式报告执行到哪一步。如果我忘记了，你可以说"验证呢？"要求我补上。**

### 为什么 agent 会忘记验证？

> agent 有"first plausible solution bias"——写完代码自读一遍，"看起来对"，就停了。没有外部钩子打断这个模式。本 SOP 就是人为打破机制。

### 如何确保不被跳过？

| 机制 | 作用 |
|------|------|
| 本 SOP 放在文件最前面 | 每次读 coding-rules 首先看到 |
| `verify-loop` 检查 `verify_loop.enabled` | 若 `true`（默认），不可跳过 |
| 用户在对话里监督 | 改完没验证，用户可追问 |

---

## 约束 1：代码风格（违反 = 不可接受）

### Kotlin → Google Kotlin Style（硬规则）
1. **缩进**：4 空格，禁 Tab
2. **命名**：类 UpperCamelCase；函数/属性 lowerCamelCase；常量 UPPER_SNAKE_CASE；包全小写无下划线
3. **行长**：软上限 100，硬上限 120
4. **Import**：字母序、不分组、禁通配 `import kotlinx.*`、不 import 未使用
5. **Lambda**：`it` 仅单参数且含义明确；多行 lambda 写显式参数名
6. **数据类**：字段统一放类顶部，优先 `val`
7. **字符串模板**：简单变量 `"$name"`，表达式 `"${expr}"`
8. **命名参数**：>2 参数的方法调用强制命名参数
9. **避免 `!!`**：用 `?:` 或显式判空

### Java → 阿里 Java 手册（硬规则）
1. **缩进**：4 空格
2. **命名**：类 UpperCamelCase（名词）；方法 lowerCamelCase（动词）；常量 UPPER_SNAKE_CASE；异常类 `Exception` 后缀
3. **注释（强制）**：所有类有类注释；公有方法有 Javadoc（入参/出参/异常）；字段有注释；格式 `/** 描述. */`
4. **OOP**：重写加 `@Override`；禁止实例访问静态成员；构造方法不注入业务逻辑
5. **集合**：`subList` 不能强转 ArrayList、不能改原 List；优先 `ArrayList<>(other)`
6. **并发**：禁 `Executors.newFixedThreadPool`，用 `new ThreadPoolExecutor(...)`；高并发 Map 用 `ConcurrentHashMap`
7. **控制语句**：嵌套 ≤4 层；`if` 复合条件加括号；`switch` 必有 `default`
8. **异常**：禁 catch 后不处理；`finally` 不 return；抛异常带 cause
9. **日志**：用 SLF4J，占位符 `logger.info("user {}", id)`，不字符串拼接；LOGGER 大写

### 反例（agent 禁止输出）
```kotlin
// ❌ 错
class foo_bar {}                  // 命名错
fun DoSomething():Int{return 0}   // 大括号位置 + 缺空格
import kotlin.*                   // 通配符
```

> **linter 映射**：Kotlin 用 ktlint/detekt，Java 用 Checkstyle/阿里 P3C。规则 ID 见各 linter 文档，CI 强制跑。

---

## 约束 2：最小改动范围（违反 = 不可接受）

> 详见 `references/minimal-change.md`。核心：**只动用户明确要求的**。

### 严禁（白名单外 = 禁止）
- 改不相关的注释/代码逻辑/import
- 改格式/缩进/换行/空行
- 改变量/方法/类名
- 重构（提方法/合并/设计模式）
- 修其他 bug / 补 TODO / 加日志
- 改 build.gradle 依赖 / manifest 权限 / aapt 资源

### 允许（白名单）
- 改用户明确要求的行/方法/类
- 改直接相关代码（A 调 B，改 A 必须 B 配合）
- 必要的 import（因新引用）
- 必要的 Javadoc（方法签名变了）
- 必要的测试（逻辑变了）

### 改前 6 问（每次 Edit 前逐一问）
```
□ 用户明确说要改这块吗?
□ 这是直接相关的代码吗?
□ 我有没有顺手改其他地方?
□ 我有没有改注释(非必要)?
□ 我有没有改格式/缩进?
□ 我有没有加用户没要求的代码?
```
**任何一题答 No/不确定 → 不要做。**

> **harness 兜底**：Edit 后 `_harness/scripts/diff_guard.sh` 会机械检查，agent 跳过自检也会被拦。

---

## 约束 3：注释只写代码本身（严禁 AI 味）

> 详见 `references/no-ai-smell.md`。核心：注释给代码读者看，不是 AI 工作记录。

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
- 约束：使用限制（"必须主线程调用"）
- KDoc/Javadoc：公共 API 文档
- 魔法数字：`// 5 秒超时(车机 ANR 阈值)`

> **harness 兜底**：Edit 后 `_harness/scripts/ai_smell_check.sh` 机械扫描 AI 味短语。

---

## 输出前自检（必走）

```
风格(Kotlin): □4空格 □命名 □import序无通配 □lambda参数 □命名参数
风格(Java):   □Javadoc □@Override □静态类名访问 □无Executors □LOGGER大写
改动范围:     □只动要求的 □没动注释 □没动import □没动格式 □没重构 □diff无惊喜
去AI味:       □无对话痕迹 □无历史 □无AI思考 □无主观 □无未来 □只写代码本身
```
**任一不过 → 不要提交。**

---

## 当用户没明确说但代码需要改时

**不擅自做决定**，直接问：
```
❓ 修改 [X] 时需要顺便处理 [Y]，因为 [原因]。
   - 选项1: [方案]
   - 选项2: [方案]
   你想怎么选?
```
不要默默改。

---

## 与其他 skill 的关系

| Skill | 关注 |
|-------|------|
| arch-rules | 架构层（类组织/AIDL/Car API）|
| coding-rules（本 skill）| 代码层（风格/范围/注释）|
| _harness/scripts | 机械检查（diff_guard/ai_smell）|

两者互补，通常同时加载。
