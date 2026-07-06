<!-- applies_to: mobile — 手机端 + 三套 Android 对照架构 -->

# 手机 Android 架构规则（三套对照项目专用）

> 适用：本项目三套 Android 子项目 + Flutter 模块，手机端，普通应用签名。

---

## 1. 三套子项目架构对照

### legacy-android（传统 Java + XML）
| 要素 | 实现 |
|------|------|
| 语言 | Java |
| UI | XML 布局 + `findViewById` |
| 导航 | Navigation Component（XML `nav_graph.xml`） |
| 底部 Tab | `TabContainerFragment` + `ChildFragmentManager` hide/show |
| 计时器 | `Handler.postDelayed()` |
| 数据库 | `SQLiteOpenHelper` 手写 SQL |
| 状态管理 | Fragment 成员变量（mutable） |

### jetpack-android（现代 MVVM）
| 要素 | 实现 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose `@Composable` |
| 导航 | Navigation Compose（代码 `AppNavHost.kt`） |
| 底部 Tab | `MainTabScreen` + `NavigationBar` + `mutableIntStateOf` |
| 计时器 | `Coroutines delay` + `StateFlow` |
| 数据库 | Room `@Database` + `@Dao` + `Flow` |
| 状态管理 | `ViewModel` + `StateFlow<TimerUiState>`（不可变 `data class`） |

### native-android（Flutter Add-to-App 壳）
| 要素 | 实现 |
|------|------|
| 语言 | Kotlin |
| UI | Compose（原生部分）+ Flutter AAR（计时器部分） |
| 导航 | Navigation Compose（同 jetpack） |
| 底部 Tab | `MainTabScreen` + `NavigationBar`（同 jetpack，Tab1 跳 Flutter） |
| 计时器 | **Flutter 模块**（`Timer.periodic` + Riverpod） |
| 数据库 | sqflite（Flutter 侧） |
| 状态管理 | Flutter `TimerNotifier` + `TimerState`（`copyWith`） |
| 混合通信 | `MethodChannel`（`com.pomodoro/history`） |

### flutter_module（Flutter 模块）
| 要素 | 实现 |
|------|------|
| 语言 | Dart |
| UI | Widget tree + `CustomPaint` |
| 导航 | `Navigator.push` |
| 计时器 | `Timer.periodic` + Riverpod `Notifier` |
| 数据库 | sqflite `openDatabase()` |
| 状态管理 | `TimerNotifier` + `TimerState`（`copyWith` 不可变） |
| 部署 | 编译为 AAR，嵌入 `native-android` |

---

## 2. 导航方案对照

### 四级导航路由对照表
| 等级 | legacy-android | jetpack / native-android | flutter_module |
|------|---------------|--------------------------|----------------|
| Tab 容器 | `tabContainerFragment` (R.id) | `"main"` (字符串) | — |
| 第 2 级 | `homeFragment` | `"home"` | `HomePage` |
| 第 3 级 | `listFragment` | `"list"` | `ListPage` |
| 第 4 级 | `detailFragment/{itemId}` | `"detail/{itemId}"` | `DetailPage(itemId:)` |
| 第 5 级 | `commentFragment/{itemId}` | `"comment/{itemId}"` | `CommentPage(itemId:)` |
| 回到首页 | `popBackStack(R.id.tabContainer, false)` | `popBackStack("main", false)` | `Navigator.popUntil` |

### 传参方式对照
| 方案 | 传参方式 | 特点 |
|------|---------|------|
| legacy Fragment | `Bundle(args).putString("itemId", id)` | 运行时取，无类型检查 |
| Compose NavHost | `navController.navigate("detail/$itemId")` | 字符串路由，运行时解析 |
| Flutter Navigator | `DetailPage(itemId: itemId)` | 构造函数传参，编译期类型检查 |

### 导航改动规则
- 改路由名时，**三套同步改**（legacy 的 `nav_graph.xml` + jetpack/native 的 `AppNavHost.kt`）
- 新增导航级别时，先在 `jetpack-android` 实现，再对照到另外两套
- `native-android` 的 Tab1 → `FlutterContainerActivity` 是特殊跳转，不走 NavHost

---

## 3. 计时器实现对照

