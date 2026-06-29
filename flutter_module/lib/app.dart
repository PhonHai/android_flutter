import 'package:flutter/material.dart';
import 'timer/timer_widget.dart';
import 'navigations/home/home_page.dart';

/// ═══════════════════════════════════════════════════════════
/// Flutter 模块根组件 — MaterialApp + 首页
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
/// 这个类 ≈ Android 的 Application + 启动 Activity 的组合
///
/// | Flutter               | Android 对应               | 说明                     |
/// |-----------------------|---------------------------|--------------------------|
/// | `MaterialApp`         | `@style/Theme.Material3`  | 全局主题、路由、导航       |
/// | `StatelessWidget`     | 无状态的 Composable       | 不需要维护内部状态         |
/// | `Scaffold`            | `CoordinatorLayout`       | 页面脚手架（AppBar+Body） |
/// | `AppBar`              | `Toolbar` / `ActionBar`   | 顶部导航栏               |
/// | `SafeArea`            | `fitsSystemWindows=true`  | 避开刘海屏/系统栏        |
/// | `Padding`             | `android:padding`          | 内边距                   |
///
/// ===== 为什么这里用 StatelessWidget 而不是 StatefulWidget？ =====
/// 这个 Widget 只是配置（主题、首页路由），运行时不需要变。
/// 真正的状态在 TimerNotifier (Riverpod ViewModel) 里管理。
/// ≈ Android 中 Activity 不直接持有业务状态，通过 ViewModel 管理
///
/// ===== 面试话术 =====
/// "PomodoroApp 是 Flutter 模块的根组件，设置 Material3 主题和番茄红品牌色。
///  首页直接用 TimerWidget 而不用 Navigator 路由表，
///  因为导航由原生壳 Activity 管理（FlutterContainerActivity 加载 Flutter）。
///  对标绿联云中 Flutter 模块只负责单一功能页面的方案。"
class PomodoroApp extends StatelessWidget {

  // const 构造函数 = 编译时常量，不依赖外部状态
  // super.key 传给父类 Widget（Flutter 用 key 做 Widget 身份比对）
  const PomodoroApp({super.key});

  /// Widget.build() ≈ Android:
  ///   - Composable 函数体
  ///   - RecyclerView.Adapter.onCreateViewHolder() + onBindViewHolder()
  /// 每次 UI 需要更新时，Flutter 会重新调用 build() 生成新的 Widget 树
  @override
  Widget build(BuildContext context) {
    // BuildContext = 当前 Widget 在 Widget 树中的位置
    // ≈ Android Context（获取主题、资源、导航等）

    return MaterialApp(
      title: '番茄钟',
      debugShowCheckedModeBanner: false,

      // ===== 路由表（导航演示用）=====
      // routes = 命名路由映射，对标 Compose 的 NavHost composable() 注册
      routes: {
        '/nav_home': (context) => const HomePage(),
      },

      // ===== 主题设置 =====
      // ThemeData ≈ android:theme="@style/AppTheme"
      theme: ThemeData(
        // ColorScheme.fromSeed = Material3 动态取色
        // 从种子色自动生成完整的色彩方案（primary/secondary/surface/error...）
        // 0xFFE53935 = 番茄红（Material Red 600）
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFFE53935),  // 番茄红
        ),
        useMaterial3: true,  // 启用 Material3 设计语言（Material You）
      ),

      // ===== 首页（非路由模式） =====
      // home: 直接指定首页 Widget（不经过路由表）
      // 注意: Flutter 模块内部没有 Navigator.push 跳转
      //       页面级导航走原生 Activity（FlutterContainerActivity → PomodoroListActivity）
      home: Scaffold(
        appBar: AppBar(
          title: const Text('番茄钟'),
          centerTitle: true,  // 标题居中（iOS 风格）
        ),
        body: const SafeArea(
          // SafeArea = 内容区域自动避开系统 UI（状态栏/导航栏/刘海屏）
          // ≈ Android 中 android:fitsSystemWindows="true"
          child: Padding(
            // EdgeInsets.symmetric(horizontal: 24) = 左右各 24dp 内边距
            padding: EdgeInsets.symmetric(horizontal: 24),
            // TimerWidget = 番茄钟计时页面（ConsumerWidget）
            child: TimerWidget(),
          ),
        ),
        // ═══════════ 底部按钮：进入导航演示 ═══════════
        // PersistentFooterButton = 底部固定的按钮（不随内容滚动）
        // 点击 → Navigator.pushNamed("/nav_home") = 进入 4 级导航演示
        // 演示完按返回键 → Navigator.pop → 回到计时页面
        //
        // 【关键】必须用 Builder 包裹！
        // 因为这里的 context 是 PomodoroApp.build() 的 context，
        // 它在 MaterialApp 外面，拿不到内部的 Navigator。
        // Builder 会创建一个新的 context，这个 context 在 MaterialApp 里面，
        // 所以 Navigator.pushNamed 才能生效。
        // 对标传统 Java: Activity 的 Context 才能 startActivity，Application Context 不行
        bottomNavigationBar: Builder(
          builder: (innerContext) => Padding(
            padding: const EdgeInsets.all(16),
            child: SizedBox(
              height: 48,
              child: OutlinedButton.icon(
                onPressed: () {
                  Navigator.pushNamed(innerContext, '/nav_home');
                },
                icon: const Icon(Icons.explore),
                label: const Text('查看 4 级导航演示'),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
