---
name: project-memory
description: 项目记忆系统。首次进入项目时生成 .agent/project-map.md + .agent/project-skill.md + .agent/progress.json（explore 模式）；长周期开发中基于 git diff 增量更新（sync 模式）。当用户说"熟悉项目""梳理项目结构""进入新项目""同步项目地图""更新项目记忆""代码改了同步一下"时触发。让 agent 不每次重新扫描，而是维护一份结构化、低 token、可增量更新的项目记忆。
applies_to: universal
allowed-tools:
  - Read
  - Glob
  - Grep
  - Edit
  - Write
  - Bash
argument-hint: [模式: explore|sync，默认自动判断]
---

# 项目记忆系统

> **核心思想**（来自 harness engineering）：不让 LLM 每次重新扫描，而是维护一份**结构化、低 token、可增量更新**的项目记忆。所有 agent 任务开始前先读它。
>
> 合并 v1 的 android-explore + android-sync，用**稳定锚点 ID** 替代易变的 §编号，新增 **JSON 会话间记忆**。

---

## 两个模式

| 模式 | 触发 | 做什么 |
|------|------|--------|
| **explore** | 首次进项目 / `.agent/project-map.md` 不存在 / 重大重构 | 全量扫描，生成 `.agent/project-map.md` + `.agent/project-skill.md` + `.agent/progress.json` |
| **sync** | 提交后 / PR 合并前 / 改了 Manifest/AIDL/Gradle | 基于 git diff 增量更新，只 Edit 受影响区块 |

**自动判断**：`.agent/project-map.md` 不存在 → explore；存在 → sync。

---

## 核心原则（节省 token 不降智）

1. **shell 做机械活，LLM 只做推理**：扫描、解析、统计交给 `scripts/inspect.sh`，LLM 时间省给推理
2. **只记路径和标识符，不存代码片段**：`.agent/project-map.md` 目标 < 1000 行 / < 30k token
3. **结构化优于叙述**：Mermaid 图 + 表格代替散文
4. **增量 > 全量**：sync 只动 diff 影响的区块，不重写整个文件
5. **用锚点不用编号**：引用用 `<a id="sec-entry">` 锚点名，不用 `§3.2`（编号重排会断链）

---

## explore 模式流程

### Step 1：检测项目类型（脚本干）
运行 `bash $SKILL_DIR/scripts/inspect.sh <project_root>`，输出分块结构化数据：
- 项目类型（AOSP 整包 / 多模块 Gradle / 单应用 / 混合）
- Gradle 模块列表
- AndroidManifest.xml 关键声明
- AIDL 文件清单
- 车机特征评分（检测 CarAppLib / Launcher3 / SettingsLib）
- 代码规模统计（Kotlin/Java/CPP 行数与比例）
- Intent Filter / 系统权限 / 核心入口提示

> 脚本已修复 v1 的 typo bug（`AUTOMOMOTIVE_HITS`）并加 `head` 截断防爆 token。

### Step 2：根据项目类型定位核心入口
读 `skills.config.yaml` 的 `project_type`（automotive/mobile/universal），按类型加载对应 conventions：
- automotive：找 Launcher/Settings/CarService 入口
- mobile：找 Activity/Application/ViewModel 入口
- universal：找主要 Activity/Service/Provider 入口

### Step 3：识别模块边界和 IPC 接口
- Gradle 多模块：读 `settings.gradle.kts` 识别拓扑
- AIDL 跨进程：扫描 `.aidl` 文件，记录 package 路径
- Car API（仅 automotive）：扫描 `android.car.*` 引用
- 系统权限：扫描 `uses-permission` 和 sharedUserId

### Step 4：抽取核心业务流程
- Grep `Intent.ACTION_*` / `IntentFilter` 注册
- 找主要 Activity / Service / Provider 入口
- 输出 1-3 条主业务流程（Mermaid 时序图）

### Step 5：生成三个文件

**a) `.agent/project-map.md`** — 用 `templates/project-map.template.md`，**必须用锚点 ID**：
```markdown
<a id="sec-meta"></a>
## 1. 项目元信息
...
<a id="sec-entry"></a>
## 2. 核心入口
...
```
其他 skill 引用本文件时用 `[入口](.agent/project-map.md#sec-entry)`，不用 `§2`。

