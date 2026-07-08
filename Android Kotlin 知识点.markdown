# Android Kotlin 面试宝典（Java 开发者视角）


# 一、Kotlin 到底是什么？

**Kotlin 不是另一种 Java。** 它是 JetBrains 开发的、在 JVM 上运行的现代编程语言，Google 在 2019 年宣布 Kotlin 为 Android 开发首选语言。

```
传统 Java 痛点:                   Kotlin 解决:
─────────────────────────────    ──────────────────────────
null 满天飞，NPE 防不胜防        → 空安全类型系统，编译期消灭 NPE
getter/setter 样板代码太多       → data class 一行搞定
匿名内部类冗长 (new Callback)    → Lambda + 高阶函数
没有扩展机制，Utils 类泛滥       → 扩展函数直接给第三方类加方法
线程管理麻烦 (new Thread)        → 协程轻量级并发，可取消
类型推断弱 (必须写类型)           → 类型推断 (var/val)
没有默认参数 (方法重载爆炸)      → 默认参数 + 命名参数
switch 只能匹配枚举/基本类型     → when 表达式，任意类型+智能转换
```

> **一句话**：Kotlin ≈ Java 的语法糖 + 空安全 + 协程 + 函数式编程。**你能读懂 Java 就能读懂 Kotlin，只是写法更短、更安全。**

---

# 二、Kotlin 知识点金字塔

按企业使用频率和面试权重排序：

**★★★★★ 必会（面试必问，工作中天天用）**
- 空安全 (Nullable type / Elvis / Safe call)
- 协程 (Coroutines — launch/async/withContext)
- Flow / StateFlow / SharedFlow
- Sealed class（密封类）
- Extension functions（扩展函数）
- Data class（数据类）
- lateinit vs by lazy

**★★★★ 高频（每周都用，面试经常问）**
- Scope functions（作用域函数 — let/run/apply/also/with）
- object 关键字（三种用法）
- Companion object（替代 Java static）
- 高阶函数 + inline/noinline/crossinline
- 协程异常处理 (CoroutineExceptionHandler)
- 协程取消机制 (isActive / yield / ensureActive)

**★★★ 常用（看项目，面试偶遇）**
- 泛型型变（in / out）
- 委托属性（by 关键字）
- reified 关键字
- Channel
- suspendCoroutine / suspendCancellableCoroutine
- 结构化并发 (structured concurrency)

---

# 三、空安全 — Null Safety（★★★★★）

## Java 的痛

```java
// Java：任何引用都可能为 null，编译器不会帮你检查
String name = getUserName();  // 鬼知道是不是 null?
int len = name.length();      // 运行时崩溃！NullPointerException

// 传统防御：到处写 if (xxx != null)
if (name != null) {
    int len = name.length();  // 漏写一个就崩
}
```

**数据**：Java 中最常见的崩溃类型就是 NPE。

## Kotlin 的解决

```kotlin
// Kotlin：类型系统直接分「可空」和「不可空」
var name: String = "张三"      // 不可空，永远不可能是 null
name = null                    // ❌ 编译错误！String 不能赋 null

var nullableName: String? = "李四"  // 可空，用 ? 标记
nullableName = null                  // ✅ 编译通过

// 操作可空类型时，必须处理 null 情况（编译器强制）
val len = nullableName.length    // ❌ 编译错误！不能直接调用
val len = nullableName?.length   // ✅ 安全调用运算符 ?.，null 时返回 null
```

## 三个核心运算符

```kotlin
// 1. ?. （安全调用运算符）
//    对象为 null 就不往下走，不会 NPE
val upper = user?.name?.uppercase()  // 链式调用，中间任意为 null 就返回 null

// 对比 Java：
//   String upper = user != null && user.name != null
//                 ? user.name.toUpperCase() : null;

// 2. ?: （Elvis 运算符 — 空合并）
//    左侧为 null 就用右侧默认值
val displayName = userName ?: "默认用户"

// 对比 Java：
//   String displayName = userName != null ? userName : "默认用户";

// 3. !! （非空断言 — 慎用！）
//    告诉编译器"我确定它不是 null"，但如果真的是 null → 依然 NPE
val len = name!!.length  // 只在 100% 确定非空时用
```

## 对比表格

| 功能 | Java | Kotlin |
|------|------|--------|
| 声明非空变量 | `String name;`（默认 null） | `var name: String = ""`（必须赋值） |
| 声明可空变量 | 同上（都是可空） | `var name: String? = null` |
| 安全访问 | `if (x != null) x.y()` | `x?.y()` |
| 提供默认值 | `x != null ? x : def` | `x ?: def` |
| 强制非空 | 无（默认就是） | `x!!.y()`（⚠危险） |

## 面试高频

> **Q: Kotlin 如何在编译期消灭 NPE？**
>
> A: Kotlin 的类型系统把「可空」和「不可空」作为类型的一部分。`String` 和 `String?` 是两个不同的类型，`String` 变量永远不能赋值为 null，编译器会强制检查。可空类型必须用 `?.` 或 `?:` 等运算符来处理 null 的情况，否则编译不通过。**这就把运行时 NPE 变成了编译错误。**

---

# 四、Data Class（★★★★★）

## Java 的痛点

```java
// Java：一个简单的"用户"类
public class User {
    private String name;
    private int age;

    // 1. 构造函数
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 2. getter/setter（20+ 行样板代码）
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    // 3. equals() / hashCode()
    // 4. toString()
    // 5. copy() — 没有，需要自己写
    // 总共：50+ 行代码，几乎全是重复劳动
}
```

## Kotlin 的解决

```kotlin
// Kotlin：一行搞定
data class User(val name: String, val age: Int)

// 自动生成：
// 1. 构造函数
// 2. getter（val 不可变）/ setter（var 可变）
// 3. equals() + hashCode()  — 按所有主构造参数比较
// 4. toString()             — 格式: User(name=张三, age=25)
// 5. copy()                 — 复制并修改部分字段
// 6. componentN()           — 解构声明

// 使用
val user = User("张三", 25)
val olderUser = user.copy(age = 30)  // 只改 age，name 不变
val (name, age) = user               // 解构
```

## componentN 和解构声明

```kotlin
data class Result(val code: Int, val msg: String, val data: Any?)

// 解构：把 data class 拆成多个变量
val (code, msg, data) = apiResult
// 等价于：
// val code = apiResult.component1()
// val msg = apiResult.component2()
// val data = apiResult.component3()

// 配合 when 用
when (val (code, msg, data) = apiResult) {
    code == 0 -> showSuccess(data)
    else -> showError(msg)
}
```

## 面试高频

> **Q: data class 和普通 class 的区别？**
>
> A: data class 自动生成 equals()/hashCode()/toString()/copy()/componentN()。普通 class 没有这些。注意 data class 的要求：主构造函数至少有一个参数、不能用 open（不能继承）、不能用 abstract/sealed/inner。

> **Q: data class 的 copy() 是怎么实现的？**
>
> A: copy() 是一个成员函数，内部创建新实例并传入当前字段值和修改值。它是浅拷贝——如果字段是引用类型，只复制引用不复制对象本身。

---

# 五、Sealed Class — 密封类（★★★★★）

## Java 的替代方案

```java
// Java：用枚举或 int 常量表示"状态"，不安全
public class Result<T> {
    public static final int TYPE_LOADING = 0;
    public static final int TYPE_SUCCESS = 1;
    public static final int TYPE_ERROR = 2;

    public int type;
    public T data;
    public String errorMsg;

    // 问题：
    // 1. type 可以赋值为 3、4、5...不存在的值
    // 2. data 和 errorMsg 可能同时为 null
    // 3. when 处理时可能漏掉某个分支
}
```

