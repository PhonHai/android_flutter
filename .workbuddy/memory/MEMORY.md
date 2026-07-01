# 项目长期约定

## Skill 执行可靠性
- skill 是文本指令，agent 可能跳过 verify-loop/confirm-first → 用户不可见 = "盲盒"
- 缓解: coding-rules 已加入强制 SOP，verify-loop 已加入关联引用
- 最终兜底: 用户在对话里监督，改完代码追问"构建通过了吗？"
