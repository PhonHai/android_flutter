---
name: "git-commit"
description: "Conventional Commits 规范提交。分析 diff 自动判定 type/scope/description，提交前扫描密钥/签名文件。触发词：提交/commit。verify-loop 通过后自动进入。"
---

# Git 提交（Conventional Commits）

**ROLE**：生成 Conventional Commits 格式的 commit message + 执行 git commit。自动分析 diff 判定 type/scope。

**WHEN**：用户说"提交"/"commit" / verify-loop 通过后自动进入。

**SAFETY**：提交前必须扫描暂存区，拦截 `.keystore`/`.pk8`/平台签名/密钥文件。

---

## Conventional Commit 格式

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

## Commit Types

| Type | 用途 |
|------|------|
| `feat` | 新功能 |
| `fix` | bug 修复 |
| `docs` | 仅文档 |
| `style` | 格式/风格（无逻辑改动） |
| `refactor` | 重构（无功能/修复） |
| `perf` | 性能优化 |
| `test` | 加/改测试 |
| `build` | 构建系统/依赖 |
| `ci` | CI/配置 |
| `chore` | 维护/杂项 |
| `revert` | 回滚 |

---

## 工作流

### Step 1：分析 diff
```bash
git diff --staged  # 或 git diff（无暂存时）
git status --porcelain
```

### Step 2：暂存文件（按需）
一个逻辑改动一个 commit。

### Step 3：生成 commit message
分析 diff 决定 Type / Scope / Description（现在时、祈使语气、<72 字符）。

### Step 4：执行提交
```bash
git commit -m "<type>[scope]: <description>"
```

---

## Android 安全红线（绝不提交）

| 类型 | 文件 | 原因 |
|------|------|------|
| 签名密钥 | `*.keystore` `*.jks` `*.pem` `*.p12` | 泄露=别人能伪造签名 |
| 平台密钥 | `platform.pk8` `platform.x509.pem` | 红线 |
| 凭证 | `credentials.json` `secrets.xml` `.env` | 密钥/密码 |
| 本地配置 | `local.properties` | 含签名密码 |
| 临时/构建 | `build/` `.gradle/` `*.apk` `*.aab` | 二进制不该进 git |

### 检查命令（提交前自动跑）
```bash
git diff --staged --name-only | grep -iE '\.(keystore|jks|pem|p12|pk8)$|credentials|secrets|\.env$|local\.properties'
```
命中则中止提交。

---

## Git 安全协议（不可违反）

- **绝不**改 git config
- **绝不**无显式要求跑破坏性命令（`--force`、hard reset）
- **绝不**跳过 hook（`--no-verify`）除非用户明确要求
- **绝不**force push 到 main/master
- 提交失败 → 读错误信息，别盲目重试

---

## 与其他 skill 的关系

```
verify-loop(验证通过) → git-commit(规范提交) → 提示 project-memory sync
```

提交成功后输出：
```
✓ 已提交: <sha>
  <type>[scope]: <description>
下一步: "同步项目" 更新 .agent/project-map.md
```