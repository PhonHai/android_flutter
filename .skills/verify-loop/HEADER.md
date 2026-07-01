---
name: verify-loop
description: 闭环验证。改完代码后强制构建+测试+对照原始需求比较，失败则修复重试。任何 Edit/Write 代码后自动触发（除非用户豁免）。max_retries: 2（config 可配）。
applies_to: universal
user-invocable: false
header_only: true
---

# verify-loop · HEADER

**ROLE**：Build → 读完整输出 → 对照需求比较 → 失败则修复重试（最多 2 次）。

**WHEN**：任何 Edit/Write 代码后自动触发。用户说"跳过验证"/"不检查"/"别跑构建"时豁免。

**SCRIPTS**：`sk diff-guard post`（最小改动校验）/ 构建命令从 `skills.config.yaml` 读取（默认 `./gradlew assembleDebug`）。

**LOAD SKILL.md**：修改代码完成、准备跑构建时。小改动/文档改动不加载。