## Kotlin sealed class

```kotlin
// sealed class：类型安全的有限状态集合
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

// 使用：when 必须穷举所有子类（编译器强制）
fun handleResult(result: Result<List<File>>) {
    when (result) {
        is Result.Loading -> showLoading()        // 必须写
        is Result.Success -> showFiles(result.data) // 必须写
        is Result.Error -> showError(result.message) // 必须写
    }
    // ✅ 编译器帮你检查：三个分支都写了，完美
    // ❌ 如果漏写 Loading 分支 → 编译报错
}
```

## sealed class vs enum class

| 维度 | enum class | sealed class |
|------|-----------|-------------|
| 实例数 | 每个枚举值是**单例** | 每个子类可以有**多个实例** |
| 数据携带 | 不能（枚举值本身是常量） | 子类可以有**不同参数** |
| 适用场景 | 简单状态（ON/OFF） | 复杂状态（Success<T>/Error(msg)） |
| 继承 | 不能继承 | 子类可以继承 |

## 实战：网络请求封装

```kotlin
// 这就是面试题中的 Result 封装
// 比 Java 的 int+if-else 安全得多
sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// ViewModel 里用
class FileViewModel : ViewModel() {
    private val _files = MutableStateFlow<UiState<List<NasFile>>>(UiState.Idle)
    val files: StateFlow<UiState<List<NasFile>>> = _files.asStateFlow()

    fun loadFiles() {
        _files.value = UiState.Loading
        viewModelScope.launch {
            val result = fileRepository.getFileList(1)
            _files.value = when (result) {
                is Result.Success -> UiState.Success(result.data)
                is Result.Error -> UiState.Error(result.message)
                is Result.Loading -> UiState.Loading
            }
        }
    }
}
```

## 面试高频

> **Q: sealed class 和 Java 的枚举有什么区别？**
>
> A: Java 枚举是单例集合，每个枚举值只有唯一实例，不能带数据。sealed class 是类型继承体系，不同子类可以带不同数据（比如 `Success(data)` 和 `Error(msg)` 参数类型不同），而且 when 穷举是编译器强制检查的，不会漏掉分支。

---

# 六、Extension Functions — 扩展函数（★★★★★）

## Java 痛点

```java
// Java：想给 String 加一个"是否为空或空白"的方法
// 方案 1：写成静态工具方法
public class StringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
// 使用：StringUtils.isBlank(str)  — 不够直观

// 方案 2：继承 String — 不行，String 是 final 类

// 方案 3：装饰器模式 — 太笨重
```

## Kotlin 扩展函数

```kotlin
// 直接给 String 类"添加"一个方法
fun String.isBlank(): Boolean {
    return this.trim().isEmpty()
}

// 使用：就像 String 本来就有这个方法一样
val str = "  hello  "
val result = str.isBlank()        // ✅ 直接调，不需要工具类
val result2 = "   ".isBlank()     // true

// 甚至可以给可空类型扩展
fun String?.isNullOrBlank2(): Boolean {
    return this == null || this.trim().isEmpty()
}

val n: String? = null
n.isNullOrBlank2()  // true，不会 NPE
```

## 原理

```kotlin
// 扩展函数不是真的修改了 String 类
// 它编译后是静态方法，第一个参数是被扩展的类实例
//
// fun String.isBlank(): Boolean { return this.trim().isEmpty() }
// ↓ 编译为 ↓
// public static final boolean isBlank(String $this) {
//     return $this.trim().isEmpty();
// }

// 所以：扩展函数不能访问 private 成员
// 扩展函数是「静态解析」的，不是「动态派发」
```

## 面试高频

> **Q: 扩展函数的原理是什么？**
>
> A: 扩展函数是**编译期语法糖**，不真的修改目标类。编译后变成**静态方法**，被扩展的类实例作为第一个参数传入。所以扩展函数是「静态解析」的——调用时取决于变量的**声明类型**，而不是运行时的实际类型。也不能访问目标类的 private 成员。

> **Q: 扩展函数和继承有什么区别？**
>
> A: ① 扩展不需要继承，可以对 final 类（如 String）添加方法 ② 扩展是静态解析的，不参与多态 ③ 扩展不能访问 private 成员 ④ 扩展不能 override，同名的成员函数优先级高于扩展函数。

---

# 七、lateinit vs by lazy（★★★★★）

## Java 的延迟初始化

```java
// Java：延迟初始化（懒加载）
private HeavyObject heavyObject;

public HeavyObject getHeavyObject() {
    if (heavyObject == null) {
        heavyObject = new HeavyObject();  // 首次使用时才创建
    }
    return heavyObject;
}
```

## Kotlin 的两种方式

```kotlin
// 方式 1：lateinit — 延迟赋值（适合依赖注入 / 框架回调）
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    // lateinit：声明时不赋值，用之前保证已赋值
    // 如果没赋值就访问 → UninitializedPropertyAccessException

    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 注意：lateinit 不能用于 Int/Long/Double 等基本类型
    }
}

// 方式 2：by lazy — 真正懒加载（适合计算开销大的属性）
class HeavyService {
    val expensiveData: List<Data> by lazy {
        // 第一次访问时才执行，之后缓存结果
        fetchFromNetwork()  // 只执行一次
    }

    val db by lazy {
        AppDatabase.getInstance(context)  // 单例
    }
}

// 线程安全控制：
val safe by lazy { ... }                     // 默认 LazyThreadSafetyMode.SYNCHRONIZED
val unsafe by lazy(LazyThreadSafetyMode.NONE) { ... }  // 单线程场景，性能更高
```

## 对比表格

| 维度 | lateinit | by lazy |
|------|----------|---------|
| 适用类型 | 引用类型（不能用于 Int/Float） | 任意类型 |
| 初始化时机 | 手动赋值时 | 第一次访问属性时 |
| 线程安全 | 不保证（需自行处理） | 默认线程安全（SYNCHRONIZED） |
| 可重复赋值 | ✅ 可以多次赋值 | ❌ 不可变（val 只读） |
| 访问未初始化 | UninitializedPropertyAccessException | 自动执行初始化 |
| 使用场景 | ViewBinding / DI 注入 | 计算开销大的属性 / 单例 |
| Java 对标 | `HeavyObject obj = null;` 手动赋值 | `HeavyObject obj = getOrCreate()` 懒加载 |

## 面试高频

> **Q: lateinit 和 by lazy 如何选择？**
>
> A: **业务场景决定**。lateinit 适合「赋值时机不由你控制」的场景——比如 ViewBinding 在 onCreate 赋值、Hilt 自动注入。by lazy 适合「延迟计算（计算成本高）+ 只计算一次」的场景——比如数据库实例、网络配置。再说个坑：如果两个 Fragment 共享同一个 lateinit 属性，第二个 Fragment 可能读到第一个赋的值，造成诡异 bug。

> **Q: lateinit 的线程安全问题？**
>
> A: lateinit 本身不保证线程安全。多线程同时访问未初始化的 lateinit 属性，可能同时触发初始化逻辑导致竞态条件。最佳实践是在单一线程（如主线程 onCreate）完成初始化赋值。

---

# 八、Scope Functions — 作用域函数（★★★★）

## Java vs Kotlin

