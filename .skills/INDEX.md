# Skills Runtime Index — PomodoroNative

> 10 个 Skill，位于 `.skills/` 目录下。面向 Android 手机端（mobile）。

## 行为约束 Skill（8 个）

| # | Skill | 作用 | 触发方式 |
|---|-------|------|---------|
| 1 | **project-memory** | 维护 `.agent/` 项目记忆 | 说"梳理项目/同步项目" |
| 2 | **code-locate** | 查 project-map 索引定位代码 | 输入含类名/文件名自动 |
| 3 | **arch-rules** | 架构约束（线程/ANR/导航/数据层/混合通信） | 改 Manifest/Gradle/系统层自动 |
| 4 | **confirm-first** | 改前输出方案卡等同意 | 任何修改前自动 |
| 5 | **lazy-build** | 懒人阶梯 YAGNI→复用→最少代码 | 说"懒人模式" / 写新代码时 |
| 6 | **coding-rules** | 代码风格 + 最小改动 + 去 AI 味 | Edit/Write 代码自动 |
| 7 | **verify-loop** | 改完自动校验 | 改完代码后自动 |
| 8 | **git-commit** | Conventional Commits + 安全扫描 | 说"提交" |

## 最佳实践 Skill（2 个）

| # | Skill | 内容 | 来源 |
|---|-------|------|------|
| 9 | **jetpack-compose-mvvm** | UDF / StateFlow+ViewModel / Sealed Class / Side Effects | [Google Compose UI Architecture](https://developer.android.com/develop/ui/compose/architecture) |
| 10 | **flutter-best-practices** | 分层 / MVVM / Repository / 不可变数据 / DI / 测试 | [Flutter Architecture Recommendations](https://docs.flutter.dev/app-architecture/recommendations) |

## 触发词速查

| 你说的 | 发生什么 |
|--------|---------|
| **梳理项目** | 全量扫描，生成 `.agent/project-map.md` + 进度文件 |
| **同步项目** | git diff 增量更新 project-map |
| **提交** | Conventional Commits 规范提交 + 安全扫描 |
| **懒人模式** | lazy-build full 档：阶梯强制执行 |
| **懒人lite** | 写完提供更懒的替代方案 |
| **懒人ultra** | YAGNI 极端主义 |
| **停止懒人** | 关闭 lazy-build |

## 目录结构

```
.skills/
├── arch-rules/               # 架构约束（universal + mobile + flutter-hybrid）
│   └── conventions/
├── jetpack-compose-mvvm/     # Compose + MVVM 最佳实践
├── flutter-best-practices/   # Flutter 架构最佳实践
├── code-locate/              # 代码定位
├── coding-rules/             # 编码规范
├── confirm-first/            # 改前确认
├── git-commit/               # 提交规范
├── lazy-build/               # 懒人模式
├── project-memory/           # 项目记忆
├── verify-loop/              # 闭环验证
├── _harness/                 # 强制检查脚本
│   ├── scripts/              # sk.sh + diff_guard + ai_smell + anchor
│   └── hooks/
├── templates/                # project-map / progress / skill 模板
├── INDEX.md                  # 本文件
└── skills.config.yaml        # 项目配置
```

> 每个 skill 首次加载只读 HEADER.md（~1KB），用到完整规则时才读 SKILL.md。
