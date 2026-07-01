<!-- project-skill.md 模板 v2 — 项目级 skill 行为定制 -->
<!-- 放项目 .agent/project-skill.md。agent 每次任务开始前必读本文件。-->
<!-- 合并 v2 原 AGENTS.md 承担的工作流入口内容 + 项目级 skill 行为定制（启用列表 / 项目特殊约束）。-->

# project-skill.md — 项目级 Skill 行为

> 本文件定义"这个项目用 skill 的方式"。agent 每次执行任务前**先读本文件** + 再读 `.agent/project-map.md`，再开始分析代码。
>
> 配合 `.agent/project-map.md`（项目结构地图）+ `.agent/progress.json`（会话间记忆）一起使用。

## 阅读顺序（任何任务开始前）

1. 读本文件（`.agent/project-skill.md`）了解项目级 skill 行为
2. 读 `.agent/project-map.md` 了解项目结构（按需读锚点区块）
3. 读 `.agent/progress.json` 了解上次进度
4. 然后开始分析代码

## 项目基本信息

| 项 | 值 |
|---|---|
| 项目名 | <一句话定位> |
| 类型 | <automotive / mobile / universal> |
| 项目地图 | `.agent/project-map.md` |
| 进度记忆 | `.agent/progress.json` |

## 启用的 skill 列表

| Skill | 是否启用 | 本项目特殊配置 |
|---|---|---|
| project-memory | ✅ 必开 | 探索/同步模式 |
| code-locate | ✅ 必开 | 默认行为 |
| arch-rules | ✅ 按项目类型 | automotive：加载 universal + automotive + launcher + settings + system-app |
| coding-rules | ✅ 全开 | 三大铁律 |
| confirm-first | ✅ 改前必确认 | 默认严格 |
| verify-loop | ✅ 改完必验证 | 见"构建命令" |

## 项目特殊约束（arch-rules + coding-rules 之外的本地补充）

> 这一段是项目级补充，团队规范、签名、CI 等"只有这个项目才有"的规则。

### 签名与权限
- sharedUserId: <android.uid.system 或无>
- 特权权限: <列表>

### 构建命令
- 编译: `<./gradlew assembleDebug>`
- 测试: `<./gradlew :app:test>`
- Lint: `<./gradlew lint>`

### 团队规范
- <团队规范 1>
- <团队规范 2>

### 不允许的依赖/库
- <项目禁止使用的库>

## 任务分流（5 类标准流程）

### A. 查询/定位代码
```
用户问代码位置 → code-locate 判断触发
  → 触发: 读 .agent/project-map.md#sec-entry/#sec-ipc 索引 → Read 精准行 → 回答
  → 不触发: 反问用户确认范围
```

### B. 修改代码（最常用）
```
code-locate 定位 → confirm-first 输出方案+等确认
  → 用户同意 → coding-rules 风格检查 → Edit
  → verify-loop 验证 → project-memory sync 更新索引
```

### C. 解释/理解代码
```
code-locate 定位 → Read 相关代码 → 解释（不修改，不触发 confirm）
```

### D. 提交代码
```
verify-loop 确认验证通过 → git commit
  → project-memory sync 更新 project-map.md + progress.json
```

### E. 新项目/首次进入
```
project-memory explore 模式 → 生成 .agent/project-map.md + .agent/project-skill.md + .agent/progress.json
```

## Skill 触发一览

| Skill | 何时用 | 触发方式 |
|-------|--------|---------|
| project-memory | 首次梳理 / 提交后同步 | explore/sync 模式 |
| code-locate | 任何代码定位 | 自动（代码标识符触发） |
| arch-rules | 改 Launcher/Settings/AIDL/Manifest | 自动加载 |
| coding-rules | 写/改 .kt/.java | 自动加载 |
| confirm-first | 准备修改代码 | 强制（修改任务必走） |
| verify-loop | 改完代码后 | 强制（验证闭环） |

## 关键约束（不可违反）

1. **改前必确认**：任何修改前输出"📋方案/🤔原因/🎯影响/⚠️风险"，等用户同意
2. **最小改动**：只动用户明确要求的，不顺手改无关代码/注释/格式
3. **注释去 AI 味**：注释只写 what/why/约束，不留对话/历史/AI思考痕迹
4. **改完必验证**：跑 verify-loop，对照原始需求而非自己的代码自证
5. **引用用锚点**：引用 `.agent/project-map.md` 用 `#sec-xxx`，不用 §编号

## 长周期维护

- 每周批量 sync 一次 project-map.md
- project-map.md 超 1000 行 → 拆子模块文档，留链接
- progress.json 记录 verify 统计，供 trace 驱动改进

## 紧急豁免

用户明确说"直接改/不用问/你看着办"时，可跳过 confirm，但仍受：
- coding-rules 最小改动约束
- verify-loop 验证（除非用户也说"不用验证"）