```java
// Java：临时变量到处都是
Person person = getPerson();
String name = person.getName();
int age = person.getAge();
String info = name + "今年" + age + "岁";
System.out.println(info);
```

```kotlin
// Kotlin：五种作用域函数，让代码更紧凑
// 核心区别：上下文对象怎么访问（this vs it）+ 返回值是什么

// run：返回 lambda 结果，上下文用 this
val info = person.run {
    "${name}今年${age}岁"  // this.name, this.age
}

// let：返回 lambda 结果，上下文用 it
person.let {
    println("${it.name}今年${it.age}岁")
}

// apply：返回对象本身，上下文用 this（适合初始化配置）
val intent = Intent().apply {
    action = Intent.ACTION_VIEW
    data = Uri.parse("https://example.com")
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
}

// also：返回对象本身，上下文用 it（适合打印日志、额外操作）
person.also {
    Log.d("TAG", "处理用户: ${it.name}")
}.run {
    // 继续操作
}

// with：不是扩展函数，返回 lambda 结果
with(person) {
    println("$name 今年 $age 岁")
}

// with 典型用法：对同一对象「多次操作」，最后返回 lambda 结果（汇总值）
// 适合：已有对象，连续加工 / 读取它，再得到一个新值
data class User(val name: String, var age: Int, var score: Int = 0)
val user = User("小明", 20)
val report = with(user) {
    age += 1                              // 操作1：修改年龄
    val fullName = "$name·$age"           // 操作2：读取并拼接
    println("处理中: $fullName")          // 操作3：读取并打印
    "用户=$fullName, 成年=${age >= 18}"    // 返回汇总结果（lambda 最后一行）
}
println(report)  // 用户=小明·21, 成年=true
```

## 选择原则

```
        返回 receiver        返回 lambda 结果
        ─────────────        ─────────────────
this     apply                  with / run
it       also                   let
```

| 函数 | 上下文 | 返回值 | 适用场景 |
|------|--------|--------|---------|
| `let` | it | lambda 结果 | 取值不改值：非空检查、链式调用、局部变量 |
| `run` | this | lambda 结果 | 取新值：基于对象/变量，修改+算结果再 |
| `apply` | this | 对象本身 | 改对象+返对象：一般用来初始化配置 |
| `also` | it | 对象本身 | 不改对象+返对象：（日志、校验） |
| `with` | this | lambda 结果 | 对同一对象多次操作：类似run聚焦同一对象 |

### with 与 let 的核心区别

两者**功能等价**——都能对同一对象多次操作并返回 lambda 结果。区别只在语法与空安全配合：

| 维度 | `with(obj) {...}` | `obj.let { it -> ... }` |
|------|-------------------|--------------------------|
| 身份 | 顶层函数，`obj` 作为参数传入 | 扩展函数，在 `obj` 上调用 |
| 访问上下文 | `this`（可省略，直接写 `age`） | `it`（必须写，或改名 `u`） |
| 返回值 | lambda 最后一行 | lambda 最后一行（相同） |
| 空安全配合 | ❌ 无法配合 `?.` | ✅ `obj?.let { }` 天然非空判断 |

**空安全是关键差异**——实战中 `let` 比 `with` 常用的根本原因：

```kotlin
val user: User? = fetchUser()        // 可能为 null

user?.let { println(it.name) }       // ✅ 为 null 时整块跳过

// with 直接这么写会编译报错（user 是 User?，with 需要 User）
with(user) { ... }                   // ❌ 类型不匹配

// with 想配合空安全得绕一圈
user?.let { with(it) { println(name) } }
```

### 经验法则

- **对象一定非空、想少打字、聚焦它的成员调用** → `with`（或 `run`，两者几乎一样，`run` 是扩展版 `with`）
- **对象可能为空、要顺手做非空判断** → `let`（配 `?.`）
- **还要把对象本身返回去做链式调用**（而不是返回结果） → `also` / `apply`

> 一句话：`with` 和 `let` 的"能做什么"一样，区别在"怎么调"和"能不能配 `?.` 做空安全"。

## 实战场景

```kotlin
// let：空安全 + 操作可空对象
user?.let {  // 只有 user 非空时才执行
    saveToDatabase(it)
}

// apply：View 配置（替代 Java 的 builder 模式）
val textView = TextView(context).apply {
    text = "Hello"
    textSize = 16f
    setTextColor(Color.RED)
    gravity = Gravity.CENTER
}

// run：计算结果
val avg = list.run {
    filter { it > 0 }.let { it.sum() / it.size }
}

// also：调试打印
val list = mutableListOf(1, 2, 3).also {
    Log.d("TAG", "原始列表: $it")
}.apply {
    add(4)
}
```

## 面试高频

> **Q: let、run、apply、also、with 怎么选？**
>
> A: 看两点：①你需不需要返回值 ②你想用 this 还是 it。**配置对象用 apply，转换数据用 let，计算结果用 run，附加操作用 also，同一对象多次操作用 with。** 记不住就统统用 let，大部分场景够用。

---

# 九、Object 关键字（★★★★）

Java 中每个概念都独立的，Kotlin 用 `object` 关键字统一了三个场景。

## 用法一：单例

```java
// Java 单例（双重检查锁定 — 20 行样板代码）
public class NetworkManager {
    private static volatile NetworkManager instance;
    private NetworkManager() {}
    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }
}
```

```kotlin
// Kotlin 单例：一个 object 搞定
object NetworkManager {
    private val client = OkHttpClient()

    fun request(url: String): Response {
        return client.newCall(Request.Builder().url(url).build()).execute()
    }
}

// 使用：
NetworkManager.request("https://api.example.com")
```

## 用法二：匿名内部类

```java
// Java 匿名内部类
view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show();
    }
});
```

```kotlin
// Kotlin：用 object 表达式，或者直接 lambda
view.setOnClickListener(object : View.OnClickListener {
    override fun onClick(v: View?) {
        Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show()
    }
})

// 更常见：lambda 简化
view.setOnClickListener { Toast.makeText(context, "点击了", Toast.LENGTH_SHORT).show() }
```

## 用法三：Companion Object（替代 Java static）

```java
// Java：静态成员和方法
public class Constants {
    public static final String BASE_URL = "https://api.example.com";
    public static final int TIMEOUT = 30;

    public static String getApiUrl(String path) {
        return BASE_URL + path;
    }
}
```

```kotlin
// Kotlin：没有 static，用 companion object
class Constants {
    companion object {
        const val BASE_URL = "https://api.example.com"
        const val TIMEOUT = 30

        fun getApiUrl(path: String): String = "$BASE_URL$path"
    }
}

// 调用方式和 Java static 一样
Constants.BASE_URL
Constants.getApiUrl("/users")

// companion object 可以有名字、可以实现接口
class AppDatabase private constructor() {
    companion object Factory {
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
```

> **Java 视角**：`object` 是 Kotlin 的「单例关键字」，用来替代 Java 的 static 成员、匿名内部类和手工单例模式。`companion object` 是「类级别的单例对象」，相当于 Java `static {}` 块 + 静态方法的合体。

---

# 十、高阶函数 + inline（★★★★）

## Java 没有高阶函数

```java
// Java：要传递行为，必须用接口
// 1. 先定义接口
interface OnResult {
    void onSuccess(String data);
    void onError(Throwable e);
}

// 2. 方法接收接口
void fetchData(OnResult callback) { ... }

// 3. 调用：匿名内部类
fetchData(new OnResult() {
    @Override
    public void onSuccess(String data) { ... }
    @Override
    public void onError(Throwable e) { ... }
});
```

