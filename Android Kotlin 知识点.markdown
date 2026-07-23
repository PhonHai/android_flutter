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

<a id="toc"></a>
# 二、Kotlin 知识点金字塔（目录）

按企业使用频率和面试权重排序：

**★★★★★ 必会（面试必问，工作中天天用）**

1. [空安全 (Nullable type / Elvis / Safe call)](#s1-null-safety)
2. [协程 (Coroutines — launch/async/withContext)](#s2-coroutines)
3. [Flow — 冷流](#s3-flow)
4. [StateFlow / SharedFlow — 热流](#s4-stateflow-sharedflow)
5. [Sealed class（密封类）](#s5-sealed-class)
6. [Extension functions（扩展函数）](#s6-extension-functions)
7. [Data class（数据类）](#s7-data-class)
8. [lateinit vs by lazy](#s8-lateinit-vs-lazy)

**★★★★ 高频（每周都用，面试经常问）**

9. [Scope functions（作用域函数 — let/run/apply/also/with）](#s9-scope-functions)
10. [object 关键字（三种用法）+ Companion object](#s10-object)
11. [高阶函数 + inline/noinline/crossinline](#s11-higher-order)
12. [协程异常处理 (CoroutineExceptionHandler)](#s12-coroutine-exception)
13. [协程取消机制 (isActive / yield / ensureActive)](#s13-coroutine-cancel)

**★★★ 常用（看项目，面试偶遇）**

14. [泛型型变（in / out）](#s14-variance)
15. [委托属性（by 关键字）](#s15-delegate)
16. [reified 关键字](#s16-reified)
17. [Channel — 协程间通信](#s17-channel)
18. [结构化并发 (structured concurrency)](#s18-structured-concurrency)

**补充知识点**

19. [value class / Sequence / infix / Smart Cast](#s19-extras)

**总汇**

20. [Kotlin 高频面试题总汇](#s20-interview)
21. [Kotlin 知识点速查表](#s21-cheatsheet)

---

<a id="s1-null-safety"></a>
# 三、空安全 — Null Safety（★★★★★） [↑ 返回目录](#toc)

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

<a id="s2-coroutines"></a>
# 四、Coroutines — 协程（★★★★★） [↑ 返回目录](#toc)

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

## 挂起的真正含义（扔物线视角）

扔物线有句大白话：**「协程就是一个线程框架」**。别把它想得多玄乎——它本质是一套帮你方便地切线程的 API。**「挂起（suspend）」**是协程最反直觉的地方，抓住一句话就通了：

> **挂起的是「协程」，不是「线程」，也不是「函数」。**

把 `launch { ... }` 里的代码块当成一个协程。当协程执行到某个 `suspend` 函数（比如 `delay`、`withContext`、网络请求）时：

1. **协程从当前线程「脱离」**——注意是脱离，不是停下来。线程从此不再管这个协程剩下的代码。
2. **线程被解放**：如果是后台线程，它要么被回收、要么去接别的任务（和 Java 线程池的线程一模一样）；如果是 Android 主线程，它立刻回去干正事（每秒 60 次的界面刷新、响应用户点击）。
3. **协程带着剩余代码，去 suspend 函数指定的线程继续跑**（比如 `withContext(Dispatchers.IO)` 指定的 IO 线程）。
4. **suspend 函数执行完后，协程自动把线程切回来**——切到你启动它的那个线程。

Java 对照一下就清楚了：

```java
// 传统 Java：你手动切出去、再手动切回来
new Thread(() -> {
    String result = networkRequest();                   // 在子线程
    runOnUiThread(() -> textView.setText(result));       // 手动切回主线程
}).start();

// 协程：切出去、切回来都是自动的，你只写"顺序代码"
viewModelScope.launch {            // 在主线程启动
    val result = api.login(user)   // 遇到 suspend，自动切到 IO 线程执行
    showFiles(result)              // suspend 结束后，自动切回主线程
}
```

> ⚠️ **最大误区**：`suspend` 函数**不等于**运行在子线程。它只是「可以被挂起」。要不要切线程，由 `Dispatchers` 决定；不指定就停在原来的线程。

## suspend 的底层：CPS 状态机转换

`suspend` 是个**编译器指令**，它自己不创建线程、不挂起任何东西。它的唯一作用是告诉编译器：**「请把我这个函数改写成一个状态机」**。具体做两件事：

1. **改签名**：在参数列表最后，偷偷加一个 `Continuation<T>` 类型的隐藏参数（你可以把它理解成「挂起点之后要继续执行的代码」这个回调）。
2. **改函数体（CPS 转换）**：把你的顺序代码切成一段段，用 `label` 标记执行到哪了。

看一个极简例子反编译后的逻辑（伪代码）：

```kotlin
// 源码
suspend fun simpleDelay(): String {
    delay(1000)
    return "Done"
}

// 编译器生成的状态机（本质，简化版）
class SimpleDelaySM : Continuation<Any?> {
    var label = 0                                  // 记录执行到哪一步
    override fun resumeWith(data: Result<Any?>) {
        when (label) {
            0 -> {
                label = 1
                val r = delay(1000, this)          // delay 也是 suspend
                if (r == COROUTINE_SUSPENDED) return COROUTINE_SUSPENDED  // 挂起！线程释放
            }
            1 -> {
                // 恢复：从这里继续，不会再调 delay
                return "Done"
            }
        }
    }
}
```

执行流程：第一次进 `label=0`，启动 `delay` 定时器，返回 `COROUTINE_SUSPENDED` → **线程被释放去干别的**；1 秒后定时器回调 `resumeWith`，`label=1` → 直接返回 `"Done"`，**线程切回**。

> 💡 **易记口诀**：`suspend` = 编译器把你的代码「切片」，每片用 `label` 标记；挂起 = 保存现场 + 返回 `SUSPENDED`（线程溜了）；恢复 = 跳到对应 `label` 接着跑。这就是为什么协程能「用同步的写法写异步」——它底层就是个自动管理的状态机 + 回调。

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

## CoroutineScope（作用域）核心知识

> 你前面卡住，根子就在这儿：`viewModelScope.launch { }` 里的 `viewModelScope` 到底是什么？为什么 `withContext` 要写 `Dispatchers.IO`？这一节把"作用域"一次讲透。读完这一节，前面 `async` / `withContext` 的疑问会全部串起来。

### 1. 什么是 CoroutineScope

一句话：**CoroutineScope 是「协程的家」——它规定了协程生在哪、死在哪、用什么线程跑。**

```kotlin
// 你写的每一句 launch / async / withContext，前面都必须有个 scope
viewModelScope.launch { ... }   // ← 这个 viewModelScope 就是"家"
lifecycleScope.launch { ... }   // ← Activity/Fragment 里的"家"
GlobalScope.launch { ... }      // ← 全局"家"（不推荐用于 UI）
```

源码层面，`CoroutineScope` 只有一个成员：

```kotlin
public interface CoroutineScope {
    public val coroutineContext: CoroutineContext  // ← 就这一个！
}
```

**所以"作用域"的本质 = 一个 `CoroutineContext`（协程上下文）。** 你调用 `scope.launch {}` 时，新建的协程会成为这个 scope 的**子协程**，并继承它的 context。

**Java 对标**：把 `CoroutineScope` 想成一个「自带生命周期的线程池 + 上下文容器」。传统 Java 里 `ExecutorService.submit(task)` 的线程池不会自动跟着 Activity 销毁；而 `viewModelScope` 会在 ViewModel 销毁时自动 `cancel()` 掉里面所有任务——这就是它存在的意义。

### 2. CoroutineContext：一个装了四样东西的"包裹"

`coroutineContext` 不是单个值，而是一个**组合**（类似 Map），最多装四类元素：

| 元素 | 作用 | Java 对标 |
|------|------|-----------|
| `Job` | 协程的"句柄"，负责取消、等待、生命周期 | `Future` / `Thread` 本身 |
| `CoroutineDispatcher` | 决定协程跑在哪个/哪些线程 | 线程池 `Executor` |
| `CoroutineName` | 调试用的名字 | `Thread.getName()` |
| `CoroutineExceptionHandler` | 未捕获异常的兜底处理 | `Thread.UncaughtExceptionHandler` |

```kotlin
// 自己拼一个 scope（生产里少用，理解用）
val myScope = CoroutineScope(
    Dispatchers.IO                  // 调度器：后台线程
    + SupervisorJob()               // Job：用 Supervisor 隔离子协程失败
    + CoroutineName("my-scope")     // 名字：调试看得到
)
```

> 用 `+` 把元素拼进 context，同类型后者覆盖前者（比如再 `+ Dispatchers.Main` 就换成主线程）。

### 3. 为什么必须有作用域（结构化并发）

没有作用域会怎样？协程一启动就"放飞自我"，**页面关了它还在跑** → 内存泄漏、甚至崩溃（去更新一个已销毁的 View）。

结构化并发（Structured Concurrency）的三大保证：

1. **取消会向下传播**：scope 取消 → 它里面所有子协程全取消。
2. **父协程会等子协程**：`launch { async{...}; async{...} }` 外层会等里面两个都结束才返回。
3. **错误会向上冒泡**：子协程抛异常 → 默认取消父协程（除非用 `SupervisorJob`）。

```kotlin
// 反例：GlobalScope，页面销毁后协程还在跑 → 泄漏 + 可能崩溃
GlobalScope.launch {            // ❌ 没有"家"，永远跟着进程活
    delay(5000)
    textView.text = "done"      // Activity 早销毁了，更新 UI 直接崩
}

// 正例：viewModelScope，页面销毁自动取消
viewModelScope.launch {         // ✅ ViewModel 清空时自动 cancel
    delay(5000)
    _state.value = "done"       // 安全：ViewModel 在就不会更新已死 UI
}
```

**Java 对标**：`ExecutorService` 得手动 `shutdown()` / `shutdownNow()`，忘了就泄漏；协程作用域把"关闭"绑定到生命周期，自动帮你做。（取消/异常的细节见后文「协程取消机制」「结构化并发」两节。）

### 4. 内置作用域一览（记住这 5 个）

| 作用域 | 在哪用 | 什么时候自动取消 | 推荐度 |
|--------|--------|------------------|--------|
| `GlobalScope` | 任意处 | 永不（随进程） | ❌ UI 层禁用，易泄漏 |
| `viewModelScope` | ViewModel 里 | ViewModel `onCleared` | ✅✅✅ 最常用 |
| `lifecycleScope` | Activity / Fragment | `onDestroy` | ✅✅✅ Activity 里用 |
| `rememberCoroutineScope()` | Compose 函数里 | Composable 离开组合 | ✅✅ Compose 专用 |
| `runBlocking` | `main` / 测试 | 跑完内部协程才返回（阻塞当前线程） | ⚠️ 只用于入口/测试 |

```kotlin
// ViewModel 里（最常见）
class MyVm : ViewModel() {
    fun load() = viewModelScope.launch { ... }   // ViewModel 销毁自动取消
}

// Activity / Fragment 里（你问题 2 的正解）
class MyActivity : AppCompatActivity() {
    fun load() = lifecycleScope.launch {          // ← 用 lifecycleScope，不是 viewModelScope
        val data = withContext(Dispatchers.IO) { dao.queryAll() }
        textView.text = data                      // 自动回主线程
    }
}
```

> **关键纠正你问题 2 的误区**：`withContext` 不能裸放在普通方法里，它必须处在协程中；在 Activity 里就是放进 `lifecycleScope.launch { }`。`viewModelScope` 和 `lifecycleScope` 是"同一个东西的两个绑定版本"——都包了 `Dispatchers.Main.immediate`，区别只在**绑定到谁的生命周期**（ViewModel vs LifecycleOwner）。

### 5. 作用域 × 调度器：子协程去哪个线程？

这是你问题 2 的核心：**`withContext(Dispatchers.IO)` 为什么非写不可？**

```kotlin
viewModelScope.launch {
    // launch 没指定调度器 → 继承 viewModelScope 的 → Dispatchers.Main（主线程！）
    val data = withContext(Dispatchers.IO) {
        dao.queryAll()        // ← withContext 临时切到 IO 后台
    }                          // ← 出了大括号，自动切回 Main
    textView.text = data      // ← 在主线程，安全更新 UI
}
```

规则：
- **子协程默认继承 scope 的调度器**。`viewModelScope` / `lifecycleScope` 默认都是 `Dispatchers.Main.immediate`（主线程）。
- 所以 `launch { }` 里**默认就在主线程跑**。`dao.queryAll()` 这种耗时操作若不切走，就会**卡主线程 / ANR**。
- `withContext(Dispatchers.IO)` 的作用：**临时**把这段切到后台，执行完**自动切回**原来的主线程。这就是它"防止跑主线程"的方式。
- 想让整个协程都在后台跑，也可以 `viewModelScope.launch(Dispatchers.IO) { ... }`，但其中更新 UI 就得再 `withContext(Main)`。

```kotlin
// 等价写法对比
viewModelScope.launch(Dispatchers.IO) {            // 整段在 IO
    val data = dao.queryAll()
    withContext(Dispatchers.Main) { textView.text = data }  // 更新 UI 再切回 Main
}
// vs（更直观，推荐）
viewModelScope.launch {                             // 主线程起步
    val data = withContext(Dispatchers.IO) { dao.queryAll() }  // 只这块去 IO
    textView.text = data                            // 主线程
}
```

### 6. runBlocking：桥接"阻塞世界"的入口

`runBlocking` 是唯一会**阻塞当前线程**直到内部协程全完成的协程启动器——它和 `suspend` 的"不阻塞"正好相反。

```kotlin
fun main() = runBlocking {        // 阻塞 main 线程，直到里面跑完（程序入口/测试用）
    launch { delay(1000); println("hello") }
    println("world")              // 先打印 world，1 秒后 hello
}
```

- **用途**：`main` 函数、单元测试。生产代码的 UI 层**绝对别用**（会卡死主线程）。
- **Java 对标**：类似 `CountDownLatch.await()` 把主线程拦住等子任务——`runBlocking` 就是协程版的"等所有任务完成再继续"。

### 7. 易记口诀

- **作用域 = 协程的家**：规定了"生在哪、死在哪、用什么线程"。
- **context = 四件套**：Job（生命周期）+ Dispatcher（线程）+ Name（调试）+ Handler（异常）。
- **UI 层用 viewModelScope / lifecycleScope，别用 GlobalScope**：避免泄漏。
- **默认在主线程**：`launch {}` 不写调度器就跑在 scope 的 Main 上，`withContext(IO)` 才去后台。
- **withContext = 临时出差，自动回乡**：去 IO 干完活，自动回 Main 更新 UI。

## Dispatchers 调度器

| Dispatcher | 作用 | Java 对标 |
|-----------|------|-----------|
| `Dispatchers.Main` | 主线程（UI 操作） | `Handler.post` / `runOnUiThread` |
| `Dispatchers.IO` | 文件/网络/数据库 | `new Thread` 或线程池 |
| `Dispatchers.Default` | CPU 密集型计算 | `Executors.newFixedThreadPool` |
| `Dispatchers.Unconfined` | 不指定，随调用线程 | 少用 |

## 实战：NAS 文件列表加载

```kotlin
// @Inject constructor：Hilt（依赖注入框架）的注解，标注在主构造函数上
// 作用：告诉 Hilt「这个类的实例由你来创建，构造函数里声明的依赖由你自动注入」
//   - 本例声明了 private val fileRepo: FileRepository
//   - Hilt 会先准备好 FileRepository（它自己也要能被 Hilt 提供），再传进构造函数
//   - 你不用手动 new FileViewModel(...)，框架替你组装好，谁需要就注入给谁
// Java 对照：传统写法是你自己 new 并拼依赖 ——
//            FileViewModel vm = new FileViewModel(new FileRepository());
//            依赖一多就变成「依赖地狱」；用 @Inject 后依赖由框架统一管理，解耦且易测试
// 注意：@Inject 是 Hilt/DI 机制，不是协程语法；本 demo 只是用协程(loadFiles)顺带展示 Hilt 注入 Repository
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

<a id="s3-flow"></a>
# 五、Flow — 冷流（★★★★★） [↑ 返回目录](#toc)

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

## 冷流的本质（为什么不消费不生产）

Flow 是**冷流（Cold Stream）**，这是它和很多数据流最大的区别，也是最容易记混的点：

- **只有调用 `collect { }` 时，上游代码才会执行**；你不收集，它就不产生任何数据——像一条「懒加载的流水线」。
- **每个收集者都触发一次完整的生产**：同一个 Flow 被 collect 两次，上游的 `flow { ... }` 代码就跑两遍，两份独立的数据序列，互不干扰。

Java 对照：非常像 Java 8 的 `Stream`——你不调用终止操作（如 `collect()`/`reduce()`），中间操作一行都不执行。区别是 Flow 是**异步的、可挂起的** Stream，能跨线程发射。

```kotlin
val newsFlow = getLatestNews()      // 这行只是"声明"流，啥都没发生
lifecycleScope.launch {
    newsFlow.collect { ... }        // ① 第一次 collect → 上游从头跑一遍
}
lifecycleScope.launch {
    newsFlow.collect { ... }        // ② 第二次 collect → 上游又从头跑一遍
}
```

> 🔑 口诀：**冷流 = 懒 + 私有**。不消费不生产，谁消费谁独占一份。热流（StateFlow/SharedFlow）正好相反——不消费也生产、多订阅者共享同一份。

## 背压：suspend 天然解决

**背压（Backpressure）**：生产者发数据的速度，远大于消费者处理的速度，数据越堆越多 → 内存暴涨 → OOM。

- **RxJava** 要解决这个问题，得手动选策略：`onBackpressureBuffer`（缓存）、`onBackpressureDrop`（丢弃）……
- **Flow** 靠 `suspend` 自动搞定：`emit()` 本身是一个**挂起函数**。下游 `collect` 正在忙（处于挂起中），上游的 `emit` 就**自动跟着挂起**，等下游腾出手才继续发。根本不需要你写溢出策略。

```kotlin
flow {
    while (true) {
        emit(produceItem())   // 如果下游慢，这里自动挂起，不会堆积
    }
}.collect { item ->
    delay(1000)               // 下游处理很慢
    process(item)
}
```

常用背压操作符（仍是基于 suspend，只是换个策略）：
- `buffer(n)`：开一个 n 大小的缓冲，生产消费并行，提升吞吐
- `conflate()`：只保留最新值，丢弃中间来不及处理的（类似 LiveData 行为）
- `collectLatest { }`：新数据到来时，立刻取消正在进行的旧数据处理

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

## 与 RxJava 对照

很多 Android 老项目是 RxJava 写的，面试常问「Flow 和 RxJava 有啥区别」。本质上是同一件事（响应式数据流）的两套实现：

| Kotlin Flow | RxJava | 说明 |
|-------------|--------|------|
| `Flow`（冷流） | `Observable` / `Flowable` | 数据流本体 |
| `StateFlow` | `BehaviorSubject` | 持有最新状态，新订阅立即拿到 |
| `SharedFlow` | `PublishSubject` / `ReplaySubject` | 事件广播 |
| `flow { emit() }` | `Observable.create()` | 创建流 |
| `map` / `filter` / `debounce` / `flatMap` | 同名操作符 | 操作符基本一一对应 |
| `flowOn(Dispatchers.IO)` | `subscribeOn(Schedulers.io())` + `observeOn()` | 切线程（flowOn 只影响上游） |
| `suspend` 自动背压 | `onBackpressureBuffer/Drop` | 背压处理 |
| `CoroutineScope` 取消 | `Disposable.dispose()` | 取消订阅 |
| `catch { }` | `onErrorResumeNext()` | 异常恢复 |

> 一句话：Flow 不是来「取代」RxJava 所有功能的，而是用协程的 `suspend` 把响应式编程**变简单**了——背压靠挂起自动解决，取消靠作用域自动管理，不用你手动 `dispose`。

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

<a id="s4-stateflow-sharedflow"></a>
# 六、StateFlow / SharedFlow — 热流（★★★★★） [↑ 返回目录](#toc)

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

<a id="s5-sealed-class"></a>
# 七、Sealed Class — 密封类（★★★★★） [↑ 返回目录](#toc)

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

## Java enum：比 int 常量好，但仍受限

```java
// 用枚举替代 int 常量：type 只能取枚举定义的值，不能随意赋 3/4/5
public enum ResultType { LOADING, SUCCESS, ERROR }

public class Result<T> {
    public ResultType type;
    public T data;
    public String errorMsg;

    // 处理时若漏写分支，Java 的 switch 不会编译报错，只能靠 default 兜底
    void handle() {
        switch (type) {
            case LOADING: showLoading();   break;
            case SUCCESS: showData(data);  break;
            case ERROR:   showError(errorMsg); break;
            // 漏掉某个分支 → 编译不报错，最多走 default（不写 default 也只是告警）
        }
    }
}
```

- ✅ 比 int 常量好：`type` 只能取枚举定义的有限值，杜绝了「赋任意整数」的低级 bug。
- ❌ 但仍受限（这正是 Kotlin `sealed class` 要解决的）：
  1. **每个枚举值是单例**，所有地方共享同一实例，无法像 `Success<T>(data)` 那样各自携带**不同类型**的数据；
  2. 枚举的构造参数在**所有枚举值间是同一套**，不能让 `Success` 带 `data`、`Error` 带 `message` 这种**不同形参**；
  3. `switch` 处理枚举**不强制穷举**，漏分支编译期不报（除非配 `default` 或第三方检查），仍有漏处理风险。

> 一句话：Java enum 解决了「取值安全」，但没解决「带异构数据 + 编译期强制穷举」——后者才是 `sealed class` 的杀手锏。

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

## Java 真的没有密封类吗？

要分两说，这点对 8 年 Java 经验的你很重要：

**老 Java（8 及以前，也是 Android 最常见的环境）**：没有 sealed，只能用「枚举 + 抽象类组合」或「int 常量」模拟，类型不安全（前面 Java 痛点那段已经演示了 int 常量的坑）。

**Java 17+**：终于引入了 `sealed interface` + `permits` + `record`，几乎是 Kotlin sealed class 的一一对应：

```java
// Java 17 的密封接口 + record（不可变数据载体）
public sealed interface Result<T> permits Success, Error, Loading {
}
public record Success<T>(T data) implements Result<T> {}
public record Error(String message, Throwable exception) implements Result<T> {}
public record Loading() implements Result<T> {}

// 处理时编译器强制穷举（像 Kotlin 的 when）
static <T> void handle(Result<T> r) {
    if (r instanceof Success<T> s) { System.out.println(s.data()); }
    else if (r instanceof Error e) { System.out.println(e.message()); }
    else if (r instanceof Loading) { System.out.println("loading"); }
    // 漏掉任意一个分支 → 编译告警/错误
}
```

> ⚠️ 现实：**Android 的 minSdk 限制**让绝大多数项目用不上 Java 17 的 sealed。所以 Kotlin 的 `sealed class` 在 Android 里仍是实现「类型安全状态树」的首选方案。

## 穷举的原理 & 易记口诀

为什么 `when(result)` 能**强制**你写全所有分支？因为编译器知道 `sealed class` 的**全部子类**——它们必须定义在同一 module 的同一 package 内（早期版本曾要求同文件，位置规则详见后文「子类的位置规则」一节），编译器在编译期就能数清楚有几个"叶子"。

这带来一个 Java `int` 常量/`enum` 都给不了的好处：

- **漏写分支 → 编译报错**（或你主动写 `else` 兜底）
- **以后新增一个子类**（比如 `Result.Timeout`）→ 所有 `when` 处**立刻编译报错**提醒你去补，不会漏到线上

对比一下三种方案的安全性：

| 方案 | 漏分支的后果 | 类型安全 |
|------|--------------|----------|
| Java `int` 常量 + `switch` | 运行时走到 `default`，可能 NPE/逻辑错误 | ❌ 可赋任意值 |
| Java `enum` | 编译可查（但每个值只能带固定数据） | ✅ 但受限 |
| Kotlin `sealed class` | **编译期强制穷举**，新增子类自动报警 | ✅ 且子类可带不同数据 |

> 🔑 易记口诀：**密封类 = 封闭的继承树 + 编译期强制穷举**。比 `if-else`/`int` 常量安全得多，是 Kotlin 里封装「成功/失败/加载/超时」这类有限状态的终极武器。

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

## when 作表达式 vs 语句：穷举是有条件的（★大坑）

> ⚠️ 本文前面说「`when(result)` 会强制穷举」——**这句话不完整，是最容易在面试翻车的地方**。穷举只在 `when` 作为**表达式**时才被硬强制；作为语句时有历史遗留的宽松期。

```kotlin
// ① when 作为「表达式」：有返回值 / 被赋值 → 强制穷举，漏分支必编译报错
val msg = when (result) {
    is Result.Success -> "ok"
    is Result.Error   -> "fail"
    is Result.Loading -> "loading"
}   // 漏掉任意一个分支 → 编译错误 ✅

// ② when 作为「语句」：不取返回值 → Kotlin 1.6 之前【不强制】，漏分支静默通过！
when (result) {
    is Result.Success -> showFiles()
    is Result.Error   -> showError()
    // 漏了 Loading，1.6 之前编译器一声不吭 ❌ → 线上漏处理
}
```

- **Kotlin 1.7 起**：对 sealed class 的 `when` **语句**也会给出穷举警告（可配置为 error），但本质机制仍是「表达式才硬强制」。
- **面试小技巧**：想百分百强制穷举，让 `when` 变成表达式即可——写成 `val x = when(...) {}` 或在末尾 `.let {}`，漏分支立刻编译报错，比依赖编译器配置更稳。

## out T + Nothing 的协变技巧：为什么 Loading 能是 Result<Nothing>

> 前面代码里写了 `Result<out T>`、`Error : Result<Nothing>`，但没解释**为什么**。这是泛型 + sealed 的经典考点。

```kotlin
sealed class Result<out T> {                     // out = 协变：Result<Success> 可当作 Result<Any> 使用
    data class Success<T>(val data: T) : Result<T>()
    data object Loading : Result<Nothing>()      // Loading 不带数据，用 Nothing
}
```

- `Nothing` 是 Kotlin 里**所有类型的子类型**（底类型）。配合 `out` 协变，`Result<Nothing>` 就能安全赋给任意 `Result<T>`。
- **好处**：`Loading` / `Error` 这种无参状态可以做成**全局单例**（`data object`），不用为每个 `T` 各 `new` 一个实例，节省内存也更语义清晰。

## data object（Kotlin 1.9）：为什么用 data object 而不是 object

```kotlin
data object Loading : Result<Nothing>()
```

- `object` 本身是**单例**；加 `data` 前缀后，Kotlin 会为它自动生成**规范的 `toString()`**（打印 `Loading` 而不是 `Result$Loading@1a2b3c`），以及基于类型的 `equals()` / `hashCode()`。
- **Kotlin 1.9 才稳定**。`1.9` 之前只能写 `object Loading`，日志和相等判断体验都很差。
- **面试可答**：`data object` 是 1.9 稳定的语法糖，专为 sealed class 里「无参、单例的状态节点」（如 Loading / Empty / Idle）设计。

## sealed class 可以有共享成员与抽象方法

> 前面把 sealed class 当纯「标签」用，漏了它本质是**类**，能放共享逻辑——这是面试加分点。

```kotlin
sealed class Screen(val route: String) {          // 共享构造参数：所有子类都带 route
    data object Home   : Screen("home")
    data object Detail : Screen("detail")

    abstract fun title(): String                  // 抽象方法：子类各自实现
    fun fullPath() = "app://$route"               // 共享方法：所有子类复用
}
```

- 子类可以共享父类的属性、方法；也可以声明 `abstract` 成员强制子类实现。
- 对比：`enum` 也能有成员，但每个枚举值都是同一个类、不能各自带**不同类型**的构造参数——这正是 sealed 胜出的地方。

## 子类的位置规则（1.5+）与底层实现

### 位置规则（前文「必须同一文件」已过时）
- **Kotlin 1.1–1.4**：sealed 的直接子类必须写在**同一个 `.kt` 文件**里。
- **Kotlin 1.5 起**：放宽为**同一个 module 的同一个 package**（可拆到多个文件）。
- 跨 package / 跨 module 仍然禁止——否则编译器数不清「叶子」，穷举保证就失效了。

### 底层实现原理
- sealed class 编译后 = 一个 **`abstract class` + 私有（private）构造函数**。
- 因为构造函数私有，外部代码无法 `new` 一个子类，也无法在别的包里继承它——这就是「封闭继承树」的实现机制。
- 也正是「私有构造 + 同包可见」让编译器能在编译期静态枚举所有子类，从而提供穷举检查。

> 📌 前文「穷举的原理」一节已同步更正为「同 module 同 package（早期版本曾要求同文件）」。

## sealed interface（1.5+）与三大应用场景

### sealed interface：比 sealed class 更灵活
```kotlin
sealed interface UiEvent                              // 纯状态标记，没有类层级包袱
data class Navigate(val route: String) : UiEvent
data object Refresh : UiEvent
```
- **sealed class 是单继承**：一个子类只能属于一个 sealed 父类。
- **sealed interface 可多实现**：一个类能同时 `implements` 多个 sealed interface，组合更自由。做纯「事件 / 标记」时更常用 interface。

### 三大典型应用场景（面试常让举例）
1. **UI 状态**：`UiState`（Idle / Loading / Success<T> / Error）——你项目里 `FileViewModel` 用的就是这招。
2. **网络结果**：`Result`（Success / Error / Loading），替代 Java 的 int 常量。
3. **MVI 架构的 Intent / Event**：用户意图、一次性事件（如 `ShowToast` / `Navigate`）用 sealed 建模最合适，配合 `when` 穷举处理，绝不会漏事件。

> 💡 **sealed class vs sealed interface 选型**：需要共享状态/方法 → sealed class；只是纯标记、且可能多继承 → sealed interface。

## 面试高频

> **Q: sealed class 和 Java 的枚举有什么区别？**
>
> A: Java 枚举是单例集合，每个枚举值只有唯一实例，不能带数据。sealed class 是类型继承体系，不同子类可以带不同数据（比如 `Success(data)` 和 `Error(msg)` 参数类型不同），而且 when 穷举是编译器强制检查的，不会漏掉分支。

---

<a id="s6-extension-functions"></a>
# 八、Extension Functions — 扩展函数（★★★★★） [↑ 返回目录](#toc)

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

扩展函数**不是真的修改了原来的类**，它只是编译期的语法糖。编译后，它变成了一个**静态方法**，被扩展的对象作为**第一个参数**传进去。

### 反编译看本质

```kotlin
// Kotlin 源码
fun String.isBlank(): Boolean {
    return this.trim().isEmpty()
}
val result = "   ".isBlank()

// ↓ 编译后等价于（用 Android Studio → Show Kotlin Bytecode → Decompile 看到）↓
public final class StringKt {
    public static final boolean isBlank(@NotNull String $this$isBlank) {
        Intrinsics.checkNotNullParameter($this$isBlank, "$this$isBlank");
        return $this$isBlank.trim().isEmpty();
    }
}
// 调用处：StringKt.isBlank("   ")
```

三个关键点一眼看穿：

1. **类名 + `Kt`**：扩展函数被放进一个叫 `原文件名Kt` 的类里（上面叫 `StringKt`，因为定义在 `String.kt` 文件）。
2. **接收者当首参**：`"   "` 被作为第一个参数 `$this$isBlank` 传入，所以 `this` 其实就是这个参数。
3. **零运行时开销**：调用就是普通静态方法调用，没有创建包装对象、没有反射。

### 从 Java 怎么调用？

```java
// Java 里没有"扩展函数"概念，老老实实调静态方法
boolean r1 = StringKt.isBlank("   ");          // 类名是 文件名 + Kt
// 或静态导入后直接用
import static com.xxx.StringKt.isBlank;
boolean r2 = isBlank("   ");
```

> 所以扩展函数**本质上就是 Java 的工具类方法**（`StringUtils.isBlank(str)`），只是 Kotlin 让你写起来、读起来像"对象自带的方法"。这也是它最大的用途——**取代 Java 里那一堆 `StringUtils`、`DateUtils`、`ViewUtils`**。

### 静态分派（为什么不能重写）

扩展函数是**静态解析**的：调用时看的是变量的**声明类型**，不是运行时的实际类型。

```kotlin
open class View
class Button : View()

fun View.showTag() = "I'm a View"
fun Button.showTag() = "I'm a Button"

fun printTag(v: View) {
    println(v.showTag())   // 看声明类型 View → 调用 View 的扩展，输出 "I'm a View"
}

printTag(Button())          // 即使传入的是 Button 实例，仍输出 "I'm a View"
```

这就解释了几个面试常考点：
- 扩展函数**不能 override**（它不参与多态，是静态绑定）
- 同名的**成员函数优先级高于扩展函数**（类自己有这方法，就不会用扩展的）
- 扩展函数**不能访问 private / protected 成员**（毕竟它只是外部静态方法，没有类内部权限）

### 扩展属性同理

```kotlin
// 给 String 加一个"最后一个字符"的属性（只有 getter，没有幕后字段）
val String.lastChar: Char
    get() = this[length - 1]

// 编译后同样是静态方法：StringKt.getLastChar(String $this)
// 注意：扩展属性没有 backing field，不能存数据，只能 computed
```

> 💡 易记：**扩展 = 编译器帮你把 `obj.method()` 偷偷改写成 `XxxKt.method(obj)`**。所以它灵活（能给 `final` 的 `String` 加方法）、无开销，但也因此不参与继承多态。

## 面试高频

> **Q: 扩展函数的原理是什么？**
>
> A: 扩展函数是**编译期语法糖**，不真的修改目标类。编译后变成**静态方法**，被扩展的类实例作为第一个参数传入。所以扩展函数是「静态解析」的——调用时取决于变量的**声明类型**，而不是运行时的实际类型。也不能访问目标类的 private 成员。

> **Q: 扩展函数和继承有什么区别？**
>
> A: ① 扩展不需要继承，可以对 final 类（如 String）添加方法 ② 扩展是静态解析的，不参与多态 ③ 扩展不能访问 private 成员 ④ 扩展不能 override，同名的成员函数优先级高于扩展函数。

---

<a id="s7-data-class"></a>
# 九、Data Class（★★★★★） [↑ 返回目录](#toc)

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

<a id="s8-lateinit-vs-lazy"></a>
# 十、lateinit vs by lazy（★★★★★） [↑ 返回目录](#toc)

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

<a id="s9-scope-functions"></a>
# 十一、Scope Functions — 作用域函数（★★★★） [↑ 返回目录](#toc)

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

<a id="s10-object"></a>
# 十二、Object 关键字 + Companion Object（★★★★） [↑ 返回目录](#toc)

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

<a id="s11-higher-order"></a>
# 十三、高阶函数 + inline（★★★★） [↑ 返回目录](#toc)

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

<a id="s12-coroutine-exception"></a>
# 十四、协程异常处理（★★★★） [↑ 返回目录](#toc)

## 传播规则

> 下面例子里的 `throw` 只是"占位"——真实业务里异常来自你调用的代码（网络超时抛 `IOException`、`response.body()!!` 空指针抛 NPE、JSON 解析失败抛异常……）。**异常怎么传播，和你是手写 `throw` 还是业务代码抛，完全一样。** 你真正该搞清楚的是：异常"不捕获"时，到底去哪了。

### 规则 1：launch 的异常 → 向上传播，并取消兄弟协程

```kotlin
// 真实业务版（不是手写 throw，而是业务调用本身可能崩）
viewModelScope.launch {                 // 父 launch（普通的 Job）
    launch {
        val user = api.getUser()        // 假设这里网络断了 → 抛 IOException
    }
    launch {
        val files = fileRepo.list()     // 上面的异常会让这个也"被取消"！
    }
}
```

发生了什么：
1. 子协程 A 抛异常 → 上抛给父 launch。
2. 父 launch 是普通 `Job`（不是 supervisor），收到异常 → **自己被取消，并顺带取消兄弟 B**。
3. 异常继续上抛到 `viewModelScope`（它是 `SupervisorJob`，**不会**再取消其它顶层协程，但异常依然没被处理）。
4. **没有任何 try-catch、也没有 CoroutineExceptionHandler → 异常交给平台默认处理器 → 在 Android 上就是 App 崩溃（红屏），堆栈打印在 Logcat（tag `AndroidRuntime`）。**

> 你问"崩溃的 log 该打印在哪看不出来"——答案就是：**不处理的话，它直接变成应用崩溃**，日志在 Logcat 的 `AndroidRuntime` 里，而且 App 已经死了。所以"兜底"不是可选项，必须写。

### 规则 2：async 的异常 → 在 await() 时才抛出

```kotlin
viewModelScope.launch {
    val deferred = async {
        api.getUser()                    // 这里崩，异常被"存"进 deferred
    }
    // 此刻还没事，异常被 deferred 收着
    val user = deferred.await()          // ← 在这里才真正抛出，必须 try-catch
}
```

> `async` 把异常"寄存"在 `Deferred` 里，直到 `await()` 才释放。所以 `async` 的异常**必须在 `await()` 处 try-catch**（handler 接不住，见后文）。

### 规则 3（核心）：兜底到底写哪？两个正确姿势

**姿势 A — 预期的业务错误：在调用处 try-catch，转成 UiState（这是"正常"的兜底位置）**

```kotlin
viewModelScope.launch {
    _state.value = UiState.Loading
    try {
        val user = api.getUser()        // 可能抛（网络/解析）
        _state.value = UiState.Success(user)
    } catch (e: Exception) {
        // ✅ 这里就是"日志 + 提示"该写的地方：
        Log.e("LoginVm", "加载用户失败", e)                    // 日志 → Logcat
        _state.value = UiState.Error(e.message ?: "加载失败")  // 提示 → UI 层 collect 后显示
    }
}
// UI 层：state.collect { when(it) { is Error -> showToast(it.msg) } }
```

**姿势 B — 意料之外的崩溃：交给全局 CoroutineExceptionHandler（见下一节）**
当你没预判到的 bug（如某处 `null!!`）导致崩溃、没有 try-catch 接住时，由 BaseViewModel 里装的 `globalExceptionHandler` 兜底——它记日志 + 发到 `ErrorEventBus`，在根 Activity 一处弹 Toast。**你不用在每个 ViewModel 里手写兜底，handler 自动接管。**

### 对照：不处理 vs 处理

```kotlin
// ❌ 不处理：某个业务调用崩 → 协程取消 + 兄弟被取消 + App 崩溃
viewModelScope.launch {
    launch { riskyApi.callA() }   // 崩了
    launch { doSomethingElse() }  // 被连累取消
}

// ✅ 处理（预期错误各自接住；彼此独立用顶层 launch，避免一个崩连累另一个）
viewModelScope.launch {           // 顶层1
    try { riskyApi.callA() } catch (e: Exception) { _state.value = UiState.Error(e.message) }
}
viewModelScope.launch {           // 顶层2（SupervisorJob 隔离，互不影响）
    try { doSomethingElse() } catch (e: Exception) { _state.value = UiState.Error(e.message) }
}
```

## CoroutineExceptionHandler

`CoroutineExceptionHandler` 是协程**未捕获异常**的兜底处理器——类似 Java 的 `Thread.setDefaultUncaughtExceptionHandler`，但作用在协程作用域上。它解决两个问题：**① 异常没被 try-catch 兜住时，阻止 App 崩溃；② 把散落在各处的异常集中到一处处理。**

### 它和 try-catch 怎么分工？（关键认知）

异常在协程里的传递路径是这样的：

```
子协程 throw
   ↓ 向上传播
父协程 / 作用域
   ↓ 如果某层有 try-catch → 被捕获，handler 不触发（你预期的、业务错误）
   ↓ 如果一路没人 catch，到达「根协程」
   ↓ 根协程的 context 里有 CoroutineExceptionHandler？
       是 → handler 触发（兜底，Unexpected 崩溃）
       否 → 抛给平台 → App 崩溃
```

- **try-catch**：处理「预期内、业务相关」的错误（如网络 4xx、空数据）→ 转成 `UiState.Error` 显示。
- **CoroutineExceptionHandler**：处理「意料之外」的崩溃（如你没预判到的 NPE bug）→ 记日志 + 给个兜底提示，**不让 App 崩**。

> 经验：**handler 不是用来替代 try-catch 的**，它是"最后的安全网"。正常业务错误用 try-catch 转 UI 状态；handler 只接住你漏掉的意外。

### 坑 1：handler 只对「根协程」生效，装在子协程上没用

```kotlin
// ❌ 错误：handler 装在子协程上，不会触发
viewModelScope.launch {
    launch(handler) {              // 子协程不是"根"，handler 被忽略
        throw RuntimeException("err")
    }
}   // 异常一路上抛到 viewModelScope，viewModelScope 没 handler → 崩溃

// ✅ 正确：handler 装在根协程（直接挂在 scope 上）
viewModelScope.launch(handler) {   // 这是根协程，handler 生效
    launch {
        throw RuntimeException("err")   // 异常上抛到根 → handler 接住
    }
}
```

### 坑 2：handler 接不住 async 的异常

`async` 把异常存进 `Deferred`，等到 `await()` 才抛。所以 handler 对 `async` 无效，必须用 try-catch 包住 `await()`：

```kotlin
viewModelScope.launch(handler) {
    val d = async { throw RuntimeException("err") }
    try {
        d.await()                  // 在这里捕获 async 的异常
    } catch (e: Exception) {
        _state.value = UiState.Error(e.message)
    }
}
```

### 实战：多个 ViewModel 共用同一个 handler，错误统一在一处提示

这才是你关心的"全局、统一"做法。三个角色：**一个共享 handler → 一个错误事件总线 → 一处订阅展示**。

**第一步：定义全局唯一的 handler + 错误事件总线（单例）**

```kotlin
// 错误事件总线：handler 把异常丢进来，UI 层统一订阅
object ErrorEventBus {
    private val _errors = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errors: SharedFlow<String> = _errors.asSharedFlow()
    fun post(msg: String) = _errors.tryEmit(msg)
}

// 全局唯一 handler：只负责「收异常 → 记日志 → 丢进总线」
val globalExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("Global", "未捕获协程异常: ${throwable.message}", throwable)
    ErrorEventBus.post(throwable.message ?: "发生未知错误")
}
```

> **`globalExceptionHandler` 在哪定义的？为什么 BaseViewModel 能读到？**
> 它和 `ErrorEventBus` 都是**顶层声明（top-level，写在文件最外层、不在任何 class 内）**，放在一个独立的全局文件里（例如 `core/GlobalExceptionHandler.kt`）。文档为了"分步讲解"把它们拆成了两个代码块，但真实项目里它们就是两个相邻文件：
>
> ```kotlin
> // core/GlobalExceptionHandler.kt  ← 独立的顶层文件
> object ErrorEventBus {                 // 顶层单例
>     private val _errors = MutableSharedFlow<String>(extraBufferCapacity = 1)
>     val errors: SharedFlow<String> = _errors.asSharedFlow()
>     fun post(msg: String) = _errors.tryEmit(msg)
> }
>
> val globalExceptionHandler = CoroutineExceptionHandler { _, t ->   // 顶层 val
>     Log.e("Global", "未捕获协程异常: ${t.message}", t)
>     ErrorEventBus.post(t.message ?: "发生未知错误")
> }
> ```
>
> **关键点：Kotlin 顶层 `val` / `object` 编译后变成 `GlobalExceptionHandlerKt.globalExceptionHandler`，属于整个 module，不是某个类的成员。** 只要 `BaseViewModel`（在 `BaseViewModel.kt`）和这个文件在**同一个 module、同一个包（或手动 `import`）**，就能直接写名字引用——这正是第二步 `BaseViewModel` 的 `safeScope` 能读到它的原因。不需要 `static`、不需要单例包裹、不需要依赖注入，顶层声明天生全局可见。

**第二步：BaseViewModel 把 handler 装进自己的作用域，所有 VM 继承它**

```kotlin
// 方式 A（推荐）：自定义 safeScope，用 SupervisorJob 防止一个失败取消其它协程
abstract class BaseViewModel : ViewModel() {
    // SupervisorJob：子协程互不影响；handler：统一兜底
    protected val safeScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate + globalExceptionHandler
    )

    override fun onCleared() {
        super.onCleared()
        safeScope.cancel()          // 等价于 viewModelScope 的自动取消
    }

    // 顺手包一个 safeLaunch，调用更省事
    protected fun safeLaunch(block: suspend CoroutineScope.() -> Unit) =
        safeScope.launch(block = block)
}

// 方式 B（想保留 viewModelScope 的自动取消）：包一层即可
abstract class BaseViewModel2 : ViewModel() {
    protected fun safeLaunch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(globalExceptionHandler, block = block)
}

// 具体 ViewModel：完全不用关心 handler，直接抛异常就走统一处理
class LoginViewModel : BaseViewModel() {
    fun login() = safeLaunch {
        throw RuntimeException("登录失败")   // → globalExceptionHandler → 总线
    }
}

class FileViewModel : BaseViewModel() {
    fun load() = safeLaunch {
        throw RuntimeException("加载失败")    // → 同一个 handler
    }
}
```

**第三步：在根 Activity / 根 Composable 统一订阅总线，一处弹错误**

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 整个 App 的错误，只在这一个地方展示
        lifecycleScope.launch {
            ErrorEventBus.errors.collect { msg ->
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                // 或 Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
```

这样：**任意 ViewModel 抛出的未捕获异常 → 统一进 `globalExceptionHandler` → 进 `ErrorEventBus` → 只在 MainActivity 一个地方弹 Toast/Snackbar**。handler 只写一次，所有 VM 复用；`SupervisorJob` 保证一个 VM 内某个协程崩溃不会连累其它协程。

### Java 对标

| Kotlin | Java |
|--------|------|
| `CoroutineExceptionHandler` | `Thread.setDefaultUncaughtExceptionHandler` / `Thread.UncaughtExceptionHandler` |
| `try-catch` 转 `UiState.Error` | 方法内 `try-catch` 返回 `Result` / `Optional` |
| `ErrorEventBus` 集中展示 | 观察者 / EventBus（如 `LiveData`、`RxJava Subject`） |

## 面试高频

> **Q: 协程的异常怎么处理？和 Java try-catch 有什么区别？**
>
> A: 基本原则：**try-catch 捕获协程体内部的异常**（和在 Java 里一样），`CoroutineExceptionHandler` 捕获**未被 try-catch 捕获的异常**（类似 Java 的 `Thread.setDefaultUncaughtExceptionHandler`）。注意：`launch` 的异常默认向上传播，`async` 的异常在 `await()` 时抛出。**实战中，ViewModel 层用 try-catch 捕获业务异常转为 UiState.Error（这是主路径）；而 `CoroutineExceptionHandler` 作为「安全网」装在 BaseViewModel 的作用域上（配 SupervisorJob + 错误事件总线），兜底意料之外的崩溃并统一在一处提示，它不替代 try-catch、也不用于正常业务错误。**

---

<a id="s13-coroutine-cancel"></a>
# 十五、协程取消机制（★★★★） [↑ 返回目录](#toc)

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

---

<a id="s14-variance"></a>
# 十六、泛型型变 — in / out（★★★） [↑ 返回目录](#toc)

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

<a id="s15-delegate"></a>
# 十七、委托属性 — by（★★★） [↑ 返回目录](#toc)

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

<a id="s16-reified"></a>
# 十八、reified 关键字（★★★） [↑ 返回目录](#toc)

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

<a id="s17-channel"></a>
# 十九、Channel — 协程间通信（★★★） [↑ 返回目录](#toc)

- `Channel` 类似 `BlockingQueue` 但挂起而非阻塞，用于**多个协程之间**传递数据流（一对多、多对多）。
- 常用类型：`Channel()`（默认无缓冲、`send` 挂起等 `receive`）、`Channel(UNLIMITED)`、`Channel(CONFLATED)`（只保留最新）、`Channel(BUFFERED)`。
- 对比 `Flow`：Channel 是**热**的、点对点通信、需手动关闭；Flow 是**冷**的、单一生产者多订阅、自动管理。

```kotlin
val channel = Channel<Int>()
launch { for (i in 1..3) channel.send(i); channel.close() }
launch { for (x in channel) println(x) } // 1 2 3
```

---

<a id="s18-structured-concurrency"></a>
# 二十、结构化并发（★★★） [↑ 返回目录](#toc)

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

## Job 家族：Job() / SupervisorJob() / Deferred / CompletableJob

**每个协程背后都有一个 `Job`**，它就是协程的"句柄"和生命周期对象（`launch` 的返回值就是 `Job`）。但 `Job` 不止一种，`SupervisorJob` 是其中一个变体。这一节把整族讲清。

### 1. 一句话区分四个成员

| 名字 | 是什么 | 怎么得到 |
|------|--------|----------|
| `Job` | 协程生命周期的接口/句柄：可取消、可查状态、可等待完成 | `launch { }` 的返回值就是 `Job` |
| `Job()` | 工厂函数，返回一个**普通** `Job`（精确类型是 `CompletableJob`） | 自己构造作用域：`CoroutineScope(Job())` |
| `SupervisorJob()` | 工厂函数，返回一种"**不连坐**"的 `Job` | `CoroutineScope(SupervisorJob())` |
| `Deferred<T>` | `Job` 的**子接口**，额外多了 `await()` 拿结果 | `async { }` 的返回值 |

### 2. 核心对比：`Job()` vs `SupervisorJob()`（取消传播行为完全相反）

```kotlin
// A. Job()：子协程崩溃 → 连坐父协程 + 所有兄弟协程
val scopeA = CoroutineScope(Job() + Dispatchers.Main)
scopeA.launch { throw RuntimeException("a 崩了") }   // a 崩
scopeA.launch { /* 这个 b 也被取消！被 a 连坐 */ }   // ← 受连累

// B. SupervisorJob()：子协程崩溃 → 只取消自己，兄弟无恙
val scopeB = CoroutineScope(SupervisorJob() + Dispatchers.Main)
scopeB.launch { throw RuntimeException("a 崩了") }   // a 崩
scopeB.launch { /* 这个 b 继续跑，不受影响 */ }      // ← 不连坐
```

**口诀：`Job()` 连坐，`SupervisorJob()` 各自为政。**

> **Java 视角**：`Job()` 就像"一个 `Future` 失败，就把你手里的整个 `ExecutorService` 关掉"；`SupervisorJob()` 就像"一组各自独立的 `Future`，谁挂谁挂，互不影响"。

### 3. ⚠️ 最大的坑：SupervisorJob 只保护"直接子协程"

```kotlin
val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
scope.launch {                          // 直接子协程（受 Supervisor 保护）
    launch { throw RuntimeException("孙崩了") }   // 孙协程
    launch { /* 这个兄弟会被取消！因为孙的父是外层 launch，不是 scope */ }
}
```

`SupervisorJob` 隔离的是"**scope 的直接子协程**"。一旦你在某个 `launch` 里**再 `launch`**，内层就是外层 `launch` 的子、不是 scope 的子——内层崩依然会取消外层那个 `launch` 及其兄弟。**想让更深层也互不连坐，用 `supervisorScope { }` 块：**

```kotlin
supervisorScope {                      // 块内所有 launch 互相独立
    launch { throw RuntimeException("a 崩了") }
    launch { /* 这个 b 不受影响 */ }    // supervisorScope 自己就是 SupervisorJob
}
```

### 4. supervisorScope vs coroutineScope

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

### 5. Deferred<T> 与 CompletableJob

```kotlin
// Deferred：async 返回，Job 的子接口，多了 await() 拿结果
val d: Deferred<User> = async { api.getUser() }
val u: User = d.await()                // 挂起当前协程，直到有结果

// CompletableJob：Job() 工厂实际返回的类型，可手动 complete()
val job: CompletableJob = Job()
job.complete()                         // 手动标记完成
job.invokeOnCompletion { println("协程结束了") }
```

**这些 `complete()` / `invokeOnCompletion` 到底干嘛用？**

- `complete()`：**手动把 Job「标记完成」**（不再 active）。但裸 `Job()` 没有"返回值"可用，实际项目里几乎不直接这么用。更常见的是它的"带结果版" **`CompletableDeferred<T>`**——既能 `complete(value)` 手动填结果，又能 `await()` 在别处等这个结果。
- `invokeOnCompletion { cause -> ... }`：给 Job 注册一个**结束回调**，无论正常完成、异常、还是被取消都会触发，回调里能拿到 `cause`。用途类似 `finally`，但挂在 Job 上、能区分"正常结束 vs 被取消"。

**业务场景 1：等一个「未来才发生的事件」，再让别处继续**

```kotlin
// 等「用户登录成功」这个未来事件，再去拉主页数据
val loginReady = CompletableDeferred<User>()   // 可完成的 Deferred：既 complete() 填值，又 await() 等

// 某处（如登录接口回调）登录成功时，手动"填好"结果
fun onLoginSuccess(user: User) {
    loginReady.complete(user)                  // 手动标记完成并塞入 user
}

// 另一处等待这个事件（登录没完成前，一直挂起停在这）
viewModelScope.launch {
    val user = loginReady.await()              // 阻塞的是「协程」，不是线程
    loadHome(user)                             // 登录一完成就继续
}
```

这就是 `CompletableDeferred` 的典型价值：**把一个"延迟到未来才产生的结果"当作同步的 `await()` 来用**，比回调嵌套清爽太多。

**业务场景 2：协程结束时统一做清理 / 埋点 / 计时**

```kotlin
val job = viewModelScope.launch { fetchData() }
job.invokeOnCompletion { cause ->
    when (cause) {
        null -> println("正常完成")                       // cause == null 表示正常完成
        is CancellationException -> println("被取消")      // 调用了 cancel()
        else -> println("异常结束: $cause")               // 协程体内抛了异常
    }
}
```

> 记忆点：`cause == null` = 正常收工；非 null = 异常或取消（取消抛的是 `CancellationException`）。`finally` 管"协程体内部"的清理，`invokeOnCompletion` 管"整个 Job 结束"的清理。

### 6. Job 的状态机（面试常问）

```
              start()                 自己跑完(还有子协程在跑)
        New ─────────► Active ───────────────────────────► Completing ──(子都结束)──► Completed
                        │  ▲                                      │
                        │  │ cancel() / 父协程取消                │ cancel() / 父协程取消
                        ▼  │                                      ▼
                    Cancelling ───────────(清理完毕)──────────► Cancelled
```

**逐状态拆解（对照你的中文：活动/完成中/已完成/取消中/取消完成）：**

| 状态 | 中文 | 含义 | 关键点 |
|------|------|------|--------|
| `New` | 新建 | 协程已创建，但还没开始跑 | 多数情况一闪而过；普通 `launch` 创建后立刻被调度进 `Active`；只有 `CoroutineStart.LAZY` 或手动 `Job()` 才容易观察到 |
| `Active` | **活动 / 运行中** | 正在执行协程体 | 最常见状态；`isActive = true` |
| `Completing` | **完成中** | 协程体已跑完，但还在等它的**子协程**结束 | 过渡态，几乎观察不到 |
| `Completed` | **已完成** | 自己和所有子协程都结束了 | `isCompleted = true` |
| `Cancelling` | **取消中** | 收到了 `cancel()` 或父协程取消，正在执行清理（finally、等子协程取消） | 过渡态 |
| `Cancelled` | **已取消（取消完成）** | 清理完毕 | `isCancelled = true`，且 `isCompleted = true`（取消也是一种"完成"） |

**两条主线（记住这俩就不会乱）：**
- **正常线**：`New → Active → Completing → Completed`（干活 → 活干完等娃 → 全结束）
- **取消线**：任何"活跃阶段"被 `cancel()` 或父取消 → `Cancelling → Cancelled`（进入清理 → 清理完）

**三个判断属性的真相（最容易混）：**

```kotlin
job.isActive      // New / Active / Completing 时为 true；Cancelling / Cancelled 时为 false
job.isCompleted   // Completing / Completed / Cancelling / Cancelled 时为 true（一旦进入"收尾流程"就算完成）
job.isCancelled   // Cancelling / Cancelled 时为 true
```

> 一句话记忆：**Active 是「干活中」；Completing / Cancelling 是两个收尾过渡态（一个正常收尾、一个取消收尾）；Completed 和 Cancelled 是两条线的终点。** `isCompleted` 在"取消"时也 true，因为取消也是一种结束。

### 7. 常用 API

```kotlin
job.cancel()                  // 取消自己
scope.cancelChildren()        // 只取消所有子协程，scope 本身留着
job.join()                    // 挂起，等 job 结束（挂起函数，不在当前线程阻塞）
job.invokeOnCompletion { }    // 完成时回调
```

### 8. Java 对照表

| Kotlin | Java 近似物 |
|--------|-------------|
| `Job` | `Future` / `Thread` 的句柄（可取消、可查状态） |
| `Job()` 连坐 | 一个任务失败取消共享的线程池（需自己写逻辑） |
| `SupervisorJob()` | 一组各自独立的 `Future`，互不影响 |
| `Deferred<T>` | `CompletableFuture<T>`（能 `get()` 拿结果） |
| `job.join()` | `future.get()`（但 `join()` 不抛业务异常，异常在 await 处） |

### 9. 实战：为什么 `viewModelScope` 用 `SupervisorJob`

`viewModelScope` / `lifecycleScope` 内部本质上是 `SupervisorJob() + Dispatchers.Main.immediate`。所以你在 ViewModel 里写多个 `viewModelScope.launch { }`，**一个崩溃不会连坐其它**——这正是日常开发默认就安全的原因。前面「协程异常处理」里 `BaseViewModel` 用 `SupervisorJob()` 也是同一个用意：一个协程崩溃，同 VM 的其它协程照常工作。

## 面试高频

> **Q: 结构化并发解决了什么问题？**
>
> A: 解决了「协程泄漏」问题——传统线程一旦启动就没人管了，Activity 销毁后线程还在后台跑。结构化并发让协程的生命周期和它的作用域绑定，作用域销毁时所有子协程自动取消。**简单说：你在 ViewModel 里 launch，ViewModel 销毁时协程自动取消，不需要你手动关。**

---

<a id="s19-extras"></a>
# 二十一、补充知识点（★★★） [↑ 返回目录](#toc)

> 本章补齐在前面章节未单独展开的考点：value class、Sequence、infix、Smart Cast。

## value class（原 inline class）

- `value class Name(val value: String)`：编译期把包装类型**内联到使用处**（消除对象分配开销），运行时就是底层的 `String`，但类型安全（不能把任意 String 当 Name 传）。
- 限制：只能有一个主构造属性；不能参与继承；Java 侧看到的是底层类型。
- 对标 Java 的「基本类型包装」思路，但更安全。`@JvmInline` 标注。

## Sequence — 惰性序列

- `sequence { ... }` / `list.asSequence()`：和 `Iterable` 不同，操作是**惰性、逐个元素**执行的（类似 Java Stream 的 lazy），适合处理大集合或无限序列，避免中间集合开销。
- 对比 `List`：普通集合每步都生成新集合（急切）；`Sequence` 像流水线，元素挨个流过所有操作符。

```kotlin
list.asSequence().filter { it > 0 }.map { it * 2 }.take(3).toList()
```

## infix 函数

- `infix fun Int.add(other: Int) = this + other`，调用可写成 `2 add 3`（省去括号和点）。
- 限制：必须是**成员函数或扩展函数、只有一个参数、不能是 vararg**。标准库 `to`、`shl`、`until` 都是 infix。
- 本质只是语法糖，提升可读性，无性能差异。

## Smart Cast（智能转换）

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

<a id="s20-interview"></a>
# 二十二、Kotlin 高频面试题总汇 [↑ 返回目录](#toc)

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

<a id="s21-cheatsheet"></a>
# 二十三、Kotlin 知识点速查表 [↑ 返回目录](#toc)

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
