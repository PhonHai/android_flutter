---
name: code-locate
description: 代码定位。输入含代码标识符（.kt/.java/.aidl/类名/方法签名/行号引用）时自动查 .agent/project-map.md 索引。纯中文描述不触发。
applies_to: universal
user-invocable: false
header_only: true
---

# code-locate · HEADER

**ROLE**：读 `.agent/project-map.md` 索引 → 输出定位结果（文件路径 + 锚点 ID），不维护 project-map。

**WHEN**：用户输入含代码文件名/类名/方法签名/AIDL 接口/行号引用时自动触发。纯中文描述反问用户确认范围。

**SCRIPTS**：无独立脚本。依赖 project-memory 产出的 `.agent/project-map.md`。

**LOAD SKILL.md**：需要查索引时。纯问答/已知道文件位置时不加载。