## Kotlin 高阶函数

```kotlin
// 高阶函数：参数或返回值是函数的函数

// 1. 方法接收一个函数参数
inline fun fetchData(
    onSuccess: (String) -> Unit,   // 函数类型：接收 String，返回 Unit
    onError: (Throwable) -> Unit
) {
    try {
        val data = networkRequest()
        onSuccess(data)
    } catch (e: Exception) {
        onError(e)
    }
}

// 2. 调用：lambda 语法，简洁直观
fetchData(
    onSuccess = { data -> println("成功: $data") },
    onError = { error -> println("失败: $error") }
)

// 如果最后一个参数是 lambda，可以提出来
fetchData("参数") { data ->
    println("成功: $data")
}
```

## inline / noinline / crossinline

```kotlin
// inline：编译时将函数体的代码直接复制到调用处
// 好处：避免匿名类对象创建（每次 lambda 都是一个匿名类）

// 不加 inline 的话，每次调 highOrderFunc 都创建一个匿名类对象
// 循环 1000 次 = 1000 个匿名类 = GC 压力
inline fun highOrderFunc(action: () -> Unit) {
    println("before")
    action()
    println("after")
}

// noinline：让某个 lambda 参数不内联（可以用作变量传递）
inline fun mixed(
    inlineAction: () -> Unit,      // 内联
    noinline noInlineAction: () -> Unit  // 不内联，可存入变量
) {
    inlineAction()
    val saved = noInlineAction  // ✅ noinline 才能当对象保存
}

// crossinline：不允许 lambda 中的 return 跳出外部函数
inline fun crossInlineExample(
    crossinline action: () -> Unit
) {
    Thread {
        action()  // 在另一个线程中执行
    }.start()
}
// 不加 crossinline 的话，lambda 里的 return 会试图跳出 crossInlineExample，
// 但此时已经在另一个线程了，不可能跳出 → 编译报错
// crossinline 阻止这种非局部 return
```

## 面试高频

> **Q: inline 关键字为什么对高阶函数很重要？**
>
> A: 不加 inline 时，每次 lambda 调用都会创建一个**匿名类对象**（类似 Java 匿名内部类），频繁调用会产生大量临时对象增加 GC 压力。inline 把函数体直接复制到调用处，lambda 参数也被展开，不创建匿名类。**性能敏感场景（如循环 1000 次）inline 效果明显。**

> **Q: crossinline 的作用？**
>
> A: 防止 lambda 中的 `return` 错误地跳出外部函数。当 lambda 被传给另一个执行上下文（如新线程、回调），lambda 里写 `return` 是想返回当前函数还是外部函数？编译器不允许非局部 return，用 crossinline 明确告诉编译器"允许我这样做但禁止跳出去"。

---

# 十一、Coroutines — 协程（★★★★★）

**这是面试中最重要的 Kotlin 知识点，没有之一。**

## Java 异步的问题

```java
// Java：异步操作的历史演变

// 方式 1：原始线程
new Thread(() -> {
    final String result = networkRequest();  // 网络请求
    runOnUiThread(() -> textView.setText(result));  // 切回主线程
}).start();

// 方式 2：回调（回调地狱）
api.login(user, new Callback() {
    void onSuccess(Token token) {
        api.getFiles(token, new Callback() {
            void onSuccess(List<File> files) {
                runOnUiThread(() -> showFiles(files));
            }
            void onError(Throwable e) { ... }
        });
    }
    void onError(Throwable e) { ... }
});

// 方式 3：RxJava（学习成本高）
api.login(user)
    .flatMap(token -> api.getFiles(token))
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(files -> showFiles(files));
```

## Kotlin 协程：像同步一样写异步

```kotlin
// 协程：用写同步代码的方式写异步，编译器自动转成状态机
viewModelScope.launch {
    // 这三行看起来像同步执行，但不阻塞主线程
    val token = api.login(user)         // 挂起，不阻塞
    val files = api.getFiles(token)     // 挂起，不阻塞
    showFiles(files)                    // 自动回到主线程
}
// 不需要回调、不需要线程切换代码、不需要嵌套
```

## 三个核心构建块

```kotlin
// 1. launch：启动协程，没有返回值
viewModelScope.launch {
    // 执行但不关心结果（比如埋点上报）
    analytics.reportEvent("page_view")
}

// 2. async：启动协程，返回 Deferred<T>，可 await 获取结果
viewModelScope.launch {
    val user = async { userRepo.getUser() }       // 并发
    val files = async { fileRepo.getFileList() }  // 并发
    // 两个请求同时执行，await 等待两者完成
    show(user.await(), files.await())
}

// 3. withContext：切换线程上下文，返回结果
viewModelScope.launch {
    val data = withContext(Dispatchers.IO) {
        // 在 IO 线程执行数据库查询
        dao.queryAll()
    }
    // 自动回到 launch 所在的线程（主线程），更新 UI
    textView.text = data
}
```

## Dispatchers 调度器

| Dispatcher | 作用 | Java 对标 |
|-----------|------|-----------|
| `Dispatchers.Main` | 主线程（UI 操作） | `Handler.post` / `runOnUiThread` |
| `Dispatchers.IO` | 文件/网络/数据库 | `new Thread` 或线程池 |
| `Dispatchers.Default` | CPU 密集型计算 | `Executors.newFixedThreadPool` |
| `Dispatchers.Unconfined` | 不指定，随调用线程 | 少用 |

## 实战：NAS 文件列表加载

```kotlin
class FileViewModel @Inject constructor(
    private val fileRepo: FileRepository
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<NasFile>>>(UiState.Idle)
    val state: StateFlow<UiState<List<NasFile>>> = _state.asStateFlow()

    fun loadFiles() {
        viewModelScope.launch {
            _state.value = UiState.Loading

            // withContext 切换线程，返回结果
            val result = withContext(Dispatchers.IO) {
                fileRepo.getFileList(1, 20)
            }

            _state.value = when (result) {
                is Result.Success -> UiState.Success(result.data)
                is Result.Error -> UiState.Error(result.message)
                is Result.Loading -> UiState.Loading
            }
        }
    }
}
```

## 面试高频

> **Q: 协程是轻量级线程吗？为什么"轻量"？**
>
> A: 协程运行在线程上，但一个线程可以运行**成千上万个**协程。因为协程是**用户态调度**（编译器的状态机实现），不是操作系统调度。协程的挂起不阻塞线程，线程可以继续跑其他协程。启动一个协程 ≈ 创建一个状态机对象（几 KB），启动一个线程 ≈ 创建 OS 线程（几 MB）。

> **Q: launch、async、withContext 的区别？**
>
> A: `launch` 无返回值，适用于"执行即忘"（埋点上报）。`async` 返回 Deferred<T>，通过 await 获取结果，适用于并发请求。`withContext` 是**挂起函数**，直接返回结果，适用于切换线程（IO→Main）。选择原则：需要并发+需要结果→async；只需切换线程→withContext；不关心返回值→launch。

---

# 十二、结构化并发（★★★★）

## Java 线程：不可控

```java
// Java：线程一旦启动，就脱离了控制
Thread thread = new Thread(() -> {
    while (true) {
        networkRequest();
        Thread.sleep(1000);
    }
});
thread.start();
// 除了 Thread.stop()（已废弃，不安全），没有好办法取消它
// 如果 Activity 销毁了，这个线程还在后台跑
```

