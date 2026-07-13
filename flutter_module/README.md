# flutter_module —— Flutter 版（跨端模块）

## 项目定位
用 Flutter（Dart + Riverpod + sqflite）还原番茄钟，经 **Flutter Add-to-App** 作为 Gradle 子项目 `:flutter` 被 `native-android` 接入（不是预编译 jar/aar）。
功能范围与 legacy 接近（番茄钟 + 历史 + 4 级导航演示 + MethodChannel），**没有** jetpack 那样的文件/传输模块。

## 技术栈
- 语言：Dart
- UI：Widget 树（声明式，Material3）
- 状态：Riverpod（`Notifier` ≈ ViewModel + `StateFlow`）
- 持久化：sqflite（手写 SQL，**无自动刷新**）
- 通信：MethodChannel（与原生壳交互）

## 功能地图（当前真实状态）
- 首页（`PomodoroApp` / `app.dart`）：`TimerWidget` 番茄钟 + 底部按钮「进入 4 级导航演示」
- 4 级导航演示（命名路由 `/nav_home`）：`HomePage` → `ListPage` → `DetailPage/{itemId}` → `CommentPage/{itemId}`
- 历史记录：`HistoryPage` + `HistoryDb`（sqflite）
- 原生通信：`MethodChannels`（Flutter 调原生保存历史等）

> 对照 legacy：功能模块基本一致（番茄钟 + 历史 + 导航演示），只是用 Flutter 重写。
> 对照 jetpack：没有「文件 / 传输」模块（那些在 `native-android` 原生壳里）。

## 实际文件树（`lib/`）
```
lib/
├── main.dart                         # 入口 runApp(PomodoroApp())
├── app.dart                         # 根组件 MaterialApp + 路由 + 主题
├── models/
│   └── pomodoro_record.dart         # 数据模型
├── timer/
│   ├── timer_widget.dart            # 番茄钟页面（ConsumerWidget）
│   ├── timer_state.dart             # 状态（copyWith）
│   └── timer_notifier.dart          # Riverpod Notifier（≈ ViewModel）
├── channels/
│   └── method_channels.dart         # MethodChannel 定义
├── history/
│   ├── history_page.dart            # 历史列表页
│   └── history_db.dart              # sqflite 封装
└── navigations/
    ├── home/home_page.dart          # 4 级导航：第 1 级
    ├── list/list_page.dart          # 4 级导航：第 2 级
    ├── detail/detail_page.dart      # 4 级导航：第 3 级（带 itemId）
    └── comment/comment_page.dart    # 4 级导航：第 4 级（带 itemId）
```
（另有 `test/widget_test.dart`、`pubspec.yaml`、`analysis_options.yaml`）

## 如何运行
- 独立跑：`cd flutter_module && flutter run`（默认 `main.dart`）
- 作为模块：在 `native-android` 里 Run App，再 `flutter attach`
