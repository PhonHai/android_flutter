# Android 高频面试题（100题）

> 适用岗位： Android 高级开发工程师 
> 技术栈：Android + Jetpack + Kotlin + Flutter + 鸿蒙（HarmonyOS） 
> 业务场景：NAS 私有云 APP、音频设备 APP、IoT 智能硬件 
> 文档说明：模拟真实面试层层递进对话，每题含考察点与易踩坑警示

---

## 目录

### 一、Kotlin 核心能力（18题）

- [1. Kotlin 中 `lateinit` 和 `by lazy` 的区别及使用场景](#1-kotlin-中-lateinit-和-by-lazy-的区别及使用场景)
- [2. Kotlin 协程的 `launch`、`async`、`withContext` 的区别](#2-kotlin-协程的-launchasyncwithcontext-的区别)
- [3. Flow 的冷流和热流区别，StateFlow 和 SharedFlow 的应用场景](#3-flow-的冷流和热流区别stateflow-和-sharedflow-的应用场景)
- [4. Kotlin 密封类的实际应用（结合网络请求 Result 封装）](#4-kotlin-密封类的实际应用结合网络请求-result-封装)
- [5. Kotlin 扩展函数的原理和注意事项](#5-kotlin-扩展函数的原理和注意事项)
- [6. 协程的异常处理机制（CoroutineExceptionHandler vs try-catch）](#6-协程的异常处理机制coroutineexceptionhandler-vs-try-catch)
- [7. Kotlin 高阶函数和内联函数（inline/noinline/crossinline）](#7-kotlin-高阶函数和内联函数inlinenoinlinecrossinline)
- [8. Kotlin 作用域函数（let/run/apply/also/with）的选择原则](#8-kotlin-作用域函数letrunapplyalsowith的选择原则)
- [9. Kotlin 泛型型变（in/out）的理解](#9-kotlin-泛型型变inout的理解)
- [10. Kotlin 委托属性的原理（by 关键字）](#10-kotlin-委托属性的原理by-关键字)
- [11. 结构化并发（structured concurrency）的理解](#11-结构化并发structured-concurrency的理解)
- [12. Kotlin 中的 `object` 关键字的三种用法](#12-kotlin-中的-object-关键字的三种用法)
- [13. Kotlin vs Java 在空安全上的优势](#13-kotlin-vs-java-在空安全上的优势)
- [14. Kotlin Flow 的背压（backpressure）处理](#14-kotlin-flow-的背压backpressure处理)
- [15. `suspendCoroutine` 和 `suspendCancellableCoroutine` 的区别](#15-suspendcoroutine-和-suspendcancellablecoroutine-的区别)
- [16. Kotlin Channel 的使用场景](#16-kotlin-channel-的使用场景)
- [17. Kotlin `reified` 关键字的原理](#17-kotlin-reified-关键字的原理)
- [18. 协程取消的协作机制（isActive、yield、ensureActive）](#18-协程取消的协作机制isactiveyieldensureactive)

### 二、Jetpack 核心组件（22题）

- [19. ViewModel 如何做到旋转屏幕不丢失数据？源码级别说明](#19-viewmodel-如何做到旋转屏幕不丢失数据源码级别说明)
- [20. ViewModel 和 AndroidViewModel 的区别和使用场景](#20-viewmodel-和-androidviewmodel-的区别和使用场景)
- [21. ViewModel 的 onCleared 什么时候调用？如何手动触发？](#21-viewmodel-的-oncleared-什么时候调用如何手动触发)
- [22. SavedStateHandle 的用法和原理](#22-savedstatehandle-的用法和原理)
- [23. LiveData 的 setValue 和 postValue 的区别](#23-livedata-的-setvalue-和-postvalue-的区别)
- [24. LiveData 粘性事件问题如何解决？](#24-livedata-粘性事件问题如何解决)
- [25. LiveData 和 Kotlin Flow 如何选择？](#25-livedata-和-kotlin-flow-如何选择)
- [26. Lifecycle 的 5 种 State 和 7 种 Event 的映射关系](#26-lifecycle-的-5-种-state-和-7-种-event-的映射关系)
- [27. 如何监听应用前后台切换？（ProcessLifecycleOwner）](#27-如何监听应用前后台切换processlifecycleowner)
- [28. Room 数据库的 `@Entity` 注解参数详解](#28-room-数据库的entity-注解参数详解)
- [29. Room 如何进行数据库版本升级（Migration）](#29-room-如何进行数据库版本升级migration)
- [30. Room 如何实现一对多、多对多关系？](#30-room-如何实现一对多多对多关系)
- [31. Room + Flow 实现响应式数据查询](#31-room--flow-实现响应式数据查询)
- [32. Navigation 组件的 Safe Args 如何使用？](#32-navigation-组件的-safe-args-如何使用)
- [33. DataBinding 的优缺点和使用注意事项](#33-databinding-的优缺点和使用注意事项)
- [34. Hilt 依赖注入的基本使用](#34-hilt-依赖注入的基本使用)
- [35. Paging 3 的分页加载原理](#35-paging-3-的分页加载原理)
- [36. WorkManager 如何保证任务一定执行？](#36-workmanager-如何保证任务一定执行)
- [37. DataStore vs SharedPreferences 如何选择？](#37-datastore-vs-sharedpreferences-如何选择)
- [38. Startup 库的初始化优化原理](#38-startup-库的初始化优化原理)
- [39. AppCompat 和 Material Design 组件使用经验](#39-appcompat-和-material-design-组件使用经验)
- [40. CameraX 的使用经验（结合 IoT 设备扫码场景）](#40-camerax-的使用经验结合-iot-设备扫码场景)

### 三、MVVM 架构实践（10题）

- [41. MVVM 三层（Model/View/ViewModel）职责如何划分？](#41-mvvm-三层modelviewviewmodel职责如何划分)
- [42. Repository 模式的设计要点](#42-repository-模式的设计要点)
- [43. 如何设计一个可复用的网络请求封装（Retrofit + 协程 + Flow）](#43-如何设计一个可复用的网络请求封装retrofit--协程--flow)
- [44. MVVM 中如何处理一次性事件（Toast/Navigation）？](#44-mvvm-中如何处理一次性事件toastnavigation)
- [45. 多模块项目如何管理依赖和通信？](#45-多模块项目如何管理依赖和通信)
- [46. Clean Architecture 在 Android 中的实践](#46-clean-architecture-在-android-中的实践)
- [47. 如何进行组件化/模块化拆分？](#47-如何进行组件化模块化拆分)
- [48. MVI 架构与 MVVM 的对比](#48-mvi-架构与-mvvm-的对比)
- [49. 单向数据流（UDF）的实际项目实践](#49-单向数据流udf的实际项目实践)
- [50. 如何进行依赖注入框架选型（Hilt vs Koin）？](#50-如何进行依赖注入框架选型hilt-vs-koin)

### 四、网络与数据层（10题）

- [51. OkHttp 的拦截器链机制](#51-okhttp-的拦截器链机制)
- [52. Retrofit 的动态代理实现原理](#52-retrofit-的动态代理实现原理)
- [53. 如何设计网络请求的缓存策略？](#53-如何设计网络请求的缓存策略)
- [54. HTTPS 的 SSL Pinning 实现](#54-https-的-ssl-pinning-实现)
- [55. WebSocket 在 IoT 场景的使用](#55-websocket-在-iot-场景的使用)
- [56. Protobuf vs JSON 在 IoT 场景的选择](#56-protobuf-vs-json-在-iot-场景的选择)
- [57. Room + Flow 实现离线优先策略](#57-room--flow-实现离线优先策略)
- [58. 文件上传下载的断点续传设计](#58-文件上传下载的断点续传设计)
- [59. 多线程下载的实现原理](#59-多线程下载的实现原理)
- [60. 如何进行网络请求的重试和降级？](#60-如何进行网络请求的重试和降级)

### 五、多线程与并发（6题）

- [61. Android 中的线程池如何设计？线程数量如何确定？](#61-android-中的线程池如何设计线程数量如何确定)
- [62. Handler 机制源码分析](#62-handler-机制handlerloopermessagequeuemessage源码分析)
- [63. IntentService 和 JobIntentService 的区别](#63-intentservice-和-jobintentservice-的区别)
- [64. Thread.sleep、Object.wait、协程 delay 的区别](#64-threadsleepobjectwait协程-delay-的区别)
- [65. 多个网络请求并发，如何等待全部完成后更新 UI？](#65-多个网络请求并发如何等待全部完成后更新-ui)
- [66. synchronized、volatile、Atomic 在 Android 中的应用](#66-synchronizedvolatileatomic-在-android-中的应用)

### 六、性能优化（10题）

- [67. APP 启动优化（冷启动/热启动）有哪些方案？](#67-app-启动优化冷启动热启动有哪些方案)
- [68. 内存泄漏的常见场景和排查方法](#68-内存泄漏的常见场景和排查方法)
- [69. RecyclerView 的卡顿优化方案](#69-recyclerview-的卡顿优化方案)
- [70. Bitmap 的优化策略（质量压缩/采样率/缓存）](#70-bitmap-的优化策略质量压缩采样率缓存)
- [71. ANR 的产生原因和排查思路](#71-anr-的产生原因和排查思路)
- [72. APK 包体积优化方案](#72-apk-包体积优化方案)
- [73. 如何监控线上卡顿和崩溃？](#73-如何监控线上卡顿和崩溃)
- [74. LayoutInflater 的优化（AsyncLayoutInflater）](#74-layoutinflater-的优化asynclayoutinflater)
- [75. WebView 的内存泄漏如何解决？](#75-webview-的内存泄漏如何解决)
- [76. 电量优化的常见方案](#76-电量优化的常见方案)

### 七、IoT 与蓝牙通信（8题）—— IoT 核心加分项

- [77. Android BLE（低功耗蓝牙）的通信流程](#77-android-ble低功耗蓝牙的通信流程)
- [78. 蓝牙 GATT 协议的理解](#78-蓝牙-gatt-协议的理解)
- [79. 蓝牙扫描和连接的最佳实践](#79-蓝牙扫描和连接的最佳实践)
- [80. 如何实现蓝牙设备的长连接和断线重连？](#80-如何实现蓝牙设备的长连接和断线重连)
- [81. 多设备蓝牙连接的管理方案](#81-多设备蓝牙连接的管理方案)
- [82. 蓝牙通信的安全性考虑](#82-蓝牙通信的安全性考虑)
- [83. IoT 设备 OTA 升级的 APP 端实现](#83-iot-设备-ota-升级的-app-端实现)
- [84. MQTT 协议在 IoT 场景的应用](#84-mqtt-协议在-iot-场景的应用)

### 八、Flutter 与跨平台（6题）—— 跨平台技术栈

- [85. Flutter 的架构分层（Framework/Engine/Embedder）](#85-flutter-的架构分层frameworkengineembedder)
- [86. Flutter 的 Widget、Element、RenderObject 三棵树](#86-flutter-的-widgetelementrenderobject-三棵树)
- [87. Flutter 与 Android 原生通信（Platform Channel）](#87-flutter-与-android-原生通信platform-channel)
- [88. Flutter 的状态管理方案（Provider/Riverpod/Bloc）](#88-flutter-的状态管理方案providerriverpodbloc)
- [89. Flutter 混合开发的集成方式](#89-flutter-混合开发的集成方式)
- [90. 从 Android 原生转到 Flutter 开发需要注意什么？](#90-从-android-原生转到-flutter-开发需要注意什么)

### 九、鸿蒙开发（4题）—— 跨平台技术栈

- [91. 鸿蒙的 Ability 组件与 Android Activity 的对比](#91-鸿蒙的-ability-组件与-android-activity-的对比)
- [92. 鸿蒙的 ArkTS 语言与 Kotlin 的对比](#92-鸿蒙的-arkts-语言与-kotlin-的对比)
- [93. 鸿蒙的分布式能力在实际产品中的应用](#93-鸿蒙的分布式能力在实际产品中的应用)
- [94. Android 项目迁移到鸿蒙的关键步骤](#94-android-项目迁移到鸿蒙的关键步骤)

### 十、软技能与项目经验（6题）

- [95. 描述一个你解决过的最难的技术问题（STAR 法则）](#95-描述一个你解决过的最难的技术问题star-法则)
- [96. 如何进行代码审查（Code Review）？](#96-如何进行代码审查code-review)
- [97. 如何保证线上版本的稳定性？](#97-如何保证线上版本的稳定性)
- [98. 技术方案评审时你会关注哪些方面？](#98-技术方案评审时你会关注哪些方面)
- [99. 如何平衡开发效率和代码质量？](#99-如何平衡开发效率和代码质量)
- [100. 你对公司产品有什么了解？为什么选择我们？](#100-你对公司产品有什么了解为什么选择我们)

---

## 一、Kotlin 核心能力（18题）

### 1. Kotlin 中 `lateinit` 和 `by lazy` 的区别及使用场景

**【面试官提问】** 在 Kotlin 开发中，你经常会用到 `lateinit` 和 `by lazy` 来延迟初始化属性。请说说它们的区别，以及在云存储 APP 中分别适合用在什么场景？

**【候选人回答】** 两者本质上有三个维度的区别：

**1. 初始化时机不同：**
- `lateinit`：声明时仅标记为"稍后初始化"，首次访问时不会自动初始化，必须手动赋值。如果在赋值前访问，会抛出 `UninitializedPropertyAccessException`。
- `by lazy`：首次访问属性时自动执行 lambda 表达式完成初始化，默认线程安全（`LazyThreadSafetyMode.SYNCHRONIZED`）。

**2. 可变性不同：**
- `lateinit` 修饰 `var`，后续可以重新赋值。
- `by lazy` 修饰 `val`，一旦初始化就不能再改变。

**3. 适用类型不同：**
- `lateinit` 只能修饰非空类型，且不能用于基本类型（Int、Long 等），也不能用于可空类型。
- `by lazy` 可以用于任何类型。

**在NAS 私有云 APP 中的实际场景：**

```kotlin
// lateinit 典型场景：依赖注入的对象，在 onCreate 中初始化
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
 @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
 private lateinit var binding: ActivityMainBinding
 
 override fun onCreate(savedInstanceState: Bundle?) {
 binding = ActivityMainBinding.inflate(layoutInflater)
 // binding 在 onCreate 中确定，后续不再改变（但语法上是 var）
 }
}

// by lazy 典型场景：计算密集或需要 Context 的对象
class NasFileRepository(private val context: Context) {
 // 数据库实例，只在首次访问时才创建，且永不改变
 private val database by lazy {
 AppDatabase.getInstance(context)
 }
}
```

**【面试官追问】** 你说 `by lazy` 默认是线程安全的，那如果我知道自己只在单线程使用，能优化性能吗？`lateinit` 有没有类似的线程安全问题？

**【候选人回答】**

可以优化。`lazy` 函数接受一个 `mode` 参数：

```kotlin
// 默认：线程安全，使用同步锁
val db by lazy { AppDatabase.getInstance(context) }

// 单线程模式：不做同步，性能更高
val db by lazy(LazyThreadSafetyMode.NONE) { AppDatabase.getInstance(context) }

// 发布模式：允许多线程同时初始化，但只使用第一个完成的结果
val db by lazy(LazyThreadSafetyMode.PUBLICATION) { AppDatabase.getInstance(context) }
```

`lateinit` 本身不保证线程安全。如果多线程同时访问未初始化的 `lateinit` 属性，可能同时触发初始化逻辑导致竞态条件。通常通过 `::property.isInitialized` 检查，但检查与使用之间仍有时间窗口。最佳实践是在单一线程（如主线程 `onCreate`）完成 `lateinit` 的初始化赋值。

**【考察点与得分技巧】** 面试官真正想考察的：

1. **不只背概念，要结合场景**：能说出在IoT 应用这种 IoT APP 中，初始化 NAS 数据库连接用 `lazy`，注入 ViewModel/ViewBinding 用 `lateinit`。
2. **理解底层差异**：`lateinit` 编译后是给 backing field 加 null 标记，`by lazy` 编译后生成一个 `Lazy` 委托对象。
3. **线程安全有意识**：能说清楚各自的线程安全风险是加分项。

**【易踩坑警示】**
- 常见错误：用 `lateinit` 声明 `Int` 类型，编译直接报错。
- 常见错误：在 Fragment 中 `lateinit var binding`，但在 `onDestroyView` 后仍可能被访问（因为 Fragment 实例可能存活），导致 crash。应该在 `onDestroyView` 中将 binding 置为 null（用可空类型）。
- 常见错误：认为 `by lazy` 一定延迟到首次访问，但在构造时如果作为初始化参数传递，可能提前触发。

---

### 2. Kotlin 协程的 `launch`、`async`、`withContext` 的区别

**【面试官提问】** 协程中有三个常用的启动方式：`launch`、`async` 和 `withContext`。请说明它们的区别，以及在NAS 私有云 的文件列表加载场景中如何选择？

**【候选人回答】**

核心区别在于**返回值**和**用途**：

| 特性 | `launch` | `async` | `withContext` |
|------|----------|---------|---------------|
| 返回值 | `Job` | `Deferred<T>` | `T`（直接返回结果） |
| 用途 | 执行不关心返回值的任务 | 并发执行并获取返回值 | 切换上下文执行代码块 |
| 异常处理 | 向上传播到父协程 | 调用 `await()` 时抛出 | 像普通函数一样直接抛出 |
| 是否挂起 | 否（普通函数） | 否（普通函数） | 是（挂起函数） |

**文件列表加载场景的代码示例：**

```kotlin
class FileListViewModel(
 private val fileRepo: FileRepository,
 private val userRepo: UserRepository
) : ViewModel() {
 
 fun loadFileList() {
 viewModelScope.launch {
 // withContext 切换线程，返回值直接使用
 val token = withContext(Dispatchers.IO) {
 userRepo.refreshToken()
 }
 
 // async 并发请求多个数据源
 val filesDeferred = async { fileRepo.getFileList(token) }
 val storageDeferred = async { fileRepo.getStorageInfo(token) }
 
 // await 获取结果
 val files = filesDeferred.await()
 val storage = storageDeferred.await()
 
 // 回到主线程更新 UI
 _uiState.value = FileListState(files, storage)
 }
 }
}
```

选择原则：需要并发 + 需要结果 → `async`；只需切换线程 → `withContext`；发网络请求但不需要返回值（如埋点上报） → `launch`。

**【面试官追问】** 如果在一个 `launch` 内部调用 `async`，然后 `launch` 被取消了，里面的 `async` 会被取消吗？协程取消是如何传播的？

**【候选人回答】**

会被取消。这是 Kotlin 协程**结构化并发**的核心机制：

1. `launch` 取消时，会递归取消所有子协程（包括内部 `async`）。
2. 取消是协作式的，`async` 内部的挂起点（如网络请求的 IO 操作）会检查取消状态并抛出 `CancellationException`。
3. `async` 被取消后，后续调用 `await()` 也会抛出 `CancellationException`。

**传播路径**：`viewModelScope.cancel()` → `launch` 的 `Job` 被取消 → 递归取消子 `Job`（`async` 返回的 `Deferred` 也是 `Job` 的子类） → 子协程内部挂起点检测到取消 → 抛出 `CancellationException`。

关键是利用好 `viewModelScope` 和 `lifecycleScope`，在页面销毁时自动取消，避免云存储 APP 中后台网络请求继续消耗电量。

**【考察点与得分技巧】**
1. 区分 `launch` 和 `async` 是基础，能深入讲取消传播是高级。
2. 结合 `viewModelScope` 和 `lifecycleScope` 讲生命周期管理是面试官关注的。
3. 提到 `coroutineScope {}` 和 `supervisorScope {}` 对取消传播的不同影响是大大加分。

**【易踩坑警示】**
- 常见错误：在 `launch` 内部用 `try-catch` 想捕获 `async` 的异常，但 `async` 异常只在 `await()` 时才抛出，如果忘了 `await()`，异常会被静默吞掉。
- 常见错误：把 `withContext` 和 `async { }.await()` 混用，在不需要并发的场景用 `async` 多此一举。

---

### 3. Flow 的冷流和热流区别，StateFlow 和 SharedFlow 的应用场景

**【面试官提问】** Kotlin Flow 中有冷流和热流的概念，`StateFlow` 和 `SharedFlow` 都是热流。请详细说明它们的区别，以及假设你在开发NAS 私有云 的实时上传进度功能时，你会选哪个？

**【候选人回答】**

**冷流（Cold Flow）**特点：
- 每次调用 `collect` 时才会开始生产数据
- 每个收集者有独立的数据流
- `flow {}` 构建器创建的是冷流
- 典型场景：网络请求 `flow { emit(api.getData()) }`

**热流（Hot Flow）**特点：
- 数据生产与收集者无关，收集者只接收最新数据
- 多个收集者共享同一数据流
- `StateFlow` 和 `SharedFlow` 是热流

**StateFlow vs SharedFlow：**

| 特性 | StateFlow | SharedFlow |
|------|-----------|------------|
| 必须有初始值 | 是 | 否 |
| 值去重 | 自动去重（equals 比较） | 不去重 |
| 行为 | 始终保持最新值，类似 LiveData | 事件流，可配置 replay 和缓冲 |
| 新订阅者 | 立即收到当前最新值 | 根据 replay 配置收到历史值 |
| 典型场景 | UI 状态 | 一次性事件、事件总线 |

**NAS 私有云 上传进度的选择：**

```kotlin
// 使用 StateFlow 表示上传状态 —— 需要保持最新值且去重
class UploadViewModel : ViewModel() {
 private val _uploadState = MutableStateFlow(UploadState())
 val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()
 
 fun uploadFile(file: File) {
 viewModelScope.launch {
 fileRepo.upload(file).collect { progress ->
 _uploadState.update { it.copy(progress = progress) }
 // StateFlow 自动去重，相同进度不会重复通知 UI
 }
 }
 }
}

// 使用 SharedFlow 表示一次性事件 —— Toast/Snackbar
private val _toastEvent = MutableSharedFlow<String>()
val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

// SharedFlow 配置 replay=0 确保新订阅者不会收到旧事件
private val _navEvent = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
```

上传进度选 `StateFlow`，因为：
1. 进度值需要保持最新状态（旋转屏幕后重新订阅能拿到当前进度）
2. 需要去重（避免 50%→50% 重复刷新 UI）
3. 有明确的初始值（初始进度 0%）

**【面试官追问】** `SharedFlow` 的 `replay` 和 `extraBufferCapacity` 怎么理解？如果我用 `replay = 1` 的 `SharedFlow` 来替代 `StateFlow` 会有什么问题？

**【候选人回答】**

`replay`：新订阅者能收到多少个历史值。`extraBufferCapacity`：在没有订阅者时，生产者最多能缓存多少个值（加上 `replay` 组成总缓存）。

```kotlin
// replay=2, extraBufferCapacity=3 → 总缓存=5
// 新订阅者收到最近2个值，生产者最多缓存5个（超过则suspend）
val flow = MutableSharedFlow<Int>(replay = 2, extraBufferCapacity = 3)
```

用 `replay = 1` 的 `SharedFlow` 替代 `StateFlow` 的问题：
1. **不会去重**：连续两次 emit 相同值，UI 会刷新两次。
2. **没有 `.value` 属性**：无法同步读取当前值。
3. **语义不清**：`StateFlow` 表示"状态"，`SharedFlow` 表示"事件"，团队成员容易混淆用途。

**【考察点与得分技巧】**
1. 能准确区分冷/热流是基础。
2. 结合IoT 应用上传进度场景讲选择逻辑，体现工程思维。
3. 提到 `stateIn()` 和 `shareIn()` 将冷流转热流的操作符是加分项。

**【易踩坑警示】**
- 常见错误：用 `StateFlow` 发 Toast 事件，屏幕旋转后重新订阅又弹一次（粘性）。
- 常见错误：`stateIn()` 在 `ViewModel` 中使用 `WhileSubscribed()` 策略但 `stopTimeoutMillis` 设置太短，导致配置变更时 Flow 重启。

---

### 4. Kotlin 密封类的实际应用（结合网络请求 Result 封装）

**【面试官提问】** Kotlin 的密封类（`sealed class`）在实际项目中经常用来封装网络请求结果。请结合NAS 私有云 的 API 请求场景，设计一个 Result 封装，并说明密封类比普通枚举的优势。

**【候选人回答】**

```kotlin
sealed class ApiResult<out T> {
 data class Success<T>(val data: T) : ApiResult<T>()
 data class Error(val code: Int, val message: String, val exception: Throwable? = null) : ApiResult<Nothing>()
 object Loading : ApiResult<Nothing>()
}

// 网络层使用
class NasRepository(private val api: NasApiService) {
 
 suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResult<T> {
 return try {
 val response = apiCall()
 if (response.isSuccessful && response.body() != null) {
 ApiResult.Success(response.body()!!)
 } else {
 ApiResult.Error(
 code = response.code(),
 message = parseErrorMessage(response)
 )
 }
 } catch (e: IOException) {
 ApiResult.Error(code = -1, message = "网络连接失败，请检查NAS是否在线", exception = e)
 } catch (e: Exception) {
 ApiResult.Error(code = -2, message = "未知错误", exception = e)
 }
 }
}

// ViewModel 中使用——when 表达式穷举所有情况
fun loadFileList() {
 viewModelScope.launch {
 _uiState.value = FileListUiState(isLoading = true)
 when (val result = repo.getFileList()) {
 is ApiResult.Success -> {
 _uiState.value = FileListUiState(files = result.data)
 }
 is ApiResult.Error -> {
 _uiState.value = FileListUiState(
 errorMsg = result.message,
 needReconnect = result.code == -1 // NAS断连特殊处理
 )
 }
 is ApiResult.Loading -> { /* 已在上面处理 */ }
 }
 }
}
```

**密封类比枚举的优势：**

1. **每个子类可以携带不同类型的数据**：`Success` 带泛型数据，`Error` 带错误码和消息，枚举做不到。
2. **`when` 表达式穷举检查**：如果漏掉某个分支，编译报错。新增状态时编译器会提醒你更新所有 `when` 分支。
3. **类型安全**：`when` 中 `is ApiResult.Success ->` 之后，`result.data` 自动 smart cast 为 `T`，无需手动强转。

**【面试官追问】** 你说密封类的子类可以带不同类型数据。那密封类和密封接口（Kotlin 1.5+）有什么区别？在IoT 应用的多模块项目中怎么选择？

**【候选人回答】**

| 特性 | `sealed class` | `sealed interface` |
|------|---------------|-------------------|
| 构造函数 | 可以有构造函数 | 不能有构造函数 |
| 属性 | 可以定义属性 | 只能定义抽象属性 |
| 多继承 | 子类只能继承这一个密封类 | 子类可以实现多个接口 |
| 可见性 | 子类可以是 `internal`/`private` | 子类同样灵活 |
| 适用版本 | Kotlin 1.0+ | Kotlin 1.5+ |

在IoT 应用多模块项目中：
```kotlin
// core模块：定义密封接口（更灵活，各模块可扩展）
sealed interface UiEvent {
 data class ShowToast(val message: String) : UiEvent
 data class Navigate(val route: String) : UiEvent
 data class ShowDialog(val title: String, val content: String) : UiEvent
}

// 密封接口的优势：子类可以实现其他接口
data class ShareFileEvent(val fileId: String) : UiEvent, Parcelable
```

选型原则：如果子类还需要实现其他接口/继承其他类 → `sealed interface`；否则 `sealed class` 足以胜任。

**【考察点与得分技巧】**
1. 不只是会用密封类，要结合网络请求的错误处理场景展示工程能力。
2. 能区分 `sealed class` 和 `sealed interface` 说明跟进 Kotlin 版本演进。
3. 在错误状态中体现 IoT 特殊逻辑（NAS 断连重连）是加分项。

**【易踩坑警示】**
- 常见错误：用 `enum` 替代 `sealed class` 做 Result，然后通过额外的 nullable 字段传递数据，代码臃肿且类型不安全。
- 常见错误：`when` 中写了 `else` 分支，新增子类时编译器不再报错，容易遗漏。

---

### 5. Kotlin 扩展函数的原理和注意事项

**【面试官提问】** Kotlin 的扩展函数用起来很方便，但你知道它是怎么实现的吗？在云存储 APP 中给 `File` 类扩展了一个 `formatFileSize()` 方法，这个方法真的被"添加"到了 `File` 类中吗？

**【候选人回答】**

扩展函数**并非真正修改原始类**，它只是语法糖。

**原理（反编译成 Java 看）**：

```kotlin
// Kotlin 扩展函数
fun File.formatFileSize(): String {
 val size = this.length()
 return when {
 size < 1024 -> "${size}B"
 size < 1024 * 1024 -> "${size / 1024}KB"
 else -> "${size / (1024 * 1024)}MB"
 }
}

// 反编译后的 Java 代码
public final class FileExtKt {
 public static final String formatFileSize(@NotNull File $this$formatFileSize) {
 long size = $this$formatFileSize.length();
 // ... 格式化逻辑
 }
}
```

可以看出，扩展函数编译后就是一个**静态工具方法**，接收者对象作为第一个参数传入。这就是为什么：
1. 扩展函数**无法访问私有成员**（它只是一个外部静态方法）。
2. 扩展函数**不是多态的**（静态解析，编译时确定）。
3. 扩展函数**可以被同名成员函数遮蔽**（成员函数优先级更高）。

**云存储 APP 中的应用：**

```kotlin
// 实用扩展：View 的防抖点击
fun View.setOnSafeClickListener(interval: Long = 600L, onClick: (View) -> Unit) {
 var lastClickTime = 0L
 setOnClickListener { view ->
 val now = System.currentTimeMillis()
 if (now - lastClickTime >= interval) {
 lastClickTime = now
 onClick(view)
 }
 }
}

// 实用扩展：Activity/Fragment 的 ViewModel 获取
inline fun <reified T : ViewModel> Fragment.getViewModel(
 factory: ViewModelProvider.Factory? = null
): T {
 return if (factory != null) {
 ViewModelProvider(this, factory)[T::class.java]
 } else {
 ViewModelProvider(this)[T::class.java]
 }
}
```

**【面试官追问】** 你说成员函数优先级高于扩展函数，那如果确实想用扩展函数版本怎么办？还有，扩展属性是怎么实现的？

**【候选人回答】**

成员函数优先级不可覆盖，这是语言设计决定的。最佳实践是**避免定义与成员函数同名的扩展函数**。如果已有同名，只能通过不同命名来区分。

扩展属性**不能有 backing field**，本质上是一个 getter/setter 扩展：

```kotlin
// 扩展属性：本质是静态 getter/setter 方法
val View.isVisible: Boolean
 get() = visibility == View.VISIBLE

var View.isGone: Boolean
 get() = visibility == View.GONE
 set(value) { visibility = if (value) View.GONE else View.VISIBLE }

// 反编译后
public static final boolean isVisible(@NotNull View $this$isVisible) {
 return $this$isVisible.getVisibility() == View.VISIBLE;
}

public static final void setGone(@NotNull View $this$isGone, boolean value) {
 $this$isGone.setVisibility(value ? View.GONE : View.VISIBLE);
}
```

扩展属性只是语法糖，不能真正添加字段到类中。

**【考察点与得分技巧】**
1. 能说出"静态方法 + 接收者参数"的实现原理是关键。
2. 知道扩展函数不是多态的，区别于虚函数。
3. 结合实际业务场景展示实用扩展函数，体现工程价值。

**【易踩坑警示】**
- 常见错误：大量定义全局可见的扩展函数，导致自动补全列表爆炸，IDE 性能下降。应该限定可见性（`private`/`internal`）。
- 常见错误：在 Java 代码中调用 Kotlin 扩展函数，必须知道类名是 `FileNameKt`，方法签名多一个参数，容易写错。

---

### 6. 协程的异常处理机制（CoroutineExceptionHandler vs try-catch）

**【面试官提问】** 协程中的异常处理有两种主要方式：`try-catch` 包裹和 `CoroutineExceptionHandler`。请说明它们的工作原理和适用场景。在NAS 私有云 文件上传失败的场景下，怎么设计异常处理？

**【候选人回答】**

**两种方式的工作原理：**

**1. `try-catch`（适用于 `launch` 和 `async`）：**
- 直接包裹挂起函数调用
- 只能捕获**当前协程内**同步抛出的异常
- 对于 `launch`，`try-catch` 放在内部才有效

**2. `CoroutineExceptionHandler`（仅适用于 `launch`）：**
- 作为协程上下文元素注册
- **只能处理未被捕获的异常**（作为最后的兜底）
- 对 `async` 无效（`async` 的异常需要通过 `await()` 处理）

**关键机制对比：**

```kotlin
// launch 的异常传播路线：
// 子协程 → 父协程 → CoroutineExceptionHandler（如果注册了）
// 最终如果没有 handler，调用线程的 uncaughtExceptionHandler

// async 的异常传播路线：
// 异常被封装在 Deferred 中，调用 await() 时才抛出

// supervisorScope 阻断异常的向上传播
```

**文件上传的异常处理设计：**

```kotlin
class UploadViewModel(
 private val uploadRepo: UploadRepository
) : ViewModel() {
 
 // 全局异常兜底
 private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
 when (throwable) {
 is IOException -> {
 _uploadState.update { it.copy(error = "NAS设备连接中断，请检查网络") }
 // 进入断线重连逻辑
 scheduleReconnect()
 }
 is CancellationException -> {
 // 取消是正常行为，不处理
 }
 else -> {
 _uploadState.update { it.copy(error = "上传失败：${throwable.message}") }
 // 上报崩溃日志
 CrashReporter.report(throwable)
 }
 }
 }
 
 fun uploadFiles(files: List<File>) {
 viewModelScope.launch(exceptionHandler) {
 files.forEach { file ->
 // 单个文件失败不影响其他文件（supervisorScope）
 supervisorScope {
 launch {
 try {
 uploadRepo.upload(file).collect { progress ->
 _uploadState.update { 
 it.copy(currentProgress = progress) 
 }
 }
 } catch (e: IOException) {
 // 单个文件失败记录到失败列表
 _failedFiles.add(file to e.message)
 }
 }
 }
 }
 }
 }
}
```

**【面试官追问】** 为什么 `CoroutineExceptionHandler` 对 `async` 无效？如果我在 `async` 中抛异常，handler 捕获不到怎么办？

**【候选人回答】**

`async` 设计的初衷就是**让调用者决定如何处理异常**——通过 `await()` 来获取结果或处理异常。如果 `CoroutineExceptionHandler` 能捕获 `async` 的异常，调用者调用 `await()` 就不会再抛异常了，这违背了设计意图。

正确做法：

```kotlin
// 方案1：调用 await() 时 try-catch
viewModelScope.launch {
 val deferred = async { riskyOperation() }
 try {
 val result = deferred.await()
 } catch (e: Exception) {
 // 处理异常
 }
}

// 方案2：使用 supervisorScope + async
viewModelScope.launch {
 supervisorScope {
 val deferred = async { riskyOperation() }
 // 即使 await() 抛异常，supervisorScope 也不会取消
 // 但当前 launch 仍需要捕获异常
 }
}
```

核心记忆：**`launch` → `CoroutineExceptionHandler`，`async` → `await()` + try-catch**。

**【考察点与得分技巧】**
1. 区分两种异常处理方式的适用场景是基础。
2. 知道 `supervisorScope` 阻断传播是进阶。
3. 在智能硬件的 IoT 场景中体现异常分级处理（IO 异常 → 重连，普通异常 → 提示，取消异常 → 忽略）。

**【易踩坑警示】**
- 常见错误：在 `launch` 外部包 `try-catch`，以为能捕获内部异常。实际上 `launch` 内部的异常会被传播，外部 `try-catch` 捕获不到。
- 常见错误：在 `viewModelScope.launch` 中注册了 `CoroutineExceptionHandler`，但异常发生在 `async` 中，导致 handler 无效，异常静默丢失。

---

### 7. Kotlin 高阶函数和内联函数（inline/noinline/crossinline）

**【面试官提问】** Kotlin 中的高阶函数配合 `inline` 很常见。请解释 `inline`、`noinline` 和 `crossinline` 的区别，以及在云存储 APP 中封装数据库事务时，为什么推荐使用 `inline`？

**【候选人回答】**

**inline（内联）**：将函数体直接复制到调用处，避免创建匿名类和函数调用的开销。

```kotlin
// 内联函数
inline fun <T> executeTransaction(db: RoomDatabase, crossinline block: () -> T): T {
 db.beginTransaction()
 try {
 val result = block()
 db.setTransactionSuccessful()
 return result
 } finally {
 db.endTransaction()
 }
}

// 调用处编译后等价于：
db.beginTransaction()
try {
 val result = block() // 直接展开
 db.setTransactionSuccessful()
} finally {
 db.endTransaction()
}
```

**noinline**：标记某个函数参数不内联。当需要将该 lambda 作为对象传递（如存储为属性、传给非内联函数）时使用。

```kotlin
inline fun setupListener(
 crossinline onClick: () -> Unit,
 noinline onLongClick: () -> Unit // 需要作为对象存储
) {
 view.setOnClickListener { onClick() }
 view.onLongClickListener = View.OnLongClickListener { onLongClick(); true } // 必须作为对象
}
```

**crossinline**：标记 lambda 不允许非局部返回（non-local return）。被 `crossinline` 修饰的 lambda 中不能使用 `return`（只能 `return@lambda`）。

```kotlin
inline fun repeatAction(times: Int, crossinline action: (Int) -> Unit) {
 for (i in 0 until times) {
 action(i)
 }
}

// 调用时
repeatAction(3) { index ->
 println(index)
 // return ← 编译错误！crossinline 禁止非局部返回
}
```

**为什么数据库事务用 `inline`？**

```kotlin
// 不用 inline：每次调用都创建 Function0 匿名对象
fun dbTransaction(block: () -> Unit) { ... }

// 用 inline：零开销抽象，代码直接展开
inline fun dbTransaction(block: () -> Unit) { ... }

// 结合 crossinline 防止 return 跳出事务导致没有 endTransaction()
inline fun dbTransaction(crossinline block: () -> Unit) {
 database.beginTransaction()
 try {
 block()
 database.setTransactionSuccessful()
 } finally {
 database.endTransaction() // 必须执行
 }
}
```

**【面试官追问】** 既然 `inline` 这么好，为什么不全用 `inline`？什么情况不适合内联？

**【候选人回答】**

不适合 `inline` 的情况：

1. **函数体过大**：内联会导致字节码膨胀，增大 APK 体积。通常只对短小的高阶函数内联。
2. **递归函数**：递归函数无法完全展开。
3. **非高阶函数**：普通函数内联没有收益（没有 lambda 参数，不需要消除匿名类开销），反而增加体积。
4. **通过反射调用**：内联的函数无法通过反射访问。

JVM 本身也有 JIT 内联优化，对于普通函数，JIT 会在运行时自动内联热点代码。Kotlin 的 `inline` 主要是为了**消除 lambda 的匿名类开销**。

```kotlin
// 不应该 inline：函数体太大
inline fun processLargeData(data: List<Int>) {
 // 100行业务逻辑...
}

// 不应 inline：无 lambda 参数，没有收益
inline fun add(a: Int, b: Int) = a + b
```

**【考察点与得分技巧】**
1. 能区分三个关键字是基础，能讲出 `crossinline` 防止非局部返回的作用是进阶。
2. 结合数据库事务场景，说明 `crossinline` 保证 `endTransaction()` 一定执行，展示安全意识。
3. 提到 APK 体积影响，说明有工程考量。

**【易踩坑警示】**
- 常见错误：内联函数中直接调用非内联函数的 lambda 参数，编译报错。需要加 `noinline` 或 `crossinline`。
- 常见错误：在内联函数中使用 `return` 直接跳出外层函数，如果没有 `crossinline` 保护，可能产生难以预期的行为。

---

### 8. Kotlin 作用域函数（let/run/apply/also/with）的选择原则

**【面试官提问】** Kotlin 提供了 5 个作用域函数：`let`、`run`、`apply`、`also`、`with`。它们很容易混淆。请用一句话概括它们各自最适合的场景，并在一个NAS 私有云 的初始化和配置场景中展示如何选择。

**【候选人回答】**

**一句话选择原则：**

| 函数 | 引用对象方式 | 返回值 | 最适合场景 |
|------|------------|--------|-----------|
| `let` | `it`（可重命名） | Lambda 结果 | 可空对象的安全操作 + 结果转换 |
| `run` | `this` | Lambda 结果 | 对象配置 + 计算结果 |
| `apply` | `this` | 对象本身 | 对象初始化配置 |
| `also` | `it` | 对象本身 | 副作用操作（日志、校验） |
| `with` | `this` | Lambda 结果 | 对非可空对象的一批操作 |

**记忆口诀**：`apply` 和 `also` 返回自身（**A** 开头 = 返回 **A** 自己），`let` 和 `run` 返回结果。`this` 系列隐式调用，`it` 系列显式调用。

**NAS 私有云 初始化和配置场景：**

```kotlin
class NasDeviceManager {
 
 fun initializeNas(config: NasConfig?) {
 // let：安全处理可空配置 + 转换结果
 val validConfig = config?.let { cfg ->
 if (cfg.ip.isBlank()) throw IllegalArgumentException("IP不能为空")
 cfg.copy(port = cfg.port.takeIf { it > 0 } ?: 5000) // 默认端口
 } ?: NasConfig.default()
 
 // apply：初始化设备连接对象
 val nasClient = NasClient().apply {
 host = validConfig.ip
 port = validConfig.port
 timeout = 30_000
 retryCount = 3
 }
 
 // run：对已有对象执行一批操作并返回结果
 val connectionResult = nasClient.run {
 authenticate(validConfig.username, validConfig.password)
 connect()
 }
 
 // also：记录日志和埋点（不影响主流程）
 connectionResult.also { result ->
 Log.d("NasInit", "连接结果: ${result.state}")
 Analytics.track("nas_connect", mapOf("success" to result.isSuccess))
 }
 
 // with：对非空对象集中操作
 with(connectionResult) {
 if (isSuccess) {
 startHeartbeat()
 syncFileIndex()
 } else {
 scheduleReconnect()
 }
 }
 }
}
```

**【面试官追问】** `let` 经常用于可空链式调用，但如果嵌套多层 `let` 会怎样？有更好替代方案吗？

**【候选人回答】**

嵌套多层 `let` 会产生"回调地狱"般的代码：

```kotlin
// 不推荐：嵌套 let
user?.let { u ->
 u.profile?.let { p ->
 p.avatar?.let { a ->
 loadImage(a.url)
 }
 }
}

// 推荐：使用 if 或 early return
val avatarUrl = user?.profile?.avatar?.url
if (avatarUrl != null) {
 loadImage(avatarUrl)
}

// 或者在函数中使用 early return
fun loadUserAvatar(user: User?) {
 val url = user?.profile?.avatar?.url ?: return
 loadImage(url)
}
```

另外 `let` 中可以用命名参数替代 `it` 提高可读性：

```kotlin
nasDevice?.let { device ->
 connectToNas(device.ip, device.port)
}
```

**【考察点与得分技巧】**
1. 不只背表格，能结合实际初始化场景选择合适的作用域函数。
2. 知道过深层级的问题和替代方案，说明代码审美。
3. `also` 做埋点/日志是很多面试官期待的实用场景。

**【易踩坑警示】**
- 常见错误：`apply` 中最后一行是表达式时以为会返回那个表达式的值，但 `apply` 永远返回对象本身。
- 常见错误：在链式调用中，`let` 的返回值和 `also` 的返回值混用，造成业务数据丢失。

---

### 9. Kotlin 泛型型变（in/out）的理解

**【面试官提问】** Kotlin 中泛型的 `in` 和 `out` 修饰符是什么意思？你在IoT 应用的 NAS 文件列表场景中会如何设计一个支持多类型文件（图片/视频/文档）的适配器？

**【候选人回答】**

`in` 和 `out` 是声明处型变（Declaration-site variance），控制泛型参数的子类型关系。

**out（协变）**：`Producer<out T>` —— 只生产 T，不消费 T。
- 如果 `Dog` 是 `Animal` 的子类，那么 `Producer<Dog>` 也是 `Producer<Animal>` 的子类。
- 只能用于输出位置（返回值类型），不能用于输入位置（参数类型）。
- Java 等价：`? extends T`。

**in（逆变）**：`Consumer<in T>` —— 只消费 T，不生产 T。
- 如果 `Dog` 是 `Animal` 的子类，那么 `Consumer<Animal>` 是 `Consumer<Dog>` 的子类（注意方向相反）。
- 只能用于输入位置，不能用于输出位置。
- Java 等价：`? super T`。

**在文件列表中的应用：**

```kotlin
// 文件资源的抽象层次
interface NasFileItem {
 val id: String
 val name: String
 val size: Long
}

class ImageFile : NasFileItem { /* ... */ }
class VideoFile : NasFileItem { /* ... */ }
class DocumentFile : NasFileItem { /* ... */ }

// 使用 out 协变：适配器只读取文件信息用于展示，不写入
class FileListAdapter<out T : NasFileItem>(
 private val items: List<T>,
 private val onClick: (T) -> Unit
) {
 fun getItem(position: Int): T = items[position]
 
 fun bindView(holder: ViewHolder, position: Int) {
 val item = items[position]
 holder.title.text = item.name
 holder.subtitle.text = formatFileSize(item.size)
 holder.itemView.setOnClickListener { onClick(item) }
 }
 // 注意：不能有 fun addItem(item: T)，因为 out 禁止 T 作为输入参数
}

// 使用场景：不同类型文件可以用同一个适配器展示基本信息
val imageAdapter = FileListAdapter(imageFiles) { image -> openImageViewer(image) }
val videoAdapter = FileListAdapter(videoFiles) { video -> playVideo(video) }
```

**UnsafeVariance 注解的使用场景：**

```kotlin
// 有时确实需要在 out 位置消费 T，用 @UnsafeVariance 抑制
class FileListAdapter<out T : NasFileItem> {
 fun compare(a: @UnsafeVariance T, b: @UnsafeVariance T): Int {
 return a.name.compareTo(b.name)
 }
 // 注意：这绕过了类型安全检查，需要自己保证安全
}
```

**【面试官追问】** 为什么 Kotlin 能在声明处做型变，而 Java 只能在使用处（`? extends`/`? super`）做？Kotlin 的型变有什么限制？

**【候选人回答】**

因为 **Kotlin 在泛型声明时就明确标记了 in/out**，编译器可以在声明处检查是否违反了型变规则：

- 标记 `out` 后，编译器禁止 T 出现在 `in` 位置（如方法参数）。
- 标记 `in` 后，编译器禁止 T 出现在 `out` 位置（如返回值）。

而 Java 的泛型是不型变的（invariant），只能在每个使用点用 `? extends`/`? super` 声明。Java 的问题是代码臃肿：

```java
// Java 每次使用都要写通配符
public void copyAll(List<? extends NasFileItem> src, List<? super NasFileItem> dest) { ... }
```

Kotlin 的限制：
- 型变只能用于类/接口的泛型参数，不能用于函数局部的泛型（函数泛型只能在调用处型变）。
- 使用 `in` 或 `out` 后，违反了方向规则会导致编译错误。

**【考察点与得分技巧】**
1. 理解 PECS 原则（Producer Extends, Consumer Super）是基础。
2. 结合文件列表适配器演示 `out` 的实际价值。
3. 能比较 Kotlin 和 Java 型变机制的差异，展示语言层面的深度理解。

**【易踩坑警示】**
- 常见错误：在 `out` 类型上定义 `fun add(item: T)`，编译器报错后不知原因。
- 常见错误：把 `in` 和 `out` 的方向记反，尤其是在逆变场景容易搞混。

---

### 10. Kotlin 委托属性的原理（by 关键字）

**【面试官提问】** Kotlin 的 `by` 关键字可以实现属性委托，例如 `by lazy`、`by viewModels()`。请说明委托属性的编译原理。在云存储 APP 中你会如何自定义一个委托来实现 `SharedPreferences` 的便捷读写？

**【候选人回答】**

**编译原理：**

```kotlin
class MyClass {
 var name: String by Delegate()
}

// 编译后等价于：
class MyClass {
 private val name$delegate = Delegate()
 
 var name: String
 get() = name$delegate.getValue(this, this::name)
 set(value) = name$delegate.setValue(this, this::name, value)
}
```

委托对象必须提供 `getValue()` 和 `setValue()`（var 时需要）操作符函数。`ReadOnlyProperty` 和 `ReadWriteProperty` 接口封装了这些要求。

**应用到 SharedPreferences 的便捷读写：**

```kotlin
// 自定义委托
class PreferenceDelegate<T>(
 private val prefs: SharedPreferences,
 private val key: String,
 private val defaultValue: T
) : ReadWriteProperty<Any?, T> {
 
 @Suppress("UNCHECKED_CAST")
 override fun getValue(thisRef: Any?, property: KProperty<*>): T {
 return when (defaultValue) {
 is String -> prefs.getString(key, defaultValue) as T
 is Int -> prefs.getInt(key, defaultValue) as T
 is Long -> prefs.getLong(key, defaultValue) as T
 is Boolean -> prefs.getBoolean(key, defaultValue) as T
 is Float -> prefs.getFloat(key, defaultValue) as T
 else -> throw IllegalArgumentException("不支持的类型")
 }
 }
 
 override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
 with(prefs.edit()) {
 when (value) {
 is String -> putString(key, value)
 is Int -> putInt(key, value)
 is Long -> putLong(key, value)
 is Boolean -> putBoolean(key, value)
 is Float -> putFloat(key, value)
 else -> throw IllegalArgumentException("不支持的类型")
 }
 apply()
 }
 }
}

// 扩展函数，方便使用
inline fun <reified T> SharedPreferences.delegate(
 key: String, 
 defaultValue: T
): PreferenceDelegate<T> = PreferenceDelegate(this, key, defaultValue)

// 云存储 APP 中的使用
class NasSettings(private val prefs: SharedPreferences) {
 var autoBackup: Boolean by prefs.delegate("auto_backup", false)
 var nasIp: String by prefs.delegate("nas_ip", "")
 var lastSyncTime: Long by prefs.delegate("last_sync_time", 0L)
 var uploadQuality: Int by prefs.delegate("upload_quality", 80)
}

// 使用起来像普通属性
val settings = NasSettings(prefs)
settings.autoBackup = true // 自动写入 SharedPreferences
if (settings.autoBackup) { // 自动从 SharedPreferences 读取
 startBackup()
}
```

**【面试官追问】** 属性委托的延迟加载（`lazy`）和可观察属性（`observable`）也是基于委托实现的。你知道 `Delegates.observable` 的原理吗？在IoT 应用的设备状态变化场景中如何使用？

**【候选人回答】**

`Delegates.observable` 的原理：

```kotlin
// 简化版实现
class ObservableProperty<T>(
 initialValue: T,
 private val onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit
) : ReadWriteProperty<Any?, T> {
 private var value: T = initialValue
 
 override fun getValue(thisRef: Any?, property: KProperty<*>): T = value
 
 override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
 val oldValue = this.value
 this.value = value
 onChange(property, oldValue, value)
 }
}
```

**IoT 应用设备状态变化场景：**

```kotlin
class NasDeviceViewModel : ViewModel() {
 // 监听设备状态变化，自动触发相应逻辑
 var deviceStatus: NasDeviceStatus by Delegates.observable(NasDeviceStatus.IDLE) { _, old, new ->
 when {
 old == NasDeviceStatus.CONNECTED && new == NasDeviceStatus.DISCONNECTED -> {
 // 断连：暂停上传任务，启动重连
 pauseAllUploads()
 scheduleReconnect()
 }
 old == NasDeviceStatus.DISCONNECTED && new == NasDeviceStatus.CONNECTED -> {
 // 重连成功：恢复上传任务
 resumeAllUploads()
 syncFileIndex()
 }
 new == NasDeviceStatus.LOW_STORAGE -> {
 // 存储不足：弹窗提示用户
 _events.emit(UiEvent.ShowStorageWarning)
 }
 }
 }
}
```

**【考察点与得分技巧】**
1. 说出编译后的 `getValue`/`setValue` 调用是核心。
2. 自定义 SharedPreferences 委托展示"理解原理并能扩展"的能力。
3. 结合IoT 应用设备状态变化展示 `observable` 的实用场景。

**【易踩坑警示】**
- 常见错误：在委托的 `getValue` 中做耗时操作，导致属性读取阻塞 UI 线程。
- 常见错误：在自定义委托中忘记考虑线程安全，多线程读写时出现数据不一致。

---

### 11. 结构化并发（structured concurrency）的理解

**【面试官提问】** Kotlin 协程强调"结构化并发"。请解释什么是结构化并发，它解决了传统线程编程的什么问题？在云存储 APP 的文件下载功能中，如何利用结构化并发管理并发下载任务？

**【候选人回答】**

**结构化并发**是一种编程范式：协程的生命周期被限定在特定的作用域内，所有子协程必须在父协程完成之前完成，形成一个树状结构。

```kotlin
// 结构化并发：父协程等待所有子协程完成
suspend fun loadNasDashboard() = coroutineScope {
 val storage = async { fetchStorageInfo() }
 val files = async { fetchRecentFiles() }
 val users = async { fetchUsers() }
 
 DashboardData(storage.await(), files.await(), users.await())
 // 三个子协程完成后，coroutineScope 才返回
 // 一旦有异常，所有子协程被取消
}
```

**解决了传统线程的什么问题：**

1. **资源泄漏**：线程池中忘记 shutdown 导致线程泄漏；结构化并发的协程作用域取消时自动清理。
2. **生命周期管理混乱**：线程的 `start()`/`join()`/`interrupt()` 管理复杂；结构化并发中，取消父协程自动取消所有子协程。
3. **异常传播不可控**：线程中未捕获异常导致进程崩溃；结构化并发有明确的异常传播和取消机制。

**文件下载的结构化并发设计：**

```kotlin
class DownloadManager(
 private val nasApi: NasApiService,
 private val fileDao: FileDao
) {
 // 全局下载作用域（应用级别）
 private val downloadScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
 
 suspend fun downloadFiles(fileIds: List<String>) = coroutineScope {
 // coroutineScope 等待所有下载完成
 fileIds.map { fileId ->
 async {
 try {
 // 单个文件下载（带重试）
 downloadSingleFile(fileId)
 } catch (e: Exception) {
 // 失败记录但不影响其他文件
 fileDao.markFailed(fileId, e.message)
 null
 }
 }
 }.awaitAll()
 .filterNotNull() // 只返回成功的
 }
 
 private suspend fun downloadSingleFile(fileId: String) = coroutineScope {
 // 一个文件的下载包含获取信息 + 下载数据
 val fileInfo = async { nasApi.getFileInfo(fileId) }
 val downloadToken = async { nasApi.getDownloadToken(fileId) }
 
 val info = fileInfo.await()
 val token = downloadToken.await()
 
 // 分片下载
 nasApi.downloadChunks(info.totalSize, token)
 }
 
 fun cancelAll() {
 downloadScope.cancel() // 取消所有下载
 }
}
```

**关键结构对比：**

| 作用域 | 异常行为 | 取消传播 | 适用场景 |
|--------|---------|---------|---------|
| `coroutineScope` | 子协程异常取消兄弟 | 向下传播 | 必须全部成功的任务组 |
| `supervisorScope` | 子协程异常不影响兄弟 | 向下传播 | 独立任务组（如下载多个文件） |
| `viewModelScope` | 关联 ViewModel 生命周期 | 页面销毁时取消 | UI 相关协程 |

**【面试官追问】** 你说 `coroutineScope` 和 `supervisorScope` 的异常行为不同。在IoT 应用批量下载 10 个文件时，如果第 3 个失败了，你会选择哪个？为什么？

**【候选人回答】**

选择 `supervisorScope`。

```kotlin
suspend fun batchDownload(fileIds: List<String>) = supervisorScope {
 // 第3个文件失败了，第4-10个继续下载
 fileIds.map { fileId ->
 async {
 try {
 downloadFile(fileId)
 DownloadResult.Success(fileId)
 } catch (e: Exception) {
 DownloadResult.Failed(fileId, e.message)
 }
 }
 }.awaitAll()
}
```

如果用 `coroutineScope`，第 3 个失败会取消第 1-10 的所有协程，之前下载到一半的文件也会被取消，用户体验极差。

**判断原则**：任务之间相互独立 → `supervisorScope`；任务之间互相依赖（一个失败全盘皆输） → `coroutineScope`。

**【考察点与得分技巧】**
1. 能从"解决了什么问题"的角度解释结构化并发，而非只背概念。
2. `coroutineScope` vs `supervisorScope` 的选择结合实际下载场景，体现工程判断力。
3. 提到 `viewModelScope`/`lifecycleScope` 的自动取消，对应对生命周期管理的重视。

**【易踩坑警示】**
- 常见错误：使用 `GlobalScope.launch` 打破结构化并发，协程逃逸，无法管理生命周期。这是 Kotlin 官方最不推荐的做法。
- 常见错误：在 `suspend` 函数中忘记用 `coroutineScope` 包裹，直接 `launch` 会报错（`launch` 是 `CoroutineScope` 的扩展函数）。

---

### 12. Kotlin 中的 `object` 关键字的三种用法

**【面试官提问】** Kotlin 的 `object` 关键字有三种用法。请分别说明并给出在IoT 应用的代码中的实际例子。

**【候选人回答】**

**1. 对象声明（Object Declaration）—— 单例模式**

```kotlin
// IoT 应用的全局日志管理器（单例）
object NasLogger {
 private const val TAG = "NAS_LOG"
 private var isDebug = BuildConfig.DEBUG
 
 fun d(message: String) {
 if (isDebug) Log.d(TAG, message)
 }
 
 fun e(message: String, throwable: Throwable? = null) {
 Log.e(TAG, message, throwable)
 }
 
 fun setDebug(debug: Boolean) {
 isDebug = debug
 }
}

// 使用
NasLogger.d("NAS设备连接成功")
```

**2. 伴生对象（Companion Object）—— 类级别的成员**

```kotlin
// IoT 应用NAS设备的工厂方法
class NasDevice private constructor(
 val ip: String,
 val port: Int,
 val deviceId: String
) {
 companion object {
 private const val DEFAULT_PORT = 5000
 const val TAG = "NasDevice"
 
 fun fromDiscovery(info: DiscoveryInfo): NasDevice {
 return NasDevice(
 ip = info.ip,
 port = info.port ?: DEFAULT_PORT,
 deviceId = info.deviceId
 )
 }
 
 @JvmStatic
 fun createLocal(ip: String): NasDevice {
 return NasDevice(ip, DEFAULT_PORT, "local_$ip")
 }
 }
}

// 使用
val device = NasDevice.fromDiscovery(discoveryInfo)
val tag = NasDevice.TAG // 伴生对象常量
```

**3. 对象表达式（Object Expression）—— 匿名内部类**

```kotlin
// IoT 应用蓝牙设备回调（匿名对象）
fun connectBleDevice(address: String) {
 val callback = object : BluetoothGattCallback() {
 override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
 when (newState) {
 BluetoothProfile.STATE_CONNECTED -> {
 NasLogger.d("蓝牙设备已连接: $address")
 gatt?.discoverServices()
 }
 BluetoothProfile.STATE_DISCONNECTED -> {
 NasLogger.d("蓝牙设备已断开: $address")
 scheduleReconnect(address)
 }
 }
 }
 
 override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
 // 发现服务后的处理
 }
 }
 bluetoothGatt = device.connectGatt(context, false, callback)
}
```

**【面试官追问】** 伴生对象和 Java 的 `static` 完全等价吗？在 JVM 上混合开发时有什么需要注意的？

**【候选人回答】**

不完全等价，区别如下：

1. **伴生对象本身是一个真实对象**，可以实现接口：

```kotlin
interface Factory<T> {
 fun create(): T
}

class NasDevice private constructor() {
 companion object : Factory<NasDevice> {
 override fun create(): NasDevice = NasDevice()
 }
}
// Java static 做不到
```

2. **访问方式不同**：
```kotlin
// Kotlin 中
NasDevice.create() // 直接访问

// Java 中访问 Kotlin 伴生对象（默认情况下）
NasDevice.Companion.create(); // 需要通过 Companion

// 加上 @JvmStatic 后
NasDevice.create(); // 像 static 一样使用
```

3. **伴生对象可以命名**：
```kotlin
class NasDevice {
 companion object Factory { // 有名字的伴生对象
 fun create(): NasDevice = NasDevice()
 }
}
// 使用
NasDevice.Factory.create()
```

**混合开发注意事项**：
- 供 Java 调用的方法加 `@JvmStatic` 和 `@JvmField` 注解。
- 伴生对象中的 `const val` 编译为真正的 static final，普通 `val` 需要通过 `Companion` 访问。
- 匿名 `object` 表达式在 Java 中等价于匿名内部类，但 Kotlin 中可以访问非 final 的闭包变量。

**【考察点与得分技巧】**
1. 三种用法各给出实际场景，展示语言掌握度。
2. 伴生对象与 Java static 的区别，展示跨语言开发经验。
3. `@JvmStatic`/`@JvmField` 注解的提及，体现混合开发经验。

**【易踩坑警示】**
- 常见错误：在 Java 代码中直接调用 Kotlin 伴生对象方法，IDE 提示找不到（没加 `@JvmStatic`）。
- 常见错误：在单例 `object` 的 `init` 块中使用 `Context`，可能导致内存泄漏或测试困难。

---

### 13. Kotlin vs Java 在空安全上的优势

**【面试官提问】** Kotlin 最大的卖点之一是空安全。请系统地说明 Kotlin 是如何通过类型系统消除 NPE 的？在IoT 应用的 NAS API 返回数据解析中，Kotlin 的空安全机制带来了哪些好处？

**【候选人回答】**

Kotlin 的空安全通过**类型系统**在编译期消除绝大多数 NPE：

**核心机制：**

**1. 可空类型与不可空类型的显式区分：**
```kotlin
var name: String = "Example" // 不可空
var nickname: String? = null // 可空

name.length // 安全，编译器保证不为 null
nickname.length // 编译错误！必须先处理可空性
```

**2. 安全调用操作符 `?.` 与 Elvis 操作符 `?:`：**
```kotlin
// 链式安全调用
val fileSize = nasApi.getFileInfo(id)?.size ?: 0L

// 等价于 Java 的嵌套判空
Long fileSize = 0L;
NasFileInfo info = nasApi.getFileInfo(id);
if (info != null) {
 fileSize = info.getSize();
}
```

**3. 安全类型转换 `as?`：**
```kotlin
val imageFile = fileItem as? ImageFile // 转换失败返回 null
imageFile?.resolution // 安全访问
```

**4. `!!` 非空断言（明确标记风险操作）：**
```kotlin
// 在确认非空的场景
val token = prefs.getString("token", null)!! // token 必须存在，否则宁愿 crash
```

**在NAS 私有云 API 响应解析中的应用：**

```kotlin
// JSON 响应解析——Java 方式（地狱判空）
data class NasApiResponse(
 val code: Int,
 val message: String?,
 val data: FileListData?
)

data class FileListData(
 val files: List<NasFile>?,
 val totalCount: Int?
)

// Kotlin 处理
fun parseFileList(response: NasApiResponse): List<NasFile> {
 return when {
 response.code != 200 -> {
 NasLogger.e("API错误: ${response.message}")
 emptyList()
 }
 else -> response.data?.files?.filterNotNull() ?: emptyList()
 }
}

// 如果用 Java：
// if (response.getCode() != 200) { ... }
// FileListData data = response.getData();
// if (data == null) return Collections.emptyList();
// List<NasFile> files = data.getFiles();
// if (files == null) return Collections.emptyList();
// return files.stream().filter(Objects::nonNull).collect(Collectors.toList());
```

**5. 平台类型（Platform Types）—— 与 Java 互操作的风险点：**
```kotlin
// Java 方法返回的 String 在 Kotlin 中是 String!（平台类型）
val name = javaMethod() // 类型为 String!
name.length // 编译器不报错，但运行时可能 NPE

// 最佳实践：显式声明类型
val name: String? = javaMethod() // 明确可空
```

**【面试官追问】** `let`、`run` 和作用域函数在空安全方面有什么配合使用技巧？

**【候选人回答】**

```kotlin
// 1. let 链式处理可空对象
nasDevice?.let { device ->
 connect(device.ip, device.port)
}?.let { session ->
 authenticate(session)
}?.let { token ->
 loadData(token)
} ?: run {
 // 任何一步为 null 都执行这里
 showError("NAS连接失败")
}

// 2. takeIf/takeUnless 条件过滤
val validFile = file.takeIf { it.size > 0 && it.isReadable }
// validFile 为 File? 类型

// 3. 空安全 + when 的组合
val result: String = when {
 wifi?.ssid != null -> "已连接: ${wifi?.ssid}"
 ethernet?.ip != null -> "有线: ${ethernet?.ip}"
 else -> "未连接网络"
}
```

**【考察点与得分技巧】**
1. 不只讲 `?.` 和 `?:`，要展示从类型系统层面理解空安全。
2. 结合 JSON 解析场景展示实际收益，这个场景面试官几乎必问。
3. 提到平台类型风险和互操作注意事项，证明有混合开发经验。

**【易踩坑警示】**
- 常见错误：滥用 `!!`，"先把功能跑通后面再处理"——结果上线后各种 crash。`!!` 应该极其克制，仅用于"当前必须非空，否则程序不应继续"的场景。
- 常见错误：Java 返回的集合在 Kotlin 中可能为 null（未初始化），直接遍历 crash。应使用 `orEmpty()` 处理。

---

### 14. Kotlin Flow 的背压（backpressure）处理

**【面试官提问】** 当 Flow 的生产者速度远快于消费者时，就会出现背压问题。在NAS 私有云 的实时文件同步场景中，服务端可能会快速推送大量文件变更事件。请说明 Flow 有哪些处理背压的方式？

**【候选人回答】**

**背压（Backpressure）** 是指生产者发射数据的速度超过了消费者的处理速度。Flow 提供了多种操作符来处理背压：

**1. `buffer()` — 使用缓冲区（无界或指定容量）**

```kotlin
flow {
 for (i in 1..1000) {
 emit(i) // 快速发射
 }
}.buffer(capacity = 64) // 添加缓冲区，生产者不等待消费者
 .collect { value ->
 delay(100) // 慢速消费
 println(value)
}
// buffer(Channel.RENDEZVOUS) 默认零缓冲区
// buffer(Channel.UNLIMITED) 无限制缓冲区（可能导致 OOM）
// buffer(Channel.CONFLATED) 只保留最新值
```

**2. `conflate()` — 只保留最新值**

```kotlin
flow {
 for (i in 1..1000) {
 emit(i)
 }
}.conflate() // 丢弃中间值，只保留最新
 .collect { value ->
 delay(100)
 println(value) // 输出 1, 可能跳过很多, 接近 1000
}
```

**3. `collectLatest()` — 有新值就取消旧处理**

```kotlin
flow {
 for (i in 1..1000) {
 emit(i)
 }
}.collectLatest { value ->
 delay(100) // 每次新值到达，取消前一次 collect 的处理
 println(value)
}
// 最终只输出 1000（最后的 emit 完成的处理）
```

**4. `sample()` — 周期性采样**

```kotlin
flow { /* 快速发射 */ }
 .sample(200) // 每 200ms 取一个值
 .collect { value -> /* 处理 */ }
```

**文件同步的背压处理设计：**

```kotlin
class FileSyncManager(private val nasApi: NasApiService) {
 
 fun observeFileChanges(): Flow<List<FileChange>> {
 return nasApi.fileChangeFlow() // WebSocket 推送，可能非常频繁
 .conflate() // 高频率变更时只保留最新一批
 .buffer(Channel.RENDEZVOUS) // 不额外缓存，直接交给下游
 .flatMapLatest { changes ->
 // 有新变更到，取消旧的处理
 flow {
 emit(syncToLocal(changes))
 }
 }
 }
 
 private suspend fun syncToLocal(changes: List<FileChange>): SyncResult {
 return withContext(Dispatchers.IO) {
 // 比较耗时的本地同步操作
 database.withTransaction {
 changes.forEach { applyChange(it) }
 }
 SyncResult.Success
 }
 }
}
```

**选择策略：**

| 场景 | 操作符 | 原因 |
|------|-------|------|
| UI 数据展示（如进度条） | `conflate()` | 只关心最新状态 |
| 搜索输入框 | `debounce()` + `collectLatest()` | 忽略中间输入，取消旧请求 |
| 事件上报（不能丢） | `buffer(Channel.UNLIMITED)` | 所有事件都要处理 |
| 传感器数据 | `sample()` | 按固定频率采样 |

**【面试官追问】** `conflate()` 和 `collectLatest()` 都会丢弃数据，它们的区别是什么？什么时候用哪个？

**【候选人回答】**

```kotlin
// conflate：丢数据但不取消消费者
flow {
 emit(1)
 emit(2) // 1 还没消费完，2 覆盖了缓冲区
 emit(3) // 2 还没消费完，3 覆盖
}.conflate().collect { value ->
 delay(500)
 println(value) // 输出：1, 3（2 被丢弃）
}

// collectLatest：取消旧的消费者
flow {
 emit(1)
 emit(2) // 取消正在处理 1 的消费者
 emit(3) // 取消正在处理 2 的消费者
}.collectLatest { value ->
 delay(500)
 println(value) // 输出：3（只有最后一个是完整处理的）
}
```

**选择依据**：

- `conflate()`：消费者处理**不能被中断**（如数据库事务、文件写入中途不能取消）。
- `collectLatest()`：消费者处理**可以被中断**且新数据到来时旧处理结果已无意义（如搜索请求、UI 刷新）。

在IoT 应用场景中：
- 文件索引更新 → `conflate()`（不能中断数据库写入）
- 搜索结果展示 → `collectLatest()`（新搜索词来了，旧搜索结果没意义）

**【考察点与得分技巧】**
1. 不只列出操作符，能讲出每种操作符的内部机制（buffer 容量、取消行为）。
2. 结合实际场景做选择（文件同步选 conflate，搜索选 collectLatest）。
3. 提到 `Channel.UNLIMITED` 的 OOM 风险，体现安全意识。

**【易踩坑警示】**
- 常见错误：在高频生产者中使用 `buffer(Channel.UNLIMITED)`，内存持续增长导致 OOM。
- 常见错误：`conflate()` 丢失中间值后，依赖"每条数据都处理"的业务逻辑会出错。

---

### 15. `suspendCoroutine` 和 `suspendCancellableCoroutine` 的区别

**【面试官提问】** 当需要将回调式 API 转换为协程时，会用到 `suspendCoroutine` 和 `suspendCancellableCoroutine`。请说明它们的区别，并演示如何用它们封装NAS 私有云 SDK 中的回调式 API。

**【候选人回答】**

**区别：**

| 特性 | `suspendCoroutine` | `suspendCancellableCoroutine` |
|------|-------------------|------------------------------|
| 取消支持 | 不支持取消 | 支持取消（通过 `invokeOnCancellation`） |
| 资源释放 | 无法感知协程取消 | 可以在协程取消时释放回调注册的资源 |
| 适用场景 | 简单的、不可取消的一次性回调 | 需要释放资源的回调（网络、蓝牙、文件操作） |
| API 版本 | Kotlin 1.1+ | Kotlin 1.2+ |

**推荐：几乎总是使用 `suspendCancellableCoroutine`**。

**NAS 私有云 SDK 的封装：**

```kotlin
// SDK 提供的原始回调 API
interface NasConnectionCallback {
 fun onConnected(session: NasSession)
 fun onError(code: Int, message: String)
}

class NasSdkWrapper {
 
 // 方案1：suspendCoroutine（不推荐，无法感知取消）
 suspend fun connectSimple(): NasSession = suspendCoroutine { continuation ->
 NasSdk.connect(object : NasConnectionCallback {
 override fun onConnected(session: NasSession) {
 continuation.resume(session)
 }
 override fun onError(code: Int, message: String) {
 continuation.resumeWithException(NasException(code, message))
 }
 })
 }
 
 // 方案2：suspendCancellableCoroutine（正确做法）
 suspend fun connect(): NasSession = suspendCancellableCoroutine { continuation ->
 val callback = object : NasConnectionCallback {
 override fun onConnected(session: NasSession) {
 if (continuation.isActive) {
 continuation.resume(session)
 }
 }
 override fun onError(code: Int, message: String) {
 if (continuation.isActive) {
 continuation.resumeWithException(NasException(code, message))
 }
 }
 }
 
 val requestId = NasSdk.connect(callback)
 
 // 关键：协程被取消时，取消 SDK 的连接请求
 continuation.invokeOnCancellation { cause ->
 NasSdk.cancelConnect(requestId)
 NasLogger.d("NAS连接被取消: ${cause?.message}")
 }
 }
 
 // 蓝牙设备扫描的封装
 suspend fun scanBleDevices(timeoutMs: Long = 10000): List<BleDevice> = 
 suspendCancellableCoroutine { continuation ->
 val scanner = BluetoothLeScannerCompat.getScanner()
 val devices = mutableListOf<BleDevice>()
 
 val scanCallback = object : ScanCallback() {
 override fun onScanResult(callbackType: Int, result: ScanResult) {
 devices.add(BleDevice.from(result))
 }
 
 override fun onScanFailed(errorCode: Int) {
 if (continuation.isActive) {
 continuation.resumeWithException(
 BleException("扫描失败: $errorCode")
 )
 }
 }
 }
 
 scanner.startScan(scanCallback)
 
 // 超时自动停止
 val timeoutJob = CoroutineScope(Dispatchers.Default).launch {
 delay(timeoutMs)
 if (continuation.isActive) {
 continuation.resume(devices.toList())
 }
 }
 
 continuation.invokeOnCancellation {
 scanner.stopScan(scanCallback)
 timeoutJob.cancel()
 }
 }
}
```

**【面试官追问】** `continuation.resume` 可以多次调用吗？如果不小心调用了两次会发生什么？

**【候选人回答】**

**不能多次调用**。`Continuation` 的设计是单次使用，多次调用会导致 `IllegalStateException: Already resumed`。

```kotlin
suspend fun badExample(): String = suspendCancellableCoroutine { cont ->
 // 错误做法：可能多次调用 resume
 button.setOnClickListener {
 cont.resume("clicked") // 第二次点击会崩溃
 }
}

// 正确做法：检查 isActive
suspend fun goodExample(): String = suspendCancellableCoroutine { cont ->
 button.setOnClickListener {
 if (cont.isActive) {
 cont.resume("clicked")
 button.isEnabled = false // 防止重复点击
 }
 }
}
```

在实际项目中，一次点击后应取消监听器注册，避免资源泄漏和重复回调。

**【考察点与得分技巧】**
1. 知道 `suspendCancellableCoroutine` 是正确选择，而非 `suspendCoroutine`。
2. `invokeOnCancellation` 释放资源是核心得分点——体现对资源管理的敏感性。
3. `continuation.isActive` 的检查，防止 `Already resumed` 异常。

**【易踩坑警示】**
- 常见错误：使用 `suspendCoroutine` 封装蓝牙扫描，页面退出时协程取消但蓝牙仍在扫描，持续耗电。
- 常见错误：forget 调用 `invokeOnCancellation`，导致回调注册泄漏（如 EventBus 监听器未注销）。

---

### 16. Kotlin Channel 的使用场景

**【面试官提问】** Kotlin 的 `Channel` 和 `Flow` 都可以传递数据流。请说明 `Channel` 的特点以及什么场景下应该用 `Channel` 而不是 `Flow`。在IoT 应用多设备管理的场景中，如何使用 `Channel`？

**【候选人回答】**

**Channel 的核心特点：**

- **"热"数据通道**，类似 `BlockingQueue`
- 支持多个生产者和多个消费者
- 发送和接收都是挂起函数（`send()`/`receive()`）
- 可以关闭（`close()`），关闭后不能再发送
- **非响应式**：不是基于 `collect` 的声明式处理

**Channel vs Flow 选择：**

| 场景 | 推荐 | 原因 |
|------|------|------|
| 响应式数据流（数据绑定到 UI） | Flow | 声明式、支持操作符链 |
| 多生产者单消费者 | Channel | Flow 只能单生产者 |
| Actor 模式 / 状态机 | Channel | 更自然的消息处理模型 |
| 生产者消费者速率不同 | Channel | 内置缓冲策略 |
| 需要关闭通道通知 | Channel | `close()` + `for (item in channel)` |

**IoT 应用多设备管理的 Channel 应用：**

```kotlin
// 场景：管理多个智能设备的命令队列
class DeviceCommandManager {
 
 // 每个设备独立的命令通道（多生产者单消费者模式）
 private val commandChannels = ConcurrentHashMap<String, Channel<DeviceCommand>>()
 private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
 
 fun registerDevice(deviceId: String) {
 val channel = Channel<DeviceCommand>(capacity = Channel.BUFFERED)
 commandChannels[deviceId] = channel
 
 // 启动消费者协程
 scope.launch {
 for (command in channel) { // channel 关闭时自动退出循环
 try {
 executeCommand(deviceId, command)
 } catch (e: Exception) {
 NasLogger.e("执行命令失败: ${command.type}", e)
 }
 }
 NasLogger.d("设备 $deviceId 命令通道已关闭")
 }
 }
 
 // 多个组件可以同时发送命令（多生产者）
 suspend fun sendCommand(deviceId: String, command: DeviceCommand) {
 commandChannels[deviceId]?.send(command) ?: throw DeviceNotRegistered(deviceId)
 }
 
 fun unregisterDevice(deviceId: String) {
 commandChannels.remove(deviceId)?.close()
 }
}

// 事件总线——Channel 的另一个经典场景
object EventBus {
 private val _events = Channel<AppEvent>(Channel.BUFFERED)
 val events = _events.receiveAsFlow() // 对外暴露为 Flow
 
 suspend fun post(event: AppEvent) {
 _events.send(event)
 }
}

sealed class AppEvent {
 data class NasConnected(val deviceId: String) : AppEvent()
 data class NasDisconnected(val deviceId: String) : AppEvent()
 data class FileSyncCompleted(val count: Int) : AppEvent()
 object StorageLow : AppEvent()
}
```

**【面试官追问】** `Channel` 有几种类型（RENDEZVOUS、BUFFERED、CONFLATED、UNLIMITED）？在设备命令队列中为什么选择 `BUFFERED`？

**【候选人回答】**

| 类型 | 容量 | 行为 |
|------|------|------|
| `RENDEZVOUS`（默认） | 0 | 发送方必须等到接收方准备好 |
| `BUFFERED` | 64（默认） | 64 个缓冲，超过则挂起发送方 |
| `CONFLATED` | 1 | 只保留最新值，发送覆盖旧值 |
| `UNLIMITED` | 无限 | 永不挂起发送方，可能 OOM |

**设备命令队列选 `BUFFERED` 的原因：**

```kotlin
// BUFFERED：允许适当缓冲命令，避免发送方频繁挂起
// 但如果命令堆积（64个以上），自动挂起发送方，起到背压作用
// 比 UNLIMITED 更安全（防止内存爆炸）
// 比 RENDEZVOUS 更高效（减少发送方挂起次数）
```

选择决策矩阵：
- 设备命令队列 → `BUFFERED`（允许适当缓冲 + 背压保护）
- UI 事件 → `CONFLATED`（只关心最新状态）
- 不可丢失的数据上报 → `UNLIMITED` + 上限检查（确保所有数据都处理）

**【考察点与得分技巧】**
1. 能指出 Flow 和 Channel 的不同设计哲学（声明式 vs 命令式）。
2. `receiveAsFlow()` 将 Channel 转为 Flow，展示组合使用能力。
3. 设备命令队列 + Buffer 策略，展示 IoT 场景的系统设计能力。

**【易踩坑警示】**
- 常见错误：`Channel` 忘记 `close()`，导致 `for (item in channel)` 循环永不退出，协程泄漏。
- 常见错误：在使用 `CONFLATED` 时，期望每条消息都处理，但实际会被丢弃。

---

### 17. Kotlin `reified` 关键字的原理

**【面试官提问】** Kotlin 的 `reified` 关键字让泛型在运行时也能被访问。请解释它的原理，以及在云存储 APP 中你能用它做什么？

**【候选人回答】**

**原理：`reified` 结合 `inline` 使用，将泛型类型在编译期"具象化"。**

普通泛型在 JVM 上会被**类型擦除**，运行时无法获取泛型类型：

```kotlin
// 编译错误：Cannot use 'T' as reified type parameter
fun <T> parseJson(json: String): T {
 return Gson().fromJson(json, T::class.java)
}
```

使用 `reified` + `inline`：

```kotlin
inline fun <reified T> parseJson(json: String): T {
 return Gson().fromJson(json, T::class.java)
}

// 调用处
val result: NasFileInfo = parseJson(jsonString)

// 编译后等价于（内联展开）：
val result: NasFileInfo = Gson().fromJson(jsonString, NasFileInfo::class.java)
```

因为 `inline` 函数会将函数体直接**复制到调用处**，编译器知道调用处的具体类型，所以 `T::class.java` 可以被替换为实际类型。

**云存储 APP 中的应用：**

```kotlin
// 1. 通用 JSON 解析
inline fun <reified T> Gson.fromJsonSafe(json: String): T? {
 return try {
 fromJson(json, T::class.java)
 } catch (e: JsonSyntaxException) {
 NasLogger.e("JSON解析失败", e)
 null
 }
}

// 2. Fragment 扩展——获取 ViewModel
inline fun <reified T : ViewModel> Fragment.sharedViewModel(): T {
 return ViewModelProvider(requireActivity())[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.viewModel(): T {
 return ViewModelProvider(this)[T::class.java]
}

// 3. Intent 扩展——类型安全的导航传参
inline fun <reified T : Activity> Context.startActivity(vararg extras: Pair<String, Any?>) {
 val intent = Intent(this, T::class.java)
 extras.forEach { (key, value) ->
 when (value) {
 is String -> intent.putExtra(key, value)
 is Int -> intent.putExtra(key, value)
 is Long -> intent.putExtra(key, value)
 is Boolean -> intent.putExtra(key, value)
 // ...
 }
 }
 startActivity(intent)
}

// 使用
startActivity<FileDetailActivity>("file_id" to "12345")

// 4. SharedPreferences 泛型委托（复用前面讲过的）
inline fun <reified T> SharedPreferences.delegate(key: String, default: T) = ...
```

**【面试官追问】** 你说 `reified` 必须配合 `inline`。如果一个函数接收了 `reified` 泛型的 lambda 参数，这个 lambda 能在非内联上下文中使用吗？

**【候选人回答】**

不能直接使用。但可以通过技巧传递：

```kotlin
// 错误：non-inline 函数不能有 reified 参数
// fun <reified T> saveToCache(value: T) { ... }

// 正确方式1：通过 inline 函数桥接
fun <T> saveToCache(value: T, clazz: Class<T>) {
 val json = Gson().toJson(value)
 // 存储 json 和 clazz.name
}

inline fun <reified T> saveToCache(value: T) {
 saveToCache(value, T::class.java) // 桥接到非内联版本
}

// 正确方式2：内联函数参数可以传递 reified 类型
inline fun <reified T> withType(block: (Class<T>) -> Unit) {
 block(T::class.java)
}

// 使用
withType<NasFileInfo> { clazz ->
 // clazz 就是 NasFileInfo::class.java
 cache.put(clazz.name, data)
}
```

这就是为什么很多库都有"内联桥接非内联"的设计模式。

**【考察点与得分技巧】**
1. 解释清楚类型擦除和 `reified` + `inline` 的原理。
2. 给出多个实际使用场景（JSON 解析、ViewModel 获取、Activity 跳转）。
3. 知道 `reified` 的局限性（不能用于非内联函数、不能用于属性）。

**【易踩坑警示】**
- 常见错误：忘记加 `inline`，只有 `reified`，编译报错。
- 常见错误：在非内联的 lambda 中使用 `reified` 泛型，编译报错。

---

### 18. 协程取消的协作机制（isActive、yield、ensureActive）

**【面试官提问】** 协程的取消是"协作式"的。请说明这意味着什么？`isActive`、`yield()`、`ensureActive()` 分别怎么用？在IoT 应用的大文件下载场景中，如何让下载能够及时响应取消？

**【候选人回答】**

**"协作式取消"**意味着：取消协程不会强制终止正在执行的代码，而是设置一个取消标记。协程需要**主动检查**这个标记并响应取消。

**三个 API 对比：**

| API | 类型 | 作用 | 使用场景 |
|-----|------|------|---------|
| `isActive` | 属性检查 | 返回 `true` 如果协程未被取消 | 需要自定义取消逻辑的循环 |
| `yield()` | 挂起函数 | 检查取消 + 让出执行权给其他协程 | CPU 密集型循环中的取消检查点 |
| `ensureActive()` | 函数检查 | 如果已取消则立即抛出 `CancellationException` | 不循环，但需要检查取消状态 |

```kotlin
// isActive：手动检查
while (isActive) {
 // 处理下一批数据
 processBatch()
}

// yield：挂起点检查
repeat(1000000) {
 calculateNextValue()
 yield() // 每次循环检查取消 + 让出执行权
}

// ensureActive：关键节点检查
fun criticalOperation() {
 ensureActive() // 检查是否已取消
 // 如果不取消，继续执行
 performRiskyOperation()
}
```

**IoT 应用大文件下载的取消设计：**

```kotlin
class FileDownloader(private val nasApi: NasApiService) {
 
 suspend fun downloadFile(fileId: String, savePath: String): Flow<DownloadProgress> = flow {
 val fileInfo = nasApi.getFileInfo(fileId)
 val chunks = calculateChunks(fileInfo.size, chunkSize = 1024 * 1024) // 1MB每块
 
 var downloadedBytes = 0L
 
 for ((index, chunk) in chunks.withIndex()) {
 // 关键：每个块下载前检查是否已取消
 ensureActive()
 
 val data = nasApi.downloadChunk(fileId, chunk.offset, chunk.size)
 
 saveChunkToFile(savePath, chunk.offset, data)
 downloadedBytes += data.size
 
 emit(
 DownloadProgress(
 total = fileInfo.size,
 downloaded = downloadedBytes,
 percent = (downloadedBytes * 100 / fileInfo.size).toInt()
 )
 )
 }
 }.flowOn(Dispatchers.IO)
 
 // 对于无法在循环中检查取消的阻塞操作
 suspend fun downloadWithProgress(fileId: String): Flow<Int> = flow {
 val job = coroutineContext[Job]!!
 
 val task = CompletableDeferred<Int>()
 val listener = object : DownloadListener {
 override fun onProgress(percent: Int) {
 // 检查取消状态
 if (job.isActive) {
 // 通过 Channel 等方式发送进度
 }
 }
 
 override fun onComplete() {
 task.complete(100)
 }
 }
 
 nasApi.startDownload(fileId, listener)
 
 // 协程取消时同时取消 SDK 的下载
 job.invokeOnCompletion {
 nasApi.cancelDownload(fileId)
 }
 
 emit(task.await())
 }
}
```

**重要：suspend 函数的挂起点本身就是取消检查点**

```kotlin
suspend fun download() {
 // delay、withContext、网络请求等挂起函数内部都会检查取消
 delay(1000) // 取消检查点
 withContext(Dispatchers.IO) { ... } // 取消检查点
 nasApi.getFileInfo(id) // 取消检查点（如果内部是挂起函数）
}
```

**非协作式代码的问题：**

```kotlin
// 这个循环不响应取消——即使调用了 job.cancel()
suspend fun badLoop() {
 var sum = 0L
 for (i in 1..10000000000) {
 sum += i // 纯计算，无挂起点，不检查取消
 }
 // 取消被忽略，循环执行完才能响应
}

// 修正：添加 yield() 或 ensureActive()
suspend fun goodLoop() {
 var sum = 0L
 for (i in 1..10000000000) {
 sum += i
 if (i % 10000 == 0) yield() // 每 10000 次检查一次取消
 }
}
```

**【面试官追问】** 有一些 suspend 函数底层调用的是阻塞 API（如 `InputStream.read()`），这些阻塞调用无法响应协程取消，怎么办？

**【候选人回答】**

需要使用 `withContext(Dispatchers.IO)` 配合线程中断：

```kotlin
suspend fun downloadWithBlockingApi(url: String, outputFile: File) = 
 withContext(Dispatchers.IO) {
 // Dispatchers.IO 的线程在协程取消时会被 interrupt
 val connection = URL(url).openConnection() as HttpURLConnection
 try {
 connection.inputStream.use { input ->
 outputFile.outputStream().use { output ->
 val buffer = ByteArray(8192)
 var bytesRead: Int
 while (input.read(buffer).also { bytesRead = it } != -1) {
 // 注意：InputStream.read() 本身不响应 interrupt
 // 但后续的 write 或者循环中的检查可以
 output.write(buffer, 0, bytesRead)
 }
 }
 }
 } catch (e: InterruptedIOException) {
 // 协程取消时线程被 interrupt，抛出此异常
 throw CancellationException("下载被取消", e)
 }
 }

// 更好的做法：使用 NIO 的可中断通道
suspend fun downloadWithNio(url: String, outputFile: File) = 
 withContext(Dispatchers.IO) {
 val connection = URL(url).openConnection() as HttpURLConnection
 val channel = Channels.newChannel(connection.inputStream)
 val fileChannel = FileChannel.open(
 outputFile.toPath(), 
 StandardOpenOption.CREATE, 
 StandardOpenOption.WRITE
 )
 try {
 fileChannel.transferFrom(channel, 0, Long.MAX_VALUE)
 // FileChannel.transferFrom 响应线程中断
 } catch (e: ClosedByInterruptException) {
 throw CancellationException("下载被取消", e)
 }
 }
```

关键理解：`Dispatchers.IO` 的线程在协程取消时会被 `interrupt()`，阻塞 API 如果响应中断，就可以转为 `CancellationException`。

**【考察点与得分技巧】**
1. "协作式"的本质——需要代码主动检查，不是抢占式 kill。
2. 三种 API 的适用场景区分，展示对 API 设计的理解。
3. 大文件下载的分块循环中加 `ensureActive()`，体现实战经验。
4. 阻塞 API 转协程取消的处理（配合 `Dispatchers.IO` 的线程中断），这是高级话题。

**【易踩坑警示】**
- 常见错误：在纯计算循环中没有 `yield()` 或 `isActive` 检查，导致协程取消不了，`viewModelScope` 中泄漏计算任务。
- 常见错误：捕获了 `CancellationException` 但不重新抛出，破坏了协程的取消传播。只有在特定场景（如 `finally` 块）才需要捕获后重抛。

---