## Kotlin 协程：结构化

```kotlin
// 结构化并发：协程在「作用域」中运行，作用域取消→全部协程取消

// 1. viewModelScope：ViewModel 级别的协程作用域
//    ViewModel 销毁 → viewModelScope 取消 → 内部所有 launch/async 自动取消
class MyViewModel : ViewModel() {
    fun loadData() {
        viewModelScope.launch {      // 子协程
            launch {                  // 孙协程
                repository.fetch()
            }
        }
        // 当用户退出 Activity → ViewModel.onCleared()
        // → viewModelScope.cancel() → 递归取消所有子协程
    }
}

// 2. lifecycleScope：Activity/Fragment 级别的协程作用域
//    Activity 销毁 → lifecycleScope 取消
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            // 这个协程在 Activity 销毁时自动取消
        }
    }
}
```

## 父子协程的取消传播

```
viewModelScope.cancel()
  └── launch (子协程)
        ├── launch (孙协程) → 被取消
        └── async (孙协程) → 被取消
              └── await() 抛出 CancellationException

注意：
- 子协程异常 → 传播给父协程 → 父协程取消所有其他子协程
- 这就是「结构化并发」的含义
- 如果子协程的异常不想传播，用 SupervisorJob
```

## 面试高频

> **Q: 结构化并发解决了什么问题？**
>
> A: 解决了「协程泄漏」问题——传统线程一旦启动就没人管了，Activity 销毁后线程还在后台跑。结构化并发让协程的生命周期和它的作用域绑定，作用域销毁时所有子协程自动取消。**简单说：你在 ViewModel 里 launch，ViewModel 销毁时协程自动取消，不需要你手动关。**

---

# 十三、协程异常处理（★★★★）

## 传播规则

```kotlin
// 规则 1：launch 的异常 → 默认向上传播到父协程
viewModelScope.launch {
    launch {
        throw RuntimeException("出错了")  // 这个异常会传播给父协程
    }
    launch {
        // 也被取消了！因为父协程收到了子协程的异常
    }
}

// 规则 2：async 的异常 → await() 时才抛出
viewModelScope.launch {
    val deferred = async {
        throw RuntimeException("出错了")
    }
    // 这时候还没事，异常还没抛出
    deferred.await()  // 在这里才抛出异常
}

// 规则 3：try-catch 和 CoroutineExceptionHandler
viewModelScope.launch {
    try {
        repository.fetchData()
    } catch (e: Exception) {
        // 捕获协程内的异常
        _state.value = UiState.Error(e.message ?: "未知错误")
    }
}
```

## CoroutineExceptionHandler

```kotlin
// 全局异常处理器（只在 launch 中生效，async 不行）
val handler = CoroutineExceptionHandler { _, throwable ->
    Log.e("TAG", "协程异常: ${throwable.message}")
}

// 只捕获 launch 的未捕获异常（async 的异常要 await()）
viewModelScope.launch(handler) {
    // 发生异常 → handler 捕获 → 不崩溃
    throw RuntimeException("测试异常")
}

// 注意：如果异常被 try-catch 捕获了，handler 就不会触发
```

## 面试高频

> **Q: 协程的异常怎么处理？和 Java try-catch 有什么区别？**
>
> A: 基本原则：**try-catch 捕获协程体内部的异常**（和在 Java 里一样），`CoroutineExceptionHandler` 捕获**未被 try-catch 捕获的异常**（类似 Java 的 `Thread.setDefaultUncaughtExceptionHandler`）。注意：`launch` 的异常默认向上传播，`async` 的异常在 `await()` 时抛出。**实战中，ViewModel 层用 try-catch 捕获异常转为 UiState.Error，不靠全局处理器。**

---

# 十四、Flow — 冷流（★★★★★）

## 为什么需要 Flow？

```java
// Java：数据变化通知
// 方式：Listener 模式
interface OnDataChange {
    void onDataChanged(List<Item> items);
}

class DataManager {
    private List<OnDataChange> listeners = new ArrayList<>();

    void addListener(OnDataChange l) { listeners.add(l); }
    void removeListener(OnDataChange l) { listeners.remove(l); }
    // 问题：手动注册/注销，容易泄漏
    // 问题：数据变化时手动通知，代码分散
}
```

## Flow = 协程里的序列

```kotlin
// Flow：按顺序发射多个值的「冷流」数据流

// 创建 Flow
fun getLatestNews(): Flow<News> = flow {
    while (true) {
        val latest = api.fetchLatestNews()
        emit(latest)              // 发射数据
        delay(5000)               // 5 秒后再次获取
    }
}

// 收集 Flow（冷流：只有调用 collect 才执行）
lifecycleScope.launch {
    viewModel.latestNews.collect { news ->
        // 每次有新数据，自动更新 UI
        newsAdapter.submitList(news)
    }
}
```

## Flow vs LiveData

```kotlin
// LiveData：够用但有限
class MyViewModel : ViewModel() {
    private val _data = MutableLiveData<List<String>>()
    val data: LiveData<List<String>> = _data
    // ① 操作符少（只有 map/switchMap）
    // ② 只能在主线程 observe
    // ③ 不支持背压
}

// Flow：功能完整
class MyViewModel : ViewModel() {
    private val _data = MutableStateFlow<List<String>>(emptyList())
    val data: StateFlow<List<String>> = _data.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            repository.getDataStream()
                .map { it.filter { condition } }    // 各种操作符
                .catch { _data.value = emptyList() } // 异常处理
                .flowOn(Dispatchers.IO)               // 切线程
                .collect { _data.value = it }
        }
    }
}
```

## Flow 操作符链

```kotlin
viewModelScope.launch {
    repository.getFileStream()
        .map { it.toUiModel() }              // 转换
        .filter { it.isVisible() }           // 过滤
        .debounce(300)                        // 防抖（搜索场景）
        .catch { e -> emit(emptyList()) }     // 异常恢复
        .flowOn(Dispatchers.IO)               // 上游在 IO 执行
        .collect { list ->
            _state.value = UiState.Success(list)
        }
}
```

## Room + Flow 自动刷新

```kotlin
@Dao
interface FileDao {
    @Query("SELECT * FROM files ORDER BY modified_time DESC")
    fun getAllFiles(): Flow<List<FileEntity>>
    // Room 自动监听表变化，数据变了立即重新发射
}

// ViewModel 里
class FileListViewModel(repository: FileRepository) : ViewModel() {
    val files: StateFlow<List<FileEntity>> = repository
        .getAllFiles()                                // Flow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    // stateIn：把 Flow 转为 StateFlow
    // WhileSubscribed(5000)：最后一个观察者取消后 5 秒才停止上游
}
```

## 面试高频

> **Q: Flow 是什么？和 LiveData 有什么区别？**
>
> A: Flow 是 Kotlin 协程中的冷数据流，可以发射多个值。**区别**：① Flow 不是生命周期感知的，需要配合 `repeatOnLifecycle` 或 `collectAsStateWithLifecycle`；LiveData 内置生命周期感知 ② Flow 操作符丰富（map/filter/combine/zip 等），LiveData 只有 map/switchMap ③ Flow 支持线程切换（flowOn），LiveData 只能在主线程 observe ④ **新趋势**：Room/DataStore/Retrofit 对 Flow 的支持更好。**推荐原则**：ViewModel 层用 StateFlow，View 层用 `collectAsStateWithLifecycle` 收集。

---

