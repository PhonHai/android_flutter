---
name: lazy-build
description: 精简构建纪律。写代码前走懒人阶梯：YAGNI→复用→stdlib→原生→现有依赖→一行→最小代码。三档：lite/full(默认)/ultra。触发词：懒人模式/懒人lite/懒人ultra/停止懒人/yagni。
applies_to: universal
allowed-tools: [Read, Glob, Grep, Edit, Write, Bash]
argument-hint: "[lite|full|ultra]"
header_only: true
---

# lazy-build · HEADER

**ROLE**：决定"写多少代码"——7 级阶梯从 YAGNI 到最小 diff，bug 治根因，停在第 1 个能解决问题的 rung。

**WHEN**：写新代码前默认 full 档；用户说"懒人模式/lite/ultra"切档，说"停止懒人"关闭。

**INTENSITY**：lite=写+提供更懒替代方案 / full(默认)=阶梯强制执行 / ultra=YAGNI 极端，删除优先于新增。

**RELATION**：confirm-first 输出方案 → lazy-build 决定写多少 → coding-rules 决定怎么写。

**LOAD SKILL.md**：确认要写/改代码时。方案阶段可只看 HEADER。
