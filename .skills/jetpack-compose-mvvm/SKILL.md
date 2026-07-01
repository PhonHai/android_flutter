---
name: jetpack-compose-mvvm
description: Jetpack Compose + MVVM 最佳实践。基于 Google 官方 Compose UI Architecture 指南。改 Compose @Composable / ViewModel / StateFlow / NavHost 时自动加载。禁止直接改 State，必须走 UDF。
applies_to: android
user-invocable: false
---

# Jetpack Compose + MVVM 最佳实践

> **来源**：[Google 官方 Compose UI Architecture 指南](https://developer.android.com/develop/ui/compose/architecture)
>
> **适用**：`jetpack-android/`、`native-android/`（Compose 部分）

---

## 1. 核心架构：单向数据流（UDF）

```
                  State 向下流（只读）
        ┌──────────────────────────────────┐
        ↓                                  │
    ┌─────────┐                     ┌──────────┐
    │  View   │                     │ViewModel │
    │(Composable)│                   │ StateFlow │
    └─────────┘                     └──────────┘
        │                                  ↑
        └──────────────────────────────────┘
                  Event 向上流（回调）
```

### 铁的纪律
- **Composable 只读 State，不写 State**（写是 ViewModel 的职责）
- **事件通过回调/方法传给 ViewModel**（不直接在 Composable 里改状态）
- **State 只有一个来源**（`StateFlow` / `LiveData` 暴露，ViewModel 内部持有 `MutableStateFlow`）

---

## 2. StateFlow + ViewModel 标准写法

### ✅ 正确（推荐）
```kotlin
// ViewModel 持有 MutableStateFlow（内部可变）
class TimerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()  // 对外只读

    fun onStartClick() {
        _uiState.update { it.copy(isRunning = true) }
    }
}

// Composable 只消费 State
@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Button(onClick = viewModel::onStartClick) {
        Text(if (uiState.isRunning) "暂停" else "开始")
    }
}
```

> ⚠️ **本项目当前代码**：`jetpack-android` 的 `TimerViewModel` 用的是 `private val _uiState = MutableStateFlow(...)`，`TimerScreen` 通过 `collectAsState()` 消费。这个方向是对的，但建议改用 `collectAsStateWithLifecycle()`（更安全）。

### ❌ 禁止
```kotlin
// ❌ Composable 里直接改 ViewModel 的 state
var state by remember { mutableStateOf(TimerUiState()) }
Button(onClick = { state = state.copy(isRunning = true) }) // 错！

// ❌ 直接暴露 MutableStateFlow
val uiState = MutableStateFlow(TimerUiState()) // 错！外部可以改
```

---

## 3. UI State 建模

### Sealed Class（多状态场景）
```kotlin
sealed class SignInUiState {
    data object SignedOut : SignInUiState()
    data object Loading : SignInUiState()
    data class Error(val message: String) : SignInUiState()
    data class SignedIn(val user: User) : SignInUiState()
}
```

### Data Class（单状态多字段场景）
```kotlin
data class TimerUiState(
    val remainingSeconds: Int = 1500,
    val isRunning: Boolean = false,
    val sessions: Int = 0,
    val error: String? = null
)
```

**选择规则**：
| 场景 | 用什么 |
|------|--------|
| 有互斥状态（Loading / Error / Success） | `sealed class` |
| 一直有数据，但字段值变化 | `data class` |
| 两者混合 | `data class` + `sealed class`（sealed 包含 data class） |

> ⚠️ **本项目**：`TimerUiState` 是 `data class`，适合单状态多字段场景，方向正确。

---

## 4. Composable 参数设计

### ✅ 传最少的参数
```kotlin
// ✅ 好：只传需要显示的
@Composable
fun PomodoroHeader(title: String, subtitle: String) { ... }

// ❌ 差：传整个对象（无关字段变化也会触发重组）
@Composable
fun PomodoroHeader(record: PomodoroEntity) { ... }
```

### ✅ 参数类型用不可变值
```kotlin
// ✅ 好：String + lambda
@Composable
fun MyTopBar(text: String, onBackPressed: () -> Unit) { ... }
```

### 参数过多时 → 合并为 data class
```kotlin
data class PomodoroCardConfig(
    val title: String,
    val progress: Float,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun PomodoroCard(config: PomodoroCardConfig) { ... }
```

---

## 5. 事件处理规范

### UI 事件 → ViewModel 方法
```kotlin
// ViewModel
class TimerViewModel : ViewModel() {
    fun onToggleTimer() { ... }
    fun onResetTimer() { ... }
    fun onSessionComplete() { ... }
}

// Composable
Button(onClick = viewModel::onToggleTimer) { ... }
```

### 一次性事件（Snackbar / Navigation）
用 `Channel` + `SharedFlow`，不用 `StateFlow`：
```kotlin
// ViewModel
private val _events = Channel<UiEvent>(Channel.BUFFERED)
val events = _events.receiveAsFlow()

// Composable
LaunchedEffect(Unit) {
    viewModel.events.collect { event ->
        when (event) {
            is UiEvent.ShowSnackbar -> { /* 显示 Snackbar */ }
            is UiEvent.Navigate -> navController.navigate(event.route)
        }
    }
}
```

---

## 6. Side Effects（副作用）正确用法

| API | 用途 | 何时用 |
|-----|------|--------|
| `LaunchedEffect(key)` | 启动协程，key 变化重启 | 收集 Flow、发网络请求 |
| `rememberCoroutineScope()` | 获取 Composable 作用域协程 | 按钮点击里启动协程 |
| `DisposableEffect(key)` | 注册/反注册监听 | Observer、Listener |
| `SideEffect {}` | 每次重组后执行（非 Compose 状态） | 同步到非 Compose 代码 |
| `derivedStateOf {}` | 从多个 State 计算派生值 | 避免不必要重组 |

### ✅ 正确
```kotlin
// 收集 Flow
LaunchedEffect(Unit) {
    viewModel.uiState.collect { state -> /* ... */ }
}

// 只读一次
LaunchedEffect(key1 = itemId) {
    viewModel.loadItem(itemId)
}
```

### ❌ 禁止
```kotlin
// ❌ 在 Composable 函数体里直接 launch 协程
viewModelScope.launch { ... }

// ❌ 在 Composable 里调用 suspend 函数（除非在 LaunchedEffect 里）
@Composable
fun bad() {
    val data = fetchData()  // ❌ Composable 不能 suspend
}
```

---

## 7. Navigation Compose 规范

### 路由定义
```kotlin
// ✅ 集中管理路由常量
object Routes {
    const val MAIN = "main"
    const val HOME = "home"
    const val LIST = "list"
    const val DETAIL = "detail/{itemId}"
    const val COMMENT = "comment/{itemId}"

    fun detail(itemId: String) = "detail/$itemId"
    fun comment(itemId: String) = "comment/$itemId"
}

// NavHost
NavHost(navController, startDestination = Routes.MAIN) {
    composable(Routes.MAIN) { MainTabScreen(navController) }
    composable(Routes.DETAIL, arguments = listOf(navArgument("itemId") { type = NavType.StringType })) { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
        DetailScreen(itemId, navController)
    }
}
```

### 导航参数安全取值
```kotlin
// ✅ 有默认值 + null 检查
val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable

// ❌ 直接 !!（不安全的强制解包）
val itemId = backStackEntry.arguments!!.getString("itemId")!!
```

> ⚠️ **本项目**：`native-android` 的 `AppNavHost.kt` 有正确的安全取值写法，`jetpack-android` 的 `AppNavHost.kt` 可以参考对齐。

---

## 8. 常见反模式（禁止）

| 反模式 | 原因 | 正确做法 |
|--------|------|---------|
| Composable 里 `var state by remember { mutableStateOf(...) }` 做业务状态 | 状态不能跨 Composable 共享 | 放到 ViewModel 的 StateFlow |
| `viewModel.uiState.value.copy(...)` 直接赋值 | 绕过 ViewModel 方法 | 通过 `update { it.copy(...) }` 在 ViewModel 里改 |
| 在 Composable 里调用 `viewModelScope.launch` | 生命周期不对 | 用 `LaunchedEffect` |
| 传整个 Entity 到 Composable | 无关字段变化触发重组 | 只传需要的字段 |
| `modifier = Modifier.fillMaxSize()` 重复写 | 性能浪费 | 复用 modifier |

---

## 9. 本项目对照检查

| 检查项 | jetpack-android | native-android | 
|--------|:--:|:--:|
| ViewModel + StateFlow | ✅ 正确 | ⚠️ Compose 部分 OK |
| UDF 模式 | ✅ | ✅ |
| data class UI State | ✅ `TimerUiState` | ⚠️ 大部分 Compose 用简单参数 |
| 安全导航取值 | ⚠️ 有 !! | ✅ |
| Side Effect 正确 | ✅ | ✅ |
| collectAsStateWithLifecycle | ❌ 用的 collectAsState | ❌ |

---

## 参考链接

- [Compose UI Architecture](https://developer.android.com/develop/ui/compose/architecture)
- [State and Jetpack Compose](https://developer.android.com/develop/ui/compose/state)
- [ViewModel in Compose](https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state)
- [Architecture Samples (Google)](https://github.com/android/architecture-samples)
