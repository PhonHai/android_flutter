import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'app.dart';

/// ═══════════════════════════════════════════════════════════
/// Flutter 模块入口 — 原生 Android 壳从这里加载整个 Flutter UI
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
/// 这个文件 ≈ Android 的 Application.onCreate() + Activity.setContentView()
///
/// | Flutter               | Android 对应              | 说明                     |
/// |-----------------------|--------------------------|--------------------------|
/// | `main()`              | `Application.onCreate()` | 应用入口，只执行一次       |
/// | `runApp(widget)`      | `setContentView(layout)` | 把 Widget 树设为根 UI      |
/// | `ProviderScope`       | `Hilt @HiltAndroidApp`   | DI 容器根节点             |
/// | `WidgetsFlutterBinding`| `Activity.onCreate()`   | 初始化引擎和平台通道       |
///
/// ===== 加载流程 =====
/// 原生 Android 壳 (PomodoroApplication)
///   → 预热 FlutterEngine
///     → 执行 Dart 入口 (main())
///       → ProviderScope (DI 容器)
///         → PomodoroApp (MaterialApp)
///           → TimerWidget (首页)
///
/// ===== 面试话术 =====
/// "main() 是 Flutter 模块的入口，原生壳通过 FlutterEngine 预热时执行。
///  ProviderScope 对标 Android Hilt DI 容器，为整个 Widget 树提供依赖注入。
///  WidgetsFlutterBinding.ensureInitialized() 初始化平台通道，
///  让 Flutter 可以调用 MethodChannel 和原生通信。"
void main() {
  // ===== 初始化 Flutter 引擎绑定 =====
  // 必须在使用 runApp() 前调用
  // 确保平台通道（MethodChannel/EventChannel）就绪
  // ≈ Android Activity.onCreate() 中 setContentView 前的初始化
  WidgetsFlutterBinding.ensureInitialized();

  // ===== 挂载根 Widget 树 =====
  // runApp() ≈ Android 的 setContentView()
  // ProviderScope = Riverpod 的 DI 根容器
  //   对标 Android Hilt 的 @HiltAndroidApp 注解
  //   所有 Provider/Notifier 必须在 ProviderScope 子树内才能使用
  runApp(
    const ProviderScope(
      // PomodoroApp = Flutter 模块的根 Widget
      // 对标 Android 的 Application 启动的第一个 Activity
      child: PomodoroApp(),
    ),
  );
}