**b) `.agent/project-skill.md`** — 用 `templates/project-skill.template.md`，合并原 AGENTS.md 的"工作流入口"内容 + 项目级 skill 行为定制（启用列表 / 项目特殊约束）。

**c) `.agent/progress.json`** — 用 `templates/progress.json`，记录会话间状态（JSON 优于 Markdown，agent 不易误改结构化数据）。

### Step 6：放置位置（统一在 .agent/ 下）
- `<project_root>/.agent/project-map.md`
- `<project_root>/.agent/project-skill.md`
- `<project_root>/.agent/progress.json`

---

## sync 模式流程

### Step 1：读 .agent/project-map.md 元信息（只读必要部分）
用 Read + offset/limit 读顶部元信息 + 变更日志区，不读全文。

### Step 2：获取 diff 范围
默认 `git diff HEAD~1..HEAD --name-status`。读 `.agent/progress.json` 里记录的 `last_commit`，从该 sha 开始。

### Step 3：解析 diff 影响（脚本干）
运行 `bash $SKILL_DIR/scripts/sync.sh <diff_range>`，输出：
- 受影响文件清单（A/M/D/R）
- 影响区域识别（manifest/aidl/gradle/入口/IPC/权限）
- 推荐更新的锚点区块

### Step 4：精准 Edit 受影响区块
按脚本输出的"推荐更新"，**只 Edit 对应锚点区块**。每个 Edit 只动一个表格行或一个区块，不重写整个章节。

常见映射：
- 新增/删除 Activity/Service → 更新 `#sec-entry`
- AIDL 改动 → 更新 `#sec-ipc`
- Manifest 权限变化 → 更新 `#sec-permission`
- Gradle 模块变化 → 更新 `#sec-modules`
- Car API 新引用 → 更新 `#sec-ipc`（仅 automotive）

### Step 5：更新元信息
修改顶部 `> 最后更新: 时间 · git commit: sha`

### Step 6：追加变更日志
在变更日志区**顶部**插入新行（不修改历史）：
```markdown
| 2026-06-24 | a1b2c3d | 新增类 | #sec-entry | 新增 FooActivity 入口 |
```

### Step 7：更新 .agent/progress.json
更新 `last_commit`、`last_sync`、`changes_count`。

### Step 8：大改动提示
若单次 diff > `thresholds.sync_large_change_files`（默认 20）个核心文件，提示：
> ⚠️ 改动较大，建议跑 explore 模式重新梳理，而非增量更新。

---

## 不要做的事

- ❌ 复制粘贴大段源码到 `.agent/project-map.md`
- ❌ 让 `.agent/project-map.md` 超过 `thresholds.project_map_max_lines`（默认 1000 行）
- ❌ sync 时重新跑全量 inspect.sh（那是 explore 的活）
- ❌ 重写 `.agent/project-map.md` 整个文件（只 Edit 受影响区块）
- ❌ 用 §编号引用（用锚点 ID）
- ❌ 把 diff 内容贴进 `.agent/project-map.md`（只记路径和摘要）
- ❌ 修改变更日志已有行（只 append/顶部插入）

---

## 会话间记忆（.agent/progress.json）

> 来自 Anthropic 实践：JSON 优于 Markdown，agent 不易误改结构化数据。

`.agent/progress.json` 记录跨会话状态，每个新会话的 agent 先读它了解"上次做到哪"：
- `last_commit`：上次同步到的 git commit
- `last_sync`：上次同步时间
- `known_unknowns`：agent 识别错误需手动修正的项
- `changes_count`：累计变更次数
- `verify_stats`：验证通过/失败统计（供 trace 驱动改进）

---

## 与其他 skill 的关系

| Skill | 关系 |
|-------|------|
| `code-locate` | 依赖本 skill 生成的 `.agent/project-map.md` 索引 |
| `arch-rules` | 引用 `.agent/project-map.md` 的 `#sec-permission` 确认签名 |
| `confirm-first` | 改完代码后触发 sync 更新索引 |
| `verify-loop` | 验证通过后才 sync（避免同步错误代码） |

**完整工作流**：
```
首次: explore → 生成 .agent/project-map.md + .agent/project-skill.md + .agent/progress.json
日常: 定位(10) → 确认(40) → 改代码 → 验证(50) → sync更新索引
```
