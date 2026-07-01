---
name: confirm-first
description: 强制"先确认、再动手"。任何准备修改项目代码(Kotlin/Java/manifest/gradle/资源/AIDL/Shell/配置)的任务，agent 必须先输出"修改方案+修改原因+影响范围+风险评估"，等用户明确同意后才能 Edit/Write/Bash。用户说"修bug""加功能""改名"等都触发。纯查询/读代码/解释不触发。v2 强化"同意"判定标准，避免弱模型误判。
applies_to: universal
user-invocable: false
---

# 改前必确认（强约束）

> **核心立场**：即使 1 行修改，也要先让用户看到方案，同意后再改。
> v2 强化"同意"判定——明确列出哪些算同意、哪些不算，避免弱模型把"嗯"当同意。

---

## 为什么必须有这个 skill

1. **弱模型 context 小，边角情况易出错**——先看方案能 catch 错误
2. **开发者对项目最熟**——agent 看局部，用户能给更好方案
3. **防止 agent 自由发挥**——和"最小改动"配合，杜绝"顺手改"
4. **可审计**——所有修改有"方案→同意→执行"记录

---

## 强制流程（7 步）

### Step 1：理解任务
读清楚用户要做什么。不清楚就**先问**（用 AskUserQuestion），不要猜。

### Step 2：定位代码
- 用 `code-locate` 找目标位置
- Read 相关代码块，**只读必须读的**
- 看懂当前实现

### Step 3：设计修改方案
思考：怎么改？改什么文件/方法/行？改动大小？

### Step 4：输出"修改方案"卡片（必须，格式固定）
见 `references/proposal-template.md`。4 章节：
1. **📋 修改方案** — 文件/位置/改前/改后
2. **🤔 修改原因** — 为什么？有别的方案吗？
3. **🎯 影响范围** — 动哪些文件/模块？破坏 build？
4. **⚠️ 风险评估** — 可能引入什么 bug？怎么验证？怎么回滚？

### Step 5：STOP — 等用户确认

**绝对不能调用 Edit/Write/Bash 修改命令**。

### Step 6：判定"同意"（v2 强化，机械可判）

**✅ 算明确同意**（可执行）：
- "同意" / "好" / "可以" / "行" / "OK" / "ok" / "go" / "改吧" / "确认" / "没问题" / "就这样"

**❌ 不算同意**（继续等，不执行）：
- "嗯" / "哦" / "看看" / "我想想" / "等一下" / "再看看"
- 换方案 / 再详细点 / 只改 A 不改 B
- 沉默（没回复）

> **弱模型保护**：拿不准算不算同意 → **默认不算，继续等或反问**"确认执行吗?"

### Step 7：用户同意后执行
- Edit 优先，Write 慎用
- 一次只改一个文件
- 改完跑 `verify-loop` 验证
- 改完跑 `project-memory sync` 更新索引
- 改完简短回报

---

## 不触发的场景

- ✓ 纯查询："XX 在哪里""为什么这样"
- ✓ 解释代码："这段什么意思"
- ✓ 代码 review："看看有什么问题"
- ✓ 测试/构建命令："跑一下测试"

---

## 用户明确豁免时

用户**明确**说以下之一，可跳过确认：
- "直接改" / "不用问" / "你看着办" / "小事改吧"

但：
- **仍要改完简短说明**改了什么、为什么
- **仍受** `coding-rules` 最小改动约束
- **仍要走** `verify-loop` 验证（除非用户也说"不用验证"）

### "小事"判定标准（agent 自判）
- 单文件 + < `confirm_first.small_change_lines`（默认 5 行）+ 不动 manifest/gradle/aidl = 真"小事"，可直接改
- 多文件 / 动核心逻辑 / 改 manifest = **不是小事**，仍需确认

> `always_confirm_exts`（.xml/.gradle/.kts/.aidl/.sh）里的文件，**无论多小都要确认**。

---

## 完整对话示例

```
👤 用户: 帮我把 launcher 启动慢的问题修一下

🤖 agent: (用 code-locate 定位 Launcher.onCreate)
          (Read Launcher.java onCreate, line 42-58)

🤖 agent: 📋 修改方案
【文件】packages/apps/Launcher3/src/.../Launcher.java
【位置】onCreate(), line 42-58
【改前】LauncherModel.loadWorkspace(this);  // 同步加载
【改后】LauncherModel.loadWorkspaceAsync(this, () -> workspace.refresh());
【改动】2 行

🤔 修改原因
  onCreate 同步 loadWorkspace 阻塞主线程 200-500ms，导致首屏白屏。
  方案A(推荐): 异步加载，首屏先渲染壳
  方案B: WorkerThread，改 LauncherModel 接口，范围大
  选A：改最小，用户感知最直接。

🎯 影响范围
  - 只改 Launcher.java 一个方法
  - 不改 LauncherModel 内部（已有 async 接口）
  - 不动 Manifest、不动依赖

⚠️ 风险评估
  风险: 中。首屏可能短暂显示空 workspace
  验证: 1) ./gradlew :Launcher3:assembleDebug  2) adb install + 重启  3) adb logcat 看 onCreate 耗时
  回滚: 单文件，git revert

  同意我就改?

👤 用户: 同意

🤖 agent: 好的，正在改...
(Edit)
已改 Launcher.java onCreate，改动 2 行。
建议跑 verify-loop 验证 + project-memory sync 更新索引。
```

---

## 错误示范（agent 禁止）

- ❌ 默默改完才汇报
- ❌ 方案含糊："我准备改一下这个文件"（立刻改）
- ❌ "我准备改" 后立刻跟 Edit（不等回复）
- ❌ 用户说"小修一下"就跳过所有确认（重命名可能影响 50 处调用）
- ❌ 改了用户没要求的"相关"代码
- ❌ 把"嗯"当同意直接改

---

## 与其他 skill 的关系

```
用户提需求
   ↓
[code-locate] 定位代码
   ↓
[confirm-first] 输出方案 + 等确认 ← 本 skill
   ↓
用户明确同意
   ↓
[coding-rules] 风格 + 最小改动检查
[arch-rules] 架构合规检查
   ↓
执行 Edit
   ↓
[_harness] diff_guard + ai_smell 机械检查
   ↓
[verify-loop] 验证闭环
   ↓
[project-memory sync] 更新索引
```
