---
name: flutter-best-practices
description: Flutter 架构最佳实践。基于 Flutter 官方 Architecture Recommendations。改 Dart / Widget / Riverpod / sqflite / MethodChannel 代码时自动加载。强制 UDF、不可变数据、分层架构。
applies_to: flutter
user-invocable: false
header_only: true
---

# flutter-best-practices · HEADER

**ROLE**：Flutter 官方架构最佳实践——分层架构、UDF、不可变数据、MVVM。

**WHEN**：写/改 `.dart` 文件（Widget / ViewModel / Repository / Model）时自动加载。

**SOURCE**：[Flutter Architecture Recommendations](https://docs.flutter.dev/app-architecture/recommendations)

**CORE RULES**（5 秒速查）：
- UI 层只管显示，不放业务逻辑
- 数据只向下流（UDF），事件只向上传
- 数据模型不可变（`final` 字段 + `copyWith`）
- Repository 模式隔离数据源
- 依赖注入（Provider / Riverpod）不全局访问

**LOAD SKILL.md**：当代码涉及 Flutter 架构分层、状态管理、数据持久化时。
