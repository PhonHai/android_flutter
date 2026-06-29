# PomodoroNative — 项目级 Skill 行为

> 最后更新: 2026-06-28
>
> 本文件由 project-memory (explore) 自动生成。
> Agent 进项目后先读 `.agent/project-map.md`，再读本文件了解项目级行为定制。

---

## 1. 项目身份

| 属性 | 值 |
|------|-----|
| 项目名称 | PomodoroNative |
| 项目类型 | Flutter + Android 原生混合 (Add-to-App AAR) |
| 主技术栈 | Dart/Riverpod (Flutter) + Kotlin/Compose (Android) |
| 构建入口 | `native-android/.gradlew assembleDebug` |
| 平台 | workbuddy |

---

## 2. Skill 启用列表

所有 8 个 skill 已启用:

| Skill | 状态 | 项目级定制 |
|-------|------|-----------|
| `project-memory` | ✅ | `.agent/project-map.md` 已有初始版本 |
| `code-locate` | ✅ | 查 `.agent/project-map.md` 索引入口 |
| `arch-rules` | ✅ | flutter + universal conventions |
| `coding-rules` | ✅ | Dart/Kotlin 风格 + no-ai-smell |
| `confirm-first` | ✅ | `.dart/.yaml/.json/.sh` 强制确认 |
| `lazy-build` | ✅ | `flutter build apk --debug` |
| `verify-loop` | ✅ | 构建验证, 最多重试 2 次 |
| `git-commit` | ✅ | 提交前 `sk commit-safety` |

---

## 3. 项目特殊约束

### 架构约束
- **双层代码库**: `flutter_module/` 和 `native-android/` 分属两套技术栈，修改需同时考虑两侧影响
- **MethodChannel 协议**: 修改 `com.pomodoro/history` 的信道方法时，必须同步更新 Flutter `method_channels.dart` 和原生 `MethodChannelHandler.kt`
- **构建入口唯一**: 始终从 `native-android/` 目录执行 Gradle，Flutter 模块是 AAR 嵌入
- **无原生导航**: Flutter 模块内部不应添加 Navigator.push，页面级导航走原生 Activity

### 代码风格
- Dart: `flutter_lints ^6.0.0` 全量规则
- Kotlin: `kotlin.code.style=official`
- 务必运行 `flutter analyze` (Flutter 侧) + native-android AS Sync (原生侧) 验证

### 数据流
- Week 1 策略: sqflite 在 Flutter 侧直接持久化，MethodChannel 仅占位
- Week 2 计划: 引入 Room 替代 sqflite，数据流改为 Flutter → MethodChannel → Room

---

## 4. 完整性要求

修改代码时必做的检查:
1. `flutter analyze` 0 issues
2. `flutter test` 全量通过
3. `native-android/.gradlew assembleDebug` BUILD SUCCESSFUL
4. 如有 MethodChannel 改动 → 同步更新两端

---

## 5. 面试准备上下文

此项目为**绿联云面试**准备，核心展示点:
- **Flutter Add-to-App**: AAR 模块嵌入原生 App
- **MethodChannel 跨端通信**: Flutter ↔ Kotlin 数据交互
- **Riverpod 状态管理**: 对标 Android ViewModel + StateFlow
- **Room 数据库集成**: (Week 2) 对标绿联云影视中心架构

面试回答要点:
- FlutterContainerActivity 使用 `FlutterFragment.withCachedEngine()` 预热引擎，秒开页面
- PomodoroApplication 管理 FlutterEngine 生命周期，与 Activity 解耦
- 数据流: Flutter (sqflite/Riverpod) → MethodChannel → Native (Room/Compose)
