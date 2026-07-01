---
name: git-commit
description: Conventional Commits 规范提交。分析实际 diff 自动判定 type/scope/description，智能分组暂存。车机安全红线：绝不提交 .keystore/.pk8/平台密钥。触发词：提交/commit。verify-loop 通过后自动进入。
applies_to: universal
allowed-tools: [Bash, Read]
user-invocable: true
header_only: true
---

# git-commit · HEADER

**ROLE**：生成 Conventional Commits 格式的 commit message + 执行 git commit。自动分析 diff 判定 type/scope。

**WHEN**：用户说"提交"/"commit" / verify-loop 通过后自动进入。

**SAFETY**：提交前必须跑 `sk commit-safety` 扫描暂存区，拦截 `.keystore`/`.pk8`/平台签名/密钥文件。

**LOAD SKILL.md**：用户明确要提交时。不应在开发中途加载。
