# Post-Edit Hook 模板（Edit 后验证）

> Edit/Write 执行后，强制跑检查脚本 + 提示进入 verify-loop。
> 把"改完自检"从自觉变成机械执行。

## 检查内容（PostToolUse）

Edit/Write 完成后，hook 自动执行：

1. **`diff_guard.sh`** — 检查本次改动有无"用户没要求的改动"（空行/import/多文件）
2. **`ai_smell_check.sh <改的文件>`** — 扫描新增注释有无 AI 味短语
3. **提示 verify-loop** — 提醒"改完请跑 verify-loop 验证"
4. **提示 sync** — 若新增/删除了类，提醒"建议跑 project-memory sync 更新索引"

## Claude Code 配置

```json
{
  "hooks": {
    "PostToolUse": [{
      "matcher": "Edit|Write",
      "hooks": [{
        "type": "command",
        "command": "bash .skills/_harness/scripts/ai_smell_check.sh \"$CLAUDE_FILE_PATH\" && bash .skills/_harness/scripts/diff_guard.sh"
      }]
    }]
  }
}
```

## 无原生 hook 的平台

AGENTS.md 强约束：
```markdown
## Edit/Write 后必做（不可跳过）
每次改完代码，必须依次执行：
1. `bash .skills/_harness/scripts/diff_guard.sh`
   → 有违规则 git checkout 撤回，修正后再改
2. `bash .skills/_harness/scripts/ai_smell_check.sh <刚改的文件>`
   → 有 AI 味则删除注释，重新 Edit
3. 跑 verify-loop 验证（构建/测试）
4. 若新增/删除类，跑 project-memory sync 更新 .agent/project-map.md
```

## 失败处理

- diff_guard 报错 → 不许进入下一步，必须先撤回无关改动
- ai_smell_check 报错 → 必须删除 AI 味注释后重新 Edit
- verify 失败 → 进入 verify-loop 的修复循环（最多 max_retries 次）