# 十五、StateFlow / SharedFlow — 热流（★★★★★）

## 冷流 vs 热流

```kotlin
// 冷流（Flow）：每个 collector 都从头开始执行
val coldFlow = flow {
    emit("A")
    emit("B")
}
coldFlow.collect { println(it) }  // 打印 A, B
coldFlow.collect { println(it) }  // 又打印 A, B（重新执行）

// 热流（StateFlow/SharedFlow）：数据独立于 collector
// StateFlow：有初始值，保留最新值
val stateFlow = MutableStateFlow("初始值")
stateFlow.value = "新值"          // 直接改
stateFlow.collect { ... }         // 只收到"新值"（如果在此之前已 collect）
stateFlow.collect { ... }         // 也只收到"新值"
```

## StateFlow（推荐替代 LiveData）

```kotlin
class TimerViewModel : ViewModel() {

    // StateFlow = 状态持有者 + 可观察
    // 类似 LiveData，但强制有初始值
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()
    // asStateFlow() 对外暴露只读版本，不让外部修改

    fun start() {
        viewModelScope.launch {
            _uiState.value = TimerUiState(isRunning = true)
            delay(10_000)
            _uiState.value = TimerUiState(isRunning = false)
        }
    }
}

// Activity 里收集（生命周期感知）
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // 只在 STARTED-RESUMED 时收集，DESTROYED 时自动停止
                    updateUI(state)
                }
            }
        }
    }
}
```

## SharedFlow（一次性事件）

```kotlin
// StateFlow 不适合"一次性事件"——因为它是粘性的
// 比如导航事件：StateFlow 保留最新值，屏幕旋转后重新 collect → 又导航一次

// SharedFlow：不保留值（除非设 replay），适合事件
class NavigationViewModel : ViewModel() {
    private val _events = MutableSharedFlow<NavigationEvent>()
    val events: SharedFlow<NavigationEvent> = _events.asSharedFlow()

    fun navigateToDetail(id: String) {
        viewModelScope.launch {
            _events.emit(NavigationEvent.GoToDetail(id))
            // 所有当前正在 collect 的观察者收到事件
            // 后来才 collect 的观察者收不到（replay = 0）
        }
    }
}
```

## 对比表格

| 维度 | LiveData | StateFlow | SharedFlow |
|------|----------|-----------|------------|
| 初始值 | 可选 null | **强制** | 无（可配 replay） |
| 粘性 | ✅ 是 | ✅ 是 | ❌ 否（默认） |
| 生命周期感知 | ✅ 内置 | ❌ 需 repeatOnLifecycle | ❌ 需 repeatOnLifecycle |
| 操作符 | 少 | 全套 Flow | 全套 Flow |
| 线程 | 主线程 | 任意线程 | 任意线程 |
| 适合场景 | View 层 | ViewModel 层状态 | 一次性事件（导航/Toast） |

## 面试高频

> **Q: StateFlow 和 SharedFlow 的区别？**
>
> A: 核心区别：**StateFlow 保留最新值，新 collector 立即收到**（适合状态）。**SharedFlow 不保留值，只向当前活跃 collector 发送**（适合一次性事件）。实战：ViewModel 的 UI 状态用 StateFlow，导航/Toast/Snackbar 用 SharedFlow。**如果导航用 StateFlow，屏幕旋转后会重复触发导航 → 坑。**

---

# 十六、泛型型变 — in / out（★★★）

## Java 的泛型通配符

```java
// Java：? extends T（协变）和 ? super T（逆变）
// 记不住哪个是哪个
List<? extends Animal> list = new ArrayList<Dog>();  // 只能读，不能写
list.add(new Dog());  // ❌ 编译错误

List<? super Dog> list2 = new ArrayList<Animal>();   // 只能写，不能读
Animal a = list2.get(0);  // 只能读到 Object
```

## Kotlin 的 in / out

```kotlin
// Kotlin：out = 只读（生产），in = 只写（消费）

// out（协变）：只能读取，不能写入
// Source<Dog> 是 Source<Animal> 的子类型
interface Source<out T> {
    fun get(): T        // ✅ 生产 T — 可以
    // fun set(item: T) // ❌ 消费 T — 编译错误
}

// in（逆变）：只能写入，不能读取
// Consumer<Animal> 是 Consumer<Dog> 的子类型
interface Consumer<in T> {
    fun accept(item: T)  // ✅ 消费 T — 可以
    // fun get(): T      // ❌ 生产 T — 编译错误
}

// 如果同时要读和写：不变（不加 in/out）
interface ReadWrite<T> {
    fun get(): T
    fun set(item: T)
}
```

## 实战场景

```kotlin
// out：泛型类的泛型参数只出现在「输出」位置
// 比如 Result 的定义
sealed class Result<out T> {       // ← out
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}
// 因为用了 out，可以写：
val result: Result<Any> = Result.Success("hello")  // Success<String> 是 Result<Any> 的子类型

// in：比较器
interface Comparator<in T> {
    fun compare(a: T, b: T): Int
}
val animalComp: Comparator<Animal> = ...
val dogComp: Comparator<Dog> = animalComp  // ✅ Comparator<Animal> 是 Comparator<Dog> 的子类型
```

## 面试高频

> **Q: Kotlin 的 in 和 out 和 Java 的 ? extends / ? super 对应关系？**
>
> A: `out T` = Java `? extends T`（协变，只读不写），`in T` = Java `? super T`（逆变，只写不读）。Kotlin 的写法更简洁：**out = 生产（Producer），in = 消费（Consumer）**。

---

# 十七、委托属性 — by（★★★）

## Java 没有委托属性

```java
// Java：属性访问都需要手动写 getter/setter
// 想做"懒加载"、"观察者模式"、"属性校验"——都要手写
private String name;

public String getName() {
    // 想加个"每次访问都打日志"的功能？需要手写
    Log.d("TAG", "name 被访问了");
    return name;
}

// 想加个"属性变化时通知"？需要手写 Listener
```

## Kotlin 委托属性

