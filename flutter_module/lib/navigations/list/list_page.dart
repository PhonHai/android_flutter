import 'package:flutter/material.dart';
import '../detail/detail_page.dart';

/// 第 2 级：列表页 — Navigator.push 带参数跳转
///
/// 【三套方案传参对比】
///
/// 传统 Java (Bundle):
///   Bundle args = new Bundle();
///   args.putString("itemId", itemId);
///   navController.navigate(R.id.action_list_to_detail, args);
///
/// Jetpack Compose (字符串路由):
///   onNavigate("detail/$itemId")
///   ← NavHost 从路由字符串提取参数
///
/// Flutter (构造函数传参):
///   Navigator.push(context, MaterialPageRoute(
///     builder: (context) => DetailPage(itemId: itemId),
///   ));
///   ← 直接当构造函数参数传，最直观
class ListPage extends StatelessWidget {
  const ListPage({super.key});

  @override
  Widget build(BuildContext context) {
    // 模拟 5 条数据
    final items = List.generate(5, (i) => '${i + 1}');

    // ListView.builder = RecyclerView / LazyColumn
    return Scaffold(
      appBar: AppBar(title: const Text('列表 (第 2 级)')),
      body: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: items.length,
        itemBuilder: (context, index) {
          final itemId = items[index];
          return Card(
            child: ListTile(
              title: Text('条目 $itemId'),
              subtitle: const Text('点击查看详情'),
              onTap: () {
                // ═══════════ 关键：构造函数传参跳转 ═══════════
                //
                // 对比 Compose: navController.navigate("detail/$itemId")
                // 对比传统 Java: navController.navigate(R.id.xxx, bundleOf("itemId", itemId))
                //
                // Flutter 不需要 Bundle 也不需要字符串拼接
                // 直接把 itemId 当构造函数参数传
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => DetailPage(itemId: itemId),
                  ),
                );
              },
            ),
          );
        },
      ),
    );
  }
}