| 要素 | legacy | jetpack | native | flutter |
|------|--------|---------|--------|---------|
| 倒计时 | `Handler.postDelayed` | `Coroutines delay` | Flutter 模块内 | `Timer.periodic` |
| 状态 | Fragment 成员变量 | `StateFlow<TimerUiState>` | Flutter `TimerState` | `TimerState` |
| 不可变性 | 直接修改成员 | `data class.copy()` | `copyWith()` | `copyWith()` |
| UI 更新 | `tvTime.setText()` | Compose 自动重组 | Widget 自动重建 | Widget 自动重建 |
| 停止回收 | `handler.removeCallbacks()` | `coroutineScope.cancel()` | `_timer?.cancel()` | `_timer?.cancel()` |

### 计时器改动规则
- `legacy-android` 的 `TimerFragment`：改计时逻辑时保持 `Handler` 风格，不要混入 Coroutines
- `jetpack-android` 的 `TimerViewModel`：状态必须走 `StateFlow`，不要用 `LiveData` 混搭
- `native-android`：计时器在 Flutter 模块中，原生侧只管加载和通信

---

## 4. 数据层对照

| 要素 | legacy | jetpack | native | flutter |
|------|--------|---------|--------|---------|
| 数据库 | SQLiteOpenHelper | Room | sqflite（Flutter 侧） | sqflite |
| 实体 | POJO | `@Entity` data class | Dart class | Dart class |
| 查询 | `db.rawQuery()` | `@Query` + `Flow` | `db.query()` | `db.query()` |
| 异步 | 主线程（不推荐） | `Flow` 自动 | `Future` | `Future` |

### 数据层改动规则
- `legacy-android` 的 `DatabaseHelper.java`：改表结构时同步改 `onCreate` 和 `onUpgrade`
- `jetpack-android` 的 Room：改 `@Entity` 时升 `version`，写 `Migration`
- `flutter_module` 的 `history_db.dart`：改表时同步改 `onCreate` 和 `onUpgrade`
- **MethodChannel 的 `saveRecord`/`getRecords` 当前为 TODO 占位**，未来接入 Room 时只改 `MethodChannelHandler.kt`

---

## 5. 底部 Tab 架构对照

### legacy-android（FragmentTransaction 手动管理）
- `TabContainerFragment` + `ChildFragmentManager` + `hide/show`
- 切换不重建 Fragment
- 四级导航时 `NavController` 全屏替换 `TabContainerFragment`

### jetpack-android / native-android（Compose）
- `MainTabScreen` + `NavigationBar` + `mutableIntStateOf` + `when`
- Tab1：jetpack 是 `TimerScreen`，native 是 `FlutterTabContent`（按钮跳 Flutter）
- Tab2-4：占位 `PlaceholderContent`

### Tab 改动规则
- 加新 Tab 时，三套同步加（legacy 改 `TabContainerFragment` + XML，jetpack/native 改 `MainTabScreen`）
- native-android 的 Tab1 是特殊设计（跳 Flutter），不要改成直接嵌入 Flutter UI

---

## 6. 生命周期

- ANR 阈值：**前台 5 秒 / 后台 200 秒**
- `onDestroy` / `onCleared`：反注册 listener、停止计时器、释放资源
- 配置变更（旋转）：`jetpack-android` 用 ViewModel 存活；`legacy-android` 可在 Manifest 锁方向
- 后台限制（Android 8+）：后台 Service 受限，用 WorkManager（本项目暂未涉及）

---

## 7. 对照修改原则（核心纪律）

### 什么时候三套同步改
- 导航路由变更（加/删/改路由名）
- 底部 Tab 结构变更
- 数据模型字段变更（PomodoroRecord/Entity）
- 功能新增（如加一个新页面）

### 什么时候只改一套
- 计时器 UI 样式微调（各套独立）
- 单套 bug 修复
- 教学注释更新

### 同步修改顺序
1. 先改 `jetpack-android`（现代版，最清晰）
2. 对照改 `legacy-android`（等价 Java 写法）
3. 对照改 `native-android`（等价 Kotlin + Compose，注意 Flutter 交互部分）
4. 如涉及 Flutter 模块，最后改 `flutter_module`

---

## 8. 改前检查清单（手机端专用）

```
□ 确认改哪套？三套同步还是只改一套？
□ IO 在主线程? → Dispatchers.IO / HandlerThread / async-await
□ 导航路由改了? → 三套 nav_graph / AppNavHost 同步
□ Tab 结构改了? → 三套 TabContainer / MainTabScreen 同步
□ MethodChannel 改了? → 原生和 Flutter 两端同步
□ 计时器改了? → onDestroy/onCleared 回收资源
□ 走 confirm-first 输出方案?
```
