<a id="top"></a>
# Android Jetpack 面试宝典（Android开发必备）


> **面向**：
> - 有 Java 传统 Android 开发经验、正在学习 Jetpack 的工程师
> - Android 初中级 → 中高级岗位面试
> - 想理解"为什么这么设计"而非"怎么用"的开发者

---

<a id="what"></a>
# 一、Jetpack 到底是什么？

> [⬆ 返回目录](#catalog)


**Jetpack 不是一个框架。** 它是 Google 官方推出的一套 Android 开发组件集合，目的是解决传统 Android 开发的几大痛点：

```
传统痛点:                     Jetpack 方案:
───────────────────────────  ───────────────────────────
生命周期管理麻烦，容易泄漏    → Lifecycle 自动感知
Activity 旋转数据丢失         → ViewModel 保留数据
回调地狱（callback hell）     → LiveData / Flow 响应式
Fragment 跳转混乱             → Navigation 统一管理
SQLite 手写 SQL 容易错        → Room 自动生成
SharedPreferences 卡 UI 线程  → DataStore 异步 + Flow
后台任务被系统杀              → WorkManager 保证执行
```

### 官方推荐架构

```
┌─────────────────────────────────────┐
│  UI Layer (Activity / Fragment)     │ ← 只负责显示、事件
├─────────────────────────────────────┤
│  ViewModel                          │ ← 持有 UI 状态、业务逻辑
├─────────────────────────────────────┤
│  Repository                         │ ← 统一数据源（缓存策略）
├──────────────────┬──────────────────┤
│  Remote (网络)   │  Local (本地)    │
│  Retrofit / Ktor │  Room / DataStore│
└──────────────────┴──────────────────┘
```

> **Java 视角**：以前 MVC 中 Activity 既管 UI 又管数据，Jetpack 把每层拆开，各管各的。

---

<a id="catalog"></a>
# 二、Jetpack 最重要的组件（★★★★★）

> 按企业使用频率排序。**点击组件名跳转到对应章节**；每个章节标题旁有「⬆ 返回目录」链接。

## ★★★★★ 必会
- [Lifecycle](#lifecycle)
- [ViewModel](#viewmodel)
- [LiveData / StateFlow](#livedata)
- [Navigation](#navigation)
- [Room](#room)
- [DataStore](#datastore)
- [WorkManager](#workmanager)
- [Paging3](#paging3)

## ★★★★ 高频
- [SavedStateHandle](#savedstatehandle)
- [ViewBinding](#viewbinding)
- [Coroutine](#coroutine)（Jetpack 大量依赖协程）
- [Flow](#flow)

## ★★★ 常用
- [Hilt](#hilt)（Dagger 简化版）
- CameraX（待补充）
- Benchmark（待补充）

## ★ 新兴
- [Compose](#compose)（很多公司仍以 View 体系为主）

## 进阶 / 附录
- [Repository](#repository)
- [MVVM](#mvvm)
- [ViewModel + LiveData 标准写法](#vm-livedata)
- [DataBinding](#databinding)
- [面试最高频问题](#interview)
- [企业项目标准架构](#arch)
- [岗位必须掌握](#must)
- [学习路线](#roadmap)
- [核心记忆图](#memo)

---

<a id="lifecycle"></a>
# 三、Lifecycle（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack Lifecycle 是一套**生命周期感知组件**，让类（ViewModel/协程/Flow 收集等）能自动响应 Activity/Fragment 的生命周期事件（ON_CREATE/ON_START/ON_RESUME…），而不必手动在 onStart/onStop 里写回调。
- **解决什么问题**：传统在 Activity 的 onStart/onStop 里手动注册/注销监听、启停定时器、取消网络——容易漏注销导致内存泄漏，或在停止后还更新 UI 而崩溃。
- **怎么用**：实现 `DefaultLifecycleObserver` 监听事件；用 `lifecycleScope` 启动生命周期感知协程；`repeatOnLifecycle(STARTED){ }` 让 Flow 收集跟随生命周期；`LifecycleService` 让 Service 也感知。
- **为什么这样用**：把生命周期逻辑从 Activity 解耦到独立 Observer，避免漏注销；运行期保证「停止时不更新 UI」，根治 lifecycle-related crash。

## 传统 Java 的问题

```java
// Java 传统：需要手动管理生命周期
public class MyLocationActivity extends Activity {
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onStart() {
        super.onStart();
        // 1. 每次 onStart 都要注册
        locationManager.requestLocationUpdates(...);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 2. 每次 onStop 都要注销（忘了就泄漏）
        locationManager.removeUpdates(locationListener);
    }

    // 3. 如果还有 Handler / 网络请求 / 蓝牙
    //    每个地方都要重复写 onCreate/onStart/onStop/onDestroy
    //    十处代码里漏一处 = 内存泄漏
}
```

**问题**：生命周期相关代码散落在各处，容易漏写某个回调，导致内存泄漏或崩溃。

## Lifecycle 解决思路

```kotlin
// Jetpack Lifecycle：声明式绑定，组件自己管自己
class MyLocationComponent(private val lifecycle: Lifecycle) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun start() {
        // 组件自动感知 onStart，不需要 Activity 手动调用
        startLocationUpdates()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stop() {
        // 组件自动感知 onStop，不会忘记注销
        stopLocationUpdates()
    }
}

// Activity 里只需要一句
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(MyLocationComponent(lifecycle))
    }
    // 不需要写 onStart/onStop 了！组件自己监听
}
```

## 核心类

| 类 | 角色 | Java 对标 |
|-----|------|---------|
| `Lifecycle` | 持有生命周期状态（CREATED/STARTED/RESUMED/DESTROYED） | 无直接对标 |
| `LifecycleOwner` | 提供 Lifecycle 的对象（Activity/Fragment 默认实现） | 无 |
| `LifecycleObserver` | 监听生命周期变化的接口 | 无，以前靠手动 override |
| `DefaultLifecycleObserver` | Java 8+ 推荐方式（替代 @OnLifecycleEvent 注解） | 无 |

### 推荐写法：DefaultLifecycleObserver（替代 @OnLifecycleEvent）

```kotlin
// @OnLifecycleEvent 有反射性能开销，推荐用 DefaultLifecycleObserver
class MyCameraComponent : DefaultLifecycleObserver {
    override fun onStart(owner: LifecycleOwner) {
        // 启动相机预览
    }
    override fun onStop(owner: LifecycleOwner) {
        // 停止相机预览
    }
}

// 使用
lifecycle.addObserver(MyCameraComponent())
```

## Lifecycle.Event 完整事件

```
ON_CREATE → ON_START → ON_RESUME → (运行中) → ON_PAUSE → ON_STOP → ON_DESTROY
```
- Activity 经历：CREATE → START → RESUME → PAUSE → STOP → DESTROY
- Fragment 多一个 `ON_DESTROY_VIEW`（视图销毁但实例还在）

## lifecycleScope：生命周期感知的协程作用域

```kotlin
// lifecycleScope 是 Activity/Fragment 自带的 CoroutineScope
// 绑定生命周期：DESTROYED 时自动取消所有协程
class MyActivity : AppCompatActivity() {
    fun loadData() {
        lifecycleScope.launch {
            val data = repo.fetch()   // Activity 销毁时自动取消
            textView.text = data
        }
    }
}
```

## repeatOnLifecycle：Flow 收集感知生命周期（重点）

```kotlin
// 传统 View 里收集 StateFlow，要配合 repeatOnLifecycle 避免后台收集
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 只在 STARTED 以上状态收集，STOPPED 时自动取消收集
                    render(state)
                }
            }
        }
    }
}
```
- 不用 `repeatOnLifecycle` → 后台仍收集，浪费电量/流量
- Compose 里用 `collectAsStateWithLifecycle()` 内部就是 `repeatOnLifecycle`

## ProcessLifecycleOwner / LifecycleService

- `ProcessLifecycleOwner`：监听**整个 App 前后台切换**（不是单个 Activity）。适合「App 进后台暂停所有播放」。
- `LifecycleService`：让 Service 也实现 LifecycleOwner，Service 可被 LifecycleObserver 监听。
- `LocalLifecycleOwner`（Compose）：提供当前组合的生命周期，副作用 API（`LaunchedEffect` 等）内部用它。

## 常见坑 / 面试追问

1. **用 `launchWhenStarted` 收集 Flow** → 已废弃，STOPPED 时协程挂起不取消仍占资源。用 `repeatOnLifecycle(STARTED)`。
2. **在 Compose 里裸 `collectAsState`** → 不感知生命周期，用 `collectAsStateWithLifecycle`。
3. **Observer 没移除** → 用 `DefaultLifecycleObserver` 自动跟随，或 `lifecycle.removeObserver()`。
4. **在 `onCreate` 里读 `lifecycle.currentState`** → 此时是 CREATED，不是 RESUMED，别误判。
5. **`@OnLifecycleEvent` 注解** → 有反射开销，已废弃，改用 `DefaultLifecycleObserver`。

## 你项目对照

| 概念 | jetpack-android 真实文件 |
|------|------------------------|
| `collectAsStateWithLifecycle` | `ui/timer/TimerScreen.kt` |
| `@AndroidEntryPoint`（Lifecycle 感知注入） | `ui/MainActivity.kt` |
| VM 协程随 Lifecycle 取消 | 各 VM 的 `viewModelScope`（VM 绑 Lifecycle） |

## 面试高频

> **Q: Lifecycle 如何避免内存泄漏？**
>
> A: Activity/Fragment 销毁时，Lifecycle 状态变为 DESTROYED，LifecycleObserver 通过 `DefaultLifecycleObserver.onDestroy()` 收到通知 → 自动取消注册/释放资源。**不需要开发者手动管理**。

> **Q: 除了 Activity/Fragment，还有什么实现了 LifecycleOwner？**
>
> 答：`ProcessLifecycleOwner`（监听整个 App 前后台）、自定义 `LifecycleRegistry`（手动管理）。

---

<a id="viewmodel"></a>
# 四、ViewModel（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack ViewModel 是**为 UI 管理数据的类**，独立于 Activity/Fragment 的生命周期——屏幕旋转重建 Activity 时 ViewModel 不销毁，数据保留。
- **解决什么问题**：传统把数据放 Activity，旋转屏幕 Activity 重建 → 数据丢失 → 重新请求；且数据逻辑和 UI 逻辑混在 Activity 里臃肿。
- **怎么用**：继承 `ViewModel`，把状态（`MutableStateFlow`）和操作（`fun load()`）放进去；UI 用 `viewModel()`/`hiltViewModel()` 取实例；耗时操作用 `viewModelScope.launch`；Hilt 场景加 `@HiltViewModel`。
- **为什么这样用**：ViewModel 的生命周期长于 Activity（绑 `ViewModelStore`），天然扛配置变更；`viewModelScope` 让协程跟它绑定自动取消；把数据逻辑从 UI 分离，符合 MVVM 单一职责。

## 为什么需要 ViewModel？

```java
// Java 传统：Activity 旋转后数据丢失
public class TimerActivity extends Activity {
    private int seconds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 旋转屏幕后 seconds 变成 0，因为 Activity 重建了
        // 只能用 onSaveInstanceState + onRestoreInstanceState 手动保存
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds");
        }
    }
}
```

```kotlin
// Jetpack ViewModel：旋转不丢失数据
class TimerViewModel : ViewModel() {
    var seconds = 0
        private set

    fun tick() {
        seconds++
    }
    // onCleared() 只在 Activity 真正 finish 时调用，旋转不触发
}

class TimerActivity : AppCompatActivity() {
    private val viewModel: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 旋转屏幕后，viewModel.seconds 仍然是之前的值
    }
}
```

## 原理

```
旋转屏幕时：
  Activity 旧实例 → onDestroy（不是 finish）
  ViewModelStore 保留 ViewModel（存在 NonConfigurationInstances 里）
  Activity 新实例 → 从 ViewModelStore 取同一个 ViewModel

Activity finish 时：
  ViewModelStore 清空 → 调用 ViewModel.onCleared()
```

> **Java 视角**：以前 onRetainNonConfigurationInstance + getLastNonConfigurationInstance 手动保存数据，ViewModel 封装了这套机制。

## ViewModel 不能持有 View 引用

```kotlin
// ❌ 错误：ViewModel 持有 Activity 引用 → 内存泄漏
class BadViewModel : ViewModel() {
    var activity: Activity? = null  // Activity 已销毁，ViewModel 还引用着
}

// ✅ 正确：ViewModel 只持有 StateFlow/LiveData，用 observe 发到 UI
class GoodViewModel : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result  // 对外只读
}
```

## ViewModelFactory：带参数构造

```kotlin
class UserViewModel(
    private val userId: String,
    private val repo: UserRepository
) : ViewModel() {

    // 需要自定义 Factory 才能传参
    class Factory(
        private val userId: String,
        private val repo: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return UserViewModel(userId, repo) as T
        }
    }
}

// Activity 里用
val viewModel: UserViewModel by viewModels {
    UserViewModel.Factory("123", UserRepository())
}
```

## viewModelScope：协程安全

```kotlin
class DataViewModel : ViewModel() {
    // viewModelScope 是 ViewModel 自带的 CoroutineScope
    // ViewModel 销毁时自动取消所有协程，不需要手动管理
    fun loadData() {
        viewModelScope.launch {
            val result = repository.fetchData()  // 网络请求
            _data.value = result
        }
    }
}
```

## 完整实战（对照你项目的 TimerViewModel / FileViewModel）

```kotlin
// 你项目 TimerViewModel.kt：典型的 Hilt + StateFlow + viewModelScope 模式
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: TimerRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TimerUiState(...))
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()   // 对外只读

    fun start() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {       // 绑 VM 生命周期
            while (true) { delay(1000); tick() }
        }
    }
    override fun onCleared() { timerJob?.cancel() }   // 释放
}
```

## @HiltViewModel：Hilt 注入 ViewModel

```kotlin
// 传统：要手写 ViewModelFactory 传 repo（见上节）
// Hilt：加 @HiltViewModel + @Inject constructor，Hilt 自动注入依赖
@HiltViewModel
class FileViewModel @Inject constructor(
    private val repository: FileRepository
) : ViewModel()

// Compose 里取：hiltViewModel()（Hilt 自动装配依赖）
@Composable
fun FileListScreen(vm: FileViewModel = hiltViewModel()) { ... }
```
- 不用再写 Factory，Hilt 按 `@Inject constructor` 自动造好依赖。

## SavedStateHandle：进程死亡恢复

```kotlin
// SavedStateHandle：ViewModel 自带的键值存储，进程被杀也能恢复
class TimerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    // 优先从 SavedStateHandle 恢复，没有用默认值
    val remaining = savedStateHandle.get<Int>("remaining") ?: 10

    fun saveState() {
        savedStateHandle["remaining"] = _uiState.value.remainingSeconds
    }
}
```
- 旋转屏幕 → ViewModel 不销毁，数据天然在
- 系统杀进程重启 → SavedStateHandle 的值会恢复
- 对比 `rememberSaveable`（Compose UI 级），SavedStateHandle 是 VM 级

## onCleared：资源释放

```kotlin
class TimerViewModel : ViewModel() {
    private var timerJob: Job? = null

    override fun onCleared() {
        timerJob?.cancel()   // VM 销毁时取消计时协程
        // 也适合：关闭数据库连接、注销广播、释放资源
    }
}
```
- `onCleared` 只在 Activity 真正 finish 时调用（旋转不触发）
- `viewModelScope` 的协程会自动取消，但手动管理的 Job 要自己在 onCleared 取消

## 与 Compose 集成

```kotlin
// 取 VM 实例
@Composable
fun TimerScreen(vm: TimerViewModel = hiltViewModel()) { ... }   // Hilt
@Composable
fun HistoryScreen(vm: HistoryViewModel = viewModel()) { ... }   // 非 Hilt
```
- `viewModel()`/`hiltViewModel()` 默认参数：方便 `@Preview` 不传 VM
- VM 的作用域默认绑 NavBackStackEntry（每个路由一个 VM 实例）

## 常见坑 / 面试追问

1. **VM 持有 View/Activity 引用** → 内存泄漏。VM 只能持有数据/业务对象。
2. **在 VM 里用 `GlobalScope`** → 永不取消，用 `viewModelScope`。
3. **手写 Factory 传 repo** → 用 Hilt `@HiltViewModel` 省掉。
4. **在 `onCleared` 里更新 UI** → 此时 UI 已销毁，只能做资源释放。
5. **VM 里放大数据** → VM 不销毁（旋转），大对象一直占内存。大数据用 Room/DataStore。
6. **多个 Composable 共享 VM** → 在 NavHost 层 `hiltViewModel()` 取，子路由传参。

## 你项目对照

| 概念 | jetpack-android 真实文件 |
|------|------------------------|
| @HiltViewModel + StateFlow | `viewmodel/TimerViewModel.kt`、`FileViewModel.kt`、`HistoryViewModel.kt`、`SettingsViewModel.kt`、`TransferViewModel.kt` |
| viewModelScope.launch | 各 VM |
| SavedStateHandle（进程恢复） | `viewmodel/TimerViewModel.kt` |
| onCleared / Job 取消 | `viewmodel/TimerViewModel.kt`（`timerJob?.cancel()`） |

## 面试高频

> **Q: ViewModel 为什么不会因为旋转而销毁？**
>
> A: Activity 旋转时会先调用 `onDestroy()`（不是 finish），Activity 的 `ViewModelStore` 保存在 `NonConfigurationInstances` 中，新 Activity 从同一个 `ViewModelStore` 中获取 ViewModel 实例。只有 Activity 真正 finish 时，`ViewModelStore` 才会清空，触发 `onCleared()`。

> **Q: 为什么不能在 ViewModel 中保存 Activity 引用？**
>
> A: ViewModel 的生命周期长于 Activity — 旋转后 Activity 重建但 ViewModel 不变。如果 ViewModel 持有旧的 Activity 引用，会导致该 Activity 无法被 GC，造成**内存泄漏**。ViewModel 的设计原则就是不持有任何 View/Activity 引用。

---

<a id="livedata"></a>
# 五、LiveData（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用（含 StateFlow）

- **是什么**：LiveData 是 Jetpack 的**生命周期感知可观察数据持有者**；`StateFlow` 是 Kotlin 协程生态的热流，是 LiveData 在 Compose 时代的继任者。两者都用于「把 ViewModel 的状态变化通知给 UI」。
- **解决什么问题**：传统用接口回调/Handler 通知 UI 数据变化——手动管理、易漏、不感知生命周期（后台更新 UI 崩溃）。LiveData/StateFlow 让 UI 自动收到新值且安全。
- **怎么用**：ViewModel 里用 `MutableStateFlow`/`MutableLiveData` 持有状态，暴露只读版本；UI 订阅——LiveData 用 `observe(LifecycleOwner){}`，StateFlow 用 `collectAsStateWithLifecycle()`。
- **为什么这样用**：LiveData 自动感知生命周期避免后台更新崩溃；StateFlow 操作符更丰富、线程自由、与协程一体化。**Compose 时代首选 StateFlow + collectAsStateWithLifecycle**，LiveData 适合传统 View 体系。

## 传统 Java 的问题

```java
// Java 传统：数据变化通知 UI
// 方式 1：Callback 接口
public interface OnDataChanged {
    void onDataChanged(String data);
}
// 问题：不感知生命周期，Activity 销毁后回调还在执行 → 崩溃

// 方式 2：EventBus
// 问题：Activity 不可见时仍然收到事件，全局广播难以追踪

// 方式 3：Handler
// 问题：Activity 销毁后 Handler 消息还在队列里 → 内存泄漏
```

## LiveData 解决

```kotlin
// LiveData = 生命周期感知的可观察数据容器
class MyViewModel : ViewModel() {
    // 内部可变，对外只读
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    fun loadUser() {
        _userName.value = "张三"       // 主线程更新
        // 子线程用 _userName.postValue("张三")
    }
}

class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel: MyViewModel by viewModels()

        // observe() 自动感知生命周期
        // Activity 可见 → 接收更新；不可见 → 暂停接收；销毁 → 自动取消
        viewModel.userName.observe(this) { name ->
            textView.text = name  // 只在 Activity 可见时更新 UI
        }
    }
}
```

## setValue vs postValue

```kotlin
// setValue：必须在主线程调用
viewModel.userName.value = "李四"

// postValue：在子线程调用，切回主线程更新
viewModel.userName.postValue("李四")

// postValue 的陷阱：连续多次 postValue 只会保留最后一次
// 因为 postValue 内部只存一个 pending value
viewModel.userName.postValue("A")  // 被覆盖
viewModel.userName.postValue("B")  // 最终值
// 观察者只会收到 "B"

// 如果多个值都要收到，用 setValue + 切主线程
```

## Transformations：数据转换

```kotlin
// 场景：根据 userId 查询用户详情
class UserViewModel(private val userId: String) : ViewModel() {
    private val _userId = MutableLiveData(userId)

    // map：一对一转换
    val userName: LiveData<String> = Transformations.map(_userId) { id ->
        "用户: $id"
    }

    // switchMap：一对多（每次输入变化时重新执行函数）
    val userDetail: LiveData<User> = Transformations.switchMap(_userId) { id ->
        repository.getUser(id)  // 返回 LiveData<User>
    }
}
```

## MediatorLiveData：合并多个数据源

```kotlin
class CombinedViewModel : ViewModel() {
    private val liveData1 = MutableLiveData<String>()
    private val liveData2 = MutableLiveData<Int>()

    val combined = MediatorLiveData<String>().apply {
        addSource(liveData1) { value = combine() }  // 任一变化都触发
        addSource(liveData2) { value = combine() }
    }

    private fun combine(): String {
        val v1 = liveData1.value ?: ""
        val v2 = liveData2.value?.toString() ?: ""
        return "结果: $v1 - $v2"
    }
}
```

## LiveData vs StateFlow 选型（核心）

| 维度 | LiveData | StateFlow |
|------|----------|-----------|
| 线程 | 只能主线程 `setValue` | 任意线程 `emit` |
| 操作符 | map/switchMap（少） | map/filter/combine/zip/…（全套） |
| 背压 | 不支持 | 支持 |
| 生命周期感知 | ✅ 内置（observe 传 LifecycleOwner） | ❌ 需 `collectAsStateWithLifecycle` / `repeatOnLifecycle` |
| 初始值 | 可为 null | 强制有初始值 |
| 粘性 | 是（新观察者收到最新值） | 是 |
| Compose 支持 | 需 `observeAsState` | `collectAsStateWithLifecycle` 原生 |

**选型原则**：
- 传统 View 体系（Activity/Fragment + XML）→ LiveData 够用且省事
- Compose 时代 / 协程项目 → **StateFlow**（操作符丰富、与协程一体化）
- 一次性事件（导航/Toast）→ 都别用，用 Channel（见 Flow 章）

## 为什么 Compose 时代选 StateFlow

1. LiveData 的 `observe` 要 LifecycleOwner，Compose 里不直观
2. StateFlow 操作符丰富（map/filter/combine），LiveData 只有 map/switchMap
3. StateFlow 与协程一体化，Room/DataStore 都返回 Flow，直接接 StateFlow 无需转换
4. `collectAsStateWithLifecycle` 让 StateFlow 也具备生命周期感知

## StateFlow 实战（对照项目）

```kotlin
// 你项目 FileViewModel.kt：StateFlow + sealed class 状态机
@HiltViewModel
class FileViewModel @Inject constructor(
    private val repository: FileRepository
) : ViewModel() {
    private val _files = MutableStateFlow<Result<List<NasFile>>>(Result.Loading)
    val files: StateFlow<Result<List<NasFile>>> = _files.asStateFlow()   // 对外只读

    init { loadFiles() }

    fun loadFiles() {
        viewModelScope.launch {
            _files.value = Result.Loading
            _files.value = repository.getFileList(1)   // Success 或 Error
        }
    }
}

// UI 收集
@Composable
fun FileListScreen(vm: FileViewModel = hiltViewModel()) {
    val files by vm.files.collectAsStateWithLifecycle()   // ✅ 生命周期感知
}
```

## 常见坑 / 面试追问

1. **LiveData `postValue` 连续调用丢值** → 只保留最后一个。要每个都收到用 `setValue` 切主线程。
2. **LiveData 在 VM 里暴露可变** → UI 能改状态。暴露只读 `LiveData`（用 `val x = _x`）。
3. **StateFlow 不感知生命周期** → 裸 `collectAsState` 后台仍收集。用 `collectAsStateWithLifecycle`。
4. **StateFlow 初始值乱设** → UI 先显示初始值再闪真实值。初始值要语义合理（如 `Result.Loading`）。
5. **用 LiveData 做一次性事件** → 粘性导致旋转重复。用 Channel。

## 你项目对照

| 概念 | jetpack-android 真实文件 |
|------|------------------------|
| StateFlow + asStateFlow | `viewmodel/TimerViewModel.kt`、`FileViewModel.kt`（`_uiState`/`_files`） |
| collectAsStateWithLifecycle（推荐） | `ui/timer/TimerScreen.kt` |
| collectAsState（旧写法） | `ui/main/FileListScreen.kt` 等（建议升级） |
| LiveData | 项目未使用（Compose 时代全用 StateFlow） |

## 面试高频

> **Q: LiveData 为什么不会内存泄漏？**
>
> A: `LiveData.observe()` 方法接收 `LifecycleOwner`（如 Activity），LiveData 会自动绑定到该 LifecycleOwner 的 Lifecycle。当 LifecycleOwner 状态变为 **DESTROYED** 时，LiveData 自动移除观察者，因此不会发生"Activity 已销毁但仍被回调持有"的内存泄漏。

> **Q: LiveData 和 StateFlow 的区别？**
>
> A: 见 Flow 章节。

---

<a id="navigation"></a>
# 六、Navigation（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack Navigation 是 Android 的**导航框架**，用导航图（NavGraph）统一管理页面路由、参数传递、回退栈。分 XML 版（Navigation Component）和 Compose 版（Navigation Compose）。
- **解决什么问题**：传统用多个 Activity + Intent 跳转——回退栈混乱、参数传递靠 Bundle 易错、深层链接难做、转场动画不统一。
- **怎么用**：定义 `NavHost` + `composable("路由"){ 屏幕 }`；用 `navController.navigate("路由")` 跳转、`popBackStack()` 返回；参数走 `"detail/{id}"` 路由模板。
- **为什么这样用**：单 Activity + 多 Composable/Fragment 架构，回退栈框架管、参数类型安全（SafeArgs）、深层链接原生支持；Compose 版全 Kotlin 可重构。

## 传统 Java 的方式

```java
// Java 传统 Fragment 跳转
FragmentManager fm = getSupportFragmentManager();
FragmentTransaction ft = fm.beginTransaction();
ft.replace(R.id.container, new HomeFragment());
ft.addToBackStack(null);
ft.commit();
// 传参数需要 Bundle + arguments
// 返回栈管理混乱，多个 Fragment 之间跳转耦合严重
```

## Navigation XML 版

```xml
<!-- res/navigation/nav_graph.xml -->
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:startDestination="@id/homeFragment">

    <fragment
        android:id="@+id/homeFragment"
        android:name="com.example.HomeFragment">
        <action
            android:id="@+id/to_detail"
            app:destination="@id/detailFragment" />
    </fragment>

    <fragment
        android:id="@+id/detailFragment"
        android:name="com.example.DetailFragment">
        <argument
            android:name="itemId"
            app:argType="string" />
    </fragment>
</navigation>
```

```kotlin
// Activity 里
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // NavHostFragment 在 xml 里声明
        val navController = findNavController(R.id.nav_host_fragment)
    }
}

// Fragment 里跳转
class HomeFragment : Fragment() {
    fun onItemClick(itemId: String) {
        // 使用 action ID 跳转（SafeArgs 推荐）
        val action = HomeFragmentDirections.toDetail(itemId)
        findNavController().navigate(action)
    }
}
```

## SafeArgs：类型安全的参数传递

```kotlin
// 传统方式：key-value 字符串，容易拼错
val bundle = Bundle()
bundle.putString("itemId", "123")
navController.navigate(R.id.to_detail, bundle)

// SafeArgs 方式：自动生成 Directions 类
// 1. build.gradle 添加 safe-args 插件
// 2. nav_graph.xml 声明 argument
// 3. 自动生成类：
val action = HomeFragmentDirections.toDetail("123")
navController.navigate(action)

// 接收方：
// DetailFragmentArgs.fromBundle(arguments).itemId
```

## Navigation Compose 版

```kotlin
// Compose 版不需要 XML，用函数声明路由
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            route = "detail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            DetailScreen(itemId)
        }
    }
}
```

> **Java 视角**：Navigation 把 `<fragment> + <action>` 的导航关系从代码解耦到 XML，类似于以前的 `<intent-filter>` 的声明式思路。但 Compose 版又回到"代码声明路由"的方式。

## 完整实战（对照你项目的 AppNavHost）

```kotlin
// 你项目 ui/AppNavHost.kt：Compose 版导航，全 Kotlin 声明路由
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "timer") {
        composable("timer") { TimerScreen(navController, ...) }
        composable("home") { HomeScreen(navController) }
        composable("list") { ListScreen(navController) }
        composable("detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            DetailScreen(itemId)
        }
        composable("comment") { CommentScreen() }
    }
}

// 页面里跳转（TimerScreen.kt）
OutlinedButton(onClick = { navController.navigate("home") }) { Text("进入 4 级导航演示") }
```

## 底部导航（对照 MainTabScreen）

```kotlin
// 你项目 ui/main/MainTabScreen.kt：底部 Tab + 内部状态切换
@Composable
fun MainTabScreen() {
    var selectedTab by remember { mutableStateOf(0) }   // 当前 Tab
    when (selectedTab) {
        0 -> TimerTab()
        1 -> FileListScreen()
        2 -> SettingsScreen()
        3 -> TransferListScreen()
    }
    // 底部导航栏点击切换 selectedTab
}
```
- 底部 Tab 可用 `remember { mutableStateOf }` 管理（简单场景）
- 也可用 Navigation 的 `NavHost` + `composable` 做底部导航（更规范，回退栈统一管）

## 回退栈管理（popUpTo / launchSingleTop）

```kotlin
// 跳转时配置回退栈
navController.navigate("home") {
    popUpTo("home") { inclusive = true }   // 弹出到 home（含），避免回退栈堆积
    launchSingleTop = true                  // 已在栈顶不重复创建
    restoreState = true                     // 恢复状态
}
```
- `popUpTo`：弹出回退栈到某路由，避免无限堆积
- `launchSingleTop`：目标已在栈顶则不重建
- `restoreState`/`saveState`：切换 Tab 时保存/恢复状态

## 嵌套导航图

```kotlin
NavHost(navController, startDestination = "main") {
    composable("main") { MainScreen() }
    // 嵌套图：登录流程
    navigation(startDestination = "login_input", route = "login_flow") {
        composable("login_input") { LoginInputScreen() }
        composable("login_verify") { LoginVerifyScreen() }
    }
}
// 跳整个图：navController.navigate("login_flow")
```

## 深层链接

```kotlin
composable(
    "detail/{itemId}",
    deepLinks = listOf(navDeepLink { uriPattern = "myapp://detail/{itemId}" })
) { ... }
// 外部打开 myapp://detail/123 → 直接进详情页
```

## 常见坑 / 面试追问

1. **路由字符串拼错** → 跳转失败。可用常量或类型安全路由（Navigation 2.8+ 的 `@Serializable` route）。
2. **`navigate` 重复调用** → 回退栈堆积多个同一页。用 `launchSingleTop = true`。
3. **参数没声明 `navArgument`** → 取不到值。`composable("detail/{id}"){ }` 要配 `navArgument("id")`。
4. **`popBackStack()` 返回 false** → 栈空了。判断 `if (!navController.popBackStack()) finish()`。
5. **底部导航用 Navigation 但没 `popUpTo`** → Tab 切换回退栈乱。
6. **VM 作用域** → 默认绑 NavBackStackEntry，离开路由 VM 销毁；想跨路由共享用父 NavHost 的 `hiltViewModel()`。

## 你项目对照

| 概念 | jetpack-android 真实文件 |
|------|------------------------|
| NavHost + composable 路由 | `ui/AppNavHost.kt` |
| navigate 跳转 | `ui/timer/TimerScreen.kt`、`ui/list/ListScreen.kt` |
| 底部 Tab（remember 状态） | `ui/main/MainTabScreen.kt` |
| XML 版 Navigation | `legacy-android/`（传统工程用 `nav_graph.xml`） |

## 面试高频

> **Q: Navigation 相比 FragmentTransaction 有什么优势？**
>
> A: ① 可视化导航图（XML 直观展示页面关系）② 自动管理返回栈 ③ SafeArgs 保证参数类型安全 ④ 支持 DeepLink ⑤ 支持动画/过渡 ⑥ 与 Toolbar 自动集成。

---

<a id="room"></a>
# 七、Room（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack Room 是 Android 官方的**SQLite ORM**，用注解（`@Entity`/`@Dao`/`@Database`）把 Kotlin 对象映射到数据库表，编译期校验 SQL。
- **解决什么问题**：原生 SQLite 要手写 SQL 字符串、手动 cursor 解析、无编译期检查（SQL 写错运行时才崩）、样板代码多。
- **怎么用**：定义 `@Entity` 数据类（表）、`@Dao` 接口（增删改查，可返回 `Flow`）、`@Database` 抽象类（数据库）；Hilt 在 `@Module` 里 `@Provides` 提供单例；VM 经 Repository 调 DAO。
- **为什么这样用**：编译期校验 SQL（写错表名/列名直接编译报错）；返回 `Flow` 让数据变化自动通知 UI；与协程/Flow/Hilt 深度集成；是 Android 本地存储的事实标准。

## 传统 SQLite vs Room

```java
// Java 传统 SQLiteOpenHelper
public class UserDbHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE =
        "CREATE TABLE users (" +
        "id INTEGER PRIMARY KEY," +
        "name TEXT," +
        "age INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);  // SQL 拼错只能运行时发现
    }

    // 查数据：
    Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{"1"});
    String name = cursor.getString(cursor.getColumnIndex("name"));  // 列名写错编译不报错
    // 手动关 Cursor，忘关 = 内存泄漏
}
```

```kotlin
// Room：Entity + DAO + Database
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    val age: Int
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: Int): User?  // 编译时自动检查 SQL 语法

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>  // Room + Flow = 自动刷新 UI
}

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        // 单例，避免多次创建
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context, AppDatabase::class.java, "app.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

// 使用
val user = db.userDao().getUser(1)  // Kotlin 协程，自动切 IO 线程
```

## TypeConverter：自定义类型

```kotlin
// Room 只支持基本类型，Date 需要转换
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}

@Database(entities = [User::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() { ... }
```

## Migration：数据库升级

```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE users ADD COLUMN phone TEXT")
    }
}

Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
    .addMigrations(MIGRATION_1_2)
    .build()
```

> **Java 视角**：Room ≈ 把 SQLiteOpenHelper + Cursor + ContentValues 的重复工作交给编译期注解处理器，SQL 写在 `@Query` 注解里，编译时就帮你检查对错。再也不用写 `cursor.getString(cursor.getColumnIndex("xxx"))` 了。

## 完整实战（对照你项目的 PomodoroDatabase / PomodoroDao）

```kotlin
// 你项目 data/PomodoroDatabase.kt
@Database(entities = [PomodoroEntity::class], version = 1, exportSchema = false)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun pomodoroDao(): PomodoroDao   // 编译期生成实现
}

// 你项目 data/PomodoroDao.kt
@Dao
interface PomodoroDao {
    @Insert
    suspend fun insert(record: PomodoroEntity): Long      // 写：suspend

    @Query("SELECT * FROM pomodoro_records ORDER BY endTime DESC LIMIT 100")
    fun getAllRecords(): Flow<List<PomodoroEntity>>        // 读：返回 Flow
}
```
- `@Database` 声明表 + 版本，编译期生成建表 SQL 和 DAO 实现
- DAO 的 `suspend fun` 写、`Flow` 读——**「写用 suspend，读用 Flow」**是 Room 最佳实践

## DAO 注解详解

| 注解 | 作用 | 示例 |
|------|------|------|
| `@Insert` | 插入 | `@Insert suspend fun insert(user: User): Long` |
| `@Update` | 更新（按主键） | `@Update suspend fun update(user: User)` |
| `@Delete` | 删除（按主键） | `@Delete suspend fun delete(user: User)` |
| `@Query` | 自定义 SQL（编译期校验） | `@Query("SELECT * FROM users WHERE id = :id") suspend fun getUser(id: Int): User?` |
| `@Transaction` | 事务（多条原子） | `@Transaction suspend fun transfer(...) { ... }` |

- `@Query` 里 `:id` 绑定方法参数，编译期检查表名/列名/语法
- 批量插入：`@Insert suspend fun insertAll(users: List<User>)`

## 返回 Flow 的意义（核心）

```kotlin
// 返回 Flow：数据库一变，UI 自动收到新列表
@Query("SELECT * FROM pomodoro_records")
fun getAllRecords(): Flow<List<PomodoroEntity>>

// ViewModel 里转 StateFlow
val records = dao.getAllRecords().stateIn(viewModelScope, WhileSubscribed(5000), emptyList())
// UI collect → 数据库 insert/update/delete → Flow 自动发射 → UI 自动刷新
```
- 对比传统 `List<>` 同步返回：数据变了不会自动刷新，要手动重查
- Room 的 Flow 是**冷流**，每次 collect 触发查询；`stateIn` 转热流共享

## 与 Hilt 集成

```kotlin
// 你项目 di/AppModule.kt：Hilt 提供 Room 单例
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides @Singleton
    fun providePomodoroDatabase(@ApplicationContext ctx: Context): PomodoroDatabase =
        Room.databaseBuilder(ctx, PomodoroDatabase::class.java, "pomodoro.db").build()

    @Provides @Singleton
    fun providePomodoroDao(db: PomodoroDatabase): PomodoroDao = db.pomodoroDao()
}
```
- 之前 `PomodoroDatabase.getInstance(context)` 手动单例 → 现在 Hilt 管理，全局唯一
- `@ApplicationContext`：数据库生命周期同 Application

## @Relation 关系 / @Embedded

```kotlin
// 一对多：User 有多个 Post
data class UserWithPosts(
    @Embedded val user: User,
    @Relation(parentColumn = "id", entityColumn = "userId")
    val posts: List<Post>
)
@Transaction   // 关系查询要事务
@Query("SELECT * FROM users WHERE id = :userId")
suspend fun getUserWithPosts(userId: Int): UserWithPosts?

// @Embedded：把一个对象拍平存进表
@Entity
data class Address(val city: String, val street: String)
@Entity
data class User(@Embedded val address: Address, ...)
```

## 常见坑 / 面试追问

1. **`@Query` SQL 写错表名** → 编译报错（这正是 Room 的好处，运行时不崩）。
2. **主线程读写数据库** → `suspend`/`Flow` 自动切 IO，但同步返回类型（`List<>` 非 Flow）会崩。
3. **`@Relation` 不加 `@Transaction`** → 多次查询不一致。
4. **数据库升级不加 Migration** → 崩 `IllegalStateException`。加 `Migration` 或 `fallbackToDestructiveMigration`（开发期）。
5. **`exportSchema = false` 没设** → 默认 true 会生成 schema JSON，需配目录。
6. **大对象存 Room** → 序列化/反序列化开销，大数据考虑文件存储。

## 你项目对照

| 概念 | jetpack-android 真实文件 |
|------|------------------------|
| `@Database` + 单例 | `data/PomodoroDatabase.kt` |
| `@Dao` + `@Insert`/`@Query` | `data/PomodoroDao.kt` |
| 返回 Flow（自动刷新） | `PomodoroDao.getAllRecords()` |
| Hilt 提供 DB 单例 | `di/AppModule.kt`（`providePomodoroDatabase`/`providePomodoroDao`） |
| Repository 调 DAO | `data/repository/TimerRepository.kt` |

---

<a id="datastore"></a>
# 八、DataStore（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack DataStore 是 Android 官方的**键值/对象存储**，用协程 + Flow 实现，替代 SharedPreferences。分 Preferences DataStore（键值）和 Proto DataStore（类型安全对象）。
- **解决什么问题**：SharedPreferences 的痛点——同步 `commit` 阻塞、`apply` 无返回、无类型安全、API 老旧、`getXxx` 默认值易错、不支持错误处理。
- **怎么用**：Preferences 版用 `dataStore` 委托 + `edit { }` 写、`data.map { }` 读 Flow；Proto 版写 Schema + Serializer。都返回 Flow，VM 里 `stateIn` 转 StateFlow 给 UI。
- **为什么这样用**：协程异步不阻塞、Flow 响应数据变化、事务性 `edit` 保证一致性；Preferences 简单，Proto 类型安全。Compose 时代偏好存储首选。

## SharedPreferences 的问题

```java
// Java 传统 SP
SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
sp.edit().putString("token", "abc").apply();
String token = sp.getString("token", "");  // 可能在 IO 线程卡 UI
```

**SP 的三大问题**：
1. `apply()` 异步但不可观察，无法知道何时写完
2. `commit()` 同步但可能卡 UI 线程
3. **跨进程不安全**（MODE_MULTI_PROCESS 已废弃）
4. 首次加载会阻塞 UI（同步读文件）

## DataStore Preferences

```kotlin
// DataStore：完全异步，基于 Flow
val Context.dataStore by preferencesDataStore(name = "settings")

// 写
suspend fun saveToken(token: String, context: Context) {
    context.dataStore.edit { preferences ->
        preferences[stringPreferencesKey("token")] = token
    }
}

// 读（自动刷新）
val tokenFlow: Flow<String?> = context.dataStore.data.map { preferences ->
    preferences[stringPreferencesKey("token")]
}

// Activity 里
lifecycleScope.launch {
    dataStore.data.collect { prefs ->
        val token = prefs[stringPreferencesKey("token")] ?: ""
        // 值变化时自动收到通知
    }
}
```

## DataStore Proto

```kotlin
// Proto DataStore：类型安全，需要定义 proto schema
// settings.proto 文件
message UserPreferences {
    string token = 1;
    int32 theme = 2;
}

// 生成的代码：UserPreferences 是类型安全的对象
val token = preferences.token  // 不会拼错 key 名
```

> **Java 视角**：SP 的数据变化需要自己手动通知（或者用 ContentObserver），DataStore 直接用 Flow 推送变化，观察者模式内置。

## 完整实战（对照你项目的 SettingsDataStore）

你项目 `data/SettingsDataStore.kt` 是完整的 DataStore 实战案例，包含「文件级单例 + 类型安全 Key + Flow 读 + suspend 写 + Hilt 注入」全链路：

```kotlin
// ① 文件级单例：Context 扩展属性 + 委托，懒加载单例
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// ② 类型安全的 Key（编译期知道类型，写错类型编译报错）
companion object {
    val POMODORO_DURATION = intPreferencesKey("pomodoro_duration")   // Int 类型
    val THEME_MODE = intPreferencesKey("theme_mode")
}

// ③ 读：返回 Flow，数据变化自动推送
val pomodoroDuration: Flow<Int> = context.dataStore.data.map { prefs ->
    prefs[POMODORO_DURATION] ?: 10   // 没存过给默认值
}

// ④ 写：suspend + edit {} 事务性
suspend fun setPomodoroDuration(seconds: Int) {
    context.dataStore.edit { prefs -> prefs[POMODORO_DURATION] = seconds }
}
```

> 核心模式记一句：**「读用 Flow，写用 suspend」**——读返回 Flow 自动推送变化，写用 suspend 保证异步不阻塞 + edit 事务性。

## 与 Hilt / ViewModel 集成

```kotlin
// SettingsDataStore.kt：@Singleton + @Inject，Hilt 全局提供单例
@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context   // ← 必须 Application 级 Context
) { ... }

// SettingsViewModel.kt：注入 DataStore，把 Flow 转成 StateFlow 给 UI
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
) : ViewModel() {
    val pomodoroDuration = dataStore.pomodoroDuration
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 10)   // 冷流转热流

    fun setPomodoroDuration(seconds: Int) {
        viewModelScope.launch { dataStore.setPomodoroDuration(seconds) }   // 协程里调 suspend
    }
}
```

- **为什么用 `@ApplicationContext`**：DataStore 是单例，生命周期同 Application；用 Activity Context 会随 Activity 销毁被回收，单例失效。
- **完整数据流**：用户点 RadioButton → VM.launch → DataStore.edit 写磁盘 → data Flow 发射 → map 转换 → stateIn → collectAsState → UI 自动更新。全程无手动 notify。

## 错误处理

```kotlin
// 读：用 catch 兜底 IOException
val tokenFlow: Flow<String?> = context.dataStore.data
    .catch { emit(emptyPreferences()) }   // 读失败给空，不崩
    .map { it[TOKEN_KEY] }

// 写：edit 抛 IOException，调用方 try/catch
suspend fun saveToken(token: String) {
    try { context.dataStore.edit { it[TOKEN_KEY] = token } }
    catch (e: IOException) { /* 处理 */ }
}
```

## SharedPreferences 迁移

```kotlin
// 从 SP 迁移到 DataStore，一行搞定
private val Context.dataStore by preferencesDataStore(
    name = "settings",
    produceMigrations = listOf(
        SharedPreferencesMigration(context, "old_sp_name")   // 自动读 SP 写入 DataStore
    )
)
```

## 常见坑 / 面试追问

1. **用 Activity Context 创建 DataStore** → Activity 销毁单例失效。必须 `@ApplicationContext`。
2. **在主线程调 `edit`** → `edit` 是 suspend，必须在协程里调（会自动切 IO）。
3. **`intPreferencesKey` 存 String** → 编译报错（这正是类型安全的好处）。
4. **多个 DataStore 实例同名** → 文件冲突崩溃。一个 name 只能有一处 `preferencesDataStore`。
5. **读 Flow 没 `stateIn` 就给 UI** → 每个 UI collect 都重跑读取，浪费。转 StateFlow 共享。
6. **Proto vs Preferences 选型** → 键值少用 Preferences（简单），结构化对象多用 Proto（类型安全，但要写 .proto）。

## 你项目对照

| 概念 | jetpack-android 真实文件 |
|------|------------------------|
| 文件级单例 + 委托 | `data/SettingsDataStore.kt`（`Context.dataStore by preferencesDataStore`） |
| 类型安全 Key | `companion object` 里的 `intPreferencesKey` |
| Flow 读 | `pomodoroDuration`/`themeMode` 等 `data.map { }` |
| suspend 写 | `setPomodoroDuration` 等 `edit { }` |
| Hilt 注入 | `@Singleton class SettingsDataStore @Inject constructor(@ApplicationContext context)` |
| VM 里 stateIn | `viewmodel/SettingsViewModel.kt` |

---

<a id="workmanager"></a>
# 九、WorkManager（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack WorkManager 是 Android 的**后台任务调度框架**，保证「需要可靠执行的后台任务」即使 App 退出/重启后也能完成（如上传日志、同步数据）。
- **解决什么问题**：前台任务用协程即可，但 App 被杀协程就没了；AlarmManager/JobScheduler API 碎片化、版本差异大、不可靠。
- **怎么用**：定义 `Worker`（`doWork()` 里写任务逻辑）；用 `WorkRequest`（OneTime/Periodic）+ `Constraints`（网络/充电）+ `setInputData` 配置；`WorkManager.enqueue()` 提交；可观察 `LiveData<WorkInfo>` 状态。
- **为什么这样用**：WorkManager 自动按系统版本选最优实现（JobScheduler/AlarmManager），保证任务可靠完成；支持约束（仅充电/联网）、重试、链式任务；是持久后台任务的标准方案。

## 什么时候用？

- 上传日志 → 即使 App 退出也要保证上传成功
- 同步数据 → 需要网络条件满足时再执行
- 定期任务 → 每天清理一次缓存
- 约束条件 → 只在充电 + Wi-Fi 时执行

```kotlin
// 1. 定义任务
class UploadWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            uploadLogs()            // 协程，自动切后台线程
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry()  // 自动重试
            else Result.failure()
        }
    }
}

// 2. 提交任务
val request = OneTimeWorkRequestBuilder<UploadWorker>()
    .setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)  // 有网才执行
            .setRequiresCharging(true)                       // 充电时才执行
            .build()
    )
    .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
    .build()

WorkManager.getInstance(context).enqueue(request)

// 3. 定期任务
val dailyRequest = PeriodicWorkRequestBuilder<UploadWorker>(24, TimeUnit.HOURS)
    .build()
WorkManager.getInstance(context).enqueue(dailyRequest)

// 4. 任务链（串行执行）
WorkManager.getInstance(context)
    .beginWith(workA)
    .then(workB)
    .then(workC)
    .enqueue()
```

> **Java 视角**：以前用 Service 做后台任务，但 Android 8+ 限制后台 Service，系统随时会杀。WorkManager 自动选择最优方案（JobScheduler / AlarmManager），保证任务在满足条件时一定会执行，App 退出也不影响。

## Worker 类型选型

| Worker | 适用 | 特点 |
|--------|------|------|
| `Worker` | 简单同步 | 在后台线程跑，要自己管线程 |
| `CoroutineWorker`（推荐） | 协程项目 | `doWork` 是 suspend，可用协程 |
| `RxWorker` | RxJava 项目 | 返回 Single/Rx |
| `ListenableWorker` | 自定义 | 最底层，自己管线程调度 |

```kotlin
// 推荐：CoroutineWorker（你项目用协程，配合最佳）
class UploadWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return try {
            api.uploadLogs()          // suspend，协程里调
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
```

## Constraints 约束详解

```kotlin
Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)   // 联网（UNMETERED=仅 Wi-Fi）
    .setRequiresCharging(true)                        // 充电中
    .setRequiresBatteryNotLow(true)                   // 电量不低
    .setRequiresStorageNotLow(true)                   // 存储不低
    .setRequiresDeviceIdle(false)                     // 设备空闲（Doze）
    .build()
```

- 约束不满足时任务**不执行**，等条件满足再跑。
- `NetworkType.UNMETERED` 适合大文件上传（仅 Wi-Fi，省流量）。

## 重试与退避策略

```kotlin
OneTimeWorkRequestBuilder<UploadWorker>()
    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
    .build()
// doWork 返回 Result.retry() → 按 30s, 60s, 120s... 退避重试
// BackoffPolicy: LINEAR（线性）/ EXPONENTIAL（指数，默认）
// 用 runAttemptCount 限制重试次数，避免无限重试
```

## 任务链：串行 / 并行 / 合并

```kotlin
// 串行：A → B → C
WorkManager.getInstance(ctx).beginWith(workA).then(workB).then(workC).enqueue()

// 并行后合并：A、B 都完成才跑 C
WorkManager.getInstance(ctx)
    .beginWith(listOf(workA, workB))   // A、B 并行
    .then(workC)                        // 都完成才 C
    .enqueue()

// 唯一链：防重复（KEEP 保留旧的 / APPEND 追加 / REPLACE 替换）
WorkManager.getInstance(ctx).enqueueUniqueWork("upload_chain", ExistingWorkPolicy.KEEP, chain)
```

## 观察任务状态（WorkInfo）

```kotlin
WorkManager.getInstance(ctx).getWorkInfoByIdLiveData(request.id)
    .observe(lifecycleOwner) { info ->
        when (info.state) {
            WorkInfo.State.RUNNING   -> showLoading()
            WorkInfo.State.SUCCEEDED -> showDone()
            WorkInfo.State.FAILED    -> showError()
            WorkInfo.State.ENQUEUED  -> showQueued()   // 等待约束满足
            WorkInfo.State.CANCELLED -> showCanceled()
        }
    }
```

## 保活原理 & 何时用 Foreground Service

- **保活原理**：WorkManager 把任务持久化到 SQLite，App 被杀后系统重启时按约束重新调度，任务不会丢。
- **不是所有后台任务都该用 WorkManager**：
  - 不需要可靠（点一下加载个数据）→ 协程就够。
  - 用户可见的即时后台（音乐播放、下载进度）→ **Foreground Service**。
  - 需要可靠完成但不急（上传日志、同步）→ **WorkManager**。
- 长任务可 `setForeground(...)` 提升为前台，避免被系统杀。

## 常见坑 / 面试追问

1. **用 WorkManager 做即时任务** → 它会延迟到约束满足才跑，不适合「立即」需求。用 Foreground Service。
2. **PeriodicWorkRequest 间隔最小 15 分钟** → 系统限制，不能更短。
3. **doWork 里更新 UI** → Worker 不在主线程，不能直接改 UI。用 WorkInfo LiveData 观察状态。
4. **不处理 retry 上限** → 无限重试耗电。用 `runAttemptCount` 限制。
5. **同名 unique work 重复 enqueue** → 按 ExistingWorkPolicy 处理，注意 APPEND 可能乱序。
6. **任务崩溃没结果** → 区分 `Result.failure()`（正常失败，不重试）和异常崩溃（系统按退避重试）。

> **面试题：WorkManager 和 Service 区别？** Service 是组件（前台用户可见、后台 8+ 被限）；WorkManager 是任务调度（持久化、约束、重试，App 杀也能跑）。需要可靠完成的后台任务用 WorkManager，需要持续运行的前台用 Foreground Service。

## 你项目对照

> 你项目（jetpack-android）暂未使用 WorkManager（番茄钟/文件管理都是前台即时任务，用协程即可）。**如果未来加「离线日志上传」或「定期 NAS 同步」**就适合用 WorkManager：定义 `UploadWorker`/`SyncWorker`，配 `NetworkType.CONNECTED` 约束，`PeriodicWorkRequest` 定期跑。

---

<a id="paging3"></a>
# 十、Paging3（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack Paging3 是 Android 的**分页加载库**，让列表「按需加载更多」（滚动到底部自动拉下一页），支持 Room/Retrofit/网络+缓存混合数据源。
- **解决什么问题**：传统分页要手动监听滚动、管理页码、处理加载状态/错误/重试——逻辑散乱易错；一次性加载大量数据内存爆炸。
- **怎么用**：定义 `PagingSource`（单数据源分页）或 `RemoteMediator`（网络+本地缓存）；VM 里 `Pager(config){ }.flow` 得到 `Flow<PagingData>`；UI 用 `LazyColumn` + `items(pagingItems)` 渲染，`collectAsLazyPagingItems` 收集。
- **为什么这样用**：自动管理分页/预加载/状态（加载中/错误/重试）；与 Compose `LazyColumn`、Room、Retrofit 深度集成；`PagingData` 感知列表差异高效更新。

## 传统分页 vs Paging3

```java
// Java 传统分页
int currentPage = 0;
List<Item> allItems = new ArrayList<>();

void loadNextPage() {
    currentPage++;
    api.getItems(currentPage, new Callback() {
        // 手动拼接到列表
        // 手动处理加载状态（loading / error）
        // 还要处理重复点击、并发请求等问题
    });
}
```

```kotlin
// Paging3 声明式分页
class ItemPagingSource : PagingSource<Int, Item>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        return try {
            val page = params.key ?: 1
            val response = api.getItems(page, params.loadSize)
            LoadResult.Page(
                data = response.items,
                prevKey = null,
                nextKey = if (response.hasNext) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

// ViewModel
class ItemViewModel : ViewModel() {
    val flow = Pager(PagingConfig(pageSize = 20)) {
        ItemPagingSource()
    }.flow.cachedIn(viewModelScope)  // 旋转不丢失数据

    // 或者用 LiveData
    val liveData = Pager(PagingConfig(pageSize = 20)) {
        ItemPagingSource()
    }.liveData
}

// Activity 里
val adapter = ItemAdapter()
lifecycleScope.launch {
    viewModel.flow.collectLatest { pagingData ->
        adapter.submitData(pagingData)
    }
}
```

## RemoteMediator：网络 + 缓存

```kotlin
// 既从网络加载，又缓存到 Room
class ItemRemoteMediator(
    private val dao: ItemDao,
    private val api: ItemApi
) : RemoteMediator<Int, Item>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Item>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> state.lastItemOrNull()?.page ?: 1
            }
            val response = api.getItems(page)
            dao.insertAll(response.items)  // 缓存到 Room
            MediatorResult.Success(endOfPaginationReached = response.items.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
```

> **Java 视角**：传统分页要自己处理"是否正在加载"、"还有没有下一页"、"加载失败是否重试"等状态。Paging3 把这一切封装成 `PagingSource`，你只需要告诉它"给定页码，返回数据"即可。

## PagingConfig 配置详解

```kotlin
Pager(
    config = PagingConfig(
        pageSize = 20,                 // 每页大小
        prefetchDistance = 10,         // 距底部 10 条时预加载下一页
        enablePlaceholders = true,     // 先占位（空白）再加载，减少跳动
        initialLoadSize = 40           // 首次加载 40 条（建议是 pageSize 的倍数）
    ),
    pagingSourceFactory = { ItemPagingSource() }
).flow
```

- `pageSize`：每次请求多少。
- `prefetchDistance`：提前多少条触发加载（用户体验关键，太小到底才加载会卡）。
- `enablePlaceholders`：先渲染空占位，数据到了再填，列表高度不跳。
- `initialLoadSize`：首屏多加载点，避免刚进来就触发二次加载。

## 加载状态（LoadState / LoadStateAdapter）

Paging3 提供 `LoadState` 处理「加载中/错误/完成」三种状态（传统分页最头疼的部分）：

```kotlin
// Compose 里
val items: LazyPagingItems<Item> = viewModel.flow.collectAsLazyPagingItems()
when (items.loadState.refresh) {
    is LoadState.Loading   -> LoadingIndicator()
    is LoadState.Error     -> ErrorRetry { items.retry() }
    is LoadState.NotLoading -> LazyColumn { items(items) { ItemRow(it) } }
}
// append 状态：加载下一页
if (items.loadState.append is LoadState.Loading) { item { LoadingFooter() } }
```

- `refresh`：刷新（首次/下拉）状态；`append`：加载下一页；`prepend`：向前加载（少用）。
- **`LoadStateAdapter`**：可给列表加头部/尾部「加载更多」状态项（传统 View 用，Compose 直接判断 loadState）。

## Compose 集成（collectAsLazyPagingItems）

```kotlin
@Composable
fun ItemList(viewModel: ItemViewModel = hiltViewModel()) {
    val items: LazyPagingItems<Item> = viewModel.pagedItems.collectAsLazyPagingItems()
    LazyColumn {
        items(items) { item ->               // items 直接接 LazyPagingItems，自动加载下一页
            ItemRow(item)
        }
        if (items.loadState.append is LoadState.Loading) {
            item { LoadingFooter() }
        }
    }
}
```

## 三层数据源架构

| 层 | 角色 | 何时用 |
|----|------|--------|
| `PagingSource` | 单数据源（仅网络 或 仅 Room） | 数据源单一 |
| `RemoteMediator` | 网络 + Room 缓存混合 | 离线可用 + 网络分页 |
| `BoundaryCallback`（旧） | 已废弃 | 用 RemoteMediator 替代 |

- **仅网络**：`PagingSource` 直接调 API。
- **网络 + 本地缓存**：`RemoteMediator`——REFRESH 时先读 Room 显示，同时拉网络写 Room；APPEND 时拉网络写 Room，Room 通过 `PagingSource` 自动通知 UI。实现离线可看。

## 对照你项目：FileListScreen 手动分页 vs Paging3

你项目 `ui/main/FileListScreen.kt` 现在**手动实现分页**：

```kotlin
// 你项目的手动分页（FileListScreen.kt）
val shouldLoadMore by remember {
    derivedStateOf { lastVisibleItem >= totalItems - 3 }   // 滚到底判断
}
LaunchedEffect(shouldLoadMore) {
    if (shouldLoadMore) viewModel.loadMore()               // 手动调下一页
}
```

**如果用 Paging3 改造**，不需要 `derivedStateOf`/`LaunchedEffect`/`loadMore`/`currentPage` 这套手动逻辑：

```kotlin
// Paging3 改造后
val items = viewModel.pagedFiles.collectAsLazyPagingItems()   // 自动分页
LazyColumn {
    items(items, key = { it.fileId }) { file -> FileItem(file) }   // 自动加载下一页
}
// FileViewModel 里：Pager(PagingConfig(20)){ FilePagingSource(api) }.flow.cachedIn(viewModelScope)
```

> 手动分页约 30 行状态管理（页码/加载状态/错误/重试），Paging3 几行搞定且自带状态。**列表量大、分页复杂时强烈建议 Paging3**；量小简单的列表手动也行。

## 常见坑 / 面试追问

1. **忘了 `cachedIn(viewModelScope)`** → 旋转屏幕数据丢失（PagingData 是冷流，要缓存）。
2. **`PagingSource` 的 `nextKey` 算错** → 加载到错页或无限加载。`nextKey = if (hasNext) page+1 else null`。
3. **`key` 不唯一** → 列表错位（和 LazyColumn 一样要唯一 key）。
4. **`initialLoadSize` 没设** → 默认是 pageSize×3，首屏可能不够。
5. **网络错误不重试** → 用 `items.retry()` 或加重试按钮。
6. **和 Room 一起用直接观察 Room Flow** → 应该用 `RemoteMediator` 统一管，避免网络/本地不一致。

> **面试题：Paging3 为什么用 `cachedIn(viewModelScope)`？** PagingData 是冷流，每次 collect 都重新触发分页；`cachedIn` 把它缓存到 viewModelScope，旋转屏幕不重新分页、数据保留。

## 你项目对照

> 你项目（jetpack-android）暂未使用 Paging3，`FileListScreen` 用手动分页（`derivedStateOf` + `loadMore`）。**如果文件数量增大、需要预加载/离线缓存**，可按上面「改造」一节迁移到 Paging3 + `RemoteMediator`（网络 + Room 缓存）。

---

<a id="savedstatehandle"></a>
# 十一、SavedStateHandle（★★★★）

> [⬆ 返回目录](#catalog)


## 场景

系统杀死 App 后恢复数据（不是旋转，是低内存被系统杀掉）。

```kotlin
// ViewModel 使用 SavedStateHandle
class MyViewModel(
    private val handle: SavedStateHandle  // 自动注入，不需要手动创建
) : ViewModel() {

    var count: Int
        get() = handle["count"] ?: 0
        set(value) { handle["count"] = value }

    // 或者用 LiveData 封装
    val countLiveData: LiveData<Int> = handle.getLiveData("count", 0)
}

// 用法（Activity）
val viewModel: MyViewModel by viewModels()
viewModel.count++  // 旋转/被杀后自动恢复

// 即使系统杀进程：
// SavedStateHandle = Bundle（会存到 onSaveInstanceState 里）
// 下次打开 Activity 时自动恢复数据
```

> **Java 视角**：相当于 `onSaveInstanceState(outState: Bundle)` + `onRestoreInstanceState(savedInstanceState: Bundle)` 的合成，不用再手动写 key-value 序列化。

---

<a id="viewbinding"></a>
# 十二、ViewBinding（★★★★）

> [⬆ 返回目录](#catalog)


## 替代 findViewById

```kotlin
// 1. 在 build.gradle 开启
android {
    viewBinding { enabled = true }
}

// 2. Activity 中使用
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)     // 替代 R.layout.activity_main

        binding.tvTitle.text = "Hello"   // 替代 findViewById(R.id.tv_title)
        binding.btnSubmit.setOnClickListener { ... }
    }
}

// Fragment 中使用
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, ...): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // 防止内存泄漏
    }
}
```

> **Java 视角**：`findViewById` 的问题——类型强制转换不安全、XML id 拼错不报错、每个 View 都要写一行声明。ViewBinding 编译时自动生成 `ActivityMainBinding`，类型安全、空安全。

---

<a id="coroutine"></a>
# 十三、Coroutine（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Kotlin 协程是一种**轻量级线程**，用 `suspend` 函数实现「挂起不阻塞」的异步编程。一行 `viewModelScope.launch { }` 就能启动一个协程。
- **解决什么问题**：传统异步的痛点——`new Thread` 开销大、`AsyncTask` 已废弃、回调嵌套地狱、RxJava 学习曲线陡。协程用**同步写法写异步**，彻底避免回调地狱。
- **怎么用**：在 `viewModelScope` 里 `launch { }` 启动；耗时操作用 `withContext(Dispatchers.IO)` 切线程；`suspend` 函数挂起等待结果；`async` 并发取多个结果再 `await`。
- **为什么这样用**：协程是编译器生成的状态机（非 OS 调度），挂起时让出线程不阻塞；结构化并发让它跟 ViewModel 生命周期绑定，自动取消不泄漏；写法像同步、可读性强。

## 为什么 Jetpack 需要协程？

```
传统 Java 异步：
  new Thread(() -> { ... }).start()           → 线程开销大
  Executors.newFixedThreadPool(5).execute()   → 管理麻烦
  Retrofit Call.enqueue(new Callback() {})    → 回调嵌套
  RxJava Observable                            → 学习成本高

Kotlin 协程：
  viewModelScope.launch(Dispatchers.IO) { ... }  → 轻量、可取消、结构化
```

## 三大核心

```kotlin
// 1. launch：启动协程，无返回值
viewModelScope.launch {
    val data = repository.fetch()  // 挂起，不阻塞线程
    // fetch 执行完成后自动回来，更新 UI
    textView.text = data
}

// 2. async：启动协程，有返回值
viewModelScope.launch {
    val user = async { repository.getUser() }    // 并发
    val posts = async { repository.getPosts() }  // 并发
    // await() 等待两个都完成
    show(user.await(), posts.await())  // 不阻塞，但 suspend 等待
}

// 3. withContext：切线程
viewModelScope.launch {
    val data = withContext(Dispatchers.IO) {
        // IO 线程执行
        repository.fetchFromDatabase()
    }
    // 回到主线程更新 UI
    textView.text = data
}
```

## Dispatchers

| Dispatcher | 作用 | Java 对标 |
|------------|------|-----------|
| `Dispatchers.Main` | 主线程（UI 操作） | Handler.post |
| `Dispatchers.IO` | 文件/网络/数据库 | new Thread 或线程池 |
| `Dispatchers.Default` | CPU 密集型计算 | Executors.newFixedThreadPool |
| `Dispatchers.Unconfined` | 不指定 | 很少用 |

## Structured Concurrency（结构化并发）

```kotlin
// 传统：线程一旦启动就失控
new Thread(() -> {
    while (true) {
        fetchData();  // 没人能取消它
        Thread.sleep(1000);
    }
}).start();

// 协程：自动取消
viewModelScope.launch {
    while (true) {
        fetchData()     // ViewModel 销毁时 cancel
        delay(1000)     // delay 比 Thread.sleep 好：可取消
    }
}
// viewModelScope.cancel() 会自动取消内部所有协程
```

## suspend 函数：挂起的本质

`suspend` 是协程的灵魂——它标记「这个函数可以挂起」，但**挂起 ≠ 阻塞**：

- **挂起**：协程在挂起点保存当前状态（局部变量、执行位置）到 `Continuation`，然后**让出线程**，线程去跑别的协程；恢复时从 `Continuation` 还原状态继续执行。
- **阻塞**：线程停在这里什么都不干（如 `Thread.sleep`），浪费资源。

```kotlin
// 你项目 FileRepository.kt：suspend 函数，内部 withContext 切 IO 线程
suspend fun getFileList(page: Int): Result<List<NasFile>> {
    return withContext(Dispatchers.IO) {   // 挂起到 IO 线程执行，主线程释放
        api.getFileList(page)              // Retrofit 的 suspend 函数，挂起等响应
    }                                       // 恢复，回到调用方线程
}
```

**原理（面试常问）**：编译器对每个 `suspend` 函数做 **CPS（Continuation-Passing Style）变换**，把它编译成一个**状态机**——每个挂起点是一个 state，状态间靠 `Continuation` 传递。所以 `suspend` 函数不是魔法，是编译器生成的状态机 + 回调，只是写起来像同步。

- `suspend` 函数**只能**在协程或别的 `suspend` 函数里调用。
- `suspend` 本身**不切线程**——它只是「可挂起」。真要切线程靠 `withContext(Dispatchers.X)`。
- 你项目里 `PomodoroDao.insert`、`SettingsDataStore.setPomodoroDuration`、`NasApiService.getFileList` 全是 `suspend`。

## 协程作用域（CoroutineScope）与结构化并发（进阶）

**CoroutineScope** 是协程的「容器」，提供 `Job` + `CoroutineContext`。所有协程**必须**在某个 scope 里启动——否则编译器报警告、且无法统一管理。

**结构化并发**的核心规则：
1. 父 Job 取消 → 所有子协程**自动取消**。
2. 子协程未完成 → 父不能完成（父会等子）。
3. 子协程抛异常 → 默认传给父，父取消所有兄弟（可用 `SupervisorJob` 隔离）。

```kotlin
// 你项目 TimerViewModel.kt：timerJob 是 viewModelScope 的子协程
timerJob?.cancel()                              // 只取消计时协程
timerJob = viewModelScope.launch { ... }        // 新建子协程
// ViewModel.onCleared() → viewModelScope.cancel() → timerJob 自动取消
```

**Android 内置 scope**（背下来）：
- `viewModelScope`：绑 ViewModel，`onCleared()` 时自动取消。**写 VM 逻辑必用**。
- `lifecycleScope`：绑 Activity/Fragment 生命周期。
- `rememberCoroutineScope()`：Compose 里取 scope，绑组合生命周期。
- ❌ **禁止 `GlobalScope`**：App 级永不取消，极易内存泄漏。

## Job 与取消

`launch` 返回 `Job`，`async` 返回 `Deferred<T>`（`Job` 的子类，带返回值）。

| 方法 | 作用 |
|------|------|
| `job.cancel()` | 取消（协作式，不立刻停） |
| `job.join()` | 等待完成 |
| `job.cancelAndJoin()` | 取消并等它停 |
| `isActive` | 是否还在运行（取消后变 false） |

**取消是「协作式」的**：调 `cancel()` 只是发个信号，协程要主动响应——`delay/await/yield` 会自动检查并取消；**纯 CPU 循环不会停**，需手动 `if (!isActive) break` 或 `ensureActive()`。

```kotlin
// 你项目 TimerViewModel.kt：delay 会响应取消
timerJob = viewModelScope.launch {
    while (true) {
        delay(1000)   // ← 在这里被取消（VM 销毁时）
        tick()
    }
}
// 如果写成 while(true){ tick() } 不带 delay，cancel 了也不会停 → 死循环泄漏
```

## 异常处理

| 场景 | 处理方式 |
|------|---------|
| `launch` 抛异常 | 传给父 Job，默认 cancel 整个 scope。用 `try/catch` 或 `CoroutineExceptionHandler` |
| `async` 异常 | 存在 `Deferred` 里，`await()` 时才抛（不主动传播） |
| 想让子互不影响 | 用 `SupervisorJob`（`viewModelScope` 内部就是） |
| 全局兜底 | `CoroutineExceptionHandler`（只能放 scope 根） |

```kotlin
// 推荐写法：launch 里 try/catch
viewModelScope.launch {
    try {
        _files.value = repository.getFileList(1)   // 可能抛网络异常
    } catch (e: Exception) {
        _files.value = Result.Error(e.message ?: "未知错误")
    }
}
```

- `viewModelScope` 用 `SupervisorJob`：一个子协程崩了不会连累 VM 里其他协程。
- `CoroutineExceptionHandler` 适合「不期望发生」的异常兜底；业务异常还是 `try/catch` 更可控。

## 协程与 Android 生命周期

| scope | 绑定 | 何时取消 | 用在哪 |
|-------|------|---------|--------|
| `viewModelScope` | ViewModel | `onCleared()` | VM 里所有异步逻辑（最常用） |
| `lifecycleScope` | Activity/Fragment | `onDestroy()` | Activity 级一次性操作 |
| `rememberCoroutineScope()` | Compose 组合 | 离开组合 | Compose `onClick` 里 `launch` |
| `GlobalScope` | App | 永不 | ❌ 别用 |

- 你项目 VM 全用 `viewModelScope.launch`：`TimerViewModel`/`FileViewModel`/`SettingsViewModel`/`TransferViewModel`。
- Compose 里别直接 `scope.launch`，要用 `rememberCoroutineScope()` 或 `LaunchedEffect`（见第十六章第 8 节）。

## delay vs Thread.sleep

| | `delay` | `Thread.sleep` |
|---|---------|---------------|
| 性质 | `suspend` 函数 | 阻塞线程 |
| 阻塞线程？ | ❌ 否，让出线程 | ✅ 是，线程干等 |
| 可取消？ | ✅ 是 | ❌ 否 |
| 用在 | 协程里 | 传统线程 |

```kotlin
// 你项目 TimerViewModel：delay(1000) 每秒 tick 一次，挂起不阻塞主线程
while (true) { delay(1000); tick() }
// TransferViewModel：delay(300) 模拟网络延迟
```

## 常见坑 / 面试追问

1. **主线程做网络/DB** → `NetworkOnMainThreadException`。用 `withContext(Dispatchers.IO)`。
2. **用 `GlobalScope`** → 永不取消，泄漏。改 `viewModelScope`/`lifecycleScope`。
3. **`suspend` 函数里没有真正挂起点** → 仍是同步阻塞（要有 `withContext`/`delay`/`await`）。
4. **取消后纯 CPU 循环不停** → 加 `isActive` 检查或 `ensureActive()`。
5. **`async` 不 `await` 就忽略** → 异常被吞、结果丢失。
6. **在 Composable 里直接 `launch`** → 用 `rememberCoroutineScope()` 或 `LaunchedEffect`。
7. **`launch` 里抛异常未处理** → 崩 App（加 `try/catch` 或 `CoroutineExceptionHandler`）。

> **补充面试题：suspend 函数为什么不阻塞线程？** 编译器把 suspend 函数编译成状态机（CPS 变换）：挂起点处保存局部变量到 `Continuation`，函数返回让出线程；恢复时用 `Continuation` 还原状态继续执行。线程没被阻塞，能去跑别的协程。
>
> **补充面试题：viewModelScope 为什么能自动取消协程？** viewModelScope 绑到 ViewModel，内部用 `SupervisorJob`，在 `ViewModel.onCleared()` 里调 `job.cancel()`，结构化并发保证 cancel 传播给所有子协程。

## 你项目对照总表

| 本节概念 | jetpack-android 真实文件 |
|---------|------------------------|
| `viewModelScope.launch` | `viewmodel/TimerViewModel.kt`、`FileViewModel.kt`、`SettingsViewModel.kt`、`TransferViewModel.kt` |
| `withContext(Dispatchers.IO)` | `data/repository/FileRepository.kt`（`getFileList`） |
| `suspend fun` | `data/PomodoroDao.kt`、`data/SettingsDataStore.kt`、`data/repository/*`、`network/NasApiService.kt` |
| Job 管理与取消 | `viewmodel/TimerViewModel.kt`（`timerJob?.cancel()`） |
| `delay` | `viewmodel/TimerViewModel.kt`（`delay(1000)`）、`viewmodel/TransferViewModel.kt`（`delay(300)`） |

## 面试高频

> **Q: launch 和 async 的区别？**
>
> A: `launch` 启动一个协程，**没有返回值**，适用于"执行完就不需要结果"的场景（如写入数据库）。`async` 返回 `Deferred<T>`，可以通过 `await()` 获取返回值，适用于"需要并发执行并收集结果"的场景。

> **Q: 协程是轻量级线程吗？为什么"轻量"？**
>
> A: 协程运行在线程上，但一个线程可以运行**成千上万个**协程。因为协程是用户态调度（编译器的状态机），不是操作系统调度；协程的挂起不阻塞线程，线程可以继续跑其他协程。

---

<a id="flow"></a>
# 十四、Flow（★★★★★）

> [⬆ 返回目录](#catalog)


## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Kotlin Flow 是协程生态的**数据流 API**，类似 RxJava 的 Observable 但更轻量。`StateFlow` 是它的热流变体，是 LiveData 在 Compose 时代的继任者。
- **解决什么问题**：LiveData 操作符少（只有 map/switchMap）、不支持背压、只能在主线程 observe。Flow 提供全套操作符（map/filter/combine/zip/debounce…）、支持线程切换和背压，且 Room/DataStore/Retrofit 都原生返回 Flow。
- **怎么用**：ViewModel 里用 `MutableStateFlow` 持有状态、`asStateFlow()` 暴露只读；UI 用 `collectAsStateWithLifecycle()` 收集；冷流转热流用 `stateIn`；一次性事件用 `Channel` + `receiveAsFlow`。
- **为什么这样用**：Flow 与协程一体化（`suspend` 收集，天然支持取消和背压）；`StateFlow` 比 LiveData 多了操作符和线程自由度，是 Compose 时代状态承载的标准方案；冷流懒加载省资源、热流可多 UI 共享。

## 从 LiveData 到 Flow

```kotlin
// LiveData：够用但有限
// ① 只能在主线程 observe
// ② 操作符少（只有 map/switchMap）
// ③ 不支持背压

// Flow：协生代替代方案
class MyViewModel : ViewModel() {
    // LiveData 版本
    val userLiveData: LiveData<User> = repository.getUser()

    // StateFlow 版本（推荐替代 LiveData）
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    fun loadUser() {
        viewModelScope.launch {
            // Flow 支持丰富的操作符
            repository.getUserStream()
                .map { it.toUiModel() }           // 转换
                .filter { it.isValid() }           // 过滤
                .catch { _user.value = null }      // 异常处理
                .flowOn(Dispatchers.IO)             // 切线程
                .collect { _user.value = it }
        }
    }
}

// Activity 里收集
lifecycleScope.launch {
    viewModel.user.collect { user ->
        // 每次变化自动收到
        textView.text = user?.name
    }
}
```

## 三大流水类型

```kotlin
// 1. 冷流：每次 collect 都重新执行（懒加载）
val coldFlow = flow {
    emit("只有被 collect 时才执行")
}

// 2. StateFlow：热流，有初始值，保留最新值
//    观察者从 collect 开始收到最新值
val stateFlow = MutableStateFlow("初始值")

// 3. SharedFlow：热流，无初始值，可配置 replay
//    事件通知场景（如导航、Toast）
private val _events = MutableSharedFlow<NavigationEvent>()
val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

fun navigateToHome() {
    viewModelScope.launch {
        _events.emit(NavigationEvent.GoHome)
    }
}
```

## StateFlow vs LiveData

| 维度 | LiveData | StateFlow |
|------|----------|-----------|
| 初始值 | 可为 null（不强制） | **强制**有初始值 |
| 线程 | 主线程 observe | 任意线程 collect |
| 生命周期感知 | ✅ 内置（observe 传 LifecycleOwner） | ❌ 需要 `collectAsStateWithLifecycle()` |
| 操作符 | map / switchMap（有限） | 全套 Flow 操作符 |
| 背压 | 不支持 | 支持（buffer/conflate） |
| 适合场景 | View 层 | ViewModel 层（推荐） |

## 冷流 vs 热流（核心概念，面试必问）

| | 冷流（`Flow`） | 热流（`StateFlow`/`SharedFlow`） |
|---|--------------|------------------------------|
| 生产时机 | **被 collect 时才生产** | 独立于收集者，一直生产 |
| 多个收集者 | 各看各的，各跑一份生产逻辑 | 共享同一份数据流 |
| 类比 | 视频点播（每人各看各的） | 直播（多人看同一路） |
| 典型来源 | `flow{}`、Room DAO 返回的 `Flow` | `MutableStateFlow`、`MutableSharedFlow` |

```kotlin
// 冷流：没收集者就不执行；两个收集者各跑一份
val cold = flow { emit(repo.query()); println("执行了") }   // query 被调几次取决于几个收集者

// 热流：独立存在，所有收集者共享最新值
val hot = MutableStateFlow(0)   // 即使没收集者，值也存在
```

> 你项目里 `PomodoroDao.getAllRecords()` 返回冷流（每次 collect 查一次库）；`HistoryViewModel` 用 `stateIn` 把它转成 StateFlow（热流），多个 UI 共享、且避免重复查库。

## Channel：一次性事件（防粘性）

`StateFlow` 是**粘性**的——新订阅者会立刻收到上一个值。这对「状态」是对的（UI 要最新状态），但对「事件」是错的：

- ❌ 用 `StateFlow` 发「导航到首页」事件 → 旋转屏幕后 UI 重新订阅 → 又收到一次 → **重复导航**。
- ✅ 用 `Channel` 发事件 → 不缓存 → 新订阅者只收订阅后的 → **不会重复**。

```kotlin
// 你项目 TimerViewModel.kt：计时完成弹 Snackbar 用 Channel，不重弹
private val _events = Channel<TimerEvent>(Channel.BUFFERED)   // 不缓存
val events = _events.receiveAsFlow()                          // 转 Flow 给 UI collect

// 计时完成时
_events.trySend(TimerEvent.Finished)

// UI 侧（TimerScreen.kt）
LaunchedEffect(Unit) {
    viewModel.events.collectLatest { event ->   // 旋转屏幕后重新 collect，不会重弹
        when (event) {
            is TimerEvent.Finished -> snackbarHostState.showSnackbar("计时完成")
        }
    }
}
```

> **面试标准答案**：「一次性事件（导航/Toast/Snackbar）用 `Channel` + `receiveAsFlow`，状态用 `StateFlow`」。

## 常用操作符

| 操作符 | 作用 | 典型场景 |
|--------|------|---------|
| `map { }` | 转换每个值 | Entity → UiModel |
| `filter { }` | 过滤 | 只保留有效数据 |
| `onEach { }` | 对每个值做副作用（不改变值） | 打日志 |
| `flatMapLatest { }` | 上游发新值时**取消旧的**处理 | 搜索框输入（新关键词来了取消旧请求） |
| `combine(f2) { a, b -> }` | 合并两个流（任一变化都发） | 用户信息 + 文章列表组合 |
| `zip(f2) { a, b -> }` | 配对合并（两边都来一个才发） | 并行请求等齐 |
| `debounce(300)` | 防抖（停顿 300ms 才发） | 搜索输入 |
| `distinctUntilChanged()` | 去重（值没变不发射） | 避免重复刷新 |
| `catch { }` | 捕获**上游**异常 | 网络错误兜底 |
| `flowOn(Dispatchers.IO)` | 切换上游执行线程 | 网络/DB 操作 |
| `stateIn` / `shareIn` | 冷流转热流 | 多 UI 共享 |

```kotlin
// 组合示例：搜索框防抖 + 切线程 + 转换
searchQuery
    .debounce(300)                       // 停顿300ms才发
    .distinctUntilChanged()              // 关键词没变不重查
    .flatMapLatest { query -> repo.search(query) }   // 新查询取消旧的
    .flowOn(Dispatchers.IO)              // 上游在 IO 线程
    .catch { emit(emptyList()) }         // 出错给空列表
    .collect { results -> show(results) }
```

## 冷流转热流：stateIn / shareIn

冷流每次 collect 都重跑——多个 UI 收集就重跑多次（多次查库/多次请求）。`stateIn` 把冷流转成 `StateFlow`（热流），所有收集者共享一份数据。

```kotlin
// 你项目 HistoryViewModel.kt：Room 冷流转 StateFlow
val records: StateFlow<List<PomodoroEntity>> = repository.getAllRecords()  // 冷流
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),   // 无订阅 5 秒后停（省资源）
        initialValue = emptyList()                         // 初始值（StateFlow 强制要）
    )
```

- `SharingStarted.WhileSubscribed(5000)`：最后一个收集者离开 5 秒后停止上游（推荐，省资源）。
- `SharingStarted.Lazily`：第一个收集者来后永不停（直到 scope 取消）。
- `SharingStarted.Eagerly`：scope 一创建就启动（一般不用）。
- `shareIn` 类似，但转成 `SharedFlow`（事件流，可配 `replay`）。

## 线程切换：flowOn vs withContext

| | `flowOn(Dispatchers.IO)` | `withContext(Dispatchers.IO)` |
|---|------------------------|------------------------------|
| 作用于 | **上游**（生产 + 之前的操作符） | 当前代码块 |
| 用在 | Flow 链上 | 普通协程代码 |
| 注意 | 不影响 `collect` 处 | — |

```kotlin
// ✅ Flow 里用 flowOn
repository.getFiles()
    .map { ... }          // 在 IO 线程执行（flowOn 影响上游）
    .flowOn(Dispatchers.IO)
    .collect { ... }      // 在调用方线程（主线程）执行

// ❌ 不要用 withContext 包 flow{}（会有警告/行为异常）
// withContext(IO) { flow { emit(...) } }   // 别这么写
```

> 你项目 `FileRepository.getFileList` 用 `withContext(Dispatchers.IO)`（因为是普通 suspend 函数，不是 Flow）；而 Flow 链上该用 `flowOn`。两者场景不同。

## 背压：buffer / conflate / collectLatest

当生产快、消费慢时产生**背压**。默认 Flow 是「挂起收集器」流式——生产者等消费者处理完再发下一个。

| 方式 | 作用 |
|------|------|
| `buffer(capacity)` | 加缓冲区，生产和消费**并发**跑（不互相等） |
| `conflate()` | 只保留最新值，跳过中间的（消费慢时丢旧的） |
| `collectLatest { }` | 消费时若有新值来，**取消旧的处理**，处理最新 |

```kotlin
// 你项目 TimerScreen.kt：collectLatest 收 Channel 事件
viewModel.events.collectLatest { event ->   // 新事件来时取消旧的 showSnackbar
    snackbarHostState.showSnackbar(...)     // 如果上一个还没弹完，被打断
}
```

## 异常处理：catch 操作符

| 方式 | 能捕获的范围 |
|------|------------|
| `.catch { }` | 只捕**上游**异常（`catch` 之前的操作符）；下游和 `collect` 块内的异常捕不到 |
| `try { flow.collect{} } catch(e) {}` | 全部（含 collect 块） |

```kotlin
repository.getUserStream()
    .map { it.toUiModel() }
    .catch { emit(UiModel.Error("加载失败")) }   // 捕 map/上游的异常，给个错误值
    .collect { uiModel -> render(uiModel) }     // 这里抛异常 catch 捕不到
```

- `flow{}` 里 `throw` 会让流**终止**；`catch` 后可 `emit` 一个降级值让流继续。
- 异常**透明性**：`catch` 之前的操作符抛异常会向下游传播，`catch` 之后的不会向上传。

## 在 Compose 中收集

| 方式 | 特点 | 用在哪 |
|------|------|--------|
| `collectAsStateWithLifecycle()` | ✅ 感知生命周期，后台不收集 | Compose 生产必用 |
| `collectAsState()` | ⚠️ 不感知生命周期，后台仍收集 | 旧写法，建议升级 |
| `repeatOnLifecycle(STARTED){ collect }` | 传统 View 里用 | Activity/Fragment |

```kotlin
// 你项目 TimerScreen.kt：✅ 推荐
val state by viewModel.uiState.collectAsStateWithLifecycle()

// 你项目 FileListScreen.kt / HistoryScreen.kt：⚠️ 旧写法，建议升级成 WithLifecycle
val files by viewModel.files.collectAsState()
val records by viewModel.records.collectAsState()
```

## 常见坑 / 面试追问

1. **用 StateFlow 发一次性事件** → 粘性导致旋转后重复执行。改 `Channel` + `receiveAsFlow`。
2. **`collectAsState` 不感知生命周期** → 后台仍收集浪费电。用 `collectAsStateWithLifecycle`。
3. **冷流被多 UI collect 多次执行** → 多次查库/请求。用 `stateIn` 转热流共享。
4. **Flow 链里用 `withContext` 切线程** → 应该用 `flowOn`。
5. **`collect` 里抛异常未 catch** → 崩 App。用 `try/catch` 包 collect 或 `.catch{}`。
6. **`SharedFlow` replay 配置错** → 要么丢事件（replay=0），要么重复（replay 太大）。事件流一般 `replay = 0`。
7. **`StateFlow` 初始值乱设** → UI 先收到初始值再收到真实值，会闪一下。初始值要语义合理（如 `Result.Loading`）。

> **补充面试题：StateFlow 和 SharedFlow 区别？** StateFlow 是「有初始值、保留最新值、conflate」的热流，适合**状态**；SharedFlow 是「可配 replay、可多播」的热流，适合**事件**。StateFlow 本质是 `SharedFlow` 的特化（replay=1 + conflate）。
>
> **补充面试题：冷流和热流区别？** 冷流被 collect 时才生产，多收集者各跑一份；热流独立存在，多收集者共享。Room 返回冷流，`MutableStateFlow` 是热流。

## 你项目对照总表

| 本节概念 | jetpack-android 真实文件 |
|---------|------------------------|
| `MutableStateFlow` + `asStateFlow` | `viewmodel/TimerViewModel.kt`（`_uiState`）、`viewmodel/FileViewModel.kt`（`_files`） |
| `Channel` + `receiveAsFlow` 一次性事件 | `viewmodel/TimerViewModel.kt`（`_events`） |
| `stateIn` 冷流转热流 | `viewmodel/HistoryViewModel.kt`（`records`） |
| `collectAsStateWithLifecycle`（推荐） | `ui/timer/TimerScreen.kt` |
| `collectAsState`（旧写法） | `ui/main/FileListScreen.kt`、`ui/history/HistoryScreen.kt`、`ui/settings/SettingsScreen.kt`、`ui/transfer/TransferListScreen.kt` |
| `collectLatest` | `ui/timer/TimerScreen.kt`（收 Channel 事件） |
| Room 返回冷流 | `data/PomodoroDao.kt` → `data/repository/TimerRepository.kt`（`getAllRecords`） |

## 面试高频

> **Q: Flow 和 LiveData 的区别？**
>
> A: ① Flow 是 Kotlin 协生生态的一部分，支持丰富的操作符（map/filter/combine/zip） ② Flow 支持线程切换（flowOn） ③ LiveData 自动感知生命周期，而 StateFlow 需要 `Lifecycle.repeatOnLifecycle` 或 `collectAsStateWithLifecycle` 配合 ④ 很多新组件（Room / DataStore / Retrofit）的 Flow 支持比 LiveData 更好。**推荐原则**：ViewModel 层用 StateFlow，View 层用 `collectAsStateWithLifecycle` 收集。

---

<a id="hilt"></a>
# 十五、Hilt — 依赖注入（★★★★★）★JD 100% 必考

> [⬆ 返回目录](#catalog)


> JD 明确要求 **Hilt** 作为依赖注入方案。本项目（jetpack-android）已用 Hilt 注入 ViewModel/Retrofit/DataStore。原十七章 MVVM 用手写 `ViewModelFactory`，生产环境标准做法是用 Hilt。

## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Hilt 是 Google 基于 Dagger 为 Android 定制的**依赖注入（DI）框架**，用注解（`@Inject`/`@Module`/`@Provides`）声明依赖关系，编译期生成装配代码。
- **解决什么问题**：传统写法在类内部 `new` 依赖——紧耦合、难测试（无法注入 Mock）、难替换、生命周期混乱。DI 把「造依赖」交给容器，类只声明「我要什么」。
- **怎么用**：Application 加 `@HiltAndroidApp` 建容器；Activity/Fragment 加 `@AndroidEntryPoint` 开入口；VM 用 `@HiltViewModel` + `@Inject constructor` 声明依赖；第三方类（Retrofit/Room）在 `@Module` 里用 `@Provides` 教 Hilt 怎么造。
- **为什么这样用**：编译期生成代码（错误早暴露、不靠反射）；结构化作用域（Singleton/ViewModel 级生命周期）；解耦可测试（能注入假实现做单测）；Google 官方与 Android 组件深度集成，是 Android DI 的事实标准。

## 为什么需要依赖注入（DI）？

- 传统写法：`ViewModel` 里 `val repo = TaskRepository(api, db)`，依赖在类内部 `new`——**紧耦合、难测试、难替换**。
- DI 思想：依赖由外部提供（构造传入），类只声明「我需要什么」，不关心怎么造。好处：可测试（注入假实现）、可复用、生命周期统一。

> 光说抽象难懂，看一段「同一段逻辑」的两种写法：

```kotlin
// ❌ 不用 DI：依赖在类内部自己 new（紧耦合、难测试）
class TimerViewModel : ViewModel() {
    // 问题 1：系统构造 ViewModel 时不会给你 Application Context，
    //         这里拿不到 appContext，真实项目里根本写不出来
    private val db = PomodoroDatabase.getInstance(appContext)
    private val dao = db.pomodoroDao()
    private val repo = TimerRepository(dao)   // 自己 new 依赖

    // 问题 2：换测试环境时无法把 repo 换成假实现
    //         → 单元测试必须连真实数据库，又慢又不稳定
    // 问题 3：每个 VM 都自己 new 一套 db/dao → 多重实例、资源浪费
}

// ✅ 用 Hilt：只声明「我需要什么」，怎么造交给容器
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repo: TimerRepository   // 容器自动把单例 TimerRepository 传进来
) : ViewModel()
// TimerRepository 也是 @Inject constructor(dao)，Hilt 会一路递归把 dao 也造好
```

**一句话**：DI 把「谁负责 new 依赖」从「类自己」交给了「框架容器」，类因此变得**纯粹、可替换、可测试**。

## Hilt 核心注解

| 注解 | 作用 |
|------|------|
| `@HiltAndroidApp` | 在 `Application` 上，触发 Hilt 代码生成（必备） |
| `@AndroidEntryPoint` | 标记 Activity/Fragment/View/ViewModel，让其可被注入 |
| `@Inject` | 标记「构造器/字段」需要被注入 |
| `@Module` + `@Provides` | 在模块里提供第三方对象（Retrofit、OkHttp、Room） |
| `@Singleton` / `@ViewModelScoped` | 控制实例作用域（全局单例 / 与 ViewModel 同生命周期） |
| `@HiltViewModel` | 标记 ViewModel，使其构造可注入 Repository |

## 最小可用示例

```kotlin
@HiltAndroidApp
class PomodoroApplication : Application()

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: TimerViewModel by viewModels() // Hilt 自动注入构造依赖
}

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository   // Hilt 自动提供
) : ViewModel()

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun provideRetrofit(): NasApiService =
        Retrofit.Builder().baseUrl(BASE).addConverterFactory(...).build()
            .create(NasApiService::class.java)
}
```

## 每个注解「为什么必须加」（少一个会怎样）

前面示例里那一串注解，不是装饰品——**每一个都对应一个编译期/运行期的硬约束**：

- **`@HiltAndroidApp`（在 Application 上）**：触发 Hilt 代码生成，并建立**全局 DI 容器（SingletonComponent）**。少了它 → 编译报错 `Expected @HiltAndroidApp` 或运行时所有注入为 null。它是整张依赖图的「根」。
- **`@AndroidEntryPoint`（在 Activity/Fragment 上）**：让该组件成为容器的「入口点」，Hilt 才能往里塞依赖（包括 `by viewModels()` 拿到的已注入 VM）。少了它 → 该组件里取不到任何注入对象，运行时空指针/崩溃。
- **`@Inject constructor`（在类构造上）**：告诉 Hilt「用这个构造造我，并递归满足它的参数」。少了它 → Hilt 不知道怎么造这个类，用到时编译报错。
- **`@HiltViewModel`（在 VM 上）**：标记 VM，让 Hilt 把它放进 **ViewModelComponent**（与 VM 生命周期绑定，横竖屏不重建）。少了它 → 只写 `@Inject` 不行，`hiltViewModel()` 找不到这个 VM。
- **`@Module` + `@InstallIn` + `@Provides`（在模块里）**：第三方/没法改源码的类（Retrofit、Room、OkHttp）不能加 `@Inject constructor`，就用 Module 教 Hilt「怎么造」；`@InstallIn` 决定「这个提供方装进哪个容器、哪些组件可见」。
- **`@Singleton`（作用域）**：让提供的实例全局唯一。少了它 → 每次要都 new 一个，既浪费也可能状态不一致。

## Hilt 是怎么「自动装配」的（以你项目为例）

很多人卡在「我没写 new，对象怎么来的？」——答案是：**Hilt 编译期扫描后生成了装配代码，运行期沿依赖树递归把对象造好递给你。**

```
@HiltAndroidApp  PomodoroApplication
  └─ 生成 SingletonComponent（全局 DI 容器）
       ├─ @Provides @Singleton OkHttpClient     ← AppModule
       ├─ @Provides @Singleton Retrofit          （需要 OkHttpClient，Hilt 自动注入）
       ├─ @Provides @Singleton NasApiService     （需要 Retrofit）
       ├─ @Provides @Singleton PomodoroDatabase  （需要 @ApplicationContext）
       └─ @Provides @Singleton PomodoroDao       （需要 Database）

@AndroidEntryPoint  MainActivity
  └─ by viewModels() 取得 TimerViewModel
        └─ TimerViewModel @HiltViewModel @Inject constructor(repo: TimerRepository)
              └─ TimerRepository @Singleton @Inject constructor(dao: PomodoroDao)
                    └─ PomodoroDao  ← 来自 AppModule 的 @Provides
```

**原理**：Hilt 在**编译期**扫描所有 `@Inject` 构造和 `@Provides`，为每个生成「如何制造」的代码（你能在 `build/generated/ksp` 下看到 `xxx_HiltModules`、`PomodoroApplication_HiltComponents` 等生成类）。运行时你 `by viewModels()` 要 VM，Hilt 就沿上面这棵树**递归**把依赖一个个造好、传进去。你写的只是「声明」，装配代码全是生成的。

> 所以「为什么这么用」的终极答案：**你用注解声明依赖关系，Hilt 在编译期把「怎么满足这些依赖」的胶水代码全部生成好，运行期自动注入。你不写胶水，但胶水确实存在。**

## @InstallIn 与 Component 作用域

`@InstallIn(XxxComponent::class)` 决定「这个 Module 提供的对象，能在哪些组件的容器里被用到」。常见 Component：

| Component | 生命周期 | 典型 @InstallIn 位置 |
|-----------|---------|---------------------|
| `SingletonComponent` | 整个 App（= Application） | 全局单例，如 Retrofit / Room |
| `ViewModelComponent` | 单个 ViewModel | VM 内部需要的依赖 |
| `ActivityComponent` | 单个 Activity | Activity 级依赖 |
| `ActivityRetainedComponent` | Activity（横竖屏不重建） | 跨配置变更的依赖 |

- **作用域必须匹配**：一个 `@Singleton` 的提供方只能装进 `SingletonComponent`；若装进更小作用域会**编译报错**。**原则**：模块提供什么级别的实例，就用对应级别的 Component。
- 你项目里 `AppModule` 全部 `@InstallIn(SingletonComponent::class)` + `@Singleton`，因为 Retrofit / Room 都是全局唯一。

## 构造注入 vs 字段注入

```kotlin
// ① 构造注入（@Inject constructor）—— 用于你能改源码的类（VM / Repository / DataStore）
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repo: TimerRepository   // 不可变 val，构造时即注入，易测试
) : ViewModel()

// ② 字段注入（@Inject lateinit var）—— 用于系统构造的类（Activity / Fragment / View）
//    因为不能给系统类加构造参数，只能在字段上标 @Inject，Hilt 在 onCreate 后回填
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var analytics: Analytics
    // ⚠️ 字段注入发生在 onCreate 之后，构造方法 / init 块里不能用 analytics
}
```

## 真实项目对照（你 jetpack-android 的实现）

文档上面那段「最小可用示例」较抽象，下面是你代码里**真实存在的等价实现**，直接对得上：

| 文档概念 | 你项目里的真实文件 |
|---------|------------------|
| `@HiltAndroidApp` | `PomodoroApplication.kt`（类上有 `@HiltAndroidApp`） |
| `@AndroidEntryPoint` | `MainActivity.kt` |
| `@Module @InstallIn(SingletonComponent)` | `di/AppModule.kt`（`provideOkHttpClient` / `provideRetrofit` / `provideNasApiService` / `providePomodoroDatabase` / `providePomodoroDao`，全 `@Provides @Singleton`） |
| `@Singleton @Inject constructor` | `TimerRepository.kt`、`FileRepository.kt`、`SettingsDataStore.kt` |
| `@HiltViewModel @Inject constructor` | `TimerViewModel.kt`、`FileViewModel.kt`、`HistoryViewModel.kt`、`SettingsViewModel.kt` |

你 `AppModule.kt` 头部注释本身也画了链路：`AppModule → OkHttpClient → Retrofit → NasApiService → FileRepository → FileViewModel`，和上面依赖树完全一致。

## 常见坑 / 面试追问

1. **Fragment 也要加 `@AndroidEntryPoint`** —— 很多人只给 Activity 加，Fragment 里一注入就崩。
2. **ViewModel 必须 `@HiltViewModel`** —— 只写 `@Inject` 不行，`hiltViewModel()` / `by viewModels()` 找不到它。
3. **第三方类只能靠 `@Module @Provides`** —— Retrofit / Room / OkHttp 源码不在你手里，没法加 `@Inject constructor`，必须手写提供方式。
4. **`@Provides` 方法的参数 Hilt 自动注入** —— 如 `provideRetrofit(okHttpClient: OkHttpClient)`，不用你手动 new，`okHttpClient` 由同模块的 `@Provides` 满足。
5. **循环依赖报错** —— A 要 B、B 要 A → 编译期失败，需抽接口或重构。
6. **构建与注册** —— module 的 `build.gradle` 需启用 Hilt（ksp + `hilt-android-compiler` + `com.google.dagger.hilt.android` 插件），且 `AndroidManifest.xml` 的 `android:name` 要指向 `@HiltAndroidApp` 那个 Application 类。

> 🔑 一句话记忆：**`@HiltAndroidApp` 建容器 → `@AndroidEntryPoint` 开入口 → `@Inject`/`@HiltViewModel` 声明「我要什么」→ `@Module @Provides` 教 Hilt「怎么造第三方」→ 编译期生成装配代码 → 运行期自动递依赖。**

## Hilt vs Dagger vs Koin（150题·74）

| 维度 | Hilt | Dagger | Koin |
|------|------|--------|------|
| 出身 | Google 官方，基于 Dagger | Google，纯编译期 | 社区，运行时 |
| 与 Android 集成 | ✅ 内置 `@AndroidEntryPoint` | ❌ 需手动写 `AndroidInjector` | ✅ 简单 API |
| 编译速度 | 比 Dagger 慢但省心 | 最快但样板多 | 运行时解析稍慢 |
| 学习曲线 | 中（约定优于配置） | 陡 | 低 |

**面试高频：** Hilt = 「为 Android 量身定制的 Dagger」。99% 的 Android 项目选 Hilt，除非是纯 Kotlin 多平台（KMP）才用 Koin。

---

<a id="compose"></a>
# 十六、Jetpack Compose — 声明式 UI（★★★★★）★JD 100% 必考

> [⬆ 返回目录](#catalog)


> JD 中「Jetpack Compose」为 100% 必选项，且本项目（jetpack-android）全部用 Compose 编写（11 个屏幕）。本章按「为什么 → 核心 API → 原理 → 实战 → 你项目对照」展开，目标是让你**既能在面试讲清原理，又能直接看懂自己项目里每个 Compose 屏幕的每一行**。

## 总览：是什么 · 解决什么 · 怎么用

- **是什么**：Jetpack Compose 是 Android 官方的**声明式 UI 工具包**，用 `@Composable` Kotlin 函数描述 UI，替代传统 XML 布局 + `findViewById`/`ViewBinding` + 命令式 `setText`。
- **解决什么问题**：传统 View 的状态与 UI 不同步（要手动同步所有控件，漏一个就 bug）、UI 难复用（XML 是静态结构）、样板代码多。Compose 让「UI = f(state)」，状态变 UI 自动刷新。
- **怎么用**：写 `@Composable` 函数描述 UI；用 `remember { mutableStateOf() }` 管本地状态；用 `Modifier` 链设样式/布局；状态放 ViewModel，UI 用 `collectAsStateWithLifecycle()` 收集；列表用 `LazyColumn`。
- **为什么这样用**：声明式让状态与 UI 永远一致（不会漏更新）；函数即 UI 天然可复用；编译期生成跳过/重组逻辑保证性能；和 Flutter 思路相通，便于混合架构叙事。

## 1. 为什么需要 Compose（传统 View 的痛点）

传统 Android UI = XML 写布局 + Java/Kotlin `findViewById`/`ViewBinding` 拿控件 + 命令式 `setText/setEnabled/setOnClickListener` 改 UI。痛点：

- **状态与 UI 不同步**：你要手动保证「数据变了 → 所有相关控件都更新」，漏一个就 bug。比如计时器剩 1 秒时按钮该显示「暂停」，忘写 `btnStart.setText("暂停")` 就错了。
- **UI 不可复用**：XML 是静态结构，一段布局想复用得抽 `<include>` 或自定义 View，成本高。
- **样板代码多**：每个屏幕都要 findViewById/ViewBinding + setListener + 手动同步状态。

Compose 的解法：**UI 是 Kotlin 函数，输入状态、输出 UI；状态变，框架自动重新执行函数刷新 UI**——你只描述「当前状态该长什么样」，不用手动改控件。

```
┌────────────────────────────┬────────────────────────────────────────┐
│ 传统 Java (activity_main)  │ Compose (你项目 TimerScreen.kt)        │
├────────────────────────────┼────────────────────────────────────────┤
│ <LinearLayout vertical>    │ Column { }                             │
│ <TextView text="25:00"/>   │ Text(text = state.formattedTime)       │
│ <Button onClick="start"/>  │ Button(onClick = { vm.start() })       │
│ findViewById(R.id.tv_time) │ val state by vm.uiState.collectAsState │
│ tvTime.setText("24:59")    │ state 变化 → Compose 自动重组          │
│ XML 写布局 + Java 写逻辑   │ 全部 Kotlin 代码写                     │
└────────────────────────────┴────────────────────────────────────────┘
```

> 这张对照表直接摘自你 `ui/timer/TimerScreen.kt` 文件头部注释——你项目本身就在用这种「传统→Compose」映射帮自己理解。

## 2. 核心心智模型：UI = f(state)

- **声明式**：你描述「状态 S 对应的 UI 是什么样」，框架负责把 UI 渲染成 S 的样子。状态变 → 框架自动**重组（Recomposition）**受影响的 Composable。
- **对比命令式**：传统 View 是「先建好 UI 树，再手动 `setText/setEnabled` 改它」；Compose 是「给函数一个状态，函数返回 UI，状态变函数自动重跑」。

```kotlin
// 声明式：state.status 变了，when 自动重算，Text 自动更新——你不用手动 setText
Text(
    text = when (state.status) {
        TimerStatus.IDLE, TimerStatus.FINISHED -> "开始"
        TimerStatus.RUNNING -> "暂停"
        TimerStatus.PAUSED -> "继续"
    }
)
// 命令式等价：btnStart.setText(if (running) "暂停" else "开始") —— 漏调就 bug
```

> 上面这段就来自你 `TimerScreen.kt` 第 213-218 行。

- **单向数据流（UDF）**：状态放 ViewModel 向下流，事件（`onClick`）向上回传 ViewModel。UI 永远是状态的纯函数，不持有可变逻辑。

## 3. 第一个 Composable：函数即 UI

```kotlin
@Composable                     // ← 这个注解让函数成为 UI 单元
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name", modifier = modifier)
}
```

- `@Composable` 函数特征：首字母大写、返回 `Unit`（不返回视图）、可在别的 `@Composable` 里调用、参数可以有默认值。
- **`Modifier` 修饰符**：Compose 里几乎所有样式/布局都靠 Modifier 链表达（padding/size/fillMaxWidth/clickable/background…），习惯作为最后一个参数且给默认值 `= Modifier`。

## 4. 布局：Column / Row / Box（对标 LinearLayout / FrameLayout）

| Compose | 传统 View | 作用 |
|---------|----------|------|
| `Column` | 垂直 `LinearLayout` | 子元素纵向排列 |
| `Row` | 水平 `LinearLayout` | 子元素横向排列 |
| `Box` | `FrameLayout` / `Stack` | 子元素层叠（后写的盖在前面） |
| `Spacer(Modifier.height(8.dp))` | `<Space>` / `marginTop` | 留白 |

```kotlin
// 你项目 TimerScreen.kt 的真实结构（精简）
Column(                                          // ≈ 垂直 LinearLayout
    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),  // ≈ match_parent + paddingHorizontal
    horizontalAlignment = Alignment.CenterHorizontally,             // ≈ gravity center_horizontal
    verticalArrangement = Arrangement.Center                        // ≈ layout_gravity center_vertical
) {
    Box(modifier = Modifier.size(260.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(260.dp)) { /* 画环形进度，见第 10 节 */ }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = state.formattedTime, fontSize = 48.sp)
            Text("已完成 ${state.completedSessions} 个番茄")
        }
    }
    Spacer(modifier = Modifier.height(40.dp))
    Row(horizontalArrangement = Arrangement.Center) {
        TextButton(onClick = { viewModel.reset() }) { Text("重置") }
        Button(onClick = { viewModel.start() }) { Text("开始") }
    }
}
```

- `Arrangement` = 子元素在主轴上的排布（Center/SpaceBetween/SpaceEvenly）；`Alignment` = 交叉轴对齐。
- `fillMaxSize()` ≈ `match_parent`，`wrapContentSize()` ≈ `wrap_content`。

## 5. 状态：remember / mutableStateOf / rememberSaveable

**核心问题**：Composable 会在状态变化时被**重新调用（重组）**，函数里的局部变量会重新初始化——不保护的话状态就丢了。

```kotlin
@Composable
fun Counter() {
    // ❌ 普通变量：每次重组都变回 0
    // var count = 0

    // ✅ remember 把值「记住」跨重组保留；mutableStateOf 让它的变化能触发重组
    var count by remember { mutableStateOf(0) }
    Button(onClick = { count++ }) { Text("$count") }
}
```

- `remember { ... }`：在组合里缓存一个对象，重组时复用（不重新执行块）。
- `mutableStateOf(x)`：把 `x` 包成**可观察状态**，读它的 Composable 会被订阅，`x` 变 → 自动重组。
- `by` 委托：`var count by remember { mutableStateOf(0) }` 让你能像普通变量一样 `count++`（否则要 `count.value++`）。
- **`rememberSaveable`**：在 `remember` 基础上额外**持久化到 SavedState**，旋转屏幕/进程被杀恢复后状态还在。`remember` 只保重组，不保进程恢复。

```kotlin
// 你项目 MainTabScreen.kt：底部 Tab 选中状态
var selectedTab by remember { mutableStateOf(0) }   // 重组不丢，够用

// 你项目 TimerScreen.kt：Snackbar 状态
val snackbarHostState = remember { SnackbarHostState() }   // 跨重组复用同一个实例
```

> 选型口诀：**普通跨重组用 `remember`；要扛旋转/进程恢复用 `rememberSaveable`**。

## 6. 派生状态：derivedStateOf（减少重组）

当一个状态是从别的状态**计算**来的，且源状态变化频繁、但你只关心「计算结果是否变了」——用 `derivedStateOf` 降频，避免无谓重组。

```kotlin
// 你项目 FileListScreen.kt 的真实代码：滚动到底部才加载更多
val listState = rememberLazyListState()

val shouldLoadMore by remember {
    derivedStateOf {
        val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        val total = listState.layoutInfo.totalItemsCount
        lastVisible >= total - 3 && total > 0   // 只在「快到底」时为 true
    }
}
LaunchedEffect(shouldLoadMore) {                 // 只在 shouldLoadMore 变化时触发
    if (shouldLoadMore && files is Result.Success) viewModel.loadMore()
}
```

- 没有它：滚动每帧 `visibleItemsInfo` 都变，你会疯狂调用 `loadMore()`。
- 有它：`shouldLoadMore` 只在「false→true」那一刻变，`LaunchedEffect` 才触发一次。
- **原则**：`if (a > threshold)` 这类「从高频源算出低频布尔」就用 `derivedStateOf`。

## 7. 稳定性：@Immutable / @Stable（性能关键）

重组是按「参数是否变了」判断要不要重跑的。但有些类型 Compose 判不准——**判定为「不稳定」就保守地每次都重组**，白白浪费。

```kotlin
// ❌ List<NasFile> 被判不稳定：Compose 不知道列表内容有没有变 → 每次都重组
@Composable
fun FileList(files: List<NasFile>) { ... }   // 父级任何状态变都可能重跑这里

// ✅ 用 @Immutable 告诉 Compose「我的字段永远不会变，相等就是内容相等」
@Immutable
data class NasFile(val fileId: String, val name: String, ...)   // 加了注解
@Composable
fun FileList(files: List<NasFile>) { ... }   // 现在只在 files 真变了才重组
```

- `@Immutable`：承诺所有字段 `val` 且不可变（如 data class 全 val）。
- `@Stable`：字段可变，但变化会通知（`MutableState` 字段等）。
- **常见不稳定类型**：`List`、`Map`、普通 `class`（非 data class）。解法：包成 `@Immutable` data class，或用 `ImmutableList`（kotlinx.collections.immutable）。

## 8. 副作用 API（生命周期感知）

Composable 应该是「无副作用」的纯函数——但现实里你要发网络请求、注册监听、弹 Snackbar。这些**必须用专门的副作用 API**，让框架能在合适的生命周期执行/取消/清理：

| API | 何时执行 | 何时取消/清理 | 典型用途 |
|-----|---------|--------------|---------|
| `LaunchedEffect(key)` | 进入组合 / key 变化 | 离开组合 / key 变化时自动取消协程 | 启动协程收集流、加载更多 |
| `DisposableEffect(key)` | 进入组合 | `onDispose { }` 清理 | 注册/注销监听器 |
| `SideEffect` | 每次成功重组后 | — | 把 Compose 状态同步给非 Compose 对象 |
| `rememberCoroutineScope()` | 取得绑定组合的 scope | 离开组合自动取消 | 在 `onClick` 回调里 `launch` 协程 |
| `rememberUpdatedState(value)` | — | — | 让长效 LaunchedEffect 拿到最新值而不重启 |

```kotlin
// 你项目 TimerScreen.kt：用 LaunchedEffect 收集 Channel 一次性事件（计时完成弹 Snackbar）
val snackbarHostState = remember { SnackbarHostState() }
LaunchedEffect(Unit) {                          // 进入组合启动协程，离开自动取消
    viewModel.events.collectLatest { event ->   // 事件流
        when (event) {
            is TimerEvent.Finished -> {
                snackbarHostState.showSnackbar(
                    message = "计时完成！已完成 ${state.completedSessions} 个番茄"
                )
            }
        }
    }
}
```

> 为什么用 `LaunchedEffect` 而不是直接 `viewModel.events.collect{}`？因为直接 collect 会阻塞组合、且离开页面时不取消会泄漏。`LaunchedEffect` 把协程绑定到组合生命周期，离开页面自动停。

## 9. 列表：LazyColumn（对标 RecyclerView）

`LazyColumn` = Compose 版 `RecyclerView`——只渲染可见项，滚动时复用。

```kotlin
// 你项目 FileListScreen.kt / HistoryScreen.kt 的真实写法
LazyColumn(
    state = listState,
    contentPadding = PaddingValues(vertical = 8.dp)   // 列表整体内边距（不是 item 间距）
) {
    items(result.data, key = { it.fileId }) { file ->  // ← key 防止错位！
        FileItem(file)
    }
}
```

- **`key` 必须给**：列表增删/排序时，`key` 让 Compose 认得「这是同一个 item」从而正确复用、动画。不给 key 会用位置当 key，数据错位、动画错乱。
- `items()` 遍历列表生成 item；还有 `item { }` 放单个项、`itemsIndexed()` 带下标。
- `LazyRow` 是横向版；`LazyVerticalGrid` 是网格版。
- 对照传统：`LazyColumn` ≈ `RecyclerView` + `ListAdapter`，但不用写 `ViewHolder`、不用写 `onCreateViewHolder/onBindViewHolder`。

## 10. 自定义绘制：Canvas（对标 onDraw）

```kotlin
// 你项目 TimerScreen.kt：画环形进度（精简）
Canvas(modifier = Modifier.size(260.dp)) {
    val strokeWidth = 12.dp.toPx()
    // 背景圆环
    drawArc(color = Color(0xFFE0E0E0), startAngle = -90f, sweepAngle = 360f,
            useCenter = false, style = Stroke(width = strokeWidth))
    // 进度弧线（番茄红），sweepAngle 随 state.progress 变
    drawArc(color = Color(0xFFE53935), startAngle = -90f,
            sweepAngle = 360f * state.progress, useCenter = false,
            style = Stroke(width = strokeWidth))
}
```

- `Canvas { ... }` 的 lambda 接收者是 `DrawScope`，能在里面 `drawArc/drawCircle/drawLine/drawRect/drawPath`。
- 对标传统 `View.onDraw(canvas: Canvas)`，但不用继承 View、不用 invalidate——`state.progress` 变 → 重组 → Canvas 重画。
- `dp.toPx()` 把 dp 转像素（DrawScope 里 `12.dp.toPx()`）。

## 11. 主题：MaterialTheme

```kotlin
// 你项目各 Screen 统一用主题取颜色/字号
Text(
    text = "暂无文件",
    style = MaterialTheme.typography.bodyLarge,              // 字号样式
    color = MaterialTheme.colorScheme.error                  // 错误色
)
TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer   // 顶栏背景
))
```

- `MaterialTheme` 通过 `CompositionLocal` 向整棵 UI 树提供 `colorScheme`（颜色）和 `typography`（字号）。
- 切深色模式时 `colorScheme` 自动切到 dark 版，所有引用它的 Composable 自动更新——**不用手写 night 资源**。
- 自定义主题：在 `MaterialTheme(colorScheme = ..., typography = ..., content = { ... })` 里包住你的 App。

## 12. 与 ViewModel / Hilt 集成

```kotlin
// ① 取 ViewModel（Hilt 场景）—— 你项目 FileListScreen.kt / MainTabScreen.kt
@Composable
fun FileListScreen(
    viewModel: FileViewModel = hiltViewModel()   // Hilt 自动注入，默认参数方便预览
) {
    val files by viewModel.files.collectAsState()   // 订阅 StateFlow
}

// ② 收集 Flow 的两种方式
val state by vm.uiState.collectAsStateWithLifecycle()   // ✅ 推荐：感知生命周期，后台不收集
val state by vm.uiState.collectAsState()                // ⚠️ 不感知生命周期，后台仍收集（旧写法）
```

- **`collectAsStateWithLifecycle()`**（来自 `lifecycle-runtime-compose`）：App 进后台时自动停止收集，省电省流量。**生产必用这个，不要裸 `collectAsState`**。
- `hiltViewModel()` 在 Compose 里取被 `@HiltViewModel` 标记的 VM（配合 Hilt 自动注入依赖）。非 Hilt 用 `viewModel()`。
- 对照你项目：`TimerScreen` 用 `collectAsStateWithLifecycle`，而 `FileListScreen`/`HistoryScreen` 用了 `collectAsState`——你可以顺手把它们升级成 `WithLifecycle` 版。

## 13. 导航：Navigation Compose

```kotlin
// 你项目 AppNavHost.kt（精简）
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "timer") {
        composable("timer") { TimerScreen(navController, ...) }
        composable("home")  { HomeScreen(navController) }
        composable("detail/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId")
            DetailScreen(itemId)
        }
    }
}

// 页面里跳转：你项目 TimerScreen.kt
OutlinedButton(onClick = { navController.navigate("home") }) { Text("进入 4 级导航演示") }
```

- `NavHost` 声明路由表，`composable("路由") { 屏幕内容 }` 注册每个页面。
- `rememberNavController()` 取得控制器，`navigate("路由")` 跳转。
- 路由参数：`"detail/{itemId}"` + `arguments.getString("itemId")` 提取。
- 对标传统 `NavGraph` 的 XML，但全是 Kotlin 代码，类型更安全、可重构。

## 14. 重组原理 & 性能优化

**重组不是「整个函数从头跑」**，框架是智能的：

- **可跳过（Skippable）**：如果 Composable 的所有参数都「没变」，框架跳过它的重组。这就是稳定性（第 7 节）重要的原因——不稳定类型无法跳过。
- **可重启（Restartable）**：单个 Composable 可作为重组的独立单元，状态变只重启它，不波及父级。
- **重组是乐观的**：框架可能因状态连续变化而取消进行中的重组，所以 Composable 必须**幂等、无副作用**——这也是副作用必须用专门 API（第 8 节）的原因。

性能优化清单：
1. **给 data class 加 `@Immutable`/`@Stable`**（第 7 节），让框架能跳过。
2. **`derivedStateOf` 降频**（第 6 节），避免高频状态触发重组。
3. **`LazyColumn` 给 `key`**（第 9 节），列表复用正确。
4. **避免在 Composable 里创建不必要的 lambda / 对象**：`remember { }` 缓存计算重的值。
5. **状态读尽量「下放」**：只在真正用到的叶子 Composable 读 State，缩小重组范围。

## 15. 常见坑 / 面试追问

1. **`remember` 不保旋转**：旋转屏幕进程不杀但 Activity 重建，`remember` 的值会丢——要 `rememberSaveable`。
2. **在 Composable 里直接 `collect{}` 会阻塞**：必须用 `collectAsState`/`collectAsStateWithLifecycle` 或在 `LaunchedEffect` 里 collect。
3. **`collectAsState` 不感知生命周期**：进后台仍收集，浪费——生产用 `collectAsStateWithLifecycle`。
4. **`LazyColumn` 不给 `key`**：增删排序时 item 复用错位、动画错乱。
5. **不稳定类型导致全量重组**：`List`/普通 `class` 当参数 → 每次都重组，加 `@Immutable` 或换 `ImmutableList`。
6. **副作用写在 Composable 顶层**：直接 `viewModel.load()` 会在每次重组都执行——必须包进 `LaunchedEffect`。
7. **`mutableStateOf` 没包 `remember`**：`var x = mutableStateOf(0)` 每次重组新建一个，状态丢失——要 `remember { mutableStateOf(0) }`。

## 16. 你项目对照总表

| 本节概念 | jetpack-android 真实文件 |
|---------|------------------------|
| 布局 Column/Row/Box/Canvas | `ui/timer/TimerScreen.kt` |
| `collectAsStateWithLifecycle` + Channel 事件 | `ui/timer/TimerScreen.kt`（`LaunchedEffect`+`collectLatest`） |
| `hiltViewModel()` + `collectAsState` | `ui/main/FileListScreen.kt`、`ui/main/MainTabScreen.kt`、`ui/transfer/TransferListScreen.kt` |
| `remember{mutableStateOf}` 本地状态 | `ui/main/MainTabScreen.kt`（selectedTab）、`ui/main/FileListScreen.kt`（selectedType） |
| `derivedStateOf` + `LaunchedEffect` 加载更多 | `ui/main/FileListScreen.kt`（shouldLoadMore） |
| `LazyColumn` + `items(key)` | `ui/main/FileListScreen.kt`、`ui/history/HistoryScreen.kt`、`ui/transfer/TransferListScreen.kt`、`ui/list/ListScreen.kt` |
| `MaterialTheme` 主题 | 各 Screen（`colorScheme`/`typography`） |
| Navigation Compose | `ui/AppNavHost.kt`、`ui/list/ListScreen.kt`（navigate） |

## Compose vs View 一句话

> XML 是「建好 UI 树再手动改」，Compose 是「描述当前状态对应的 UI，框架负责刷新」。

**面试高频（8年 Java 视角）：** 公司从 XML 切 Compose 的动因——开发效率、状态一致性、动态化、以及和 Flutter 思路相通（你接下来要接的 Flutter 模块），便于混合架构叙事。

---

<a id="repository"></a>
# 十七、Repository（★★★★★）

> [⬆ 返回目录](#catalog)


## 为什么需要 Repository？

```kotlin
// 没有 Repository 的写法：ViewModel 直接调网络和数据库

class WrongViewModel : ViewModel() {
    // ❌ ViewModel 直接依赖两个数据源
    private val api = RetrofitClient.api
    private val dao = AppDatabase.getInstance().userDao()

    fun getUser(id: String) {
        // ❌ ViewModel 里做"先查缓存再查网络"的逻辑
        // 另一个 ViewModel 也要做同样的逻辑 → 重复代码
        // 换数据源（比如从 Room 换 SQLDelight）→ 所有 ViewModel 都要改
    }
}
```

```kotlin
// 有 Repository：统一数据入口
class UserRepository(
    private val api: UserApi = RetrofitClient.api,
    private val dao: UserDao = AppDatabase.getInstance().userDao()
) {
    // 缓存策略：【先缓存 + 强制刷网络】即 stale-while-revalidate
    fun getUser(id: String): Flow<Result<User>> = flow {
        // 1. 先发缓存
        val cached = dao.getUser(id)
        if (cached != null) emit(Result.success(cached))

        // 2. 再查网络（本策略下这一步总会执行，不论上面是否命中缓存）
        try {
            val response = api.getUser(id)
            dao.insert(response)  // 缓存到本地
            emit(Result.success(response))
        } catch (e: Exception) {
            if (cached == null) emit(Result.failure(e))
            // 有缓存就不报错
        }
    }
}

// ViewModel 只依赖 Repository
class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {
    fun getUser(id: String) {
        viewModelScope.launch {
            repository.getUser(id).collect { ... }
        }
    }
    // 如果以后要改数据来源（比如换数据源）
    // 只需要改 Repository，ViewModel 完全不用动
}
```

> **Java 视角**：Repository ≈ DAO 模式（Data Access Object）的升级版。以前每个 Activity 自己直接调 API 或数据库，Repository 把"数据从哪里来、缓存策略是什么"集中到一个地方，ViewModel 只需说"给我数据"，不需要知道数据从哪来。

---

<a id="mvvm"></a>
# 十八、MVVM（★★★★★）

> [⬆ 返回目录](#catalog)


## 完整的 MVVM 实战

```kotlin
// ======= 1. Data Layer =======
// Room Entity
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val completed: Boolean = false
)

// Room DAO
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>    // Room + Flow = 自动刷新

    @Insert
    suspend fun insert(task: Task)

    @Delete
    suspend fun delete(task: Task)
}

// Repository
class TaskRepository(private val dao: TaskDao) {
    val allTasks: Flow<List<Task>> = dao.getAllTasks()

    suspend fun addTask(title: String) {
        dao.insert(Task(title = title))
    }

    suspend fun deleteTask(task: Task) {
        dao.delete(task)
    }
}

// ======= 2. ViewModel =======
data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        // 自动监听数据库变化
        viewModelScope.launch {
            repository.allTasks.collect { tasks ->
                // update 来自 kotlinx.coroutines.flow 的扩展函数 MutableStateFlow.update
                // （需 import kotlinx.coroutines.flow.update；内部用 compareAndSet 原子读改写，避免并发更新互相覆盖）
                // it 是当前 TaskUiState，copy() 由 data class 自动生成，只改 tasks/isLoading 两个字段
                _uiState.update { it.copy(tasks = tasks, isLoading = false) }
            }
        }
    }

    fun addTask(title: String) {
        viewModelScope.launch {
            repository.addTask(title)
            // Room Flow 会自动推送新数据，不需要手动更新 State
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}

// ======= 3. UI Layer =======
class MainActivity : AppCompatActivity() {
    private val viewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(TaskRepository(...))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 收集 StateFlow（需要 lifecycle-aware）
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 自动更新 UI
                    adapter.submitList(state.tasks)
                    progressBar.visibility = if (state.isLoading) VISIBLE else GONE
                }
            }
        }
    }
}
```

## 与传统 MVC 对比

```
MVC（传统）:                          MVVM（Jetpack）:
─────────────────────────            ─────────────────────────
Activity: View + Controller          Activity: 只负责 View
Model: 数据                          ViewModel: 业务逻辑
耦合: 高                             Repository: 数据管理
测试: 难（依赖 Android 环境）         松耦合，可测试性好
数据更新: 手动 setText()             响应式，数据变 UI 自动变
```

---

<a id="vm-livedata"></a>
# 十九、ViewModel + LiveData 标准写法

> [⬆ 返回目录](#catalog)


```kotlin
// 1. Repository：数据层
class UserRepository {
    suspend fun fetchUser(id: String): Result<User> {
        return try {
            val response = RetrofitClient.api.getUser(id)
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// 2. ViewModel：业务层
class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    // Loading / Success / Error 都用 LiveData 表示
    private val _user = MutableLiveData<UserUiState>()
    val user: LiveData<UserUiState> = _user

    fun loadUser(id: String) {
        _user.value = UserUiState.Loading
        viewModelScope.launch {
            repository.fetchUser(id).fold(
                onSuccess = { _user.value = UserUiState.Success(it) },
                onFailure = { _user.value = UserUiState.Error(it.message) }
            )
        }
    }
}

// 3. Activity：UI 层，只负责显示
class UserActivity : AppCompatActivity() {
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        viewModel.user.observe(this) { state ->
            when (state) {
                is UserUiState.Loading -> showLoading()
                is UserUiState.Success -> showUser(state.data)
                is UserUiState.Error -> showError(state.message)
            }
        }
    }
}
```

---

<a id="databinding"></a>
# 二十、DataBinding — 数据绑定（★★★★）

> [⬆ 返回目录](#catalog)


> 150题有 3 道 DataBinding 题（65-67）。它与 ViewBinding 容易混淆：ViewBinding 只做「findViewById 的类型安全替代」，DataBinding 额外支持「布局里直接绑定数据 + 表达式」。

## DataBinding 是什么

- 在 XML 布局里用 `<layout>` 包裹，通过 `@{}` 表达式把数据直接绑到 View 属性，减少 Activity 里的赋值代码。
- 配合 `BaseObservable` / `ObservableField` 或 `LiveData` 可双向刷新。

```xml
<layout>
  <data>
    <variable name="user" type="com.x.User" />
  </data>
  <TextView android:text="@{user.name}" />
</layout>
```

## 双向绑定与 BindingAdapter（150题·66）

- 双向绑定：`android:text="@={viewModel.name}"`（注意 `=`），用户输入自动写回数据。
- `@BindingAdapter("imageUrl")`：自定义属性绑定逻辑（如 Glide 加载图片），是 DataBinding 最常用扩展点。

```kotlin
@BindingAdapter("imageUrl")
fun ImageView.load(url: String) = Glide.with(this).load(url).into(this)
```

## DataBinding vs ViewBinding（150题·67）

| 维度 | ViewBinding | DataBinding |
|------|-----------|------------|
| 能力 | 仅类型安全访问 View | 数据绑定 + 表达式 + 双向 |
| 编译开销 | 小 | 大（需处理表达式） |
| 适用 | 新项目首选 | 需要布局直接绑数据时用 |

**面试高频：** 现代项目**优先 ViewBinding + StateFlow/Compose**，DataBinding 表达式能力易被 Compose 取代；但若维护老项目或大量表单双向绑定，DataBinding 仍有价值。

---

<a id="interview"></a>
# 二十一、Jetpack 面试最高频问题

> [⬆ 返回目录](#catalog)


## 1. ViewModel 为什么不会因为旋转销毁？

ViewModel 实例保存在 `ViewModelStore` 中，`ViewModelStore` 存储在 Activity 的 `NonConfigurationInstances` 对象里。Activity 旋转时触发 `onDestroy()` 但不触发 `finish()`，系统不会清理 `NonConfigurationInstances`。新 Activity 从同一个 `ViewModelStore` 中取出之前的 ViewModel。

## 2. LiveData 为什么不会内存泄漏？

`LiveData.observe(LifecycleOwner, Observer)` 会给 `LifecycleOwner` 添加 `LifecycleObserver`，当 `LifecycleOwner` 状态变为 `DESTROYED` 时，LiveData 自动移除 Observer。所以即使 Observer 持有 View 引用，Activity 销毁时引用链也会断开。

## 3. setValue 和 postValue 区别？

`setValue()` 必须在**主线程**调用，立即更新数据并通知观察者。`postValue()` 可以在**任意线程**调用，内部通过 Handler 切到主线程再更新。注意 `postValue()` 会覆盖中间值——连续两次 postValue 只会保留最后一次。

## 4. Room 和 SQLiteOpenHelper 区别？

| Room | SQLiteOpenHelper |
|------|----------------|
| 编译时 SQL 校验 | SQL 写错运行时才崩溃 |
| 自动生成代码 | 手写 Cursor + ContentValues |
| 返回 Flow/LiveData，自动刷新 | 手动监听变化 |
| 自动处理线程切换 | 手动切 IO 线程 |
| 内置 Migration 管理 | 手写升级逻辑 |

## 5. DataStore 为什么替代 SharedPreferences？

① **异步**：SP 的 `getString()` 可能同步读文件卡主线程；DataStore 完全基于 `Flow` / `suspend`。② **类型安全**：Proto DataStore 根据 schema 生成类。③ **异常安全**：SP 文件损坏时静默吞掉异常；DataStore 抛出 `CorruptionException`，保证数据一致性。

## 6. WorkManager 什么时候用？

需要**保证执行**的后台任务——上传日志、同步数据、定期清理缓存。即使 App 退出、系统重启，WorkManager 保证在条件满足时执行。**不适合**：即时任务（用协程）、短时间延迟（用 Handler/ AlarmManager）。

## 7. Paging3 原理？

核心是 `PagingSource` 声明式分页：传入 `LoadParams`（页码、加载数量），返回 `LoadResult.Page`（数据列表、下一页 key）。`Pager` 负责调度，`PagingData` 携带数据流，`PagingDataAdapter` 自动 DiffUtil 更新 RecyclerView。

## 8. Navigation 优点？

统一管理 Fragment 跳转：① 可视化导航图 ② SafeArgs 保证类型安全 ③ 自动返回栈管理 ④ DeepLink 支持 ⑤ 与 Toolbar 自动集成。

## 9. Repository 为什么存在？

集中管理数据来源和缓存策略。ViewModel 不需要知道数据来自网络还是本地缓存，只需要说"给我数据"。换数据源时只需改 Repository。

## 10. MVVM 为什么比 MVC 好？

MVVM 三层职责分明：View 只显示 UI，ViewModel 管业务逻辑，Repository 管数据。每层可独立测试。View 和 ViewModel 通过 LiveData/Flow 解耦，ViewModel 不持有 View 引用，避免内存泄漏。

---

<a id="arch"></a>
# 二十二、企业项目标准架构

> [⬆ 返回目录](#catalog)


```
┌─────────────────────────────────────────────────────┐
│  Presentation Layer                                 │
│  Activity / Fragment / Compose UI                   │
│  └── 只负责渲染、事件回调、observe/collect 数据      │
├─────────────────────────────────────────────────────┤
│  ViewModel Layer                                    │
│  └── 持有 UiState（StateFlow/LiveData）              │
│      调用 Repository，不直接接触网络/数据库           │
├─────────────────────────────────────────────────────┤
│  Repository Layer                                   │
│  └── 封装数据来源策略（先缓存→再网络）               │
├──────────────────┬──────────────────────────────────┤
│  Data Layer       │  Data Layer                      │
│  Remote           │  Local                           │
│  Retrofit/Ktor    │  Room/DataStore                  │
│  OkHttp           │  File/SharedPreferences(旧)      │
└──────────────────┴──────────────────────────────────┘
```

**依赖关系（从上往下，单行）：**
> UI → ViewModel → Repository → DataSource（Remote / Local）

---

<a id="must"></a>
# 二十三、Android 岗位必须掌握（★★★★★）

> [⬆ 返回目录](#catalog)


## 第一梯队（必须熟练）★JD 100% 要求

- Kotlin 语言
- MVVM / MVI 架构
- ViewModel
- LiveData / StateFlow
- Lifecycle
- Coroutine
- Flow
- Retrofit + OkHttp
- Room
- **Jetpack Compose（声明式 UI，JD 显性要求）**
- **Hilt（依赖注入，JD 硬性要求）**
- Navigation（含 Navigation Compose）
- ViewBinding

## 第二梯队（最好会）

- DataStore
- Paging3
- WorkManager
- Glide / Coil
- SharedFlow（一次性事件）
- DataBinding（维护老项目）

## 第三梯队（了解即可）

- CameraX
- Benchmark
- 鸿蒙 ArkUI（看公司方向，你的目标产品线强相关）

---

<a id="roadmap"></a>
# 二十四、推荐学习路线（7天速成）

> [⬆ 返回目录](#catalog)


**Day 1**：Lifecycle + ViewModel + LiveData（理解生命周期感知）
**Day 2**：MVVM 架构 + Repository 模式（搭建完整数据流）
**Day 3**：Room + DataStore（本地持久化）
**Day 4**：Coroutine + Flow（异步编程，替代回调）
**Day 5**：Hilt 依赖注入 + Navigation / Navigation Compose（DI + 页面管理）
**Day 6**：Jetpack Compose 声明式 UI（@Composable / 状态 / 重组 / 副作用 API）+ WorkManager
**Day 7**：整体架构串联（Compose + Hilt + ViewModel + Flow）+ 高频面试题复盘 + 手写 MVVM Demo

---

<a id="memo"></a>
# 二十五、核心记忆图

> [⬆ 返回目录](#catalog)


```
UI (Activity / Fragment / Compose)
  │   observe() / collect()
  ▼
ViewModel
  │   viewModelScope.launch {}
  │   持有 UiState (StateFlow / LiveData)
  ▼
Repository ─── 缓存策略
  ┌──────┴──────┐
  ▼             ▼
Remote        Local
(Retrofit)    (Room / DataStore)
  │             │
  └──────┬──────┘
         ▼
       Service / Server
```

---

<a id="summary"></a>
# 总结

> [⬆ 返回目录](#catalog)


真正企业开发中，Jetpack 并非所有组件都会用到，但以下组件几乎是 Android 开发的标配：

- **Lifecycle** → 自动管理生命周期
- **ViewModel** → UI 数据在旋转/销毁时保留
- **LiveData / StateFlow** → 响应式数据推送
- **Coroutine + Flow** → 异步编程基础设施
- **Room** → 类型安全的本地数据库
- **Navigation** → 统一页面管理
- **DataStore** → SP 替代方案
- **WorkManager** → 可靠后台任务
- **Repository** → 统一数据入口
- **ViewBinding** → 类型安全 findViewById

掌握以上内容，可以覆盖绝大多数 Android 应用开发和中高级 Android 岗位面试。
