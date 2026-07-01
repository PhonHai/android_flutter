---
name: git-commit
description: 用 Conventional Commits 规范执行 git 提交。分析实际 diff 自动判定 type/scope/description，智能分组暂存，绝不提交密钥。用户说"提交""commit""/commit"时触发；verify-loop 通过后自动进入。支持 conventional commit 格式 + breaking change + 安全协议。Android/车机特有：系统签名文件/密钥库/平台密钥绝不提交。
applies_to: universal
allowed-tools:
  - Bash
argument-hint: "[可选: type|scope|description 覆盖]"
---

# Git 提交（Conventional Commits）

> ship 阶段的 skill。verify-loop 通过后，把改动用规范的 commit message 提交。
>
> 来自用户提供的 git-commit skill，适配 Android/车机安全红线。

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

## Breaking Changes

```bash
# type/scope 后加感叹号
feat!: remove deprecated endpoint

# 或用 BREAKING CHANGE footer
feat: allow config to extend other configs

BREAKING CHANGE: `extends` key behavior changed
```

---

## 工作流

### Step 1：分析 diff

```bash
# 有暂存就用暂存的 diff
git diff --staged

# 没暂存就用工作区 diff
git diff

# 同时看状态
git status --porcelain
```

### Step 2：暂存文件（按需）

如果没暂存，或想按逻辑分组：

```bash
# 暂存指定文件
git add path/to/file1 path/to/file2

# 按模式暂存
git add *.kt

# 交互式暂存
git add -p
```

**一个逻辑改动一个 commit**。lazy-build 的最小 diff 原则在这里继续生效：别把不相关的改动塞一个 commit。

### Step 3：生成 commit message

分析 diff 决定：
- **Type**：这是什么改动？
- **Scope**：影响哪个模块/区域？（车机项目常见 scope：`launcher`/`settings`/`car-service`/`aidl`/`manifest`/`gradle`）
- **Description**：一行说清改了什么（现在时、祈使语气、<72 字符）

### Step 4：执行提交

```bash
# 单行
git commit -m "<type>[scope]: <description>"

# 多行带 body/footer
git commit -m "$(cat <<'EOF'
<type>[scope]: <description>

<可选 body>

<可选 footer>
EOF
)"
```

---

## Android/车机安全红线（绝不提交）

> 这一层是本 skill 在通用 git-commit 基础上加的，车机/系统应用项目尤其重要。

**提交前必须检查工作区/暂存区没有以下文件**：

| 类型 | 文件 | 原因 |
|------|------|------|
| 签名密钥 | `*.keystore` `*.jks` `*.pem` `*.p12` | 泄露=别人能伪造你的系统签名 |
| 平台密钥 | `platform.pk8` `platform.x509.pem` `shared.pk8` | AOSP 平台签名泄露=红线 |
| 凭证 | `credentials.json` `secrets.xml` `.env` | 密钥/密码 |
| 本地配置 | `local.properties` `gradle.properties`（含 key 别名密码） | 含签名密码 |
| 临时/构建 | `build/` `.gradle/` `*.apk` `*.aab` | 二进制不该进 git |
| IDE | `.idea/` `*.iml` `.DS_Store` | 个人配置 |

### 检查命令（提交前自动跑）

```bash
# 扫暂存区有无危险文件
git diff --staged --name-only | grep -iE '\.(keystore|jks|pem|p12|pk8)$|credentials|secrets|\.env$|local\.properties|platform\.' && {
  echo "⚠️ 暂存区含敏感文件，中止提交"
  exit 1
}
```

如果命中，**中止提交**，告诉用户哪些文件危险，让用户 `git restore --staged <file>` 取消暂存。

### .gitignore 建议

如果项目根没有 `.gitignore` 或缺少上述项，提醒用户补充：
```
*.keystore
*.jks
*.pem
*.p12
*.pk8
local.properties
.env
credentials.json
secrets.xml
build/
.gradle/
*.apk
*.aab
.idea/
*.iml
.DS_Store
```

---

## Best Practices

- **一个逻辑改动一个 commit**：lazy-build 改的 + 顺手修的别混，分两个 commit
- **现在时**："add" 不是 "added"
- **祈使语气**："fix bug" 不是 "fixes bug"
- **引用 issue**：`Closes #123` / `Refs #456`
- **description < 72 字符**
- **body 解释"为什么"，不是"改了什么"**（diff 已经说了改了什么）

## Git 安全协议（不可违反）

- **绝不**改 git config
- **绝不**无显式要求跑破坏性命令（`--force`、hard reset）
- **绝不**跳过 hook（`--no-verify`）除非用户明确要求
- **绝不**force push 到 main/master
- hook 失败 → 修问题后建**新** commit（别 amend）
- 提交失败 → 读错误信息，别盲目重试

---

## 与其他 skill 的关系

| Skill | 关系 |
|-------|------|
| `verify-loop` | verify 通过后才进本 skill。verify 没过别提交 |
| `lazy-build` | 本 skill 继承最小 diff 原则：一个 commit 一个逻辑改动 |
| `coding-rules` | 提交的代码必须已通过 ai_smell_check 和 diff_guard |
| `project-memory` | 提交成功后提示用户"同步项目"更新 `.agent/project-map.md` |

**完整链路收尾**：
```
verify-loop(验证通过) → git-commit(规范提交) → 提示 project-memory sync
```

---

## 触发

- 用户说"提交" / "commit" / "/commit" → 触发
- **建议**在 verify-loop 通过后再调用本 skill（agent 自觉行为，不是 hook 强制）
  - `skills.config.yaml` 的 `verify_loop.enabled: true` 表示 agent 自觉跑 verify-loop，不是 verify 通过的"硬性"开关
  - 实际工程上：跑完 `sk verify` 或 verify-loop 输出的 `✓ first_pass_ok` 后再调本 skill

提交成功后输出：
```
✓ 已提交: <sha>
  <type>[scope]: <description>
下一步: "同步项目" 更新 .agent/project-map.md
```
