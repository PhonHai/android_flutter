import 'package:flutter/material.dart';

/// 第 4 级：总结页 — 导航链条终点
///
/// 首页(番茄钟) → 列表 → 详情 → 总结页 ← 链条终点
/// 避免「首页番茄钟 → 点4级 → 又是番茄钟」的循环。
///
/// 【返回栈演示】
///   「返回详情页」→ Navigator.pop(context) 回到详情
///   「回到首页」→ Navigator.popUntil(context, (route) => route.isFirst) 到根页面
///   系统返回键 → 逐级 pop
class CommentPage extends StatelessWidget {
  final String itemId;

  const CommentPage({super.key, required this.itemId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('总结 (第 4 级)')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('✅', style: TextStyle(fontSize: 64)),
            const SizedBox(height: 24),

            Text(
              '条目 $itemId 已完成',
              style: Theme.of(context).textTheme.headlineSmall,
            ),
            const SizedBox(height: 16),

            Text(
              '导航演示结束，按返回键可逐级回溯',
              style: TextStyle(color: Colors.grey[600]),
            ),
            const SizedBox(height: 8),

            Text(
              '返回键路径: 总结 → 详情 → 列表 → 首页 → 退出',
              style: TextStyle(fontSize: 12, color: Colors.grey[500]),
            ),

            const SizedBox(height: 48),

            // 「返回详情页」— pop 回到上一层
            ElevatedButton(
              onPressed: () => Navigator.pop(context),
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 48),
              ),
              child: const Text('返回详情页 (pop)', style: TextStyle(fontSize: 16)),
            ),

            const SizedBox(height: 16),

            // 「回到首页」— 跳到根页面（跳过中间层级）
            // popUntil((route) => route.isFirst) = 一直 pop 直到根页面
            // 对标 Compose: navController.popBackStack("home", false)
            // 对标传统 Java: navController.popBackStack(R.id.homeFragment, false)
            OutlinedButton(
              onPressed: () {
                Navigator.popUntil(
                  context,
                  (route) => route.isFirst,
                );
              },
              style: OutlinedButton.styleFrom(
                minimumSize: const Size(double.infinity, 48),
              ),
              child: const Text('回到首页 (pop to root)', style: TextStyle(fontSize: 16)),
            ),

            const SizedBox(height: 16),

            Text(
              '回到首页后可以在番茄钟页面开始计时',
              style: TextStyle(fontSize: 12, color: Colors.grey[500]),
            ),
          ],
        ),
      ),
    );
  }
}
