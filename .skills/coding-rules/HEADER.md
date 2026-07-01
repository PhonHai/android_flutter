---
name: coding-rules
description: 强制代码纪律。三大约束：(1) Kotlin Google Style + Java 阿里手册 (2) 最小改动范围：只改用户要求的，禁重构/禁改格式/禁修旁bug (3) 注释去AI味：只写代码本身做什么，禁对话/历史/AI思考。任何 Edit/Write .kt/.java 自动加载。
applies_to: universal
user-invocable: false
header_only: true
---

# coding-rules · HEADER

**ROLE**：决定"怎么写代码"——风格 + 最小改动 + 去 AI 味。

**WHEN**：任何准备 Edit/Write `.kt`/`.java`/`.kts` 时自动加载。

**CONSTRAINTS**：
1. 风格：Kotlin=Google Style（4空格/命名/import），Java=阿里手册
2. 最小改动：只动用户明确要求的，不动注释/格式/重构/旁bug
3. 去AI味：注释只写"是什么"，不写"为什么这么做/之前试过什么"

**SCRIPTS**：`sk diff-guard`（最小改动校验）/ `sk ai-smell <file>`（AI味检查）。

**LOAD SKILL.md**：确认要 Edit/Write 代码时。方案阶段只看 HEADER。
