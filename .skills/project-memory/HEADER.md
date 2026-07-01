---
name: project-memory
description: 项目记忆。explore=全量扫描生成 .agent/ 三件套，sync=git diff 增量更新。触发词：梳理项目 / 同步项目 / 熟悉项目 / 更新项目记忆。
applies_to: universal
allowed-tools: [Read, Glob, Grep, Edit, Write, Bash]
argument-hint: "[explore|sync]"
header_only: true
---

# project-memory · HEADER

**ROLE**：维护结构化项目记忆（project-map + project-skill + progress.json），agent 不每次重新扫描。

**WHEN**：首次进项目 / 用户说"梳理项目" → explore。改代码后用户说"同步项目" → sync。

**SCRIPTS**：`sk inspect [root]`（全量）/ `sk sync [range]`（增量）/ `sk anchor [map]`（校验锚点）。

**LOAD SKILL.md**：真正执行 explore/sync 时。纯问答/读代码不加载。
