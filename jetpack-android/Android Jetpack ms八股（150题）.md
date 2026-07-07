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
   data class 的设计初衷是纯粹的数据载体。如果一个类没有数据，它就违背了设计初衷，用普通 class 即可。
2. 主构造函数的参数必须用 `val` 或 `var` 声明
   答案：因为参数必须暴露为类的属性。如果不加，它们只是构造函数的局部变量，编译器无法基于它们生成 getter/setter、equals()、hashCode() 以及 componentN() 解构函数。
3. 不能使用 `abstract`、`open`、`sealed`、`inner` 修饰
4. `copy()` 是**浅拷贝**，引用类型字段只复制引用
5. 自动生成的 `equals()` 和 `hashCode()` 只基于主构造函数属性，类体内声明的属性不参与
6. 如果数据类的父类已经定义了 `equals()` 且为 `final`，编译器不会重写
7. 在 data class 中永远不要直接使用数组作为属性。要么用集合类型替代，要么手动重写 equals/hashCode，要么将数组封装成正确的值对象。这也是为什么 Kotlin 官方文档和社区风格指南都反复强调这一点的原因。

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

主要用于消除工具类的样板代码，比如封装 Android 的 View 显隐、SharedPreferences 的链式调用、或者为第三方 SDK 补充不存在的便捷方法，极大地
  提升了业务层的代码可读性。

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

