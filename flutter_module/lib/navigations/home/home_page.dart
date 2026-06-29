import 'package:flutter/material.dart';
import '../list/list_page.dart';

/// ═══════════════════════════════════════════════════════════
/// 第 1 级：首页 — Flutter Navigator.push 入口
/// ═══════════════════════════════════════════════════════════
///
/// 【传统 Java 思维 → Flutter Navigator 映射】
///
/// 传统 Java (Navigation Component):
///   btnList.setOnClickListener(v -> {
///     navController.navigate(R.id.action_home_to_list);
///   });
///
/// Jetpack Compose (NavHost):
///   Button(onClick = { onNavigate("list") }) { Text("进入列表") }
///   ← AppNavHost 里 navController.navigate("list")
///
/// Flutter (Navigator.push):
///   ElevatedButton(
///     onPressed: () {
///       Navigator.push(
///         context,
///         MaterialPageRoute(builder: (context) => ListPage()),
///       );
///     },
///     child: Text('进入列表'),
///   )
///
/// 【Flutter Navigator 核心概念】
///
/// Navigator = 路由管理器（全局单例，通过 context 获取）
///   - Navigator.push(context, route) = 压栈新页面
///   - Navigator.pop(context) = 弹出当前页面
///   - 返回键自动调 pop（和 Android NavController 一样）
///
/// MaterialPageRoute = Material 风格的路由（带过渡动画）
///   - builder: (context) => XxxPage() = 构建目标页面
///   - 自动处理 Android 的页面切换动画（从右滑入）
///
/// 对比三套方案:
///   传统 Java: Bundle 传参 → navController.navigate(R.id.xxx, args)
///   Compose:   字符串路由 → navController.navigate("detail/$itemId")
///   Flutter:   构造函数传参 → Navigator.push(context, MaterialPageRoute(
///                             builder: (_) => DetailPage(itemId: itemId),
///                           ))
///   ← Flutter 最直接，把参数当构造函数参数传
class HomePage extends StatelessWidget {
  const HomePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('首页 (第 1 级)')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text(
              '4 级导航演示',
              style: TextStyle(fontSize: 24),
            ),
            const SizedBox(height: 8),
            Text(
              '点击下方按钮进入下一级',
              style: TextStyle(color: Colors.grey[600]),
            ),
            const SizedBox(height: 48),

            // ═══════════ 按钮：跳转到列表页 ═══════════
            //
            // Navigator.push(context, route) = 压栈新页面
            // MaterialPageRoute = Material 风格路由（带过渡动画）
            // builder: (context) => ListPage() = 构建目标页面
            //
            // 对比 Compose: navController.navigate("list")
            // 对比传统 Java: navController.navigate(R.id.action_home_to_list)
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => const ListPage(),
                  ),
                );
              },
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 56),
              ),
              child: const Text('进入列表 (第 2 级)', style: TextStyle(fontSize: 18)),
            ),
          ],
        ),
      ),
    );
  }
}
