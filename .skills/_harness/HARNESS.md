# Harness 强制层 — 把"自觉执行"变成"机械拦截"

> **v1 的核心缺陷**：所有约束（改前6问、触发决策树、去AI味）都靠 agent 读文档自觉执行，弱模型会跳过。
> **v2 的修复**：增加本层，用脚本和 hook 在 Edit/Write 前后强制检查，agent 跳过自检也会被拦下。

---

## 一、Harness 是什么

来自 Claude Code 源码逆向和 OpenAI/Stripe 实践：

> "The model is the agent. The code is the harness. Build great harnesses."
> 模型是 agent，**代码是缰绳**。缰绳越好，agent 越稳。

harness = 工具 + 权限 + hooks + 沙盒 + 记忆 + 检查脚本。本目录是 skills 套件的"缰绳层"。

---

## 二、三层防御（参考 Claude Code 六层安全模型简化）

| 层 | 机制 | 遵守率 | 平台支持 |
|----|------|--------|---------|
| 1 | **文档约束**（SKILL.md 里的规则） | ~90%（弱模型更低） | 所有平台 |
| 2 | **Hook 拦截**（PreToolUse/PostToolUse） | ~99% | Claude/ZCode 原生；Trae/Cursor 降级 |
| 3 | **脚本检查**（本目录 scripts/） | 100%（脚本必跑） | 所有平台（agent 调用） |

**设计原则**：层 1 靠 agent 自觉，层 2 靠平台 hook，层 3 靠脚本兜底。三层叠加，弱模型也跑不偏。

---

## 三、检查脚本说明

### `scripts/diff_guard.sh` — 最小改动守卫
- **何时跑**：Edit/Write 之后（PostToolUse）
- **做什么**：对比 `git diff`，检查是否有"用户没要求的改动"（无关 import/格式/注释）
- **输出**：违规清单 + 风险等级
- **用法**：`bash .skills/_harness/scripts/diff_guard.sh`

### `scripts/ai_smell_check.sh` — AI 味注释检查
- **何时跑**：Edit/Write 之后
- **做什么**：扫描改动文件，找"我们决定""用户要求""我加了""重构自"等 AI 味短语
- **输出**：命中的行 + 建议删除
- **用法**：`bash .skills/_harness/scripts/ai_smell_check.sh <file>`

### `scripts/anchor_check.sh` — 锚点引用校验
- **何时跑**：维护 .agent/project-map.md 时
- **做什么**：检查 skill 文档里引用的锚点 ID 在 .agent/project-map.md 里是否存在
- **用法**：`bash .skills/_harness/scripts/anchor_check.sh`

---

## 四、配置 Hook（按平台）

### Claude Code（原生支持，最强）
编辑 `.claude/settings.json`：
```json
{
  "hooks": {
    "PreToolUse": [{
      "matcher": "Edit|Write",
      "hooks": [{"type": "command", "command": "bash .skills/_harness/scripts/diff_guard.sh --pre"}]
    }],
    "PostToolUse": [{
      "matcher": "Edit|Write",
      "hooks": [{"type": "command", "command": "bash .skills/_harness/scripts/ai_smell_check.sh \"$CLAUDE_FILE_PATH\""}]
    }]
  }
}
```

### ZCode
参考 ZCode 文档的 Tool Hook 配置，接入同样脚本。

### Trae / Cursor（无原生 hook）
在项目 rule 里强约束：
```markdown
## 强制流程（不可跳过）
每次 Edit/Write 代码后，必须执行：
1. `bash .skills/_harness/scripts/diff_guard.sh` — 检查改动范围
2. `bash .skills/_harness/scripts/ai_smell_check.sh <改的文件>` — 检查注释
任一脚本报错，必须修正后再继续。
```
弱模型可能跳过，靠 `verify-loop` 兜底。

### 通用兜底
AGENTS.md 里写死流程，见 `hooks/post-edit-verify.md`。

---

## 五、harness 配置开关

在 `skills.config.yaml` 控制：
```yaml
harness:
  diff_guard: true      # 开启最小改动检查
  ai_smell_check: true  # 开启 AI 味检查
  anchor_check: false   # 仅维护 .agent/project-map.md 时开
```
脚本启动时读 config，关闭的检查直接跳过。

---

## 六、Trace 驱动迭代（harness engineering 核心理念）

> "Harness engineering is not vibes-based." — 不是凭感觉，靠度量。

建议维护以下指标（记在 `progress.json`）：
- **改动范围违规率**：diff_guard 命中次数 / 总 Edit 次数
- **AI 味残留率**：ai_smell_check 命中次数 / 总文件数
- **验证通过率**：verify-loop 一次通过 / 总修改任务
- **回滚次数**：验证失败回滚的次数

指标变差 → 往 AGENTS.md 加规则（免疫系统机制）。指标变好 → harness 有效。

---

## 七、何时不该过度 harness

> "如果 harness 持续变复杂，是过度工程化的信号。" — Manus 6个月重写5次，方向是简化。

- 小项目（< 1万行）：开 `diff_guard` + `ai_smell_check` 即可，verify-loop 选配
- 强模型（Claude/GPT）：可放宽 confirm-first，保留 verify-loop
- 弱模型：全开，尤其 strict 模式 + 强制确认
