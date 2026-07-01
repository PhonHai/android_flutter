# 最小改动检查清单

> 抗 agent "自作主张"的强约束。v2 增加 harness 集成（diff_guard.sh 机械兜底）。

---

## 核心原则

**只动用户明确要求的。其他全部保持原样。**

---

## 严禁清单（白名单外 = 禁止）

| 严禁 | 例子 |
|------|------|
| 改不相关注释 | 改方法A时，不动方法B的注释，不动A里其他块的注释 |
| 改不相关逻辑 | 修bug时，不顺手重构相邻代码 |
| 改不相关 import | 不"清理"没动文件的 import |
| 改格式/缩进/换行 | 除非用户说"格式化" |
| 改名 | 除非用户明确说重命名 |
| 增删空行 | 改完别"美化" |
| 重构 | 提方法/合并/设计模式——没说就不做 |
| 修其他 bug | 看到旁边bug不动 |
| 补 TODO | 除非用户要做 |
| 增删日志 | 不在要求范围内 |
| 改 build.gradle 依赖 | 不"顺手"加依赖 |
| 改 manifest 权限 | 不"顺便"加权限 |
| 改 aapt 资源 | 不顺手加 string/drawable |

## 允许清单（明确白名单）

- ✓ 改用户**明确说**要改的行/方法/类
- ✓ 改**直接相关**代码（A调B，改A必须B配合才能工作）
- ✓ 必要 import（加了新类引用）
- ✓ 必要 Javadoc（方法签名变了，文档同步）
- ✓ 必要测试（逻辑变了，测试同步——只动必须动的）

---

## 改前 6 问（每次 Edit 前逐一问）

```
□ 用户明确说要改这块吗?
□ 这是直接相关的代码吗?
□ 我有没有顺手改其他地方?
□ 我有没有改注释(非必要时)?
□ 我有没有改格式/缩进/换行?
□ 我有没有加任何用户没要求的代码?
```
**任何一题答 No/不确定 → 不要做。**

---

## 方法内部改动范围（尤其重要）

```kotlin
fun foo() {
    // Block A: 用户要求改这里
    val a = 1
    
    // Block B: 用户没要求改这里
    val b = 2  // 这个注释别动
    
    // Block C: 用户没要求改这里
    val c = 3  // 这个注释也别动
}
```
**只能改 Block A**。Block B/C 的代码、注释、变量名，全部原样保留。

**关键判断**：看 `git diff`，如果有任何"用户没提到的修改行"出现，就是违规。

---

## harness 兜底（v2 新增）

即使 agent 跳过自检，`_harness/scripts/diff_guard.sh` 会在 Edit 后机械检查：
- 纯空行改动（无关格式调整）
- import 改动 > 2 行（顺手清理）
- 改动文件数 > 5（一次改太多）

命中则报错，agent 必须修正后再继续。

### 配置
```yaml
harness:
  diff_guard: true   # skills.config.yaml
```

---

## 分两步提交（建议，大改动时）

1. 先只改用户要求的，提交：`git commit -m "fix: <用户要求的事>"`
2. 若发现需顺带改的，单独提交：`git commit -m "refactor: <顺带的事，单独说明>"`

不要混在一个 commit 里。

---

## 反例 vs 正例

### ❌ 反例：顺手改了
```kotlin
// 用户要求：把 if (a == null) 改成 if (a == null || b == 0)
// agent 顺手：
fun check(a: A?, b: Int) {
-   if (a == null) return
+   if (a == null || b == 0) return  // ✓ 用户要求的
    // 下面这些 agent 不该动：
-   val result = compute(a)          // agent "优化"了变量名
+   val r = compute(a)               // ❌ 没要求改名
-   log("done")                      // agent "清理"了日志
+   // log("done")                   // ❌ 注释掉了日志
}
```

### ✓ 正例：只改要求的
```kotlin
fun check(a: A?, b: Int) {
-   if (a == null) return
+   if (a == null || b == 0) return  // 只改这一行
    val result = compute(a)          // 原样
    log("done")                      // 原样
}
```

---

## 自我审查流程

改完后：
1. `git diff` 看全部改动
2. 逐行问："这行是用户要求的吗？"
3. 有"惊喜"行 → `git checkout` 撤回
4. 跑 `bash .skills/_harness/scripts/diff_guard.sh` 机械确认
5. 通过后再提交

---

## 设计依据

为什么这么严格：
- agent 最容易犯的错不是"风格不对"，而是"顺手改了不该改的"
- 国产模型 context 小，改动越多越容易出错
- 最小改动 = 最小风险 = 最易 review = 最易回滚
- 这是和"代码风格"同等重要的硬约束
