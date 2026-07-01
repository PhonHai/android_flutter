---
name: jetpack-compose-mvvm
description: Jetpack Compose + MVVM 最佳实践。基于 Google 官方 Compose UI Architecture 指南。改 Compose @Composable / ViewModel / StateFlow / NavHost 时自动加载。禁止直接改 State，必须走 UDF。
applies_to: android
user-invocable:false
header_only: true
---

# jetpack-compose-mvvm · HEADER

**ROLE**：Google 官方 Jetpack Compose 架构最佳实践——不是可选项，是应该遵守的规范。

**WHEN**：写/改 `@Composable` 函数、`ViewModel`、`StateFlow`、`NavHost`、`NavigationBar` 时自动加载。

**SOURCE**：[Compose UI Architecture](https://developer.android.com/develop/ui/compose/architecture)

**CORE RULES**（5 秒速查）：
- 状态只向下流，事件只向上流（UDF）
- UI 层永远不改 State，只读
- 用 `sealed class` / `data class` 建模 UI State
- 每个 Composable 只传最少的参数（不改 `State<T>` 的引用）
- ViewModel 暴露 immutable State，内部用 `MutableStateFlow`

**LOAD SKILL.md**：当代码涉及 Compose + ViewModel + StateFlow 时。
