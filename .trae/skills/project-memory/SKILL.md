---
name: "project-memory"
description: "维护项目结构化记忆（project-map + project-skill + progress.json），explore 全量扫描生成，sync 基于 git diff 增量更新。触发词：梳理项目/同步项目/熟悉项目/更新项目记忆。"
---

# 项目记忆系统

**ROLE**：维护结构化项目记忆，agent 不每次重新扫描。

**WHEN**：首次进项目 / 用户说"梳理项目" → explore。改代码后用户说"同步项目" → sync。

**SCRIPTS**：`sk inspect [root]`（全量）/ `sk sync [range]`（增量）/ `sk anchor [map]`（校验锚点）。

> **核心思想**（来自 harness engineering）：不让 LLM 每次重新扫描，而是维护一份**结构化、低 token、可增量更新**的项目记忆。所有 agent 任务开始前先读它。

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
运行 `bash $SKILL_DIR/scripts/inspect.sh <project_root>`，输出分块结构化数据。

### Step 2：根据项目类型定位核心入口
读 `skills.config.yaml` 的 `project_type`，按类型加载对应 conventions。

### Step 3：识别模块边界和 IPC 接口
- Gradle 多模块：读 `settings.gradle.kts` 识别拓扑
- AIDL 跨进程：扫描 `.aidl` 文件
- 系统权限：扫描 `uses-permission` 和 sharedUserId

### Step 4：抽取核心业务流程
找主要 Activity / Service / Provider 入口，输出 1-3 条主业务流程（Mermaid 时序图）。

### Step 5：生成三个文件
- `.agent/project-map.md` — 用 `templates/project-map.template.md`，**必须用锚点 ID**
- `.agent/project-skill.md` — 用 `templates/project-skill.template.md`
- `.agent/progress.json` — 用 `templates/progress.json`，记录会话间状态

### Step 6：放置位置
- `<project_root>/.agent/project-map.md`
- `<project_root>/.agent/project-skill.md`
- `<project_root>/.agent/progress.json`

---

## sync 模式流程

1. 读 `.agent/project-map.md` 元信息（只读必要部分）
2. 获取 diff 范围：默认 `git diff HEAD~1..HEAD --name-status`
3. 解析 diff 影响：运行 `bash $SKILL_DIR/scripts/sync.sh <diff_range>`
4. 精准 Edit 受影响区块：只 Edit 对应锚点区块
5. 更新元信息：修改顶部时间戳和 commit sha
6. 追加变更日志：在变更日志区**顶部**插入新行
7. 更新 `.agent/progress.json`：更新 `last_commit`、`last_sync`、`changes_count`
8. 大改动提示：若单次 diff > 20 个核心文件，提示建议跑 explore

---

## 不要做的事

- ❌ 复制粘贴大段源码到 `.agent/project-map.md`
- ❌ 让 `.agent/project-map.md` 超过 1000 行
- ❌ sync 时重新跑全量 inspect.sh
- ❌ 重写 `.agent/project-map.md` 整个文件
- ❌ 用 §编号引用（用锚点 ID）
- ❌ 把 diff 内容贴进 `.agent/project-map.md`
- ❌ 修改变更日志已有行

---

## 会话间记忆（.agent/progress.json）

记录跨会话状态：`last_commit`、`last_sync`、`known_unknowns`、`changes_count`、`verify_stats`。

---

## 与其他 skill 的关系

| Skill | 关系 |
|-------|------|
| `code-locate` | 依赖本 skill 生成的 `.agent/project-map.md` 索引 |
| `arch-rules` | 引用 `.agent/project-map.md` 的权限锚点 |
| `confirm-first` | 改完代码后触发 sync 更新索引 |
| `verify-loop` | 验证通过后才 sync |

**完整工作流**：
```
首次: explore → 生成 .agent/ 三件套
日常: 定位 → 确认 → 改代码 → 验证 → sync 更新索引
```