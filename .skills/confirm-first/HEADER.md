---
name: confirm-first
description: 强制"先确认再动手"。任何准备修改项目代码的任务，必须先输出"修改方案+原因+影响范围+风险"，等用户明确同意后才能 Edit/Write/Bash。纯查询/读代码不触发。
applies_to: universal
user-invocable: false
header_only: true
---

# confirm-first · HEADER

**ROLE**：修改前输出 Proposal（方案+原因+影响+风险）→ 等待用户同意 → 才动手。

**WHEN**：任何准备 Edit/Write/Bash 修改项目代码时自动触发。纯读/解释/问答不触发。单文件 ≤5 行改动豁免（config 可配）。

**OUTPUT**：4 段 Proposal Card（改什么 / 为什么 / 影响范围 / 风险等级）→ 末尾"同意这个方案吗？"

**LOAD SKILL.md**：确认需要出方案卡时（不是所有对话都需要）。读代码/查文档时不加载。
