import 'package:flutter/material.dart';
import '../comment/comment_page.dart';

/// 第 3 级：详情页 — 接收参数 + 跳转评论页
///
/// 【Flutter 参数传递】
///
/// Flutter 用构造函数传参（自带类型检查，Bundle 做不到）:
///   class DetailPage extends StatelessWidget {
///     final String itemId;  ← 不可变字段，类型安全
///     const DetailPage({required this.itemId});
///   }
///
/// 传统 Java 用 Bundle（无类型检查，运行时取）:
///   String itemId = getArguments().getString("itemId");
///
/// Compose 用路由字符串（也是运行时解析）:
///   val itemId = backStackEntry.arguments?.getString("itemId")
///
/// Flutter 的构造函数传参是最安全的方案。
class DetailPage extends StatelessWidget {
  final String itemId;

  const DetailPage({super.key, required this.itemId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('详情 (第 3 级)')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              '条目 ID: $itemId',
              style: const TextStyle(fontSize: 28),
            ),
            const SizedBox(height: 16),
            Text(
              '这是详情页内容...',
              style: TextStyle(color: Colors.grey[600]),
            ),
            const SizedBox(height: 48),

            // ═══════════ 跳转评论页 ═══════════
            //
            // 把 itemId 继续传给评论页
            // 和 ListPage → DetailPage 一样的模式
            ElevatedButton(
              onPressed: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => CommentPage(itemId: itemId),
                  ),
                );
              },
              style: ElevatedButton.styleFrom(
                minimumSize: const Size(double.infinity, 56),
              ),
              child: const Text('完成总结 (第 4 级)', style: TextStyle(fontSize: 18)),
            ),
          ],
        ),
      ),
    );
  }
}
