# Android + Jetpack 面试八股文（150题）

> 适用岗位：Android 开发工程师 
> 技术栈：Android + Jetpack + Kotlin + MVVM + Flutter + 鸿蒙 
> 面向级别：中高级 / 资深工程师（5年+）

---

## 目录

### 一、Kotlin 核心（30题）
1. [密封类（sealed class）的作用与使用场景](#kotlin-01)
2. [数据类（data class）的原理与注意事项](#kotlin-02)
3. [扩展函数的原理与限制](#kotlin-03)
4. [高阶函数与 Lambda 表达式](#kotlin-04)
5. [内联函数（inline）原理与使用场景](#kotlin-05)
6. [协程基础：launch、async 与 withContext 的区别](#kotlin-06)
7. [Flow 冷流与热流的区别](#kotlin-07)
8. [StateFlow 与 SharedFlow 的区别与使用场景](#kotlin-08)
9. [suspend 函数的原理与挂起机制](#kotlin-09)
10. [协程上下文与调度器（CoroutineContext & Dispatchers）](#kotlin-10)
11. [结构化并发（Structured Concurrency）](#kotlin-11)
12. [协程异常处理机制](#kotlin-12)
13. [作用域函数：let、run、apply、also、with 的区别](#kotlin-13)
14. [委托属性（Delegated Properties）原理](#kotlin-14)
15. [延迟初始化：lateinit 与 by lazy](#kotlin-15)
16. [Kotlin 可空类型与空安全机制](#kotlin-16)
17. [Kotlin 类型安全与智能转换（Smart Cast）](#kotlin-17)
18. [泛型型变：in 与 out](#kotlin-18)
19. [object 关键字的三种用法](#kotlin-19)
20. [伴生对象（companion object）与静态成员](#kotlin-20)
21. [解构声明（Destructuring Declarations）](#kotlin-21)
22. [内联类（inline class / value class）](#kotlin-22)
23. [协程取消机制与取消协作](#kotlin-23)
24. [Channel 的使用场景与类型](#kotlin-24)
25. [supervisorScope 与 coroutineScope 的区别](#kotlin-25)
26. [Sequence 与集合的区别](#kotlin-26)
27. [reified 关键字的作用](#kotlin-27)
28. [infix 函数的使用与限制](#kotlin-28)
29. [by lazy 与 lateinit 的底层实现对比](#kotlin-29)
30. [Kotlin 协程在 Android 中的最佳实践](#kotlin-30)

### 二、Jetpack 核心组件（40题）
#### ViewModel（8题）
31. [ViewModel 的生命周期与原理](#jetpack-01)
32. [ViewModelProvider 的创建过程源码分析](#jetpack-02)
33. [SavedStateHandle 的原理与使用](#jetpack-03)
34. [ViewModelScope 与 viewModelScope](#jetpack-04)
35. [AndroidViewModel vs ViewModel](#jetpack-05)
36. [onCleared 的调用时机与资源释放](#jetpack-06)
37. [跨 Fragment 共享 ViewModel](#jetpack-07)
38. [ViewModel 的工厂模式与依赖注入](#jetpack-08)

#### LiveData（6题）
39. [LiveData 的原理机制](#jetpack-09)
40. [Transformations：map 与 switchMap](#jetpack-10)
41. [MediatorLiveData 的使用场景](#jetpack-11)
42. [postValue 与 setValue 的区别](#jetpack-12)
43. [observeForever 的内存泄漏问题](#jetpack-13)
44. [LiveData vs Flow 的对比与选择](#jetpack-14)

#### Lifecycle（6题）
45. [LifecycleOwner 与 LifecycleObserver](#jetpack-15)
46. [Lifecycle 状态机与事件流转](#jetpack-16)
47. [ProcessLifecycleOwner 的应用](#jetpack-17)
48. [LifecycleService 的使用场景](#jetpack-18)
49. [自定义 LifecycleOwner](#jetpack-19)
50. [Lifecycle 在 MVP/MVVM 中的应用](#jetpack-20)

#### Room（8题）
51. [Entity、DAO、Database 三要素](#jetpack-21)
52. [Room 中 @Query、@Insert、@Update、@Delete 的高级用法](#jetpack-22)
53. [Room 与 Flow 的集成](#jetpack-23)
54. [数据库迁移（Migration）策略](#jetpack-24)
55. [TypeConverter 自定义类型转换](#jetpack-25)
56. [@Relation、@ForeignKey、@Index 的使用](#jetpack-26)
57. [Room 与 RxJava/协程的集成](#jetpack-27)
58. [Room FTS 全文搜索](#jetpack-28)

#### Navigation（6题）
59. [NavGraph 与 NavHostFragment 的工作原理](#jetpack-29)
60. [Safe Args 参数传递](#jetpack-30)
61. [Deep Link 的实现方式](#jetpack-31)
62. [条件导航与导航拦截](#jetpack-32)
63. [返回栈管理与多重返回栈](#jetpack-33)
64. [NavigationUI 与底部导航集成](#jetpack-34)

#### DataBinding（3题）
65. [DataBinding 的工作原理](#jetpack-35)
66. [双向绑定与自定义 BindingAdapter](#jetpack-36)
67. [DataBinding 与 ViewBinding 的对比](#jetpack-37)

#### WorkManager（3题）
68. [WorkManager 任务调度机制](#jetpack-38)
69. [约束条件与任务重试策略](#jetpack-39)
70. [链式任务与唯一任务](#jetpack-40)

### 三、MVVM 架构与设计模式（20题）
71. [MVVM 三层职责划分](#mvvm-01)
72. [Repository 模式的设计原则](#mvvm-02)
73. [Hilt 依赖注入原理与使用](#mvvm-03)
74. [Koin vs Hilt vs Dagger 对比](#mvvm-04)
75. [单例模式在 Android 中的安全实现](#mvvm-05)
76. [观察者模式的多种实现](#mvvm-06)
77. [工厂模式与抽象工厂模式](#mvvm-07)
78. [建造者模式（Builder）的实际应用](#mvvm-08)
79. [适配器模式在 RecyclerView 中的体现](#mvvm-09)
80. [策略模式的实际应用](#mvvm-10)
81. [Clean Architecture 分层架构](#mvvm-11)
82. [UseCase 的设计原则](#mvvm-12)
83. [单向数据流（UDF）的设计与实践](#mvvm-13)
84. [Android 状态管理最佳实践](#mvvm-14)
85. [事件处理：SingleLiveEvent 与 EventWrapper](#mvvm-15)
86. [MVI 架构的核心思想](#mvvm-16)
87. [MVP vs MVVM vs MVI 对比](#mvvm-17)
88. [模块化架构设计原则](#mvvm-18)
89. [依赖倒置原则（DIP）在 Android 中的应用](#mvvm-19)
90. [Android 中的组合优于继承实践](#mvvm-20)

### 四、Android 基础（30题）
91. [Activity 的生命周期与常见场景](#android-01)
92. [Fragment 生命周期与 FragmentManager](#android-02)
93. [Activity 的四种启动模式](#android-03)
94. [Android 进程优先级与保活](#android-04)
95. [Binder IPC 机制深入](#android-05)
96. [AIDL 的使用与原理](#android-06)
97. [Handler、Looper、MessageQueue 原理解析](#android-07)
98. [Handler 内存泄漏与解决方案](#android-08)
99. [View 绘制流程：measure、layout、draw](#android-09)
100. [事件分发机制详解](#android-10)
101. [RecyclerView 优化与缓存机制](#android-11)
102. [属性动画与补间动画的区别](#android-12)
103. [Bitmap 优化与内存管理](#android-13)
104. [常见内存泄漏场景与排查](#android-14)
105. [LeakCanary 原理分析](#android-15)
106. [ANR 产生原因与排查方法](#android-16)
107. [OOM 优化与内存监控](#android-17)
108. [ProGuard/R8 混淆原理与规则](#android-18)
109. [多渠道打包方案](#android-19)
110. [Gradle 构建优化](#android-20)
111. [组件化与模块化架构设计](#android-21)
112. [热修复与插件化原理](#android-22)
113. [WebView 安全与优化](#android-23)
114. [APK 瘦身方案](#android-24)
115. [Android 屏幕适配方案](#android-25)
116. [Android 权限机制（运行时权限）](#android-26)
117. [应用启动优化](#android-27)
118. [卡顿监控与优化](#android-28)
119. [Android Service 的使用与限制](#android-29)
120. [BroadcastReceiver 的原理与限制](#android-30)

### 五、网络与数据（15题）
121. [OkHttp 拦截器链机制](#net-01)
122. [Retrofit 动态代理原理](#net-02)
123. [协程 + Retrofit 封装的最佳实践](#net-03)
124. [HTTPS 证书校验与 SSL Pinning](#net-04)
125. [WebSocket 的实现与心跳机制](#net-05)
126. [TCP 与 UDP 的区别及 Socket 编程](#net-06)
127. [三级缓存策略设计](#net-07)
128. [序列化方式对比：Serializable、Parcelable、Kotlin Serialization](#net-08)
129. [JSON 解析框架对比：Gson、Moshi、kotlinx.serialization](#net-09)
130. [Android 文件存储方式对比](#net-10)
131. [SharedPreferences 与 DataStore](#net-11)
132. [ProtoBuf 序列化与优势](#net-12)
133. [网络安全配置（Network Security Config）](#net-13)
134. [DNS 优化与 HTTPDNS](#net-14)
135. [文件上传与下载的实现方案](#net-15)

### 六、性能优化（10题）
136. [冷启动、热启动、温启动优化](#perf-01)
137. [内存优化全面策略](#perf-02)
138. [包体积优化方案](#perf-03)
139. [电量优化策略](#perf-04)
140. [网络请求优化](#perf-05)
141. [UI 渲染优化：过度绘制与布局层级](#perf-06)
142. [Android Profiler 工具使用](#perf-07)
143. [Systrace 与 Perfetto 使用](#perf-08)
144. [Benchmark 基准测试](#perf-09)
145. [启动任务调度框架设计](#perf-10)

### 七、测试与质量（5题）
146. [单元测试：JUnit + Mockito + MockK](#test-01)
147. [UI 测试：Espresso 与 Compose Testing](#test-02)
148. [CI/CD 流程设计](#test-03)
149. [代码审查要点与规范](#test-04)
150. [崩溃监控与 APM 体系](#test-05)

---

## 一、Kotlin 核心（30题）

### <a id="kotlin-01"></a>1. 密封类（sealed class）的作用与使用场景

**标准回答：**

密封类是 Kotlin 中的一种特殊类，使用 `sealed` 关键字修饰。它的核心特征是在**编译期就确定了所有子类的数量和类型**——所有子类必须在同一个文件中声明（Kotlin 1.5 后允许在同一包内，但要求在同一模块中）。密封类常用于表示受限的类层次结构，最经典的应用场景是**网络请求结果的封装**和**UI 状态的建模**。

```kotlin
sealed class Result<out T> {
 data class Success<T>(val data: T) : Result<T>()
 data class Error(val exception: Throwable) : Result<Nothing>()
 object Loading : Result<Nothing>()
}
```

密封类的核心优势在于配合 `when` 表达式使用时，编译器能进行**穷尽性检查**（Exhaustive Checking）——如果 when 分支没有覆盖所有子类，编译器会直接报错。这意味着当你新增一个状态时，编译器会强制你处理所有引用位置，极大地减少了因遗漏状态导致的 Bug。相比之下，普通抽象类无法获得这种编译期保障。

对于智能硬件的 IoT 设备交互场景，密封类特别适合表示设备的连接状态（连接中、已连接、断开、错误等）。

**高频追问：**
1. 密封类与枚举类的本质区别是什么？枚举是单例的值的集合，而密封类的每个子类可以有不同的状态和多个实例。
2. Kotlin 1.5 对密封类的改进有哪些？（允许同包不同文件、支持在 sealed interface 中使用）
3. 实际项目中你是如何用密封类做 UI 状态管理的？结合 StateFlow 使用。

**面试官考察点：**
- 对 Kotlin 类型系统的深度理解
- 能否在正确场景使用密封类（Restricted Class Hierarchies）
- 是否理解编译时安全检查的价值

**易踩坑：**
- ❌ 将密封类用在子类可能频繁增减的场景——这会导致大量修改
- ❌ 认为密封类可以替代枚举——两者的语义和用途不同
- ❌ Kotlin 1.5 以前把子类写在别的文件中导致编译失败

---

### <a id="kotlin-02"></a>2. 数据类（data class）的原理与注意事项

**标准回答：**

数据类是 Kotlin 中用于纯粹保存数据的类，使用 `data` 关键字修饰。编译器会自动生成以下方法：
- `equals()` / `hashCode()` 基于主构造函数中声明的所有属性
- `toString()` 格式为 `"ClassName(prop1=value1, prop2=value2)"`
- `componentN()` 支持解构声明
- `copy()` 方法用于浅拷贝

```kotlin
data class User(val id: Int, val name: String, var age: Int)
```

使用时需注意关键约束：
1. 主构造函数必须至少有一个参数
2. 主构造函数的参数必须用 `val` 或 `var` 声明
3. 不能使用 `abstract`、`open`、`sealed`、`inner` 修饰
4. `copy()` 是**浅拷贝**，引用类型字段只复制引用
5. 自动生成的 `equals()` 和 `hashCode()` 只基于主构造函数属性，类体内声明的属性不参与
6. 如果数据类的父类已经定义了 `equals()` 且为 `final`，编译器不会重写

数据类特别适合做网络请求的 DTO（Data Transfer Object）、Room 的 `@Entity` 等场景。

**高频追问：**
1. `copy()` 是深拷贝还是浅拷贝？如何实现深拷贝？（浅拷贝，需手动实现或序列化/反序列化）
2. 数据类可以和继承一起使用吗？（可以，但有很多限制，Kotlin 1.9 后支持数据类继承数据类）
3. 自动生成的 `equals` 对数组类字段是如何比较的？（使用引用比较，不是内容比较，这是坑）

**面试官考察点：**
- 对 Kotlin 编译器生成代码的理解
- 是否在实际项目中使用过 data class 的高级特性
- 对 copy 机制的深入理解（immutability 模式）

**易踩坑：**
- ❌ 以为 `copy()` 是深拷贝，导致修改副本后原对象也被影响
- ❌ 在 `data class` 中使用数组类型字段，`equals` 比较时不符合预期
- ❌ 在 JPA/Hibernate Entity 中使用 `data class`，会与代理机制冲突

---

### <a id="kotlin-03"></a>3. 扩展函数的原理与限制

**标准回答：**

扩展函数是 Kotlin 的一大特性，允许在不继承或使用装饰模式的情况下，为现有类添加新的函数。其原理是**编译期将扩展函数转换为静态方法**，调用时把接收者对象作为第一个参数传入。

```kotlin
// Kotlin 源码
fun String.addPrefix(prefix: String): String = "$prefix$this"

// 编译后等价于（反编译 Java）
public static String addPrefix(String $this$addPrefix, String prefix) {
 return prefix + $this$addPrefix;
}
```

扩展函数的重要限制：
1. **静态解析**：具体调用哪个扩展函数由调用处的**声明类型**决定，而非运行时实际类型（没有多态）
2. **无法访问私有成员**：只能访问接收者的 `public`/`internal` 成员
3. **成员函数优先**：如果类已有同名同签名成员函数，扩展函数永远不会被调用
4. **不支持重写**：扩展函数不是虚函数

扩展函数最适合的场景是为第三方库、Android SDK 类添加便捷方法，例如为 `View` 添加 `setVisible()` / `setGone()` 扩展，提升代码可读性。

**高频追问：**
1. 扩展属性和扩展函数的区别是什么？（扩展属性不能有 backing field，必须显式定义 getter）
2. 成员扩展函数是什么？有什么作用？（在一个类内部为另一个类定义扩展，形成双重接收者）
3. 扩展函数可以实现多态吗？（不能，它是静态解析的）

**面试官考察点：**
- 是否理解 Kotlin 扩展函数的编译原理（语法糖）
- 是否知道扩展函数的限制和使用边界
- 项目中是否合理使用扩展函数改善 API

**易踩坑：**
- ❌ 以为扩展函数可以像成员函数一样被多态调用
- ❌ 在 Kotlin 代码中定义了 `String.isNullOrEmpty()` 扩展，与标准库冲突
- ❌ 滥用扩展函数替代工具类，导致代码难以追踪

---

### <a id="kotlin-04"></a>4. 高阶函数与 Lambda 表达式

**标准回答：**

高阶函数是将函数作为参数或返回值的函数。Kotlin 通过 `FunctionN` 类型和 Lambda 表达式来支持。每个 Lambda 表达式在编译时会被转换为一个 `FunctionN` 接口的匿名实现（N 为参数个数）。

```kotlin
// 高阶函数定义
fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> {
 val result = mutableListOf<T>()
 for (item in this) {
 if (predicate(item)) result.add(item)
 }
 return result
}

// 调用时使用尾随 Lambda 语法
val even = listOf(1, 2, 3, 4).customFilter { it % 2 == 0 }
```

关键知识点：
1. **尾随 Lambda**：最后一个参数是 Lambda 时，可以写在括号外面
2. **`it` 隐式参数**：单参数 Lambda 可以用 `it` 代替显式参数名
3. **Lambda vs 匿名函数**：Lambda 无法显式指定返回类型（通过最后一行表达式推断），匿名函数可以
4. **闭包**：Lambda 可以访问和修改其外部作用域的变量
5. **SAM 转换**：Kotlin Lambda 可以自动转换为 Java 的单抽象方法接口

在 Android 中，高阶函数广泛用于回调替代（替代 `interface`）、集合操作（`map`/`filter`/`reduce`）、协程构建器等场景。

**高频追问：**
1. 每次创建 Lambda 都会创建新的匿名类实例吗？（不一定，如果 Lambda 不捕获外部变量，会被编译为单例；捕获则每次创建新实例）
2. SAM 转换的具体机制是什么？如何自定义 SAM 接口？（Kotlin 1.4+ 支持 `fun interface`）
3. 带接收者的 Lambda（`A.() -> Unit`）是什么？你用过吗？（`apply`/`buildString` 等）

**面试官考察点：**
- 对函数式编程的理解深度
- 是否了解 Lambda 的编译实现和性能影响
- 实际编码中是否能灵活运用高阶函数

**易踩坑：**
- ❌ 在高频调用的循环中大量创建捕获变量的 Lambda，导致对象分配压力
- ❌ 混淆 Lambda 的 `return` 和 `return@label`，导致非预期的函数返回
- ❌ 在 `init {}` 中使用 `return` 来试图返回 Lambda 结果（实际会返回 `init` 块）

---

### <a id="kotlin-05"></a>5. 内联函数（inline）原理与使用场景

**标准回答：**

内联函数使用 `inline` 关键字修饰，其核心原理是**编译时将函数体直接插入调用处**，避免函数调用的额外开销（特别是 Lambda 参数创建匿名对象的开销）。

```kotlin
inline fun <T> measureTime(block: () -> T): Pair<T, Long> {
 val start = System.nanoTime()
 val result = block()
 return result to (System.nanoTime() - start)
}
```

内联函数的关键特性：
1. **Lambda 参数默认内联**：传入的 Lambda 也会被内联到调用处，不会创建匿名类对象
2. **`noinline`**：标记不需要内联的 Lambda 参数（例如需要传递给非内联函数时）
3. **`crossinline`**：标记不可非局部返回的 Lambda（防止在嵌套上下文中 `return`）
4. **非局部返回**：内联 Lambda 中的 `return` 会直接从**外层函数**返回

内联函数不能用于：
- 虚函数（open/override）
- 递归函数
- 包含复杂流程控制的大型函数（会导致字节码膨胀）

实际应用场景：`measureTime`、`withLock`、`use`、集合操作的 `forEach`/`filter` 等标准库函数，以及需要消除 Lambda 开销的高频调用函数。

**高频追问：**
1. `crossinline` 和 `noinline` 的本质区别是什么？什么时候必须用 `crossinline`？
2. 内联函数会导致代码膨胀，如何控制？（控制内联函数体大小，合理使用 `noinline`）
3. 内联属性（`inline val`）的原理是什么？（内联 getter 方法）

**面试官考察点：**
- 是否理解内联函数的编译期行为
- 能否正确选择 `inline`/`noinline`/`crossinline`
- 对非局部返回机制的理解

**易踩坑：**
- ❌ 在所有高阶函数上都加 `inline`，导致字节码膨胀
- ❌ 内联函数中无法通过反射或方法引用获取 Lambda 参数
- ❌ 不理解 `crossinline` 的作用，在非内联上下文中误用 `return`

---

### <a id="kotlin-06"></a>6. 协程基础：launch、async 与 withContext 的区别

**标准回答：**

`launch`、`async`、`withContext` 是 Kotlin 协程中最核心的三个构建器，它们的核心区别在于**返回值类型和使用场景**：

| 构建器 | 返回值 | 是否阻塞 | 异常传播 | 典型场景 |
|--------|--------|----------|----------|----------|
| `launch` | `Job` | 否 | 自动传播（父协程取消） | 不关心结果的并发任务 |
| `async` | `Deferred<T>` | 否 | 调用 `await()` 时抛出 | 需要返回值的并发计算 |
| `withContext` | `T` | 是（挂起） | 直接抛出 | 切换上下文执行 |

```kotlin
// launch: 发射后不管
viewModelScope.launch {
 uploadLog() // 不关心结果
}

// async: 并发获取数据
viewModelScope.launch {
 val user = async { api.getUser() }
 val config = async { api.getConfig() }
 showResult(user.await(), config.await()) // 并发等待两个结果
}

// withContext: 切换线程
suspend fun loadFromDb(): List<User> = withContext(Dispatchers.IO) {
 dao.getAllUsers()
}
```

关键要点：
- `async` + `await()` 是并发执行的，而连续两个 `withContext` 是串行的
- `launch` 内部的异常会自动传播到父协程，`async` 的异常需要 `await()` 才会抛出
- `withContext` 不会创建新协程，只在当前协程中切换上下文，是最轻量的切换方式

**高频追问：**
1. `async { ... }` 内部异常如果不调用 `await()` 会怎样？（异常在 deferred 完成时被封装，不传播）
2. `launch` + `withContext` 和 `async` 的区别是什么？何时该用哪个？
3. `coroutineScope { async {} }` 和直接 `async {}` 的区别？（结构化并发 vs 需要外部作用域）

**面试官考察点：**
- 是否能根据场景正确选择协程构建器
- 对并发和串行执行的理解
- 对异常传播机制的掌握

**易踩坑：**
- ❌ 在 `suspend` 函数中直接用 `async` 而不加 `coroutineScope` 包裹，破坏结构化并发
- ❌ 用 `launch` 替代 `async` 获取返回值，写出错误的异常处理代码
- ❌ 忘记 `await()` 导致 `Deferred` 泄漏或结果未正确处理

---

### <a id="kotlin-07"></a>7. Flow 冷流与热流的区别

**标准回答：**

Flow 是 Kotlin 协程中的异步数据流。其核心分类是冷流（Cold Flow）和热流（Hot Flow）：

**冷流（Cold Flow）**：每次调用 `collect` 时才会启动数据生产，每个收集器独立获得一份完整数据流。`flow {}` 构建器创建的 Flow 默认是冷流。类似"点播"——每个消费者都从头开始消费。

```kotlin
val coldFlow = flow {
 println("开始生产数据")
 emit(1)
 delay(100)
 emit(2)
}
// 两个 collector 各自从头收到 1, 2
```

**热流（Hot Flow）**：数据生产与收集器无关，不管有没有收集者都在持续生产数据。多个收集器共享同一数据流，新收集器只能收到订阅之后的数据。`StateFlow` 和 `SharedFlow` 是典型的热流。

```kotlin
val hotFlow = MutableSharedFlow<Int>()
// 即使没有人 collect，数据也会被发射（取决于 repla\y 配置）
```

区别总结：
| 特性 | 冷流 | 热流 |
|------|------|------|
| 数据生成 | 每个收集器独立触发 | 独立于收集器 |
| 多收集者 | 各自独立完整流 | 共享流 |
| 生命周期 | 跟随收集者 | 独立管理 |
| 典型应用 | 数据库监听、网络请求 | UI 状态、事件总线 |

**高频追问：**
1. `channelFlow` 和 `flow {}` 的区别是什么？（前者支持并发发送，类似 Channel）
2. `callbackFlow` 的 `awaitClose` 有什么用？不调会怎样？
3. 如何将冷流转换为热流？（`shareIn` / `stateIn` 操作符）

**面试官考察点：**
- 对 Flow 机制的深层理解
- 能否在 UI 层和 Data 层选择合适的 Flow 类型
- 对数据流生命周期的管理能力

**易踩坑：**
- ❌ 用冷流做事件分发（事件消费一次就没了，多个 observer 只有一个能收到）
- ❌ 忘记冷流每次 collect 都重新执行，导致数据库查询等副作用重复执行
- ❌ 混淆 `shareIn` 的 `SharingStarted` 策略，导致收集者泄漏

---

### <a id="kotlin-08"></a>8. StateFlow 与 SharedFlow 的区别与使用场景

**标准回答：**

`StateFlow` 和 `SharedFlow` 都是热流，但它们的核心设计目标不同：

**StateFlow**：用于**状态管理**，总是持有最新值，类似 LiveData 的 Flow 替代品。
- 必须有初始值
- 只保留最新值（repla\y = 1）
- 相同值去重（基于 `equals` 判断，可通过 `distinctUntilChanged` 控制）
- 新订阅者立即收到最新值
- `value` 属性可以同步读取当前值

```kotlin
class MyViewModel : ViewModel() {
 private val _uiState = MutableStateFlow(UiState())
 val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
```

**SharedFlow**：用于**事件分发**，可以有多个订阅者，支持配置缓存策略。
- 不需要初始值
- 可配置 `repla\y`（缓存几个历史值给新订阅者）
- 可配置 `extraBufferCapacity`（缓冲区大小）
- `onBufferOverflow` 控制缓冲区满时的策略（SUSPEND/DROP_OLDEST/DROP_LATEST）
- 不去重，每次 `emit` 都会触发收集

```kotlin
// 一次性事件
private val _events = MutableSharedFlow<UiEvent>(
 repla\y = 0,
 extraBufferCapacity = 64,
 onBufferOverflow = BufferOverflow.DROP_OLDEST
)
```

对于IoT 场景，设备状态用 `StateFlow`（温度、连接状态），用户操作事件用 `SharedFlow`（点击配网按钮）。

**高频追问：**
1. `StateFlow` 的值相等判断是怎么实现的？什么情况下去重会失效？
2. `SharedFlow` 的 `repla\y` 和 `extraBufferCapacity` 有什么区别？
3. 如何选择 `shareIn` 和 `stateIn` 的 `SharingStarted` 策略？

**面试官考察点：**
- 能否区分"状态"和"事件"的语义差异
- 对 Flow 系统 API 的熟练度
- 架构中正确选择数据流类型的能力

**易踩坑：**
- ❌ 用 `StateFlow` 做一次性事件（如 Snackbar）——配置变更后事件会重放
- ❌ 用 `SharedFlow` 做 UI 状态——新订阅者收不到当前状态
- ❌ `StateFlow` 默认不去重自定义对象（除非重写 `equals`）

---

### <a id="kotlin-09"></a>9. suspend 函数的原理与挂起机制

**标准回答：**

`suspend` 是 Kotlin 协程的核心关键字。标记为 `suspend` 的函数可以在不阻塞线程的情况下**挂起**执行，等待结果完成后**恢复**。其底层依赖 CPS（Continuation-Passing Style，延续传递风格）转换。

编译时原理：
1. 编译器为每个 `suspend` 函数添加一个 `Continuation<T>` 类型的隐式参数
2. 函数体被编译为状态机（跟 `yield return` 类似）
3. 每个挂起点对应状态机的一个状态（label）
4. 挂起时保存局部变量到 `Continuation` 对象中，释放当前线程
5. 恢复时从 `Continuation` 中恢复状态继续执行

```kotlin
// Kotlin 源码
suspend fun fetchData(): String {
 val result = api.getData() // 挂起点
 return result
}

// 反编译后（简化）
// fun fetchData(continuation: Continuation<String>): Any? {
// when (continuation.label) {
// 0 -> {
// continuation.label = 1
// val result = api.getData(continuation)
// if (result == COROUTINE_SUSPENDED) return COROUTINE_SUSPENDED
// }
// 1 -> { ... 恢复执行 }
// }
// }
```

关键点：挂起不等于阻塞。挂起时线程被释放可以执行其他任务，这是协程高效的根本原因。

**高频追问：**
1. `COROUTINE_SUSPENDED` 是什么？它和返回值是什么关系？
2. 如果在非协程上下文中调用 suspend 函数，会怎样？（编译不通过，需在协程或其他 suspend 中调用）
3. `suspendCoroutine` 和 `suspendCancellableCoroutine` 的区别？

**面试官考察点：**
- 对协程底层机制的深刻理解
- 是否只是"会用"协程，还是真正理解其原理
- 能否编写自定义挂起函数（如回调转协程）

**易踩坑：**
- ❌ 在 Java 代码中调用 suspend 函数时直接传 `null` 作为 Continuation
- ❌ 认为 suspend 是"让线程阻塞等待"——完全错误
- ❌ 将 suspend 函数写在协程外面直接调用

---

### <a id="kotlin-10"></a>10. 协程上下文与调度器（CoroutineContext & Dispatchers）

**标准回答：**

`CoroutineContext` 是协程的配置集合，由一个或多个 `Element` 组成（基于联合类型的设计）。核心 Element 包括：

- **Job**：控制协程生命周期（启动、取消、等待完成）
- **CoroutineDispatcher**：决定协程在哪个线程执行
- **CoroutineName**：调试时的协程名称
- **CoroutineExceptionHandler**：处理未捕获异常

主要调度器：

| 调度器 | 线程池 | 适用场景 |
|--------|--------|----------|
| `Dispatchers.Main` | 主线程 | UI 更新 |
| `Dispatchers.IO` | 弹性线程池（默认最大64） | 网络、文件 IO |
| `Dispatchers.Default` | CPU 核心数线程池 | 计算密集型（排序、加密） |
| `Dispatchers.Unconfined` | 不限制（挂起点后的线程取决于恢复方） | 测试、特殊场景 |

上下文组合与继承：
```kotlin
// 组合多个 Element
val context = Dispatchers.IO + Job() + CoroutineName("MyCoroutine")

// 子协程继承父协程上下文
coroutineScope {
 // 继承 CoroutineName 和 Job，但可以覆盖 Dispatchers
 launch(Dispatchers.Default) { /* CPU 密集型 */ }
}
```

操作符 `+` 的行为：右侧的 Element 会覆盖左侧的同类型 Element。

**高频追问：**
1. `Dispatchers.IO` 和 `Dispatchers.Default` 共享线程池吗？（是的，都基于 `DefaultScheduler`）
2. `Dispatchers.Main` 是如何获取主线程的？（Android 通过 `Handler` 向主线程 Looper 投递）
3. `withContext` 切换调度器的开销大吗？（很小，不创建新协程，只是挂起和恢复）

**面试官考察点：**
- 对线程池和调度器的理解
- 能否根据任务类型选择合适调度器
- 对协程性能的理解

**易踩坑：**
- ❌ 在 `Dispatchers.IO` 中做大量 CPU 计算——应使用 `Dispatchers.Default`
- ❌ 在多处使用 `Dispatchers.Unconfined` 导致不可预测的执行线程
- ❌ 以为 `launch(Dispatchers.IO)` 中的协程子协程也都在 IO 线程——子协程继承上下文

---

### <a id="kotlin-11"></a>11. 结构化并发（Structured Concurrency）

**标准回答：**

结构化并发是 Kotlin 协程的核心理念之一，指的是**协程的生命周期由其所在的协程作用域（CoroutineScope）来控制**。它的核心原则是：

1. **父协程会等待所有子协程完成**：`coroutineScope {}` 会挂起直到内部所有协程执行完毕
2. **子协程异常时会取消其他子协程**：一个子协程失败，兄弟协程被取消
3. **父协程取消时会取消所有子协程**：取消是树形传播的
4. **作用域限制了协程的存活范围**：ViewModelScope 绑定在 ViewModel 上

```kotlin
suspend fun loadData() = coroutineScope {
 val user = async { api.getUser() }
 val config = async { api.getConfig() }
 // 如果 getUser 抛异常，getConfig 也会被取消
 user.await() to config.await()
}
```

Android 中常用的协程作用域：
- `viewModelScope`：ViewModel 清除时自动取消
- `lifecycleScope`：生命周期 DESTROYED 时取消
- `GlobalScope`：不推荐使用（无结构化约束）
- `MainScope`：手动管理，需要调用 `cancel()`

结构化并发的优势：无需手动管理协程引用和取消逻辑，从根本上防止协程泄漏。

**高频追问：**
1. `coroutineScope` 和 `supervisorScope` 的区别？（前者异常传播到兄弟协程，后者隔离异常）
2. `GlobalScope` 为什么是危险的？（没有结构化约束，不知道何时该取消）
3. 如何在 Android 中自定义协程作用域绑定到特定生命周期？

**面试官考察点：**
- 是否写过渡散的协程代码（用 GlobalScope）
- 对协程生命周期管理的理解
- 能否避免协程泄漏

**易踩坑：**
- ❌ 在 `suspend` 函数中使用 `GlobalScope.launch`，完全失去结构化并发
- ❌ 在 Fragment 中调用 `lifecycleScope.launchWhenStarted` 而非 `repeatOnLifecycle`
- ❌ 以为 ViewModel 中启动的协程会随着 Activity 销毁而取消

---

### <a id="kotlin-12"></a>12. 协程异常处理机制

**标准回答：**

协程的异常处理分为两种模式：

**1. launch 模式（异常传播模式）**：
- 异常立即抛出，传播到父协程，父协程取消所有子协程
- 可通过 `CoroutineExceptionHandler` 捕获未处理的异常
- `Handler` 只能设置在根协程（root coroutine）的上下文中

```kotlin
val handler = CoroutineExceptionHandler { _, exception ->
 logError(exception)
}
scope.launch(handler) {
 throw RuntimeException("Error") // 被 Handler 捕获
}
```

**2. async 模式（异常封装模式）**：
- 异常被封装在 `Deferred` 中，调用 `await()` 时才抛出
- 如果不调用 `await()`，异常不会传播

```kotlin
val deferred = scope.async {
 throw RuntimeException("Deferred Error")
}
// 此时异常未抛出
try {
 deferred.await() // 异常在这里抛出
} catch (e: Exception) {
 handleError(e)
}
```

**supervisorScope**：内部的子协程异常不会影响兄弟协程，每个子协程需要自行处理异常。

```kotlin
supervisorScope {
 launch { throw Exception("child1 fails") } // 不取消 child2
 launch { delay(Long.MAX_VALUE) } // child2 继续执行
}
```

**高频追问：**
1. 为什么在 `async` 根协程中设置 `CoroutineExceptionHandler` 无效？（因为异常被封装在 Deferred 中）
2. `supervisorScope` 中 `launch` 的异常如何捕获？（在 `launch` 内部 try-catch，或使用 `CoroutineExceptionHandler`）
3. `cancel()` 和异常是一回事吗？（不是，取消是协作式的，异常是非预期的）

**面试官考察点：**
- 对协程异常传播树的深刻理解
- 能否写出异常安全的协程代码
- 是否在项目中遇到过协程异常问题

**易踩坑：**
- ❌ 在 `async` 调用了 `launch` 的子协程中期望 `CoroutineExceptionHandler` 生效
- ❌ 使用了 `try-catch` 却没注意 `CancellationException` 是特殊的（不应被吞噬）
- ❌ 在 `supervisorScope` 中假设兄弟协程会被异常取消

---

### <a id="kotlin-13"></a>13. 作用域函数：let、run、apply、also、with 的区别

**标准回答：**

Kotlin 提供了五个作用域函数，它们的核心区别在于**接收者引用方式**和**返回值**：

| 函数 | 接收者引用 | 返回值 | 典型场景 |
|------|-----------|--------|----------|
| `let` | `it` | Lambda 结果 | 非空操作、类型转换 |
| `run` | `this` | Lambda 结果 | 对象配置 + 计算结果 |
| `apply` | `this` | 对象本身 | 对象初始化配置 |
| `also` | `it` | 对象本身 | 副作用操作（日志等） |
| `with` | `this` | Lambda 结果 | 对同一对象多次操作 |

```kotlin
// let: 可空链式调用 + 结果转换
val name = user?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown"

// run: 对象配置并返回计算结果
val result = service.run {
 port = 8080
 prepare()
 execute()
}

// apply: 对象初始化（最常用于建造者模式）
val intent = Intent().apply {
 putExtra("key", "value")
 flags = Intent.FLAG_ACTIVITY_NEW_TASK
}

// also: 不影响主流程的副作用
val user = createUser().also { Logger.d("User created: $it") }

// with: 已知非空对象的批量操作
with(binding) {
 textView.text = "Hello"
 button.isEnabled = true
}
```

**高频追问：**
1. `let` 和 `run` 在可空对象链式调用时的区别？（`let` 用 `it`，`run` 用 `this`，`let` 配合 `?.` 更自然）
2. 为什么说 `apply` 最像 Builder 模式？（返回自身，允许链式配置）
3. `with` 和 `run` 作为拓展函数和非拓展函数的区别？（`with` 是普通函数，`run` 是拓展函数）

**面试官考察点：**
- 是否理解五个函数的语义差异
- 项目中能否规范使用而非随意混用
- 对"链式调用"这种编码风格的掌握

**易踩坑：**
- ❌ 在 `apply` 中做太多事情，超出配置语义范围
- ❌ `also` 中修改对象状态导致副作用隐藏
- ❌ 嵌套使用作用域函数，出现嵌套 `it` 导致引用混乱

---

### <a id="kotlin-14"></a>14. 委托属性（Delegated Properties）原理

**标准回答：**

委托属性是 Kotlin 中通过 `by` 关键字将属性的 getter/setter 委托给另一个对象的机制。编译器会为每个委托属性生成一个 `KProperty` 对象作为标识。

```kotlin
class Example {
 var p: String by Delegate()
}

class Delegate {
 operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
 return "$thisRef: ${property.name}"
 }
 operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
 println("$value has been assigned to '${property.name}' in $thisRef")
 }
}
```

编译后生成：
```java
// 简化后的 Java 代码
class Example {
 private final Delegate p$delegate = new Delegate();
 
 public String getP() {
 return p$delegate.getValue(this, $$delegatedProperties[0]);
 }
}
```

标准库提供的委托：
- **`lazy`**：延迟初始化（线程安全可选）
- **`observable`**：属性变化观察（旧值、新值）
- **`vetoable`**：赋值前校验（返回 false 拒绝赋值）
- **`notNull`**：非空委托（必须在访问前赋值）
- **`map`**：将属性值映射到 Map 中

```kotlin
// 自定义委托：SharedPreferences 委托
class PreferenceDelegate<T>(private val key: String, private val default: T) {
 operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
 return prefs.get(key, default) ?: default
 }
}
```

**高频追问：**
1. `lazy` 的三种线程安全模式（SYNCHRONIZED/PUBLICATION/NONE）分别是什么？
2. 自定义委托属性能实现"可观察的"属性吗？（可以，参考 `observable`）
3. 委托属性的底层是用什么方式存储委托对象？（编译器自动生成的成员变量）

**面试官考察点：**
- 对 Kotlin 高级特性的掌握程度
- 是否有封装通用委托的实际经验
- 对 `by` 语法糖的编译原理理解

**易踩坑：**
- ❌ 忘了自定义委托需要 `operator` 修饰 `getValue`/`setValue`
- ❌ 委托给 `lazy` 的属性是 `val`（不可变），试图重新赋值
- ❌ 委托属性在跨线程访问时没有加锁导致并发问题

---

### <a id="kotlin-15"></a>15. 延迟初始化：lateinit 与 by lazy

**标准回答：**

`lateinit` 和 `by lazy` 是 Kotlin 实现延迟初始化的两种机制，它们的核心区别在于**初始化时机和使用限制**：

**lateinit**（晚初始化）：
- 仅用于 `var` 可变属性
- 仅用于非空类型且不能是原始类型（Int、Boolean 等）
- 初始化时机由开发者控制（通常通过外部注入）
- 访问未初始化的属性会抛出 `UninitializedPropertyAccessException`
- 可使用 `::property.isInitialized` 检查是否已初始化
- 线程不安全，需要自行处理并发

```kotlin
lateinit var repository: UserRepository

fun onCreate() {
 repository = UserRepositoryImpl()
}
```

**by lazy**（懒初始化）：
- 用于 `val` 只读属性（不可变）
- 首次访问时自动执行初始化 Lambda
- 可以用于任何类型
- 默认线程安全（`LazyThreadSafetyMode.SYNCHRONIZED`）
- 初始化后值不可修改

```kotlin
val repository by lazy {
 UserRepositoryImpl()
}
```

**高频追问：**
1. `lateinit` 的反编译原理是什么？（编译为可空的 Java 类型，访问时插入非空断言检查）
2. `by lazy` 如何实现线程安全？可否取消？（内部使用 `synchronized`；通过 `lazy(LazyThreadSafetyMode.NONE)`）
3. Android 中哪些场景该用 `lateinit`，哪些该用 `lazy`？（注入依赖用 `lateinit`，耗时初始化用 `lazy`）

**面试官考察点：**
- 对两种机制的底层实现的了解
- 能否在正确场景做出合理选择
- 对线程安全的意识

**易踩坑：**
- ❌ 对原始类型使用 `lateinit`（编译不通过）
- ❌ 使用 `isInitialized` 前忘记 `::` 调用方式
- ❌ 在 Activity 中使用 `by lazy` 做 UI 引用（Activity 重建后引用失效）

---

### <a id="kotlin-16"></a>16. Kotlin 可空类型与空安全机制

**标准回答：**

Kotlin 空安全的核心设计是将可空类型和不可空类型在类型系统层面分离，通过编译器在编译期消除 `NullPointerException` 的风险。

```kotlin
var nonNull: String = "Hello" // 不可为 null
var nullable: String? = null // 可为 null

// 非空断言操作符：!! 
val length = nullable!!.length // 如果 nullable 为 null，抛 NPE

// 安全调用操作符：?.
val length = nullable?.length // 如果 nullable 为 null，返回 null

// Elvis 操作符：?:
val length = nullable?.length ?: 0 // 如果左边为 null，使用默认值

// 安全类型转换：as?
val obj: String? = any as? String // 转换失败返回 null（不抛异常）

// 平台类型处理（来自 Java）：
val javaString: String! = javaMethod() // 无法判断是否为空
```

关键机制：
1. **智能转换**：`if (str != null)` 块内自动转换为非空类型
2. **平台类型 `String!`**：来自 Java 的类型，编译器无法确定是否为空
3. **let + ?.**：`str?.let { it.length }` 只在非空时执行
4. **非空断言 `!!` 的危险**：本质是用 NPE 换取了编译安全，不应滥用

Kotlin 的空安全机制配合 `sealed class Result` 和 `?.let{}` 等，可以在数据层和 UI 层之间安全地传递可能为空的数据。

**高频追问：**
1. 平台类型 `String!` 是什么？使用时应该注意什么？（来自 Java 代码，编译器不强制检查）
2. `?.let`、`?.run`、`?:run` 的空安全模式有哪些？
3. 如何安全地处理嵌套可空对象的属性访问？（`user?.address?.city ?: "Unknown"`）

**面试官考察点：**
- 对 Kotlin 类型系统的基础理解
- 项目代码中是否有空安全的最佳实践
- 能否识别和避免 `!!` 滥用

**易踩坑：**
- ❌ 直接在 Java 返回的平台上使用 `.` 访问（应显式声明为可空或非空）
- ❌ 过度使用 `!!` 作为"快速修复"手段，留下 NPE 隐患
- ❌ 混淆 `?.let` 和 `.let` ——后者在可空对象上不生效

---

### <a id="kotlin-17"></a>17. Kotlin 类型安全与智能转换（Smart Cast）

**标准回答：**

Kotlin 编译器能够根据条件检查自动进行类型转换，这被称为智能转换（Smart Cast）。编译器通过分析控制流（如 `if`、`when`、`&&`、`||`）中的类型检查，自动推断变量的更精确类型。

```kotlin
fun process(obj: Any) {
 if (obj is String) {
 // 自动转换为 String
 println(obj.length) // 无需手动强转
 }
 when (obj) {
 is Int -> println(obj + 1)
 is String -> println(obj.uppercase())
 is List<*> -> println(obj.size)
 }
}

// 可空类型的智能转换
val str: String? = getString()
if (str != null) {
 println(str.length) // 自动转换为 String（非空）
}

// 与 && 和 || 配合
if (obj is String && obj.length > 0) {
 // && 后 obj 已被转换
}
```

智能转换的限制（不会自动转换的情况）：
1. **可变属性（var）**：如果在检查和使用之间可能被修改，不会智能转换
2. **非 `val` lambda 捕获**：Lambda 中修改的变量不会被智能转换
3. **自定义 getter**：`open` 属性或自定义 getter 的 `val` 也不支持

```kotlin
var obj: Any = "Hello"
if (obj is String) {
 // 编译器警告：Smart cast to 'String' is impossible
 // 因为 obj 是 var，可能在多线程环境中被修改
 // println(obj.length) // 编译错误
}
```

**高频追问：**
1. 什么情况下智能转换会失效？怎么解决？（用 `as` 手动转换或用局部不可变变量）
2. 智能转换和 Java 的 `instanceof` 有什么本质不同？（编译期保证 vs 运行时检查）
3. Kotlin 2.0 中有关于智能转换的改进吗？（改进了闭包中的智能转换）

**面试官考察点：**
- 对 Kotlin 类型推断机制的理解
- 是否了解智能转换的边界和限制
- 编码时的类型安全意识

**易踩坑：**
- ❌ 对 `var` 变量做了类型检查就以为可以安全使用（智能转换失效）
- ❌ 在 Lambda 内部依赖外部变量的智能转换（编译不通过）
- ❌ 多态场景下混淆了声明类型和运行时类型

---

### <a id="kotlin-18"></a>18. 泛型型变：in 与 out

**标准回答：**

Kotlin 的泛型型变通过 `out`（协变）和 `in`（逆变）来定义子类型关系：

**`out`（协变）**：`Producer<out T>` 表示生产者，只将 T 作为**输出（返回值）**使用。如果 `A` 是 `B` 的子类，则 `Producer<A>` 是 `Producer<B>` 的子类。

```kotlin
interface Source<out T> {
 fun nextT(): T // 作为输出（返回值）
 // fun consume(t: T) // 编译错误：不能作为输入参数
}

fun demo(src: Source<String>) {
 val src2: Source<Any> = src // 协变允许 String 子类型向上转型
}
```

**`in`（逆变）**：`Consumer<in T>` 表示消费者，只将 T 作为**输入（参数）**使用。如果 `A` 是 `B` 的子类，则 `Consumer<B>` 是 `Consumer<A>` 的子类（方向相反）。

```kotlin
interface Comparable<in T> {
 fun compareTo(other: T): Int // 作为输入
}

fun demo(comp: Comparable<Number>) {
 val comp2: Comparable<Double> = comp // 逆变允许反向转换
}
```

**`where` 子句**用于声明多个上界：
```kotlin
fun <T> copyWhenGreater(list: List<T>, threshold: T): List<String>
 where T : CharSequence, T : Comparable<T> {
 return list.filter { it > threshold }.map { it.toString() }
}
```

在实际 Android 开发中，`List<out E>`、`Comparator<in T>` 都是典型的型变应用。

**高频追问：**
1. PECS 原则在 Kotlin 中如何体现？（Producer-Extends 对应 `out`，Consumer-Super 对应 `in`）
2. 声明处型变与使用处型变的区别？（`out`/`in` 在类声明处 vs 类型投影 `*`）
3. 为什么 `MutableList<E>` 没有使用 `out` 或 `in`？（既读又写，是不变类型）

**面试官考察点：**
- 对泛型理论的深入理解
- 能否区分协变和逆变的应用场景
- 对类型安全的认知

**易踩坑：**
- ❌ 在 `in` 位置使用了 `out` 标注的类型（编译报错）
- ❌ 不理解星投影 `*` 的含义（`out Any?` 的缩写）
- ❌ Java 的 `? extends T` 和 `? super T` 与 Kotlin 的 `out`/`in` 概念混淆

---

### <a id="kotlin-19"></a>19. object 关键字的三种用法

**标准回答：**

Kotlin 中 `object` 关键字有三种截然不同的用途：

**1. 单例对象（Object Declaration）**：
```kotlin
object AppConfig {
 var baseUrl: String = "https://api.example.com"
 fun getConfig() = this
}
// 线程安全的懒加载单例，首次访问时初始化
AppConfig.baseUrl = "https://new.example.com"
```
编译后生成：持有 `INSTANCE` 静态字段 + 静态初始化块。

**2. 伴生对象（Companion Object）**：
```kotlin
class MyClass {
 companion object Factory {
 fun create(): MyClass = MyClass()
 const val TAG = "MyClass" // 编译期常量
 }
}
// 调用方式
MyClass.create()
MyClass.TAG
```
编译后生成静态内部类 + 静态字段（持有实例引用）。

**3. 匿名对象（Object Expression）**：
```kotlin
// 创建匿名内部类
val listener = object : OnClickListener {
 override fun onClick(v: View) {
 // 处理点击
 }
}

// 无父类的匿名对象（可返回给外部）
val point = object {
 var x = 0
 var y = 0
}
```

三种用法的本质区别：
| 用法 | 类名 | 实例数 | 延迟初始化 | 能否访问外部作用域 |
|------|------|--------|-----------|-------------------|
| object | 有（AppConfig） | 1 | 是 | 否 |
| companion object | 有（Factory） | 1 | 否（类加载时） | 否 |
| object expression | 无（匿名） | N | N/A | 是 |

**高频追问：**
1. 单例对象的线程安全是如何保证的？（类加载器机制 + `synchronized`）
2. 伴生对象内部能访问外部类的实例成员吗？（不能，相当于 Java 的静态内部类）
3. 如何给伴生对象起名？名称有什么作用？（`companion object Factory`，名称用于扩展函数等）

**面试官考察点：**
- 对 Kotlin 类体系的全面理解
- 是否能正确使用单例和匿名对象
- 对编译后产物的了解

**易踩坑：**
- ❌ 混淆 `object` 和 `data object`（Kotlin 1.9+ 新特性）
- ❌ 在匿名对象中访问外部作用域的非 final 变量（同 Java 的匿名内部类限制）
- ❌ 认为伴生对象是类加载时才创建（实际上是首次访问时）

---

### <a id="kotlin-20"></a>20. 伴生对象（companion object）与静态成员

**标准回答：**

伴生对象是 Kotlin 中替代 Java `static` 关键字的机制。它是在类内部声明的 `object`，通过 `companion` 关键字标记，使得其成员可以通过类名直接访问。

```kotlin
class User private constructor(val name: String) {
 companion object {
 private const val DEFAULT_NAME = "Guest"
 
 fun createDefault(): User = User(DEFAULT_NAME)
 
 // @JvmStatic 暴露给 Java 调用
 @JvmStatic
 fun create(name: String): User = User(name)
 
 // @JvmField 将属性暴露为静态字段
 @JvmField
 val TAG = "User"
 }
}

// Kotlin 调用
val user = User.createDefault()
val user2 = User.create("Alice")

// Java 调用
User user = User.Companion.create("Alice"); // 不加注解时
User user2 = User.create("Alice"); // 加 @JvmStatic 后
String tag = User.TAG; // 加 @JvmField 后
```

伴生对象的关键特性：
1. 每个类只能有一个伴生对象
2. 伴生对象可以实现接口
3. 可为伴生对象定义扩展函数
4. 伴生对象本身是单例，但其宿主类可以有多个实例
5. 使用 `@JvmStatic` 和 `@JvmField` 控制 Java 互操作性

```kotlin
// 伴生对象实现接口
interface Factory<T> { fun create(): T }

class MyClass {
 companion object : Factory<MyClass> {
 override fun create() = MyClass()
 }
}
```

**高频追问：**
1. 伴生对象中的 `const` 和普通 `val` 区别？（`const` 是编译期常量，可内联；`val` 是运行时常量）
2. 伴生对象和顶层属性/函数哪个更好？（取决于语义是否与类相关）
3. 为什么说伴生对象比 Java 的 static 更好？（可以继承/实现接口，是真正的对象）

**面试官考察点：**
- 对 Kotlin 替代 Java static 机制的理解
- JVM 互操作性的掌握程度
- 对 const 编译期常量的认知

**易踩坑：**
- ❌ 认为伴生对象 == Java static（伴生对象是真正的对象，可以有扩展、继承）
- ❌ 在 Java 中调用 Kotlin 伴生对象时不加 `@JvmStatic` 导致调用路径变复杂
- ❌ 用 `const val` 在 `data class` 的伴生对象中定义常量（常量不是 `componentN` 的一部分）

---

### <a id="kotlin-21"></a>21. 解构声明（Destructuring Declarations）

**标准回答：**

解构声明允许将一个对象的属性一次性分解到多个变量中。其底层依赖 `componentN()` 方法（N 从 1 开始）。

```kotlin
// data class 自动生成 componentN()
data class Point(val x: Int, val y: Int)

val (x, y) = Point(10, 20) // 解构声明
// 等价于：
// val x = point.component1()
// val y = point.component2()

// 实际应用示例
for ((index, element) in list.withIndex()) { /* ... */ }
val (key, value) = map.entry
val (name, age) = user

// 自定义 componentN
class Color(val r: Int, val g: Int, val b: Int, val a: Int) {
 operator fun component1() = r
 operator fun component2() = g
 operator fun component3() = b
 operator fun component4() = a
}
val (red, green, blue, alpha) = Color(255, 0, 0, 100)
```

Lambda 参数中的解构（Kotlin 1.1+）：
```kotlin
map.mapValues { (key, value) -> "$key -> $value" }
map.mapValues { (_, value) -> "$value" } // 用 _ 忽略不需要的字段
```

实际开发中的应用：
- `Pair` 和 `Triple` 的返回值解构：`val (result, elapsed) = measureTime { ... }`
- 数据类解构（需注意字段顺序匹配）
- `Map` 遍历的 `(key, value)` 模式

**高频追问：**
1. 如果只解构部分字段怎么办？（用 `_` 占位：`val (name, _) = user`）
2. 解构声明在函数返回多值时的最佳实践？（用 `data class` 或 `Pair`/`Triple`）
3. `componentN` 的性能开销？（内联函数的开销可忽略）

**面试官考察点：**
- 对 Kotlin 语法糖的熟悉度
- 编码风格和可读性意识
- 能否在合适场景使用解构

**易踩坑：**
- ❌ 解构时字段顺序错误导致变量赋值混乱（`data class` 的顺序即声明顺序）
- ❌ 用 `Pair`/`Triple` 返回多值但语义不清晰（不如用命名 data class）
- ❌ 忘记了 `_` 可以占位，写了不需要的变量

---

### <a id="kotlin-22"></a>22. 内联类（inline class / value class）

**标准回答：**

内联类（Kotlin 1.5 后改称 value class）用 `@JvmInline value class` 声明，其核心特征是**在运行时尽量使用包装的值类型，而不是包装类对象**，从而避免堆分配开销。

```kotlin
@JvmInline
value class UserId(val id: String)

@JvmInline
value class Password(val value: String)

fun login(userId: UserId, password: Password) {
 // 类型安全：UserId 和 Password 不能混用
}

// 使用时
login(UserId("123"), Password("secret"))
// 编译后近似于 login("123", "secret") — 无装箱开销
```

内联类的要求：
1. 主构造函数必须有且仅有一个参数
2. 必须用 `val` 声明（只读）
3. 不能有 `init` 块
4. 不能有 backing field 的属性
5. 可以实现接口，但不能继承其他类

```kotlin
@JvmInline
value class Meter(val value: Double) : Comparable<Meter> {
 override fun compareTo(other: Meter): Int = value.compareTo(other.value)
}
```

装箱场景（仍会创建对象）：
- 作为可空类型使用（`Meter?`）
- 作为泛型类型参数（`List<Meter>`）
- 作为接口类型使用（`Comparable<Meter>` 接口引用）

实际应用：类型安全包装（避免基本类型混淆）、单位系统、ID 封装等。

**高频追问：**
1. 内联类和 type alias 的区别？（type alias 只是别名无类型安全，value class 是强类型）
2. 内联类的 boxing 开销在什么情况下产生？（可空、泛型、接口类型）
3. 内联类和非内联类的内存分配差异？（内联类优先使用栈，普通类在堆上）

**面试官考察点：**
- 对 Kotlin 性能优化特性的关注
- 对类型安全设计的理解
- 能否区分内联类和 typealias

**易踩坑：**
- ❌ 期望内联类在所有场景都不装箱（泛型和可空场景会装箱）
- ❌ 把 typealias 当 value class 用（没有类型安全检查）
- ❌ 在内联类中定义复杂的属性和方法（语义上不符合轻量类型的目标）

---

### <a id="kotlin-23"></a>23. 协程取消机制与取消协作

**标准回答：**

协程取消是**协作式**的，即协程需要主动响应取消信号，而不是被强制终止。取消流程如下：

1. 调用 `job.cancel()` 触发取消
2. 协程在**挂起点**检查取消状态
3. 如果发现取消，抛出 `CancellationException`
4. 异常向上传播，最终协程结束

```kotlin
val job = scope.launch {
 repeat(1000) { i ->
 println("Progress: $i")
 delay(500) // 挂起点 — 在此检查取消
 // 或手动检查
 if (isActive.not()) throw CancellationException()
 }
}
job.cancel()
```

关键 API：
- **`isActive`**：检查协程是否仍活跃
- **`ensureActive()`**：如果不活跃则抛 `CancellationException`
- **`yield()`**：挂起当前协程，让处理器检查取消状态

非协作代码的问题：
```kotlin
// 不会被取消的计算——没有挂起点
launch {
 var i = 0
 while (true) {
 i++ // 纯计算，无挂起点，永远不会取消
 }
}

// 修复：添加协作点
launch {
 var i = 0
 while (isActive) { // 手动检查
 i++
 }
}
```

资源清理：使用 `finally` 块或 `use` 函数确保资源释放。

**高频追问：**
1. `CancellationException` 为什么特殊？（不会被 `CoroutineExceptionHandler` 捕获，不会导致父协程崩溃）
2. `finally` 中的挂起函数还能执行吗？（不能，需要用 `withContext(NonCancellable)` 包裹）
3. `cancelAndJoin()` 和 `cancel()` 的区别？（前者挂起等待协程完全结束）

**面试官考察点：**
- 是否理解取消是协作机制而非强制终止
- 能否编写可取消的协程代码
- 对资源清理（close/close）的考虑

**易踩坑：**
- ❌ 以为 `cancel()` 会立即终止计算密集型协程
- ❌ 在 `finally` 中直接调用挂起函数（需要用 `NonCancellable`）
- ❌ catch `CancellationException` 后不重新抛出（破坏了取消机制）

---

### <a id="kotlin-24"></a>24. Channel 的使用场景与类型

**标准回答：**

Channel 是协程间的**通信管道**，用于在不同协程之间传递数据流。它类似于 `BlockingQueue`，但是挂起而非阻塞。

Channel 类型（按容量和行为分类）：

| 类型 | 容量 | 行为 |
|------|------|------|
| `Channel.RENDEZVOUS` | 0（默认） | 发送方挂起直到接收方就绪 |
| `Channel.UNLIMITED` | 无限 | 发送永不挂起（无限缓冲） |
| `Channel.CONFLATED` | 1 | 只保留最新值，新值覆盖旧值 |
| `Channel.BUFFERED` | 64（默认） | 缓冲区满后发送方挂起 |
| `Channel(capacity)` | 自定义 | 缓冲区满后发送方挂起 |

```kotlin
// RENDEZVOUS：生产者-消费者严格同步
val channel = Channel<Int>()
launch { channel.send(1) } // 挂起等待接收
launch { println(channel.receive()) }

// CONFLATED：状态流，只关心最新值
val conflated = Channel<Int>(Channel.CONFLATED)
// 适合处理 UI 刷新事件

// 管道模式
fun CoroutineScope.producer() = produce<Int> {
 for (i in 1..5) send(i)
}
fun CoroutineScope.consumer(channel: ReceiveChannel<Int>) = launch {
 for (value in channel) println(value)
}
```

`onBufferOverflow`：当缓冲区满时的策略（`SUSPEND`/`DROP_OLDEST`/`DROP_LATEST`）。

现代 Android 开发中，Channel 逐渐被 `SharedFlow` 替代，但在生产者-消费者模式、背压处理等场景仍有优势。

**高频追问：**
1. Channel 和 Flow 的核心区别是什么？（Channel 是"热"通信管道，Flow 是"冷"数据流）
2. `produce` 构建器和 `actor` 的区别？（produce 返回 ReceiveChannel，actor 返回 SendChannel）
3. `broadcast` Channel 是什么？为什么被废弃？（多订阅者 Channel，被 SharedFlow 取代）

**面试官考察点：**
- 是否理解 Channel 和 Flow 的适用边界
- 对协程通信机制的理解深度
- 是否了解背压处理

**易踩坑：**
- ❌ Channel 没有 close 导致接收方 `for` 循环永远等待
- ❌ RENDEZVOUS Channel 导致死锁（发送和接收都在等待）
- ❌ 混用 Channel 和 LiveData/StateFlow 导致数据不一致

---

### <a id="kotlin-25"></a>25. supervisorScope 与 coroutineScope 的区别

**标准回答：**

`supervisorScope` 和 `coroutineScope` 都是作用域构建器，用于创建子作用域，但异常传播行为有本质区别：

**coroutineScope**（异常传播模式）：
- 如果任意一个子协程失败，则取消所有子协程
- 异常向上传播到父作用域
- 适用于"一个失败则全部失败"的原子操作场景

```kotlin
suspend fun loadData() = coroutineScope {
 val user = async { api.getUser() }
 val config = async { api.getConfig() }
 // getUser 失败 → config 被取消 → 整个 loadData 失败
 user.await() to config.await()
}
```

**supervisorScope**（异常隔离模式）：
- 子协程失败**不会**取消兄弟协程
- 子协程异常**不会**向上传播（子协程自己处理）
- 适用于"每个子协程的失败独立处理"的场景

```kotlin
suspend fun loadUI() = supervisorScope {
 launch { loadUserProfile() } // 此协程失败
 launch { loadNotifications() } // 此协程不受影响，继续执行
}
```

核心区别总结：
| 特性 | coroutineScope | supervisorScope |
|------|----------------|-----------------|
| 异常传播 | 兄弟间传播 | 隔离 |
| 失败影响 | 全失败 | 独立失败 |
| 典型场景 | 数据并发获取 | UI 组件加载 |
| 对应 Job 类型 | Job | SupervisorJob |

**高频追问：**
1. `supervisorScope` 的内部实现与 `SupervisorJob` 是什么关系？（`supervisorScope` 内部创建 `SupervisorJob`）
2. `ViewModel` 的 `viewModelScope` 用的是哪种？（内部使用了 `SupervisorJob`）
3. 为什么 `lifecycleScope` 也需要 `supervisorScope` 的行为？（每个 UI 组件的加载不应互相阻塞）

**面试官考察点：**
- 深入理解协程的异常传播机制
- 能否根据业务需求选择正确的作用域
- 对 Jetpack 中协程作用域的认知

**易踩坑：**
- ❌ 需要原子性操作时使用了 `supervisorScope`，导致部分操作成功、部分失败
- ❌ 在 UI 场景使用 `coroutineScope`，一个数据加载失败影响了其他 UI 更新
- ❌ 认为 `supervisorScope` 内部不需要 try-catch（异常仍需要子协程自己处理）

---

### <a id="kotlin-26"></a>26. Sequence 与集合的区别

**标准回答：**

`Sequence` 是 Kotlin 中的惰性计算序列，与集合（`List`/`Set` 等）的核心区别在于**操作的执行方式**：

**集合的及早求值**：每个操作符都会立即遍历并创建中间集合
```kotlin
listOf(1, 2, 3, 4)
 .map { it * it } // 1. 立即遍历，生成 [1,4,9,16]
 .filter { it > 5 } // 2. 再遍历，生成 [9,16]
 .first() // 3. 取第一个元素 = 9
// 总遍历次数：3次，创建了2个中间集合
```

**Sequence 的惰性求值**：构建操作链但不执行，终端操作时才逐个元素执行整条链
```kotlin
sequenceOf(1, 2, 3, 4)
 .map { it * it } // 1. 只记录操作（暂不执行）
 .filter { it > 5 } // 2. 只记录操作（暂不执行）
 .first() // 3. 终端操作：逐个元素执行
// 执行过程：1->1(不满足>5)->2->4(不满足>5)->3->9(满足，返回)
// 总遍历次数：1次，0个中间集合，只需处理3个元素就停止
```

Sequence 的优势场景：
1. **大数据集**（数千以上元素）：避免中间集合的内存开销
2. **提前终止**（`first`、`take`）：避免不必要的计算
3. **无限序列**：`generateSequence` 创建无限序列

```kotlin
// 无限自然数序列
val natural = generateSequence(1) { it + 1 }
val first10Even = natural.filter { it % 2 == 0 }.take(10).toList()
```

**高频追问：**
1. Sequence 在什么场景下反而比集合更慢？（小数据集的创建开销大于中间集合分配开销）
2. `asSequence()` 和直接创建 Sequence 有什么区别？（`asSequence` 包装现有集合）
3. Sequence 的并行操作？（没有内置支持，需要用 `asFlow` 转换后使用协程）

**面试官考察点：**
- 对函数式编程操作性能的理解
- 是否关注集合操作的性能影响
- 能否合理选择 Sequence 和集合

**易踩坑：**
- ❌ 所有场景都用 Sequence 替代集合（小数据集下开销更大）
- ❌ 对 Sequence 进行了两次终端操作（每次终端操作都会重新执行整条链）
- ❌ 创建 Sequence 时的副作用依赖（惰性执行时机不可控）

---

### <a id="kotlin-27"></a>27. reified 关键字的作用

**标准回答：**

`reified` 关键字只能与 `inline` 函数配合使用，它的作用是**在运行时保留泛型类型信息**，使得可以在函数体内访问类型实参（Java 泛型擦除导致无法直接获取类型信息）。

```kotlin
// 没有 reified：编译错误
fun <T> checkType(obj: Any) {
 if (obj is T) { } // 错误：Cannot check for instance of erased type T
}

// 使用 reified：编译通过
inline fun <reified T> checkType(obj: Any): Boolean {
 return obj is T // 可行！因为内联后 T 被替换为具体类型
}

// 实际应用
val result: String = checkType<String>("Hello") // true
```

常见使用场景：

**1. 简化泛型 class 引用**：
```kotlin
// 传统方式（繁琐）
val listType = object : TypeToken<List<String>>() {}.type

// reified 方式（简洁）
inline fun <reified T> typeOf(): Type = object : TypeToken<T>() {}.type
```

**2. 类型安全的解析函数**：
```kotlin
inline fun <reified T> Intent.getParcelableExtraCompat(key: String): T? {
 return when (T::class) {
 Parcelable::class -> getParcelableExtra<Parcelable>(key) as? T
 else -> null
 }
}
```

**3. 简化 startActivity**：
```kotlin
inline fun <reified T : Activity> Context.startActivity() {
 startActivity(Intent(this, T::class.java))
}
// 使用：startActivity<DetailActivity>()
```

**高频追问：**
1. 为什么 `reified` 必须搭配 `inline`？（内联后类型 T 被替换为具体类型 class，不存在擦除）
2. `T::class.java` 和 `T::class` 的区别？（`.java` 获取 Java Class 对象，`.class` 获取 KClass）
3. `reified` 能用在属性上吗？（可以，`inline val` + `reified`，Kotlin 1.1+）

**面试官考察点：**
- 对 JVM 泛型擦除的理解
- 对 `inline` 和 `reified` 的关联认知
- 能否写出简洁优雅的泛型代码

**易踩坑：**
- ❌ 不加 `inline` 单独使用 `reified`（编译报错）
- ❌ 在非内联的函数中使用带 `reified` 的泛型参数
- ❌ `reified` 不能用于 `Class<T>` 类型的参数转换

---

### <a id="kotlin-28"></a>28. infix 函数的使用与限制

**标准回答：**

`infix` 关键字允许将函数作为中缀操作符使用（省略点和括号），提升代码可读性。它有严格的使用限制：

```kotlin
// 定义
infix fun Int.add(other: Int): Int = this + other

// 调用
val result = 3 add 5 // 等同于 3.add(5)

// 标准库中的 infix 函数
val map = mapOf(1 to "one", 2 to "two") // to
val pair = "key" to "value" // to
for (i in 1 until 10) { } // until
for (i in 10 downTo 1) { } // downTo
5 shl 2 // shl
```

限制条件（必须全部满足）：
1. **必须是成员函数或扩展函数**
2. **有且仅有一个参数**
3. **参数不能是可变参数**（不能用 `vararg`）
4. **参数不能有默认值**
5. **调用优先级低于算术运算符**（`1 + 2 add 3` 会先算 `1 + 2`）

```kotlin
// 自定义 DSL 风格的 infix 函数
class Assertion<T>(val actual: T) {
 infix fun equalsTo(expected: T) {
 if (actual != expected) throw AssertionError()
 }
}
// 使用：1 asset equalsTo 1
```

实际项目中的应用：
- 测试断言 DSL（`result shouldBe expected`）
- Route 定义（`"home" mapTo HomeScreen::class`）
- 时间单位（`5 seconds`、`10 minutes`）

**高频追问：**
1. `infix` 函数的优先级是怎样的？（低于算术和类型转换，高于布尔运算）
2. 可以用 `infix` 重载 `to` 函数吗？（`to` 已经是扩展函数，可以自定义类似功能）
3. `infix` 和操作符重载（`operator`）的区别？

**面试官考察点：**
- 对 Kotlin DSL 风格的掌握
- 是否理解操作符优先级
- 对可读性和语法的平衡

**易踩坑：**
- ❌ 把 `infix` 定义成顶层函数（必须是成员或扩展函数）
- ❌ 两个 `infix` 连续调用时忘记优先级问题（需加括号）
- ❌ 过度使用 `infix` 使代码变得晦涩难懂

---

### <a id="kotlin-29"></a>29. by lazy 与 lateinit 的底层实现对比

**标准回答：**

这两种延迟初始化机制虽然表面相似，但底层实现完全不同：

**by lazy 的底层实现**：

`lazy` 函数返回 `Lazy<T>` 接口的实现类（默认 `SynchronizedLazyImpl`）：
```kotlin
// 简化后的源码实现
private class SynchronizedLazyImpl<out T>(
 initializer: () -> T, lock: Any? = null
) : Lazy<T>, Serializable {
 private var initializer: (() -> T)? = initializer
 @Volatile private var _value: Any? = UNINITIALIZED_VALUE
 private val lock = lock ?: this

 override val value: T
 get() {
 val _v1 = _value
 if (_v1 !== UNINITIALIZED_VALUE) {
 return _v1 as T // 已初始化，直接返回
 }
 return synchronized(lock) {
 val _v2 = _value
 if (_v2 !== UNINITIALIZED_VALUE) {
 _v2 as T // 双重检查
 } else {
 val typedValue = initializer!!()
 _value = typedValue
 initializer = null // 释放 Lambda 引用
 typedValue
 }
 }
 }
}
```
- 编译后生成私有属性持有 `Lazy` 实例，getter 委托给 `lazy.value`
- 线程安全通过 `synchronized` + `@Volatile` + 双重检查锁定（DCL）实现
- 初始化后 `initializer` 被置 null，释放闭包引用

**lateinit 的底层实现**：
- 编译后将属性类型改为对应的可空类型
- 访问时自动插入非空断言 `!!` 检查
- 实际上就是用一个标记位记录是否已赋值

```java
// Kotlin: lateinit var name: String
// 反编译 Java：
private String name;
public String getName() {
 if (name == null) throw UninitializedPropertyAccessException(...);
 return name;
}
```

**高频追问：**
1. `lazy(LazyThreadSafetyMode.NONE)` 对比 `LazyThreadSafetyMode.PUBLICATION` 在实现上的区别？
2. Android 中 `viewBinding` 应该用 `lazy` 还是 `lateinit`？（lateinit，因为 Fragment/Activity 重建后要重新赋值）
3. 为什么 `lateinit` 不设计为线程安全？（性能考虑，用它就是为了避免额外开销）

**面试官考察点：**
- 是否有阅读源码的习惯
- 对并发安全和性能的权衡理解
- 对 Android 生命周期场景的深入理解

**易踩坑：**
- ❌ 在 Fragment 中使用 `by lazy` 持有 View 引用，导致重建后引用指向旧的 View
- ❌ 对 `lateinit var` 反复赋值（可以重新赋值，但无法复位为"未初始化"）
- ❌ 忘记 `by lazy` 默认是线程安全的（有锁开销）

---

### <a id="kotlin-30"></a>30. Kotlin 协程在 Android 中的最佳实践

**标准回答：**

在 Android 项目中使用协程，应遵循以下最佳实践：

**1. 选择合适的作用域**：
```kotlin
// ViewModel 中使用 viewModelScope
class MyViewModel : ViewModel() {
 fun loadData() {
 viewModelScope.launch {
 val data = repository.getData()
 _uiState.value = data
 }
 }
}

// UI 层中使用 lifecycleScope + repeatOnLifecycle
class MyFragment : Fragment() {
 init {
 lifecycleScope.launch {
 repeatOnLifecycle(Lifecycle.State.STARTED) {
 viewModel.uiState.collect { state ->
 updateUI(state)
 }
 }
 }
 }
}
```

**2. 正确使用调度器**：
```kotlin
// suspend 函数内部切换线程
suspend fun fetchData(): Data = withContext(Dispatchers.IO) {
 api.getData()
}

// ViewModel 中启动，在 Main 线程更新 UI
viewModelScope.launch {
 val data = withContext(Dispatchers.IO) { heavyComputation() }
 _uiState.value = data // 自动在主线程（viewModelScope 默认 Main）
}
```

**3. 异常处理的分层策略**：
```kotlin
// Data 层：封装为 Result
suspend fun getData(): Result<Data> = runCatching {
 api.getData()
}

// ViewModel 层：处理展示
viewModelScope.launch {
 repository.getData()
 .onSuccess { _uiState.value = UiState.Success(it) }
 .onFailure { _uiState.value = UiState.Error(it) }
}
```

**4. 测试友好**：注入 Dispatcher，使用 `StandardTestDispatcher` 进行测试

**5. 避免 GlobalScope**：始终使用有边界的作用域

**高频追问：**
1. `repeatOnLifecycle` vs `launchWhenStarted` 的区别？（前者在低于 STARTED 时会取消并重启，更安全）
2. 协程取消后 Database 操作会回滚吗？（不会自动回滚，需用 `withTransaction`）
3. 如何调试协程？(JVM 参数 `-Dkotlinx.coroutines.debug` + 协程命名）

**面试官考察点：**
- 项目实战中是否遵循工业标准
- 对应用生命周期和协程生命周期的协调能力
- 是否关注测试和可调试性

**易踩坑：**
- ❌ 在 Fragment 中直接 `lifecycleScope.launch { viewModel.state.collect {} }`（应用后台后仍然收集）
- ❌ 忘记在 Repository 层切换线程（`suspend` 函数本身不切换线程）
- ❌ 使用 `GlobalScope` 或自行创建 `CoroutineScope` 而不管理生命周期

---

## 二、Jetpack 核心组件（40题）

#### ViewModel（8题）

### <a id="jetpack-01"></a>31. ViewModel 的生命周期与原理

**标准回答：**
ViewModel 通过 ViewModelStore 实现跨 Configuration Change 存活。核心机制：ComponentActivity 在 onRetainNonConfigurationInstance() 中将 ViewModelStore 保存到 NonConfigurationInstances 对象中，Activity 重建时通过 getLastNonConfigurationInstance() 取回。正常 finish() 时，Activity 的 ON_DESTROY 触发 ViewModelStore.clear() 调用所有 ViewModel.onCleared()。Key 类是 ViewModelStore——内部用 HashMap<String, ViewModel> 存储，ViewModelProvider 通过 key（默认类名）查找或创建。

**高频追问：**
1. Fragment 的 ViewModel 如何存活？通过 FragmentManagerViewModel（存在宿主 Activity 的 ViewModelStore 中），内部 Map<Fragment.mWho, ViewModelStore>。
2. ViewModel 内部是怎么保证不持有 View 引用的？设计约定+代码审查+Lint 检查，没有强制机制。
3. Config Change 和 Process Death 哪个场景 ViewModel 会丢失？Process Death 会丢失普通 ViewModel，SavedStateHandle 可以恢复。

**面试官考察点：**
- 是否读过 ViewModelProvider.get() / ViewModelStore 源码
- 能否区分 Config Change 和真正的 Activity Destroy
- 是否理解 FragmentManagerViewModel 的层级管理

**易踩坑：**
- ❌ 认为 ViewModel 通过 onSaveInstanceState 保存——完全不同的机制
- ❌ 在 ViewModel 中持有 Activity/View/Context 引用——导致内存泄漏和单元测试困难

---

### <a id="jetpack-02"></a>32. ViewModelProvider 的创建过程源码分析

**标准回答：**
ViewModelProvider.get() 调用链：get(key, modelClass) → ViewModelStore.get(key) → 若已存在则返回，否则 Factory.create(modelClass) → 新 ViewModel 存入 ViewModelStore → 返回。Factory 默认有三种：NewInstanceFactory（无参构造，反射 newInstance）、AndroidViewModelFactory（传入 Application）、SavedStateViewModelFactory（注入 SavedStateHandle）。SavedStateViewModelFactory 内部通过反射查找带 SavedStateHandle 参数的构造函数。

**高频追问：**
1. 反射创建 ViewModel 有什么性能影响？一次反射开销可忽略，ViewModel 创建后缓存在 ViewModelStore 中后续无需反射。
2. SavedStateHandle 是怎么注入 ViewModel 构造函数的？SavedStateViewModelFactory 反射查找接受 SavedStateHandle 参数的构造器并传入。
3. 自定义 Factory 的典型场景？需要传构造参数（如 userId）时。

**面试官考察点：**
- 对 ViewModel 创建完整链路的理解
- Factory 模式的三种实现及适用场景
- SavedStateHandle 的注入原理

**易踩坑：**
- ❌ 忘记自定义 Factory 导致无参 ViewModel 无法传参
- ❌ 误用 AndroidViewModel 的 Application 做重量级初始化

---

### <a id="jetpack-03"></a>33. SavedStateHandle 的原理与使用

**标准回答：**
SavedStateHandle 解决进程被杀后 ViewModel 状态恢复问题。原理：内部维护 Map<String, Object>，通过 SavedStateRegistry（ComponentActivity 的 onSaveInstanceState 底层）持久化。支持 getLiveData()、getStateFlow()、set()/get() API。支持的存储类型：Bundle 兼容类型（int/long/boolean/String/float/double/Parcelable/Serializable）。存储限制：Bundle 约 1MB，不适合存大量数据。

**高频追问：**
1. SavedStateHandle 和 onSaveInstanceState 的关系？SavedStateHandle 底层就是通过 SavedStateRegistry 走 onSaveInstanceState 机制。
2. 进程重建后数据恢复的时机？Activity onCreate 之前就完成恢复，ViewModel 创建时 SavedStateHandle 已有数据。
3. 复杂对象怎么存？拆分为基本字段或存 JSON String + TypeConverter。

**面试官考察点：**
- 理解 VM 生命周期中 Process Death 场景的薄弱点
- SavedStateHandle 作为 ViewModel + onSaveInstanceState 的桥梁
- Bundle 大小限制（1MB TransactionTooLargeException）

**易踩坑：**
- ❌ 存入几百 KB 的数据导致 TransactionTooLargeException
- ❌ 用 SavedStateHandle 存所有 ViewModel 字段——只应存需要恢复的关键数据

---

### <a id="jetpack-04"></a>34. ViewModelScope 与 viewModelScope

**标准回答：**
viewModelScope 是 ViewModel 的扩展属性，返回一个 CoroutineScope，绑定了 ViewModel 的整个生命周期（从创建到 onCleared）。内部实现：CloseableCoroutineScope（SupervisorJob() + Dispatchers.Main.immediate）。ViewModel.onCleared() 中 scope.cancel() 自动取消所有协程。这意味着在 viewModelScope 中启动的协程不需要手动 cancel，ViewModel 清理时自动处理。

**高频追问：**
1. viewModelScope 用的 SupervisorJob 而不是普通 Job 为什么？SupervisorJob 一个子协程失败不影响其他子协程，避免一个网络请求失败取消所有协程。
2. Dispatchers.Main.immediate 和 Dispatchers.Main 的区别？immediate 如果已在主线程则立即执行不排队，减少一帧延迟。
3. viewModelScope 和 lifecycleScope 什么时候分别用？viewModelScope 用于 ViewModel 中需要与 VM 同生命周期的协程；lifecycleScope 用于 View/Fragment 层。

**面试官考察点：**
- SupervisorJob vs Job 的理解
- 协程结构化并发的自动取消机制
- viewModelScope vs lifecycleScope 适用场景

**易踩坑：**
- ❌ 在 ViewModel 中用 GlobalScope 或手动创建 scope 导致泄漏
- ❌ viewModelScope 中 launch 而没有 switch context 就直接做网络请求

---

### <a id="jetpack-05"></a>35. AndroidViewModel vs ViewModel

**标准回答：**
AndroidViewModel 继承 ViewModel，唯一区别是持有 Application 引用（通过构造函数传入）。AndroidViewModelFactory 自动提供 Application 参数。使用场景：需要访问 Application Context 获取系统服务（ConnectivityManager、NotificationManager、Resources 等）但不需要 Activity Context。注意：只持有 Application Context 不会导致内存泄漏，因为其生命周期是整个进程。

**高频追问：**
1. AndroidViewModel 为什么要传 Application 而非 Context？限定为 Application Context 保证不会误用 Activity Context 导致泄漏。
2. 如果 ViewModel 需要 Context 但不想用 AndroidViewModel？通过 ApplicationProvider.getApplicationContext() 获取（需引入 testing 依赖不太优雅），或自定义 Factory 注入 Application。
3. AndroidViewModel 能直接操作 UI 吗？不能，ViewModel 不应该持有任何 View 引用。

**面试官考察点：**
- ViewModel 的 Context 隔离原则
- Application Context vs Activity Context 生命周期差异
- 对测试友好性的考虑（AndroidViewModel 单元测试需要 Application）

**易踩坑：**
- ❌ AndroidViewModel 的构造函数中做重量级初始化阻塞主线程
- ❌ 传入非 Application Context 导致泄漏

---

### <a id="jetpack-06"></a>36. onCleared 的调用时机与资源释放

**标准回答：**
onCleared() 在 ViewModel 不再使用且即将销毁时调用：Activity finish()→ON_DESTROY→ViewModelStore.clear()→所有 ViewModel.onCleared()。Config Change 时不调用（ViewModel 存活）。适合在 onCleared 中：取消协程（viewModelScope 自动取消此点可以不做）、关闭资源流（如 observeForever 的 LiveData 移除）、清理回调注册。注意 onCleared 在主线程调用，耗时任务需切换到后台。

**高频追问：**
1. onCleared 和 Fragment.onDestroyView 谁先调用？Fragment onDestroyView 先调用（ViewModel 仍存活），Fragment onDestroy 后 ViewModelStore 才可能清理。
2. 如果 Fragment 被 replace 而不是 remove，onCleared 会调用吗？不会，replace 默认不 destroy Fragment，ViewModel 仍在 backstack 中存活。
3. 怎么主动触发 ViewModel 清理？获取 ViewModelStore，手动 remove 或 clear。

**面试官考察点：**
- onCleared 时机与 Activity/Fragment 生命周期的对应关系
- 资源释放的最佳实践
- ViewModel 存活的边界条件

**易踩坑：**
- ❌ 在 onCleared 中做 UI 操作（此时 View 可能已销毁）
- ❌ 依赖 onCleared 释放内存——它不一定被及时调用

---

### <a id="jetpack-07"></a>37. 跨 Fragment 共享 ViewModel

**标准回答：**
同一 Activity 范围内的多个 Fragment 可通过共享 ViewModel 通信。方式：都用 activityViewModels() 或都通过同一个 ViewModelStoreOwner（通常是宿主 Activity）获取 ViewModel：
```kotlin
// Fragment A
private val sharedVM: SharedViewModel by activityViewModels()
// Fragment B
private val sharedVM: SharedViewModel by activityViewModels()
```
原理：两个 Fragment 通过同一个 ViewModelStore（Activity 的）存取 ViewModel，key 相同（类名）所以共享同一实例。Navigation 中也可通过 navGraphViewModels 在 NavGraph 范围内共享。

**高频追问：**
1. activityViewModels 和 navGraphViewModels 的区别？前者 Activity 级共享；后者绑定到某个 NavGraph，该 NavGraph 销毁时 ViewModel 自动清理。
2. 共享 ViewModel 可能造成什么问题？ViewModel 过于臃肿（所有 Fragment 共享一个）；需要在 Fragment 销毁时清理各自状态。
3. 两个 Fragment 在不同 Activity 中怎么共享？无法通过 ViewModel 共享，需走 Repository/数据库/EventBus。

**面试官考察点：**
- ViewModel 作用域的理解（Activity Scope vs Fragment Scope vs NavGraph Scope）
- 是否用 ViewModel 做 Fragment 通信代替 EventBus/接口回调

**易踩坑：**
- ❌ 用 Activity 的 ViewModelStore 共享所有数据导致 VM 臃肿
- ❌ Fragment 销毁后共享 VM 的数据残留未被清理

---

### <a id="jetpack-08"></a>38. ViewModel 的工厂模式与依赖注入

**标准回答：**
当 ViewModel 需要构造参数时，不能直接用默认 Factory（只支持无参构造或 AndroidViewModel），必须自定义 Factory：
```kotlin
class UserViewModelFactory(private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(userId) as T
    }
}
```
更好的方式是用 Hilt：@HiltViewModel + @Inject constructor 自动生成 Factory。或手动用 AbstractSavedStateViewModelFactory 同时支持 SavedStateHandle + 自定义参数。

**高频追问：**
1. Hilt 怎么生成 ViewModel 的 Factory？通过 @HiltViewModel 注解，Hilt 编译时生成 ViewModelProvider.Factory 的子类，自动注入依赖。
2. AssistedInject 可以在 ViewModel 中用来注入运行时参数，传参方式问？需要自定义 Factory + AssistedFactory 接口。
3. 为什么不用 Koin 的 viewModel { }？Koin 的 viewModel DSL 运行时反射创建，不如 Hilt 编译时安全。

**面试官考察点：**
- Factory 模式的实际应用
- DI 框架与 ViewModel 的集成方式
- 构造参数传递的多种方案

**易踩坑：**
- ❌ 不用 Factory 强行用单例模式传参
- ❌ Hilt 的 @Assisted 注解引入不熟悉的依赖

---

#### LiveData（6题）

### <a id="jetpack-09"></a>39. LiveData 的原理机制

**标准回答：**
LiveData 是持有数据并可被观察的类，生命周期感知。observe(LifecycleOwner, Observer) 注册时内部创建 LifecycleBoundObserver（实现 LifecycleEventObserver）。当 Lifecycle 达到 STARTED 才分发数据，DESTROYED 时自动移除 Observer。数据变化通过 setValue/postValue → dispatchingValue → considerNotify（检查 Observer 状态是否 active）→ observer.onChanged()。关键字段 mVersion 防重复通知——每个 Observer 有自己的 mLastVersion，比对后才通知。

**高频追问：**
1. LiveData 粘性事件的源码原因是什么？Observer 注册时检查 mLastVersion（初始为 -1）< mVersion（可能大于 -1），条件满足立即通知。
2. observeForever 的表现和风险？始终处于 active 状态，不受 Lifecycle 限制，需要手动 removeObserver 否则泄漏。
3. LiveData 有什么性能开销？Observer 注册/反注册在 Lifecycle 变化时触发，频繁注册有轻微开销。

**面试官考察点：**
- mVersion 版本机制——解决 Lifecycle 重建重新通知和去重
- LifecycleBoundObserver 的生命周期感知原理

**易踩坑：**
- ❌ 误以为 Lifecycle DESTROYED 后 LiveData 所有事件都不触发（observeForever 仍触发）
- ❌ 在 onInactive 做不了太多事情后就移除 observer 了

---

### <a id="jetpack-10"></a>40. Transformations：map 与 switchMap

**标准回答：**
Transformations 提供函数式转换操作：
- map(LiveData<X>, Function<X,Y>)：将 X 类型 LiveData 转为 Y 类型，每次 X 变化自动触发 Y 更新。内部用 MediatorLiveData 包装。
- switchMap(LiveData<X>, Function<X, LiveData<Y>>)：当 X 变化时，切换到新的 LiveData<Y> 作为数据源，旧的 LiveData 自动移除。适合根据 userId 切换查询不同用户的 LiveData。
区别：map 做 1:1 同步转换；switchMap 做 1:N 动态切换数据源。

**高频追问：**
1. switchMap 底层怎么管理旧的 LiveData？MediatorLiveData 内部维护 Map<LiveData, Observer>，switchMap 的 Function 返回新 LiveData 时先 removeSource 旧的再 addSource 新的。
2. 多个 LiveData 怎么合并？MediatorLiveData.addSource() 分别添加多个数据源。
3. Flow 的 map/flatMapLatest 与 Transformations 的对应关系？map ↔ LiveData.map，flatMapLatest ↔ LiveData.switchMap。

**面试官考察点：**
- 函数式转换思维在 LiveData 的应用
- MediatorLiveData 的内部管理机制（Multiple Sources）
- Flow 和 LiveData 操作符的对应关系

**易踩坑：**
- ❌ 在 map 中做耗时操作——应在数据源头处理（Repository 层转换）
- ❌ switchMap 中 Function 每次触发都创建新 LiveData 导致源泄漏

---

### <a id="jetpack-11"></a>41. MediatorLiveData 的使用场景

**标准回答：**
MediatorLiveData 继承 MutableLiveData，可以合并多个 LiveData 数据源。核心 API：`addSource(LiveData&lt;S&gt;, Observer&lt;S&gt;)` → 观察一个源 LiveData，变化时更新自身 value。`removeSource(LiveData)` → 停止观察。典型场景：多个数据源合并为一个 UI 状态（如合并网络状态和数据库数据）；动态切换数据源（如搜索框输入变化动态切换搜索结果）。

**高频追问：**
1. MediatorLiveData vs Flow.combine 的对比？MediatorLiveData 手动管理合并逻辑；Flow.combine 声明式自动合并任意数量 Flow。
2. addSource 后什么时候 removeSource？当不再需要该数据源（如 switchMap 场景下切换到新源时）必须移除，否则内存泄漏。
3. MediatorLiveData 内部是如何管理多个 Source 的？内部维护 Map<LiveData<?>, Observer<?>> mSources 存储所有观察关系。

**面试官考察点：**
- 多数据源合并的实际应用能力
- 手动 addSource/removeSource 管理
- 与 Flow 操作符的选择场景

**易踩坑：**
- ❌ addSource 后忘记 removeSource 导致内存泄漏
- ❌ 多个 source 同时更新导致 value 被覆盖多次

---

### <a id="jetpack-12"></a>42. postValue 与 setValue 的区别

**标准回答：**
setValue(T)：必须在主线程调用，同步通知所有激活的 Observer。底层直接更新 mData 并调 dispatchingValue()。postValue(T)：可在任意线程调用，通过 ArchTaskExecutor.getInstance().postToMainThread() 将掷到主线程执行。内部 synchronized(mDataLock) 保护 mPendingData，连续 postValue 多次只有最后一个值会生效（Runnable 只入队一次）。mPendingData 用 NOT_SET 标记判断是否有待处理任务。

**高频追问：**
1. postValue 的 mPendingData 和 setValue 的 mVersion 是什么关系？mPendingData 管理异步投递；mVersion 管理 Observer 是否已经收到该值。两者独立但配合工作。
2. 为什么连续 postValue 中间值会丢失？mPostValueRunnable 执行时才读取 mPendingData 的最新值，中间 set 已覆盖。
3. 在子线程回调中 postValue 到 LiveData，主线程 observe 一定能收到吗？如果 LiveData 的 Lifecycle 处于 active 状态则能收到，否则需等到 active 才通知。

**面试官考察点：**
- 源码级别的 mPendingData 和线程安全机制
- 异步更新的覆盖问题

**易踩坑：**
- ❌ 子线程调用 setValue→抛出 IllegalStateException
- ❌ 依赖 postValue 的中间值做计数（只能拿到最后一次值）

---

### <a id="jetpack-13"></a>43. observeForever 的内存泄漏问题

**标准回答：**
observeForever(Observer) 注册的 Observer 没有 Lifecycle 绑定，永远处于 active 状态。这意味着它不会被自动移除，如果外部持有 Observer 或 LiveData 的引用链中包含 Activity/Fragment 就形成泄漏。解决：1）在适当时机（如 onDestroy）手动调用 removeObserver；2）尽量用 observe(LifecycleOwner, Observer) 代替 observeForever；3）使用 Lifecycle 的 addObserver 在 DESTROYED 时自动调用 removeObserver。

**高频追问：**
1. observeForever 的使用场景有哪些？Repository 层监听数据变化（无 LifecycleOwner）；需要在后台接收 LiveData 通知的场景。
2. 和 LiveData.observe 的性能差异？observeForever 不需要 Lifecycle 状态检查，略快但不显著。
3. 多个 observeForever 怎么管理？用 CompositeDisposable 或类似模式集中管理，统一在某个时机批量移除。

**面试官考察点：**
- observeForever 与 observe 的本质区别（有无 Lifecycle 绑定）
- 手动管理 Observer 生命周期的能力

**易踩坑：**
- ❌ 在 Activity 中 observeForever 且不 removeObserver 导致 Activity 泄漏
- ❌ 重复 observeForever 同一个 Observer 导致触发多次

---

### <a id="jetpack-14"></a>44. LiveData vs Flow 的对比与选择

**标准回答：**
| 维度 | LiveData | Flow |
|------|---------|------|
| 生命周期感知 | 内置（自动） | 需手动 repeatOnLifecycle |
| 线程调度 | 主线程（setValue） | 任意线程（flowOn） |
| 操作符 | 仅 map/switchMap | 丰富（map/filter/flatMap/combine等） |
| 粘性 | 有粘性 | StateFlow有粘性/SharedFlow可配 |
| 跨平台 | Android Only | 跨平台（KMP） |
| 与DataBinding | 天然配合 | 需转LiveData（asLiveData） |

选择建议：UI简单场景→LiveData；数据层/复杂转换→Flow；一次性事件→SharedFlow（replay=0）；跨平台项目→Flow。

**高频追问：**
1. StateFlow vs LiveData 的关键差异？StateFlow 必须初始值、去重用 equals 而非版本号、不感知 Lifecycle。
2. Flow 怎么转 LiveData？flow.asLiveData() 或 stateIn 配合 asLiveData。
3. DataBinding 中如何用 Flow？binding.setLifecycleOwner 后用 StateFlow 的 collectAsState（Compose场景），或 flow.asLiveData() 给 XML 用。

**面试官考察点：**
- 不是选边站，展示根据场景灵活选择的工程思维
- LiveData→Flow 迁移路径

**易踩坑：**
- ❌ 已有复杂操作符需求的场景仍选 LiveData（迁移成本高）
- ❌ lifecycleScope 中 collect Flow 不配合 repeatOnLifecycle

---

#### Lifecycle（6题）

### <a id="jetpack-15"></a>45. LifecycleOwner 与 LifecycleObserver

**标准回答：**
LifecycleOwner 是拥有生命周期的对象（Activity/Fragment 默认实现），通过 getLifecycle() 返回 LifecycleRegistry。LifecycleObserver 是观察生命周期的接口（DefaultLifecycleObserver 为推荐接口）。owner.lifecycle.addObserver(observer) 注册观察者。核心流程：owner 生命周期变化 → LifecycleRegistry.handleLifecycleEvent(Event) → moveToState 转换 State → sync() 同步所有 Observer → Observer.onStateChanged/onLifecycleEvent。

**高频追问：**
1. DefaultLifecycleObserver vs LifecycleObserver 注解方式？DefaultLifecycleObserver 是接口（编译时检查），@OnLifecycleEvent 是注解（运行时反射）。推荐接口方式。
2. 多个 Observer 按什么顺序通知？按 addObserver 的注册顺序。
3. addObserver 时如果 Lifecycle 已到 DESTROYED 会怎样？直接抛 IllegalStateException。

**面试官考察点：**
- Lifecycle 组件的设计模式（观察者模式）
- 接口方式 vs 注解方式的优劣

**易踩坑：**
- ❌ 在 DESTROYED 后注册 Observer 直接 crash
- ❌ 用 @OnLifecycleEvent 注解在混淆后可能失效

---

### <a id="jetpack-16"></a>46. Lifecycle 状态机与事件流转

**标准回答：**
Lifecycle 内部是有限状态机。5 种 State：INITIALIZED（初始）→CREATED（已创建）→STARTED（可见）→RESUMED（前台）→DESTROYED（销毁）。7 种 Event：ON_CREATE/ON_START/ON_RESUME/ON_PAUSE/ON_STOP/ON_DESTROY/ON_ANY。状态转移由 LifecycleRegistry.moveToState(State) 触发，内部通过 upEvent（向前）和 downEvent（向后）计算 Event，然后 sync() 同步所有 Observer。ON_ANY 比较特殊——不参与状态转换，只是通配符用来监听所有 Event。getStateAfter(Event) 方法编码了 Event→State 的映射关系。

**高频追问：**
1. 什么场景会触发 ON_PAUSE 但不会 ON_STOP？半透明 Activity 在上面，下面 Activity 收到 ON_PAUSE 但不到 ON_STOP。
2. sync() 中的 forwardPass 和 backwardPass 是什么意思？forwardPass：Observer 状态落后则推进；backwardPass：Observer 状态超前则回退。确保所有 Observer 状态同步。
3. ON_ANY 在内部怎么被特殊处理？不调用 moveToState，直接分发给注册了 ON_ANY 的 Observer。

**面试官考察点：**
- 5 State + 7 Event 精确掌握
- 状态转移矩阵及其实现（getStateAfter/moveToState）
- ON_ANY 的特殊性

**易踩坑：**
- ❌ 混淆 Event（动作）和 State（状态）的概念
- ❌ 假设所有 Event 都对应到不同的 State（ON_PAUSE→STARTED,不是 PAUSED）

---

### <a id="jetpack-17"></a>47. ProcessLifecycleOwner 的应用

**标准回答：**
ProcessLifecycleOwner 监听整个 APP 的前后台切换。原理：通过 ReportFragment 注入 Activity，监听每个 Activity 的 onStart/onStop，维护计数器（mStartedCounter/mResumedCounter）。当第一个 Activity onStart 时 → ON_START（进前台）；当最后一个 Activity onStop 700ms 后 → ON_STOP（进后台）。700ms 延迟（mDelayedPauseDuration）是用来过滤快速切换（如权限弹窗）。用法：在 Application onCreate 中 ProcessLifecycleOwner.get().lifecycle.addObserver()。

**高频追问：**
1. ProcessLifecycleOwner 如何注入 Activity？内部注册 ActivityLifecycleCallbacks，在 onActivityCreated 中注入 ReportFragment。
2. 延迟 700ms 在什么场景会失效？连续快速切换多个 Activity 时延迟会重置。
3. ProcessLifecycleOwner 和 ActivityLifecycleCallbacks 怎么选？前者用来判断整体前后台，后者用来监控每个 Activity 的生命周期。

**面试官考察点：**
- ReportFragment + 计数器的实现机制
- 时间延迟的设计意图
- 与普通 LifecycleOwner 的区别

**易踩坑：**
- ❌ 用 ProcessLifecycleOwner 监控单个 Activity 生命周期（应该用 ActivityLifecycleCallbacks）
- ❌ 在 onStop 中做耗时操作（700ms 延迟后才回调，但回调本身也在主线程）

---

### <a id="jetpack-18"></a>48. LifecycleService 的使用场景

**标准回答：**
LifecycleService 是 Service + LifecycleOwner 的组合，让 Service 也能拥有 Lifecycle。内部持有 ServiceLifecycleDispatcher，在 Service 的 onCreate/onStartCommand/onDestroy 中分发对应的 Lifecycle Event。使用场景：在 Service 中需要生命周期感知组件（如用 LiveData 观察数据变化）时。但 Android 8+ 后台 Service 受限制，通常用 WorkManager 或 Foreground Service 替代。

**高频追问：**
1. LifecycleService 和普通 Service + 手动 LifecycleOwner 的区别？LifecycleService 自动管理 Lifecycle 事件分发，手动的需要自行在对应回调中 handleLifecycleEvent。
2. LifecycleService 能保持后台存活吗？不能，它只是加了 Lifecycle 能力，仍受 Android 8+ 后台限制。
3. 现在还有必要用 LifecycleService 吗？WorkManager 是更好的替代方案，除非有特殊原因必须用 Service+Lifecycle 组件。

**面试官考察点：**
- Service 与 Lifecycle 的结合方式
- 对后台限制的认知

**易踩坑：**
- ❌ LifecycleService 中用 LiveData observe（被 Service 的 DESTROYED 自动解绑）
- ❌ 认为 LifecycleService 可以绕过后台 Service 限制

---

### <a id="jetpack-19"></a>49. 自定义 LifecycleOwner

**标准回答：**
实现 LifecycleOwner 接口，返回一个 LifecycleRegistry 实例：
```kotlin
class MyLifecycleOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle get() = registry
    
    fun onCreate() { registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE) }
    fun onDestroy() { registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY) }
}
```
常见场景：自定义 View 需要生命周期感知；非 Activity/Fragment 的组件需要集成 Lifecycle。注意事项：必须在主线程调用 handleLifecycleEvent；状态转移必须合法（如不能从 DESTROYED 回到 RESUMED）；任何 Observer 添加后要确保能在 DESTROYED 时被移除。

**高频追问：**
1. LifecycleRegistry 为什么需要 LifecycleOwner 的引用？主要用于在 DESTROYED 状态时判断是否可以添加新的 Observer（不能）。
2. 自定义 LifecycleOwner 用在哪？支付 SDK 回调组件、地图 SDK 封装、自定义播放器等。
3. handleLifecycleEvent 能在子线程调用吗？不能，内部检查主线程（ArchTaskExecutor.isMainThread）。

**面试官考察点：**
- LifecycleOwner 的手动实现能力
- 生命周期事件的正确调度时机

**易踩坑：**
- ❌ 未按合法顺序调用 handleLifecycleEvent 导致 IllegalStateException
- ❌ 忘记在 DESTROYED 时 clean up 导致 Observer 泄漏

---

### <a id="jetpack-20"></a>50. Lifecycle 在 MVP/MVVM 中的应用

**标准回答：**
在 MVP 中：Presenter 通过实现 LifecycleObserver + @OnLifecycleEvent 或 DefaultLifecycleObserver 感知 View（Activity/Fragment）生命周期，在 ON_DESTROY 时解除 View 引用防止内存泄漏。在 MVVM 中：ViewModel 不直接持有 Lifecycle 引用（它本身是 LifecycleObserver 的目标），但 ViewModel 内部的 LiveData 通过 LifecycleOwner 自动管理订阅。View 层（lifecycleScope/flowWithLifecycle）用 Lifecycle 控制协程和 Flow 的订阅范围。

**高频追问：**
1. Presenter 应该感知哪些生命周期事件？onCreate（初始化）、onDestroy（清理 View 引用）。onStart/onStop 看具体需求。
2. ViewModel 为什么不直接实现 LifecycleObserver？ViewModel 的生命周期比 Lifecycle 长（跨 Config Change），绑定 Lifecycle 会导致 ViewModel 在 Config Change 时被通知不合适。
3. Compose 中有 Lifecycle 吗？有，LocalLifecycleOwner.current 可以获取，通常用 collectAsStateWithLifecycle。

**面试官考察点：**
- Lifecycle 在不同架构模式下的正确使用方式
- Presenter 防泄漏的标准做法（实现 LifecycleObserver）
- ViewModel 不直接依赖 Lifecycle 的设计原因

**易踩坑：**
- ❌ Presenter 持有 View 引用不使用 Lifecycle 管理（经典泄漏）
- ❌ ViewModel 中通过 Lifecycle 观察（违背 ViewModel 的设计原则）

---

#### Room（8题）

### <a id="jetpack-21"></a>51. Entity、DAO、Database 三要素

**标准回答：**
Room 三大核心组件：@Entity 定义表结构（data class + 注解），通过 tableName/indices/foreignKeys 参数配置；@Dao 定义数据访问接口（interface 或 abstract class），通过 @Insert/@Update/@Delete/@Query 声明方法，Room 编译时生成实现类（XXXDao_Impl）；@Database 抽象类声明数据库（entities 列表 + version + DAO 抽象方法），编译时生成 RoomDatabase_Impl 子类负责数据库创建/升级/获取 DAO。三者关系：Database 持有 DAO，DAO 操作 Entity，Entity 映射表。

**高频追问：**
1. Room 编译生成的 Impl 类在哪个目录？build/generated/source/kapt（或 ksp）/debug。
2. DAO 方法能返回哪些类型？void/Long/Int/List/Flow/LiveData/Single（配合 Guava）。
3. Room 如何支持 Kotlin suspend 函数？Room 编译时检测 suspend 函数，生成 Coroutine 调用代码，底层在 RoomDatabase 的事务线程池中执行。

**面试官考察点：**
- Room 的编译时代码生成机制
- 三要素的定义和关系
- Kotlin 协程 suspend 的函数，与 Room 集成方式

**易踩坑：**
- ❌ @Entity data class 不声明主键导致编译错误
- ❌ @Database entities 列表漏掉某个 @Entity 导致运行时崩溃

---

### <a id="jetpack-22"></a>52. Room 中 @Query、@Insert、@Update、@Delete 的高级用法

**标准回答：**
@Query 支持原生 SQL，高级用法：1）返回复杂类型（JOIN 自定义 POJO）；2）参数绑定（:paramName 或 ?编号）；3）返回 Flow/LiveData 实现响应式查询；4）与 @Transaction 配合多表查询保证一致性。@Insert 的 onConflict 策略：REPLACE（替换）/IGNORE（忽略）/ABORT（中止回滚事务）。@Update/@Delete 自动根据主键匹配，也支持 onConflict。批量操作用 @Insert(onConflict=REPLACE) fun insertAll(vararg users: User)。

**高频追问：**
1. Room 的 @RawQuery 和 @Query 的区别？@RawQuery 运行时构建 SQL（SupportSQLiteQuery），灵活性高但无编译检查。
2. ON CONFLICT REPLACE 的实际行为？DELETE 旧行 + INSERT 新行（不是 UPDATE），因此未指定字段会恢复默认值。
3. 如何返回部分字段？定义只含所需字段的 POJO：@Query("SELECT name, age FROM users") fun getNamesAndAges(): List<NameAndAge>。

**面试官考察点：**
- SQL 和 Room 注解的混合使用能力
- onConflict 策略理解
- 编译时 SQL 验证的价值

**易踩坑：**
- ❌ ON CONFLICT REPLACE 误以为会保留旧行的未指定字段（实际会清为默认值）
- ❌ 用 + 拼接 SQL 字符串而非参数绑定（SQL 注入风险）

---

### <a id="jetpack-23"></a>53. Room 与 Flow 的集成

**标准回答：**
Room 从 2.2+ 原生支持 Flow 返回值：@Query 方法返回 Flow<T> 类型时，Room 编译时生成包含 InvalidationTracker 的代码。当目标表发生 INSERT/UPDATE/DELETE 时，InvalidationTracker 通知所有注册的 Observer → Room 重新执行查询 → Flow 发射新数据。这是一个"表级"监听（Table Level），不是行级。关键类：CoroutineRoomDatabase 提供 Flow 查询的协程支持。每次 subscribe 都会立即发射一次（像 StateFlow）。

**高频追问：**
1. Room Flow 是冷流还是热流？冷流——每次 collect 执行查询；多次 collect 会启动独立查询。
2. 如何实现多表合并的 Flow？用 database.withTransaction{} 查多表，或 MediatorLiveData 合并。
3. Flow 在 Room 中的线程模型？查询默认在 Room 的 IO 线程池执行；collect 的线程取决于调用方设置的 Dispatcher。

**面试官考察点：**
- InvalidationTracker 表级变化监听
- 冷流特性：每次 collect 独立查询
- Room Flow 与常规 Flow 的区别

**易踩坑：**
- ❌ 假设 Room Flow 是热流导致多次不必要的 collect 查询
- ❌ 在 ViewModel 中直接 collect Room Flow 没有配合 viewModelScope

---

### <a id="jetpack-24"></a>54. 数据库迁移（Migration）策略

**标准回答：**
Room 数据库版本升级通过 Migration(startVersion, endVersion) 处理，migrate() 中执行 ALTER TABLE/CREATE TABLE 等 SQL。Room 自动找最短迁移路径（v1→v3 可能需要 v1→v2→v3 两步）。如果没有覆盖某路径的 Migration，Room 抛 IllegalStateException。兜底方案：fallbackToDestructiveMigration() 会清空数据库重建（只适合开发环境）；fallbackToDestructiveMigrationFrom(3, 4) 指定范围允许破坏性迁移。

**高频追问：**
1. SQLite 的 ALTER TABLE 限制怎么绕过？SQLite 不支持 DROP COLUMN/重命名列等操作，需创建新表→复制数据→删旧表→重命名。
2. 多版本 Migration 如何测试？MigrationTestHelper + @Rule Room 测试迁移路径，验证迁移前后数据一致性。
3. 生产环境如何处理破坏性迁移？绝不能在生产使用——应导出/导入用户数据或写完整 Migration 路径。

**面试官考察点：**
- 多版本 Migration 链
- destructiveMigration 的生产禁忌
- SQLite 限制与应对方案

**易踩坑：**
- ❌ 生产环境配置 destructiveMigration——用户数据全丢
- ❌ ALTER TABLE 不加 DEFAULT 值导致已有行该列为 NULL

---

### <a id="jetpack-25"></a>55. TypeConverter 自定义类型转换

**标准回答：**
Room 仅支持基本类型（int/long/float/double/String/byte[]）和其包装类型。复杂类型（如 Date、List、枚举、自定义对象）需要 TypeConverter：
```kotlin
class Converters {
    @TypeConverter fun fromTimestamp(value: Long?) = value?.let { Date(it) }
    @TypeConverter fun dateToTimestamp(date: Date?) = date?.time
}
@Database(entities=[User::class], version=1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()
```
@TypeConverters 可标记在 Database/DAO/Entity/字段/方法级别，作用域越小优先级越高。一对转换方法签名必须对称。

**高频追问：**
1. TypeConverter 和 @Embedded 的区别？TypeConverter 将单个字段序列化为单个列（如 Date→Long）；@Embedded 将嵌套对象展开为多列。
2. 如何存储 List 类型？用 Gson/Moshi 将 List 序列化为 JSON String + TypeConverter。
3. TypeConverter 的性能影响？读 DB 时反序列化有开销，不建议转换大对象。

**面试官考察点：**
- SQLite 类型系统与 Kotlin 类型的桥接
- @TypeConverters 作用域优先级
- TypeConverter vs @Embedded vs 外键的选型

**易踩坑：**
- ❌ TypeConverter 忘注册到 @Database/@DAO 导致编译失败
- ❌ 用 TypeConverter 存储几百 KB 的大对象（性能差）

---

### <a id="jetpack-26"></a>56. @Relation、@ForeignKey、@Index 的使用

**标准回答：**
@ForeignKey：声明表间约束关系（entity 父表，parentColumns 父列，childColumns 子列），onDelete/onUpdate 支持 CASCADE（级联）/SET_NULL/SET_DEFAULT/RESTRICT/NO_ACTION 五种策略。@Index：为列创建索引加速查询，unique=true 保证唯一性。@Relation：用于 POJO 中定义嵌套查询关系（一对多/一对一），它不是 SQL JOIN——Room 先查主表再用主键集合 IN 查询子表（两步查询）。多对多需中间关联表 + @Junction 参数。

**高频追问：**
1. @Relation 为什么不直接生成 JOIN？为了处理嵌套结果映射（一行主表数据对应多行子表数据），JOIN 会产生冗余行。
2. 什么时候该手动 JOIN 代替 @Relation？数据量超过数百条时，@Relation 两步查询比 JOIN 差。
3. ForeignKey 的 index 为什么默认会创建？Room 要求外键列必须建索引，否则编译器报警（warning）。

**面试官考察点：**
- 外键约束级联策略
- @Relation 的两步查询本质
- 索引的应用场景

**易踩坑：**
- ❌ @Relation 误以为是 JOIN（实际是两步查询，大数量 N+1）
- ❌ 忘记 @ForeignKey 的父表在 @Database entities 中声明

---

### <a id="jetpack-27"></a>57. Room 与 RxJava/协程的集成

**标准回答：**
Room 原生支持 RxJava3/2 的返回类型（Flowable/Single/Maybe/Completable）。Flowable 和 LiveData 一样支持表级数据变化监听。协程支持更彻底：DAO 方法声明为 suspend 函数，Room 自动在内部 IO 线程池执行，完成后返回结果。Room 的协程集成不需要额外配置，只需 kotlinx-coroutines 依赖。对比：RxJava 的 Flowable 适合需要丰富操作符的场景；协程 + Flow 是现代推荐方案（Kotlin 原生、简洁）。

**高频追问：**
1. suspend 和 RxJava 同时用时 DAO Transaction 怎么保证？@Transaction 注解保证 suspend 函数和 RxJava 同在一个事务中。
2. Room 的协程 Dispatcher 是哪个？Room 内部使用的专用 Executor（非 Dispatchers.IO），通过 setTransactionExecutor/setQueryExecutor 可自定义。
3. suspend DAO 函数能取消吗？可以，Coroutine 取消后 Room 检测到协程状态在下次交互时抛 CancellationException。

**面试官考察点：**
- Room 对各种异步框架的支持程度
- 协程集成不需要额外库的特点
- 总结时事务保证方式

**易踩坑：**
- ❌ DAO suspend 函数在主协程 scope 调用仍会切到 Room 的 IO 线程
- ❌ 假设 Room 和协程是独立的事务环境——@Transaction 同时适用

---

### <a id="jetpack-28"></a>58. Room FTS 全文搜索

**标准回答：**
Room 通过 @Fts4(contentEntity=Entity::class) 或 @Fts3 注解创建全文索引虚拟表。FTS（Full-Text Search）支持 MATCH 查询（比 LIKE 高效得多）：
```kotlin
@Fts4(contentEntity = Book::class)
@Entity(tableName = "books_fts")
data class BookFts(@ColumnInfo(name = "title") val title: String)

@Dao interface BookDao {
    @Query("SELECT * FROM books_fts WHERE books_fts MATCH :query")
    fun search(query: String): Flow<List<BookFts>>
}
```
FTS4 支持 prefix 匹配（query*），FTS5 更强大（支持 column filter、ORDER BY rank）。FTS 适合需要模糊搜索标题/内容的场景。注意 FTS 表需要与主表同步（触发器或手动更新）。

**高频追问：**
1. FTS4 vs FTS5 怎么选？FTS5 查询功能更强（支持列过滤、BM25 排序）但 Room 2.2+ 才支持；一般新项目用 FTS5。
2. FTS 和 LIKE '%keyword%' 的性能差异？FTS 用倒排索引，大数据量下比 LIKE 快几个数量级。
3. 如何保持 FTS 表与原表同步？@Fts4 的 contentEntity 会自动设置外部内容表，原表变化时 FTS 索引同步更新。

**面试官考察点：**
- FTS 原理（倒排索引）
- 中文分词问题（默认不分词需配合第三方分词器）
- Room FTS 的集成方式

**易踩坑：**
- ❌ 用 LIKE '%关键字%' 做全文搜索（性能极差）
- ❌ FTS 表中包含不需要搜索的字段（影响索引效率）

---

#### Navigation（6题）

### <a id="jetpack-29"></a>59. NavGraph 与 NavHostFragment 的工作原理

**标准回答：**
NavGraph 是导航图的编程表示（Navigation XML → NavInflater 解析 → NavGraph 对象）。包含 NavDestination（Fragment/Activity/Dialog 等）和 Action（导航操作）。NavController 是导航控制器，持有 NavGraph 的引用，管理 backStack（ArrayDeque<NavBackStackEntry>）。NavHostFragment 是 NavController 的容器，负责 inflate 目标 Fragment 和管理 Fragment 事务。导航流程：navigate(actionId) → NavController 找到目标 Destination → 创建 NavBackStackEntry → FragmentTransaction.replace → 记录返回栈。

**高频追问：**
1. NavController 如何管理返回栈？内部使用双端队列 ArrayDeque，push/pop 操作记录导航历史。
2. navigate() 和 popBackStack() 之间的生命周期调用顺序？navigate：当前 Fragment onPause→新 Fragment onCreate→onStart 等。popBackStack：当前 onPause→onStop→onDestroyView；回退的 Fragment onCreateView→onStart。
3. 多个 NavHostFragment 可以有各自的 NavController 吗？可以，每个 NavHostFragment 独立 NavController 独立 backStack。

**面试官考察点：**
- Navigation 三大组件（NavGraph/NavController/NavHost）的关系
- 返回栈的数据结构（ArrayDeque）
- Fragment 生命周期与导航操作的对应关系

**易踩坑：**
- ❌ 一个 Activity 中不用 NavHostFragment 直接操作 backStack 返回栈不通
- ❌ navigate 中传参用 Bundle 方式而不用 Safe Args（推荐的类型安全方式）

---

### <a id="jetpack-30"></a>60. Safe Args 参数传递

**标准回答：**
Safe Args 是 Navigation 组件配套的 Gradle 插件，编译时为每个 NavDestination 生成类型安全的参数类（XxxFragmentArgs）。替代直接 Bundle 传参的方式。优势：编译时类型检查（传错类型编译报错）、Kotlin 可空类型正确映射、支持默认值（android:defaultValue）。用法：navigation graph XML 中定义 <argument> → 插件生成 Args 类 → Fragment 中用 by navArgs<XxxFragmentArgs>() 获取。

**高频追问：**
1. Safe Args 底层还是 Bundle 吗？是的，生成的代码内部使用 Bundle 存取参数，Safe Args 是类型安全的封装层。
2. 如何自定义参数类型？实现 NavType 并注册。
3. Safe Args 不支持什么？用 Bundle 不能直接存 LiveData/Flow 等非序列化对象，Safe Args 也不行。

**面试官考察点：**
- Safe Args 的价值是编译时安全而非功能增强
- 底层仍是 Bundle 的认知

**易踩坑：**
- ❌ 忘加 Safe Args 插件依赖导致 navArgs() 不可用
- ❌ 混淆时 Safe Args 生成的类被混淆导致运行时反序列化失败

---

### <a id="jetpack-31"></a>61. Deep Link 的实现方式

**标准回答：**
Navigation 支持两种 Deep Link：1）显式 Deep Link：在 NavGraph 中用 <deepLink app:uri="myapp://detail/{id}" /> 声明 URI 模板，系统通过 Intent Filter 自动匹配。2）隐式 Deep Link：通过 NavController.navigate(Uri) 或 NavDeepLinkRequest 编程式导航。URL 参数通过 {参数名} 占位符自动解析，传递给 Destination 的 argument。支持 HTTP URL 的 Deep Link（app:uri="https://example.com/detail/{id}"）。

**高频追问：**
1. Deep Link 和普通 navigate 的区别？Deep Link 会自动清空返回栈到目标（类似 singleTask），并调用 navigate 重建返回栈（auto 模式会替换 backStack）。
2. 如何处理 Deep Link 的多个匹配？Navigation 按声明顺序匹配第一个满足条件的 NavDeepLink。
3. PendingIntent 与 Deep Link 的配合？用 NavDeepLinkBuilder 创建 PendingIntent，点击通知后导航到指定 Destination。

**面试官考察点：**
- Deep Link 的类型（显式/隐式）
- URI Template 参数解析
- PendingIntent 通知跳转场景

**易踩坑：**
- ❌ Deep Link URI 模板中参数名与 argument name 不一致导致参数获取失败
- ❌ Deep Link 后 backStack 状态不对（可能返回到桌面而非上一个页面）

---

### <a id="jetpack-32"></a>62. 条件导航与导航拦截

**标准回答：**
条件导航：根据业务状态判断是否允许跳转。实现方式：1）在 ViewModel 中判断后 navigate；2）使用 Navigation 2.4+ 的 NavigationUI 时设置条件。拦截返回键：在 Fragment 中重写 onBackPressed 或使用 OnBackPressedCallback。拦截导航操作：没有直接的"导航拦截器" API，需在调用 navigate 之前判断条件。实际项目中通常把导航逻辑集中到 Navigator 类（路由管理器），在这里做拦截判断。

**高频追问：**
1. 怎么实现"未登录拦截跳转到登录页"？在导航前检查登录状态，未登录则用另一个 action navigated 到登录页，参数传回目标页面 deep link。
2. Navigation 2.4 多重返回栈如何使用？在 NavOptions 中用 setPopUpTo 和 inclusive 清空中间栈。
3. NavController.OnDestinationChangedListener 能拦截吗？不能——它是监听器不能阻止导航，只能在导航后做后处理。

**面试官考察点：**
- 导航拦截的实际实现方式（前置判断）
- 多重返回栈管理
- Navigation 的监听 vs 拦截的区别

**易踩坑：**
- ❌ 在 OnDestinationChangedListener 中 navigate 其他页面导致导航循环
- ❌ 登录返回后没有恢复之前的导航状态

---

### <a id="jetpack-33"></a>63. 返回栈管理与多重返回栈

**标准回答：**
返回栈（Back Stack）由 NavController 内部 ArrayDeque 管理。Navigation 2.4+ 支持多重返回栈：每个底部导航 Tab 独立维护 backStack。通过 NavOptions.Builder().setPopUpTo(destinationId, inclusive=true/false) 控制。关键 API：popBackStack(destinationId, inclusive) 弹出到指定页面、popBackStack() 弹出当前页面。SaveState 参数（true）保存弹出页面的状态以便恢复。

**高频追问：**
1. Navigation 的返回栈 vs FragmentManager 的返回栈是什么关系？Navigation 的返回栈是基于 FragmentManager 的（内部用 FragmentTransaction），但独立管理 backStack 逻辑。
2. 怎么清空整个返回栈？navController.navigate(targetId, null, NavOptions.Builder().setPopUpTo(graph.startDestinationId, true).build())。
3. Configuration Change 后返回栈状态？默认保存并恢复。

**面试官考察点：**
- 多重返回栈的支持（Navigation 2.4+ 才真正支持）
- popUpTo + inclusive 的组合使用

**易踩坑：**
- ❌ Navigation 2.4 之前用多个 NavHostFragment 模拟多重返回栈
- ❌ popBackStack 传错 destinationId 导致弹了不该弹的页面

---

### <a id="jetpack-34"></a>64. NavigationUI 与底部导航集成

**标准回答：**
NavigationUI 提供了与 UI 组件集成的辅助方法。与 BottomNavigationView 集成：
```kotlin
val navController = findNavController(R.id.nav_host)
bottomNav.setupWithNavController(navController)
```
setupWithNavController 自动处理：菜单项选中→navigate 到对应 Destination；导航变化→更新选中状态；启用多重返回栈（2.4+）。要求：BottomNavigationView 的 menu item id 与 NavGraph 的 destination id 一致。

**高频追问：**
1. setupWithNavController 内部做了什么？注册 OnNavigationItemSelectedListener、添加 OnDestinationChangedListener 同步选中状态、处理 save/restore 返回栈。
2. 底部导航切换后之前的 Fragment 状态还在吗？Navigation 2.4+ 保存状态的 Fragment 仍在（多重返回栈）。
3. 如何自定义底部导航的行为？不用 setupWithNavController，手动 setOnNavigationItemSelectedListener 中调用 navController.navigate()。

**面试官考察点：**
- NavigationUI 的自动绑定原理
- menu item id = destination id 的约定

**易踩坑：**
- ❌ menu item id 与 destination id 不一致导致导航失效
- ❌ 底部导航直接 navigate 不处理 popUpTo 导致 backStack 无限增长

---

#### DataBinding（3题）

### <a id="jetpack-35"></a>65. DataBinding 的工作原理

**标准回答：**
DataBinding 通过编译时代码生成实现数据绑定。流程：1）XML 中用 <layout> + <data> 标签声明绑定变量；2）编译时 APT 生成 Binding 类（如 ActivityMainBinding），包含 View 引用和绑定逻辑；3）生成的 setVariable/executeBindings 方法将数据更新到 View；4）配合 LiveData/StateFlow 实现数据变化自动更新 UI（通过 LifecycleOwner 感知生命周期）。双向绑定 @={} 额外生成 InverseBindingListener。核心是自动生成的 BindingImpl 类的 executeBindings() 方法中完成所有属性的 set。

**高频追问：**
1. DataBinding 如何追踪数据变化？LiveData → 自动（内部 addObserver）；ObservableField → 自动；普通 POJO → 需手动 notifyPropertyChanged(BR.xxx)。
2. 生成的 BindingImpl.executeBindings 会有性能问题吗？正常场景无感。大量绑定表达式时可能有 1-2ms 方法执行时间（Profilable）。
3. DataBinding 和 ViewBinding 的代码生成有什么不同？DataBinding 生成完整的双向绑定代码；ViewBinding 只生成 findViewById 引用。

**面试官考察点：**
- 编译时代码生成机制
- executeBindings（关键方法）的作用
- LiveData/ObservableField 的自动更新原理

**易踩坑：**
- ❌ 修改了布局 XML 但 Binding 类没有重新生成（需 rebuild）
- ❌ DataBinding 中用普通 POJO 未 notifyPropertyChanged 导致 UI 不更新

---

### <a id="jetpack-36"></a>66. 双向绑定与自定义 BindingAdapter

**标准回答：**
双向绑定：android:text="@={viewModel.name}" 使用 @={}（带等号）表示数据从 ViewModel→View 同时 View→ViewModel（EditText 输入自动回写）。底层机制：InverseBindingListener 监听 View 的变化事件（如 TextWatcher），触发时调用 setter 更新 ViewModel。自定义 BindingAdapter：@BindingAdapter("attributeName") 注解静态方法，在 XML 中使用自定义属性来执行自定义绑定逻辑（如图片加载 app:imageUrl="@{url}"）。

**高频追问：**
1. @={} 和 @{} 生成代码有什么不同？@={} 额外生成 InverseBindingListener 和 InverseBindingMethod。
2. 双向绑定与 LiveData 怎么协同？LiveData 自动管理 View→ViewModel 和 ViewModel→View 的双向数据流。
3. BindingAdapter 可以重写系统属性吗？可以，但会覆盖默认绑定引起混淆，不推荐。

**面试官考察点：**
- 双向绑定的实现机制（InverseBindingListener）
- BindingAdapter 的自定义扩展能力

**易踩坑：**
- ❌ 双向绑定中 ViewModel 的属性类型和 EditText 类型不匹配
- ❌ BindingAdapter 方法未被 static 声明导致编译不通过

---

### <a id="jetpack-37"></a>67. DataBinding 与 ViewBinding 的对比

**标准回答：**
| 维度 | DataBinding | ViewBinding |
|------|-----------|------------|
| 编译速度 | 较慢（APT处理） | 较快 |
| 绑定表达式 | 支持（@{}） | 不支持 |
| 双向绑定 | 支持（@={}） | 不支持 |
| 自动更新 | LiveData/FlowObservable | 无 |
| 生成的代码量 | 大（含绑定逻辑） | 小（仅View引用） |
| Fragment内存安全 | 需要手动binding=null | 需手动binding=null |
| 最适合场景 | MVVM+复杂数据绑定 | 简单UI或无数据绑定需求 |

选择：MVVM架构→DataBinding；简单UI→ViewBinding；Compose→两者都不需要。

**高频追问：**
1. 可以在一个项目中同时用 DataBinding 和 ViewBinding 吗？可以。不同模块或同一个 Activity 选一种。
2. ViewBinding 有哪些局限性？不支持绑定表达式、不能自动更新 UI、不能配合 LiveData 使用。

**面试官考察点：**
- 两者的本质区别（数据绑定能力 vs View 引用）
- 根据场景选择正确方案

**易踩坑：**
- ❌ DataBinding 的 binding 未在 Fragment onDestroyView 中置 null 导致泄漏
- ❌ 混淆了 DataBinding 和 ViewBinding 的生成规则

---

#### WorkManager（3题）

### <a id="jetpack-38"></a>68. WorkManager 任务调度机制

**标准回答：**
WorkManager 用于执行可延迟的保证执行的后台任务。调度机制：任务信息序列化存入 Room WorkDatabase（持久化）。执行时 WorkManager 内部调度器（GreedyScheduler 进程内 + AlarmManager/JobScheduler 进程外）唤起 Worker.doWork()。API 23+ 用 JobScheduler（系统级最可靠），API 14-22 用 AlarmManager+BroadcastReceiver。Worker 的子类：Worker（同步）、CoroutineWorker（协程）、ListenableWorker（异步回调）。WorkRequest 约束通过 Constraints 配置（网络类型、充电状态、设备空闲）。

**高频追问：**
1. WorkManager 为什么用 Room 存储任务？保证进程被杀后任务不丢失，系统恢复后能重新调度。
2. GreedyScheduler 的作用？进程内调度器，当应用在前台时直接在当前进程执行任务，无需等待外部调度。
3. 唯一任务（UniqueWork）怎么用？beginUniqueWork(name, policy, request)，policy 决定冲突行为（KEEP/REPLACE/APPEND）。

**面试官考察点：**
- 持久化（Room）保证任务不丢失的核心理念
- 多种调度器的协作机制
- Worker 三种子类的选择

**易踩坑：**
- ❌ 用 WorkManager 执行即时应答的任务（应该用协程或 Foreground Service）
- ❌ Worker 中忽略 Result.success/failure 返回值

---

### <a id="jetpack-39"></a>69. 约束条件与任务重试策略

**标准回答：**
Constraints：通过网络类型（NetworkType.CONNECTED 等）、充电状态（isRequiresCharging）、设备空闲（isRequiresDeviceIdle）、电量水平（setRequiresBatteryNotLow）构建。当约束不满足时任务进入 ENQUEUED 状态等待。重试策略：BackoffPolicy.LINEAR（线性增长：每次等同一时长） / EXPONENTIAL（指数增长：10s→20s→40s→...）。setBackoffCriteria(initialDelay, policy) 设置初始延迟和策略。Result.retry() 触发重试，Result.failure() 标记最终失败不再重试。

**高频追问：**
1. 约束从"不满足"到"满足"后多久执行？取决于调度器的检测频率，通常在几秒到几分钟内。
2. LINEAR 和 EXPONENTIAL 重试策略怎么选？网络相关任务用 EXPONENTIAL（避免频繁重试）；本地任务用 LINEAR。
3. WorkRequest 超时时间怎么设？setInitialDelay + 约束超时时间。WorkManager 2.7+ 支持通过 WorkQuery 查询状态。

**面试官考察点：**
- 约束组合的实际场景（如充电+WiFi 批量上传）
- 重试策略的差异和应用

**易踩坑：**
- ❌ 设置了网络约束但未在 Worker 中预期断网时 Result.retry
- ❌ 指数增长未设置最大延迟导致重试间隔越来越长

---

### <a id="jetpack-40"></a>70. 链式任务与唯一任务

**标准回答：**
链式任务：beginWith(workA).then(workB).then(workC) 串行依赖执行，前一个成功才执行下一个（Result.success）。任意一个返回 Result.failure 则链中断。唯一任务用 beginUniqueWork(name, existingWorkPolicy, request) 确保同名任务只有一个实例。existingWorkPolicy 选项：KEEP（保留现有不添加）/REPLACE（取消旧任务创建新任务）/APPEND（追加到旧任务链）。组合使用 InputMerger 传递数据（ArrayCreatingInputMerger 合并数据为数组，OverwritingInputMerger 覆盖）。

**高频追问：**
1. 链式任务中某个 Worker 失败了能继续吗？不能，链中断。可用 Result.success(Data) 传递错误信息让下游 Worker 处理。
2. 多个 Worker 如何并发执行？给每个 Worker 单独 enqueue，它们并行执行。或使用 beginWith(workA, workB) 传入列表也并行。
3. 如何观察任务链的执行状态？WorkManager.getWorkInfosByTagLiveData(tag) 或 getWorkInfoByIdLiveData 监听状态变化。

**面试官考察点：**
- beginWith.then 链式编排
- existingWorkPolicy 的三种策略
- 输入/输出数据传递（Data/InputMerger）

**易踩坑：**
- ❌ 链式任务中一个失败后期待自动重试（不会，需要手动重新 enqueue）
- ❌ 忘记 setInputMerger 导致多个任务的输出数据被覆盖而非合并

---


## 五、网络与数据（15题）

### <a id="net-01"></a>121. OkHttp 拦截器链机制

**标准回答：**

OkHttp 的拦截器是其核心设计模式，采用责任链模式将所有请求/响应处理逻辑串联起来。拦截器分为两大类：**应用拦截器**（`addInterceptor`）和**网络拦截器**（`addNetworkInterceptor`）。应用拦截器在请求发送前最先执行、响应返回后最后执行，适合添加通用 Header、日志记录、缓存策略等；网络拦截器在真正发起网络请求时执行，可以拿到 Connection 信息、重定向详情等底层数据。

拦截器链的执行顺序为：应用拦截器 → RetryAndFollowUpInterceptor（重试与重定向） → BridgeInterceptor（桥接层，补全 Header） → CacheInterceptor（缓存处理） → ConnectInterceptor（建立连接） → 网络拦截器 → CallServerInterceptor（发起请求）。每个拦截器调用 `chain.proceed(request)` 将请求交给下一个拦截器，形成递归调用链，响应原路返回。

在自定义拦截器中，必须在 `intercept` 方法中调用 `chain.proceed(request)`，否则请求链会中断。实际项目中常用于：统一日志打印（网络监控）、Token 自动刷新（认证）、Mock 数据注入（测试）、请求加密/签名（安全）。

**高频追问：**
1. 应用拦截器和网络拦截器的核心区别是什么？应用拦截器不关心重定向和重试，只被调用一次；网络拦截器知晓底层连接细节，每次实际网络请求都会触发。
2. 拦截器中如何处理异步 Token 刷新？用 `Authenticator` 接口，当收到 401 响应时自动触发刷新逻辑并重试原请求。
3. 如果不调用 `chain.proceed()` 会怎样？直接返回自定义 Response 可做 Mock 拦截，但如既不 proceed 也不返回则会超时阻塞。

**面试官考察点：**
- OkHttp 内置拦截器链的完整理解（5 个内置拦截器）
- 自定义拦截器的实战经验
- 责任链模式的实际应用理解

**易踩坑：**
- ❌ 在拦截器中直接修改 RequestBody 导致 `Transfer-Encoding: chunked` 被移除，服务端解析失败
- ❌ 应用拦截器中获取 Connection 信息为空（只有网络拦截器能拿到）
- ❌ 拦截器内多次读取 ResponseBody，导致 stream 关闭后报 `IllegalStateException: closed`

---

### <a id="net-02"></a>122. Retrofit 动态代理原理

**标准回答：**

Retrofit 的核心是 `java.lang.reflect.Proxy` 动态代理机制。当我们调用 `retrofit.create(ApiService::class.java)` 时，Retrofit 通过 `Proxy.newProxyInstance` 创建一个代理对象，所有对接口方法的调用都会被转发到 `InvocationHandler.invoke` 中处理。

在 invoke 内部，Retrofit 执行以下流程：① 解析方法注解（`@GET`、`@POST`等）和参数注解（`@Path`、`@Query`、`@Body`等），构建 `ServiceMethod`；② 调用 `HttpServiceMethod.invoke` 创建 `OkHttpCall` 实例；③ 根据方法返回类型选择对应的 `CallAdapter` 进行适配——返回 `Call<T>` 直接用 `OkHttpCall`，返回 `suspend` 函数用 `KotlinExtensions.await`，返回 `Observable/Flowable` 用 RxJava3CallAdapterFactory；④ 执行 `Converter` 进行请求体序列化和响应体反序列化。

`CallAdapter` 和 `Converter` 是 Retrofit 两大扩展点。通过自定义 CallAdapter 可以支持 Kotlin Coroutines、RxJava、LiveData 等不同异步模型；通过自定义 Converter 可以支持 Gson、Moshi、kotlinx.serialization、ProtoBuf 等不同序列化格式。

**高频追问：**
1. Retrofit 如何处理 Kotlin 的 `suspend` 函数？内部通过 `suspendCancellableCoroutine` 挂起协程，调用 `Call.enqueue` 异步执行，回调中 `resume`/`resumeWithException`。
2. Retrofit 的 `create` 方法有性能开销吗？首次调用会反射解析注解并缓存 `ServiceMethod`，后续调用直接命中缓存，开销极低。
3. 如何给 Retrofit 添加多个 Converter？`addConverterFactory` 按添加顺序尝试，第一个能处理该类型的会被使用；通常 Gson 放在最后做 fallback。

**面试官考察点：**
- Java 动态代理原理与 Retrofit 的巧妙运用
- `CallAdapter` 和 `Converter` 两大扩展点的理解深度
- 对 Retrofit + 协程底层协作机制的掌握

**易踩坑：**
- ❌ 接口方法使用 Kotlin 的 `default` 参数但没有 `@JvmOverloads`，导致 Retrofit 无法正确解析
- ❌ `suspend` 函数返回 `Response<T>` 时忘记手动关闭 `errorBody`，导致内存泄漏
- ❌ Converter 顺序错误导致 Protobuf 接口走 Gson 解析崩溃

---

### <a id="net-03"></a>123. 协程 + Retrofit 封装的最佳实践

**标准回答：**

实际项目中对协程 + Retrofit 的封装通常遵循"三层统一"原则：**统一返回类型**、**统一异常处理**、**统一线程切换**。

统一返回类型推荐使用密封类封装：
```kotlin
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val msg: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}
```

封装层通常提供 `safeApiCall` 扩展函数，在 `Dispatchers.IO` 中执行网络请求，通过 `try-catch` 捕获 `HttpException`、`IOException`、`SocketTimeoutException` 等异常并转化为 `ApiResult.Error`。通过 `flow { emit(safeApiCall { api.getData() }) }.flowOn(Dispatchers.IO)` 将网络请求结果以 Flow 形式暴露给 ViewModel。

另一个关键实践是基于 OkHttp 拦截器的**统一鉴权 + 自动刷新 Token**：在 `Authenticator` 中检测 401 状态码，加锁避免并发刷新 Token，刷新成功后自动重试原请求。配合持久化存储将 Token 加密保存在 DataStore 中。对于多 BaseUrl 场景，使用 `@Url` 注解动态指定或配置多个 Retrofit 实例。

**高频追问：**
1. `safeApiCall` 中如何处理不同 HTTP 错误码？根据 code 区间：2xx→成功，4xx→客户端错误（业务逻辑处理），5xx→服务端错误（统一 Toast 提示）。
2. 如何防止并发刷新 Token？用 `Mutex.withLock` 或 `AtomicBoolean` 确保只有一个协程执行刷新逻辑，其余协程挂起等待。
3. `flowOn(Dispatchers.IO)` 对上游和下游的影响范围？只影响上游（之前的操作符），下游保持调用方的 Dispatcher。

**面试官考察点：**
- 对网络层架构设计的整体把握
- 统一异常处理和 Token 自动刷新的实现细节
- Flow + 协程在实际网络请求中的最佳实践

**易踩坑：**
- ❌ `safeApiCall` 中捕获 `CancellationException` 导致协程取消失效（应重新抛出）
- ❌ Token 刷新时未加锁，导致多个 401 请求同时触发多次刷新
- ❌ Retrofit 单例中 `baseUrl` 未以 `/` 结尾，导致路径拼接错误

---

### <a id="net-04"></a>124. HTTPS 证书校验与 SSL Pinning

**标准回答：**

HTTPS 的默认证书校验链为：客户端 → 操作系统内置 CA 根证书列表 → 逐级验证服务器证书链。Android 7.0+ 默认不再信任用户安装的 CA 证书，仅信任系统预置的 CA。

SSL Pinning（证书锁定）是更进一步的安全措施，将服务器证书或公钥硬编码在客户端中，只信任指定的证书。实现方式有三种：① **证书 Pinning**：验证服务器证书的 SHA-256 指纹是否匹配；② **公钥 Pinning**：验证公钥哈希（更灵活，证书更新后只要私钥/公钥不变仍然有效）；③ **Network Security Config**：Android 7.0+ 通过在 `res/xml/network_security_config.xml` 中配置 `<pin-set>` 声明，系统级别支持，无需修改代码。

OkHttp 层面可通过 `CertificatePinner.Builder().add("example.com", "sha256/xxx").build()` 实现。关键风险：如果证书意外更换而客户端未及时更新，所有已安装的旧版本 App 将无法访问服务器——必须有热更新或降级开关的兜底方案。生产环境建议使用公钥 Pinning 并固定备用公钥。

**高频追问：**
1. SSL Pinning 失效后用户怎么办？提示用户更新 App；建议保留备用 Pin（Backup Pin），同时做好崩溃降级。
2. Network Security Config 和 OkHttp CertificatePinner 能同时使用吗？可以，双层防护。但 NSC 是系统层验证，先于 OkHttp 执行。
3. 抓包工具（Charles/Fiddler）为什么能抓 HTTPS？因为安装了抓包工具的 CA 证书并被用户信任。Android 7.0+ 对 `networkSecurityConfig` 未配置 `trust-user-ca` 的应用不受影响。

**面试官考察点：**
- HTTPS 证书链验证全流程的理解
- SSL Pinning 的实现方式和工程化考量
- 对安全性与可用性平衡的思考（防抓包 vs 证书更新）

**易踩坑：**
- ❌ 只在 debug 环境配置 SSL Pinning，release 环境没配导致不安全
- ❌ 证书到期后忘记更新客户端 Pin，大批用户无法访问
- ❌ NSC 配置中 `cleartextTrafficPermitted="true"` 开放了不安全的明文传输

---

### <a id="net-05"></a>125. WebSocket 的实现与心跳机制

**标准回答：**

WebSocket 是基于 TCP 的全双工通信协议，通过一次 HTTP 握手（Upgrade 头）后升级为持久连接。Android 端主流实现方案：**OkHttp WebSocket**（推荐，复用 OkHttp 连接池和拦截器链）或 **Java-WebSocket** 库。

OkHttp 使用方式：`OkHttpClient.newWebSocket(request, listener)`，listener 回调 `onOpen`、`onMessage`、`onClosing`、`onClosed`、`onFailure`。连接管理通过 `WebSocket.close(code, reason)` 优雅关闭，传入状态码和原因。

心跳机制是 WebSocket 的工程核心。服务端通常有 idle timeout，客户端无消息一段时间后会被断开。心跳实现有两种：① **Ping-Pong**：OkHttp 原生支持，通过 `client.pingIntervalMillis` 配置自动发送 Ping 帧，收到 Pong 后重置计时；② **应用层心跳**：定时发送自定义的空消息或业务心跳包。推荐用 OkHttp 原生 Ping-Pong（RFC 6455 标准，不占用应用层消息通道）。重连策略使用指数退避（1s→2s→4s→...→max 60s），在 `onFailure` 和 `onClosed` 回调中触发。

实际封装中，使用 `Channel`/`Flow` 将 WebSocket 消息转为数据流，与 MVVM 架构无缝集成。

**高频追问：**
1. OkHttp 的 Ping 间隔设置多大合理？通常 30-60 秒，取决于服务端 idle timeout；一般设置为 timeout 的 1/3。
2. WebSocket 断线重连如何设计？指数退避延迟 + 最大重试次数 + 网络状态监听（ConnectivityManager）自动恢复。
3. WebSocket 和轮询的优劣对比？WebSocket 全双工低延迟适合实时场景（IM、行情）；轮询简单可靠适合低频更新。

**面试官考察点：**
- WebSocket 协议理解（HTTP Upgrade 握手）
- 心跳保活和断线重连的工程实践
- OkHttp 生态的深度利用

**易踩坑：**
- ❌ WebSocket 连接未在 Activity/Fragment 生命周期中正确释放，导致内存泄漏
- ❌ 重连时未限制最大次数，无限重试耗尽电量和流量
- ❌ `onFailure` 中直接重连未加延迟，网络抖动时频繁创建销毁连接

---

### <a id="net-06"></a>126. TCP 与 UDP 的区别及 Socket 编程

**标准回答：**

TCP（传输控制协议）和 UDP（用户数据报协议）是传输层的两大协议，核心差异在于可靠性 vs 实时性。

| 维度 | TCP | UDP |
|------|-----|-----|
| 连接 | 面向连接（三次握手/四次挥手） | 无连接 |
| 可靠性 | 可靠（确认、重传、序号） | 不可靠（尽最大努力交付） |
| 顺序 | 保证有序 | 不保证有序 |
| 流量控制 | 滑动窗口 + 拥塞控制 | 无 |
| 头部开销 | 20 字节 | 8 字节 |
| 适用场景 | HTTP、文件传输、邮件 | 视频直播、VoIP、DNS、IoT 传感器 |

Android 中 TCP Socket 使用 `java.net.Socket`：`Socket(host, port)` 建立连接，`getInputStream()/getOutputStream()` 读写数据。UDP 使用 `DatagramSocket` + `DatagramPacket`，无需建立连接直接发送。

关键实践：TCP Socket 通信必须在子线程执行；需要通过 `setSoTimeout` 设置读取超时防止永久阻塞；大数据传输需要自定义分帧协议（如固定长度头+变长体）。实际项目中一般使用 Netty 或 OkHttp 封装底层 Socket，很少直接操作裸 Socket。UDP 在局域网设备发现（mDNS/Bonjour）、IoT 指令下发等场景更适用。

**高频追问：**
1. TCP 为什么需要三次握手？两次握手无法防止已失效的连接请求突然到达服务端导致建立错误连接。
2. UDP 如何实现可靠传输？应用层添加序号、确认机制、重传逻辑（如 QUIC 协议、KCP 协议）。
3. Android 中使用 Socket 如何保活？设置 `SO_KEEPALIVE` + 应用层心跳包；更推荐用 WebSocket 替代裸 TCP。

**面试官考察点：**
- 传输层协议原理的扎实基础
- TCP 可靠性机制的理解（确认、重传、流量控制）
- 实际场景中协议选择的判断力

**易踩坑：**
- ❌ 在主线程执行 Socket 读写导致 ANR
- ❌ `InputStream.read()` 不设置超时，服务端无响应时永久阻塞
- ❌ TCP 粘包/拆包未处理，直接按 `read` 返回长度切包导致数据错乱

---

### <a id="net-07"></a>127. 三级缓存策略设计

**标准回答：**

图片/数据加载的三级缓存是移动端性能优化的核心手段，通常为：**内存缓存（L1）→ 磁盘缓存（L2）→ 网络（L3）**。

**内存缓存**：使用 `LruCache<String, Bitmap>`，以可用内存的 1/8 约 1/4 作为最大容量（`ActivityManager.getMemoryClass()` 获取），内部基于 `LinkedHashMap` + `accessOrder=true` 实现最近最少使用淘汰。读取速度约纳秒级，但进程被杀即丢失。

**磁盘缓存**：使用 `DiskLruCache` 将数据持久化到 `cacheDir`。JakeWharton 的 DiskLruCache 是经典实现，通过 journal 文件记录操作日志保证一致性。Glide 内部使用 `ActiveResources`（弱引用）+ `MemoryCache`（LruResourceCache）+ `DiskCache` + 网络/本地加载构成完整三级缓存。

**加载流程**：请求 → 查找内存缓存（命中则返回） → 查找磁盘缓存（命中则写入内存后返回） → 网络下载（写入磁盘+内存后返回）。关键设计点：① 缓存 Key 的生成策略（URL+尺寸+变换参数）② 内存缓存的 Bitmap 复用（`inBitmap`）③ 磁盘缓存的 LRU 淘汰和大小限制。

对于接口数据的三级缓存，可使用：内存（Map/ConcurrentHashMap）→ Room 数据库 → 网络，结合"缓存优先"或"网络优先"策略。

**高频追问：**
1. LruCache 的淘汰机制是什么？超出 `maxSize` 时从队头（最久未访问）开始移除，直到大小满足限制。
2. 为什么不直接用弱引用做内存缓存？弱引用依赖 GC 时机不确定，可能很快被回收，LruCache 强引用 + 主动控制更可靠。
3. 缓存更新策略有哪些？Cache-Aside（先查缓存再查源）、Read-Through/Write-Through（缓存层透明读写）、Write-Behind（异步写回）。

**面试官考察点：**
- 三级缓存架构的完整设计思路
- LruCache/LinkedHashMap 原理
- 实际框架（Glide/Fresco）缓存机制的了解

**易踩坑：**
- ❌ LruCache 的 `sizeOf` 方法返回错误大小，导致 bitmap 内存计算不符
- ❌ 磁盘缓存未做大小限制，长期使用后磁盘爆满
- ❌ 缓存 Key 使用 URL 直接拼接，未处理 URL 包含特殊字符导致的文件系统非法命名

---

### <a id="net-08"></a>128. 序列化方式对比：Serializable、Parcelable、Kotlin Serialization

**标准回答：**

| 维度 | Serializable | Parcelable | Kotlin Serialization |
|------|-------------|------------|---------------------|
| 原理 | Java 反射 + IO 流 | 模板代码手动写入 Parcel | 编译期生成序列化器 |
| 性能 | 慢（反射 + 大量临时对象） | 快（直接操作 Parcel） | 快（编译期代码生成） |
| 内存开销 | 高（IO 操作产生临时数组） | 低 | 低 |
| Android 场景 | 不推荐（仅适合简单对象持久化） | 进程间通信（Intent/Bundle）必备 | 网络请求 JSON、DataStore 存储 |
| 跨平台 | ✅ JVM 通用 | ❌ Android 专属 | ✅ JVM/JS/Native 全平台 |
| 代码侵入 | 实现 `Serializable` 即可 | 需实现 `describeContents`+`writeToParcel` | 添加 `@Serializable` 注解 |

**选择指南**：
- **Intent/Bundle 传参** → Parcelable（系统要求，`@Parcelize` 简化声明）
- **网络请求 JSON** → Kotlin Serialization / Moshi（跨平台 + 编译安全）
- **Room 存储复杂对象** → `@TypeConverter` + Gson 或直接用 Kotlin Serialization
- **本地文件持久化** → ProtoBuf 或 Kotlin Serialization（ProtoBuf 体积更小）
- **跨平台共享数据模型**（KMM） → Kotlin Serialization（唯一全平台支持）

Parcelable 配合 `@Parcelize` 注解（`kotlin-parcelize` 插件）可极大简化代码，只需在 `@Parcelize data class` 上标注即可自动生成所有 Parcelable 模板代码。

**高频追问：**
1. Serializable 的 `serialVersionUID` 有什么用？版本控制标识，不一致时反序列化会抛 `InvalidClassException`；显式声明可避免自动计算的不一致。
2. `@Parcelize` 的原理？编译器插件在编译期为 data class 自动生成 `Parcelable` 接口的标准实现代码。
3. Kotlin Serialization 和 Gson 的核心区别？Kotlin Serialization 编译期生成代码，不依赖反射，KMM 跨平台支持，对 Kotlin 特性（默认参数、可空类型）支持更好。

**面试官考察点：**
- 三种序列化方案的底层原理和性能差异
- 不同场景下的最优选择
- Kotlin 生态工具链的熟悉度

**易踩坑：**
- ❌ Serializable 用 `ObjectOutputStream` 写大量数据导致 OOM
- ❌ Parcelable 对象中忘记处理 `null` 值导致 `read`/`write` 不匹配崩溃
- ❌ Kotlin Serialization 未注册多态类型的 `SerializersModule` 导致反序列化失败

---

### <a id="net-09"></a>129. JSON 解析框架对比：Gson、Moshi、kotlinx.serialization

**标准回答：**

| 维度 | Gson | Moshi | kotlinx.serialization |
|------|------|-------|----------------------|
| 原理 | 运行时反射（默认） | 编译期代码生成 + 运行时反射 fallback | 编译期生成序列化器 |
| 性能 | 中等（反射有开销） | 快（代码生成） | 最快（纯编译期） |
| Kotlin 空安全 | 不保证（会忽略 null 声明） | 严格尊重 | 原生支持 |
| 默认值 | 不支持 Kotlin 默认参数 | 支持 | 原生支持 |
| 混淆 | 需要 keep 数据类 | 需要 keep 生成的 Adapter | 无需额外配置 |
| KMM 跨平台 | ❌ JVM only | ❌ JVM only | ✅ JVM/JS/Native |
| 成熟度 | 最成熟，资料最多 | 成熟，Square 维护 | 较新，JetBrains 官方 |

**选择建议**：
- 新项目 + KMM → kotlinx.serialization（官方、跨平台、性能最佳）
- 纯 Android + 已有 Square 生态 → Moshi（与 Retrofit 配合完美，`moshi-kotlin-codegen` 性能优秀）
- 旧项目维护 + Java/Android 混合 → Gson（兼容性最好，无需迁移成本）

Gson 最大的坑是：默认忽略 Kotlin 的可空类型声明，`{"name": null}` 会被解析为字段为 null 而不会报错，导致 NPE。解决方案：Gson 2.8.6+ 通过 `GsonBuilder` 配合 Kotlin 需要谨慎。Moshi 通过 `@Json(name="xxx")` 支持字段映射，`@JsonClass(generateAdapter=true)` 触发编译期代码生成。kotlinx.serialization 通过 `@SerialName` 注解映射字段，`Json { ignoreUnknownKeys = true }` 配置灵活。

**高频追问：**
1. Moshi 的 `@JsonClass(generateAdapter = true)` 必须配置吗？非必须但强烈推荐——编译期生成 Adapter 性能远优于反射。
2. 为什么 Google 在 Android 官方文档推荐 Moshi 而非 Gson？因为 Moshi 对 Kotlin 支持更好（空安全、默认参数），体积更小（不依赖反射）。
3. kotlinx.serialization 处理未知字段的策略？`ignoreUnknownKeys = true` 忽略未知字段；`false` 时抛异常（默认）。

**面试官考察点：**
- 三种框架的底层原理和性能理解
- Kotlin 特性的支持程度（空安全、默认值）
- 技术选型的判断力（不是追新，而是适合场景）

**易踩坑：**
- ❌ Gson 解析时字段为 `var` 且非 null 声明，但 JSON 中缺字段导致 NPE
- ❌ Moshi+Kotlin 使用 `@field:Json(name="xxx")` 而非 `@Json(name="xxx")` 导致注解不生效
- ❌ kotlinx.serialization 未添加 `kotlinx-serialization-json` 依赖只加了编译器插件

---

### <a id="net-10"></a>130. Android 文件存储方式对比

**标准回答：**

Android 提供多种文件存储方式，按数据特性选择：

| 方式 | 存储路径 | 可见性 | 生命周期 | 适用场景 |
|------|---------|--------|---------|---------|
| Internal Storage | `/data/data/包名/files` | 仅本应用 | 随应用卸载清除 | 敏感数据、配置文件 |
| External Storage (App-Specific) | `/sdcard/Android/data/包名/files` | 仅本应用 | 随应用卸载清除 | 大文件、缓存 |
| External Storage (Public) | `/sdcard/Download` 等 | 所有应用 | 不随卸载清除 | 用户主动保存的文件（照片、文档） |
| Cache Dir | `/data/data/包名/cache` | 仅本应用 | 系统可能清理 | 临时缓存 |
| SharedPreferences | `/data/data/包名/shared_prefs` | 仅本应用 | 随应用卸载清除 | 简单键值对 |
| DataStore | `/data/data/包名/files/datastore` | 仅本应用 | 随应用卸载清除 | 键值/对象存储（推荐替代 SP） |
| Room | `/data/data/包名/databases` | 仅本应用 | 随应用卸载清除 | 结构化数据 |

Android 10+ 的分区存储（Scoped Storage）要求访问公共目录必须使用 `MediaStore` API 或 SAF（Storage Access Framework），不能直接用文件路径。访问其他应用的文件需要 `READ_EXTERNAL_STORAGE`（Android 13 后细化为 `READ_MEDIA_*`），且 `MANAGE_EXTERNAL_STORAGE` 权限审核严格。

**核心原则**：敏感数据放 Internal Storage；大文件放 App-Specific External Storage（`getExternalFilesDir`）；用户可见的文件通过 MediaStore 或 SAF 存储；简单配置用 DataStore；结构化数据用 Room。

**高频追问：**
1. `getFilesDir()` 和 `getCacheDir()` 有什么区别？前者不会自动清理，后者系统在空间不足时可能删除。
2. SharedPreferences 为什么被 DataStore 取代？SP 在主线程读取可能导致 ANR（DataStore 用协程异步读写），SP 的 `apply()` 不保证写入时序，SP 不支持类型安全。
3. Android 10 分区存储最大的变化？无法通过文件路径直接访问公共目录，必须使用 MediaStore 或 SAF，`requestLegacyExternalStorage` 在 Android 11 中完全失效。

**面试官考察点：**
- 各种存储方式的生命周期和适用场景
- Android 10+ 分区存储适配经验
- 技术选型能力（什么数据放哪里）

**易踩坑：**
- ❌ `getExternalStorageDirectory()` 在 Android 10+ 返回不可写路径，不检查直接写导致崩溃
- ❌ `SharedPreferences.apply()` 在 `onPause` 中调用，Activity 销毁前未写入完成导致数据丢失
- ❌ 缓存文件放在 `getFilesDir()` 而非 `getCacheDir()`，不会自动清理导致空间浪费

---

### <a id="net-11"></a>131. SharedPreferences 与 DataStore

**标准回答：**

SharedPreferences（SP）是 Android 传统的轻量级键值存储方案，但存在严重缺陷，Google 官方推荐迁移到 Jetpack DataStore。

**SP 的核心问题**：
- **阻塞主线程**：`getXxx()` 在首次调用时会同步加载整个 XML 文件到内存，主线程 I/O 导致 ANR
- **线程不安全**：`apply()` 异步写入但无顺序保证，连续 `apply()` 可能后写的覆盖先写的；`commit()` 同步写入主线程风险更大
- **类型不安全**：`getString("age", "")` 明明存的是 Int 也能编译通过
- **不支持迁移**：数据结构变更时代码兼容性差
- **XML 全量写入**：每次修改都序列化整个 XML 文件

**DataStore 优势**：
- **异步 API**：基于 `Flow` 读取，`DataStore.edit { }` 在 `Dispatchers.IO` 执行写操作，永不阻塞主线程
- **一致性保证**：通过单个 `actor` 协程串行化所有写操作，保证原子性
- **异常处理**：读写过程抛出异常可被 Flow 捕获
- **类型安全**：Preferences DataStore 使用 `Preferences.Key<T>`；Proto DataStore 使用 ProtoBuf 定义 Schema，编译期类型安全
- **增量写入**：仅写入变化的键值对，非全量

**迁移策略**：使用 `SharedPreferencesMigration` 构建 DataStore 时自动导入旧 SP 数据，完成后可删除 SP 文件。`SharedPreferencesMigration` 的 `shouldRunMigration` 回调可控制是否执行迁移。

**高频追问：**
1. Preferences DataStore 和 Proto DataStore 怎么选？简单键值对用 Preferences DataStore；结构化复杂对象（用户配置、主题设置）用 Proto DataStore。
2. DataStore 有什么局限性？不支持 `getAll()` 遍历全部键（需用 `data.map{}`），不支持部分更新指定键，Schema 迁移需手动处理 `Key` 变化。
3. MMKV 对比 DataStore 如何选择？MMKV 性能更高（mmap 内存映射），微信团队出品，适合高频写入场景；DataStore 协程原生集成更好，适合标准 MVVM 架构。

**面试官考察点：**
- SP 的底层实现缺陷（XML 全量加载、apply 时序问题）的深度理解
- DataStore 异步架构和协程集成
- 实际迁移方案的经验

**易踩坑：**
- ❌ SP `apply()` 后立即 `exitProcess()` 或 `killProcess()`，数据未落盘即丢失
- ❌ DataStore 创建多个实例（不同 name）导致同时持有的内存开销
- ❌ Proto DataStore Schema 变更时未处理 `MIGRATE`，直接崩溃

---

### <a id="net-12"></a>132. ProtoBuf 序列化优势

**标准回答：**

Protocol Buffers（ProtoBuf）是 Google 推出的结构化数据序列化方案，以 `.proto` 文件定义 Schema，通过编译器生成各语言的数据类代码。

**核心优势——对比 JSON**：
| 维度 | JSON | ProtoBuf |
|------|------|----------|
| 体积 | 较大（字段名重复+文本编码） | 极小（二进制编码+字段编号而非名称） |
| 解析速度 | 慢（文本解析+字段名反射） | 快（二进制直接映射到内存结构） |
| Schema | 无强制（运行时错误） | 编译期强制（`.proto` 定义） |
| 前后兼容 | 需手动处理 | 原生支持（字段编号不变即可） |
| 可读性 | 人可读 | 不可读（二进制） |
| Kotlin 支持 | 需第三方库 | `Wire`（Square）/ `kotlinx-serialization-protobuf` |

**编码原理**：ProtoBuf 使用 Varint（可变长度整数）+ Tag-Length-Value 方式，字段编号和类型编码在 Tag 中（`field_number << 3 | wire_type`），不存储字段名，极大压缩体积。对于整数，小数字用 1 字节（如字段编号 1 int32=5 → `08 05`），Java int 始终 4 字节。

**Android 适用场景**：
- **网络请求**：服务端使用 gRPC/ProtoBuf，客户端使用 Wire 解析（比 JSON 小 60-70%）
- **本地缓存**：Room 用 ProtoBuf 序列化后存为 `@ColumnInfo(typeAffinity = BLOB)`，比 JSON 快且小
- **跨进程通信**：代替 JSON/Bundle 序列化 IPC 数据，减少 Binder 传输压力
- **DataStore**：Proto DataStore 确保类型安全 + 版本演进

**高频追问：**
1. ProtoBuf 如何保证向后兼容？新增字段用新编号，不删旧编号，只标记 `reserved`；可选字段缺失时使用默认值。
2. Wire 和 Google protobuf-java 的区别？Wire 专门为移动端设计，无反射、方法数少、体积小，更推荐 Android 使用。
3. ProtoBuf 的缺点是什么？不可读（排查问题需工具）、Schema 变更需双方协调、小数据量时优势不明显。

**面试官考察点：**
- 二进制协议和文本协议的本质差异
- Varint 编码原理和体积对比
- 实际项目中的 ProtoBuf 落地经验

**易踩坑：**
- ❌ proto 文件中字段编号 19000-19999 是保留范围，使用了导致编译失败
- ❌ ProtoBuf 的 `required` 字段在 Kotlin 中被映射为 `val`，删除该字段后旧客户端崩溃（应一律用 `optional`）
- ❌ Wire 4.x 和 3.x API 不兼容，迁移成本高

---

### <a id="net-13"></a>133. 网络安全配置（Network Security Config）

**标准回答：**

Network Security Config 是 Android 7.0+ 提供的声明式网络安全配置，通过 `res/xml/network_security_config.xml` 定义，在 `AndroidManifest.xml` 中引用：`android:networkSecurityConfig="@xml/network_security_config"`。

**核心配置项**：

```xml
<network-security-config>
  <!-- 基础配置：禁止明文流量 -->
  <base-config cleartextTrafficPermitted="false">
    <trust-anchors>
      <certificates src="system" /> <!-- 仅信任系统 CA -->
    </trust-anchors>
  </base-config>

  <!-- 特定域名配置 -->
  <domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">192.168.1.1</domain>
    <trust-anchors>
      <certificates src="system" />
      <certificates src="user" />   <!-- 调试时信任用户证书 -->
    </trust-anchors>
  </domain-config>

  <!-- SSL Pinning -->
  <domain-config>
    <domain includeSubdomains="true">api.example.com</domain>
    <pin-set expiration="2026-12-31">
      <pin digest="SHA-256">AAAA...</pin>      <!-- 主 Pin -->
      <pin digest="SHA-256">BBBB...</pin>      <!-- 备用 Pin -->
    </pin-set>
  </domain-config>

  <!-- Debug 专用配置（仅 debug 构建） -->
  <debug-overrides>
    <trust-anchors>
      <certificates src="system" />
      <certificates src="user" />   <!-- 允许 Charles/Fiddler 抓包 -->
    </trust-anchors>
  </debug-overrides>
</network-security-config>
```

**关键实践**：release 构建禁止 `cleartextTrafficPermitted="true"`；debug 构建通过 `debug-overrides` 允许抓包；证书固定设置 `expiration` 属性强制到期后更新；使用 `res/xml/network_security_config_debug.xml` + `res/xml/network_security_config_release.xml` 区分环境。

**高频追问：**
1. `debug-overrides` 在生产环境生效吗？不生效。系统自动判定 `android:debuggable="true"` 时才应用，release 构建忽略。
2. 如何配置特定域名的 HTTP（明文）访问？在该 `domain-config` 中设置 `cleartextTrafficPermitted="true"`，仅该域名生效。
3. NSC 和 OkHttp CertificatePinner 同时配置的情况下，哪个先生效？NSC 是系统层，先于 OkHttp 执行。NSC 不通过直接抛 `IOException`，不会到达 OkHttp 层。

**面试官考察点：**
- Android 7.0+ 网络安全机制的变化
- 生产环境和调试环境的配置分离
- SSL Pinning 的工程化落地

**易踩坑：**
- ❌ release 构建忘记覆盖 `debug-overrides`，生产环境允许用户证书导致中间人攻击
- ❌ Pin 的 `expiration` 设置为过去时间，所有请求直接失败
- ❌ `cleartextTrafficPermitted` 在 base-config 设置 true，整个应用不安全

---

### <a id="net-14"></a>134. DNS 优化与 HTTPDNS

**标准回答：**

DNS 解析是网络请求的第一跳，传统 LocalDNS 存在三大问题：① **域名劫持/故障**：运营商或中间设备篡改 DNS 响应；② **调度不准确**：LocalDNS 出口 IP 与用户实际 IP 不一致，导致 CDN 调度到错误节点；③ **解析延迟**：UDP 无连接不可靠，超时重试增加延迟（通常 100-500ms+）。

**HTTPDNS 原理**：不走 LocalDNS，直接通过 HTTP/HTTPS 协议向 HTTPDNS 服务器请求域名解析。请求格式：`GET /d?dn=example.com&ip=xxx HTTP/1.1`，响应返回 IP 列表和 TTL。优势：① 防劫持（HTTPS 加密传输）；② 精准调度（服务端基于请求 IP 分配最优节点）；③ 更快的解析速度（预解析 + 缓存）；④ 支持 IPv6 与双栈策略。

**Android 接入方案**：
- **阿里云 HTTPDNS**：`HttpDns.getService(context, accountId).getIpByHostAsync(host)` + OkHttp 拦截器中替换 IP
- **腾讯云 HTTPDNS**：`DnsPod.resolve(host)` + 自定义 OkHttp `Dns` 接口
- **自建方案**：实现 `okhttp3.Dns` 接口，`lookup(hostname)` 中先查本地缓存 → HTTPDNS 请求 → LocalDNS fallback

**OkHttp 集成**：自定义 `Dns` 接口，优先返回 HTTPDNS 解析的 IP 列表，失败时 fallback 到 `Dns.SYSTEM`。配合连接池和 HTTP/2 多路复用，进一步降低延迟。关键策略：缓存 TTL 内的解析结果；启动时预热预热核心域名；IP 不可用时标记该 IP 失效降级。

**高频追问：**
1. HTTPDNS 解析失败时的降级策略？HTTPDNS 失败 → 本地缓存 → LocalDNS fallback → 硬编码 IP（保底）。多级降级保证可用性。
2. HTTPDNS 如何配合 HTTPS 证书校验？HTTPDNS 只改变 IP 地址，Host 头仍为域名，证书校验不受影响。但需要在 OkHttp 中正确设置 `Host` 头。
3. IP 直连后证书域名校验失败怎么办？在 OkHttp 的 `HostnameVerifier` 中自定义校验逻辑或使用服务端证书的 SAN 包含 IP。

**面试官考察点：**
- DNS 解析链路的完整理解
- HTTPDNS 的防劫持和优化原理
- OkHttp Dns 接口的自定义实践经验

**易踩坑：**
- ❌ HTTPDNS 返回的 IP 缓存时间设置过长，IP 已不可用但仍在用
- ❌ IP 直连时未在 Host 头中设置原始域名，服务端 SNI 握手失败
- ❌ 只集成了 HTTPDNS 但忘记 WarmUp 预解析，首屏请求仍然慢

---

### <a id="net-15"></a>135. 文件上传与下载的实现方案

**标准回答：**

**文件上传**核心方案：

① **小文件**（< 10MB）：标准 `MultipartBody` + Retrofit，`@Multipart @POST @Part MultipartBody.Part`，后台直传。

② **大文件**（> 10MB）：分片上传（Multipart Upload）。分片控制在 1-5MB/片，使用随机 UUID 作为 `uploadId`，支持断点续传。流程：初始化上传获取 `uploadId` → 并发上传分片 → 完成上传（合并分片）。使用 WorkerManager 在后台持续执行，确保进程被杀后能恢复。

③ **文件预处理**：上传前计算 MD5/SHA256 哈希用于秒传（服务端已存在相同文件直接返回 URL）；图片先压缩（Luban/Compressor）；分片上传进度通过 Flow 实时监听。

**文件下载核心方案**：

① **普通下载**：`@Streaming @GET` + `ResponseBody`，`body.byteStream()` 流式写入文件，避免将整个文件加载到内存。用 OkHttp `addInterceptor` 监听 `ProgressResponseBody` 自定义进度。

② **断点续传**：通过 `Range` 头实现（`Range: bytes=500-`），客户端记录已下载字节数，中断后从断点继续。Room 数据库持久化下载状态（URL、文件路径、已下载大小、总大小、状态）。

③ **并发分片下载**：文件分成 N 个分片，协程并发下载各分片，`RandomAccessFile` 按偏移量写入对应的位置，速度提升显著（N 倍）。终端合并分片，校验 MD5。

**统一下载管理器**：封装 `DownloadManager` 处理线程管理、任务排队、状态监听、重试和持久化，通过 `LiveData`/`Flow` 暴露状态回调。

**高频追问：**
1. 分片上传中某个分片失败如何处理？仅重试失败分片，不重传全部。每个分片独立尝试，失败 N 次后标记上传失败。
2. 文件下载如何保证完整性？下载前获取文件 MD5/SHA256，下载完成后本地计算对比，不匹配则重试或分片校验。
3. Kotlin `Flow` 如何优雅实现下载进度通知？`callbackFlow { callback.onProgress{ trySend(it) } awaitClose{ /*取消监听*/ } }.flowOn(Dispatchers.IO)`。

**面试官考察点：**
- 大文件处理的经验（分片上传/下载）
- 断点续传的实现细节（Range 头、状态持久化）
- Retrofit + OkHttp 的灵活运用

**易踩坑：**
- ❌ `@Streaming` 下载时未在子线程读取 `ResponseBody`，主线程卡顿
- ❌ 下载完成后 `ResponseBody` 忘记 `close()`，内存/文件句柄泄漏
- ❌ 分片上传未做并发限制，同时 N 个分片导致 OOM 或网络拥堵

## 六、性能优化（10题）

### <a id="perf-01"></a>136. 冷启动、热启动、温启动优化

**标准回答：**

Android 启动分为三种状态（以 ActivityManagerService 进程状态为基准）：

| 类型 | 条件 | 流程 | 耗时 |
|------|------|------|------|
| **冷启动** | 进程不存在（系统杀死 / 首次启动） | 创建进程 → Application 初始化 → 创建 Activity → inflate UI → onDraw | 最长（1-5s+） |
| **温启动** | 进程存在但 Activity 被销毁（`finish`/`onBackPressed`） | 重新创建 Activity → inflate UI → onDraw | 中等 |
| **热启动** | 进程 + Activity 都在（从后台切回） | 直接 onResume，无需重新创建 | 最短（<100ms） |

**冷启动优化策略**：

① **Application.onCreate 优化**：延迟非首屏必需的第三方 SDK 初始化（`IdleHandler` 或 `ProcessLifecycleOwner.onStarted`）；使用 `App Startup` 库统一管理组件初始化，自动排序依赖，支持懒加载。异步初始化时应区分关键路径和非关键路径。

② **主题优化**：`windowBackground` 使用 `layer-list` 设置品牌背景色/Logo，替代白屏（约 100ms 首帧绘制加速）。`<item name="android:windowDisablePreview">true</item>` 关闭预览窗口（不推荐，会黑屏）。

③ **启动 Activity 优化**：Layout 扁平化减少 `inflate` 时间；使用 `ViewStub` 延迟加载非首屏布局；`AsyncLayoutInflater` 后台线程 inflate 复杂布局；对 RecyclerView 使用 `setHasFixedSize(true)` + `RecycledViewPool` 预缓存。

④ **闪屏页设计**：不要设置独立的 SplashActivity（浪费一次跳转），在主题中使用 `windowBackground` 实现品牌展示。

**启动耗时基准**：Google 官方要求冷启动 < 500ms；但实际 2s 以内可接受，建议指标 < 1.5s。

**高频追问：**
1. `App Startup` 库的原理？ContentProvider 自动初始化 + 拓扑排序依赖图；`Initializer<T>` 定义初始化逻辑，`@DependsOn` 声明依赖。
2. `IdleHandler` 是什么？MessageQueue 空闲时回调，适合初始化低优先级任务，但只能全局注册一个（可多个调用），需配合 `removeIdleHandler` 防止重复。
3. 如何量化启动耗时？`adb shell am start -W` 查看系统视角，`reportFullyDrawn()` 自定义完全绘制时机，`Trace.beginSection/endSection` 配合 Systrace 精确分析。

**面试官考察点：**
- 三种启动状态的区别和底层原理
- Application 初始化的时机管理
- 动手做过哪些启动优化及效果

**易踩坑：**
- ❌ `Application.onCreate` 中同步初始化所有 SDK，导致启动耗时 3s+
- ❌ 过度依赖多线程异步初始化，并发创建线程池反而增加 CPU 竞争
- ❌ `windowBackground` 使用大图导致首帧绘制更慢

---

### <a id="perf-02"></a>137. 内存优化全面策略

**标准回答：**

内存优化围绕"减少内存占用、避免内存泄漏、控制内存抖动"三个维度：

**1. 内存泄漏排查**：
- 常见泄漏源：Handler（非静态内部类持有 Activity 引用）、单例持有 Context（应该用 `ApplicationContext`）、未取消的 RxJava 订阅/协程、`WebView`（独立进程 + `onDestroy` 中 `removeAllViews` + `destroy`）、`Dialog/DialogFragment` dismiss 后未释放、匿名内部类持有外部引用。
- 检测工具：LeakCanary 自动监控 + Android Profiler Memory 手动分析 + MAT（Memory Analyzer Tool）分析 `.hprof` dump 文件。

**2. 内存占用优化**：
- Bitmap：使用 RGB_565（无透明需求时省 50% 内存）；`inSampleSize` 按需缩放；`inBitmap` 复用；Glide 自动管理尺寸适配。
- 数据结构：小数据量避免用 `HashMap<Long, X>`（Long 包装对象内存开销大），用 `SparseArray` / `ArrayMap` 替代；`StringBuilder` 避免大量 String 拼接。
- 缓存池：`Glide` BitmapPool 复用；`RecyclerView.RecycledViewPool` 跨 RecyclerView 共享 ViewHolder。

**3. 内存抖动（Memory Churn）**：
- 高频创建临时对象导致频繁 GC（`onDraw` 中 new Paint/new Path 等）、`for` 循环内创建对象。优化：对象复用池（`Pool`）、`SparseArray` 替代 `HashMap<Integer, X>`。

**4. 内存阈值监控**：`Runtime.getRuntime().maxMemory()` 获取 Heap 上限；`onTrimMemory(level)` 回调释放缓存（`TRIM_MEMORY_RUNNING_LOW` 减少内存使用；`TRIM_MEMORY_UI_HIDDEN` 释放 UI 资源）；`ComponentCallbacks2` 监听系统内存告警。

**高频追问：**
1. `SparseArray` 为什么比 `HashMap<Integer, T>` 省内存？采用二分查找 + 两个纯 int/Object 数组，避免了 Entry 对象和 Integer 自动装箱开销。
2. 如何分析 `hprof` 文件？Android Studio Profiler 导出 → MAT 打开 → Histogram 按类查看 → Dominator Tree 分析 Retention Size → Paths to GC Roots 找到泄漏引用链。
3. `onTrimMemory` 的级别含义？`5-TRIM_MEMORY_RUNNING_MODERATE`（内存紧张）、`10-TRIM_MEMORY_RUNNING_LOW`（低内存）、`15-TRIM_MEMORY_RUNNING_CRITICAL`（极低）、`20-TRIM_MEMORY_UI_HIDDEN`（后台，可释放 UI 资源）。

**面试官考察点：**
- 全链路内存优化思路（非仅防泄漏）
- 具体优化指标和效果
- 线上/线下问题排查能力

**易踩坑：**
- ❌ `onTrimMemory` 只处理 `UI_HIDDEN`，忽略 `RUNNING_LOW`/`RUNNING_CRITICAL`
- ❌ Bitmap 用 `ARGB_8888` 展示无需 alpha 的图片（用 RGB_565 节省 50%）
- ❌ `WeakReference` 不是银弹——弱引用对象仍可能存活足够久，不应依赖它替代主动释放

---

### <a id="perf-03"></a>138. 包体积优化方案

**标准回答：**

包体积直接影响下载转化率（每增大 6MB 转化率下降约 1%），Google Play 限制 200MB APK + 2GB AAB。

**1. 代码层面**：
- **混淆**：R8 全模式（`minifyEnabled true` + ProGuard 规则），不仅压缩还优化字节码（内联、移除无用代码、去除调试信息）
- **去除无用资源**：`shrinkResources true` 配合 Lint 分析；`res/raw` 中冗余文件手动删除
- **图片压缩**：使用 WebP（体积小 30-40%）/AVIF 格式；矢量图用 SVG（`app:srcCompat`）；TinyPNG/PNGQuant 有损压缩
- **方法数优化**：避免引入大型库（如整个 Guava 为用了一个方法）；用 `dependencies` 分析重复依赖并统一版本

**2. 资源层面**：
- **移除多语言**：`resConfigs "zh"` 只保留中文资源
- **移除无用的 ABI**：`ndk { abiFilters "arm64-v8a" }`（目前 95%+ 设备已支持 64 位）
- **移除无用密度**：`resConfigs "xxhdpi"` 或使用矢量图
- **AndResGuard**：资源混淆（短路径名），减小 apk 内文件路径字符串开销

**3. 动态化**：
- **App Bundle (AAB)**：按设备拆分，Play Store 下发仅包含该设备需要的资源/so/代码
- **动态功能模块**（Dynamic Feature Module）：按功能拆分，按需下载（Play Feature Delivery）
- **so 动态加载**：非核心 so 通过网络下发，首次使用时下载

**4. 分析工具**：Android Studio → Build → Analyze APK 查看各类文件占比；Matrix ApkChecker 分析 APK 内资源冗余；ClassyShark 检查 dex 方法引用。

**高频追问：**
1. R8 和 ProGuard 的区别？R8 是 ProGuard 的替代品，不仅混淆还做 desugaring、代码优化（内联、移除无副作用调用）、dex 合并，处理速度更快。
2. `shrinkResources` 为什么需要 `minifyEnabled`？因为资源 shrinker 依赖代码 minify 后的引用图，未 minify 时不确定哪些资源被引用。
3. AAB 和 APK 的核心差异？AAB 是发布格式（不上传 APK），Google Play 根据设备配置生成 Split APKs（base.apk + config.apk），用户只下载需要的。

**面试官考察点：**
- 全链路缩减思路
- 是否做过实际包体积优化（数据说话）
- AAB 和动态化方案的了解

**易踩坑：**
- ❌ `shrinkResources true` 但混淆规则中 `if` 反射使用资源，资源被移除导致运行时崩溃
- ❌ WebP 转换时保留原 PNG 文件，体积反而增大
- ❌ `ndk.abiFilters` 设置为 `armeabi-v7a` 未排除不支持的设备导致应用崩溃

---

### <a id="perf-04"></a>139. 电量优化策略

**标准回答：**

电量优化的核心是"减少唤醒次数、减少 CPU 持续工作时间、减少网络通信"。

**1. 网络层面**（占电量消耗 40-60%）：
- **合并请求**：批量上传/下载，减少网络模块唤醒频次
- **减少轮询**：用 WebSocket 替代定时 Polling，或使用 FCM 推送替代自建长连接轮询
- **降低频率**：后台同步交给 WorkManager，在充电+WiFi 条件下批量执行
- **减少数据传输量**：ProtoBuf 代替 JSON、图片压缩上传、增量同步

**2. WakeLock 和 Alarm**：
- 后台任务避免持有 WakeLock 超过 1 分钟（必要时用 `PARTIAL_WAKE_LOCK` + 超时设置）
- 用 `WorkManager` 替代 `AlarmManager`：WorkManager 自动合并多个任务的唤醒时段，减少设备唤醒次数
- 使用 `setExactAndAllowWhileIdle` 谨慎，尽量用 `setAndAllowWhileIdle` 允许合并

**3. CPU 使用**：
- 后台避免复杂计算（如位图处理、加密），移到前台或服务端
- 使用 `JobScheduler` 而非持续运行的 `Service`（Android 8.0+ 后台 Service 限制）
- 协程合理使用 `Dispatchers.Default` vs `Dispatchers.IO`，IO 场景不占 CPU 线程

**4. 传感器和定位**：
- 定位：降低更新频率（`setInterval` 不可过短），`PRIORITY_LOW_POWER` 优先网络定位（而非 GPS）
- 适时注销传感器监听（`onPause` 中 `unregisterListener`）
- 地理围栏（`GeofencingClient`）代替持续定位

**5. 检测工具**：Battery Historian 分析 `bugreport` 文件，显示 CPU 唤醒、WakeLock、网络、GPS 的精确时间线；Android Studio Profiler Energy 查看粗略能耗。

**高频追问：**
1. Doze 模式和 App Standby 对后台任务的影响？Doze 模式系统会自动限制网络访问和 WakeLock，WorkManager 自动适配；App Standby 限制非活跃应用的后台任务。
2. 如何监控线上用户电量消耗？通过 BatteryManager 获取电量消耗百分比（估算），结合 CPU 使用率、网络数据量、后台时长等指标综合建模。
3. WorkManager 如何减少电量消耗？任务合并（同一约束条件的多个任务一起执行）、延迟执行直到充电/WiFi、间隔时间最小 15 分钟。

**面试官考察点：**
- 电量消耗的主要来源（网络、WakeLock、CPU、传感器）
- Doze/App Standby 的理解和适配
- 实际做过哪些电量优化及效果

**易踩坑：**
- ❌ 后台 Service 持续定位每 1 秒更新，5 分钟内耗电 10%+
- ❌ `WakeLock` 未 `finally` 中释放，异常导致永不释放
- ❌ WorkManager `PeriodicWorkRequest` 最小间隔设 5 分钟（系统强制最小 15 分钟）

---

### <a id="perf-05"></a>140. 网络请求优化

**标准回答：**

**1. 连接层面**：
- **HTTP/2**（OkHttp 默认支持）：多路复用（一个 TCP 连接同时承载多个请求）、Header 压缩（HPACK）、Server Push（服务端主动推送资源）
- **连接池**：OkHttp 默认 `ConnectionPool(5, 5, TimeUnit.MINUTES)`，空闲连接复用减少握手开销
- **DNS 优化**：HTTPDNS 预解析 + 缓存（见第 134 题）
- **Keep-Alive**：持久连接复用，减少 TCP 三次握手 + TLS 握手开销

**2. 数据层面**：
- **序列化格式**：ProtoBuf（体积减少 60-70%，解析速度提升 3-5x）替代 JSON
- **Gzip/Brotli 压缩**：`Accept-Encoding: gzip, br`，OkHttp 自动解压，Android 端无需额外配置
- **增量同步**：`If-Modified-Since` / `ETag` 实现增量数据拉取，避免全量传输
- **分页加载**：后台接口一律分页返回，避免单次传输过量数据

**3. 缓存策略**：
- **HTTP 缓存**：`CacheControl` 定义 max-age/max-stale；OkHttp 内置 `Cache(cacheDir, 10*1024*1024)` 开启 HTTP 缓存
- **业务缓存**：三级缓存（内存/磁盘/网络），减少网络访问频次
- **离线策略**：优先展示缓存数据，后台异步更新（Stale-While-Revalidate）

**4. 请求管理**：
- **请求合并**：多个相似的 API 请求合并为一个（GraphQL 或自定义 BFF 层）
- **并发控制**：限制同时进行中的网络请求数量（Semaphore 或 OkHttp Dispatcher `maxRequestsPerHost`）
- **请求去重**：同一请求进行中时，后续相同请求复用之前的执行结果

**5. 弱网优化**：设置超时（`connectTimeout=10s`、`readTimeout=15s`、`writeTimeout=15s`）；自动重试（指数退避）；降级策略（大图先展示缩略图，WiFi 时再加载原图）。

**高频追问：**
1. OkHttp 的 `Dispatcher` 如何控制并发？`maxRequests` 全局最大并发（默认 64），`maxRequestsPerHost` 单域名最大并发（默认 5）。可以通过 `dispatcher.maxRequestsPerHost = 10` 提升并发。
2. HTTP/2 多路复用是如何实现的？TCP 连接上分多个 Stream，每个 Stream 有独立 ID，帧交错传输，接收端按 Stream ID 重组。
3. 弱网下如何优化用户体验？骨架屏（Skeleton Screen）先展示占位符；离线缓存优先展示旧数据；请求超时后展示本地缓存 + 错误提示。

**面试官考察点：**
- 网络请求全链路（DNS → TCP → TLS → HTTP → 反序列化）的优化点
- HTTP/2 多路复用的理解和运用
- 缓存策略和离线体验

**易踩坑：**
- ❌ OkHttp 设置 `dispatcher.maxRequests = 1` 导致所有请求串行
- ❌ HTTP 缓存 `Cache` 未在 `OkHttpClient` 中设置，但错误期待缓存生效
- ❌ 弱网下超时时间设得太短（如 3s），正常请求频繁失败

---

### <a id="perf-06"></a>141. UI 渲染优化：过度绘制与布局层级

**标准回答：**

Android 渲染性能的核心瓶颈是**过度绘制**（Overdraw）和**布局层级过深**。

**过度绘制**：屏幕上的一个像素被多次绘制（多次绘制 = 浪费 GPU 资源）。检测：开发者选项 → 调试 GPU 过度绘制。颜色含义：原色（无过度绘制）、蓝色（1x 过度绘制/可接受）、绿色（2x）、浅红（3x）、深红（4x+/需优化）。

优化手段：
- 移除不必要的背景：如果父布局已有背景，子布局无需重复设置（`android:background="@null"`）；Activity 的 `windowBackground` + 根布局 background 导致两层绘制
- `clipChildren="false"` 和 `clipToPadding="false"` 限制绘制区域
- 自定义 View 使用 `canvas.clipRect()` 限制绘制区域
- `ImageView` 使用与界面尺寸匹配的图（避免过度缩放）

**布局层级优化**：
- **ConstraintLayout** 替代多层嵌套 `LinearLayout` + `RelativeLayout`，扁平化层级（性能提升 20-40%）
- **Compose**：声明式 UI 天然扁平，没有 View 层级的概念
- **merge 标签**：根布局用 `<merge>` 减少一层 ViewGroup
- **ViewStub**：延迟加载非首屏布局，减小初始 inflate 时间
- **include 标签**：复用布局但引入层级（配合 merge 使用）

**渲染分析工具**：
- **GPU 呈现模式分析**（柱状图）：每帧 > 16ms 的绿线则可能掉帧
- **Layout Inspector**：实时查看 View 树层级和属性
- **Systrace/Perfetto**：追踪 measure/layout/draw 各阶段耗时

**高频追问：**
1. ConstraintLayout 为什么比多层 LinearLayout 快？层级扁平化减少了 `onMeasure` 和 `onLayout` 的递归遍历次数；ConstraintLayout 一次 measure pass 解决约束关系。
2. Compose 的渲染原理是什么？跳过 View 系统，直接在 Canvas 上绘制，通过重组（Recomposition）跟踪状态变化，只重组受影响的 Composable 节点。
3. 如何用 Lint 自动检测过度绘制？自定义 Lint 规则检测 `background` 重叠场景；Lint Inspections 中开启 Overdraw 相关检查。

**面试官考察点：**
- GPU 渲染管线的理解
- 过度绘制检测和优化手段
- 布局性能优化的工程实践

**易踩坑：**
- ❌ ConstraintLayout 使用 `match_parent` 导致约束计算异常（应使用 `0dp` + 约束）
- ❌ `<include>` 标签中重复设置 `<include>` 标签的 `layout_width/height` 被忽略（需同时覆盖 `android:layout_width`）
- ❌ Compose 中频繁重组：未使用 `remember` 缓存、`derivedStateOf` 提取派生状态

---

### <a id="perf-07"></a>142. Android Profiler 工具使用

**标准回答：**

Android Studio Profiler 是集成性能分析工具集，包含 CPU、Memory、Network、Energy 四个维度。

**CPU Profiler**：
- 记录方法调用轨迹（`Debug.startMethodTracing`）：Sampled（采样模式，低开销，推荐）和 Instrumented（插桩模式，精确但开销大）
- **Call Chart**：横轴时间、纵轴线程，展示方法调用的时间和嵌套关系
- **Flame Chart**：聚合展示方法调用链的 CPU 时间，宽的部分 = 耗时热点
- **Top Down / Bottom Up**：自顶向下/自底向上分析函数调用树
- 关键用途：定位主线程耗时方法、发现不合理的 IO/DB 操作、对比优化前后

**Memory Profiler**：
- 实时内存趋势图：Java/Kotlin 内存、Native 内存、Graphics（GL）、Stack、Code
- Dump Java Heap：`.hprof` 快照，导出后用 MAT 或直接分析 Class/Instance 数量和 Retained Size
- Allocation Tracking：记录一段时间内的对象分配（替代已废弃的 Allocation Tracker），定位内存抖动源头
- 关键用途：检测内存泄漏、分析内存占用热点、发现频繁 GC 原因

**Energy Profiler**：
- 粗略估计 CPU、网络、GPS 功耗占比
- 配合 Battery Historian 获取设备级精确分析

**Network Profiler**：
- 时间线 + 请求详情（URL、Method、Status、Size、Timeline）
- 连接时间线：DNS Resolution → Connection → TLS Handshake → Request Sent → Content Download
- 关键用途：发现慢请求、分析请求耗时分解、定位网络瓶颈

**高频追问：**
1. Sampled 和 Instrumented 模式的适用场景？常规分析用 Sampled（< 5% 开销）；精准调用次数分析用 Instrumented（15-30% 开销，影响性能测试）。
2. `hprof` 文件中 `Shallow Size` 和 `Retained Size` 的区别？Shallow = 对象本身占用的内存；Retained = 对象自身 + 其独占引用链上所有对象的总内存。
3. 如何用 Profiler 分析 ANR？CPU Profiler → 选择 ANR 发生时间段 → 查看主线程 Call Chart → 找到长时间执行的同步方法。

**面试官考察点：**
- 各 Profiler 的适用场景和分析方法
- 实际排查问题的经验
- `hprof` / method tracing 的分析能力

**易踩坑：**
- ❌ CPU Profiler Instrumented 模式用在线上环境导致严重卡顿
- ❌ `hprof` 不筛选直接查看全部对象，实例数太多无法定位泄漏
- ❌ 依赖 Memory Profiler 的实时图判断内存泄漏（GC 滞后会导致误判）

---

### <a id="perf-08"></a>143. Systrace 与 Perfetto 使用

**标准回答：**

Systrace 和 Perfetto 是 Android 系统级性能追踪工具，比 Profiler 更接近硬件和系统层。

**Systrace**（Android 4.1+，旧版，逐渐被 Perfetto 取代）：
- 原理：内核 ftrace 机制追踪系统事件（CPU 调度、VSYNC、Binder 调用、磁盘 I/O）
- 使用：`python systrace.py -t 10 -o trace.html sched freq gfx view wm am`
- 关键指标：每帧渲染时间（Frames 行，绿色=正常 <16ms，黄/红色=丢帧）、Binder 调用耗时、锁等待时间
- 自定义 Trace：`Trace.beginSection("MyWork")` / `Trace.endSection()` 或 `androidx.tracing.Trace`

**Perfetto**（Android 9+，推荐，替代 Systrace）：
- 更现代：Web UI（`ui.perfetto.dev`）打开 trace 文件，SQL 查询分析，支持长时间录制
- 使用：`perfetto -c - --txt -o trace.pftrace` 配置文件定义数据源（`ftrace`、`heap_profile`、`cpu` 等）
- 优势：相比 Systrace 支持更长的录制时间（小时级）、更低开销、更丰富的分析维度

**常见分析场景**：
- **丢帧分析**：Systrace 中 VSYNC 行 + Frames 行，看哪一帧超过 16ms，展开查看耗时阶段（measure/layout/draw/animation）
- **Binder 阻塞**：`binder_driver` 追踪点查看 Binder 调用耗时，配合 `aidl` 追踪点定位具体方法
- **IO 卡顿**：`f2fs/i/o` 追踪点定位主线程 IO 操作（`main` 线程不应有 IO）

**自定义 Trace 最佳实践**：`Trace.beginAsyncSection(name, cookie)` / `Trace.endAsyncSection(name, cookie)` 追踪异步操作；使用 Jetpack `Tracing` 库自动为生命周期添加 Trace。

**高频追问：**
1. Systrace 和 Perfetto 选哪个？Android 9+ 优先 Perfetto（更现代、长时间录制、SQL 查询）；旧设备用 Systrace。日常分析 Perfetto 已足够。
2. 如何分析丢帧（Jank）？Systrace 中选中丢帧的 Frame → 展开主线程 → 查看 measure/layout/draw 各阶段耗时 → 定位到具体方法或 View。
3. `Trace.beginSection` 的性能开销？极小（约几 μs），Release 版本通过 ProGuard 移除（`assumenosideeffects` 规则），不会影响性能。

**面试官考察点：**
- 系统级追踪工具的理解（ftrace 机制）
- 丢帧分析和 Binder 性能问题排查
- 自定义 Trace 的实践经验

**易踩坑：**
- ❌ 生产环境保留 Systrace 自定义 Trace 代码，影响性能（应通过 ProGuard 移除）
- ❌ Systrace 录制时间过长（>30s），trace 文件过大导致浏览器无法打开
- ❌ `beginAsyncSection` 和 `endAsyncSection` 的 cookie 参数不一致，Trace 无法配对

---

### <a id="perf-09"></a>144. Benchmark 基准测试

**标准回答：**

Android 提供 Jetpack Benchmark 库进行稳定、可重复的基准测试，避免 JIT/AOT 编译器、GC、CPU 频率变化等干扰。

**核心配置**：
```kotlin
@RunWith(AndroidJUnit4::class)
class MyBenchmark {
    @get:Rule val benchmarkRule = MacrobenchmarkRule()
    
    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        setupBlock = { pressHome() }
    ) { startActivityAndWait() }
}
```

**Microbenchmark**（方法级性能）：
- `BenchmarkRule.measureRepeated {}`：自动处理 warmup（JIT 编译期优化 + CPU 稳定）、多轮测量取中位数
- 适用场景：序列化性能对比（Gson vs Moshi vs kotlinx.serialization）、加密算法性能、集合操作性能

**Macrobenchmark**（系统级性能）：
- 测试完整用户操作流程：启动时间、列表滚动帧率、内存分配
- `StartupTimingMetric`：启动耗时（`timeToInitialDisplay` / `timeToFullDisplay`）
- `FrameTimingMetric`：帧率分析（`frameOverrunMs` 总掉帧时间）
- `TraceSectionMetric`：追踪自定义 Trace 区域

**测试原则**：
- 物理设备测试（避免模拟器的性能不准确），关闭省电模式，充满电 + 屏幕常亮
- `compilationMode = CompilationMode.Full()` 确保 AOT 编译后测试
- 多轮迭代（≥ 7 次），排除异常值，取中位数而非平均值

**高频追问：**
1. Benchmark 为什么要多次 warmup？前几次迭代 JIT 编译器还在优化代码，CPU 未达到稳定频率，数据波动大——warmup 消除这些噪声。
2. Microbenchmark 和 Macrobenchmark 的使用场景区别？Microbenchmark 测试单一方法/类性能；Macrobenchmark 测试完整用户流程性能。
3. `compilationMode.Full()` 和 `Partial()` 的区别？Full = 完整 AOT 编译（最接近用户安装后的性能）；Partial = 仅热点代码编译。测试应使用 Full。

**面试官考察点：**
- 性能测试的科学方法（warmup、多次迭代、物理设备）
- 是否实际使用过 Benchmark 库进行性能对比
- 对 JIT/AOT 编译优化的理解

**易踩坑：**
- ❌ 用模拟器运行 Benchmark（模拟器性能特征与真机天差地别）
- ❌ 单次测量直接作为结论（未 warmup、未多次迭代取中位数）
- ❌ Benchmark 测试中包含网络请求（网络延迟波动远大于代码性能差异）

---

### <a id="perf-10"></a>145. 启动任务调度框架设计

**标准回答：**

应用启动时需要初始化大量 SDK/模块，串行初始化浪费时间，全量并发可能导致 CPU 争抢。需要一个**任务调度框架**来管理初始化任务的依赖和并行。

**核心设计要素**：

① **有向无环图（DAG）编排**：每个任务用节点表示，`dependsOn` 声明依赖关系。使用拓扑排序确定执行顺序：入度为 0 的任务可以并行执行，执行完成后释放后续任务的依赖。

② **任务分类**：
- **关键路径任务**（BlockTask）：必须在首屏展示前完成（如路由、网络基础、AB 实验）
- **异步任务**（AsyncTask）：可延迟至首屏展示后执行（如日志 SDK、埋点、性能监控）
- **闲时任务**（IdleTask）：`IdleHandler` 空闲时执行（如预加载非首屏数据）

③ **执行策略**：
- 并行度控制：`ThreadPoolExecutor(coreSize = cpu核心数, maxSize = cpu核心数 * 2)`，避免无限制创建线程
- 任务超时：每个任务设置超时时间（如 5s），超时后标记失败但不阻塞后续任务
- 任务优先级：通过线程优先级（`Process.setThreadPriority()`）和 `PriorityBlockingQueue` 实现

④ **监控与统计**：
- 记录每个任务的开始时间、结束时间、耗时、线程名
- 可视化任务依赖图（DAG）和耗时瀑布图
- 线上监控任务耗时变化，发现新增的慢初始化

**主流方案**：阿里 `Alpha`（DAG + 线程池 + 任务分类）、`AndroidStartup`（Jetpack App Startup 思想演进版）、自研轻量级 DAG Scheduler。

**高频追问：**
1. 拓扑排序如何处理任务循环依赖？启动前校验 DAG 是否有环（DFS/BFS 检测），有环时直接抛异常，禁止启动。
2. 任务超时后如何处理依赖它的下游任务？标记上游任务为超时失败，下游任务检测到依赖失败后可选择：跳过、或使用默认值继续。
3. 关键路径和非关键路径如何调度？主线程串行执行关键路径任务（保证顺序）；非关键路径在子线程并行执行。

**面试官考察点：**
- 对启动流程的架构设计思考
- DAG 拓扑排序原理和工程化落地
- 对初始化过程的可观测性设计

**易踩坑：**
- ❌ 未限制并发线程数，CPU 100% 导致启动反而变慢
- ❌ 任务调度框架本身初始化成本高（如大量反射扫描），得不偿失
- ❌ 关键路径任务设了超时 1s 但实际需要 2s，正常流程频繁超时

---

## 七、测试与质量（5题）

### <a id="test-01"></a>146. 单元测试：JUnit + Mockito + MockK

**标准回答：**

Android 单元测试的核心框架组合：

**JUnit 5**：测试运行器（`@Test`、`@BeforeEach`、`@AfterEach`、`@DisplayName`、`@ParameterizedTest`）。Android 端通过 `android-junit5` Gradle 插件集成。`assertThat(actual, equalTo(expected))` 基于 Hamcrest/Truth 的断言风格。

**Mockito**（Java/Kotlin）：用于创建模拟对象（Mock），隔离被测代码的外部依赖。
```kotlin
val repo = mock(UserRepository::class.java)
`when`(repo.getUser(1)).thenReturn(User(1, "Test"))
// 验证方法调用
verify(repo).getUser(1)
verify(repo, never()).deleteUser(any())
```
核心概念：`mock`（创建模拟）、`stub`（预设行为）、`verify`（验证调用）、`argumentCaptor`（捕获参数）。Kotlin 中 Mockito 配合 `mockito-kotlin` 库（提供 `whenever`、`any` 等方便的 Kotlin API）使用。

**MockK**（Kotlin 原生，推荐）：专为 Kotlin 设计，支持挂起函数、扩展函数、私有方法 Mock。
```kotlin
val repo = mockk<UserRepository>()
coEvery { repo.getUser(1) } returns User(1, "Test")
coVerify { repo.getUser(1) }
```
核心优势：`coEvery` 原生支持 `suspend` 函数；`mockkClass` 可 Mock final class；`relaxed = true` 自动返回默认值。

**测试金字塔**：单元测试（70%）→ 集成测试（20%）→ UI 测试（10%）。Repository 和 UseCase 层是单元测试的核心目标。

**高频追问：**
1. Mockito 和 MockK 的核心区别？Mockito 基于 Java 反射，Kotlin final class 和 `suspend` 函数需要 `mockito-inline` + `@TestInstance(TestInstance.Lifecycle.PER_CLASS)` 兼容；MockK 原生 Kotlin 设计，`coEvery`/`coVerify` 直接支持挂起函数。
2. 如何测试 `viewModelScope.launch` 中的代码？使用 `kotlinx-coroutines-test` 的 `TestCoroutineDispatcher` 或 `UnconfinedTestDispatcher` 控制协程执行时机。
3. 什么是 `ArgumentCaptor`？捕获传给 Mock 方法的参数，用于验证方法调用时传入的参数值。

**面试官考察点：**
- 测试理论基础（测试金字塔、FIRST 原则）
- 实际编写单元测试的经验
- Repository/ViewModel 层的测试策略

**易踩坑：**
- ❌ 单元测试中直接创建 Retrofit 实例发网络请求（不是单元测试，是集成测试）
- ❌ `testImplementation` 依赖不小心写成 `implementation`
- ❌ Mockito 中 `any()` 和 Kotlin 可空类型冲突，返回值需要显式 null 处理

---

### <a id="test-02"></a>147. UI 测试：Espresso 与 Compose Testing

**标准回答：**

**Espresso**（View 体系 UI 测试）：
- 核心方法：`onView(matcher).perform(action).check(assertion)`
- `onView(withId(R.id.button)).perform(click()).check(matches(isDisplayed()))`
- 关键 API：`ViewMatchers`（withId、withText、isDescendantOfA）、`ViewActions`（click、typeText、swipeLeft）、`ViewAssertions`（matches、doesNotExist）
- 等待机制：自动等待主线程空闲（idling resource），`IdlingRegistry` 注册自定义空闲资源（如网络请求完成）
- RecyclerView 测试：`RecyclerViewActions.actionOnItemAtPosition` + `scrollToPosition`

**Compose Testing**（Compose UI 测试）：
- 语义树查找：`composeTestRule.onNodeWithText("Submit").performClick()`
- `composeTestRule.onNode(hasTestTag("loading_indicator")).assertIsNotDisplayed()`
- 关键 API：`SemanticsMatcher`（hasText、hasContentDescription、hasTestTag）、`SemanticsActions`（performClick、performTextInput、performScrollTo）
- `createComposeRule()` 创建测试环境

**测试原则**：
- 优先测试用户可见的行为和状态变化，而非内部实现
- 使用 `testTag` 而非字符串内容作为选择器（国际化友好）
- Espresso + MockWebServer 测试网络场景
- 截图测试（Snapshot Testing / Screenshot Testing）：`Roborazzi`（Compose）、`Paparazzi`（View/Compose 无设备）

**高频追问：**
1. Espresso 如何等待异步操作完成？`IdlingResource` 接口 → `CountingIdlingResource`（递增/递减计数）→ `IdlingRegistry.getInstance().register()`。Retrofit 和 Glide 都有内置的 IdlingResource 支持。
2. Compose 测试中 `Semantics` 节点是什么？Compose 生成的语义树（Accessibility Tree），每个可交互的 Composable 有对应的语义节点，测试框架通过它定位和操作 UI。
3. 如何测试 RecyclerView 中不可见的 Item？先用 `RecyclerViewActions.scrollToPosition(pos)` 滚动到目标位置，再执行操作。

**面试官考察点：**
- Espresso 三大组件（Matcher/Action/Assertion）的理解
- Compose Testing 与 View 测试的差异
- 实际项目中 UI 测试的覆盖策略

**易踩坑：**
- ❌ Espresso 测试中 `Thread.sleep` 等待异步操作（应用 IdlingResource 替代）
- ❌ Compose 测试未设置 `composeTestRule.mainClock.autoAdvance = false`，动画导致测试不稳定
- ❌ ActivityScenario 启动后没有 `close()`，后续测试受到干扰

---

### <a id="test-03"></a>148. CI/CD 流程设计

**标准回答：**

Android 项目的 CI/CD 流程通常基于 GitHub Actions、GitLab CI、Jenkins 或云效（Flow）构建。

**CI（持续集成）流程**：
```
Push → 静态代码检查 → 编译 → 单元测试 → UI 测试 → 构建产物上传
```

① **静态检查（Lint + Detekt + KtLint）**：PR/MR 触发，自动运行 Lint（`./gradlew lint`）、Detekt（Kotlin 代码质量）、KtLint（Kotlin 格式化）。检查不通过则 PR 标记失败，必须修复后才能合并。

② **编译验证**：`./gradlew assembleDebug` 确保全量编译通过，防止 Build Break。

③ **单元测试**：`./gradlew test` 运行所有单元测试，失败则 PR 不允许合并。配合 `jacocoTestReport` 生成覆盖率报告，设置最低通过阈值（如 60%）。

④ **UI 测试**：在 Firebase Test Lab 或自建设备集群上运行 Espresso/Compose 测试。根据变更文件匹配对应的测试套件，避免全量跑 UI 测试（耗时长）。

**CD（持续部署）流程**：
```
构建产物 → 签名 → 上传分发平台 → 通知相关人员
```

- **版本号管理**：自动递增 versionCode（`git rev-list --count HEAD`）；versionName 自动附加 `git commit hash` 和时间戳
- **多渠道打包**：`productFlavors` + `buildTypes` 矩阵，CI 并行打包
- **分发**：Debug 构建上传至内部分发平台（蒲公英/firim/App Center）；Release 构建上传至 Google Play Console（`gradle-play-publisher` 插件自动提审）
- **通知**：企业微信/钉钉/Slack Webhook 通知构建结果

**最佳实践**：使用 Gradle Build Cache 加速（`org.gradle.caching=true`）；模块化项目可仅编译变更模块避免全量编译；`--profile` / `--scan` 分析构建瓶颈。

**高频追问：**
1. 如何处理 CI 运行中的 Flaky Test？标记 `@FlakyTest` 或使用 `RetryRule` 重试机制，但必须定期修复。
2. `gradle-play-publisher` 怎么用？`apply plugin: "com.github.triplet.play"` → 配置 `serviceAccountCredentials` + `track = "internal"` → `./gradlew publishReleaseBundle` 自动上传。
3. 如何加速 CI 构建？Gradle Build Cache（远程缓存）、增量编译、仅编译变更模块、并行测试、Docker 预装 SDK/buildTools。

**面试官考察点：**
- 完整的 CI/CD 流程设计经验
- 对代码质量门禁的理解
- 实际处理过的构建问题

**易踩坑：**
- ❌ CI 中 `./gradlew clean` 每次全量编译（移除缓存，编译时长翻倍）
- ❌ UI 测试在 CI 的模拟器上运行（不稳定 + 慢），应使用 Firebase Test Lab 物理设备
- ❌ 签名文件（keystore）直接提交到 Git 仓库（严重安全风险）

---

### <a id="test-04"></a>149. 代码审查要点与规范

**标准回答：**

代码审查（Code Review）的核心目标是**知识共享、缺陷发现、规范统一**，不是挑刺。

**审查要点**：

① **设计**：架构是否合理（MVVM/MVI 分层是否清晰）；是否遵循 SOLID 原则；类/方法职责是否单一（Single Responsibility）

② **功能**：是否满足需求；边界条件处理（空值、网络失败、数据为空）；异常处理是否完整；是否引入了安全漏洞（日志打敏感信息、明文存储密码）

③ **复杂度**：方法是否过长（> 50 行需警惕）；嵌套层级是否过深（> 4 层）；是否滥用设计模式（过度设计）

④ **命名和注释**：变量/方法名是否自解释；注释是否说"为什么"而非"做了什么"；是否有临时调试代码残留（`FIXME`/`TODO`/`log`）

⑤ **性能**：主线程是否有 IO 操作、大循环、复杂计算；是否有不必要的对象分配；RecyclerView 是否设置了合适的缓存策略

⑥ **测试**：是否有对应的单元测试；测试是否覆盖了关键路径和边界条件

**审查规范**：
- **小而频繁的 PR**（< 400 行/次，审查效果最好）
- **PR 描述模板**：What（做了什么）、Why（为什么）、How（怎么做的）、Screenshot/Video（如有 UI 变更）
- **单次审查 < 60 分钟**（超时注意力下降）
- **非阻塞评论 vs 阻塞评论**：用 `nit:`（nitpick，建议性）vs 必须修改的标记区分优先级
- **Owner 机制**：核心模块（网络层、支付、登录）必须有对应 Owner 批准

**自动化辅助**：Danger（PR 规则自动化，如"添加了新文件但没测试"）、Checkstyle/Detekt/KtLint（风格检查）、Codecov（覆盖率变化）。

**高频追问：**
1. Code Review 发现严重设计问题但时间紧怎么办？补充 Issue 跟踪 + TODO 标记 + 技术债分类，不能简单放过但也不能阻塞发版。
2. 如何处理代码审查的分歧？用数据和业界实践说话；必要时拉第三人（Tech Lead）裁决；关键分歧上升为团队规范。
3. 小团队需要 Code Review 吗？需要，甚至更重要——小团队缺少文档沉淀，Code Review 是最有效的知识共享方式。

**面试官考察点：**
- 是否有 Code Review 的规范方法论
- 对代码质量的理解维度（不仅是"能不能跑"）
- 在团队中推动代码质量的经验

**易踩坑：**
- ❌ Code Review 只看代码风格不关注逻辑正确性
- ❌ PR 过大（1000 行+），审查者精力不足导致 review 流于形式
- ❌ 只提问题不给建议，变成"找茬大赛"

---

### <a id="test-05"></a>150. 崩溃监控与 APM 体系

**标准回答：**

APM（Application Performance Management）体系是产品质量的最后一道防线，核心包含**崩溃监控、性能监控、用户行为追踪**。

**崩溃监控**（Bugly/Firebase Crashlytics/Sentry）：

① **Java/Kotlin 崩溃**：`Thread.setDefaultUncaughtExceptionHandler` 捕获未被 try-catch 的异常，收集崩溃堆栈、设备信息、用户操作路径后上传，最后 `Process.killProcess(Process.myPid())` 退出应用。注意不要在主线程做耗时操作，可能导致 ANR。

② **Native 崩溃**：通过信号处理（`sigaction`）捕获 SIGSEGV、SIGABRT、SIGBUS 等信号，使用 `libunwind`/`libbacktrace` 解析 Native 堆栈。Breakpad/Crashpad 是标准化方案。

③ **OOM 监控**：监控 `Runtime.getRuntime().maxMemory()` 接近阈值时上报内存快照；`onTrimMemory(80)` 作为辅助信号；`oom_adj`/`proc/self/oom_score_adj` 系统 OOM 分值的监控。

④ **ANR 监控**：监控主线程消息队列处理耗时（`Looper.setMessageLogging` 配合自定义 Printer 检测消息处理时间 > 5s），主动记录当前所有线程堆栈。Android 13+ `getHistoricalProcessExitReasons` 获取系统记录的退出原因。

**性能监控**：
- 启动耗时（`reportFullyDrawn` + `onCreate` 前后打点）
- 页面帧率（`Choreographer.FrameCallback` + FPS 计算）
- 网络请求耗时/成功率（OkHttp EventListener 回调）
- 内存占用（定期采样子进程 MemoryInfo）

**用户行为追踪**：页面 PV/UV、关键操作埋点（点击、曝光、转化）、崩溃前用户操作路径重放。

**高频追问：**
1. Crashlytics 和 Bugly 如何选择？都成熟可靠。Crashlytics + Firebase 生态适合海外应用；Bugly 中文支持好、微信QQ同款更适合国内应用。
2. Native 崩溃的堆栈如何还原？编译时保留 `.so` 的符号表（`unstripped .so`），通过 `addr2line` 或 `ndk-stack` 将地址映射到函数名和行号。
3. 如何优雅退出应用？`Process.killProcess(Process.myPid())` + `System.exit(0)` 双保险。注意 `killProcess` 后 `exit` 确保虚拟机完全退出。

**面试官考察点：**
- 崩溃捕获的底层原理（Java/Native/信号处理）
- 完整的 APM 体系设计思路
- 线上问题排查和归因能力

**易踩坑：**
- ❌ `UncaughtExceptionHandler` 中做网络上传和数据库写入，耗时过长导致 ANR
- ❌ Native 崩溃时 `fork` 子进程处理但未限制子进程内存，导致 OOM
- ❌ ANR 监控通过 `FileObserver` 监听 `/data/anr/traces.txt` 变化（Android 11+ 应用无权访问该目录）

## 三、MVVM 架构与设计模式（20题）

### <a id="mvvm-01"></a>71. MVVM 三层职责划分

**标准回答：**

MVVM 架构将应用分为三层：**Model（数据层）**、**View（视图层）**、**ViewModel（视图模型层）**。各层职责分明，通过观察者模式实现松耦合：

**Model 层**：负责数据获取与业务逻辑，包括：
- Repository 模式封装数据源（网络、本地数据库、缓存）
- 数据实体定义（DTO、Entity、Domain Model）
- 业务规则与数据转换

```kotlin
class UserRepository(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) {
    suspend fun getUser(id: String): User {
        return localDataSource.getUser(id) ?: remoteDataSource.getUser(id)
            .also { localDataSource.saveUser(it) }
    }
}
```

**ViewModel 层**：连接 Model 与 View 的桥梁，核心职责：
- 持有 UI 状态（通过 StateFlow/LiveData 暴露）
- 处理用户交互逻辑（点击、输入等事件）
- 将 Model 数据转换为 View 可直接使用的 UI 数据
- 不持有 View 引用，不导入 Android View 相关类

```kotlin
class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun loadUser(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            userRepository.getUser(id)
                .onSuccess { _uiState.value = UserUiState(user = it) }
                .onFailure { _uiState.value = UserUiState(error = it.message) }
        }
    }
}
```

**View 层**：负责 UI 渲染和用户交互传递，包括 Activity/Fragment/Compose：
- 观察 ViewModel 的状态，驱动 UI 更新
- 将用户操作（点击、滑动）传递给 ViewModel
- 不包含业务逻辑，只做 UI 逻辑

```kotlin
class UserFragment : Fragment() {
    private val viewModel: UserViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.nameText.text = state.user?.name
                    binding.progressBar.isVisible = state.isLoading
                }
            }
        }
    }
}
```

**高频追问：**
1. ViewModel 为什么不能持有 View 引用？（会导致内存泄漏：ViewModel 生命周期长于 View，旋转屏幕时 View 被销毁重建但 ViewModel 存活）
2. 为什么推荐用 StateFlow 替代 LiveData？（StateFlow 是纯 Kotlin，不依赖 Android；支持更多操作符；测试更友好；可配合协程结构化并发）
3. ViewModel 中的数据如何保证进程杀死后恢复？（使用 SavedStateHandle，底层通过 Activity 的 onSaveInstanceState 机制持久化）

**面试官考察点：**
- 对 MVVM 三层职责的精确理解，不是"知道有这三层"就够
- 能否解释数据流向（从 Model → ViewModel → View 是单向的）
- 是否理解 ViewModel 的生命周期特性及其设计初衷

**易踩坑：**
- ❌ 在 ViewModel 中持有 Context 或 View 引用，导致 Activity 泄漏
- ❌ 把所有逻辑都塞进 ViewModel，使其变成"上帝对象"（几千行代码）
- ❌ View 层直接调用 Repository 绕过 ViewModel，破坏单向数据流

---

### <a id="mvvm-02"></a>72. Repository 模式的设计原则

**标准回答：**

Repository 模式是数据层的核心设计模式，它封装了数据访问逻辑，为上层提供统一的数据接口，屏蔽底层数据源的差异。

**核心设计原则：**

**1. 单一数据源原则**：Repository 决定数据的真实来源，外部不关心数据来自网络还是本地
```kotlin
class NewsRepository(
    private val remoteDataSource: NewsRemoteDataSource,
    private val localDataSource: NewsLocalDataSource
) {
    fun getNews(): Flow<List<News>> = flow {
        // 先返回缓存
        val cached = localDataSource.getAllNews()
        if (cached.isNotEmpty()) emit(cached)
        // 再从网络获取最新数据
        val remote = remoteDataSource.fetchNews()
        localDataSource.saveAll(remote)
        emit(remote)
    }
}
```

**2. 数据转换原则**：Repository 负责将 DTO 转换为 Domain Model
```kotlin
suspend fun getUser(id: String): User {
    val userDto = remoteDataSource.getUser(id)
    return userDto.toDomainModel() // DTO → Domain
}
```

**3. 错误处理原则**：统一处理各数据源的异常，返回 Result 类型或密封类
```kotlin
suspend fun getData(): Result<Data> = runCatching {
    withContext(Dispatchers.IO) { remoteDataSource.fetchData() }
}.recoverCatching { e ->
    withContext(Dispatchers.IO) { localDataSource.getData() }
        ?: throw e
}
```

**4. 测试友好原则**：通过接口抽象，方便单元测试时 Mock
```kotlin
class NewsRepository(private val api: NewsApi, private val dao: NewsDao) {
    // 直接依赖接口，方便替换
}
```

**高频追问：**
1. Repository 是否应该使用单例？（取决于数据源是否需要共享状态。大多数情况下配合 DI 使用单例作用域即可）
2. 多个 Repository 之间有依赖怎么处理？（通过构造函数注入，让 DI 框架管理依赖关系，避免在 Repository 内部直接 new 其他 Repository）
3. Repository 中应该返回 Flow 还是 suspend 函数？（频繁变化的数据用 Flow（如数据库监听），一次性请求用 suspend 函数）

**面试官考察点：**
- 是否真正理解了 Repository 作为"数据中介"的价值
- 对数据层架构的思考深度（离线优先、缓存策略）
- 代码可测试性的意识

**易踩坑：**
- ❌ 在 ViewModel 中直接调用 Retrofit，绕过 Repository
- ❌ Repository 中返回 DTO 而非 Domain Model，导致 ViewModel 了解后端数据结构
- ❌ 在 Repository 中处理 UI 逻辑（如 Toast、导航），越权操作

---

### <a id="mvvm-03"></a>73. Hilt 依赖注入原理与使用

**标准回答：**

Hilt 是 Google 基于 Dagger 封装的 Android 专用依赖注入框架，本质是 Dagger 的简化版。它通过注解处理器在编译期生成依赖注入代码。

**核心原理：**

Hilt 使用 Dagger 的 `@Component` 和 `@Subcomponent` 机制，为 Android 组件自动创建注入入口：

```kotlin
@HiltAndroidApp
class MyApplication : Application() // 生成 ApplicationComponent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() { // 自动注入
    @Inject lateinit var repository: UserRepository
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel()
```

**核心注解体系：**

| 注解 | 作用 | 作用域 |
|------|------|--------|
| `@HiltAndroidApp` | 触发 Application 级组件生成 | Application |
| `@AndroidEntryPoint` | 标记可注入的 Android 组件 | Activity/Fragment/View/Service/BroadcastReceiver |
| `@HiltViewModel` | 标记通过 Hilt 注入的 ViewModel | ViewModel |
| `@Inject` | 标记构造函数或字段注入点 | 任意 |
| `@Module` + `@InstallIn` | 提供无法用 @Inject 构造的依赖 | 按 Component 安装 |
| `@Provides` | 方法级别提供依赖实例 | Module 内 |
| `@Binds` | 接口-实现绑定（更高效） | Module 内 |
| `@Singleton` | 单例作用域 | Application |

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com")
        .client(client)
        .build()
}

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}
```

**高频追问：**
1. Hilt 和 Dagger 的关系是什么？什么场景还需要直接用 Dagger？（Hilt 是 Dagger 的上层封装；多模块复杂依赖图、非 Android 组件仍需 Dagger）
2. `@Binds` 和 `@Provides` 的区别？为什么 `@Binds` 更高效？（`@Binds` 直接绑定，不生成工厂类；`@Provides` 需要生成 Module 工厂类，开销更大）
3. Hilt 的作用域是怎么实现的？（通过 Dagger 的 `@Scope` 机制，Component 持有作用域内的实例，Component 销毁时实例随之销毁）

**面试官考察点：**
- 对依赖注入核心思想的理解（控制反转、依赖倒置）
- 编译期 DI（APT）vs 运行时 DI（反射）的认知
- 是否在项目中正确使用 Hilt 的作用域

**易踩坑：**
- ❌ 滥用 `@Singleton`，把所有依赖都设为单例（导致内存无法释放或并发问题）
- ❌ 忘记 `@AndroidEntryPoint` 导致注入失败（Fragment 必须依附于已标记的 Activity）
- ❌ 在 `@Provides` 方法中做耗时操作（每次注入都会执行，应延迟到使用时机）

---

### <a id="mvvm-04"></a>74. Koin vs Hilt vs Dagger 对比

**标准回答：**

三者都是 Android 生态中的依赖注入框架，但实现方式和设计哲学有本质区别：

| 维度 | Dagger | Hilt | Koin |
|------|--------|------|------|
| **原理** | 编译时 APT 生成代码 | 基于 Dagger 的 APT | 运行时反射 + DSL |
| **类型安全** | 编译期检查，100% 类型安全 | 编译期检查，100% 类型安全 | 运行时检查，错误可能延迟 |
| **性能** | 最优（零运行时开销） | 最优（零运行时开销） | 有运行时反射开销 |
| **代码量** | 大量样板代码 | 较少（注解驱动） | 最少（DSL 声明式） |
| **学习曲线** | 陡峭（Component/Module/Scope 等） | 中等（简化版 Dagger） | 平缓（纯 Kotlin DSL） |
| **错误信息** | 复杂难读懂 | 较清晰 | 清晰直观 |
| **构建速度** | 较慢（APT 处理） | 较慢（APT 处理） | 快（无编译处理） |
| **多模块支持** | 强大（组件依赖图） | 强大（继承 Dagger） | 支持但管理不如 Dagger |

**Dagger 示例（最复杂）：**
```kotlin
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}
```

**Hilt 示例（Dagger 简化版）：**
```kotlin
@HiltAndroidApp class MyApp : Application()
@AndroidEntryPoint class MainActivity : AppCompatActivity() {
    @Inject lateinit var repo: UserRepository
}
```

**Koin 示例（最简单）：**
```kotlin
val appModule = module {
    single { OkHttpClient.Builder().build() }
    single { Retrofit.Builder().client(get()).build() }
    factory { UserRepository(get()) }
}
startKoin { modules(appModule) }
// Activity 中
private val repo: UserRepository by inject()
```

**选型建议：**
- 大型项目/团队：Hilt（类型安全 + 编译检查 + 官方推荐）
- 中小项目/快速迭代：Koin（简单 + 无编译期开销感）
- 极高性能敏感：Dagger（手动控制 Component 生命周期）

**高频追问：**
1. Koin 的运行时 DI 有什么潜在风险？（依赖缺失在运行时才暴露；反射有一定性能开销；ProGuard 需特殊配置）
2. Hilt 如何实现多模块下的依赖隔离？（通过 `@InstallIn` 指定不同 Component，各模块的 Module 独立安装）
3. Koin 的 Scope 和 Hilt 的 Scope 有什么区别？（Koin Scope 是运行时概念，手动管理生命周期；Hilt 的 Scope 是编译期约束）

**面试官考察点：**
- 是否只了解一个框架，还是能横向对比
- 对编译时 vs 运行时 DI 的优劣理解
- 项目选型时的权衡决策能力

**易踩坑：**
- ❌ 在小项目中使用 Dagger（杀鸡用牛刀，样板代码量完全不值）
- ❌ 在大型项目中使用 Koin（没有编译期检查，多人协作时容易引入运行时错误）
- ❌ 同时使用多个 DI 框架（导致依赖管理混乱）

---

### <a id="mvvm-05"></a>75. 单例模式在 Android 中的安全实现

**标准回答：**

单例模式在 Android 中需特别注意**多线程安全**和**序列化安全**。推荐以下实现方式：

**1. Kotlin object（最推荐）：**
```kotlin
object AppConfig {
    var baseUrl: String = "https://api.example.com"
}
// 编译后等价于 Java 的静态内部类饿汉式，线程安全由 JVM 类加载机制保证
```

**2. Java 静态内部类模式（DCL 的优雅替代）：**
```java
public class AppConfig {
    private AppConfig() {}
    
    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }
    
    public static AppConfig getInstance() {
        return Holder.INSTANCE;
    }
}
```

**3. DCL（Double-Checked Locking）模式（Kotlin）：**
```kotlin
class AppConfig private constructor() {
    companion object {
        @Volatile
        private var instance: AppConfig? = null
        
        fun getInstance(): AppConfig {
            return instance ?: synchronized(this) {
                instance ?: AppConfig().also { instance = it }
            }
        }
    }
}
```

**4. 带 Context 的安全单例：**
```kotlin
class AppManager private constructor(private val context: Context) {
    companion object {
        @Volatile
        private var instance: AppManager? = null
        
        fun getInstance(context: Context): AppManager {
            return instance ?: synchronized(this) {
                instance ?: AppManager(context.applicationContext) // 使用 ApplicationContext
                    .also { instance = it }
            }
        }
    }
}
```

**Android 特殊考量：**
- 使用 `applicationContext` 而非 Activity Context（避免 Activity 泄漏）
- 注意序列化/反序列化的单例破坏（实现 `readResolve` 方法）
- 注意反射攻击（在构造函数中加防护）

```kotlin
// 防反射攻击
class SecureSingleton private constructor() {
    companion object {
        val INSTANCE = SecureSingleton()
    }
    
    init {
        if (INSTANCE != null) {
            throw IllegalStateException("Singleton already initialized")
        }
    }
}
```

**高频追问：**
1. `object` 的单例是懒加载还是饿汉式？（取决于访问时机，首次访问时初始化，属于懒加载 + 线程安全）
2. 为什么 DCL 中需要 `@Volatile`？（防止指令重排序：`instance = new Singleton()` 是非原子操作，可能先赋值再初始化，导致其他线程拿到未初始化的对象）
3. 静态内部类和 DCL 有什么本质区别？（静态内部类利用 JVM 类加载锁，更简洁；DCL 更灵活但易出错）

**面试官考察点：**
- 对 JVM 内存模型的理解（volatile、指令重排）
- 是否知道单例的潜在攻击面
- 对 Context 泄漏的敏感度

**易踩坑：**
- ❌ 单例中持有 Activity Context（最经典的 Android 内存泄漏）
- ❌ 使用 `synchronized` 修饰 `getInstance()` 整个方法（性能差，每次调用都加锁）
- ❌ 忘记处理 ProGuard 混淆对反射和序列化的影响

---

### <a id="mvvm-06"></a>76. 观察者模式的多种实现

**标准回答：**

观察者模式在 Android 中有多种实现方式，每种适用于不同场景：

**1. Java Observable/Observer（已废弃）：**
```java
// 不推荐：Observer 是类不是接口，且 Observable 的 setChanged 需要子类调用
```

**2. 自定义监听器接口（经典模式）：**
```kotlin
interface OnDataLoadedListener {
    fun onDataLoaded(data: List<User>)
    fun onError(error: Throwable)
}
// 使用时注意：持有强引用可能导致泄漏，需要 removeListener
```

**3. LiveData（生命周期感知）：**
```kotlin
class MyViewModel : ViewModel() {
    private val _data = MutableLiveData<List<User>>()
    val data: LiveData<List<User>> = _data
    
    fun loadData() {
        _data.value = repository.getUsers()
    }
}
// Activity 中
viewModel.data.observe(viewLifecycleOwner) { users -> updateUI(users) }
```

**4. StateFlow + collect（协程原生）：**
```kotlin
class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
}
// 收集端
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiState.collect { state -> updateUI(state) }
    }
}
```

**5. SharedFlow（一对多事件）：**
```kotlin
private val _events = MutableSharedFlow<UiEvent>()
val events = _events.asSharedFlow()

// 多个观察者都能收到事件
viewModel.events.onEach { event -> handle(event) }.launchIn(lifecycleScope)
```

**6. Flow + callbackFlow（回调转协程）：**
```kotlin
fun observeLocation(): Flow<Location> = callbackFlow {
    val callback = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            trySend(location)
        }
    }
    locationManager.requestLocationUpdates(callback)
    awaitClose { locationManager.removeUpdates(callback) }
}
```

**高频追问：**
1. LiveData 和 StateFlow 在实际使用中最大的区别是什么？（LiveData 生命周期感知，StateFlow 需要手动管理收集生命周期；StateFlow 支持更多 Flow 操作符）
2. SharedFlow 和 Channel 在事件分发中哪个更好？（SharedFlow 更适合多订阅者；Channel 适合生产者-消费者一对一模式）
3. 如何避免观察者模式的回调地狱？（使用 Flow 的链式操作、结构化并发、Result 类型封装异常）

**面试官考察点：**
- 对观察者模式在 Android 中演进的了解
- 能否根据场景选择最合适的实现
- 对生命周期安全的认知

**易踩坑：**
- ❌ 自定义 Listener 忘记 removeListener 导致内存泄漏
- ❌ LiveData 在非主线程 setValue（必须用 postValue）
- ❌ StateFlow 收集时未使用 repeatOnLifecycle，导致后台仍在处理

---

### <a id="mvvm-07"></a>77. 工厂模式与抽象工厂模式

**标准回答：**

**工厂模式（Factory Method）**：定义一个创建对象的接口，让子类决定实例化哪个类。在 Android 中广泛应用于 ViewModel 创建：

```kotlin
// 简单工厂
class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// 使用
val factory = ViewModelFactory(repository)
val viewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
```

**工厂模式在 Android 中的实际应用：**
- `ViewModelProvider.Factory`：创建带参数构造函数的 ViewModel
- `LayoutInflater.Factory2`：自定义 View 的创建
- `Fragment.instantiate()`：Fragment 实例化工厂
- `BitmapFactory`：位图解码工厂

**抽象工厂模式（Abstract Factory）**：提供一个接口用于创建一组相关或依赖的对象，无需指定具体类：

```kotlin
// 抽象工厂接口
interface UIFactory {
    fun createButton(): Button
    fun createTextField(): TextField
    fun createDialog(): Dialog
}

// Material Design 具体工厂
class MaterialUIFactory : UIFactory {
    override fun createButton() = MaterialButton()
    override fun createTextField() = MaterialTextField()
    override fun createDialog() = MaterialDialog()
}

// 调用方不关心具体实现
fun buildUI(factory: UIFactory) {
    val button = factory.createButton()
    val textField = factory.createTextField()
}
```

**Android 中的应用场景：**
- 多渠道 UI 风格切换（不同渠道不同主题）
- 白天/黑夜模式的具体实现切换
- 多种支付方式的统一创建接口

**高频追问：**
1. 简单工厂、工厂方法和抽象工厂的区别？（简单工厂：一个工厂类创建所有产品；工厂方法：每个产品有对应工厂；抽象工厂：创建产品族）
2. ViewModelProvider.Factory 是哪种工厂模式？（更接近简单工厂 + 工厂方法的混合）
3. 工厂模式与依赖注入的关系是什么？（工厂模式是手动控制创建，DI 是自动化的创建管理；两者可以配合使用）

**面试官考察点：**
- 对设计模式的理论理解和实际应用能力
- 能否识别 Android SDK 中的设计模式使用
- 对创建对象复杂性的管理思想

**易踩坑：**
- ❌ 滥用工厂模式——简单的 `new` 也用工厂包裹（过度设计）
- ❌ 抽象工厂接口设计过于庞大（应遵循接口隔离原则）
- ❌ 工厂方法的泛型使用不当导致类型安全检查失效

---

### <a id="mvvm-08"></a>78. 建造者模式（Builder）的实际应用

**标准回答：**

建造者模式（Builder Pattern）用于构造复杂对象，将**构建过程**与**表示**分离。在 Android 中是最常见的模式之一：

**1. 通知构建：**
```kotlin
val notification = NotificationCompat.Builder(context, CHANNEL_ID)
    .setSmallIcon(R.drawable.ic_notification)
    .setContentTitle("新消息")
    .setContentText("您有3条未读消息")
    .setPriority(NotificationCompat.PRIORITY_HIGH)
    .setAutoCancel(true)
    .build()
```

**2. OkHttp Request 构建：**
```kotlin
val request = Request.Builder()
    .url("https://api.example.com/data")
    .header("Authorization", "Bearer $token")
    .addHeader("Content-Type", "application/json")
    .cacheControl(CacheControl.FORCE_NETWORK)
    .build()
```

**3. 自定义 Dialog 构建器：**
```kotlin
class DialogBuilder(private val context: Context) {
    private var title: String = ""
    private var message: String = ""
    private var positiveText: String = "确定"
    private var onPositiveClick: (() -> Unit)? = null
    private var cancelable: Boolean = true

    fun setTitle(title: String) = apply { this.title = title }
    fun setMessage(message: String) = apply { this.message = message }
    fun setPositiveButton(text: String, action: () -> Unit) = apply {
        positiveText = text; onPositiveClick = action
    }

    fun build(): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { _, _ -> onPositiveClick?.invoke() }
            .setCancelable(cancelable)
            .create()
    }
}
```

**4. Kotlin DSL 风格的 Builder（推荐）：**
```kotlin
class ApiRequest {
    var url: String = ""
    var method: String = "GET"
    var headers: MutableMap<String, String> = mutableMapOf()

    companion object {
        inline fun build(block: ApiRequest.() -> Unit): ApiRequest {
            return ApiRequest().apply(block)
        }
    }
}
// 使用
val request = ApiRequest.build {
    url = "https://api.example.com"
    method = "POST"
    headers["Authorization"] = "Bearer token"
}
```

**高频追问：**
1. 建造者模式和工厂模式的核心区别？（建造者：逐步构造复杂对象，关注构建过程；工厂：一次性创建对象，关注创建结果）
2. Kotlin 中为什么推荐用命名参数 + 默认值替代 Builder？（Kotlin 命名参数同样清晰，编译器而非运行时检查；但 Java 互操作仍需 Builder）
3. 建造者模式如何保证不变性（Immutability）？（Builder 中收集参数，build() 时将参数传给不可变对象的构造函数）

**面试官考察点：**
- 能否识别 Android 框架中的 Builder 模式
- Kotlin 语法对传统 Java 模式的替代
- 对不可变对象构造的理解

**易踩坑：**
- ❌ 在 Builder 中做网络请求或耗时操作（build 应该是轻量操作）
- ❌ Builder 链太长导致单行代码难以阅读
- ❌ 忘记在 `build()` 中做参数校验（应在 build 时一次性校验）

---

### <a id="mvvm-09"></a>79. 适配器模式在 RecyclerView 中的体现

**标准回答：**

适配器模式（Adapter Pattern）的核心是**将一个接口转换为客户端期望的另一个接口**。RecyclerView.Adapter 是 Android 中最经典的适配器模式应用。

**RecyclerView.Adapter 的适配器模式分析：**

```kotlin
class UserAdapter(
    private var users: List<User> = emptyList(),
    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameText = binding.nameText
        val avatarImage = binding.avatarImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.nameText.text = user.name
        Glide.with(holder.itemView.context).load(user.avatar).into(holder.avatarImage)
        holder.itemView.setOnClickListener { onItemClick(user) }
    }

    override fun getItemCount(): Int = users.size

    // DiffUtil 驱动的高效更新
    fun submitList(newUsers: List<User>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(users, newUsers))
        users = newUsers
        diffResult.dispatchUpdatesTo(this)
    }
}
```

**适配器模式的三层映射：**
| 层级 | 适配内容 |
|------|---------|
| Adapter 本身 | 适配数据源（List）到 RecyclerView 的接口 |
| ViewHolder | 适配单个 View（ItemView）的复用和管理 |
| 数据绑定 | 适配数据对象（User）到 View 的具体属性 |

**其他适配器模式应用：**
- `ListView.Adapter` / `Spinner.Adapter`：同样原理
- `ViewPager2.Adapter`：适配 Fragment 到滑动页面
- `ListAdapter`（ListAdapter）：内置 DiffUtil 支持

**高频追问：**
1. RecyclerView.Adapter 复用了几种设计模式？（适配器 + 观察者 notifyItemChanged + ViewHolder 对象池/享元模式）
2. ListAdapter 和 RecyclerView.Adapter 有什么区别？（ListAdapter 内置了 DiffUtil 异步比对 + 列表更新在主线程的分发机制）
3. ViewHolder 的 viewType 机制是如何工作的？（通过 `getItemViewType()` 返回不同类型值，`onCreateViewHolder` 根据类型创建不同布局）

**面试官考察点：**
- 对设计模式在 Android 中实际体现的理解
- 对 RecyclerView 机制的深度掌握
- 是否知道 ListAdapter 简化 DiffUtil 的方式

**易踩坑：**
- ❌ 在 `onBindViewHolder` 中做耗时操作（绑定在主线程执行，阻塞滑动）
- ❌ 忘记取消异步加载（ViewHolder 复用时图片加载未取消）
- ❌ 在 Adapter 中持有 Activity 引用（应使用回调或传递 ViewModel）

---

### <a id="mvvm-10"></a>80. 策略模式的实际应用

**标准回答：**

策略模式（Strategy Pattern）定义一系列算法，把它们分别封装起来，使它们可以互相替换。策略模式让算法独立于使用它的客户端。

**Android 中的实际应用：**

**1. 图片加载策略切换（Glide ↔ Coil ↔ Fresco）：**
```kotlin
interface ImageLoader {
    fun load(url: String, target: ImageView)
    fun cancel(target: ImageView)
}

class GlideImageLoader : ImageLoader {
    override fun load(url: String, target: ImageView) {
        Glide.with(target.context).load(url).into(target)
    }
    override fun cancel(target: ImageView) {
        Glide.with(target.context).clear(target)
    }
}

class CoilImageLoader : ImageLoader {
    override fun load(url: String, target: ImageView) {
        target.load(url)
    }
    override fun cancel(target: ImageView) {
        target.disposeLoad()
    }
}
```

**2. 支付策略：**
```kotlin
interface PaymentStrategy {
    suspend fun pay(amount: Double): PaymentResult
}

class WeChatPayStrategy : PaymentStrategy {
    override suspend fun pay(amount: Double): PaymentResult {
        // 微信支付流程
        return wechatPayApi.pay(amount)
    }
}

class AlipayStrategy : PaymentStrategy {
    override suspend fun pay(amount: Double): PaymentResult {
        // 支付宝支付流程
        return alipayApi.pay(amount)
    }
}

class PaymentManager(private val strategy: PaymentStrategy) {
    suspend fun executePayment(amount: Double): PaymentResult {
        return strategy.pay(amount)
    }
}
```

**3. 动画插值器（Interpolator）—— SDK 自带的策略模式：**
```kotlin
// AccelerateInterpolator、DecelerateInterpolator、OvershootInterpolator 都是不同策略
animation.interpolator = AccelerateDecelerateInterpolator()
animation.interpolator = BounceInterpolator()
```

**4. 缓存策略：**
```kotlin
interface CacheStrategy {
    fun <T> get(key: String): T?
    fun <T> put(key: String, value: T)
}

class MemoryCacheStrategy : CacheStrategy { /* LruCache 实现 */ }
class DiskCacheStrategy : CacheStrategy { /* DiskLruCache 实现 */ }
class TwoLevelCacheStrategy(private val memory: CacheStrategy, private val disk: CacheStrategy) : CacheStrategy {
    override fun <T> get(key: String): T? {
        return memory.get(key) ?: disk.get(key)?.also { memory.put(key, it) }
    }
    // ...
}
```

**高频追问：**
1. 策略模式和状态模式的区别？（策略模式：客户端主动选择策略；状态模式：对象内部状态改变时自动切换行为）
2. Android 中哪些 SDK 组件使用了策略模式？（`Animation.Interpolator`、`HttpURLConnection` 的 `ContentHandler`、`RecyclerView.LayoutManager`）
3. 策略模式 + Kotlin 高阶函数可以怎么简化？（直接传 Lambda 替代接口定义：`fun pay(amount: Double, strategy: (Double) -> PaymentResult)`）

**面试官考察点：**
- 对策略模式"替换算法"核心的理解
- 能否识别代码中的策略模式应用
- 是否会用 Kotlin 特性简化传统设计模式

**易踩坑：**
- ❌ 策略过多时变成"策略地狱"（可以考虑工厂 + 策略结合）
- ❌ 每个策略内部持有大量状态（策略应该是无状态的或轻状态的）
- ❌ 在策略模式上再套一层大量 if-else（失去了模式的意义）

---

### <a id="mvvm-11"></a>81. Clean Architecture 分层架构

**标准回答：**

Clean Architecture（整洁架构）是 Robert C. Martin 提出的软件架构理念。在 Android 中，通常分为三层（或四层），核心原则是**依赖规则**：外层依赖内层，内层不依赖外层。

**Android 实现的三层结构：**

```
┌──────────────────────────────────────┐
│          Presentation Layer          │
│  Activity / Fragment / Compose       │
│  ViewModel (StateHolder)             │
└──────────────┬───────────────────────┘
               │ 依赖
┌──────────────▼───────────────────────┐
│           Domain Layer               │
│  UseCase（业务逻辑）                   │
│  Repository Interface（仓库接口）       │
│  Domain Model（领域模型）              │
└──────────────┬───────────────────────┘
               │ 依赖
┌──────────────▼───────────────────────┐
│            Data Layer                │
│  Repository Implementation           │
│  Remote DataSource（网络）            │
│  Local DataSource（本地数据库）        │
│  DTO / Entity Mapping                │
└──────────────────────────────────────┘
```

**各层具体实现：**

**Domain 层（核心，无任何 Android 依赖）：**
```kotlin
// 领域模型
data class User(val id: String, val name: String, val email: String)

// 仓库接口（在 Domain 层定义）
interface UserRepository {
    suspend fun getUser(id: String): Result<User>
    fun observeUser(id: String): Flow<User>
}

// 用例
class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String): Result<User> {
        return userRepository.getUser(id)
    }
}
```

**Data 层（实现 Domain 层接口）：**
```kotlin
// DTO
data class UserDto(val id: String, val name: String, val email: String) {
    fun toDomain(): User = User(id, name, email)
}

class UserRepositoryImpl(
    private val api: UserApi,
    private val dao: UserDao
) : UserRepository {
    override suspend fun getUser(id: String): Result<User> = runCatching {
        dao.getUser(id)?.toDomain() ?: api.getUser(id).also {
            dao.insert(it.toEntity())
        }.toDomain()
    }
}
```

**核心原则：**
- **依赖倒置**：Domain 定义接口，Data 实现接口
- **单向依赖**：外层 → 内层，绝不反向
- **隔离变化**：框架/I/O 变化不影响业务逻辑
- **可测试性**：Domain 层可纯 JVM 测试，无 Android 依赖

**高频追问：**
1. UseCase 是否必须？（简单 CRUD 可省略，直接调 Repository；有复杂业务逻辑或多处复用才需要）
2. Domain 层的 Model 和 Data 层的 DTO 一定要分开吗？（理想情况下要分开，Decoupling 换层时不互相影响；简单项目可省略但知道这是不纯粹的）
3. Clean Architecture 在小型项目中的代价是什么？（过度抽象、大量接口和转换代码、开发效率降低）

**面试官考察点：**
- 对架构设计原则的深层理解（不只是会用 MVVM）
- 是否理解依赖倒置的真正含义
- 对"过度架构"和"合理架构"的平衡判断

**易踩坑：**
- ❌ 刻板地每个 Repository 都套一个 UseCase（简单查询不需要）
- ❌ Domain 层引入了 Android 依赖（如 Context、View），破坏了内层纯净性
- ❌ 三层之间 DTO/Entity/Domain 转换导致大量样板代码又没统一处理

---

### <a id="mvvm-12"></a>82. UseCase 的设计原则

**标准回答：**

UseCase（用例/交互器）是 Clean Architecture 中 Domain 层的核心组件，封装单一业务逻辑。它位于 Presentation 和 Data 之间，代表应用的一个具体用例。

**设计原则：**

**1. 单一职责**：每个 UseCase 只做一件事
```kotlin
class GetUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): Result<User> {
        return userRepository.getUser(userId)
    }
}

class UpdateUserProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User): Result<Unit> {
        if (user.name.isBlank()) return Result.failure(IllegalArgumentException("Name required"))
        return userRepository.updateUser(user)
    }
}
```

**2. 使用 `invoke` 操作符**：让 UseCase 像函数一样调用
```kotlin
class GetUserUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(id: String): Result<User> = repo.getUser(id)
}

// ViewModel 中
val user = getUserUseCase(userId)
```

**3. 返回 Result 或密封类**：明确成功和失败
```kotlin
class LoginUseCase(private val authRepo: AuthRepository) {
    suspend operator fun invoke(credentials: Credentials): LoginResult {
        if (!credentials.isValid()) return LoginResult.InvalidInput
        return try {
            val token = authRepo.login(credentials)
            LoginResult.Success(token)
        } catch (e: Exception) {
            LoginResult.Error(e)
        }
    }
}
```

**4. 可组合**：UseCase 可以组合使用
```kotlin
class CheckoutUseCase(
    private val validateCartUseCase: ValidateCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val processPaymentUseCase: ProcessPaymentUseCase
) {
    suspend operator fun invoke(cart: Cart): Result<Order> {
        validateCartUseCase(cart).getOrElse { return Result.failure(it) }
        val order = createOrderUseCase(cart).getOrElse { return Result.failure(it) }
        return processPaymentUseCase(order)
    }
}
```

**5. 使用 Flow 观察数据变化**：
```kotlin
class ObserveUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(userId: String): Flow<User> {
        return userRepository.observeUser(userId)
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
    }
}
```

**高频追问：**
1. UseCase 和 Repository 的边界在哪里？（Repository 负责数据访问，UseCase 负责业务逻辑编排和验证；简单场景下 UseCase 可能退化为一层转发）
2. 为什么 UseCase 不能持有状态？（保持无状态便于复用和测试；状态应由 ViewModel 管理）
3. 多个 UseCase 的调用顺序怎么保证？（UseCase 组合、结构化并发、Flow 的 flatMapConcat 等操作符）

**面试官考察点：**
- 对 UseCase 真正价值的理解（不是跟风使用）
- 能否合理划分 UseCase 的粒度
- 对业务逻辑和 UI 逻辑分离的意识

**易踩坑：**
- ❌ 把所有逻辑都写成 UseCase（简单查询直接调 Repository 即可）
- ❌ UseCase 中引入 Android Context（破坏了领域层纯净性）
- ❌ 在 UseCase 中处理线程切换（`withContext` 应该在最外层或 Repository 中）

---

### <a id="mvvm-13"></a>83. 单向数据流（UDF）的设计与实践

**标准回答：**

单向数据流（Unidirectional Data Flow，UDF）是 Google 官方推荐的 Android 架构模式，核心原则：**状态向下流动，事件向上传递**。

```
┌─────────────────┐
│     UI (View)   │
│  显示 State      │
│  发送 Event      │
└────────┬────────┘
         │ Event ↑  │ State ↓
┌────────▼────────┐
│   ViewModel     │
│  处理 Event      │
│  更新 State      │
└────────┬────────┘
         │
┌────────▼────────┐
│  Data Layer     │
│  Repository     │
└─────────────────┘
```

**完整实现：**

**State 定义：**
```kotlin
data class TaskListUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterType: FilterType = FilterType.ALL
)
```

**Event 定义：**
```kotlin
sealed class TaskListEvent {
    data class AddTask(val task: Task) : TaskListEvent()
    data class RemoveTask(val id: String) : TaskListEvent()
    data class FilterChanged(val type: FilterType) : TaskListEvent()
    object Refresh : TaskListEvent()
}
```

**ViewModel — 接收 Event，产出 State：**
```kotlin
class TaskListViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.AddTask -> addTask(event.task)
            is TaskListEvent.RemoveTask -> removeTask(event.id)
            is TaskListEvent.FilterChanged -> applyFilter(event.type)
            is TaskListEvent.Refresh -> refreshTasks()
        }
    }

    private fun addTask(task: Task) {
        viewModelScope.launch {
            taskRepository.addTask(task)
            refreshTasks()
        }
    }
}
```

**View — 观察 State，发送 Event：**
```kotlin
@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TaskListContent(
        tasks = uiState.tasks,
        isLoading = uiState.isLoading,
        onAddTask = { viewModel.onEvent(TaskListEvent.AddTask(it)) },
        onRemoveTask = { viewModel.onEvent(TaskListEvent.RemoveTask(it)) }
    )
}
```

**UDF 的优势：**
- 数据流向清晰可追踪
- State 是单一数据源，UI 一致性有保证
- 易于调试（State 快照即可还原）
- 配置变更安全（ViewModel 存活）

**高频追问：**
1. UDF 和 Redux/MVI 的关系是什么？（本质相同：都是 State → UI + Event → State 的循环；MVI 更强调 Intent 概念）
2. ViewModel 中能不能有多个 StateFlow？（可以，但推荐合并为单个 UiState 数据类，保证状态变化的原子性）
3. 一次性事件（Snackbar/Navigation）在 UDF 中如何处理？（使用 Channel/SharedFlow + EventWrapper 消费一次标记）

**面试官考察点：**
- 是否理解"单向"的含义和双向绑定的区别
- 能否区分 State 和 Event 的语义
- 架构设计的全局视角

**易踩坑：**
- ❌ State 和 Event 定义混乱，边界不清
- ❌ ViewModel 暴露多个独立的 StateFlow 而非一个聚合 UiState
- ❌ View 中直接修改 State 值（应该通过 Event → ViewModel → State 路径）

---

### <a id="mvvm-14"></a>84. Android 状态管理最佳实践

**标准回答：**

Android 状态管理涉及多个维度：**UI 状态**、**一次性事件**、**持久化状态**。每种类型有对应的最佳实践：

**1. UI 状态管理（StateFlow + UDF）：**
```kotlin
// 集中式 UI 状态
data class HomeUiState(
    val banners: List<Banner> = emptyList(),
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val data = repository.getHomeData()
                _uiState.update { it.copy(
                    banners = data.banners,
                    products = data.products,
                    isLoading = false
                )}
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    errorMessage = e.message
                )}
            }
        }
    }
}
```

**2. 一次性事件管理（SharedFlow + EventWrapper）：**
```kotlin
// 确保事件只被消费一次
data class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) null else {
            hasBeenHandled = true
            content
        }
    }
}

class EventViewModel : ViewModel() {
    private val _events = MutableSharedFlow<Event<String>>()
    val events = _events.asSharedFlow()
    
    fun showMessage(msg: String) {
        viewModelScope.launch { _events.emit(Event(msg)) }
    }
}
```

**3. 配置变更状态保持（SavedStateHandle）：**
```kotlin
class SearchViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var searchQuery: String
        get() = savedStateHandle.get<String>("query") ?: ""
        set(value) = savedStateHandle.set("query", value)
}
```

**4. 持久化状态（DataStore + Room）：**
```kotlin
// DataStore 用于偏好设置
class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    val themeMode: Flow<ThemeMode> = dataStore.data.map { prefs ->
        ThemeMode.valueOf(prefs[THEME_KEY] ?: "SYSTEM")
    }
}
```

**最佳实践总结：**
| 状态类型 | 推荐方案 | 生命周期 |
|----------|---------|----------|
| UI 状态 | StateFlow + UiState 数据类 | ViewModel |
| 一次性事件 | SharedFlow + EventWrapper | ViewModel |
| 进程杀死恢复 | SavedStateHandle | ViewModel |
| 偏好设置 | DataStore | Application |
| 业务数据 | Room + Flow | Application |

**高频追问：**
1. 为什么要用 EventWrapper 而不是直接 SharedFlow？（SharedFlow 在配置变更重新订阅时可能重放事件，EventWrapper 确保一次性消费）
2. StateFlow 和 Compose 的 `mutableStateOf` 如何配合？（StateFlow 用于 ViewModel 层，`collectAsStateWithLifecycle` 转换为 Compose State）
3. 多个 ViewModel 之间的状态如何共享？（通过共享 Repository/UseCase 或父 ViewModel 的 SharedFlow）

**面试官考察点：**
- 对状态分类和生命周期的清晰理解
- 能否处理配置变更、进程杀死等极端场景
- 对事件/"副作用"的处理能力

**易踩坑：**
- ❌ 所有状态都用 StateFlow（一次性事件会被重放）
- ❌ 在 ViewModel 中保存大量大型对象（应分层管理内存）
- ❌ 忘记处理加载中、空数据、错误三种 UI 状态

---

### <a id="mvvm-15"></a>85. 事件处理：SingleLiveEvent 与 EventWrapper

**标准回答：**

事件的"一次性消费"是架构设计中的常见需求，Google 官方提供了 `SingleLiveEvent`，社区有 `EventWrapper` 等方案。

**问题场景：**
```kotlin
// LiveData 的问题：旋转屏幕后事件重放
class MyViewModel : ViewModel() {
    private val _navigateToDetail = MutableLiveData<String>()
    // Activity 旋转后，新的 observer 仍然收到上次的值
}
```

**方案一：SingleLiveEvent（官方方案，仅用一次）：**
```kotlin
class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }
}
// 局限：多个 observer 只有一个能收到事件
```

**方案二：EventWrapper（推荐）：**
```kotlin
open class Event<out T>(private val content: T) {
    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

// ViewModel 中
private val _navigationEvent = MutableSharedFlow<Event<String>>()
val navigationEvent = _navigationEvent.asSharedFlow()

fun navigateTo(id: String) {
    viewModelScope.launch {
        _navigationEvent.emit(Event(id))
    }
}
```

**方案三：Channel + receiveAsFlow（适合一对一场景）：**
```kotlin
private val _events = Channel<UiEvent>(Channel.BUFFERED)
val events = _events.receiveAsFlow()
// 缺点：只有一个收集者能收到事件，其他 observer 会丢失
```

**方案四：SharedFlow + EventWrapper（推荐，多对多）：**
```kotlin
private val _events = MutableSharedFlow<Event<UiEvent>>(
    replay = 0,
    extraBufferCapacity = 64,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
```

**方案对比：**
| 方案 | 多 Observer | 配置变更安全 | 实现复杂度 | 推荐度 |
|------|------------|-------------|-----------|--------|
| SingleLiveEvent | ❌ 只有一个收到 | ✅ | 低 | ⭐⭐ |
| Channel | ❌ 只有一个收到 | ✅ | 低 | ⭐⭐ |
| SharedFlow + EventWrapper | ✅ | ✅ | 中 | ⭐⭐⭐⭐⭐ |
| SharedFlow (无包装) | ✅ | ❌ 事件重放 | 低 | ⭐⭐⭐ |

**高频追问：**
1. 为什么 Google 废弃了 SingleLiveEvent？（线程安全问题 + 不支持多 observer + 不是正式 API，只出现在示例代码中）
2. EventWrapper 的 hasBeenHandled 在多线程下有并发问题吗？（有，但 Android 中 event 通常在主线程处理，影响不大；严谨场景可加 @Volatile 或 AtomicBoolean）
3. Compose 中如何处理一次性事件？（`LaunchedEffect` + `Channel` 或使用 `SideEffect` 处理副作用）

**面试官考察点：**
- 对事件"一次性消费"本质的理解
- 是否知道各方案的局限和适用场景
- 对架构细节的关注程度

**易踩坑：**
- ❌ 直接用 LiveData 做事件（配置变更后重放）
- ❌ 用 Channel 做多 observer 事件分发（只有一个 observer 能收到）
- ❌ EventWrapper 的 hasBeenHandled 在 ViewModel 重建后被重置

---

### <a id="mvvm-16"></a>86. MVI 架构的核心思想

**标准回答：**

MVI（Model-View-Intent）是借鉴前端框架（Cycle.js、Redux）的架构模式，核心理念：**不可变状态 + 单向数据流 + 意图驱动**。

**MVI 的核心循环：**
```
User Action → Intent → Model(ViewModel) → State → View(Render)
                              ↑                        │
                              └────── 形成闭环 ──────────┘
```

**完整 MVI 实现：**

**1. Intent（用户意图）：**
```kotlin
sealed class HomeIntent {
    object LoadInitialData : HomeIntent()
    object Refresh : HomeIntent()
    data class Search(val query: String) : HomeIntent()
    data class AddToCart(val productId: String) : HomeIntent()
}
```

**2. State（不可变状态）：**
```kotlin
data class HomeState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null
)
```

**3. SideEffect（副作用，可选）：**
```kotlin
sealed class HomeSideEffect {
    data class ShowToast(val message: String) : HomeSideEffect()
    data class NavigateToDetail(val productId: String) : HomeSideEffect()
}
```

**4. ViewModel（reduce 函数）：**
```kotlin
class HomeViewModel : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadInitialData -> loadProducts()
            HomeIntent.Refresh -> refreshProducts()
            is HomeIntent.Search -> searchProducts(intent.query)
            is HomeIntent.AddToCart -> addToCart(intent.productId)
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repository.getProducts()
                .onSuccess { products ->
                    _state.update { it.copy(products = products, isLoading = false) }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                    _sideEffect.send(HomeSideEffect.ShowToast("加载失败"))
                }
        }
    }
}
```

**MVI 的核心原则：**
1. **不可变 State**：状态变更通过 `copy()` 创建新对象
2. **单一数据流**：所有用户操作都转换为 Intent
3. **纯函数式 reduce**：`(State, Action) → State` 的转换逻辑
4. **副作用隔离**：导航、Toast 等通过 SideEffect 通道单独处理

**高频追问：**
1. MVI 和 MVVM 的根本区别？（MVI 更严格：状态不可变、意图显式声明、副作用隔离；MVVM 更灵活但可能状态管理混乱）
2. MVI 中为什么推荐不可变 State？（便于追踪状态变化、Diff 比较、时间旅行调试）
3. Orbit MVI 等库和手写 MVI 的区别？（库提供框架约束和简化 API，手写更灵活；大项目推荐用库保证一致性）

**面试官考察点：**
- 对 MVI 核心思想的理解（不只是会抄代码模板）
- 能否处理 MVI 的复杂度代价
- 对不可变数据和非确定性的认知

**易踩坑：**
- ❌ State 过度臃肿（一个页面的所有细节放一个 State 中，copy 开销大）
- ❌ Intent 粒度太细（每个点击都定义一个 Intent，数十个 sealed class 难以维护）
- ❌ 忘记处理配置变更后的事件重放

---

### <a id="mvvm-17"></a>87. MVP vs MVVM vs MVI 对比

**标准回答：**

三种架构模式各有优劣，核心差异在于**数据驱动方式**和**关注点分离程度**：

**MVP（Model-View-Presenter）：**

```kotlin
// Presenter 持有 View 接口引用
interface LoginView {
    fun showLoading()
    fun hideLoading()
    fun showError(msg: String)
    fun navigateToHome()
}

class LoginPresenter(private val view: LoginView, private val repo: AuthRepository) {
    fun login(username: String, password: String) {
        view.showLoading()
        repo.login(username, password) { result ->
            view.hideLoading()
            result.onSuccess { view.navigateToHome() }
                .onFailure { view.showError(it.message ?: "Error") }
        }
    }
    
    fun detach() { /* 释放 View 引用 */ } // 需要手动管理生命周期
}
```

**MVVM（Model-View-ViewModel）：**
```kotlin
// ViewModel 通过观察者暴露数据，不持有 View 引用
class LoginViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            repo.login(username, password)
                .onSuccess { _state.update { it.copy(isSuccess = true) } }
                .onFailure { _state.update { it.copy(error = it.message) } }
        }
    }
}
```

**MVI（Model-View-Intent）：**
```kotlin
sealed class LoginIntent {
    data class Login(val username: String, val password: String) : LoginIntent()
}

data class LoginState(val isLoading: Boolean = false, val error: String? = null)

class LoginViewModel : ViewModel() {
    fun processIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.Login -> login(intent.username, intent.password)
        }
    }
}
```

**全面对比：**

| 维度 | MVP | MVVM | MVI |
|------|-----|------|-----|
| **数据驱动** | 命令式（Presenter 调 View） | 响应式（View 观察 ViewModel） | 响应式（严格单向） |
| **View 引用** | Presenter 持有 View 接口 | ViewModel 不持有 View | ViewModel 不持有 View |
| **状态管理** | 无统一方案 | StateFlow/LiveData | 不可变 State |
| **用户操作** | 直接方法调用 | 直接方法调用 | 统一 Intent |
| **生命周期** | 手动 attach/detach | Jetpack 自动管理 | Jetpack 自动管理 |
| **学习曲线** | 低 | 中 | 高 |
| **模板代码** | 多（接口定义） | 少 | 中（Intent/State/SideEffect） |
| **调试难度** | 低 | 中 | 低（State 快照可回溯） |
| **适合场景** | 简单页面 | 中等复杂度 | 高复杂度+多人协作 |

**选型建议：**
- 简单页面 → MVP 或轻量 MVVM
- 常规业务 → MVVM（Google 官方推荐，生态最完善）
- 高复杂度/多人协作 → MVI（严格约束，减少出错）

**高频追问：**
1. MVP 中 Presenter 内存泄漏的根本原因是什么？（Presenter 持有 View 引用，View 生命周期结束但 Presenter 未释放 → 必须手动 detach）
2. 为什么 Google 从 MVP 转向了 MVVM？（ViewModel 生命周期管理自动化 + 响应式编程天然适合 Android 配置变更场景）
3. MVI 是不是 MVVM 的子集？（可以这么理解：MVI 是加了严格约束的 MVVM，更强调不可变状态和意图驱动）

**面试官考察点：**
- 是否真正用过上述架构而不只是背诵概念
- 对架构演进背后动机的理解
- 能否根据项目规模做出合理架构选择

**易踩坑：**
- ❌ 小项目使用 MVI（过度架构，开发效率低）
- ❌ MVP 中 Presenter 忘记在 onDestroy 中 detach（经典内存泄漏）
- ❌ MVVM 中 ViewModel 做了太多事，变成了 "Controller" 而非真正的 ViewModel

---

### <a id="mvvm-18"></a>88. 模块化架构设计原则

**标准回答：**

模块化是大型 Android 项目的基石，核心目标是**高内聚、低耦合**，提升编译速度、团队协作效率和代码复用率。

**模块化分层架构：**
```
:app                    ← 壳工程（组装各模块）
├── :feature:home       ← 首页模块
├── :feature:profile    ← 个人中心模块
├── :feature:search     ← 搜索模块
├── :core:network       ← 网络层
├── :core:database      ← 数据库层
├── :core:ui            ← 通用 UI 组件
├── :core:navigation    ← 路由模块
└── :core:common        ← 通用工具类
```

**核心设计原则：**

**1. 依赖规则（模块间通信）：**
```kotlin
// 通过抽象接口而非具体实现依赖
// :core:navigation 模块定义接口
interface FeatureEntry {
    fun getDestinationRoute(): String
}

// :feature:home 实现接口
@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    @IntoSet
    fun provideFeatureEntry(): FeatureEntry = object : FeatureEntry {
        override fun getDestinationRoute() = "home"
    }
}
```

**2. 路由实现（ARouter/DeepLink/Navigation）：**
```kotlin
// 通过路由跳转，避免模块间直接依赖
sealed class AppRoute(val path: String) {
    object Home : AppRoute("app://home")
    object Profile : AppRoute("app://profile/{userId}")
    data class Detail(val id: String) : AppRoute("app://detail/$id")
}
```

**3. 通信方式：**
```
高层模块 (app) ──→ 直接依赖 feature 模块
Feature 间 ──→ 通过路由 + 接口抽象（不直接依赖）
Feature → Core ──→ 依赖注入
Core 间 ──→ 最小化依赖（network 不依赖 database）
```

**4. 模块类型：**

| 类型 | 打包方式 | 可独立运行 | 示例 |
|------|---------|-----------|------|
| Application | APK/AAB | ✅ | :app |
| Feature | AAR/Library | ✅ (有独立壳) | :feature:home |
| Core Library | AAR/Library | ❌ | :core:network |
| Shared/Common | AAR/Library | ❌ | :core:common |

**5. config.gradle 统一管理依赖版本：**
```gradle
ext {
    version = [kotlin: "1.9.0", compose: "1.5.0"]
    deps = [retrofit: "com.squareup.retrofit2:retrofit:$version.retrofit"]
}
```

**高频追问：**
1. 模块化后如何解决资源名冲突？（使用模块前缀 `@string/feature_home_title`，或开启 Gradle `resourcePrefix`）
2. 模块间数据如何共享？（通过 `:core:common` 模块定义公共 Model/接口，各 feature 依赖 common）
3. 模块化对编译速度的实际影响？（变化集中在少数模块时显著加速；改动底层的 core 模块会导致大面积重编）

**面试官考察点：**
- 对大型项目架构的理解深度
- 是否有真实的模块化落地经验
- 对依赖管理和隔离的思考

**易踩坑：**
- ❌ 模块划分过细（50+ 个模块，维护成本剧增）
- ❌ Feature 模块间直接依赖（本质还是单体）
- ❌ 所有模块都依赖 common，导致 common 越来越臃肿

---

### <a id="mvvm-19"></a>89. 依赖倒置原则（DIP）在 Android 中的应用

**标准回答：**

依赖倒置原则（Dependency Inversion Principle）是 SOLID 中的 D，核心定义：
1. **高层模块不应依赖低层模块，两者都应依赖抽象**
2. **抽象不应依赖细节，细节应依赖抽象**

**Android 中的典型应用：**

**1. Repository 接口定义在 Domain 层（而非 Data 层）：**
```kotlin
// Domain 层（高层）定义接口
interface UserRepository {
    suspend fun getUser(id: String): User
}

// Data 层（低层）实现接口
class UserRepositoryImpl(
    private val api: UserApi,
    private val dao: UserDao
) : UserRepository {
    override suspend fun getUser(id: String): User {
        // 具体实现
    }
}
// 关键：Domain 层完全不知道 Data 层的具体实现方式
```

**2. ViewModel 依赖接口而非实现：**
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository // 依赖接口，不是实现类
) : ViewModel()
```

**3. 数据源抽象：**
```kotlin
// 定义抽象数据源
interface RemoteDataSource<T> {
    suspend fun fetch(): T
    suspend fun save(data: T)
}

interface LocalDataSource<T> {
    fun observe(): Flow<T>
    suspend fun get(): T?
    suspend fun save(data: T)
}

// 具体实现可以随时替换
class RetrofitNewsDataSource : RemoteDataSource<List<News>> { /* Retrofit 实现 */ }
class RoomNewsDataSource : LocalDataSource<News> { /* Room 实现 */ }
```

**4. 图片加载库替换（DIP 实践）：**
```kotlin
// 定义抽象
interface ImageLoader {
    fun load(url: String, into: ImageView, placeholder: Int = 0)
    fun preload(urls: List<String>)
}

// 不会出现 ViewModel 直接依赖 Glide
// class MyViewModel(private val glide: Glide) ← 违反 DIP

// 正确做法：
class MyViewModel(private val imageLoader: ImageLoader) // 依赖抽象
```

**DIP 的优势：**
- 换库不换业务代码（Glide → Coil 只需换实现）
- 单元测试时轻松 Mock
- 团队并行开发（定义接口 → 各自实现）

**高频追问：**
1. DIP 和 DI（依赖注入）的关系？（DIP 是原则/思想，DI 是实现 DIP 的一种技术手段）
2. 违反 DIP 会导致什么具体问题？（底层修改导致高层崩溃；无法替换实现（如换图片库）；单元测试困难）
3. Android Clean Architecture 中 DIP 体现在哪里？（Domain 定义接口，Data 实现接口，是 DIP 的完美体现）

**面试官考察点：**
- 对 SOLID 原则在 Android 中实际落地的理解
- 是否有面向接口编程的习惯
- 架构设计中对变化隔离的考量

**易踩坑：**
- ❌ "为了接口而接口"——简单且不会变化的功能不需要抽象
- ❌ 接口定义太细，一个类对应一个接口（接口膨胀）
- ❌ 接口定义在实现模块中（抽象定义在低层，本质上没有倒置依赖）

---

### <a id="mvvm-20"></a>90. Android 中的组合优于继承实践

**标准回答：**

"组合优于继承"是 GoF 设计模式的核心原则之一。在 Android 开发中，Kotlin 的**委托**特性让组合变得更加自然和强大。

**继承的问题：**
```kotlin
// 继承的典型问题示例
open class BaseActivity : AppCompatActivity() {
    fun showLoading() { /* ... */ }
    fun hideLoading() { /* ... */ }
    fun showError(message: String) { /* ... */ }
}

class UserActivity : BaseActivity()
class ProfileActivity : BaseActivity()
// 问题1：强耦合 — 所有子类被迫继承所有方法
// 问题2：Base 膨胀 — 随着需求增多，Base 越来越臃肿
// 问题3：多重继承不支持 — 无法同时复用多个基类的能力
```

**组合 + 委托的解决方案：**

**1. 接口 + 委托实现（经典组合）：**
```kotlin
interface LoadingView {
    fun showLoading()
    fun hideLoading()
}

class LoadingViewDelegate : LoadingView {
    private var dialog: ProgressDialog? = null
    private lateinit var context: Context
    
    fun attach(context: Context) { this.context = context }
    
    override fun showLoading() {
        dialog = ProgressDialog(context).apply { show() }
    }
    override fun hideLoading() {
        dialog?.dismiss()
    }
}

// 使用时组合
class UserActivity : AppCompatActivity(), LoadingView by LoadingViewDelegate() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this as LoadingViewDelegate).attach(this) // 委托对象初始化
        showLoading()
    }
}
```

**2. 自定义 View 的组合优于继承：**
```kotlin
// ❌ 继承方式
class RoundedImageView : AppCompatImageView() {
    // 自定义圆角逻辑与 ImageView 强耦合
}

// ✅ 组合方式
class RoundedImageDecorator(private val view: ImageView) {
    fun applyRoundCorner(radius: Float) {
        val path = Path()
        path.addRoundRect(RectF(0f, 0f, view.width.toFloat(), view.height.toFloat()),
            radius, radius, Path.Direction.CW)
        view.clipToOutline = true
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
    }
}
// 可以对任何 View 使用，不限于 ImageView
```

**3. RecyclerView 中组合 MultiType 支持：**
```kotlin
// ✅ 组合：每种 ViewType 独立处理
interface ItemViewBinder<T> {
    fun getLayoutId(): Int
    fun bind(view: View, item: T)
}

class TextItemBinder : ItemViewBinder<TextItem> { /* ... */ }
class ImageItemBinder : ItemViewBinder<ImageItem> { /* ... */ }

class CompositeAdapter(
    private val binders: Map<Class<*>, ItemViewBinder<*>>
) : RecyclerView.Adapter<ViewHolder>() { /* ... */ }
```

**4. Compose 的组合哲学（原生支持）：**
```kotlin
// Compose 天然支持组合
@Composable
fun UserCard(user: User, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column {
            UserAvatar(user.avatar)  // 组合另一个组件
            UserInfo(user.name, user.email) // 组合另一个组件
        }
    }
}
```

**高频追问：**
1. Kotlin 的 `by` 委托如何促进组合优于继承？（`by` 关键字让组合像继承一样简洁，无需手写转发代码）
2. 什么场景下继承仍然优于组合？（真正满足"is-a"关系时：`Button` → `View`、`Activity` → `Context`；框架要求继承时）
3. Android SDK 中有哪些典型的"继承被过度使用"的例子？（`BaseActivity`/`BaseFragment` 的上帝类现象）

**面试官考察点：**
- 对面向对象设计原则的实践理解
- 能否在适当场景选择继承 vs 组合
- 对 Kotlin 委托特性的掌握

**易踩坑：**
- ❌ 极端化——认为"永远不该用继承"（Android 框架本身就是继承体系，Activity 必须继承）
- ❌ 组合后忘记传递生命周期（委托对象不知道何时释放资源）
- ❌ 多层组合导致调用链过长，影响调试和可读性

### <a id="android-01"></a>91. Activity 的生命周期与常见场景

**标准回答：**

Activity 生命周期是 Android 最基础也最重要的概念，由系统回调方法组成，理解其触发时机至关重要：

**完整生命周期回调：**

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {}  // Activity 创建
    override fun onStart() {}                               // 变为可见
    override fun onResume() {}                              // 获得焦点（前台）
    override fun onPause() {}                               // 失去焦点（部分可见）
    override fun onStop() {}                                // 完全不可见
    override fun onDestroy() {}                             // Activity 销毁
    override fun onRestart() {}                             // 从停止到重新启动
}
```

**典型场景触发顺序：**

| 场景 | 回调顺序 | 关键点 |
|------|---------|--------|
| 首次启动 | onCreate → onStart → onResume | 完整创建流程 |
| 启动新 Activity（全屏覆盖） | onPause → onStop | onStop 后可能被杀 |
| 启动透明/Dialog Activity | onPause | 不会 onStop（仍可见） |
| 按 Home 键 | onPause → onStop | 进入后台 |
| 从后台返回 | onRestart → onStart → onResume | 从 onStop 恢复 |
| 旋转屏幕（默认） | onPause → onStop → onDestroy → onCreate → onStart → onResume | 完全重建 |
| 后台进程被杀后返回 | onCreate → onStart → onResume（+ 恢复 onSaveInstanceState） | 需要状态恢复 |

**生命周期感知的状态保存：**
```kotlin
// onSaveInstanceState 保证在 onStop 之前调用
override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString("key", "value")
}

// onCreate 中恢复
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val value = savedInstanceState?.getString("key")
}
```

**onStart/onStop vs onResume/onPause 关键区别：**
- **onStart/onStop**：从"是否可见"角度，适合注册/注销 UI 不可见时可暂停的操作
- **onResume/onPause**：从"是否前台"角度，适合动画、相机等前台独占资源

**高频追问：**
1. `onSaveInstanceState` 的调用时机？（在 onStop 之前，但不一定在 onPause 之后；Android P+ 在 onStop 之后也可能）
2. 为什么不能在 `onPause` 中做耗时操作？（阻塞下一个 Activity 的显示；onPause 执行完新 Activity 的 onCreate 才开始）
3. `onDestroy` 一定被调用吗？（不一定；系统资源紧张时可能直接 kill 进程；finish() 正常销毁时一定调用）

**面试官考察点：**
- 对生命周期回调顺序的精确记忆
- 实际场景中能否正确处理状态保存与恢复
- 对 Android 进程管理机制的理解

**易踩坑：**
- ❌ 在 `onCreate` 中直接获取 View 的宽高（此时 View 尚未 measure，宽高为 0）
- ❌ 在 `onPause` 中保存数据但忘记 `onStop` 可能不调用（被系统 kill）
- ❌ 透明 Activity 场景下误以为会回调 onStop

---

### <a id="android-02"></a>92. Fragment 生命周期与 FragmentManager

**标准回答：**

Fragment 生命周期比 Activity 更复杂，需要与宿主 Activity 生命周期和 FragmentManager 回退栈协同：

**Fragment 独有回调：**
```kotlin
class MyFragment : Fragment() {
    override fun onAttach(context: Context) {}    // 附加到 Activity
    override fun onCreate(savedInstanceState: Bundle?) {}
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {} // View 创建完成
    override fun onViewStateRestored(savedInstanceState: Bundle?) {}
    override fun onStart() {}
    override fun onResume() {}
    override fun onPause() {}
    override fun onStop() {}
    override fun onDestroyView() {}               // View 销毁但 Fragment 存活
    override fun onDestroy() {}
    override fun onDetach() {}                    // 与 Activity 解绑
}
```

**FragmentManager 关键概念：**

```kotlin
// FragmentManager 类型
activity.supportFragmentManager    // Activity 级别的 FragmentManager
childFragmentManager               // Fragment 内嵌套 Fragment 的管理器
parentFragmentManager              // 获取父级 FragmentManager

// 事务操作
supportFragmentManager.commit {
    add<MyFragment>(R.id.container, bundle)
    setReorderingAllowed(true)     // 优化操作顺序
    addToBackStack("tag")          // 加入回退栈
}

// 查找 Fragment
val fragment = supportFragmentManager.findFragmentByTag("tag")
val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
```

**实际场景：ViewPager2 + Fragment 的生命周期：**
```kotlin
// ViewPager2 预加载机制下，Fragment 生命周期提前
viewPager2.offscreenPageLimit = 1 // 默认预加载 1 页
// 预加载页的 onResume 不会调用，但 onCreateView 会调用
```

**核心区别：onCreateView 和 onViewCreated 的区别：**
- `onCreateView`：创建并返回 View 层次结构，此时 View 尚未完全附着
- `onViewCreated`：View 已创建，可以安全进行 View 操作（findViewById 等）

**高频追问：**
1. Fragment 回退栈的原理？（`addToBackStack` 保存事务，按返回键时 `popBackStack` 逆操作事务，恢复上一个 Fragment）
2. `commit()` vs `commitNow()` 的区别？（`commit` 异步，在主线程空闲时执行；`commitNow` 同步立即执行，不能与 `addToBackStack` 一起使用）
3. Fragment 重建时如何恢复数据？（通过 `arguments` Bundle 传递参数，`onSaveInstanceState` 保存临时状态）

**面试官考察点：**
- 对 Fragment 复杂生命周期的全面理解
- 是否遇到过 Fragment 重叠/混乱/丢失状态的问题
- 对 Fragment 在现代 Android 开发中定位的思考

**易踩坑：**
- ❌ 在 `onCreateView` 中使用 `requireActivity()` 获取 Activity（此时可能还未完全 Attach）
- ❌ 忘记在 `onDestroyView` 中清理 View 相关资源（如 RecyclerView Adapter）
- ❌ Fragment replace 未 `addToBackStack`，按返回键直接退出 Activity

---

### <a id="android-03"></a>93. Activity 的四种启动模式

**标准回答：**

Activity 的启动模式决定了 Activity 实例的创建策略和任务栈行为：

**standard（默认）：**
- 每次启动都创建新实例，放入启动者的任务栈
- 适用场景：常规页面

```xml
<activity android:name=".DetailActivity"
    android:launchMode="standard" />
```

**singleTop（栈顶复用）：**
- 如果目标 Activity 已在栈顶，不创建新实例，回调 `onNewIntent`
- 适用场景：通知点击跳转（避免重复创建同一页面）

```kotlin
// Activity 栈：[A, B, C]
// 启动 C → onNewIntent（不创建）
// 启动 B → 创建新 B：[A, B, C, B]
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent) // 更新 Intent
}
```

**singleTask（栈内复用）：**
- 如果目标 Activity 已在栈中，将其上面的 Activity 全部弹出，回调 `onNewIntent`
- 适用场景：应用主页（只有一个实例，避免多层主页）

```kotlin
// Activity 栈：[A, B, C, D]
// 启动 B（singleTask）→ 栈变为 [A, B]，C/D 被销毁
```

**singleInstance（单独栈）：**
- 目标 Activity 在新任务栈中，且该栈只能有它一个 Activity
- 适用场景：与主应用完全隔离的页面（如分享、拨号）

```kotlin
// 极少使用，大多数场景 singleTask 足够
```

**Intent Flags 动态控制（优先级高于 Manifest）：**
```kotlin
Intent.FLAG_ACTIVITY_NEW_TASK          // 在新任务中启动
Intent.FLAG_ACTIVITY_SINGLE_TOP        // 等价 singleTop
Intent.FLAG_ACTIVITY_CLEAR_TOP         // 清除之上的 Activity
Intent.FLAG_ACTIVITY_CLEAR_TASK        // 清除整个任务栈
Intent.FLAG_ACTIVITY_NO_HISTORY         // 不留在历史栈
```

**高频追问：**
1. `singleTask` 和 `FLAG_ACTIVITY_CLEAR_TOP` 的区别？（singleTask 始终只有一个实例，CLEAR_TOP 只是清除顶部，不会复用栈底实例除非配合 SINGLE_TOP）
2. `taskAffinity` 和启动模式的关系？（singleTask/singleInstance 配合 taskAffinity 指定所属任务栈；默认 taskAffinity 为包名）
3. 什么场景用 `FLAG_ACTIVITY_NEW_TASK`？（非 Activity 上下文启动 Activity 时必须用，如 Service 中启动 Activity）

**面试官考察点：**
- 对任务栈和启动模式的精确理解
- 实际踩坑经验（重复启动、任务栈混乱等）
- 对 Flags 和 launchMode 配合使用的掌握

**易踩坑：**
- ❌ 在 `applicationContext` 下启动 Activity 不加 `FLAG_ACTIVITY_NEW_TASK`（直接崩溃）
- ❌ `singleTask` + `onNewIntent` 不更新 Intent 导致获取旧数据
- ❌ `singleInstance` 导致的"回不去"问题（用户按返回键无法回到主任务栈）

---

### <a id="android-04"></a>94. Android 进程优先级与保活

**标准回答：**

Android 系统在内存不足时会按**进程优先级**从低到高杀死进程：

**进程优先级（从高到低）：**

| 优先级 | 进程类型 | 特征 | 被杀概率 |
|--------|---------|------|---------|
| 1 | 前台进程 | 用户正在交互的 Activity / 前台 Service | 几乎不会 |
| 2 | 可见进程 | Activity 可见但非前台（如被 Dialog 遮挡） | 极低 |
| 3 | 服务进程 | 运行着 `startForeground` 的 Service | 较低 |
| 4 | 后台进程 | Activity onStop 后，仍在最近任务列表 | 中等 |
| 5 | 空进程 | 无任何组件，仅作缓存 | 最高 |

**合法保活方案：**

**1. 前台 Service（官方推荐）：**
```kotlin
class KeepAliveService : Service() {
    override fun onCreate() {
        super.onCreate()
        startForeground(NOTIFICATION_ID, buildNotification())
    }
}
// Android 8.0+ 必须创建通知频道
// 通知栏会显示"XX正在运行"，用户可见
```

**2. 双进程守护（不建议，高版本受限）：**
```kotlin
// 两个 Service 互相绑定，互相拉活
// Android 8.0+ 失效，且耗电量极大
```

**3. JobScheduler / WorkManager（推荐替代传统保活）：**
```kotlin
val request = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
    .setConstraints(Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    )
    .build()
WorkManager.getInstance(context).enqueue(request)
```

**4. 厂商白名单引导（唯一实际有效的方案）：**
```kotlin
// 引导用户开启自启动/后台运行权限
fun requestAutoStart(context: Context) {
    val intent = Intent().apply {
        component = ComponentName("com.huawei.systemmanager",
            "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")
    }
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}
```

**高频追问：**
1. Android 13/14 对后台 Service 有哪些新限制？（前台 Service 需声明类型，必须有合理用途说明，系统会显示通知）
2. WorkManager 为什么比前台 Service 更好？（系统统一调度、省电、适配 Doze 模式、兼容所有版本）
3. 为什么传统"保活"方案基本无效了？（Android 8.0+ 后台限制 + Doze 模式 + 各厂商后台管理策略，常规方法全被限制）

**面试官考察点：**
- 对 Android 进程管理机制的深度理解
- 是否了解各版本对后台执行的限制演变
- 能否推荐"正道"而非"歪门邪道"的方案

**易踩坑：**
- ❌ 使用"1像素 Activity"保活（Android 8.0+ 无效，且用户体验极差）
- ❌ 频繁拉活导致耗电，被系统标记为"毒瘤应用"
- ❌ 忽略 WorkManager 的约束条件，仍然用 Service 做定时任务

---

### <a id="android-05"></a>95. Binder IPC 机制深入

**标准回答：**

Binder 是 Android 最核心的进程间通信（IPC）机制，基于 OpenBinder 实现，驱动层位于 `/dev/binder`。

**Binder 架构层次：**

```
┌──────────────────────────────────────────┐
│          Application Layer                │
│  AIDL / Intent / ContentProvider          │
└────────────────┬─────────────────────────┘
                 │
┌────────────────▼─────────────────────────┐
│        Framework Layer (Java/C++)         │
│  Binder.java / Binder.cpp                 │
│  ServiceManager                           │
└────────────────┬─────────────────────────┘
                 │ ioctl
┌────────────────▼─────────────────────────┐
│      Kernel Layer (Binder Driver)         │
│  内存映射（一次拷贝）                        │
│  线程池管理                                │
│  引用计数 / 死亡通知                        │
└──────────────────────────────────────────┘
```

**Binder 核心优势（为什么不用 Linux 原生 IPC）：**

| 特性 | Binder | Socket/Pipe | 共享内存 |
|------|--------|-------------|---------|
| 数据拷贝次数 | 1 次 | 2 次 | 0 次（但需同步） |
| C/S 支持 | 原生支持 | 需自己实现 | 需自己实现 |
| 安全性（UID/PID 识别） | 内核层支持 | ❌ | ❌ |
| 内存管理 | 内核管理 | 双端管理 | 双端管理 |

**Binder 通信流程（一次完整 RPC 调用）：**
```
Client ──transact()──→ Binder Proxy ──ioctl──→ Binder Driver
                                                      │
                                                Binder Thread Pool
                                                      │
Server ←──onTransact()──← Binder Stub ←────ioctl───←──┘
```

**Binder 的 mmap 原理（一次拷贝的秘密）：**
- Binder Driver 在物理内存中分配一块内核缓冲区
- 同时映射到接收进程的用户空间和内核空间
- 发送方只需拷贝一次数据到内核缓冲区
- 接收方直接从映射的用户空间读取（不需要再从内核拷贝到用户空间）

**高频追问：**
1. Binder 线程池大小是多少？如何处理并发？（默认最大 16 个线程，超过则阻塞等待；`binder_thread_write` 控制）
2. `oneway` 调用和非 `oneway` 的区别？（oneway 异步无返回，不会阻塞 Client；非 oneway 同步等待返回）
3. Binder 的死亡通知机制是怎么实现的？（`linkToDeath` 注册，Binder Driver 在 Server 进程死亡时发送 `BR_DEAD_BINDER` 通知 Client）

**面试官考察点：**
- 对 Android 核心机制的深层理解（区分"会写 AIDL"和"懂 Binder"）
- 对 Linux IPC 和 Binder 的对比认知
- 对性能和安全性的关注

**易踩坑：**
- ❌ 混淆 Binder 和 AIDL——AIDL 是使用 Binder 的工具，不是 Binder 本身
- ❌ 不理解数据大小限制（一次 Binder 传输最大 1MB-8KB，大约 1MB，超限抛 `TransactionTooLargeException`）
- ❌ 在 Binder 线程中做长时间操作（阻塞线程池，导致其他请求超时）

---

### <a id="android-06"></a>96. AIDL 的使用与原理

**标准回答：**

AIDL（Android Interface Definition Language）是 Android 提供的用于跨进程通信的接口定义语言，底层基于 Binder。

**完整使用流程：**

**1. 定义 AIDL 接口：**
```java
// IUserManager.aidl
package com.example.app;

import com.example.app.User;

interface IUserManager {
    User getUser(String userId);
    List<User> getUsers();
    void addUser(in User user);
}
```

**2. 定义跨进程传递的数据类：**
```java
// User.aidl
package com.example.app;
parcelable User;
```

```kotlin
@Parcelize
data class User(val id: String, val name: String, val age: Int) : Parcelable
```

**3. 实现 Server 端 Stub：**
```kotlin
class UserManagerService : Service() {
    private val binder = object : IUserManager.Stub() {
        override fun getUser(userId: String?): User? {
            return userRepository.getUser(userId ?: "")
        }
        
        override fun getUsers(): MutableList<User> {
            return userRepository.getAllUsers().toMutableList()
        }
        
        override fun addUser(user: User?) {
            user?.let { userRepository.addUser(it) }
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder
}
```

**4. Client 端绑定和使用：**
```kotlin
class ClientActivity : AppCompatActivity() {
    private var userManager: IUserManager? = null
    
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            userManager = IUserManager.Stub.asInterface(service)
            // 或使用 Kotlin 扩展
            val user = userManager?.getUser("123")
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            userManager = null
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, UserManagerService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }
}
```

**AIDL 中的方向标记：**
| 标记 | 含义 | Java 等价 |
|------|------|-----------|
| `in` | 输入参数，Client→Server 传递 | 普通参数 |
| `out` | 输出参数，Server→Client 传递 | Holder 模式 |
| `inout` | 双向传递 | 双向参数 |

**高频追问：**
1. AIDL 编译后生成了什么？（一个 Java 接口 + Stub 抽象类 + Proxy 类；Proxy 负责序列化/反序列化和 Binder 通信）
2. AIDL 和 Messenger 的区别？（AIDL 支持多线程并发，直接操作 Binder；Messenger 串行处理，适合简单消息传递）
3. AIDL 中的 `RemoteCallbackList` 是什么？（跨进程的回调列表，使用泛型 + IBinder 作为 key 解决跨进程引用相等问题）

**面试官考察点：**
- 是否真正写过 AIDL 并理解工具链
- 对跨进程通信的线程安全理解
- `in`/`out`/`inout` 的理解深度

**易踩坑：**
- ❌ 忘记处理 `RemoteException`（跨进程调用必须 try-catch）
- ❌ AIDL 回调未使用 RemoteCallbackList 导致内存泄漏
- ❌ 跨进程传输大数据（超过 Binder 限制会抛异常）

---

### <a id="android-07"></a>97. Handler、Looper、MessageQueue 原理解析

**标准回答：**

Handler 是 Android 消息机制的核心，由 Handler、Looper、MessageQueue 三者协作完成线程间通信。

**架构关系：**
```
Looper.prepare() → 创建 Looper + MessageQueue（线程私有）
Looper.loop()     → 死循环取消息 → dispatch → Handler.handleMessage()

Thread A                        Thread B (Main)
│                               │
│──sendMessage(msg)──→ MessageQueue ──→ Looper.loop()
│                        ↓              │
│                     Message           │
│                        ↓              ↓
│                     Handler.dispatchMessage()
│                        ↓
│                     Handler.handleMessage(msg)
```

**核心源码分析（简化）：**

```java
// Looper.loop() - 消息循环核心
public static void loop() {
    final Looper me = myLooper();
    final MessageQueue queue = me.mQueue;
    for (;;) { // 无限循环
        Message msg = queue.next(); // 可能阻塞（nativePollOnce）
        if (msg == null) return; // 退出
        msg.target.dispatchMessage(msg); // 分发到 Handler
        msg.recycleUnchecked(); // 回收消息
    }
}

// MessageQueue.next() - 阻塞取消息
Message next() {
    for (;;) {
        nativePollOnce(ptr, nextPollTimeoutMillis); // Native 层阻塞
        synchronized (this) {
            Message msg = mMessages; // 链表头
            if (msg != null) {
                long now = SystemClock.uptimeMillis();
                if (msg.when <= now) {
                    // 到期，取出
                    mMessages = msg.next;
                    return msg;
                } else {
                    nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                }
            }
        }
    }
}

// Handler.enqueueMessage() - 入队
private boolean enqueueMessage(MessageQueue queue, Message msg, long uptimeMillis) {
    msg.target = this;
    synchronized (this) {
        // 按时间顺序插入链表
        Message p = mMessages;
        if (p == null || uptimeMillis < p.when) {
            msg.next = p;
            mMessages = msg;
        } else {
            // 找到合适位置插入
            while (p.next != null && p.next.when <= uptimeMillis) p = p.next;
            msg.next = p.next;
            p.next = msg;
        }
    }
    nativeWake(mPtr); // 唤醒阻塞的 loop
}
```

**消息类型：**
```kotlin
// 1. 普通消息
handler.sendMessage(Message.obtain().apply { what = 1 })

// 2. 延时消息
handler.sendMessageDelayed(msg, 1000)

// 3. post Runnable
handler.post { updateUI() }

// 4. 屏障消息（SyncBarrier）- 优先处理异步消息
// 典型应用：View 的 invalidate/requestLayout
```

**高频追问：**
1. Looper 死循环为什么不会导致 ANR？（死循环只是在线程内循环，不占用 CPU 时 `nativePollOnce` 会挂起；ANR 是因为在主线程做耗时操作导致无法响应输入事件）
2. `Message.obtain()` 和 `new Message()` 的区别？（obtain 从消息池复用，减少内存分配和 GC 压力）
3. IdleHandler 是什么？有什么使用场景？（MessageQueue 空闲时的回调，适合做低优先级的延迟初始化；`Looper.myQueue().addIdleHandler { doLowPriorityWork(); false }`）

**面试官考察点：**
- 是否读过 Handler 源码而不是只使用
- 对阻塞唤醒机制的底层理解（epoll）
- 能否解释常见的 Handler 相关问题

**易踩坑：**
- ❌ 在子线程中直接 `new Handler()`（缺少 Looper，抛 `RuntimeException`）
- ❌ Handler 的 `postDelayed` 不精确（延时是最小延迟，实际延迟取决于消息队列前面的消息）
- ❌ 使用匿名内部类 Handler 不设为静态类（隐式持有 Activity 引用 → 内存泄漏）

---

### <a id="android-08"></a>98. Handler 内存泄漏与解决方案

**标准回答：**

Handler 内存泄漏是 Android 中经典的内存问题，根本原因是**匿名内部类隐式持有外部类的强引用 + 消息未处理完**。

**泄漏链路分析：**
```
MessageQueue → Message → Message.target(Handler) → Handler(匿名内部类) → Activity
```
即使 Activity 已 `onDestroy`，只要 MessageQueue 中还有指向 Handler 的 Message 未处理，Handler 就持有 Activity 引用，GC 无法回收。

**典型泄漏代码：**
```kotlin
class LeakActivity : AppCompatActivity() {
    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            // 持有 Activity 引用
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler.sendEmptyMessageDelayed(1, 60000) // 60秒延迟
        // 如果在60秒内退出 Activity → 内存泄漏
    }
}
```

**解决方案：**

**方案一：静态内部类 + WeakReference（经典方案）：**
```kotlin
class SafeActivity : AppCompatActivity() {
    private val handler = SafeHandler(this)

    private class SafeHandler(activity: SafeActivity) : Handler(Looper.getMainLooper()) {
        private val weakRef = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            weakRef.get()?.let { activity ->
                activity.handleMessage(msg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // 清理所有消息
    }
}
```

**方案二：Lifecycle 感知的 Handler（推荐）：**
```kotlin
class LifecycleHandler(lifecycle: Lifecycle) : Handler(Looper.getMainLooper()), LifecycleObserver {
    init { lifecycle.addObserver(this) }

    override fun dispatchMessage(msg: Message) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            super.dispatchMessage(msg)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        removeCallbacksAndMessages(null)
    }
}
```

**方案三：View.post() / View.removeCallbacks()（View 级别安全）：**
```kotlin
view.post { /* 自动在 View detach 时取消 */ }
```

**方案四：协程替代 Handler（最推荐）：**
```kotlin
// 使用 lifecycleScope 不需要手动管理生命周期
lifecycleScope.launch {
    delay(60000)
    updateUI() // lifecycleScope 在 onDestroy 时自动取消
}
```

**高频追问：**
1. `removeCallbacksAndMessages(null)` 中的 null 参数是什么意思？（null 表示移除所有消息和回调，传 Object token 则只移除匹配的）
2. WeakReference 在 Handler 场景中真的完全可靠吗？（不绝对：GC 时机不确定，可能 handleMessage 时 Activity 已被回收返回 null，需判空）
3. 如果不用 Handler，Android 主线程的消息机制用什么替代？（协程 + Dispatchers.Main 底层仍依赖 Handler，但生命周期由协程管理，泄漏风险低得多）

**面试官考察点：**
- 对内存泄漏"引用链"的分析能力
- 是否了解多种解决方案及其演变
- 对协程时代的 Handler 定位理解

**易踩坑：**
- ❌ 静态内部类忘了传 WeakReference，直接传 Activity（仍然是强引用）
- ❌ 在 `onDestroy` 中只 removeCallbacks 不 removeMessages（还有可能未处理的 message）
- ❌ 使用 WeakReference 但 handleMessage 中不判空直接使用

---

### <a id="android-09"></a>99. View 绘制流程：measure、layout、draw

**标准回答：**

View 的绘制流程由三大阶段组成，由 ViewRootImpl 驱动，从 DecorView 开始递归遍历 View 树：

**整体流程：**
```
ViewRootImpl.performTraversals()
    ↓
performMeasure() → measure() → onMeasure()
    ↓
performLayout() → layout() → onLayout()
    ↓
performDraw() → draw() → onDraw()
```

**1. Measure（测量阶段）：确定 View 的宽高**

```kotlin
// View.measure() 是 final 方法，子类重写 onMeasure()
override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // MeasureSpec = Mode(高2位) + Size(低30位)
    val widthMode = MeasureSpec.getMode(widthMeasureSpec) // EXACTLY | AT_MOST | UNSPECIFIED
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    
    val width = when (widthMode) {
        MeasureSpec.EXACTLY -> widthSize       // match_parent / 指定值
        MeasureSpec.AT_MOST -> min(intrinsicWidth, widthSize) // wrap_content
        MeasureSpec.UNSPECIFIED -> intrinsicWidth // ScrollView 场景
    }
    setMeasuredDimension(width, height)
}
```

**三种 MeasureSpec 模式：**
| Mode | 含义 | 典型来源 |
|------|------|---------|
| EXACTLY | 精确值 | `match_parent`、`100dp` |
| AT_MOST | 最大不超过 | `wrap_content` |
| UNSPECIFIED | 无限制 | ScrollView 内部 |

**2. Layout（布局阶段）：确定 View 的位置**

```kotlin
// ViewGroup 重写 onLayout 确定子 View 位置
override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    var top = paddingTop
    for (child in children) {
        child.layout(
            paddingLeft, top,
            paddingLeft + child.measuredWidth, top + child.measuredHeight
        )
        top += child.measuredHeight
    }
}
```

**3. Draw（绘制阶段）：将 View 绘制到 Canvas**

```kotlin
// View.draw() 的绘制顺序：
// 1. drawBackground()      绘制背景
// 2. onDraw()              绘制自身内容
// 3. dispatchDraw()        绘制子 View（ViewGroup 特有）
// 4. onDrawForeground()    绘制前景（滚动条等）
```

**触发重绘的机制：**
```kotlin
// requestLayout()：触发 measure → layout → draw（结构变化）
view.requestLayout()

// invalidate()：只触发 draw（内容变化，大小位置不变）
view.invalidate()

// 关键：requestLayout 和 invalidate 都最终调用 ViewRootImpl.scheduleTraversals()
// 通过 Choreographer 同步到 VSYNC 信号
```

**高频追问：**
1. `View.post {}` 为什么能获取到 View 宽高？（Runnable 被放入 MessageQueue，此时 View 已经经过了 measure/layout）
2. `onMeasure` 和 `measure` 的区别？（`measure` 是 final 方法，调用 `onMeasure` 并缓存结果；子类只能重写 `onMeasure`）
3. `invalidate` 和 `postInvalidate` 的区别？（`postInvalidate` 可在非 UI 线程调用，内部通过 Handler 切换到主线程）

**面试官考察点：**
- 对 View 绘制机制的系统性理解
- 能否说出 MeasureSpec 的模式和意义
- 自定义 View 的实际经验

**易踩坑：**
- ❌ 在 `onCreate` 中直接获取 View 宽高（此时未 measure，返回 0）
- ❌ `onMeasure` 中忘记调用 `setMeasuredDimension`（直接抛异常）
- ❌ 自定义 ViewGroup 忘记处理 padding 和 margin（子 View 位置计算错误）

---

### <a id="android-10"></a>100. 事件分发机制详解

**标准回答：**

Android 事件分发机制是处理触摸事件的核心，遵循**责任链模式**，从 Activity → Window → DecorView → ViewGroup → View 层层传递。

**三大核心方法：**
| 方法 | 作用 | 默认返回值 |
|------|------|-----------|
| `dispatchTouchEvent` | 分发事件 | 调用 `onTouchEvent` 或子 View 的方法 |
| `onInterceptTouchEvent` | 拦截事件（ViewGroup 特有） | false（不拦截） |
| `onTouchEvent` | 消费事件 | 取决于是否 clickable |

**事件分发流程（ACTION_DOWN 为例）：**
```
Activity.dispatchTouchEvent()
    ↓
PhoneWindow.superDispatchTouchEvent()
    ↓
DecorView.dispatchTouchEvent()
    ↓
ViewGroup.dispatchTouchEvent()
    ├── onInterceptTouchEvent() == true?
    │   ├── Yes → onTouchEvent()
    │   └── No  → 遍历子 View
    │       ├── 子 View.dispatchTouchEvent()
    │       │   ├── onTouchListener.onTouch()
    │       │   └── onTouchEvent()
    │       │       ├── onClickListener.onClick()
    │       │       └── return true/false
    │       └── 所有子 View 都 false → 自己的 onTouchEvent()
```

**伪代码理解（核心逻辑）：**
```kotlin
fun dispatchTouchEvent(event: MotionEvent): Boolean {
    var handled = false
    if (onInterceptTouchEvent(event)) {
        handled = onTouchEvent(event)
    } else {
        for (child in children.reversed()) {
            if (child.canReceivePointerEvents() && isTransformedTouchPointInView(event, child)) {
                handled = child.dispatchTouchEvent(event)
                if (handled) break
            }
        }
        if (!handled) handled = onTouchEvent(event)
    }
    return handled
}
```

**关键机制：**

**1. 事件序列的连续性：**
- DOWN → MOVE... → UP/CANCEL 是一个完整序列
- 如果子 View 不消费 DOWN，后续 MOVE/UP 不会再传给它
- 父 View 可在任意时刻拦截（onInterceptTouchEvent 返回 true）

**2. 监听器优先级：**
```kotlin
view.setOnTouchListener { v, event ->
    // 优先级高于 onClickListener
    true // 返回 true 则 onClickListener 不会被触发
}

view.setOnClickListener { // 在 onTouchEvent 中 ACTION_UP 时触发 }
```

**3. requestDisallowInterceptTouchEvent：**
```kotlin
// 子 View 阻止父 View 拦截（如 ViewPager 内嵌套 ScrollView）
parent.requestDisallowInterceptTouchEvent(true)
// 注意：DOWN 事件不受此方法影响，父 View 始终可以拦截
```

**滑动冲突解决方案：**
```kotlin
// 1. 外部拦截法：父 View 根据条件拦截
override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return when {
        ev.action == MotionEvent.ACTION_MOVE && needIntercept() -> true
        else -> false
    }
}

// 2. 内部拦截法：子 View 通过 requestDisallowInterceptTouchEvent 控制
```

**高频追问：**
1. `onTouchEvent` 返回 true 和 false 的区别？（true：事件被消费，序列中后续事件继续传给该 View；false：事件未消费，父 View 的 onTouchEvent 被调用）
2. `ACTION_CANCEL` 什么时候触发？（父 View 拦截了后续事件，当前处理事件的子 View 收到 CANCEL，表示此次交互被中断）
3. View 的 `clickable` 属性和事件分发的关系？（clickable=true 时 `onTouchEvent` 默认返回 true，消费事件）

**面试官考察点：**
- 能否画图描述事件分发流程
- 实际解决滑动冲突的经验
- 对事件序列、拦截机制的理解深度

**易踩坑：**
- ❌ 在 `onInterceptTouchEvent` 中拦截 DOWN 事件（子 View 永远收不到事件）
- ❌ 忘记处理 `ACTION_CANCEL` 导致 UI 状态卡住（如按下态不恢复）
- ❌ 滑动冲突中 `requestDisallowInterceptTouchEvent(true)` 后忘记复位

---

### <a id="android-11"></a>101. RecyclerView 优化与缓存机制

**标准回答：**

RecyclerView 的核心优势在于其**四级缓存机制**和灵活的**LayoutManager**，实现高效列表渲染。

**四级缓存机制（按查找优先级）：**

```
1. Scrap（mAttachedScrap / mChangedScrap）
   ↓ 未命中
2. Cache（mCachedViews，默认容量 2）
   ↓ 未命中
3. ViewCacheExtension（开发者自定义）
   ↓ 未命中
4. RecycledViewPool（跨 Adapter 共享池，默认每种 ViewType 5 个）
```

**各级缓存详解：**

```kotlin
// 1. mAttachedScrap：屏幕内可见的 ViewHolder
// 不重新 bind，仅用于 layout 重排
// 缓存的是还在屏幕上的 ViewHolder

// 2. mCachedViews：刚滑出屏幕的 ViewHolder
// 默认容量 2，不重新 bind（position 已确定匹配时）
// 离屏最近的 2 个 ViewHolder 保留数据和状态

// 3. ViewCacheExtension：自定义缓存策略（极少使用）
class MyCacheExtension : RecyclerView.ViewCacheExtension() {
    override fun getViewForPositionAndType(recycler: RecyclerView.Recycler, position: Int, type: Int): View? {
        // 自定义逻辑，如预加载
        return null
    }
}

// 4. RecycledViewPool：完全解绑的 ViewHolder 池
// 需要重新 onBindViewHolder
// 可设置最大容量：pool.setMaxRecycledViews(viewType, max)
```

**关键优化策略：**

**1. setHasFixedSize(true)：**
```kotlin
recyclerView.setHasFixedSize(true)
// 当 item 大小固定时，避免不必要的 requestLayout
```

**2. 预取机制（GapWorker + Prefetch）：**
```kotlin
// Android 5.0+ 默认开启，在空闲时预取即将显示的 View
// 可通过 RecyclerView.LayoutManager 控制
(layoutManager as LinearLayoutManager).apply {
    initialPrefetchItemCount = 4 // 预取数量
}
```

**3. setItemViewCacheSize：**
```kotlin
recyclerView.setItemViewCacheSize(10) // 增大离屏缓存
```

**4. DiffUtil 精准更新：**
```kotlin
class MyDiffCallback(
    private val oldList: List<Item>,
    private val newList: List<Item>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos].id == newList[newPos].id
    }
    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}
// 结合 ListAdapter 自动异步 Diff
```

**5. 多 ViewType 共用 RecycledViewPool：**
```kotlin
val sharedPool = RecyclerView.RecycledViewPool()
recyclerView1.setRecycledViewPool(sharedPool)
recyclerView2.setRecycledViewPool(sharedPool)
// 提高跨 RecyclerView 的 ViewHolder 复用率
```

**高频追问：**
1. RecyclerView 和 ListView 的核心区别？（RecyclerView 四级缓存 vs ListView 二级缓存 + RecyclerView 强制 ViewHolder + LayoutManager 解耦）
2. `notifyItemChanged()` 为什么会导致闪烁？（触发 ViewHolder 重新绑定，Scrap 缓存直接失效；推荐使用 DiffUtil 或 `setHasStableIds(true)`）
3. ViewHolder 的 `setIsRecyclable(false)` 有什么用？（禁止回收，解决某些动画场景中复用错误的问题；但用完必须恢复）

**面试官考察点：**
- 对缓存机制的层次化理解
- 实际列表优化经验（卡顿排查、缓存调优）
- 对 DiffUtil 原理的掌握

**易踩坑：**
- ❌ 在 `onBindViewHolder` 中设置点击监听器（每次 bind 都创建新 Listener，应放在 `onCreateViewHolder` 中）
- ❌ 图片加载未在 ViewHolder 回收时取消（复用时图片错乱）
- ❌ 复杂 item 未使用 `setHasStableIds(true)`（`notifyDataSetChanged` 导致所有 item 闪烁重绘）

---

### <a id="android-12"></a>102. 属性动画与补间动画的区别

**标准回答：**

Android 提供三种动画机制：**View Animation（补间动画）**、**Property Animation（属性动画）**、**Drawable Animation（帧动画）**。核心区别在于属性和补间动画：

**补间动画（View Animation）：**
```kotlin
val anim = TranslateAnimation(0f, 100f, 0f, 0f).apply {
    duration = 300
    fillAfter = true // 动画结束后停在最终位置
}
view.startAnimation(anim)
// 问题：动画后 view 的实际位置不变（点击区域仍在新位置）
```

**属性动画（Property Animation）：**
```kotlin
ObjectAnimator.ofFloat(view, "translationX", 0f, 100f).apply {
    duration = 300
    start()
}
// 优势：view 的实际属性被修改，点击区域也在新位置
```

**核心区别对比：**

| 维度 | 补间动画 | 属性动画 |
|------|---------|---------|
| **修改对象** | 只修改绘制矩阵 | 修改真实属性 |
| **生效对象** | 仅 View | 任意 Object |
| **点击区域** | 不变（动画后点击原位置） | 随动画移动 |
| **动画能力** | translate/scale/rotate/alpha | 任意属性（含自定义） |
| **组合方式** | AnimationSet | AnimatorSet |
| **API 层级** | API 1 | API 11+ |
| **插值器** | 支持 | 支持（更丰富） |

**属性动画高级用法：**

**1. ValueAnimator + 自定义更新：**
```kotlin
ValueAnimator.ofFloat(0f, 100f).apply {
    duration = 300
    addUpdateListener { animation ->
        val value = animation.animatedValue as Float
        customView.updateProgress(value)
    }
    start()
}
```

**2. AnimatorSet 组合动画：**
```kotlin
val alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
val translate = ObjectAnimator.ofFloat(view, "translationY", 0f, 200f)

AnimatorSet().apply {
    playTogether(alpha, translate) // 同时执行
    start()
}

AnimatorSet().apply {
    play(translate).after(alpha) // 顺序执行
    start()
}
```

**3. Keyframe（关键帧动画）：**
```kotlin
Keyframe.ofFloat(0f, 0f)
Keyframe.ofFloat(0.5f, 60f)
Keyframe.ofFloat(1f, 0f)
PropertyValuesHolder.ofKeyframe("translationY", keyframe1, keyframe2, keyframe3)
```

**4. TypeEvaluator + 自定义估值器：**
```kotlin
// 自定义颜色估值器
class ColorEvaluator : TypeEvaluator<Int> {
    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        // 分别计算 ARGB 通道
        val r = (startValue shr 16 and 0xff) + ((endValue shr 16 and 0xff) - (startValue shr 16 and 0xff)) * fraction
        return (a shl 24) or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
    }
}
```

**高频追问：**
1. 属性动画的原理？（通过反射调用 setter 方法修改属性值，配合 `invalidate` 触发重绘）
2. 补间动画的 Matrix 变换发生在哪一层？（在 `View.draw()` 的 Canvas 变换层，在 `applyLegacyAnimation` 中应用）
3. ViewPropertyAnimator 和 ObjectAnimator 的区别？（ViewPropertyAnimator 内部通过 animate() 直接设置多个属性，无需反射，更高效）

**面试官考察点：**
- 是否理解两者本质区别（绘制层 vs 属性层）
- 能否解决补间动画的"点击区域不变"问题
- 对高级动画 API 的熟悉程度

**易踩坑：**
- ❌ 补间动画后 view 的点击事件还在原位置（应使用属性动画）
- ❌ ObjectAnimator 的属性缺少 setter 方法（私有或包内可见 → 运行时 Crash）
- ❌ 动画结束后未调用 cancel() 导致内存泄漏（View 被移除但动画仍在运行）

---

### <a id="android-13"></a>103. Bitmap 优化与内存管理

**标准回答：**

Bitmap 是 Android 中最大的内存消耗源之一，优化 Bitmap 对于避免 OOM 至关重要。

**Bitmap 内存计算（Android 8.0+）：**
```kotlin
// 内存占用 = 宽 × 高 × 每个像素字节数
// ARGB_8888: 4 bytes/pixel（默认）
// RGB_565: 2 bytes/pixel
// 1920×1080 ARGB_8888 ≈ 8.3 MB
```

**1. 图片加载优化：**

**尺寸压缩（inSampleSize）：**
```kotlin
fun decodeSampledBitmap(res: Resources, resId: Int, reqWidth: Int, reqHeight: Int): Bitmap {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true // 只读尺寸，不加载到内存
    }
    BitmapFactory.decodeResource(res, resId, options)
    
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
```

**2. Bitmap 内存优化区域（Android 8.0 前后）：**
- Android 8.0 之前：像素数据在 Native 堆，对象在 Java 堆
- Android 8.0+：像素数据在 Native 堆，对象仍在 Java 堆，GC 更可控

**3. Bitmap 复用：**
```kotlin
val options = BitmapFactory.Options().apply {
    inMutable = true // 允许复用
    inBitmap = reusableBitmap // 复用已解码的 Bitmap
}
// 要求：复用的 Bitmap 必须 >= 新 Bitmap 的大小且同 Config
```

**4. Bitmap 配置选择：**
```kotlin
// 根据场景选择
Bitmap.Config.ARGB_8888 // 真彩色，4B/px（默认）
Bitmap.Config.RGB_565   // 无透明通道，2B/px（节省 50%）
Bitmap.Config.ARGB_4444 // 已废弃
Bitmap.Config.ALPHA_8   // 仅透明通道，1B/px
Bitmap.Config.HARDWARE  // Android 8.0+ 硬件加速位图（GPU 管理）
```

**5. 大图加载方案（BitmapRegionDecoder）：**
```kotlin
val decoder = BitmapRegionDecoder.newInstance(inputStream, false)
val options = BitmapFactory.Options()
val region = decoder.decodeRegion(
    Rect(0, 0, viewWidth, viewHeight), options
)
// 只加载可见区域，类似地图瓦片加载
```

**6. LruCache 内存缓存：**
```kotlin
val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
val cacheSize = maxMemory / 8 // 使用 1/8 可用内存

val bitmapCache = object : LruCache<String, Bitmap>(cacheSize) {
    override fun sizeOf(key: String, bitmap: Bitmap): Int {
        return bitmap.byteCount / 1024 // KB
    }
}
```

**高频追问：**
1. `Bitmap.recycle()` 在什么情况仍需要调用？（Android 3.0 前必须手动回收；现在只用于立即释放 Native 内存）
2. Glide 如何做 Bitmap 内存管理？（LruCache + BitmapPool 复用 + 生命周期感知自动回收）
3. 超大图（如 10000×10000）如何加载？（BitmapRegionDecoder 分区域加载 + 手势缩放时实时解码对应区域）

**面试官考察点：**
- 对 Bitmap 内存计算的准确理解
- 实际项目中是否遇到过 OOM 并解决
- 对图片加载框架底层原理的认知

**易踩坑：**
- ❌ 从网络获取大图直接加载（需先压缩再显示）
- ❌ 在列表中加载很多大图但不做缓存（快速滑动 OOM）
- ❌ 忘记在 onDestroy 中清理 Bitmap 引用

---

### <a id="android-14"></a>104. 常见内存泄漏场景与排查

**标准回答：**

内存泄漏是指不再使用的对象被其他仍存活的对象强引用，导致 GC 无法回收。Android 中最常见的内存泄漏场景：

**1. 静态变量持有 Activity/View：**
```kotlin
// ❌ 经典泄漏
class MainActivity : AppCompatActivity() {
    companion object {
        var instance: MainActivity? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this // Activity 退出后 instance 仍持有引用
    }
}

// ✅ 解决方案：使用 ApplicationContext 或 WeakReference
```

**2. 非静态内部类 / 匿名内部类：**
```kotlin
// ❌ Handler 泄漏
private val handler = Handler { msg ->
    updateUI() // 持有外部 Activity 引用
}

// ✅ 静态类 + WeakReference
private class SafeHandler(activity: MainActivity) : Handler(Looper.getMainLooper()) {
    private val ref = WeakReference(activity)
    override fun handleMessage(msg: Message) {
        ref.get()?.updateUI()
    }
}
```

**3. 单例持有 Context：**
```kotlin
// ❌ 单例持有 Activity Context
object ToastHelper {
    private var toast: Toast? = null
    fun show(context: Context, msg: String) {
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast?.show()
    }
}

// ✅ 使用 ApplicationContext
fun show(context: Context, msg: String) {
    toast = Toast.makeText(context.applicationContext, msg, Toast.LENGTH_SHORT)
}
```

**4. 资源未关闭：**
```kotlin
// ❌ Cursor / Stream / 数据库 未关闭
// ✅ 使用 try-with-resources 或 use
context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
    // 自动关闭
}
```

**5. RxJava/协程未取消：**
```kotlin
// ❌ RxJava Subscription 未取消
// ✅ CompositeDisposable.clear() / viewModelScope 自动管理

// ❌ 协程未绑定生命周期
// ✅ lifecycleScope / viewModelScope
```

**6. 注册未注销：**
```kotlin
// ❌ BroadcastReceiver / EventBus / Listener 注册后忘记注销
// ✅ 在 onStop/onDestroy 中配对注销
```

**排查工具和方法：**

**1. LeakCanary（自动检测）：**
```kotlin
// build.gradle 添加依赖
debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.x'
// 自动检测 Activity/Fragment 泄漏，生成 hprof 文件
```

**2. Android Profiler（手动分析）：**
- 打开 Memory Profiler → Dump Java Heap
- 按包名过滤 → 查看 Activity 实例数量（退出后应为 0）

**3. MAT (Memory Analyzer Tool)：**
- 导出 hprof → `hprof-conv` 转换 → MAT 打开
- 分析 Dominator Tree、GC Roots 路径

**高频追问：**
1. WeakReference 什么时候被回收？（GC 发现只有 WeakReference 引用时，下次 GC 将其回收；时机不确定）
2. 为什么 ApplicationContext 不会泄漏？（Application 生命周期最长，跟随进程，持有它等同于进程级引用）
3. LeakCanary 的检测原理？（通过 `WeakReference` + `ReferenceQueue` 检测 Activity 是否被正常回收）

**面试官考察点：**
- 是否经历过真实的内存泄漏排查
- 能否快速定位泄漏源
- 对 JVM GC 和引用类型的理解

**易踩坑：**
- ❌ 只关注 Activity 泄漏而忽略 Fragment 泄漏（Fragment 生命周期更复杂）
- ❌ 使用 WeakReference 但不判空（GC 后引用为 null，直接 NPE）
- ❌ MAT 分析时忽略了 Bitmap 等 Native 内存（Android 8.0 前 Native 内存不在 Java Heap 中）

---

### <a id="android-15"></a>105. LeakCanary 原理分析

**标准回答：**

LeakCanary 是 Square 开源的内存泄漏检测库，核心原理基于 **WeakReference + ReferenceQueue**。

**检测流程：**
```
onDestroy()
    ↓
创建 KeyedWeakReference(activity)
    ↓
等待 5 秒（给 GC 时间）
    ↓
GC → ReferenceQueue 中查询
    ↓
在 Queue 中？→ 未泄漏（WeakReference 已被清理）
不在 Queue 中？→ 可能泄漏 → Dump Heap
```

**核心源码解析（简化）：**

```kotlin
// 1. Activity/Fragment 生命周期监听
class ActivityDestroyWatcher : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onActivityDestroyed(activity: Activity) {
        // 创建弱引用 + UUID 标识
        val key = UUID.randomUUID().toString()
        val ref = KeyedWeakReference(activity, key, "", watchUptimeMillis)
        
        // 加入待检测队列
        watchedObjects[key] = ref
        
        // 延迟 5 秒后检查
        checkRetainedExecutor.execute {
            checkRetainedObjects()
        }
    }
}

// 2. 检测是否泄漏
fun checkRetainedObjects() {
    // 手动触发 GC
    Runtime.getRuntime().gc()
    System.runFinalization()
    
    // 从队列中移除已被回收的弱引用
    removeWeaklyReachableObjects()
    
    // 如果还有未清理的引用 → 泄漏
    val retainedKeys = watchedObjects.keys.toList()
    if (retainedKeys.isNotEmpty()) {
        // 再次 GC + 检查（避免假阳性）
        // 如果仍然存在 → 触发 Dump Heap
        dumpHeap()
    }
}

// 3. 分析 Heap Dump
fun analyzeHeap(heapDumpFile: File) {
    // 使用 Shark（LeakCanary 2.x 的解析引擎）
    val heapGraph = Hprof.open(heapDumpFile)
    
    // 从泄漏对象出发，找 GC Roots 最短路径
    val leak = heapGraph.findLeak(retainedKey) { graph ->
        // 分析引用链
        val gcRoot = graph.findShortestPathFromGcRoots()
        LeakTrace(gcRoot, leakingObject)
    }
}
```

**LeakCanary 1.x 和 2.x 的区别：**
| 版本 | 解析引擎 | 依赖 | 初始化 |
|------|---------|------|--------|
| 1.x | HAHA（基于 MAT） | 需手动 init | ContentProvider |
| 2.x | Shark（自研） | 零代码初始化 | ContentProvider 自动 |

**高频追问：**
1. 为什么需要延迟 5 秒才检查？（给 GC 时间回收正常销毁的 Activity；如果立即检查，很多正常对象还在 Finalizer 队列中）
2. `Runtime.getRuntime().gc()` 一定能触发 GC 吗？（不一定，只是建议 JVM 执行 GC；LeakCanary 会多次检查减少假阳性）
3. Shark 引擎和 HAHA 的区别？（Shark 更快、内存占用更低、支持 Android 8.0+ Native 内存分析）

**面试官考察点：**
- 是否了解 LeakCanary 的检测原理而不仅是使用
- 对 GC 和引用类型的理解
- 是否能解释"假阳性"和避免方式

**易踩坑：**
- ❌ 在 release 包中启用 LeakCanary（性能开销 + 敏感信息暴露）
- ❌ 收到泄漏通知但不分析 Root Cause（LeakCanary 给出的引用链需人工确认修复方案）
- ❌ 将 LeakCanary 的 GC 延迟设得太短（增加假阳性）

---

### <a id="android-16"></a>106. ANR 产生原因与排查方法

**标准回答：**

ANR（Application Not Responding）是 Android 系统在应用主线程未能及时响应时弹出的对话框，严重影响用户体验。

**ANR 触发条件（Android 不同版本的阈值）：**

| 触发场景 | 超时阈值 | 说明 |
|---------|---------|------|
| 输入事件（按键/触摸） | 5 秒 | 点击事件无响应 |
| BroadcastReceiver | 前台 10s / 后台 60s | `onReceive` 超时 |
| Service | 前台 20s / 后台 200s | `onCreate`/`onStartCommand` 超时 |
| ContentProvider | 10 秒 | `onCreate`/`query` 超时 |

**常见 ANR 原因：**

**1. 主线程耗时操作：**
```kotlin
// ❌ 主线程网络请求 / 文件 IO
override fun onCreate(savedInstanceState: Bundle?) {
    val data = OkHttpClient().newCall(request).execute() // ANR!
}

// ✅ 协程 / AsyncTask / 线程池
lifecycleScope.launch(Dispatchers.IO) {
    val data = api.getData()
    withContext(Dispatchers.Main) { updateUI(data) }
}
```

**2. BroadcastReceiver 耗时操作：**
```kotlin
// ❌ onReceive 中做耗时操作
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Thread.sleep(15000) // ANR！
    }
}

// ✅ 启动 Service 或 goAsync
override fun onReceive(context: Context, intent: Intent) {
    val pendingResult = goAsync()
    Thread {
        doWork()
        pendingResult.finish()
    }.start()
}
```

**3. 主线程 Binder 调用阻塞：**
```kotlin
// ❌ 主线程调用跨进程方法，Server 端处理慢
override fun onCreate(savedInstanceState: Bundle?) {
    aidlService.heavyOperation() // 主线程 Binder 调用阻塞
}
```

**4. 锁竞争导致主线程阻塞：**
```kotlin
// ❌ 主线程等待子线程持有的锁
val lock = Object()
Thread {
    synchronized(lock) {
        Thread.sleep(10000) // 长时间持锁
    }
}.start()
// 主线程
synchronized(lock) { } // 等待 10 秒 → ANR
```

**ANR 排查方法：**

**1. 查看 ANR 日志：**
```bash
adb pull /data/anr/traces.txt
# 查看 "main" 线程的堆栈，找到阻塞点
```

**2. /data/anr/anr_xxx 文件（Android 10+）：**
```xml
<anr-package>com.example</anr-package>
<subject>Input dispatching timed out</subject>
<stack>
    at com.example.MainActivity.onCreate(MainActivity.kt:42)
    <!-- 主线程堆栈 -->
</stack>
```

**3. StrictMode 开发时检测：**
```kotlin
StrictMode.setThreadPolicy(
    StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .penaltyLog()
        .penaltyDeath() // 开发时直接 Crash
        .build()
)
```

**4. BlockCanary / Android Vitals（线上监控）：**
```kotlin
// BlockCanary 监控主线程耗时方法
// 设置阈值，超过后记录堆栈
```

**高频追问：**
1. ANR 弹窗是怎么触发的？（AMS 通过 `Handler` 向主线程发送 ANR 消息，如果主线程在此之前处理完之前的操作则取消 ANR）
2. 为什么 ANR 日志中常看到 `nativePollOnce`？（这是 Looper 正常的阻塞等待，不是 ANR 原因；真正的问题在于主线程在忙什么）
3. 线上 ANR 如何监控和归因？（监控主线程 `dispatchMessage` 耗时 + 消息队列长度；结合卡顿监控确定具体方法）

**面试官考察点：**
- 对 ANR 机制的全面理解（不仅是"主线程卡顿"）
- 是否会分析 ANR trace 文件
- 线上 ANR 监控和线下排查的能力

**易踩坑：**
- ❌ 只看 ANR trace 中主线程的最后一行（有时最后一行是正常的 Looper.loop，真正的阻塞在前面）
- ❌ BroadcastReceiver 中 `goAsync()` 后忘记调用 `finish()`（还是 ANR）
- ❌ ContentProvider 的 `onCreate` 中连接数据库（整个应用启动变慢）

---

### <a id="android-17"></a>107. OOM 优化与内存监控

**标准回答：**

OOM（OutOfMemoryError）是 Android 开发中的常见难题，需要通过**预防、监控、分析**三步系统化处理。

**OOM 常见原因：**

| 原因 | 典型场景 | 占比 |
|------|---------|------|
| 大图加载 | 未压缩的图片直接加载 | 最高 |
| 内存泄漏 | 累积导致可用内存减少 | 高 |
| 大对象分配 | 超大数组/StringBuilder | 中 |
| 线程数过多 | 每个线程独立栈空间 | 中 |
| 内存碎片 | 大量小对象分配后释放 | 低 |

**1. 预防措施：**

**图片优化：**
```kotlin
// 使用 Glide/Coil 等框架自动管理
Glide.with(context)
    .load(url)
    .override(800, 600) // 限制尺寸
    .format(DecodeFormat.PREFER_RGB_565) // 节省内存
    .skipMemoryCache(false)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .into(imageView)
```

**内存缓存限制：**
```kotlin
// LruCache 限制最大容量
val cacheSize = (Runtime.getRuntime().maxMemory() / 8).toInt()
val cache = LruCache<String, Bitmap>(cacheSize) {
    override fun sizeOf(key: String, value: Bitmap) = value.byteCount
}
```

**大对象复用：**
```kotlin
// StringBuilder 复用
sb.setLength(0) // 而非 new StringBuilder()

// Bitmap 复用池
val pool = BitmapPool(MAX_SIZE)
```

**2. 线上内存监控：**

**LeakCanary（开发/灰度）：**
```kotlin
dependencies {
    debugImplementation 'com.squareup.leakcanary:leakcanary-android:2.x'
}
```

**KOOM（快手开源，线上使用）：**
```kotlin
// KOOM 支持线上 OOM 监控
OOMDetector.getInstance().init(
    OOMDetectorConfig.Builder()
        .setThreshold(0.8f) // 内存占用 80% 时触发
        .setDumpHeap(true)  // 自动 Dump
        .build()
)
```

**3. 内存指标监控：**
```kotlin
class MemoryMonitor {
    fun getMemoryInfo(): MemoryInfo {
        val runtime = Runtime.getRuntime()
        val activityManager = context.getSystemService<ActivityManager>()!!
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        return MemoryInfo(
            usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024,
            maxMemory = runtime.maxMemory() / 1024 / 1024,
            availableMemory = memoryInfo.availMem / 1024 / 1024,
            isLowMemory = memoryInfo.lowMemory
        )
    }
}

// 定时上报
lifecycleScope.launch {
    while (isActive) {
        val info = monitor.getMemoryInfo()
        if (info.usedMemory > info.maxMemory * 0.8) {
            Analytics.report("memory_warning", info)
        }
        delay(5000)
    }
}
```

**4. onTrimMemory 回调处理：**
```kotlin
override fun onTrimMemory(level: Int) {
    when (level) {
        TRIM_MEMORY_RUNNING_MODERATE -> cache.evictSome()
        TRIM_MEMORY_RUNNING_LOW -> cache.evictAll()
        TRIM_MEMORY_UI_HIDDEN -> releaseUnusedResources()
    }
}
```

**高频追问：**
1. 为什么 `Runtime.maxMemory()` 的值在不同设备差异很大？（取决于设备 RAM 大小和 dalvik.vm.heapgrowthlimit 配置，从 16MB 到 512MB 不等）
2. Native OOM 和 Java OOM 有什么区别？（Native OOM 不在 Java Heap，Android Profiler 看不到；需用 `mallinfo` 或 AddressSanitizer 分析）
3. 线上 OOM 监控如何避免 Dump 时的性能影响？（fork 子进程 Dump，KOOM 就是这个策略；主进程不受影响）

**面试官考察点：**
- 是否有完整的 OOM 防控体系经验
- 对内存分析工具的熟练度
- Native 内存和 Java 内存的区分能力

**易踩坑：**
- ❌ 只在开发环境关注 OOM，线上完全没监控
- ❌ 疯狂 `System.gc()`（只是建议，且会导致 STW，反而更卡）
- ❌ 使用 `-XX:+HeapDumpOnOutOfMemoryError` 在线上环境（dump 文件可能几百 MB，撑爆存储）

---

### <a id="android-18"></a>108. ProGuard/R8 混淆原理与规则

**标准回答：**

ProGuard 和 R8 是 Android 的代码优化工具，用于**压缩、优化、混淆**代码。Android Gradle Plugin 3.4+ 默认使用 R8（Google 自研的替代品）。

**三大功能（按执行顺序）：**

| 步骤 | 功能 | 效果 |
|------|------|------|
| Shrinking（压缩） | 移除未使用的类/方法/字段 | 减小 DEX 体积 |
| Optimization（优化） | 内联/简化/删除无用分支 | 提升运行效率 |
| Obfuscation（混淆） | 类名/方法名重命名 | 反编译难度提升 |

**基本配置（proguard-rules.pro）：**
```proguard
# 保持某个类不被混淆
-keep class com.example.model.** { *; }

# 保持类名，但方法/字段可被混淆
-keepnames class com.example.api.** { *; }

# 保持 public 构造函数（JSON 反序列化需要）
-keepclassmembers class * {
    public <init>(...);
}

# 保持枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保持注解
-keepattributes *Annotation*

# 保持行号信息（Crash 堆栈可读）
-keepattributes SourceFile,LineNumberTable

# Retrofit 接口
-keep,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Gson 序列化类
-keepattributes Signature
-keep class com.google.gson.** { *; }
-keep class com.example.dto.** { *; }
```

**R8 相比 ProGuard 的优势：**
| 维度 | ProGuard | R8 |
|------|----------|-----|
| 实现语言 | Java | Kotlin |
| 性能 | 较慢 | 快 2-3 倍 |
| 集成方式 | 独立工具 | AGP 内置 |
| Desugaring | 不支持 | 支持（API desugaring） |
| 输出 | 多步骤（.class → shrink → optimize → obfuscate → .dex） | 一步到位（.class → .dex） |

**常见需要 Keep 的规则模板：**
```proguard
# WebView JS 交互
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    !static !transient <fields>;
}

# 反射调用
-keepclassmembers class * {
    @java.lang.reflect.Field <fields>;
}

# Kotlin 伴生对象
-keep class * extends kotlin.Metadata { *; }
```

**高频追问：**
1. R8 的 Full Mode 和 ProGuard 兼容模式有什么区别？(Full Mode 更激进的优化，可能导致反射调用失败；某些库需要兼容模式）
2. `-keep`、`-keepnames`、`-keepclassmembers` 的区别？（`-keep` 保持类和成员；`-keepnames` 只保持名称不防止压缩；`-keepclassmembers` 只保持类内成员）
3. R8 的 `-whyareyoukeeping` 参数有什么用？（分析为何某个类被保留，帮助精简 Keep 规则）

**面试官考察点：**
- 对混淆原理和影响的全面理解
- 能否处理混淆导致的 Crash（ClassNotFoundException / NoSuchFieldException）
- 对 ProGuard → R8 演进的了解

**易踩坑：**
- ❌ 混淆后反射调用失败（需要 keep 反射目标）
- ❌ Gson/Moshi 序列化字段被混淆（需要 keep 数据类或用 @SerializedName）
- ❌ 只混淆 release 包，debug 包不混淆导致问题延迟发现

---

### <a id="android-19"></a>109. 多渠道打包方案

**标准回答：**

多渠道打包是指为不同应用市场生成带有不同渠道标识的 APK 包，用于统计来源和定制化配置。

**方案演进：**

| 方案 | 原理 | 速度 | 适用 |
|------|------|------|------|
| Gradle ProductFlavors | 完整构建每个渠道 | 极慢（N × 构建时间） | 渠道少 |
| 美团 Walle | 往 APK 签名块写入渠道信息 | 极快（秒级） | 渠道多 |
| VasDolly | V1/V2/V3 签名块写入 | 极快 | V2/V3 签名 |
| PackerNg | 修改 APK 注释区 | 快 | V1 签名 |

**方案一：Gradle ProductFlavors（传统方案）：**
```gradle
android {
    flavorDimensions "channel"
    productFlavors {
        xiaomi {
            dimension "channel"
            buildConfigField "String", "CHANNEL", "\"xiaomi\""
        }
        huawei {
            dimension "channel"
            buildConfigField "String", "CHANNEL", "\"huawei\""
        }
        google {
            dimension "channel"
            applicationIdSuffix ".google"
        }
    }
}
```

**方案二：Walle（推荐，美团开源）：**
```bash
# 写入渠道信息（不破坏签名）
java -jar walle-cli-all.jar put -c xiaomi app-release.apk

# 批量写入
java -jar walle-cli-all.jar batch -c xiaomi,huawei,oppo app-release.apk
```

```kotlin
// 读取渠道信息
val channel = WalleChannelReader.getChannel(context)
```

**Walle 原理（V2 签名块写入）：**
```
APK 结构：
┌────────────────┐
│  ZIP Contents  │
├────────────────┤
│  V1 Signature  │
├────────────────┤
│  V2 Block      │ ← Walle 在此写入自定义 ID-Value
│   └ Channel    │
├────────────────┤
│  V3 Block      │
└────────────────┘
```

**方案三：VasDolly（支持 V3 签名）：**
```kotlin
// 读取
val channel = ChannelReaderUtil.getChannel(context)
```

**构建流程优化：**
```gradle
// 只打包一次 release APK，然后批量写入渠道
// 构建时间：1 次编译 + N 次写入（秒级）
```

**高频追问：**
1. V1、V2、V3 签名有什么区别？（V1: JAR 签名，V2: APK 整体签名，V3: 支持密钥轮换；V2/V3 无法通过修改 ZIP 注释区注入渠道）
2. Walle 写入的信息会影响 APK 完整性吗？（不会，V2 Signing Block 中的 ID-Value 允许添加自定义键值对，不影响签名验证）
3. 渠道包太多导致分发问题怎么解决？（使用云端动态配置 + 少量渠道基准包，下发配置决定行为）

**面试官考察点：**
- 是否了解多渠道打包的实现原理（不只是会用）
- 对 APK 签名和结构的理解
- 对构建效率的关注

**易踩坑：**
- ❌ 用 ProductFlavors 打 100+ 渠道包（构建一次要几个小时）
- ❌ Walle 写入后未验证 APK 能否正常安装（写入失败但仍生成 APK）
- ❌ 混淆后 `WalleChannelReader.getChannel()` 获取不到（需要 keep 相关类）

---

### <a id="android-20"></a>110. Gradle 构建优化

**标准回答：**

Gradle 构建速度直接影响开发效率。系统化的优化可从多个层面入手：

**1. Gradle 配置优化：**

```properties
# gradle.properties
org.gradle.daemon=true                    # 开启 Daemon（默认）
org.gradle.parallel=true                  # 并行构建模块
org.gradle.caching=true                   # 构建缓存
org.gradle.jvmargs=-Xmx4096m -XX:+UseParallelGC  # JVM 内存
org.gradle.configuration-cache=true       # 配置缓存（Gradle 8.0+）
android.enableBuildCache=true             # Android 构建缓存
kotlin.incremental=true                   # Kotlin 增量编译
```

**2. 依赖管理优化：**
```gradle
// ❌ 用 + 号会每次都检查远程
implementation "com.squareup.retrofit2:retrofit:2.+"

// ✅ 固定版本 + 全局管理
implementation libs.retrofit // 配合 version catalog

// settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

**3. 模块化 + 按需编译：**
```gradle
// 只编译改动和相关依赖的模块
// 使用 implementation 代替 api（限制依赖传递）
implementation project(':core:network') // api 改成 implementation
```

**4. Build Scan 分析瓶颈：**
```bash
./gradlew build --scan
# 查看哪个 task 耗时最长、依赖解析是否慢
```

**5. Kotlin 编译优化：**
```gradle
kotlinOptions {
    jvmTarget = "17"
    // 启用增量编译和并行编译
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xskip-prerelease-check")
    }
}
```

**6. Dexing 优化（AGP 3.0+）：**
```gradle
android {
    dexOptions {
        preDexLibraries = true // 预编译库的 dex
        javaMaxHeapSize "4g"
    }
}
```

**常见优化效果汇总：**
| 优化项 | 预期提升 | 副作用 |
|--------|---------|--------|
| Daemon + 缓存开启 | 30-50% | 磁盘占用 |
| 并行构建 | 20-30% | 需确保模块间无隐式依赖 |
| 增量编译 | 50-80%（增量场景） | 偶尔需 clean |
| Configuration Cache | 30-50%（配置阶段） | Gradle 8.0+ 稳定 |

**高频追问：**
1. `api` 和 `implementation` 对构建速度的影响？（`api` 会传递依赖，下游模块变化导致上游重编；`implementation` 隔离依赖）
2. Gradle 的 Configuration 阶段慢如何排查？（`--profile` 看配置时间；检查 `afterEvaluate` 使用；避免 `android.defaultConfig` 中的 I/O 操作）
3. transformClassesWith 占时太久？（自定义 Transform/Gradle Plugin 导致的，考虑用 AGP 的 Transform API 替代方案或迁移到 ksp）

**面试官考察点：**
- 是否有完整的构建优化经验
- 对 Gradle 生命周期的理解
- 实际项目中的构建时间是否关注过

**易踩坑：**
- ❌ 盲目开启所有优化选项（某些组合可能冲突）
- ❌ 把 `clean` 作为日常操作（增量编译完全浪费）
- ❌ 依赖解析特别慢但不配置镜像源

---

### <a id="android-21"></a>111. 组件化与模块化架构设计

**标准回答：**

组件化和模块化是解决大型项目复杂度的核心手段，两者有本质区别：

**组件化 vs 模块化：**
| 维度 | 模块化 (Modularization) | 组件化 (Componentization) |
|------|------------------------|---------------------------|
| **定义** | 按职责拆分包/模块 | 按业务拆分为独立应用 |
| **可独立运行** | 否（Library） | 是（可作为独立 App 运行） |
| **通信方式** | 直接依赖接口 | 路由 + 接口下沉 |
| **目标** | 代码复用、编译提速 | 业务隔离、团队独立开发 |

**组件化架构设计：**

```
shell-app                         ← 壳工程（组装组件）
├── app-component                 ← 调试时独立 App
│
├── business-home                 ← 首页组件
│   ├── home-api                  ← 对外接口（下沉层）
│   └── home-impl                 ← 实现
│
├── business-profile              ← 个人中心组件
│   ├── profile-api
│   └── profile-impl
│
├── common-service                ← 基础服务层
│   ├── network
│   ├── database
│   ├── ui-widgets
│   └── router
│
└── foundation                    ← 基础组件
    ├── common-utils
    └── base-architecture
```

**核心设计方案：**

**1. 路由层（ARouter/DeepLink/自研）：**
```kotlin
@Route(path = "/home/main")
class HomeFragment : Fragment()

// 跳转
ARouter.getInstance().build("/home/main").navigation()

// 服务发现
@Route(path = "/service/user")
class UserServiceImpl : UserService
val userService = ARouter.getInstance().navigation(UserService::class.java)
```

**2. API 下沉（接口隔离）：**
```kotlin
// 各组件暴露接口在 api 模块
// :business-profile:profile-api
interface ProfileService {
    fun getUserInfo(userId: String): UserInfo
}

// :business-profile:profile-impl
class ProfileServiceImpl : ProfileService { /* 实现 */ }

// home 组件通过路由获取 service，不直接依赖 profile-impl
val service = ARouter.getInstance().navigation(ProfileService::class.java)
```

**3. AndroidManifest 合并策略：**
```gradle
// build.gradle (组件的)
// 独立运行时使用完整 Application
if (isRunAlone) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}

// 独立运行和集成时 Manifest 不同
// src/debug/AndroidManifest.xml（独立）
// src/main/AndroidManifest.xml（集成，去掉 LAUNCHER Activity）
```

**4. 资源名冲突解决：**
```gradle
android {
    resourcePrefix "home_" // 所有资源必须加此前缀
}
```

**高频追问：**
1. 组件化如何解决"循环依赖"？（API 下沉：提取公共接口到 api 模块；事件总线解耦；依赖倒置原则）
2. 组件间如何共享数据？（通过 common-service 定义数据模型和接口；通过路由获取其他组件的 Service）
3. 组件独立运行和集成时的数据库怎么处理？（使用 Room 的 schema 迁移；各组件数据库名称带组件前缀）

**面试官考察点：**
- 是否有 10+ 人团队的项目架构经验
- 对解耦和隔离的理解深度
- 是否真正踩过组件化的坑

**易踩坑：**
- ❌ 组件拆得过细（20+ 组件，维护成本远超收益）
- ❌ 所有组件都依赖 common 模块（common 成为新的"上帝模块"）
- ❌ Manifest 合并冲突（各组件声明同一权限但配置不同）

---

### <a id="android-22"></a>112. 热修复与插件化原理

**标准回答：**

热修复和插件化是 Android 动态化技术的两大方向，各有不同的原理和应用场景。

**热修复（Hot Fix）：**

**1. 类替换方案（QQ空间 / Nuwa）：**
```
原理：ClassLoader 的类查找机制
→ PathClassLoader.loadClass()
→ 遍历 dexElements 数组
→ 将修复的 dex 插入到数组最前面
→ 同名类的修复版本优先被加载
```

```java
// 核心实现（简化）
public static void inject(Context context) {
    PathClassLoader classLoader = (PathClassLoader) context.getClassLoader();
    Object dexElements = getDexElements(classLoader);
    
    // 加载补丁 dex
    DexClassLoader patchLoader = new DexClassLoader(patchPath, ...);
    Object patchElements = getDexElements(patchLoader);
    
    // 合并：将补丁 elements 放在前面
    Object mergedElements = combineArray(patchElements, dexElements);
    setDexElements(classLoader, mergedElements);
}
```

**2. 底层替换方案（AndFix / Sophix）：**
- Native 层直接修改方法指针指向修复后的方法
- 立即生效，无需重启
- 兼容性差，受 Android 版本和 ROM 影响

**热修复方案对比：**
| 方案 | 类替换 | so 替换 | 资源替换 | 即时生效 | 兼容性 |
|------|--------|---------|----------|----------|--------|
| Tinker | ✅ | ✅ | ✅ | ❌（需重启） | 最好 |
| Sophix | ✅ | ✅ | ✅ | ✅（部分） | 好 |
| Robust | ✅ | ❌ | ❌ | ✅ | 好 |

**插件化（Dynamic Load）：**

**核心原理（VirtualAPK / RePlugin）：**
```
宿主 App
 ├── PluginManager（插件管理）
 ├── 占坑 Activity（绕过 Manifest 注册限制）
 │   └── StubActivity（预注册的占坑 Activity）
 └── 插件加载
     ├── DexClassLoader 加载插件 dex
     ├── AssetManager 加载插件资源
     └── Hook AMS/Hook Instrumentation
```

**启动插件 Activity 的 Hook 流程：**
```java
// 1. Hook AMS：将插件 Activity 替换为占坑 Activity
@Override
public void startActivity(Intent intent) {
    if (intent.getComponent().isPlugin()) {
        // 替换为占坑 Activity，保存真实信息
        Intent stubIntent = new Intent(this, StubActivity.class);
        stubIntent.putExtra("real_class", realClassName);
        super.startActivity(stubIntent);
    }
}

// 2. Hook ActivityThread.H：将占坑 Activity 替换回来
// ActivityThread.mH.mCallback 拦截 LAUNCH_ACTIVITY 消息
// 还原为真实的插件 Activity
```

**高频追问：**
1. Tinker 为什么需要重启？（类替换方案在 ClassLoader 层面，已加载的类无法卸载，必须冷启动重新加载）
2. 插件化为什么不推荐了？（Android 12+ 对非公开 API 限制越来越严；Google 推 App Bundle + Dynamic Feature 替代）
3. 热修复和插件化的未来趋势？（热修复：App Bundle + 内置更新 → 快速发版；插件化：Dynamic Feature 官方方案替代）

**面试官考察点：**
- 对 ClassLoader、AMS、Activity 启动流程的底层理解
- 是否关注安全性和兼容性
- 对 Google 生态演进的理解

**易踩坑：**
- ❌ 热修复 patch 和基线版本不匹配导致更多崩溃
- ❌ 插件化 Hook 过多，Android 版本升级后大量适配工作
- ❌ 热修复后资源 ID 错乱（插件和宿主资源 ID 冲突）

---

### <a id="android-23"></a>113. WebView 安全与优化

**标准回答：**

WebView 是 Android 中展示 H5 页面的核心组件，在混合开发中尤为重要，需关注**安全**和**性能**两个维度。

**一、安全配置：**

**1. 禁止 JavaScript 执行危险操作：**
```kotlin
webView.settings.apply {
    javaScriptEnabled = true
    allowFileAccess = false           // 禁止访问文件系统
    allowFileAccessFromFileURLs = false   // 禁止 file:// 访问其他文件
    allowUniversalAccessFromFileURLs = false // 禁止 file:// 跨域
    allowContentAccess = false        // 禁止访问 ContentProvider
}
```

**2. SSL 证书校验 + HTTPS 强制：**
```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        // ❌ 绝对不能直接 handler.proceed()
        if (isCertTrusted(error.certificate)) {
            handler.proceed()
        } else {
            handler.cancel()
            showCertWarning()
        }
    }
}
```

**3. JavaScriptInterface 安全加固：**
```kotlin
// ❌ 危险：反射可调用任意方法
webView.addJavascriptInterface(unsafeObj, "bridge")

// ✅ 安全：只暴露需要的方法 + @JavascriptInterface
class SafeBridge(private val context: Context) {
    @JavascriptInterface
    fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
    // 不暴露任何危险操作
}
```

**4. URL 白名单校验：**
```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        val url = request.url.toString()
        return if (isAllowedDomain(url)) {
            false // WebView 自己加载
        } else {
            // 跳转到外部浏览器
            startActivity(Intent(Intent.ACTION_VIEW, request.url))
            true
        }
    }
}
```

**二、性能优化：**

**1. WebView 预创建 + 缓存池：**
```kotlin
object WebViewPool {
    private val pool = LinkedList<WebView>()
    
    fun get(context: Context): WebView {
        return if (pool.isEmpty()) {
            WebView(context.applicationContext).apply {
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setAppCacheEnabled(true)
                    cacheMode = WebSettings.LOAD_DEFAULT
                }
            }
        } else pool.poll()
    }
    
    fun recycle(webView: WebView) {
        webView.loadUrl("about:blank")
        webView.clearHistory()
        pool.offer(webView) // 复用
    }
}
```

**2. 资源拦截与缓存：**
```kotlin
webView.webViewClient = object : WebViewClient() {
    override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
        // 本地拦截静态资源
        val url = request.url.toString()
        if (url.endsWith(".png") || url.endsWith(".js")) {
            val localStream = assets.open("web_cache/${url.md5()}")
            return WebResourceResponse(mimeType, "UTF-8", localStream)
        }
        return null // 正常网络加载
    }
}
```

**3. 渲染优化：**
```kotlin
webView.settings.apply {
    setRenderPriority(WebSettings.RenderPriority.HIGH)     // 渲染优先级
    setEnableSmoothTransition(true)                         // 平滑过渡
    layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // 避免重排
}

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG) // Debug 可调试
}
```

**高频追问：**
1. WebView 加载 H5 白屏的原因有哪些？（初始化耗时、网络慢、JS 阻塞渲染、内存不足、WebView 内核崩溃）
2. WebView 和 Native 通信方案对比？（JSBridge vs JSBundle vs RemoteWebView；JSBridge 最通用）
3. Android 不同版本的 WebView 内核差异？（Android 4.4 前 WebKit，4.4+ Chromium；7.0+ Chrome Stable 可独立更新）

**面试官考察点：**
- WebView 安全漏洞的认知（任意代码执行、XSS、本地文件泄露）
- 对混合开发架构的理解
- WebView 内核版本演进的知识

**易踩坑：**
- ❌ `onReceivedSslError` 直接 `handler.proceed()`（中间人攻击风险）
- ❌ WebView 未在 `onDestroy` 中 `removeAllViews()` 和 `destroy()`（内存泄漏）
- ❌ 在 WebView 的 `shouldOverrideUrlLoading` 中使用 `Intent` 启动但没有过滤（钓鱼攻击）

---

### <a id="android-24"></a>114. APK 瘦身方案

**标准回答：**

APK 瘦身是性能优化的关键环节，直接影响下载转化率和用户存储感知。从多个维度系统性优化：

**1. 代码瘦身：**
```gradle
android {
    buildTypes {
        release {
            minifyEnabled = true     // 混淆 + 压缩
            shrinkResources = true   // 移除无用资源
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
        }
    }
}
```

**2. 资源瘦身：**

**图片优化：**
```xml
<!-- 使用 WebP 替代 PNG（体积减少 30-80%） -->
<!-- 保留 PNG 作为降级方案 -->
```

**矢量图替代位图：**
```xml
<!-- VectorDrawable + AnimatedVectorDrawable -->
<!-- 一套资源适配所有分辨率 -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp" android:height="24dp"
    android:viewportWidth="24" android:viewportHeight="24">
    <path android:fillColor="#000" android:pathData="..." />
</vector>
```

**移除无用资源：**
```bash
# 检测未使用的资源
./gradlew lint
# 移除
./gradlew removeUnusedResources
```

**2. 国际化瘦身：**
```gradle
android {
    defaultConfig {
        resConfigs "zh-rCN", "en" // 只保留需要的语言
    }
}
```

**3. 动态库瘦身：**
```gradle
android {
    defaultConfig {
        ndk {
            abiFilters "arm64-v8a" // App Bundle 下只需保留一种
        }
    }
}
```

**4. App Bundle + Dynamic Feature（Google 官方方案）：**
```gradle
// 基础模块
implementation project(':base')

// 按需下载模块
dynamicFeature project(':feature_chat')
dynamicFeature project(':feature_live')
// Google Play 自动拆分 APK，用户只下载需要的部分
```

**5. so 库压缩：**
```gradle
android {
    buildTypes {
        release {
            // 打包时压缩 so（安装时解压）
            useLegacyPackaging = false // AGP 3.6+ 默认关闭压缩，开启用 true
        }
    }
}
```

**6. 字体文件裁剪：**
```kotlin
// 字体子集化：只保留用到的字符，而非完整字体
// 使用 FontZip 等工具
// 或改用 Downloadable Fonts
```

**瘦身效果参考：**
| 优化项 | 预期减少 | 难度 |
|--------|---------|------|
| 混淆 + 资源压缩 | 15-30% | 低 |
| WebP 替换 PNG | 10-20% | 低 |
| 语言精简 | 1-5MB | 低 |
| so 精简 | 10-20MB | 低 |
| App Bundle | 按模块拆分 | 中 |
| 字体子集化 | 2-10MB | 中 |

**高频追问：**
1. `shrinkResources` 为什么需要 `minifyEnabled` 配合？（shrinkResources 依赖 minify 的代码分析结果，确定哪些资源被引用）
2. 怎么分析 APK 中各部分的大小？（Android Studio → Build → Analyze APK，或使用 `apkanalyzer` 命令行工具）
3. Dynamic Feature 的限制？（Google Play 独占，国内应用商店不支持；需要做好降级方案）

**面试官考察点：**
- 是否有完整的 APK 瘦身实践经验
- 对各优化方案的原理理解
- 是否关注 App Bundle 和 Dynamic Feature

**易踩坑：**
- ❌ `resConfigs` 后第三方 SDK 的语言资源也被过滤（可能影响 SDK 功能）
- ❌ VectorDrawable 在低版本 Android 上的兼容性（需要 `vectorDrawables.useSupportLibrary = true`）
- ❌ 过度压缩图片导致视觉质量下降

---

### <a id="android-25"></a>115. Android 屏幕适配方案

**标准回答：**

屏幕适配是 Android 开发中不可回避的问题，源于 Android 设备的碎片化（屏幕尺寸、分辨率、dpi 多样）。

**核心概念：**

| 概念 | 含义 | 基准 |
|------|------|------|
| **px** | 物理像素点 | 设备相关 |
| **dp/dip** | 密度无关像素 | 1dp = 1px @ 160dpi |
| **sp** | 缩放无关像素（字体） | 同 dp，但受用户字体设置影响 |
| **dpi** | 每英寸像素数 | mdpi=160, hdpi=240, xhdpi=320... |

**主流适配方案：**

**1. 今日头条方案（今日头条团队开源——通过修改 density）：**
```kotlin
// 以设计图 360dp 宽度为基准
// 屏幕实际宽度 / 设计图宽度 = 缩放因子
fun setCustomDensity(activity: Activity, application: Application) {
    val appDisplayMetrics = application.resources.displayMetrics
    val targetDensity = appDisplayMetrics.widthPixels / 360f
    val targetDensityDpi = (targetDensity * 160).toInt()
    
    appDisplayMetrics.density = targetDensity
    appDisplayMetrics.densityDpi = targetDensityDpi
    
    val activityDisplayMetrics = activity.resources.displayMetrics
    activityDisplayMetrics.density = targetDensity
    activityDisplayMetrics.densityDpi = targetDensityDpi
}
// 优点：一套 dp 适配所有屏幕
// 缺点：系统 DisplayMetrics 被全局修改，影响第三方库
```

**2. smallestWidth 限定符（Google 推荐）：**
```
res/
├── values/
├── values-sw320dp/    # 最小宽度 320dp
├── values-sw360dp/    # 最小宽度 360dp
├── values-sw384dp/    # 最小宽度 384dp
├── values-sw392dp/    # 最小宽度 392dp
├── values-sw411dp/    # 最小宽度 411dp
└── dimen.xml          # 在不同目录定义相同名称的不同值
```

```xml
<!-- values-sw320dp/dimen.xml -->
<dimen name="common_padding">12dp</dimen>

<!-- values-sw360dp/dimen.xml -->
<dimen name="common_padding">14dp</dimen>
```

**3. ConstraintLayout + 百分比布局：**
```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <View
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintWidth_percent="0.8"
        app:layout_constraintHeight_percent="0.3"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

**4. Compose 中的适配（现代方案）：**
```kotlin
@Composable
fun AdaptiveLayout() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    
    BoxWithConstraints {
        when {
            maxWidth < 600.dp -> PhoneLayout()
            maxWidth < 840.dp -> FoldableLayout()
            else -> TabletLayout()
        }
    }
}
```

**高频追问：**
1. 为什么不能用 px 做适配？（不同 dpi 屏幕上相同 px 的物理大小不同；ldpi 和 xxxhdpi 相差数倍）
2. 今日头条方案的原理是什么？有什么缺陷？（通过修改系统 Density 实现统一 dp 值映射；缺陷是影响系统级 DisplayMetrics，第三方库可能异常）
3. 横竖屏切换的适配策略？（使用 smallestWidth + layout-land 限定符；或 Single Activity 架构用 Compose 响应式布局）

**面试官考察点：**
- 对屏幕适配本质（density 转换）的理解
- 是否有大屏适配经验（平板/折叠屏）
- 是否了解各方案的优劣势

**易踩坑：**
- ❌ 使用"今日头条方案"但在 Dialog/Toast 中出现布局异常（Dialog 有独立的 DisplayMetrics）
- ❌ 用 sp 做布局尺寸（sp 受系统字体大小影响，不适合做间距和宽高）
- ❌ values-swxxxdp 生成过多尺寸文件但未覆盖边缘 case

---

### <a id="android-26"></a>116. Android 权限机制（运行时权限）

**标准回答：**

Android 6.0（API 23）引入运行时权限机制，将权限分为**普通权限**和**危险权限**，提升了用户隐私控制。

**权限分类：**
| 类型 | 行为 | 示例 |
|------|------|------|
| 普通权限 | 安装时授予，无法撤回 | INTERNET, WAKE_LOCK |
| 危险权限 | 运行时弹窗请求，可撤回 | CAMERA, LOCATION, STORAGE |
| 特殊权限 | 需跳转设置页授权 | SYSTEM_ALERT_WINDOW, WRITE_SETTINGS |

**标准请求流程：**
```kotlin
class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            showPermissionDeniedDialog()
        }
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED -> {
                startCamera() // 已授权
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // 用户曾经拒绝过，展示解释 Dialog
                showRationaleDialog {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }
            else -> {
                // 首次请求
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}
```

**Android 11+ 权限变更：**

**分区存储（Scoped Storage）：**
```kotlin
// Android 10+ 引入，Android 11+ 强制
// ❌ 不再需要 READ_EXTERNAL_STORAGE 读取自己的文件
// ✅ 使用 MediaStore API 或 SAF（Storage Access Framework）

// 读取图片
val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
val cursor = contentResolver.query(uri, null, null, null, null)

// 管理所有文件（特殊场景：文件管理器）
// 需在 Manifest 声明 MANAGE_EXTERNAL_STORAGE
// 并跳转设置页引导用户授权
```

**一次性权限（Android 11+）：**
```kotlin
// 用户可以选择"仅本次"授权 MICROPHONE/CAMERA/LOCATION
// 应用退出后权限自动撤销
// 无需特殊处理，系统自动管理
```

**后台位置权限（Android 10+）：**
```kotlin
// 需要单独请求 ACCESS_BACKGROUND_LOCATION
// 用户需在设置中手动开启"始终允许"
if (checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != GRANTED) {
    // 引导用户去设置页
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        startActivity(this)
    }
}
```

**高频追问：**
1. `shouldShowRequestPermissionRationale` 返回 false 的可能原因有哪些？（首次请求、用户选了"不再询问"、权限策略不允许）
2. 如何批量请求权限？（使用 `RequestMultiplePermissions` 契约，但不推荐一次请求太多，应分组、按需请求）
3. 应用被杀死后权限状态如何恢复？（权限状态由系统持久化，重新启动后无需再次请求）

**面试官考察点：**
- 对 Android 权限演变（6.0→10→11→13→14）的了解
- 对隐私合规的意识
- 实际处理"拒绝+不再询问"的用户引导

**易踩坑：**
- ❌ 启动即请求所有权限（用户体验极差，被拒概率高）
- ❌ 用户拒绝后不停重复请求（应尊重用户选择，提供替代方案）
- ❌ 忽略分区存储导致 Android 11+ 应用崩溃

---

### <a id="android-27"></a>117. 应用启动优化

**标准回答：**

应用启动速度是用户第一体验，Google 的 RAIL 模型建议冷启动在 2 秒内完成。

**启动类型：**
| 类型 | 特征 | 优化目标 |
|------|------|---------|
| 冷启动 | 进程未创建，Application 未初始化 | < 2s |
| 热启动 | 进程和 Activity 都在内存中 | < 500ms |
| 温启动 | 进程存活但 Activity 被回收 | < 1s |

**冷启动流程与优化点：**
```
点击图标
  ↓ (WindowManager 处理)
创建进程
  ↓ (fork + Zygote)
Application.attachBaseContext()  → 优化点1
  ↓
Application.onCreate()           → 优化点2（最大瓶颈）
  ↓
Activity.onCreate()              → 优化点3
  ↓
Activity.onResume()
  ↓
首帧绘制                          → 优化点4
```

**1. Application 初始化优化：**

**异步 + 懒加载：**
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // ✅ 同步初始化：必须在首屏前完成的
        initCrashReport()
        initRouter()
        
        // ✅ 异步初始化：可以延迟的
        initAsync()
    }
    
    private fun initAsync() {
        // 利用 IdleHandler 在主线程空闲时执行
        Looper.myQueue().addIdleHandler {
            initImageLoader()
            initDataBase()
            false // 只执行一次
        }
        
        // 或使用启动器框架
        AppStartup.getInstance()
            .addTask(InitImageTask())
            .addTask(InitDbTask())
            .start()
    }
}
```

**2. 启动器框架设计：**
```kotlin
class TaskDispatcher {
    private val tasks = mutableListOf<Task>()
    
    fun addTask(task: Task): TaskDispatcher {
        tasks.add(task)
        return this
    }
    
    fun start() {
        // 拓扑排序，处理依赖关系
        val sorted = topologicalSort(tasks)
        sorted.forEach { task ->
            if (task.isAsync) {
                task.executeAsync()
            } else {
                task.executeSync()
            }
        }
    }
}

abstract class Task {
    abstract val name: String
    abstract val isAsync: Boolean
    abstract val dependsOn: List<Class<out Task>>
    abstract fun execute()
}
```

**3. 启动白屏优化：**
```xml
<!-- 方法一：禁用预览窗口（不推荐） -->
<item name="android:windowDisablePreview">true</item>

<!-- 方法二：使用自定义 SplashScreen（推荐） -->
<style name="SplashTheme" parent="Theme.AppCompat.NoActionBar">
    <item name="android:windowBackground">@drawable/splash_bg</item>
</style>
<!-- Android 12+ 使用 SplashScreen API -->
```

**4. 启动时间测量：**
```bash
# 系统日志
adb shell am start -W com.example.app/.MainActivity
# TotalTime: 从 startActivity 到首帧完成
# WaitTime: TotalTime + 前一个 Activity pause 时间

# Systrace/Perfetto 精细化分析
```

**高频追问：**
1. MultiDex 对启动的影响如何解决？（5.0 以下用 MultiDex.install 同步阻塞，5.0+ ART 支持预编译；优化：异步加载非首屏 dex）
2. ContentProvider 的初始化为什么会影响启动？（Application.onCreate 之前 ContentProvider.onCreate 就会执行，太多 Provider 拖慢启动）
3. 如何监控线上的启动耗时？（通过 `reportFullyDrawn()` + 打点上报，或 AOP 插桩统计关键方法耗时）

**面试官考察点：**
- 是否有系统化的启动优化经验
- 对启动流程各阶段的了解
- 是否使用 Systrace/Perfetto 做过分析

**易踩坑：**
- ❌ 把所有初始化都移到子线程（导致首页渲染时数据未就绪）
- ❌ SplashScreen Theme 设置了一个大图（启动时加载大图反而更慢）
- ❌ 使用 ContentProvider 自动初始化但没有关闭（App Startup Library 替代）

---

### <a id="android-28"></a>118. 卡顿监控与优化

**标准回答：**

卡顿（Jank）是指 UI 渲染丢帧，用户感知为界面不流畅。Android 以 60fps（16.67ms/帧）为标准，超过即为丢帧。

**卡顿原因分类：**

| 原因 | 占比 | 典型场景 |
|------|------|---------|
| 主线程耗时操作 | 70% | IO、复杂计算、Binder 调用 |
| 过度绘制 | 15% | 多层背景叠加 |
| 布局复杂 | 10% | 深层嵌套 + RelativeLayout |
| GC 频繁 | 5% | 短时间内大量对象创建 |

**1. 线下检测工具：**

**GPU 呈现模式分析（开发者选项）：**
```
设置 → 开发者选项 → GPU 呈现模式分析 → 在 adb shell dumpsys gfxinfo 中
# 查看每帧渲染时间分布
```

**Systrace / Perfetto：**
```bash
# Systrace
python systrace.py -o trace.html gfx input view sched freq

# Perfetto（Systrace 的继任者）
adb shell perfetto -o /data/misc/perfetto-traces/trace -t 10s sched gfx input view
```

**Layout Inspector：**
```xml
<!-- 分析：布局嵌套层级、每个 View 的 measure/layout/draw 耗时 -->
```

**2. 线上监控方案：**

**Looper 消息监控（BlockCanary 原理）：**
```kotlin
object BlockDetector {
    fun install() {
        Looper.getMainLooper().setMessageLogging(object : Printer {
            private var startTime = 0L
            
            override fun println(x: String?) {
                if (x?.startsWith(">>>>> Dispatching") == true) {
                    startTime = System.currentTimeMillis()
                    // 启动一个延时检测线程
                    Thread {
                        while (true) {
                            val duration = System.currentTimeMillis() - startTime
                            if (duration > THRESHOLD) {
                                // 打印主线程堆栈
                                dumpMainThreadStack()
                                break
                            }
                            Thread.sleep(INTERVAL)
                        }
                    }.start()
                }
            }
        })
    }
}
```

**Choreographer 帧回调监控：**
```kotlin
// 监控帧耗时
Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
    override fun doFrame(frameTimeNanos: Long) {
        val droppedFrames = calculateDroppedFrames(frameTimeNanos)
        if (droppedFrames > 3) {
            reportJank(droppedFrames, getCurrentStackTrace())
        }
        Choreographer.getInstance().postFrameCallback(this)
    }
})
```

**3. 常见卡顿优化：**

**布局优化：**
```xml
<!-- ❌ 避免 -->
<RelativeLayout>
    <LinearLayout>
        <RelativeLayout> <!-- 深层嵌套 -->
        </RelativeLayout>
    </LinearLayout>
</RelativeLayout>

<!-- ✅ 使用 ConstraintLayout 扁平化 -->
<ConstraintLayout>
    <View ... layout_constraintTop_toTopOf="parent" />
    <View ... layout_constraintTop_toBottomOf="@id/view1" />
</ConstraintLayout>

<!-- ✅ 使用 ViewStub 延迟加载非首屏内容 -->
<ViewStub android:id="@+id/stub_detail" android:layout="..." />
```

**主线程耗时操作迁移：**
```kotlin
// ❌ SharedPreferences 直接在主线程 commit
prefs.edit().putString("key", "value").commit() // 同步写入磁盘

// ✅ 使用 apply() 或 DataStore
prefs.edit().putString("key", "value").apply() // 异步
```

**高频追问：**
1. 为什么 Looper 监控方案有性能开销？（每次 Message 分发都插入打印逻辑，频繁触发检测线程）
2. 线上卡顿率和用户感知卡顿的区别？（线上卡顿率是统计指标，用户感知卡顿是某个具体操作的不流畅；二者需结合）
3. RecyclerView 卡顿的常见原因？（onBindViewHolder 耗时、item 布局层级深、图片加载未复用、频繁 notifyDataSetChanged）

**面试官考察点：**
- 是否有完整的卡顿监控体系经验
- 对 Systrace 的使用和分析能力
- 是否能从"现象"定位到"根因"

**易踩坑：**
- ❌ 只关注主线程耗时而忽略 RenderThread 的问题
- ❌ 线上开启全量卡顿监控导致性能恶化
- ❌ 用 `notifyDataSetChanged()` 替代精准更新（触发不必要的重绘）

---

### <a id="android-29"></a>119. Android Service 的使用与限制

**标准回答：**

Service 是 Android 四大组件之一，用于在后台执行长时间运行的操作。随着 Android 版本演进，后台执行限制越来越严格。

**Service 类型：**

| 类型 | 启动方式 | 生命周期 | 适用场景 |
|------|---------|---------|---------|
| 前台 Service | `startForegroundService()` | 独立于启动者 | 用户可感知的后台任务（音乐播放、导航） |
| 后台 Service | `startService()` | 独立于启动者 | Android 8.0+ 被严格限制 |
| 绑定 Service | `bindService()` | 跟随绑定者 | C/S 通信（AIDL） |

**前台 Service（现代推荐方式）：**
```kotlin
class MusicService : Service() {
    override fun onCreate() {
        super.onCreate()
        // Android 8.0+ 必须在 5 秒内调用 startForeground
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("正在播放")
            .setContentText("歌曲名称")
            .setSmallIcon(R.drawable.ic_music)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> playMusic()
            ACTION_PAUSE -> pauseMusic()
            ACTION_STOP -> stopSelf()
        }
        return START_STICKY // 被杀后自动重启
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

// Android 13+ 需声明前台服务类型
<service android:name=".MusicService"
    android:foregroundServiceType="mediaPlayback"
    android:exported="false" />
```

**生命周期方法说明：**
```kotlin
// startService → onCreate → onStartCommand → [running] → onDestroy
// bindService → onCreate → onBind → [connected] → onUnbind → onDestroy

// START_STICKY：被杀后自动重启，Intent 为 null（需判空）
// START_NOT_STICKY：被杀后不重启
// START_REDELIVER_INTENT：被杀后重启，重新传递 Intent
```

**后台限制演变：**
```kotlin
// Android 8.0+：后台应用无法 startService（抛 IllegalStateException）
// 替代方案：JobIntentService / WorkManager / 前台 Service

// JobIntentService（兼容方案）：
class MyJobService : JobIntentService() {
    companion object {
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MyJobService::class.java, JOB_ID, work)
        }
    }
    override fun onHandleWork(intent: Intent) {
        doWork()
    }
}
```

**现代替代方案 — WorkManager：**
```kotlin
val request = OneTimeWorkRequestBuilder<SyncWorker>()
    .setConstraints(Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build())
    .build()
WorkManager.getInstance(context).enqueue(request)
```

**高频追问：**
1. `startService` 和 `bindService` 同时使用时怎么停止？（两者都停止时才真正销毁：先 unbindService + stopService 或 stopSelf）
2. Service 运行在哪个线程？可以更新 UI 吗？（默认运行在主线程，可以更新 UI；但长时间任务会 ANR，应使用子线程）
3. IntentService 和 Service 的区别？（IntentService 自动创建子线程、任务队列串行执行、执行完毕后自动停止）

**面试官考察点：**
- 对 Android 后台执行限制演变的了解
- 能否根据场景选择合适的后台执行方案
- 对 WorkManager 替代传统 Service 的理解

**易踩坑：**
- ❌ Android 8.0+ 在后台直接 startService（抛出异常）
- ❌ 忘记在 Service onDestroy 中清理资源（如解绑 BroadcastReceiver）
- ❌ startForeground 在 Notification 未创建频道时崩溃（Android 8.0+ 必须先创建 NotificationChannel）

---

### <a id="android-30"></a>120. BroadcastReceiver 的原理与限制

**标准回答：**

BroadcastReceiver 是 Android 四大组件之一，用于接收系统或应用发送的广播。Android 8.0+ 对隐式广播做了重大限制。

**注册方式：**

**1. 静态注册（Manifest）：**
```xml
<receiver android:name=".BootReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
<!-- Android 8.0+ 大部分隐式广播无法静态注册 -->
```

**2. 动态注册（代码）：**
```kotlin
class MainActivity : AppCompatActivity() {
    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            updateBatteryUI(level)
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(batteryReceiver) // 必须解注册
    }
}
```

**广播类型：**

| 类型 | 发送方式 | 特性 | 顺序 |
|------|---------|------|------|
| 普通广播 | `sendBroadcast()` | 所有接收者同时接收 | 无序 |
| 有序广播 | `sendOrderedBroadcast()` | 按优先级依次接收，可中断 | 有序 |
| 粘性广播 | `sendStickyBroadcast()` | 发送后注册的接收者也能收到 | 已废弃 |
| 本地广播 | `LocalBroadcastManager` | 仅应用内 | 无序 |

**Android 8.0+ 广播限制：**
```kotlin
// ❌ 不再支持静态注册的隐式广播（少数白名单除外）
// 白名单广播示例：
// - ACTION_BOOT_COMPLETED
// - ACTION_LOCALE_CHANGED
// - ACTION_TIMEZONE_CHANGED
// - 其他系统级的必须动态注册

// ✅ 使用 LocalBroadcastManager（已废弃）或替代方案
// 应用内通信推荐：
// - LiveData / StateFlow / SharedFlow
// - EventBus（但要注意生命周期）
// - 回调接口
```

**自定义广播：**
```kotlin
// 发送
val intent = Intent("com.example.app.CUSTOM_ACTION").apply {
    putExtra("data", "value")
    setPackage(packageName) // 显式指定包名（推荐）
}
sendBroadcast(intent)

// 接收（动态注册）
val filter = IntentFilter("com.example.app.CUSTOM_ACTION")
registerReceiver(myReceiver, filter)
```

**onReceive 的限制：**
```kotlin
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // ⚠️ 在主线程执行，必须在 10 秒内返回（前台）或 60 秒（后台）
        // ⚠️ onReceive 返回后 BroadcastReceiver 可能被销毁
        // ✅ 耗时操作：启动 Service 或使用 goAsync()
        val pendingResult = goAsync()
        Thread {
            doHeavyWork()
            pendingResult.finish() // 必须调用
        }.start()
    }
}
```

**高频追问：**
1. 静态注册和动态注册的 Receiver 生命周期有什么区别？（静态：跟随应用进程，由系统管理；动态：跟随注册组件，需手动解注册）
2. 为什么 Android 限制了隐式广播？（大量应用在隐式广播触发时同时启动，导致系统资源紧张、耗电；Google 通过限制来优化）
3. LocalBroadcastManager 为什么被废弃？替代方案？（安全性不如 EventBus/LiveData、不支持跨进程、代码复杂）

**面试官考察点：**
- 对广播机制底层原理的理解
- 是否了解 Android 广播限制及应对方案
- 能否正确选择应用内通信方式

**易踩坑：**
- ❌ 动态注册的 BroadcastReceiver 忘记 `unregisterReceiver`（内存泄漏 + Crash）
- ❌ `onReceive` 中 `goAsync()` 后忘记 `finish()`（系统等待超时 ANR）
- ❌ 静态注册了 Android 8.0+ 不允许的隐式广播（系统不报错但不会收到广播）