```kotlin
// 委托属性：属性的 get/set 逻辑交给另一个对象处理
// 语法：val/var 属性名 by 委托对象

// 1. 标准委托：lazy（懒加载）
val config: AppConfig by lazy {
    // 首次访问时才创建，之后缓存
    AppConfig.loadFromFile()
}

// 2. 标准委托：observable（观察属性变化）—— 赋值「之后」回调通知
// Delegates.observable(初始值) { thisRef, old, new -> ... }
//   "初始值"   —— 属性的起始值
//   lambda 三参数：thisRef(持有者，不用就写 _)、old(变更前的值)、new(变更后的值)
//   触发时机：属性被赋新值【之后】才回调 → 回调里无论写什么，赋值已经发生，无法阻止
var name: String by Delegates.observable("初始值") { _, old, new ->
    Log.d("TAG", "name 从 $old 变为 $new")   // old=旧值, new=新值；常用于日志 / 自动刷新 UI
}

// 3. 标准委托：vetoable（带否决权）—— 赋值「之前」回调，可拒绝
// Delegates.vetoable(初始值) { thisRef, old, new -> 布尔值 }
//   区别：lambda 的【返回值】决定本次赋值是否被允许
//        true  = 允许，属性变为 new
//        false = 拒绝，属性保持 old 不变
//   触发时机：属性被赋新值【之前】先回调 → 所以能拦截（这正是 "veto 否决" 的含义）
var age: Int by Delegates.vetoable(0) { _, _, new ->
    new >= 0   // 返回值：只有 new 非负才允许赋值；负数直接拒绝（age 维持旧值）
}

// 4. 自定义委托：自己写一个「委托对象」，完全掌控属性的读写逻辑
// 任何类只要实现 getValue（读）/ setValue（写），就能放在 `by` 后面
// —— 标准库里的 lazy / observable 本质上也是这种类，只是官方帮你写好了
class LoggedProperty<T>(private var value: T) {

    // getValue：当「读取」被委托的属性时，编译器自动调用它（不是你手动调）
    // 三个参数是 Kotlin 委托协议的「固定签名」，必须照着写，缺一不可：
    //   thisRef  —— 拥有该属性的对象（谁在用这个属性）；用 Any? 兜底最通用
    //   property —— 属性的元信息（KProperty），能拿到属性名、类型等
    //   返回值 T  —— 这次读取应当返回的值
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        Log.d("TAG", "读取 ${property.name} = $value")   // property.name 拿到 "token"
        return value
    }

    // setValue：当「写入」被委托的属性时，编译器自动调用它
    //   多出来的 newValue 参数 —— 这次要赋的新值
    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        Log.d("TAG", "设置 ${property.name} = $newValue")
        value = newValue                                     // 真正把值存起来
    }
}

// 使用：token 的每次 get / set 都被「转发」到 LoggedProperty 的 getValue / setValue
var token: String by LoggedProperty("")   // 初始值 "" 传给构造器；之后每次读写都打日志

// 背后的原理（编译器替你生成的等价 Java 伪代码，理解即可，不用手写）：
//   get() = loggedProperty.getValue(this, ::token)      // this=thisRef, ::token=property
//   set(v) = loggedProperty.setValue(this, ::token, v)  // v=newValue
// 所以 `by` 不是魔法，只是把属性的访问「转发」给了你的委托对象。
// 想要什么行为，就往 getValue / setValue 里写：打日志、校验、缓存、计数……都能做。
```

## 面试高频

> **Q: by lazy 的原理？**
>
> A: `by lazy` 编译后生成一个 `Lazy<T>` 委托对象，第一次访问时执行 lambda 并缓存结果。默认线程安全（SYNCHRONIZED），多线程同时访问时只有一个线程执行初始化。比 Java 的 `synchronized + double-check` 写法简洁得多。

---

# 十八、reified 关键字（★★★）

## Java 泛型的局限

```java
// Java：泛型在运行时被擦除了
public <T> T getIntentExtra(Intent intent, String key) {
    // ❌ 运行时不知道 T 是什么类型
    // return intent.getXxxExtra(key);  // 不知道调用哪个 get 方法
    return (T) intent.getStringExtra(key);  // 强制转换，不安全
}

// 解决：传 Class 参数
public <T> T getIntentExtra(Intent intent, String key, Class<T> clazz) {
    if (clazz == String.class) {
        return (T) intent.getStringExtra(key);
    } else if (clazz == Integer.class) {
        return (T) Integer.valueOf(intent.getIntExtra(key, 0));
    }
    // ... 每个类型写一次
}
```

## Kotlin reified

```kotlin
// reified：inline 函数中「保留泛型类型」
// 必须加 inline 才能用 reified（编译器把类型信息内联到调用处）
inline fun <reified T> getIntentExtra(intent: Intent, key: String): T? {
    return when (T::class) {
        String::class -> intent.getStringExtra(key) as T?
        Int::class -> intent.getIntExtra(key, 0) as T?
        Boolean::class -> intent.getBooleanExtra(key, false) as T?
        else -> null
    }
}

// 使用：不需要传 Class 参数
val name: String? = getIntentExtra(intent, "user_name")
val age: Int? = getIntentExtra(intent, "user_age")
```

## 面试高频

> **Q: reified 的作用？**
>
> A: 解决 Java 泛型类型擦除的问题。在 inline 函数中用 reified 标记泛型参数，编译时会把实际类型内联到调用处，让你可以在函数内部使用 `T::class`。**典型场景**：Gson/Retrofit 的反序列化、startActivity 封装。

---

# 十九、Kotlin 协程进阶与补漏（★★★）

> 本章补齐《Android Jetpack 面试八股（150题）》Kotlin 部分在前面章节未单独展开的考点：协程取消、Channel、supervisorScope、value class、Sequence、infix、Smart Cast。8 年 Java 转 Kotlin 的你在 150 题里会直接碰到这些。

## 协程取消机制（Cancelletion）

- 协程取消是**协作式**的：调用 `job.cancel()` 只是设置取消标志，**不会强制杀线程**，正执行的代码必须自己检查并退出。
- 检查方式：① 调用可挂起函数（如 `delay`、`yield`）时，它们内部会在取消标志置位后抛出 `CancellationException`；② 长时间 CPU 计算需手动 `isActive` 判断。

```kotlin
val job = launch(Dispatchers.Default) {
    while (isActive) {           // 必须检查 isActive 才能响应取消
        doHeavyWork()
        yield()                  // 或 delay()，让取消信号生效
    }
}
job.cancel() // 仅置位标志，doHeavyWork 跑完当前循环才停
```

**注意**：`finally` 里若还要挂起（如释放资源），必须用 `withContext(NonCancellable)` 包裹，否则会直接抛 `CancellationException`。

## Channel — 协程间通信（150题·24）

- `Channel` 类似 `BlockingQueue` 但挂起而非阻塞，用于**多个协程之间**传递数据流（一对多、多对多）。
- 常用类型：`Channel()`（默认无缓冲、`send` 挂起等 `receive`）、`Channel(UNLIMITED)`、`Channel(CONFLATED)`（只保留最新）、`Channel(BUFFERED)`。
- 对比 `Flow`：Channel 是**热**的、点对点通信、需手动关闭；Flow 是**冷**的、单一生产者多订阅、自动管理。

```kotlin
val channel = Channel<Int>()
launch { for (i in 1..3) channel.send(i); channel.close() }
launch { for (x in channel) println(x) } // 1 2 3
```

## supervisorScope vs coroutineScope（150题·25）

| 作用域 | 子协程失败时 | 典型用途 |
|--------|-------------|---------|
| `coroutineScope` | 任一子协程失败 → **取消所有兄弟 + 自己** | 需要「全部成功才算成功」的并行任务 |
| `supervisorScope` | 子协程失败 **不影响其他兄弟**（仅自己处理异常） | ViewModel 里多个独立任务（如下载+上传互不干扰） |

```kotlin
supervisorScope {
    launch { loadFileList() }   // 失败不影响下面
    launch { loadUserInfo() }   // 仍继续执行
}
```

**面试高频**：`viewModelScope` 内部用 `SupervisorJob`，所以一个 ViewModel 里多个 `launch` 互不连坐——这正是用 `supervisorScope` 的原因。

## value class（原 inline class，150题·22）

- `value class Name(val value: String)`：编译期把包装类型**内联到使用处**（消除对象分配开销），运行时就是底层的 `String`，但类型安全（不能把任意 String 当 Name 传）。
- 限制：只能有一个主构造属性；不能参与继承；Java 侧看到的是底层类型。
- 对标 Java 的「基本类型包装」思路，但更安全。`@JvmInline` 标注。

## Sequence — 惰性序列（150题·26）

- `sequence { ... }` / `list.asSequence()`：和 `Iterable` 不同，操作是**惰性、逐个元素**执行的（类似 Java Stream 的 lazy），适合处理大集合或无限序列，避免中间集合开销。
- 对比 `List`：普通集合每步都生成新集合（急切）；`Sequence` 像流水线，元素挨个流过所有操作符。

