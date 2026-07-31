<a id="top"></a>
# Flutter 面试宝典（Android 开发者视角）

> **面向**：
> - 会 Java/Kotlin、懂 Jetpack（ViewModel / Compose / Room / Hilt）、几乎不懂 Flutter 的 Android 工程师
> - 中高级 Android 岗位面试（目标公司用原生 + Flutter 混合架构）
> - 想理解「Flutter 的每个概念对应 Android 的什么」而非只是背 API

**阅读方式**：每个知识点都从你熟悉的 Android 概念入手，告诉你「Flutter 里这个等价于 Android 里的什么」，再深入原理。**遇到不理解的 Flutter 概念，先想它在 Android 里对应什么。**

---

<a id="what"></a>
# 一、Flutter 到底是什么？

> [返回目录](#catalog)

Flutter 不是「另一个 Android UI 框架」，它是完全不同的渲染模型：

```
Android 原生 App:
  你的代码 -> Android Framework（View 系统）-> Skia -> 屏幕

Flutter App:
  你的 Dart 代码 -> Flutter Framework（Widget）-> Flutter Engine（Skia）-> 屏幕
                                       跳过了 Android View 系统
```

**核心区别**：Flutter **不经过 Android 的 View 系统**。它自己画——用 Skia 引擎直接在画布上绘制每个像素。所以 Flutter 的 UI 在所有平台上长得一模一样，因为它不依赖平台的原生控件。

### Flutter 三层架构（面试必答）

```
+----------------------------------------+
|  Framework（Dart 写的，你看得到）        |
|  + Material / Cupertino（组件库）-----+ |
|  | Widgets（布局/手势/动画）           | |
|  | Rendering（RenderObject 树/绘制）   | |
|  | dart:ui（最底层 Dart API）          | |
|  +------------------------------------+ |
+----------------------------------------+
|  Engine（C++ 写的，你看不到）            |
|  Skia（2D 渲染引擎）+ Dart Runtime      |
|  + 文字排版（libtxt）+ GPU 调度         |
+----------------------------------------+
|  Embedder（平台相关）                    |
|  Android：SurfaceView + JNI            |
|  iOS：UIView + Metal                   |
+----------------------------------------+
```

**对标 Android**：
- Framework 对标 Android Framework 的 **UI 层部分**（View 系统 + Activity/Fragment 生命周期 + 资源管理 + Window 管理）。注意：Android Framework 远比这大——还包括 ContentProvider / Service / BroadcastReceiver / 通知 / 传感器 / 定位等系统服务，这些 Flutter 不替代，需要通过 MethodChannel 调原生
- Engine 对标 Android Runtime（ART）+ 原生库（Android 的 UI 渲染也用了 Skia）
- Embedder 对标 Android 的 Activity/Window 承载层（SurfaceView + JNI）

> **面试话术**："Flutter 是一个跨平台 UI 框架，用 Dart 语言编写。它跳过了 Android 的 View 系统，直接用 Skia 引擎渲染，所以 UI 表现跨平台一致。架构上分三层：Framework（Dart 层，Widget/Rendering）、Engine（C++ 层，Skia + Dart VM）、Embedder（平台适配层）。对标 Android：Engine 层相当于 Android Runtime + Skia 的组合。"

### Dart 语言一分钟速览（你已经有 Kotlin 基础）

| Dart | Kotlin | 说明 |
|------|--------|------|
| `int`/`double`/`String`/`bool` | 同名 | 基本类型一致 |
| `var x = 1;` | `var x = 1` | 类型推断 |
| `final x = 1;` | `val x = 1` | 运行时常量 |
| `const x = 1;` | `const val x = 1` | 编译时常量 |
| `String? name;` | `String? name` | 空安全 |
| `name?.length` | `name?.length` | 安全调用 |
| `class A extends B` | `class A : B()` | 继承 |
| `class C implements D` | `class C : D` | 接口实现 |
| `_privateField` | `private field` | 下划线前缀 = private |
| `void fn(int x)` | `fun fn(x: Int)` | 函数声明 |
| `() => expr` | `{ expr }` | 箭头表达式 |
| `Future<T>` | `suspend fun(): T` | 异步返回值 |
| `async` / `await` | `suspend` / 同名 | 异步语法 |

> 如果你会 Kotlin，Dart 语法 80% 一眼就能看懂。唯一大区别：**Dart 没有 `data class`，需要手动写 `copyWith`**。

---

<a id="catalog"></a>
# 二、Flutter 知识点金字塔（目录）

> 按面试重要度和企业使用频率排序。**点击跳转对应章节**。

## 必会（面试 100% 问）
- [Dart 语言必会语法速查（看代码不棘手版）](#dart)
- [Widget — 一切皆是 Widget](#widget)
- [布局系统 — Column/Row/Stack/Container](#layout)
- [StatefulWidget + setState — 本地状态](#stateful)
- [状态管理 — Riverpod（对标 ViewModel）](#riverpod)
- [路由与导航 — Navigator](#navigator)
- [MethodChannel — 与原生通信](#methodchannel)
- [Flutter 与 Android 混合开发 — Add-to-App](#addtoapp)

## 高频
- [异步编程 — Future / async-await / Stream](#async)
- [网络请求 — dio / http](#network)
- [本地存储 — sqflite（对标 Room）](#sqflite)
- [生命周期 — Widget State 生命周期](#lifecycle)
- [BuildContext — 上下文核心](#context)
- [Flutter 三棵树 — Widget/Element/RenderObject](#threetrees)

## 常用
- [动画 — AnimationController / Tween](#animation)
- [自定义绘制 — CustomPaint / Canvas](#custompaint)
- [性能优化 — const / RepaintBoundary / isolate](#performance)

## 进阶 / 附录
- [Flutter 面试高频问题总汇](#interview)
- [企业项目：native-android + flutter_module 混合架构解析](#enterprise)
- [Flutter vs React Native / Compose 对比](#compare)

---

<a id="dart"></a>
# 二点五、Dart 语言必会语法速查（看代码不棘手版）

> **这一章解决什么问题**：你读 `flutter_module/lib` 下的代码时，常被一堆符号卡住——`??`、`?.`、`..`、`{required}`、`=>`,`late`、`as`、`static const`、`.new`。这一章把这些符号**逐个拆开**，每个都用你项目里的真实代码当例子，并标出 Java/Kotlin 里对应什么。**建议把它当成字典，看代码遇到不认识的符号先翻这里。**

> [返回目录](#catalog)

---

## 1. 可空类型与空安全（Java 没有，最易卡）

Dart 和 Kotlin 一样是**空安全**语言：一个变量默认「不可能为 null」，除非你显式加 `?`。Java 没有这层约束，所以这是你第一道坎。

| 写法 | 含义 | 对标 Kotlin |
|------|------|------|
| `String name` | 非空，必须初始化 | `var name: String` |
| `String? name` | 可空，可能为 null | `var name: String?` |
| `name?.length` | 安全调用：null 时整体返回 null | `name?.length` |
| `name ?? "默认"` | `??` 左为 null 时取右（Elvis） | `name ?: "默认"` |
| `name ??= x` | 仅当 name 为 null 才赋值 | `name ?: run { name = x; name }` |
| `name!.length` | `!` 非空断言：告诉编译器「我保证不 null」，null 时直接崩 | `name!!` |
| `name as String` | `as` 强制类型转换；失败抛异常 | `name as String` |

**项目真实例子**（`device_channel.dart` 第 38 行）——一行里集齐了 `?.`、`map`、`??`：

```dart
// 原生返回 Map?（可能为 null），结果又是个 Object
return result?.map((k, v) => MapEntry(k.toString(), v == true)) ?? {};
//       │       │                                            └ 整个为 null 时退回空 Map
//       │       └ 安全调用：result 为 null 时，?.map 直接返回 null
//       └ result 本身是 Map? 类型（来自 invokeMethod<Map>，可能为 null）
```

**再看一个（`device_channel.dart` 第 54 行）**——`??` 给默认值：

```dart
return r ?? false;   // r 是 bool?，没拿到就当 false。对标 Kotlin: r ?: false
```

**`!` 非空断言（`history_db.dart` 第 58-61 行）**——你确定此时非 null 时用：

```dart
if (_db != null) return _db!;   // 刚判过非空，! 告诉编译器「放一百个心」
// 对标 Kotlin: if (_db != null) return _db!!
```

> 🔑 口诀：**`?` 声明可空、`?.` 防崩、`??` 兜底默认值、`!` 我担保非空（滥用会崩）**。看代码先找这几个符号，它们决定「这行会不会因为 null 崩」。

---

## 2. 变量与常量：var / final / const / static const

| 写法 | 含义 | 对标 Kotlin |
|------|------|------|
| `var x = 1` | 可变、类型推断 | `var x = 1` |
| `final x = 1` | 运行期只赋值一次（不可变） | `val x = 1` |
| `const x = 1` | 编译期常量，值必须写死 | `const val x = 1` |
| `static const x = 1` | 类级编译常量 | `companion object { const val x = 1 }` |

**项目真实例子**（`device_channel.dart` 第 21、26 行）：

```dart
class DeviceChannel {
  static const _channel = MethodChannel('com.pomodoro/device'); // 编译期常量单例通道
  static const permAccessFineLocation = 'android.permission.ACCESS_FINE_LOCATION';
  //        └ 类级常量，对标 Kotlin companion object 里的 const val
  // 注意开头下划线 _channel = private（见第 5 节）
}
```

> 🔑 口诀：**`var` 能改、`final` 只赋值一次、`const` 编译期就定死**。Widget 的字段几乎都是 `final`（不可变配置）。

---

## 3. 函数与参数：命名参数 / 箭头函数 / 匿名函数

### 3.1 命名参数 `{required this.x}` —— 看代码最常遇到的「花括号」

Dart 函数参数有两种：
- **位置参数** `fn(String a, int b)`：按顺序传，对标 Java。
- **命名参数** `fn({required String name, int age = 0})`：用 `name: '张三'` 传，**花括号 `{}` 就是命名参数标志**。

`{required this.name}` 三个记号拆解（你之前问过的 `Greeting`）：
- `{}` → 命名参数（对标 Kotlin `name = "张三"`）
- `required` → 必填，不传编译报错
- `this.name` → 语法糖：把传入的 `name` **直接存进字段** `this.name`

```dart
// 定义
const Greeting({required this.name});
// 调用（name: 就是传参）
Greeting(name: '张三')   // 对标 Kotlin Greeting(name = "张三")
```

### 3.2 箭头函数 `=>` 与匿名函数

`=>` 是「单表达式函数的简写」，对标 Kotlin 的 `{ expr }` 表达式体。

```dart
// 普通写法
int add(int a, int b) { return a + b; }
// 箭头简写（等价）
int add(int a, int b) => a + b;
```

**匿名函数 + 下划线参数**（`timer_notifier.dart` 第 90 行）：

```dart
_timer = Timer.periodic(const Duration(seconds: 1), (_) => _tick());
//                                                │     └ 参数没用到，用 _ 占位（Kotlin 也常用 _）
//                                                └ 匿名箭头函数，等价于 (timer) { _tick(); }
```

> 🔑 口诀：**函数参数外面包 `{}` = 命名参数（调用时写 `名: 值`）**；`=>` 是把「return 一行」缩成箭头。

---

## 4. 类与构造函数（含单例 / 私有 / 构造函数引用）

### 4.1 私有成员：`_` 前缀 = private

```dart
class TimerNotifier extends Notifier<TimerState> {
  Timer? _timer;   // 下划线开头 = 私有，对标 Kotlin private val _timer
}
```
Dart **没有 `private` 关键字**，规则是：标识符以下划线 `_` 开头 → 仅本库（本文件）可见。

### 4.2 命名构造函数 + 私有构造函数（单例模式）

`ClassName._()` 这种**带下划线**的构造函数 = 私有构造，外部 new 不了，用来做单例：

```dart
// history_db.dart 里的单例
class HistoryDatabase {
  static HistoryDatabase? _instance;
  HistoryDatabase._();   // 私有构造：外部无法 new，只能走 instance getter
  static HistoryDatabase get instance => _instance ??= HistoryDatabase._();
  //                                        └ ??= 见第 1 节：为空才 new 一次
}
```

### 4.3 `factory` 构造函数（另一种单例写法）

`factory` 允许构造函数「不返回新实例，而是返回缓存/子类实例」——单例的经典写法：

```dart
class HistoryDb {
  factory HistoryDb() => _instance;   // 不 new，直接返回既有的单例
  static final HistoryDb _instance = HistoryDb._internal();
  HistoryDb._internal();
}
// 对标 Kotlin: object HistoryDb { ... }  （Kotlin object 是语言级单例，Dart 要手写）
```

### 4.4 构造函数引用 `.new`（传给框架当工厂）

Riverpod 注册 ViewModel 时，把「构造函数本身」当参数传，而不是 new 一个实例：

```dart
// timer_notifier.dart 第 190 行
final timerProvider = NotifierProvider<TimerNotifier, TimerState>(
  TimerNotifier.new,   // 构造函数引用，≈ Kotlin 的 TimerNotifier::new
);                     // 框架在需要时自己 new，对标 Hilt 的 @Inject 构造
```

### 4.5 `const` 构造函数（见上一章 3.1 的 `Greeting`）

要求所有字段 `final` 且构造体为空，意义是「编译期固化实例、多处复用」。Widget 几乎都带 `const`。

> 🔑 口诀：**`_` 开头 = 私有；`._()` 私有构造 = 单例专用；`factory` = 可控返回；`.new` = 把构造函数当变量传；`const` 构造 = 编译期复用。**

---

## 5. `late` —— 延迟初始化（对标 Kotlin lateinit）

声明时先不赋值，第一次访问时再初始化。常用于「构造时拿不到、稍后赋值」的字段。

```dart
late AnimationController _controller;  // 对标 Kotlin: lateinit var controller: AnimationController
// 在 initState / build 里才 _controller = AnimationController(...)
```
**坑**：没初始化就访问会抛 `LateInitializationError`。对标 Kotlin `lateinit` 的 `UninitializedPropertyAccessException`。

---

## 6. 级联运算符 `..`（Java 没有，一眼懵）

`..` 让你**在同一个对象上连续调用多个方法/设属性，返回的是对象本身**（不是最后一个方法的返回值）。对标 Kotlin 的 `apply { }`、Java 的 builder 链。

**项目真实例子**（`history_db.dart` 第 122 行）：

```dart
return db.insert(
  'pomodoro_records',
  record.toMap()..remove('id'),   // 先 toMap()，再 remove('id')，返回的还是那个 Map
);
// 等价于（没有 .. 时你得这么写）：
// final m = record.toMap();
// m.remove('id');
// db.insert('pomodoro_records', m);

// 对标 Kotlin:
// record.toContentValues().apply { remove("id") }
```

> 🔑 口诀：**`a..b()..c()` = 对 a 连续做 b、c，全程还是 a**。看代码遇到 `..` 别慌，就是「链式设置」。

---

## 7. 集合操作 map / where / fold（对标 Java Stream / Kotlin 集合）

Dart 集合自带高阶函数，写法对标 Kotlin 集合 API、Java 的 Stream：

| Dart | Kotlin | Java Stream |
|------|--------|------|
| `list.map((e) => ...)` | `list.map { ... }` | `list.stream().map(...)` |
| `list.where((e) => ...)` | `list.filter { ... }` | `list.stream().filter(...)` |
| `list.fold(init, (acc, e) => ...)` | `list.fold(init) { acc, e -> }` | `list.stream().reduce(...)` |
| `list.toList()` | `list.toList()` | `list.stream().toList()` |

**项目真实例子**（`history_db.dart` 第 143 行）——把 `List<Map>` 转成 `List<对象>`：

```dart
return maps.map((map) => PomodoroRecord.fromMap(map)).toList();
//          │                     └ 每个 Map → PomodoroRecord 对象
//          └ 映射（对标 Kotlin maps.map { PomodoroRecord.fromMap(it) }）
```

---

## 8. `get` 异步 getter（容易被忽略的语法）

Dart 里 `get xxx` 定义「只读属性」，调用时像字段一样 `obj.xxx` 不用加 `()`。它可以 `async`，返回 `Future`：

```dart
// history_db.dart 第 56 行
Future<Database> get database async {   // get + async：obj.database 返回 Future<Database>
  if (_db != null) return _db!;
  _db = await _initDb();
  return _db!;
}
// 调用：final db = await history.database;  // 像字段一样访问，实际是异步方法
```
对标 Kotlin：没有直接的 async property，通常写成 `suspend fun getDatabase(): Database`。

---

## 9. 类型判断与转换：`is` / `as`

| Dart | 含义 | 对标 Java |
|------|------|------|
| `obj is String` | 判断类型（true 后自动智能转型） | `obj instanceof String` |
| `obj as String` | 强制转换，失败抛 `CastError` | `(String) obj` |
| `obj as? String` | **不存在**（Dart 没有安全转换） | `obj instanceof String ? (String) obj : null` |

> 注意：Dart **没有** Kotlin 的 `as?` 安全转换。想要「转换失败返回 null」得自己写 `obj is T ? obj as T : null`，或 `try { obj as T } catch (_) { null }`。

---

## 10. `extension` 扩展（对标 Kotlin 扩展，Java 没有）

给已有类（哪怕是 SDK 的类）加方法，不用继承：

```dart
extension IntExt on int {
  String get toHex => '0x${toRadixString(16)}';  // 5.toHex → "0x5"
}
// 对标 Kotlin: val Int.toHex get() = "0x${toString(16)}"
```
你项目里目前没用 extension，但读第三方包源码时会大量遇到。

---

## 11. `mixin` —— 多重能力复用（对标 Kotlin 的 interface + 默认实现）

Dart 单继承，但可以用 `mixin` + `with` 把「一组方法/状态」混入多个类。常见于动画、状态机：

```dart
// 文档动画章的真实例子（第 1408 行）
class _AnimatedDemoState extends State<AnimatedDemo>
    with SingleTickerProviderStateMixin {   // with = 混入这个 mixin 的能力
  late AnimationController _controller;
}
// 对标 Kotlin: class X : State<...>, SingleTickerProvider
// （Kotlin 用接口 + 默认方法实现，Dart 用 mixin 更纯粹地复用实现）
```

---

## 12. 字符串插值 `$var` / `${expr}`（Java 没有，Kotlin 有）

```dart
final name = '张三';
print('Hello, $name!');        // 单变量：$变量名
print('长度: ${name.length}');  // 表达式：${表达式}
// 对标 Kotlin: "Hello, $name!" / "长度: ${name.length}"
// 对标 Java: 只有 String.format / 拼接，没有插值
```
你在 `device_channel.dart` 的 `debugPrint('requestPermissions error: ${e.message}')` 就是这写法。

---

## 13. `copyWith` 模式 —— Dart 没有 data class，靠它代替

Kotlin 的 `data class` 自带 `copy()`（改一两个字段生成新对象）。**Dart 没有 data class**，所以状态类要手写 `copyWith`：

```dart
// timer_state.dart（被 timer_notifier.dart 大量调用）
class TimerState {
  final int remainingSeconds;
  final TimerStatus status;
  const TimerState({this.remainingSeconds = 0, this.status = TimerStatus.idle});

  TimerState copyWith({int? remainingSeconds, TimerStatus? status}) =>  // 手写 copy
    TimerState(
      remainingSeconds: remainingSeconds ?? this.remainingSeconds,
      status: status ?? this.status,
    );
}
// 调用（timer_notifier.dart）：state = state.copyWith(status: TimerStatus.running);
// 对标 Kotlin: state.copy(status = RUNNING)
```
> 看 `TimerNotifier` 代码里满屏的 `state.copyWith(...)` 就是在「不可变地更新状态」，对标 Kotlin data class 的 `copy`。

---

## 14. 异步（Future / async / await）—— 已有专章，这里只指路

`Future<T>` 对标 Kotlin `suspend fun(): T` 的「异步返回值」，`async`/`await` 对标 `suspend`/直接调用。详见 [第十章 异步编程](#async)。你项目里 `device_channel.dart` 的每个方法都是 `Future<...> async { ... await _channel.invokeMethod(...) ... }`，就是「异步调原生 + 等结果」。

---

## 15. 一句话总览（看代码时扫一眼）

| 你看到的符号 | 它是什么 | 先记住 |
|------|------|------|
| `Type?` / `?.` / `??` / `!` | 空安全家族 | 第 1 节 |
| `final` / `const` / `static const` | 不可变/常量 | 第 2 节 |
| `{required this.x}` | 命名参数 + 存字段 | 第 3 节 |
| `=>` / `(_) =>` | 箭头函数 / 匿名 | 第 3 节 |
| `_xxx` / `._()` | 私有 / 私有构造 | 第 4 节 |
| `factory` / `.new` | 单例 / 构造引用 | 第 4 节 |
| `late` | 延迟初始化 | 第 5 节 |
| `..` | 级联调用 | 第 6 节 |
| `map` / `where` / `fold` | 集合操作 | 第 7 节 |
| `get xxx async` | 异步属性 | 第 8 节 |
| `is` / `as` | 类型判断/转换 | 第 9 节 |
| `extension` | 扩展方法 | 第 10 节 |
| `mixin` / `with` | 能力混入 | 第 11 节 |
| `$var` / `${expr}` | 字符串插值 | 第 12 节 |
| `copyWith` | 手写 data class copy | 第 13 节 |

> 🔑 终极口诀：**Dart 和 Kotlin 80% 像，差异集中在——没有 data class（用 copyWith）、没有 `as?` 安全转换、用 `..` 级联、用 `mixin` 复用、用 `_` 表示私有。剩下全是 Java/Kotlin 老朋友。**

---

<a id="widget"></a>
# 三、Widget — 一切皆是 Widget

> **面试 100% 问：Flutter 里 Widget 是什么？和 Android View 有什么区别？**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用 / 为什么这样设计

- **是什么**：Widget 是 Flutter 的 UI 构建块，**一切 UI 元素都是 Widget**——不只是按钮和文字，布局（Column/Row）、样式（Padding/Center）、手势（GestureDetector）统统是 Widget。对标 Android：Widget 对标 View + Layout + 样式配置（所有 View 子类的统称）。
- **解决什么**：Android View 体系是**命令式**的——你创建一个 Button，然后 `setText()`、`setBackground()`、`setOnClickListener()`，一步步告诉系统"怎么改"。Flutter 是**声明式**的——你描述 UI "应该长什么样"，每次状态变化 Flutter 重新 build，自动算出差异再局部更新。声明式消除了"手动同步 UI 和状态"的 bug 源头。
- **怎么用**：Widget 有两种——`StatelessWidget`（无状态，构建后不变）和 `StatefulWidget`（有状态，`setState` 触发重建）。每个 Widget 的 `build()` 方法返回一个 Widget 子树。
- **为什么这样设计**：声明式 + 不可变 Widget 让 UI 变成纯函数 `UI = f(state)`，Flutter 可以精确知道"哪里变了"，只重绘必要的部分（对标 Compose 的 recomposition）。传统 View 是可变对象，系统不知道你改了哪个属性，得你自己确保一致性。

## 声明式 UI vs 命令式 UI

你已有的知识：**Jetpack Compose 就是声明式 UI**。Flutter 的 Widget 和 Compose 的 Composable 函数几乎一模一样的思想。

```java
// Android 传统 View（命令式）：
// 你自己一步步告诉系统"怎么改"
Button btn = findViewById(R.id.submit);
btn.setText("提交");
btn.setEnabled(false);
btn.setBackgroundColor(Color.GRAY);
// 问题：10 个地方改同一个 Button，谁改了什么不知道

// Flutter Widget（声明式）：
// 你描述 UI "应该长什么样"
if (isSubmitting) {
  ElevatedButton(
    onPressed: null,           // null = 禁用
    style: ButtonStyle(backgroundColor: Colors.grey),
    child: Text("提交中..."),
  );
} else {
  ElevatedButton(
    onPressed: handleSubmit,
    child: Text("提交"),
  );
}
// Flutter 自动对比前后 Widget 树，只更新变化的部分
```

## StatelessWidget vs StatefulWidget

| | StatelessWidget | StatefulWidget |
|------|-----------------|----------------|
| 对标 Android | 无状态的 Composable 函数 | 有状态 Composable + `remember` |
| 是否有状态 | 否，创建后不变 | 是，有 State 对象持状态 |
| 何时重建 | 父 Widget 重建时 | `setState()` 调用时 |
| 项目中使用 | `PomodoroApp`（只做路由+主题配置） | `DeviceDemoPage`（持有日志列表状态） |

### StatelessWidget 示例（对标 Compose 无状态 Composable）

```dart
// StatelessWidget：只根据输入参数渲染，自己不保存任何可变状态
// 对标 @Composable fun Greeting(name: String) { Text("Hello $name") }
class Greeting extends StatelessWidget {
  final String name;           // 输入参数（对标 Composable 函数参数）
  const Greeting({required this.name});

  @override
  Widget build(BuildContext context) {   // build() 对标 Composable 函数体
    return Text('Hello, $name!');
  }
}

// 使用
Greeting(name: '张三')          // 对标 Greeting(name = "张三")
```

### StatefulWidget 示例（对标 Compose + remember）

```dart
// StatefulWidget：Widget 本身是不可变的，State 对象持有可变数据
// 对标 @Composable fun Counter() { var count = remember { mutableIntStateOf(0) } }
class Counter extends StatefulWidget {
  const Counter({super.key});

  @override
  State<Counter> createState() => _CounterState();  // 创建 State 对象
}

class _CounterState extends State<Counter> {
  int _count = 0;               // 状态变量（对标 mutableIntStateOf）

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Text('${_count}', style: TextStyle(fontSize: 32)),
        ElevatedButton(
          onPressed: () {
            setState(() {       // setState() 对标 标记 Compose 重组
              _count++;         // 改变状态
            });                 // -> Flutter 自动重建 build()
          },
          child: Text('+1'),
        ),
      ],
    );
  }
}
```

**关键理解**：`StatefulWidget` 的 Widget 本身是**不可变的**（每次重建都是新对象），可变状态存在 `State` 对象中。`State` 对象通过 `setState()` 方法触发 UI 重建。对标 Compose：Compose 用 `remember` 在重组间保存引用，Flutter 用 `State` 对象。

## BuildContext：你写的每个 build(context) 里的 context 是什么？

```dart
@override
Widget build(BuildContext context) {
  // BuildContext context = 当前 Widget 在 Widget 树中的位置
  // 对标 Android 的 Context（获取主题、资源、导航等）

  // 常用场景：
  final theme = Theme.of(context);              // 对标 context.getTheme()
  final navigator = Navigator.of(context);      // 对标 context.startActivity()
  final size = MediaQuery.of(context).size;     // 屏幕尺寸
}
```

> **易记口诀**：`BuildContext` = Android `Context` + Widget 树坐标。它知道「我是谁、我在哪、我的父 Widget 是谁」。

## 项目对照

项目中 `app.dart` 的 `PomodoroApp` 就是典型的 **StatelessWidget**——它只负责配置（MaterialApp + 路由表 + 主题），不需要内部可变状态。真正的状态在 `TimerNotifier`（= ViewModel）里管。

```dart
// 项目代码：app.dart - PomodoroApp (StatelessWidget)
class PomodoroApp extends StatelessWidget {
  const PomodoroApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '番茄钟',
      routes: {
        '/nav_home':   (context) => const HomePage(),
        '/device_demo': (context) => const DeviceDemoPage(),
      },
      home: Scaffold(
        appBar: AppBar(title: const Text('番茄钟')),
        body: const SafeArea(
          child: Padding(
            padding: EdgeInsets.symmetric(horizontal: 24),
            child: TimerWidget(),             // 番茄钟计时页
          ),
        ),
      ),
    );
  }
}
```

## 面试高频

> **Q: Flutter 的 Widget 和 Android 的 View 有什么区别？**
>
> A: 三个核心区别：
> 1. **声明式 vs 命令式**：Widget 描述 UI "应该长什么样"，状态变化时 Flutter 自动重建；View 是你手动调 `setText()`、`setVisibility()` 一步步改。
> 2. **不可变 vs 可变**：Widget 是不可变的（每次 build 都是新对象），View 是可变的（同一个对象改属性）。不可变让 Flutter 能精确 diff，只重绘变化的部分。
> 3. **轻量 vs 重量**：Widget 只是配置描述（类似 Compose 的 Composable 函数），不含平台 View 的底层开销。

> **Q: setState 做了什么？为什么调用后 UI 会更新？**
>
> A: `setState` 接受一个闭包修改状态 -> 闭包执行完后标记当前 Widget 为"脏" -> Flutter 调度重建 -> 重新调用 `build()` 生成新 Widget 树 -> 对比新旧树 -> 只更新变化的 RenderObject。关键：**它不是立即重建，而是异步调度**（在当前帧结束时统一处理）。

---

<a id="layout"></a>
# 四、布局系统 — Column/Row/Stack/Container

> **面试 100% 问：Flutter 怎么做布局？对标 LinearLayout / FrameLayout 是什么？**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：Flutter 的布局也是 Widget（这是和 Android 最大的思维差异）。`Column`、`Row`、`Stack` 等都是 Widget，你把它们嵌套在一起就完成了布局。
- **解决什么**：Android 用 XML 和代码混合布局，Flutter 统一用 Dart 代码描述布局——类型安全、不用 findViewById、重构时 IDE 全程提示。
- **怎么用**：每个布局 Widget 都有一个 `children` 或 `child` 参数，把子 Widget 放进去即可。

## 核心布局 Widget 对照表

| Flutter | Android XML | 说明 |
|---------|-------------|------|
| `Column` | `<LinearLayout android:orientation="vertical">` | 垂直排列子元素 |
| `Row` | `<LinearLayout android:orientation="horizontal">` | 水平排列子元素 |
| `Stack` | `<FrameLayout>` | 层叠排列，后写的在上面 |
| `Container` | View + padding + margin + background + width/height 的组合 | 万能的装饰盒子 |
| `Expanded` | `android:layout_weight="1"` | 填充剩余空间 |
| `Padding` | `android:padding="16dp"` | 内边距 |
| `Center` | `android:gravity="center"` | 居中 |
| `Align` | `android:layout_gravity` | 对齐方式 |
| `ListView` | `ListView` / `ScrollView` | 滚动列表 |

## 代码示例（对标 Android XML）

```dart
// Flutter: 纯代码布局，类型安全
@override
Widget build(BuildContext context) {
  return Scaffold(
    appBar: AppBar(title: Text('布局示例')),
    body: Padding(
      padding: EdgeInsets.all(16.0),         // 对标 android:padding="16dp"
      child: Column(                          // 对标 LinearLayout vertical
        children: [
          Text('标题', style: TextStyle(fontSize: 24)),
          SizedBox(height: 12),               // 对标 android:layout_marginTop
          Row(                                // 对标 LinearLayout horizontal
            children: [
              Expanded(                        // 对标 layout_weight="1"
                child: ElevatedButton(
                  onPressed: () {},
                  child: Text('取消'),
                ),
              ),
              SizedBox(width: 8),
              Expanded(
                child: ElevatedButton(
                  onPressed: () {},
                  child: Text('确认'),
                ),
              ),
            ],
          ),
        ],
      ),
    ),
  );
}
```

## Container：万能的装饰盒子

```dart
// Container 对标 View + padding + margin + background + border + width/height
Container(
  width: 200,
  height: 100,
  margin: EdgeInsets.all(8),                // 外边距
  padding: EdgeInsets.symmetric(horizontal: 16, vertical: 8),  // 内边距
  decoration: BoxDecoration(
    color: Colors.blue,
    borderRadius: BorderRadius.circular(12), // 圆角
    boxShadow: [
      BoxShadow(color: Colors.black26, blurRadius: 4),  // 阴影
    ],
  ),
  child: Text('按钮', style: TextStyle(color: Colors.white)),
)
```

## ListView.builder：对标 RecyclerView

```dart
// ListView.builder 对标 RecyclerView（只渲染可见项，高效懒加载）
// 项目对照：history_page.dart 用的就是 ListView.builder
ListView.builder(
  itemCount: records.length,              // 对标 getItemCount()
  itemBuilder: (context, index) {        // 对标 onBindViewHolder()
    return ListTile(
      title: Text(records[index].title),
      subtitle: Text('${records[index].timestamp}'),
    );
  },
)
```

## 项目对照

项目里 `timer_widget.dart` 用了 `Column` + `Row` + `SizedBox` + `Expanded` 组装计时器页面。`app.dart` 用了 `Scaffold` + `SafeArea` + `Padding` 包裹整个首页。

## 面试高频

> **Q: Flutter 布局和 Android XML 布局的本质区别？**
>
> A: 本质区别是**布局也是 Widget 而不是单独的布局文件**。Android 把布局（XML）和组件（View）分离，Flutter 统一用 Widget 树——Column 既是容器也是 Widget。好处是类型安全（编译期就报错）、不依赖字符串 ID、嵌套限制通过 Widget 树的层级自然表达。代价是代码嵌套可能较深，需要用提取子 Widget 来解决。

---

<a id="stateful"></a>
# 五、StatefulWidget + setState — 本地状态

> **面试必问：Flutter 的状态和 Compose 的 remember 一样吗？**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：`StatefulWidget` 是能持有可变状态的 Widget。它分成两部分——不可变的 `Widget`（每次重建新对象）+ 可变的 `State`（在 Widget 生命周期内一直存活）。
- **解决什么**：UI 需要响应交互（按钮点击、输入框变化），这些交互改变"状态"，状态变了 UI 要跟着变。
- **怎么用**：创建 `StatefulWidget` 子类 -> 在 `State.build()` 中渲染 UI -> 需要改状态时调 `setState(() { ... })` -> Flutter 自动重建。
- **对标 Compose**：`StatefulWidget` + `State` 对标 `@Composable` 函数 + `remember { mutableStateOf(...) }`。`setState` 对标直接改 `mutableStateOf` 的值（自动触发重组）。

## State 生命周期（对标 Compose 副作用 API）

```
Widget 创建 -> State.createState() -> State.initState()
  -> State.didChangeDependencies() -> State.build() -> [Widget 显示]

用户交互 -> setState(() {...})
  -> State.build() -> [UI 更新]

Widget 移除 -> State.deactivate() -> State.dispose()
```

| 生命周期方法 | 对标 Compose | 何时调用 | 做什么 |
|-------------|-------------|---------|--------|
| `initState()` | `LaunchedEffect(Unit)` / `remember` | Widget 首次创建时，只调一次 | 初始化（创建 Timer/AnimationController） |
| `didChangeDependencies()` | `derivedStateOf` | 依赖的 InheritedWidget 变化时 | 更新依赖 |
| `build()` | Composable 函数体 | 每次需要重建时 | 返回 Widget 树 |
| `setState()` | 直接改 `mutableStateOf` | 你手动调 | 标记需要重建 |
| `dispose()` | `DisposableEffect` / `onDispose` | Widget 永久移除时 | 清理资源（取消 Timer/AnimationController） |

```dart
class TimerPage extends StatefulWidget {
  @override
  State<TimerPage> createState() => _TimerPageState();
}

class _TimerPageState extends State<TimerPage> {
  late Timer _timer;       // late = 延迟初始化（对标 lateinit）

  @override
  void initState() {
    super.initState();
    _timer = Timer.periodic(Duration(seconds: 1), (_) {
      setState(() { /* 更新 UI */ });
    });
  }

  @override
  void dispose() {
    _timer.cancel();       // 必须取消！否则内存泄漏
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Text('...');    // UI 内容
  }
}
```

## 项目对照

项目里 `device_demo_page.dart` 是 StatefulWidget 的完整实例——它持有一个 `_log` 列表，每次调用原生能力后 `setState` 插入日志，下方日志区自动刷新。

```dart
// 项目代码：device_demo_page.dart
class DeviceDemoPage extends StatefulWidget {
  const DeviceDemoPage({super.key});
  @override
  State<DeviceDemoPage> createState() => _DeviceDemoPageState();
}

class _DeviceDemoPageState extends State<DeviceDemoPage> {
  final _log = <String>[];

  void _add(String line) {
    setState(() => _log.insert(0, line));  // 插入日志并刷新 UI
  }

  // ... build() 中渲染日志列表
}
```

## 面试高频

> **Q: initState 里能调 setState 吗？为什么？**
>
> A: **不能**。`initState` 在 Widget 插入 Widget 树之前执行，此时 Element 还没绑定 RenderObject，调 `setState` 没意义，Flutter 会抛异常。如果要在初始化后立刻改状态，用 `WidgetsBinding.instance.addPostFrameCallback(() => setState(...))` 或 `Future.microtask()` 把状态变更推迟到第一帧 build 之后。

> **Q: StatefulWidget 的 State 对象在 Widget 重建时会被销毁吗？**
>
> A: **不会。** Widget 是不可变的，每次 `build()` 都会创建新的 Widget 实例，但 `State` 对象通过 `Element` 持有，只要 Element 位置没变（key 一样），State 对象就一直存活。这就是 Flutter 能做到「Widget 树全重建但性能不差」的关键——它只重建配置描述（Widget），状态和渲染对象（State/RenderObject）复用。

---

<a id="riverpod"></a>
# 六、状态管理 — Riverpod（对标 ViewModel）

> **面试必问：Flutter 怎么做状态管理？你对标 Android 的什么？**
> **这是最重要的 Flutter 知识点，没有之一。你已有的 ViewModel 经验直接映射到这里。**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用 / 为什么不是 setState

- **是什么**：Riverpod 是 Flutter 社区最主流的状态管理库之一。它让你把业务逻辑和状态抽到 Widget 树外面，任何 Widget 都能订阅和修改这些状态。
- **为什么不用 setState**：`setState` 的状态局限在一个 Widget 内，跨页面共享状态（比如"当前用户信息"要在 10 个页面用）如果只用 `setState`，就得手动一层层传——这就是 Android 里 `Intent.putExtra` 的地狱。状态管理库解决的就是「状态全局化、订阅自动化」。
- **怎么用**：定义 `Notifier`（= ViewModel）-> 注册 `NotifierProvider`（= Hilt @Inject）-> Widget 里 `ref.watch(provider)` 订阅（= `collectAsState()`）。
- **对标关系**：

| Flutter (Riverpod) | Android (Jetpack MVVM) | 职责 |
|---|---|---|
| `Notifier<T>` | `ViewModel` | 持有业务逻辑 + 状态 |
| `state` | `MutableStateFlow<T>` + `_uiState` | 当前 UI 状态 |
| `state = newState` | `_uiState.value = newState` | 更新状态，通知 UI |
| `build()` | `init {}` | 初始化 |
| `ref.onDispose()` | `onCleared()` | 清理资源 |
| `NotifierProvider` | `@HiltViewModel` + `@Inject` | DI 注册 |
| `ref.watch(provider)` | `collectAsState()` | UI 订阅状态 |
| `ref.read(provider.notifier)` | 拿到 ViewModel 引用 | 调用方法 |

## 项目对照：TimerNotifier（完整 ViewModel 实现）

这是你项目中最核心的对照代码。读完这段，你就懂了 Flutter 怎么做 MVVM：

```dart
// timer_notifier.dart —— 项目代码的核心对照

// ===== 状态定义（对标 data class TimerUiState）=====
// timer_state.dart：
class TimerState {
  final int remainingSeconds;
  final int totalSeconds;
  final TimerStatus status;  // idle / running / paused / finished
  final int completedSessions;

  const TimerState({...});

  TimerState copyWith({...}) => TimerState(...);  // 对标 Kotlin data class copy()
}

// ===== ViewModel 实现（对标 class PomodoroViewModel : ViewModel()）=====
class TimerNotifier extends Notifier<TimerState> {

  Timer? _timer;  // 私有成员（对标 private）

  @override
  TimerState build() {                    // 对标 ViewModel init {}
    ref.onDispose(() => _timer?.cancel()); // 对标 onCleared()
    return const TimerState();
  }

  void start() {                          // 对标 ViewModel.onStartClicked()
    if (state.status == TimerStatus.running) return;
    _timer?.cancel();
    if (state.remainingSeconds <= 0) {
      state = state.copyWith(remainingSeconds: state.totalSeconds);
    }
    _timer = Timer.periodic(Duration(seconds: 1), (_) => _tick());
    state = state.copyWith(status: TimerStatus.running);
  }

  void pause() {                          // 对标 ViewModel.onPauseClicked()
    _timer?.cancel();
    state = state.copyWith(status: TimerStatus.paused);
  }

  void _tick() {                          // 对标 CountDownTimer.onTick()
    if (state.remainingSeconds <= 1) {
      _timer?.cancel();
      state = state.copyWith(
        status: TimerStatus.finished,
        remainingSeconds: 0,
        completedSessions: state.completedSessions + 1,
      );
      return;
    }
    state = state.copyWith(remainingSeconds: state.remainingSeconds - 1);
  }
}

// ===== DI 注册（对标 @HiltViewModel + @Inject）=====
final timerProvider = NotifierProvider<TimerNotifier, TimerState>(
  TimerNotifier.new,
);
```

**你在项目中用它**：

```dart
// timer_widget.dart —— 项目代码

class TimerWidget extends ConsumerWidget {   // ConsumerWidget = 能读 Provider 的 Widget
  @override
  Widget build(BuildContext context, WidgetRef ref) {
    // ref.watch -> 订阅 timerProvider 的状态变化
    // 对标 val uiState by viewModel.uiState.collectAsState()
    final timerState = ref.watch(timerProvider);
    final notifier = ref.read(timerProvider.notifier);  // 拿到 ViewModel 引用

    return Column(
      children: [
        Text('${timerState.remainingSeconds ~/ 60}:${timerState.remainingSeconds % 60}'),
        ElevatedButton(
          onPressed: timerState.status == TimerStatus.running
            ? notifier.pause   // 对标 viewModel.onPauseClicked()
            : notifier.start,  // 对标 viewModel.onStartClicked()
          child: Text(timerState.status == TimerStatus.running ? '暂停' : '开始'),
        ),
      ],
    );
  }
}
```

**完整数据流**：

```
1. Notifier 改 state       -> TimerNotifier.start()
2. Riverpod 通知订阅者      -> 框架自动
3. ConsumerWidget 重建     -> ref.watch(timerProvider) 拿到新 state
4. build() 返回新 Widget 树 -> Flutter diff 并局部更新 UI
```

> **一句口诀**：`Notifier` 是 ViewModel，`state` 是 `MutableStateFlow`，`ref.watch` 是 `collectAsState`，`copyWith` 是 `data class copy()`。

## 你的经验映射表

| 在 Android 里你会... | 在 Flutter 里你写成... |
|---------------------|----------------------|
| `class MyVm : ViewModel()` | `class MyNotifier extends Notifier<MyState>` |
| `val _state = MutableStateFlow(...)` | `MyState` 类 + Notifier 的 `state` 属性 |
| `_state.value = newVal` | `state = newVal` 或 `state = state.copyWith(...)` |
| `vm.uiState.collectAsState()` | `ref.watch(myProvider)` |
| `viewModel.onStartClicked()` | `ref.read(myProvider.notifier).start()` |
| `@HiltViewModel` + `@Inject` | `final myProvider = NotifierProvider(...)` |
| `ViewModel.onCleared()` | `ref.onDispose(() { ... })` |

## 面试高频

> **Q: 为什么用 Riverpod 而不是 setState？**
>
> A: `setState` 状态局限在单个 Widget 内，跨组件共享需要层层传递（prop drilling）。Riverpod 让状态放在 Widget 树外面：
> 1. **全局可访问**：任何 Widget 都能通过 `ref.watch` 订阅同一个 Provider
> 2. **自动通知**：状态变了，所有订阅者自动重建
> 3. **生命周期绑定**：`ref.onDispose` 确保资源不泄漏
> 4. **可测试**：Provider 可以在测试中覆写
> 对标 Android：就是用 ViewModel + StateFlow 而不是在 Activity 里 `findViewById` + 手动 `setText`。

> **Q: ref.watch 和 ref.read 的区别？**
>
> A: `ref.watch` **订阅**状态变化，状态变了会重建 Widget（对标 `collectAsState()`）；`ref.read` **读取一次**，不订阅变化，通常用来拿 Notifier 调方法（对标直接拿到 ViewModel 引用）。**规则**：build 方法里用 `watch`，点击回调里用 `read`。在 build 里用 `read` 会导致状态变了 UI 不更新。

---

<a id="navigator"></a>
# 七、路由与导航 — Navigator

> **面试必问：Flutter 怎么做页面跳转？对标 Intent/startActivity 是什么？**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：`Navigator` 是 Flutter 的页面导航栈管理器，和 Android 的 Activity 返回栈是同一个概念。
- **怎么用**：`Navigator.push(context, route)` 跳转 -> `Navigator.pop(context)` 返回。
- **对标 Android**：

| Flutter Navigator | Android | 说明 |
|---|---|---|
| `Navigator.push(context, MaterialPageRoute(builder: ...))` | `startActivity(Intent)` | 跳转到新页面 |
| `Navigator.pop(context)` | `finish()` | 返回上一页 |
| `Navigator.pushNamed(context, '/route')` | `startActivity` + Intent 携带 action | 命名路由跳转 |
| `Navigator.pushNamed(context, '/detail', arguments: data)` | `Intent.putExtra("key", data)` | 传参跳转 |
| `ModalRoute.of(context)!.settings.arguments` | `getIntent().getSerializableExtra("key")` | 接收参数 |

## 匿名路由（直接创建页面对象）

```dart
// 跳转
Navigator.push(
  context,
  MaterialPageRoute(builder: (context) => DetailPage(id: 123)),
);
// 对标 startActivity(Intent(this, DetailActivity::class.java).apply { putExtra("id", 123) })

// 返回
Navigator.pop(context);
// 对标 finish()
```

## 命名路由 + 参数传递（对标项目）

项目 `app.dart` 就用了命名路由：

```dart
// app.dart —— 注册路由
MaterialApp(
  routes: {
    '/nav_home':   (context) => const HomePage(),       // 对标 NavHost composable()
    '/device_demo': (context) => const DeviceDemoPage(),
  },
  home: Scaffold(...),  // 首页（不经过路由表）
)

// 跳转（项目里的底部按钮）
Navigator.pushNamed(context, '/device_demo');   // 对标 navController.navigate("device_demo")
Navigator.pushNamed(context, '/nav_home');

// 传参数跳转
Navigator.pushNamed(context, '/detail', arguments: {'id': 123});

// 目标页面接收参数
final args = ModalRoute.of(context)!.settings.arguments as Map<String, dynamic>;
final id = args['id'];
```

## 面试高频

> **Q: Flutter 的 Navigator 和 Android 的 Activity/Fragment 跳转区别？**
>
> A: 核心区别：Flutter 的页面跳转**不创建新的系统 Activity**，所有的页面都在一个 Activity（FlutterActivity）或 FlutterFragment 内通过 Widget 树切换。好处是切换更快（不走系统 IPC）、动画自定义程度高、状态不需要序列化（没有 `Intent.putExtra` 的大小限制）。代价是原生系统分享/多任务的集成需要通过 MethodChannel 手动桥接。

> **Q: Navigator.push 和 Navigator.pushNamed 的区别？**
>
> A: `push` 直接传 `Route` 对象（匿名路由），适合简单场景；`pushNamed` 传字符串路由名，需要在 `MaterialApp` 的 `routes` 表中已注册。命名路由的好处是集中管理、支持 `onGenerateRoute` 做拦截（如未登录跳登录页）。对标：`push` 对标匿名 `Intent`；`pushNamed` 对标注册了 `action` 的 `Intent`。

---

<a id="methodchannel"></a>
# 八、MethodChannel — 与原生通信

> **面试必问：Flutter 怎么调 Android 原生 API？你项目里怎么做的？**
> **这是混合架构的核心——项目里有两个 MethodChannel：历史 + 设备能力。**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：`MethodChannel` 是 Flutter 和原生平台（Android/iOS）通信的桥梁。Flutter 通过它调原生 API、原生通过它返回结果。
- **为什么需要**：Flutter 只能做纯 UI 的事——权限申请、蓝牙、WiFi、存储访问、相机、传感器等系统能力全都必须通过原生实现。MethodChannel 就是这条「Flutter -> 原生 -> 结果返回」的管道。
- **怎么用**：两边约定一个字符串通道名（如 `com.pomodoro/device`）-> Flutter 侧 `invokeMethod` 调 -> 原生侧 `MethodCallHandler` 处理 -> 返回结果。
- **对标 Android**：MethodChannel 对标 Binder + AIDL 的简化版（不需要写 `.aidl` 文件，通过字符串 key 匹配方法）。

## 通信模型

```
Flutter 侧 (Dart)                Native 侧 (Kotlin)
-------------------              -------------------
MethodChannel('通道名')           MethodChannel('通道名')
  .invokeMethod('方法名', {        .setMethodCallHandler { call, result ->
    参数...                          when (call.method) {
  })                                    '方法名' -> { ... result.success(数据) }
  .then((result) { ... })               ...
                                     }
                                   }
```

## 项目对照：设备能力 MethodChannel

这是你项目中**最复杂的 MethodChannel 实现**，涉及 `ActivityResultLauncher` 桥接（因为权限/SAF/蓝牙开启需要弹系统框拿结果）。

### Flutter 侧（flutter_module/lib/channels/device_channel.dart）

```dart
// 项目代码：device_channel.dart
class DeviceChannel {
  static const _channel = MethodChannel('com.pomodoro/device');  // 通道名，两边一致

  // 读取 WiFi 信息（不需要弹框，直接调）
  static Future<Map<String, dynamic>> getWifiInfo() async {
    try {
      final r = await _channel.invokeMethod<Map>('getWifiInfo');
      return r?.map((k, v) => MapEntry(k.toString(), v)) ?? {};
    } catch (e) {
      debugPrint('getWifiInfo error: $e');
      return {};
    }
  }

  // 扫描周边 WiFi（需要回调）
  static Future<List<Map<String, dynamic>>> startWifiScan() async {
    try {
      final r = await _channel.invokeListMethod('startWifiScan');
      return r?.map((e) => Map<String, dynamic>.from(e as Map)).toList() ?? [];
    } catch (e) {
      return [];
    }
  }

  // 请求权限（需要弹系统框）
  static Future<Map<String, bool>> requestPermissions(List<String> permissions) async {
    try {
      final result = await _channel.invokeMethod<Map>('requestPermissions', {
        'permissions': permissions,
      });
      return result?.map((k, v) => MapEntry(k.toString(), v == true)) ?? {};
    } catch (e) {
      return {};
    }
  }
}
```

### 原生侧（native-android/app/.../DeviceChannelHandler.kt）

```kotlin
// 项目代码：DeviceChannelHandler.kt
object DeviceChannelHandler {
    private const val CHANNEL_NAME = "com.pomodoro/device"  // 通道名和 Flutter 侧一致！

    fun register(engine: FlutterEngine, host: AppCompatActivity) {
        channel = MethodChannel(engine.dartExecutor.binaryMessenger, CHANNEL_NAME).also {
            it.setMethodCallHandler { call, result -> handle(call, result) }
        }
    }

    private fun handle(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getWifiInfo" -> result.success(DeviceCapabilities.getWifiInfo(ctx))
            "startWifiScan" -> DeviceCapabilities.startWifiScan(ctx) { list -> result.success(list) }
            "requestPermissions" -> {
                // 需要弹系统权限框 -> 用 ActivityResultLauncher 桥接
                // pendingXxx 暂存 result，等用户操作完回填
                val perms = call.argument<List<String>>("permissions") ?: emptyList()
                pendingPermission = { result.success(it) }
                permissionLauncher?.launch(perms.toTypedArray())
            }
            else -> result.notImplemented()
        }
    }
}
```

## 项目中两个 MethodChannel 的架构差异

| Channel | 通道名 | Flutter 侧 | 原生侧 | 注册位置 | 需要 Activity？ |
|---------|--------|-----------|--------|---------|----------------|
| 历史 | `com.pomodoro/history` | `method_channels.dart` | `MethodChannelHandler.kt` | `PomodoroApplication` | 否，纯数据操作 |
| 设备 | `com.pomodoro/device` | `device_channel.dart` | `DeviceChannelHandler.kt` | `FlutterContainerActivity` | 是，需要弹系统框 |

**设计意图**：不需要 Activity 的通道（如历史数据保存/查询）挂在 Application 层；需要 Activity 弹系统框的通道（如权限/SAF/蓝牙）挂在 Activity 层，用 `ActivityResultLauncher` 桥接异步结果。

## 面试高频

> **Q: 为什么设备能力 MethodChannel 要注册在 FlutterContainerActivity 而不是 Application？**
>
> A: 因为设备能力的某些方法（权限申请/SAF 选目录/蓝牙开启）需要**弹系统对话框**获取用户操作结果。Android 的系统对话框必须由 Activity 启动（需要 Window 上下文），`ActivityResultLauncher` 必须在 Activity `onCreate` 之前注册。如果把通道挂在 Application 层，就没办法弹任何需要用户交互的系统框。历史数据保存/查询不弹框，所以可以挂在 Application。

> **Q: Flutter 和原生的通信是同步还是异步？**
>
> A: **异步的。** Dart 侧的 `invokeMethod` 返回 `Future<T>`，原生侧的处理在 Platform Thread 上执行，不会阻塞 Flutter 的 UI 线程。如果需要原生返回大量数据，应该用分页或流式传输（EventChannel / StreamChannel），不要一次性传大数据。

---

<a id="addtoapp"></a>
# 九、Flutter 与 Android 混合开发 — Add-to-App

> **面试必问：你们项目 Flutter 怎么集成到原生 Android 的？不是独立的 Flutter App 吗？**
> **你项目里 native-android + flutter_module 就是这个架构。**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：Add-to-App 是 Flutter 官方提供的混合开发方案——把 Flutter 模块**作为子工程嵌入**现有原生 Android App，而不是用 Flutter 重写整个 App。
- **为什么需要**：现实中的大项目不会「从零用 Flutter 重写」——成本太高、风险太大。正确做法是**逐步引入**：新功能用 Flutter 写，老功能保持原生。你的目标公司就是这种做法。
- **怎么用**：创建 Flutter Module（`flutter_module`）-> 原生工程通过 Gradle 把它挂为子项目 `:flutter` -> 原生侧用 `FlutterFragment` 或 `FlutterActivity` 加载 -> 通过 `MethodChannel` 通信。

## 你的项目架构

```
native-android/ (Gradle 根项目)
+-- app/                        <- 原生壳（Kotlin + Compose）
|   +-- MainActivity.kt         -> 原生首页（4 个 Tab）
|   +-- FlutterContainerActivity.kt  -> 加载 Flutter 模块的容器
|   +-- build.gradle.kts        -> project(":flutter") 引用
+-- settings.gradle             -> include_flutter.groovy 挂载子项目
+-- flutter_module/             <- Flutter 模块（通过 .groovy 挂为 :flutter 子项目）
    +-- lib/
        +-- main.dart
        +-- app.dart
        +-- timer/timer_notifier.dart
```

### 关键文件：settings.gradle

```groovy
// settings.gradle — 通过 include_flutter.groovy 把 flutter_module 挂为 Gradle 子项目
setBinding(new Binding([gradle: this]))
evaluate(new File(settingsDir.parentFile, 'flutter_module/.android/include_flutter.groovy'))
// 效果：flutter_module 变成 Gradle 子项目 :flutter，可以直接 project(":flutter") 引用
```

### 关键文件：app/build.gradle.kts

```kotlin
// app/build.gradle.kts
dependencies {
    // 依赖 Flutter 模块（不是 jar/aar，是子项目源码依赖！）
    debugImplementation(project(":flutter"))
    releaseImplementation(project(":flutter"))
}
```

### 关键类：FlutterContainerActivity

```kotlin
// FlutterContainerActivity.kt — 原生 Activity 承载 Flutter 页面
class FlutterContainerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flutter_container)  // 只有个 FrameLayout

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.flutter_container,
                    FlutterFragment.withCachedEngine(PomodoroApplication.ENGINE_ID)
                        .shouldAttachEngineToActivity(false)    // 引擎生命周期 != Activity
                        .build()
                )
                .commit()
        }

        // 注册设备能力 MethodChannel（需要 Activity）
        FlutterEngineCache.getInstance()
            .get(PomodoroApplication.ENGINE_ID)?.let { engine ->
                DeviceChannelHandler.register(engine, this)
            }
    }
}
```

### Flutter 引擎预热（Application 层）

```kotlin
// PomodoroApplication.kt
class PomodoroApplication : Application() {
    companion object {
        const val ENGINE_ID = "pomodoro_engine"
    }

    lateinit var flutterEngine: FlutterEngine

    override fun onCreate() {
        super.onCreate()
        // 预热 FlutterEngine：在 App 启动时就初始化，打开 Flutter 页面时秒开
        flutterEngine = FlutterEngine(this).apply {
            // 注册历史数据 MethodChannel（不需要 Activity，挂在 Application）
            MethodChannel(dartExecutor.binaryMessenger, "com.pomodoro/history").also {
                it.setMethodCallHandler(MethodChannelHandler())
            }
        }
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
        FlutterEngineCache.getInstance().put(ENGINE_ID, flutterEngine)
    }
}
```

## 面试高频

> **Q: Flutter 你是怎么集成到原生 Android App 里的？**
>
> A: "我用的 Flutter 官方 Add-to-App 方案。`flutter_module` 是一个 Flutter Module，通过 Gradle `include_flutter.groovy` 挂为子项目 `:flutter`，原生 `app/build.gradle.kts` 通过 `project(":flutter")` 引用。原生侧用 `FlutterFragment.withCachedEngine()` 加载预热好的 FlutterEngine。
> 两个 MethodChannel：`com.pomodoro/history` 负责历史数据 CRUD（挂在 Application），`com.pomodoro/device` 负责设备能力调用（挂在 Activity，因为需要弹系统框）。DeviceCapabilities 共享层让原生 Compose 设置页和 Flutter MethodChannel 共用同一份能力实现。"

> **Q: 为什么要预热 FlutterEngine？**
>
> A: FlutterEngine 初始化需要 ~200-500ms（Dart VM 启动 + 加载资源 + 渲染第一帧）。如果不预热，用户每次打开 Flutter 页面都要等这个时间。在 Application 层预热后，`FlutterContainerActivity` 直接用 `withCachedEngine` 取，几乎是秒开。

---

<a id="async"></a>
# 十、异步编程 — Future / async-await / Stream

> **对标 Kotlin：Dart 的异步模型和 Kotlin 协程 90% 相同**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：Dart 的异步编程核心是 `Future<T>`（对标 `suspend fun(): T`）和 `Stream<T>`（对标 `Flow<T>`）。
- **和 Kotlin 协程的对照**：

| Dart | Kotlin | 说明 |
|------|--------|------|
| `Future<T>` | `suspend fun(): T` | 一次异步结果 |
| `async { await ... }` | `launch { ... }` | 启动异步块 |
| `await futureExpression` | 同名 | 等待异步结果 |
| `Stream<T>` | `Flow<T>` | 多值异步数据流 |
| `await for (x in stream)` | `flow.collect { x -> ... }` | 消费流 |
| `StreamController` | `Channel` / `MutableSharedFlow` | 手动控制数据流 |
| `try/catch on Exception` | 同名 | 异常处理 |

## Future 基本用法

```dart
// Future<T> 对标 Kotlin suspend 函数

// 定义一个异步函数（对标 suspend fun fetchUser(): User）
Future<User> fetchUser(int id) async {
  await Future.delayed(Duration(seconds: 1));  // 对标 delay(1000)
  return User(id: id, name: '张三');
}

// 调用：await 等待（对标 直接调 suspend 函数）
void loadUser() async {
  try {
    final user = await fetchUser(123);   // 对标 val user = fetchUser(123)
    print(user.name);
  } catch (e) {
    print('出错: $e');
  }
}

// 链式调用（不用 await）
fetchUser(123)
  .then((user) => print(user.name))      // 对标 launch { val u = fetchUser(123); ... }
  .catchError((e) => print('出错: $e'));
```

## Stream：对标 Flow

```dart
// Stream 对标 Flow（冷流，不消费不生产）

// 创建 Stream
Stream<int> countDown(int from) async* {   // async* = Flow 的 flow { }
  for (int i = from; i >= 0; i--) {
    await Future.delayed(Duration(seconds: 1));
    yield i;                              // 对标 emit(i)
  }
}

// 消费 Stream（对标 flow.collect { ... }）
void listenDemo() async {
  await for (final value in countDown(5)) {
    print(value);  // 5, 4, 3, 2, 1, 0
  }
}
```

| Dart Stream | Kotlin Flow |
|-------------|-------------|
| `async* { yield x; }` | `flow { emit(x) }` |
| `await for (x in stream)` | `flow.collect { x -> }` |
| `stream.map(...)` | `flow.map { ... }` |
| `stream.where(...)` | `flow.filter { ... }` |
| `StreamController` | `Channel` |
| `stream.listen(...)` | `flow.launchIn(scope)` |

## 面试高频

> **Q: Dart 的 Future 和 Kotlin 的 suspend 函数有什么区别？**
>
> A: 从使用者角度看，几乎一样——都是用 `await` 等待异步结果。底层不同：Kotlin 的 `suspend` 编译成 CPS 状态机（Continuation），不阻塞线程；Dart 的 `Future` 是事件循环模型（类似 JavaScript 的 Promise），也不阻塞线程。Dart 是单线程模型（Isolate），Kotlin/JVM 是多线程模型。

> **Q: async* 和 async 有什么区别？**
>
> A: `async` 函数返回 `Future<T>`（一次结果）；`async*` 函数返回 `Stream<T>`（多次结果，用 `yield` 发射）。对标 Kotlin：`async` 对标 `suspend fun(): T`；`async*` 对标 `fun(): Flow<T> = flow { emit(...) }`。

---

<a id="network"></a>
# 十一、网络请求 — dio / http

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：`dio` 是 Flutter 最流行的 HTTP 客户端库，对标 Android 的 Retrofit + OkHttp。
- **为什么不用原生 http**：`dart:io` 自带 `HttpClient`，但 API 太底层。`dio` 提供了拦截器、超时、请求取消、文件上传下载、Cookie 管理等开箱即用功能。

## dio 基本用法（对标 Retrofit）

```dart
// dio 对标 Retrofit + OkHttp 的组合

// 1. 创建 dio 实例（对标 OkHttpClient + Retrofit.Builder）
final dio = Dio(BaseOptions(
  baseUrl: 'https://api.example.com',
  connectTimeout: Duration(seconds: 10),
  receiveTimeout: Duration(seconds: 10),
));

// 2. 添加拦截器（对标 OkHttp Interceptor）
dio.interceptors.add(InterceptorsWrapper(
  onRequest: (options, handler) {
    options.headers['Authorization'] = 'Bearer $token';
    handler.next(options);  // 继续请求
  },
  onError: (error, handler) {
    print('请求失败: ${error.message}');
    handler.next(error);
  },
));

// 3. 发起请求（对标 Retrofit 接口方法）
Future<User> fetchUser(int id) async {
  try {
    final response = await dio.get('/user/$id');
    if (response.statusCode == 200) {
      return User.fromJson(response.data);  // fromJson 对标 Moshi/Gson 反序列化
    }
    throw Exception('请求失败: ${response.statusCode}');
  } on DioException catch (e) {
    // DioException 对标 Retrofit 的 HttpException
    print('网络错误: ${e.message}');
    rethrow;
  }
}

// 4. POST 请求
Future<void> createUser(String name) async {
  final response = await dio.post('/user', data: {'name': name});
  // ...
}
```

## 与 Riverpod 配合（对标 ViewModel + Retrofit）

```dart
// Flutter 写法（没有项目代码参考，这是标准做法）
class UserNotifier extends Notifier<UserState> {
  final dio = Dio(BaseOptions(baseUrl: 'https://api.example.com'));

  @override
  UserState build() => UserState.loading();

  Future<void> loadUser(int id) async {
    state = UserState.loading();
    try {
      final response = await dio.get('/user/$id');
      final user = User.fromJson(response.data);
      state = UserState.success(user);
    } catch (e) {
      state = UserState.error(e.toString());
    }
  }
}
```

> 注意：你的 `jetpack-android` 项目里有 `NasApiService`（Retrofit 网络层），Flutter 侧目前没有网络层——`flutter_module` 里不需要联网。

---

<a id="sqflite"></a>
# 十二、本地存储 — sqflite（对标 Room）

> **对标 Room：你的 jetpack-android 用 Room + Flow 自动刷新，flutter_module 用 sqflite 手写 SQL。**

[返回目录](#catalog)

## 总览：是什么 / 解决什么 / 怎么用

- **是什么**：`sqflite` 是 Flutter 最常用的 SQLite 插件，和 Android 的 `SQLiteOpenHelper` 是同一层级的封装——你需要手写 SQL。
- **和 Room 的区别**：Room 用注解生成代码（`@Entity` / `@Dao` / `@Query`），自动把数据库结果映射为 Kotlin 对象。sqflite 要手写 `CREATE TABLE` SQL 和手写 `fromMap`/`toMap` 映射。**Room 更高级（ORM），sqflite 更底层（手写 SQL）。**
- **为什么项目用 sqflite 而不用 drift（对标 Room 的 Flutter 库）**：保持简单，演示手写 SQL 能力（面试能讲 "我知道 SQLite 底层怎么操作"）。

## 项目对照：history_db.dart

```dart
// 项目代码：history_db.dart —— sqflite 封装
class HistoryDb {
  static final HistoryDb _instance = HistoryDb._();
  factory HistoryDb() => _instance;          // 单例模式（对标 Kotlin object）
  HistoryDb._();

  Database? _db;                             // SQLiteDatabase 引用

  Future<Database> get database async {
    if (_db != null) return _db!;
    _db = await openDatabase(
      join(await getDatabasesPath(), 'pomodoro.db'),
      onCreate: (db, version) async {        // 对标 SQLiteOpenHelper.onCreate()
        await db.execute('''
          CREATE TABLE pomodoro_records (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            duration INTEGER NOT NULL,
            status TEXT NOT NULL,
            created_at TEXT NOT NULL
          )
        ''');
      },
      version: 1,
    );
    return _db!;
  }

  // 插入（对标 @Insert suspend fun insert(record: PomodoroRecord)）
  Future<int> insertRecord(PomodoroRecord record) async {
    final db = await database;
    return await db.insert('pomodoro_records', record.toMap());
  }

  // 查询全部（对标 @Query("SELECT * FROM pomodoro_records ORDER BY created_at DESC")）
  Future<List<PomodoroRecord>> getAllRecords() async {
    final db = await database;
    final List<Map<String, dynamic>> maps = await db.query(
      'pomodoro_records',
      orderBy: 'created_at DESC',
    );
    return maps.map((map) => PomodoroRecord.fromMap(map)).toList();
  }

  // 删除
  Future<void> deleteAll() async {
    final db = await database;
    await db.delete('pomodoro_records');
  }
}
```

## sqflite vs Room 对照表

| 功能 | sqflite（Flutter） | Room（Android） |
|------|-------------------|-----------------|
| 建表 | 手写 `CREATE TABLE` SQL | `@Entity` 注解自动生成 |
| DAO | 手写 `db.insert/query/delete` | `@Dao` 接口 + 注解 |
| 数据映射 | 手动 `fromMap` / `toMap` | 自动生成 |
| 自动刷新 | 否，手动重新查 | 是，返回 `Flow`，数据库变化自动通知 |
| 类型安全 | 否，`Map<String, dynamic>` | 是，编译期检查 |
| 数据库升级 | 手写 `onUpgrade` SQL | `Migration` 类 |

> **面试话术**："项目中 sqflite 是直接操作 SQLite，对标 Android 的 SQLiteOpenHelper 层级。Room 是更高级的 ORM 封装，自动生成 DAO、支持 Flow 自动刷新。如果项目数据库复杂，Flutter 可以用 drift 库替代 sqflite——drift 对标 Room，也支持 Stream 自动刷新。"

---

<a id="lifecycle"></a>
# 十三、生命周期 — Widget State 生命周期

[返回目录](#catalog)

## Flutter 应用生命周期（对标 Activity 生命周期）

| Flutter | Android | 何时触发 |
|---------|---------|---------|
| `resumed` | `onResume` | App 可见且可交互 |
| `inactive` | `onPause` | App 可见但不可交互（如来电） |
| `paused` | `onStop` | App 不可见 |
| `detached` | `onDestroy` | FlutterEngine 即将销毁 |

```dart
// WidgetsBindingObserver：监听应用前后台切换
class _MyAppState extends State<MyApp> with WidgetsBindingObserver {

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);  // 注册监听
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.paused) {
      print('App 切到后台');
    } else if (state == AppLifecycleState.resumed) {
      print('App 回到前台');
    }
  }

  @override
  void dispose() {
    WidgetsBinding.instance.removeObserver(this);  // 移除监听
    super.dispose();
  }

  @override
  Widget build(BuildContext context) => Container();
}
```

## Widget 生命周期 vs State 生命周期

| 阶段 | State 方法 | 对标 Android |
|------|-----------|-------------|
| 创建 | `createState()` -> `initState()` | `Fragment.onAttach()` -> `onCreate()` |
| 依赖变化 | `didChangeDependencies()` | `onCreate() + observer.notify()` |
| 重建 | `build()` | Composable 函数体 |
| 更新 | `didUpdateWidget(oldWidget)` | `onNewIntent()` |
| 移除 | `deactivate()` -> `dispose()` | `onDestroyView()` -> `onDestroy()` |

## 面试高频

> **Q: Flutter App 切到后台，Timer 还在跑吗？**
>
> A: **在跑。** Flutter Engine 在 App 后台时不会自动暂停 Dart 代码。如果你有 `Timer.periodic` 在计时，切到后台它还在嘀嗒。正确的做法是监听 `didChangeAppLifecycleState`，切到后台时暂停 Timer、回到前台时恢复。项目里没做这个优化（简单 Demo 不需要），生产环境必须加上。

---

<a id="context"></a>
# 十四、BuildContext — 深入理解

[返回目录](#catalog)

## BuildContext 不是你想象的"上下文"

`BuildContext` 最容易误解的点：它**不是**普通的"上下文对象"，它是一个 `Element` 的抽象接口——**每个 `BuildContext` 就是一个 Element**。

```
Widget 树                    Element 树（BuildContext）
---------                   -------------------------
MaterialApp                 MaterialApp Element
  +-- Scaffold                +-- Scaffold Element
      +-- Column                  +-- Column Element
          +-- Text                    +-- Text Element
```

你在 `build(BuildContext context)` 里拿到的 `context`，就是当前 Widget 对应的 `Element`。

## 常见用法

```dart
// 1. 获取主题
final theme = Theme.of(context);         // 对标 context.getTheme()

// 2. 获取导航器
Navigator.of(context).push(...);         // 对标 context.startActivity()

// 3. 获取屏幕尺寸
final size = MediaQuery.of(context).size;

// 4. 获取 Scaffold 的 SnackBar
ScaffoldMessenger.of(context).showSnackBar(
  SnackBar(content: Text('操作成功')),
);

// 常见错误：在 initState 里用 context
@override
void initState() {
  super.initState();
  // Navigator.of(context).push(...);  // 错误！此时 context 还没完全初始化！
  // 正确做法：
  WidgetsBinding.instance.addPostFrameCallback((_) {
    Navigator.of(context).push(...);   // 正确：第一帧后再操作
  });
}
```

---

<a id="threetrees"></a>
# 十五、Flutter 三棵树 — Widget / Element / RenderObject

> **面试高频：Flutter 的三棵树分别是什么？为什么要分开？**

[返回目录](#catalog)

## 三棵树是 Flutter 高性能的核心秘密

```
Widget 树      ->    Element 树      ->    RenderObject 树
(不可变配置)        (可变桥梁)            (真正负责绘制)
    |                   |                       |
    |  每次 build       |  复用/更新            |  布局+绘制
    |  都创建新对象      |                       |
    v                   v                       v
  Column              Column                RenderFlex
    +-- Text            +-- Text                +-- RenderParagraph
    +-- Button          +-- Button              +-- RenderBox
```

| 树 | 作用 | 可变？ | 对标 Android |
|----|------|--------|-------------|
| Widget 树 | 配置描述（"UI 应该长什么样"） | 否，不可变，每次 build 新对象 | Compose 的 Composable 函数调用链 |
| Element 树 | 桥梁——管理 Widget 和 RenderObject 的生命周期 | 是，可变，复用 | View 的引用（但不是 View 本身） |
| RenderObject 树 | 真正负责布局、绘制、命中测试 | 是，可变，复用 | View 的 measure/layout/draw |

## 为什么分开？

**因为 Widget 不可变，Flutter 能精确知道「哪里变了」**：

1. 你调 `setState` -> `build()` 重新执行 -> 生成新的 Widget 树
2. Flutter 拿着新 Widget 树和 Element 树上挂着的旧 Widget 树对比
3. 类型和 key 一样 -> Element 复用（不重建 RenderObject！）
4. 类型或 key 不同 -> Element 重建 -> RenderObject 也跟着重建

这就是为什么「Flutter build 很频繁但性能不差」——Widget 树全重建只是创建了一堆轻量的配置对象，真正的重活（布局和绘制）只在 RenderObject 树有变化时才做。

> **对标 Compose**：Compose 的 Composable 函数对标 Widget，Compose 的 recomposition slot table 对标 Element 树，Compose 的 LayoutNode 对标 RenderObject。Flutter 和 Compose 的渲染模型几乎一样：声明式配置 -> 智能 diff -> 局部更新。

## 面试高频

> **Q: Flutter 每次 setState 都重建整个 Widget 树，为什么性能还不差？**
>
> A: 因为 Widget 只是轻量的配置描述（几十个字节），创建和销毁成本极低。真正的性能瓶颈——**布局和绘制**——发生在 RenderObject 树。Element 树做了「智能复用」：Widget 类型和 key 匹配时，Element 和 RenderObject 都复用，不需要重新 layout/draw。所以重建的只是配置对象，不是渲染对象。

---

<a id="animation"></a>
# 十六、动画 — AnimationController / Tween

[返回目录](#catalog)

```dart
// 基础动画：从小到大淡入
class AnimatedBox extends StatefulWidget {
  @override
  State<AnimatedBox> createState() => _AnimatedBoxState();
}

class _AnimatedBoxState extends State<AnimatedBox>
    with SingleTickerProviderStateMixin {  // mixin = 提供 Ticker
  late AnimationController _controller;     // 对标 Animator
  late Animation<double> _animation;        // 对标 ValueAnimator

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: Duration(seconds: 1),
      vsync: this,                           // Ticker 提供者
    );
    _animation = Tween<double>(begin: 0.0, end: 1.0)  // 对标 ValueAnimator.ofFloat(0f, 1f)
        .animate(CurvedAnimation(parent: _controller, curve: Curves.easeInOut));

    _controller.forward();                   // 对标 animator.start()
  }

  @override
  void dispose() {
    _controller.dispose();                   // 对标 animator.cancel()
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return AnimatedBuilder(                  // 对标 addUpdateListener + 自动重组
      animation: _animation,
      builder: (context, child) {
        return Opacity(
          opacity: _animation.value,         // 0.0 -> 1.0
          child: Container(
            width: 200 * _animation.value,   // 0 -> 200
            height: 200 * _animation.value,
            color: Colors.blue,
          ),
        );
      },
    );
  }
}
```

**对标 Android**：

| Flutter | Android |
|---------|---------|
| `AnimationController` | `ValueAnimator` / `ObjectAnimator` |
| `Tween(begin, end)` | `ValueAnimator.ofFloat(0f, 1f)` |
| `CurvedAnimation` | `Interpolator`（如 `AccelerateDecelerateInterpolator`） |
| `AnimatedBuilder` | `AnimatorUpdateListener` |
| `addListener(() => setState())` | `animator.addUpdateListener { invalidate() }` |
| `controller.forward()` | `animator.start()` |

---

<a id="custompaint"></a>
# 十七、自定义绘制 — CustomPaint / Canvas

[返回目录](#catalog)

对标 Android 的自定义 View 的 `onDraw(Canvas canvas)`：

```dart
// Flutter 自定义绘制 对标 Android 自定义 View
class CirclePainter extends CustomPainter {     // 对标 extends View + override onDraw
  @override
  void paint(Canvas canvas, Size size) {       // 对标 onDraw(Canvas canvas)
    final paint = Paint()                       // 对标 new Paint()
      ..color = Colors.red                      // .. = setColor(...)
      ..style = PaintingStyle.fill;

    canvas.drawCircle(
      Offset(size.width / 2, size.height / 2), // 圆心 对标 (cx, cy)
      size.width / 2,                           // 半径
      paint,
    );
  }

  @override
  bool shouldRepaint(covariant CirclePainter old) => false;  // 是否需要重绘
}

// 使用
CustomPaint(
  size: Size(100, 100),
  painter: CirclePainter(),
)
```

---

<a id="performance"></a>
# 十八、性能优化

[返回目录](#catalog)

| 优化手段 | 对标 Android | 说明 |
|---------|-------------|------|
| `const` 构造函数 | Compose `@Stable` | 编译时常量，Flutter 跳过这部分的重建 |
| `RepaintBoundary` | `View.setLayerType(LAYER_TYPE_HARDWARE)` | 隔离重绘区域 |
| `ListView.builder` | `RecyclerView` | 只渲染可见项 |
| `compute()` | `AsyncTask` / `withContext(Dispatchers.Default)` | 在独立 Isolate 做计算 |
| `const` Widget 提取 | Compose 提取为独立 Composable | 减少 build 范围 |
| `devtools` | Android Profiler | Flutter DevTools 分析性能 |

```dart
// const 优化：能用 const 就用 const
// 不好
Padding(padding: EdgeInsets.all(16), child: Text('Hello'))
// 好（如果参数都是常量）
const Padding(padding: EdgeInsets.all(16), child: Text('Hello'))

// RepaintBoundary：隔离重绘
RepaintBoundary(
  child: AnimatedWidget(...),  // 只有这片区域会在动画时重绘
)
```

---

<a id="interview"></a>
# 十九、Flutter 面试高频问题总汇

[返回目录](#catalog)

> **Q1: Flutter 和 React Native 的区别？**
>
> A: Flutter 自己画（Skia），不依赖平台原生控件；RN 通过 Bridge 调原生控件渲染。Flutter 跨平台一致性更高，性能更好（无 Bridge 开销）；RN 可以用原生 UI 风格（Android 用 Material，iOS 用 Cupertino），但 Bridge 通信是瓶颈。

> **Q2: Widget 树、Element 树、RenderObject 树分别是什么？**
>
> A: 见[第十五章](#threetrees)。核心：Widget 是不可变配置，Element 是可变桥梁（生命周期管理），RenderObject 真正做布局和绘制。

> **Q3: setState 做了什么？**
>
> A: 标记当前 Element 为"脏" -> 调度 rebuild -> `build()` 重新执行 -> 新旧 Widget 树 diff -> 更新 RenderObject 树。不立即执行，在当前帧结束时统一处理。

> **Q4: Flutter 热重载的原理？**
>
> A: Dart VM 支持 Hot Reload——把修改后的 Dart 源码重新编译为增量 kernel 文件 -> 注入到运行中的 Dart VM -> 重新初始化所有 State -> 重新 build -> UI 瞬间更新。不重启 App、不丢失状态。

> **Q5: Flutter 的单线程模型怎么处理耗时操作？**
>
> A: UI 线程（也叫 Platform Thread）处理 Dart 代码 + 渲染。耗时操作用 `async/await`（不阻塞 UI 线程，Dart 事件循环处理）、或用 `compute()` 在独立 Isolate 执行（类似 Android 子线程）。

---

<a id="enterprise"></a>
# 二十、企业项目：native-android + flutter_module 混合架构解析

[返回目录](#catalog)

## 你的项目完整架构

```
native-android (原生壳)
+-- MainActivity -> MainTabScreen
|   +-- Tab 0: 番茄钟（启动 Flutter 番茄钟 按钮）
|   +-- Tab 1: 统计（占位）
|   +-- Tab 2: 设置（DeviceCapabilities: 权限/WiFi/蓝牙）<- 原生 Compose
|   +-- Tab 3: 关于（占位）
|
+-- FlutterContainerActivity（加载 flutter_module）
|   +-- 注册 DeviceChannelHandler("com.pomodoro/device")
|
+-- PomodoroApplication
    +-- 预热 FlutterEngine + 注册 MethodChannelHandler("com.pomodoro/history")

flutter_module (Flutter 跨端模块)
+-- PomodoroApp
|   +-- TimerWidget (番茄钟计时)
|   +-- /nav_home (4级导航演示)
|   +-- /device_demo (设备能力 Demo)
+-- TimerNotifier (Riverpod Notifier = ViewModel)
+-- DeviceChannel (MethodChannel "com.pomodoro/device")
+-- MethodChannels (MethodChannel "com.pomodoro/history")
+-- HistoryDb (sqflite)
```

## 关键面试话术

> "我的项目是一个原生 + Flutter 混合架构的 App，对标目标公司的绿联云 NAS 客户端技术栈：
> - 原生壳（`native-android`）负责导航、设备能力、设置等需要系统 API 的功能
> - Flutter 模块（`flutter_module`）作为 Gradle 子项目被嵌入，负责番茄钟计时等纯 UI 功能
> - 两个 MethodChannel 做双端通信：历史数据 CRUD（挂在 Application）、设备能力调用（挂在 Activity，因为需要弹系统框）
> - DeviceCapabilities 是一个共享层，原生 Compose 设置页和 Flutter MethodChannel 共用同一份设备能力实现，避免重复编码
> - 状态管理用 Riverpod，对标 Android 的 ViewModel + StateFlow
> - 数据持久化用 sqflite，对标 Android 的 Room（手写 SQL，演示对数据库底层的理解）
> - FlutterEngine 在 Application 层预热，打开 Flutter 页面秒开"

---

<a id="compare"></a>
# 二十一、Flutter vs Compose 概念对照总表

[返回目录](#catalog)

| Flutter | Jetpack Compose | 说明 |
|---------|----------------|------|
| `Widget` | `@Composable` 函数 | UI 构建基本单位 |
| `StatelessWidget` | 无状态的 Composable | 纯展示 |
| `StatefulWidget` + `State` | Composable + `remember { mutableStateOf() }` | 有本地状态 |
| `setState(() { ... })` | 直接改 `mutableStateOf` 的值 | 触发重建 |
| `BuildContext` | `Context` + 组合作用域 | 位置信息 + 环境访问 |
| `Column` / `Row` | `Column` / `Row` | 几乎一样 |
| `Stack` | `Box` | 层叠布局 |
| `Container` | `Box` + `Modifier` | 万能装饰 |
| `Modifier` (无) | `Modifier` | Flutter 用独立 Widget（Padding/SizedBox）替代 |
| `ListView.builder` | `LazyColumn` | 懒加载列表 |
| `Navigator.push/pop` | `NavController.navigate/popBackStack` | 页面导航 |
| `Notifier<T>` (Riverpod) | `ViewModel` + `StateFlow<T>` | 状态管理 |
| `ref.watch(provider)` | `viewModel.uiState.collectAsState()` | 订阅状态 |
| `copyWith(...)` | `data class copy(...)` | 不可变状态更新 |
| `MethodChannel` | AIDL / Binder | 平台通信 |
| `const` Widget | 编译器优化 | 跳过重建 |
| `MaterialApp.theme` | `MaterialTheme` | 全局主题 |

---

<a id="recommend"></a>
# 二十二、推荐学习路线

[返回目录](#catalog)

### 第 1 步（1-2 天）：Widget + 布局
打开 `flutter_module/lib/app.dart` 和 `timer_widget.dart`，对着读。每个 Widget 问自己「这在 Android 里对应什么？」。重点理解：**布局也是 Widget**（和 XML 完全不同的思维）。

### 第 2 步（2-3 天）：状态管理
读 `timer_notifier.dart`，对照你 jetpack-android 里的 `PomodoroViewModel`。理解 `Notifier` = `ViewModel`，`state` = `MutableStateFlow`。

### 第 3 步（1-2 天）：MethodChannel + 混合架构
读 `device_channel.dart` + `DeviceChannelHandler.kt` + `FlutterContainerActivity.kt`。理解通道的注册时机（Application vs Activity）和共享层 `DeviceCapabilities` 的设计意图。

### 第 4 步（1 天）：异步编程
读 `history_db.dart` 的 `async/await`，理解 `Future` = `suspend fun`，`Stream` = `Flow`。

### 第 5 步：写 Flutter 代码
给 `flutter_module` 加一个新页面（如统计页），从 UI -> 状态 -> 数据三层全部自己写一遍。

---

# 总结

Flutter 和 Jetpack Compose 的核心思想**一模一样**：声明式 UI（`UI = f(state)`）+ 不可变状态（`copyWith`）+ 响应式更新（自动 diff + 局部渲染）。你已有的 Jetpack 经验是学习 Flutter 最好的跳板——每看到一个 Flutter 概念，先想「这在 Compose 里对应什么」，然后只需要学 Dart 语法差异。

**三个最关键的心智转换**：
1. **布局也是 Widget**——没有 XML，没有单独的布局文件
2. **Widget 不可变**——每次状态变化都创建新 Widget 树，Flutter 自动 diff
3. **一切通过 MethodChannel**——Flutter 只是个 UI 框架，系统能力全靠桥接原生
