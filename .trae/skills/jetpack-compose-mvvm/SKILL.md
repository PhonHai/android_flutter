---
name: "jetpack-compose-mvvm"
description: "Jetpack Compose + MVVM 最佳实践，基于 Google 官方 Compose UI Architecture 指南。改 @Composable/ViewModel/StateFlow/NavHost 时自动加载。"
---

# Jetpack Compose + MVVM 最佳实践

> **来源**：[Google 官方 Compose UI Architecture 指南](https://developer.android.com/develop/ui/compose/architecture)
>
> **适用**：`jetpack-android/`、`native-android/`（Compose 部分）

**ROLE**：Google 官方 Jetpack Compose 架构最佳实践。

**WHEN**：写/改 `@Composable` 函数、`ViewModel`、`StateFlow`、`NavHost`、`NavigationBar` 时自动加载。

**CORE RULES**：
- 状态只向下流，事件只向上流（UDF）
- UI 层永远不改 State，只读
- 用 `sealed class` / `data class` 建模 UI State
- ViewModel 暴露 immutable State，内部用 `MutableStateFlow`

---

## 1. 核心架构：单向数据流（UDF）

```
           State 向下流（只读）
  ┌──────────────────────────────────┐
  ↓                                  │
┌─────────┐                     ┌──────────┐
│  View   │                     │ViewModel │
│(Composable)│                   │StateFlow │
└─────────┘                     └──────────┘
  │                                  ↑
  └──────────────────────────────────┘
           Event 向上流（回调）
```

### 铁的纪律
- **Composable 只读 State，不写 State**
- **事件通过回调/方法传给 ViewModel**
- **State 只有一个来源**（`StateFlow` 暴露，内部 `MutableStateFlow`）

---

## 2. StateFlow + ViewModel 标准写法

```kotlin
// ✅ 正确
class TimerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    fun onStartClick() {
        _uiState.update { it.copy(isRunning = true) }
    }
}

@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Button(onClick = viewModel::onStartClick) {
        Text(if (uiState.isRunning) "暂停" else "开始")
    }
}
```

```kotlin
// ❌ 禁止
var state by remember { mutableStateOf(TimerUiState()) }
Button(onClick = { state = state.copy(isRunning = true) }) // 错！
val uiState = MutableStateFlow(TimerUiState()) // 错！外部可改
```

---

## 3. UI State 建模

| 场景 | 用什么 |
|------|--------|
| 有互斥状态（Loading / Error / Success） | `sealed class` |
| 一直有数据，但字段值变化 | `data class` |
| 两者混合 | `data class` + `sealed class` |

---

## 4. Composable 参数设计

```kotlin
// ✅ 好：只传需要显示的
@Composable
fun PomodoroHeader(title: String, subtitle: String) { ... }

// ❌ 差：传整个对象（无关字段变化触发重组）
@Composable
fun PomodoroHeader(record: PomodoroEntity) { ... }
```

---

## 5. 事件处理规范

```kotlin
// ViewModel
class TimerViewModel : ViewModel() {
    fun onToggleTimer() { ... }
    fun onResetTimer() { ... }
}

// Composable
Button(onClick = viewModel::onToggleTimer) { ... }
```

一次性事件（Snackbar / Navigation）用 `Channel` + `SharedFlow`。

---

## 6. Side Effects 正确用法

| API | 用途 | 何时用 |
|-----|------|--------|
| `LaunchedEffect(key)` | 启动协程，key 变化重启 | 收集 Flow、发网络请求 |
| `rememberCoroutineScope()` | 获取 Composable 作用域协程 | 按钮点击里启动协程 |
| `DisposableEffect(key)` | 注册/反注册监听 | Observer、Listener |
| `derivedStateOf {}` | 从多个 State 计算派生值 | 避免不必要重组 |

---

## 7. Navigation Compose 规范

```kotlin
object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{itemId}"
    fun detail(itemId: String) = "detail/$itemId"
}
```

导航参数安全取值：
```kotlin
val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
// ❌ 禁止 val itemId = backStackEntry.arguments!!.getString("itemId")!!
```

---

## 8. 常见反模式（禁止）

| 反模式 | 正确做法 |
|--------|---------|
| Composable 里 `var state by remember { mutableStateOf(...) }` 做业务状态 | 放到 ViewModel 的 StateFlow |
| `viewModel.uiState.value.copy(...)` 直接赋值 | 通过 `update { it.copy(...) }` 在 ViewModel 里改 |
| 在 Composable 里调用 `viewModelScope.launch` | 用 `LaunchedEffect` |
| 传整个 Entity 到 Composable | 只传需要的字段 |
| `modifier = Modifier.fillMaxSize()` 重复写 | 复用 modifier |

---

## 参考链接

- [Compose UI Architecture](https://developer.android.com/develop/ui/compose/architecture)
- [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state)
- [Architecture Samples (Google)](https://github.com/android/architecture-samples)