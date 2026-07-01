---
name: verify-loop
description: 闭环验证（Build-Verify Loop）。改完代码后强制验证，对照原始需求而非自己的代码自证。agent 有"首个合理方案偏见"——写完代码会默认"看起来对"就停。本 skill 强制跑构建/测试，读完整输出，对照用户原始需求比较，失败则修复循环。任何 Edit/Write 代码后自动触发（除非用户豁免）。
applies_to: universal
user-invocable: false
---

# 闭环验证（Build-Verify Loop）

> **来自 harness engineering 实践**（LangChain/OpenAI）：模型有"first plausible solution bias"——对第一个看起来合理的方案产生偏见，不主动验证等于开环控制。
> **v2 新增**（v1 没有，改完不验证是常见降智原因）。

---

## 为什么需要这个 skill

> "The most common failure pattern was that the agent wrote a solution, re-read its own code, confirmed it looks ok, and stopped." — LangChain

agent 改完代码后的典型失败模式：
1. 写完 → 重读自己的代码 → "看起来对" → 停止
2. **没有实际跑构建/测试**
3. 即使跑了，没**对照原始需求**比较，只对照自己的代码自证

本 skill 强制打破这个模式。

---

## 四步循环（来自 LangChain）

```
1. 规划与发现：读任务 → 扫代码 → 构建计划（已在 confirm-first 完成）
2. 构建：实现计划，同时构建测试（已在 Edit 完成）
3. 验证：运行测试 → 读完整输出 → 对照原始需求比较 ← 本 skill 重点
4. 修复：分析错误 → 回溯原始规范 → 修复
```

---

## 强制流程

### Step 1：读原始需求（从 progress.json / 会话上下文）
明确"用户到底要什么"。不是 agent 理解的什么，是用户**原话**要的。

### Step 2：跑构建
按 config 的 `verify_loop.build_cmd`（默认 `./gradlew assembleDebug`）：
```bash
bash -c "<build_cmd>" 2>&1 | tail -50
```
**必须读完整输出**，不能只看最后一行"BUILD SUCCESSFUL"。

### Step 3：判定结果

**✅ 构建成功** → 进入 Step 4（对照需求）

**❌ 构建失败** → 进入修复循环：
1. 读错误输出，定位失败原因
2. **回溯原始需求**（不是猜），确认修复方向
3. 修复代码（走 confirm-first 的简化确认，因为是在修自己刚改的）
4. 重新跑构建
5. 最多重试 `verify_loop.max_retries`（默认 2）次
6. 超过次数 → STOP，报告用户："验证失败 N 次，需要人工介入"

### Step 4：对照原始需求比较（关键，不能省）

构建成功 ≠ 任务完成。问自己：
```
□ 用户要的功能/修复，代码实现了吗?
□ 实现方式符合用户的约束吗?(不动其他代码/不改 manifest 等)
□ 有没有引入用户没要求的行为?
□ 边界情况处理了吗?(null/空列表/异常)
```

**任何一题答 No → 回到 Step 4 修复，不是直接交付。**

### Step 5：跑 harness 检查
```bash
bash .skills/_harness/scripts/diff_guard.sh
bash .skills/_harness/scripts/ai_smell_check.sh <改的文件>
```
任一报错 → 修正后再继续。

### Step 6：更新 progress.json
记录验证结果（供 trace 驱动改进）：
```json
"verify_stats": {
  "total_tasks": +1,
  "first_pass_ok": +1 (若一次通过) 或 不变,
  "retries_needed": +N (重试次数)
}
```

### Step 7：报告用户
```
✅ 验证通过
- 构建: ./gradlew assembleDebug 成功
- 需求对照: 实现了 <用户要的>，未引入 <用户没要求的>
- harness: diff_guard / ai_smell 均通过
- 建议跑 project-memory sync 更新索引
```

---

## 循环检测（防 doom loop）

> 来自 Claude Code 的 LoopDetectionMiddleware：同一文件编辑超过 N 次后提醒"重新审视方案"。

若同一文件被 Edit 超过 3 次仍在修构建错误：
```
⚠️ 已对 <文件> 编辑 N 次仍未通过验证。
这可能是方案问题，不是实现问题。
建议：停下来，重新审视方案，或问用户。
```
不要无限循环修。

---

## 推理"三明治"策略（来自 harness engineering 实测）

最优推理预算分配是"高-中-高"：
- **规划阶段**（confirm-first）：高推理，充分理解问题
- **实现阶段**（Edit）：中等推理，节省 token
- **验证阶段**（本 skill）：高推理，捕获错误

> 弱模型在验证阶段尤其要慢、要细，因为这是 catch 错误的最后机会。

---

## 何时可跳过

- 用户明确说"不用验证"
- 纯文档修改（非代码）
- config 里 `verify_loop.enabled: false`

**其他情况不可跳过。** 即使"只改了 1 行"也要验证——1 行也可能引入编译错误。**agent 没有判断"这个改动太简单不需要验证"的权限——这只能由用户决定。**

## 🚨 与 coding-rules SOP 的关联

`coding-rules` 已定义强制编辑 SOP：
```
Edit/Write → 立即验证 → 对照需求 → 交付
```

本 skill 是 SOP 的第 2 步。**任何 Edit/Write 代码后，自动走本 skill 的 Step 1-7。**
跳过本 skill = 违反 SOP = 任务未完成。

---

## 验证清单（详细版见 references/verify-checklist.md）

```
□ 构建成功?（读完整输出，不只看最后一行）
□ 对照原始需求: 实现了用户要的?
□ 没引入用户没要求的行为?
□ 边界情况: null/空/异常处理了?
□ harness 检查: diff_guard + ai_smell 通过?
□ 更新 progress.json 的 verify_stats?
```

---

## 与其他 skill 的关系

```
[confirm-first] 用户同意 → Edit
   ↓
[_harness] diff_guard + ai_smell 机械检查
   ↓
[verify-loop] 构建 → 对照需求 → 修复循环 ← 本 skill
   ↓
验证通过
   ↓
[project-memory sync] 更新索引
```

**本 skill 是闭环的最后一环**——没有验证的修改等于没做完。
