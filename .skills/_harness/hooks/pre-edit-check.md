# Pre-Edit Hook 模板（Edit 前拦截）

> 在 agent 调用 Edit/Write 之前注入检查，防止"没确认就改""改动范围超限"。
> 平台原生支持：Claude Code / ZCode。其他平台降级为文档约束。

## 检查内容（PreToolUse）

Edit/Write 被调用前，hook 脚本检查：

1. **是否已确认**：本次修改任务是否已走 `confirm-first`（有方案 + 用户同意）
   - 检查方式：会话内是否出现过"📋 修改方案"且用户回复过"同意/好/go"
   - 未确认 → 阻止 Edit，提示"请先输出修改方案并等待确认"

2. **改动范围预检**：目标文件是否在 `always_confirm_exts`（manifest/gradle/aidl）
   - 命中 → 即使小改动也强制确认

3. **.agent/project-map.md 是否存在**：若不存在，提示"建议先跑 project-memory 梳理项目"

## Claude Code 配置

```json
{
  "hooks": {
    "PreToolUse": [{
      "matcher": "Edit|Write",
      "hooks": [{
        "type": "command",
        "command": "bash .skills/_harness/scripts/diff_guard.sh --pre"
      }]
    }]
  }
}
```

## 无原生 hook 的平台

在 AGENTS.md / .traerules / .cursorrules 里强约束：
```markdown
## 不可跳过的流程
调用 Edit/Write 前，必须：
1. 已输出"📋 修改方案"四章节
2. 已收到用户明确同意（"同意/好/可以/go/行/OK"）
3. 若改 manifest/gradle/aidl，必须额外确认
未满足任一 → 不要调用 Edit/Write，先补方案。
```
