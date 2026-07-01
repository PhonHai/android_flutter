---
name: arch-rules
description: 本项目架构约定（Android 三套对照 + Flutter Add-to-App + 手机端）。改 MethodChannel/FlutterEngine/NavHost/Room/Manifest/Gradle 时自动加载。避免 agent 写出不符合项目混合架构规范的代码。
applies_to: universal
user-invocable: false
header_only: true
---

# arch-rules · HEADER

**ROLE**：本项目架构领域知识——不是代码风格，是"什么能做/不能做"的架构边界。

**项目特征**：三套 Android 对照学习（legacy Java / jetpack Compose / native Flutter 混合）+ Flutter 模块（Add-to-App），手机端，普通应用签名。

**WHEN**：改 MethodChannel / FlutterEngine / NavHost / Room / Manifest / Gradle / nav_graph / MethodChannelHandler / FlutterContainerActivity 时自动加载。

**CONVENTIONS**：`universal.md`（线程/ANR/IPC/权限/资源/构建）、`mobile.md`（三套 Android 对照架构 + 手机端规则）、`flutter-hybrid.md`（Flutter Add-to-App 混合架构规则）。

**LOAD SKILL.md**：当 edits 涉及上述关键文件时。纯 UI 微调、颜色/字符串资源改动不加载。