```kotlin
list.asSequence().filter { it > 0 }.map { it * 2 }.take(3).toList()
```

## infix 函数（150题·28）

- `infix fun Int.add(other: Int) = this + other`，调用可写成 `2 add 3`（省去括号和点）。
- 限制：必须是**成员函数或扩展函数、只有一个参数、不能是 vararg**。标准库 `to`、`shl`、`until` 都是 infix。
- 本质只是语法糖，提升可读性，无性能差异。

## Smart Cast（智能转换，150题·17）

- Kotlin 编译器在判断 `x is String` 后，自动把 `x` 在该分支内**智能转换**为 `String`，无需手动强转。
- 比 Java 的 `instanceof + cast` 安全：编译器保证转换安全（前提变量是 `val` 或局部且未被修改）。
- 常用于 `when` 表达式做类型匹配 + 直接调用子类方法。

```kotlin
when (result) {
    is Success -> show(result.data)   // result 已智能转换为 Success
    is Error   -> toast(result.msg)
}
```

---

# 二十、Kotlin 高频面试题总汇

## 1. Kotlin 对比 Java 的核心优势？

| 维度 | Java | Kotlin |
|------|------|--------|
| 空安全 | ❌ 任何引用都可能 NPE | ✅ 类型系统区分可空/不可空 |
| 代码量 | 冗长（getter/setter） | 简洁（data class / lambda） |
| 异步 | 回调嵌套 / RxJava 学习成本高 | ✅ 协程写同步代码做异步 |
| 扩展 | ❌ 不能给 final 类加方法 | ✅ 扩展函数任意扩展 |
| 函数式 | 有限（Java 8+ lambda） | ✅ 一等公民高阶函数 |
| 智能转换 | ❌ 要手动 cast | ✅ when + is 自动智能转换 |
| 单例 | ✅ 需要样板代码 | ✅ object 关键字一行搞定 |
| 默认参数 | ❌ 方法重载膨胀 | ✅ 默认参数+命名参数 |

## 2. Kotlin 和 Java 可以混合编译吗？

可以。Kotlin 编译成 JVM 字节码，和 Java 完全互操作。同一个项目中可以同时有 `.kt` 和 `.java` 文件，互相调用。

## 3. 为什么用 Kotlin 不用 Java？

除了上述优势外，**生态趋势**是最大原因——Google 官方首选、Jetpack 全部用 Kotlin 写、新库优先支持 Kotlin 协程（Room/Retrofit/DataStore 的 Flow 支持）、面试 JD 明确要求 Kotlin。

## 4. Kotlin 重要但容易踩坑的点？

- **`!!` 非空断言**：能用 `?.` 和 `?:` 就尽量别用 `!!`，它本质上是在 Java 和 Kotlin 边界时用的（如 Java 返回了 null 但你知道不可能 null）
- **`postValue` 覆盖中间值**：连续两次 postValue 只会保留最后一次
- **扩展函数不是真的修改类**：静态解析，不是动态派发，不能 override
- **data class 是浅拷贝**：copy() 不复制引用类型内部数据
- **`companion object` 不是 static**：编译后是内部单例对象，Java 侧通过 `Companion` 访问

## 5. 项目中 Kotlin 和 Java 混合使用要注意什么？

- Java 调 Kotlin 时，方法名可能变成 `getXxx()`（因为 Kotlin 属性自动生成 getter）
- Kotlin 的伴生对象在 Java 中通过 `Class.Companion.xxx()` 访问，可以加 `@JvmStatic` 优化
- Kotlin 的默认参数在 Java 中需要显式传所有参数，可以加 `@JvmOverloads` 自动生成重载

---

# 二十一、Kotlin 知识点速查表

| 知识点 | 一句话总结 | Java 对标 | 面试概率 |
|--------|-----------|----------|---------|
| **空安全** | 类型系统从编译期消灭 NPE | `if (x != null)` 处处检查 | ★★★★★ |
| **协程** | 像写同步代码一样写异步 | `new Thread` / `Handler` / `Callback` | ★★★★★ |
| **Flow** | 协程中的数据流，可发射多个值 | `Listener` / `Observable` | ★★★★★ |
| **StateFlow** | 替代 LiveData 的状态容器，强制初始值 | `LiveData` | ★★★★★ |
| **Sealed class** | 类型安全的有限状态集合 | `int 常量 + if-else` | ★★★★★ |
| **Data class** | 自动生成 equals/hashCode/toString/copy | POJO 50 行 → 1 行 | ★★★★★ |
| **扩展函数** | 给任意类"添加"方法 | `Utils` 工具类 | ★★★★★ |
| **lateinit** | 延迟赋值，适合 DI/框架注入 | 声明对象后手动赋值 | ★★★★ |
| **by lazy** | 懒加载，首次访问时初始化 | 双重检查锁定单例 | ★★★★ |
| **let/apply/run** | 作用域函数，简化临时变量 | 临时变量 + 工具类 | ★★★★ |
| **object** | 单例/匿名类/伴生对象 | static / 匿名内部类 | ★★★★ |
| **companion object** | 替代 Java static | `static` 关键字 | ★★★★ |
| **inline** | 编译时展开，避免匿名类开销 | 无直接对标 | ★★★★ |
| **高阶函数** | 函数作为参数/返回值 | 回调接口 | ★★★★ |
| **in/out** | 泛型协变/逆变 | `? extends / ? super` | ★★★ |
| **by 委托** | 属性的 get/set 委托给其他对象 | 无 | ★★★ |
| **reified** | 内联函数中保留泛型类型 | `Class<T>` 参数 | ★★★ |
| **结构化并发** | 协程的作用域生命周期管理 | `thread.stop()`（已废弃） | ★★★ |
| **协程取消** | 协作式取消，需检查 isActive | `Thread.interrupt()` | ★★★ |
| **Channel** | 协程间通信，热流点对点 | `BlockingQueue`（挂起版） | ★★★ |
| **supervisorScope** | 子协程失败不连坐兄弟 | `Thread` 无此概念 | ★★★ |
| **value class** | 内联包装，零开销又类型安全 | 基本类型包装类 | ★★★ |
| **Sequence** | 惰性序列，逐个流过操作符 | Java Stream lazy | ★★★ |
| **infix** | 中缀调用 `a to b` | 无 | ★★ |
| **Smart Cast** | is 判断后自动转换 | `instanceof + cast` | ★★★ |

---

# 总结

作为 8 年 Java 经验转 Kotlin 的 Android 工程师，你最需要掌握的是：

1. **空安全** → 消灭 NPE，Java 最痛的点
2. **协程 + Flow** → Jetpack 生态的基础设施，Room/Retrofit/DataStore 全部依赖它
3. **Sealed class** → Result 封装、状态管理，面试必问
4. **data class / 扩展函数** → 日常写代码最常用的语法糖
5. **Scope functions** → 写优雅 Kotlin 代码的基础
6. **StateFlow** → Jetpack 官方推荐替代 LiveData

**学习路径建议**：
- Day 1: 空安全 + data class + 扩展函数 + object
- Day 2: 作用域函数 + 高阶函数 + inline
- Day 3-4: 协程（launch/async/withContext + 异常处理）
- Day 5: Flow + StateFlow + SharedFlow
- Day 6: sealed class + 委托 + 泛型
- Day 7: 串联所有知识点，手写一个 MVVM Demo
