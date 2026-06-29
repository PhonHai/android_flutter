package com.pomodoro.jetpack.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 第 2 级：列表页 — 展示如何从列表跳详情（带参数）
 *
 * 【传统 Java 思维 → Compose 映射】
 *
 * 传统 Java:
 *   public class ListFragment extends Fragment {
 *     public View onCreateView(...) {
 *       RecyclerView rv = root.findViewById(R.id.recycler);
 *       adapter.setItemClickListener(item -> {
 *         Bundle args = new Bundle();
 *         args.putString("itemId", item.getId());
 *         navController.navigate(R.id.to_detail, args);  ← 带参数跳转
 *       });
 *     }
 *   }
 *
 * Jetpack Compose:
 *   Button(onClick = { onNavigate("detail/$itemId") })
 *   ← 路由字符串里拼接参数: "detail/123"
 *   ← NavHost 的 "detail/{itemId}" 会自动匹配并提取 123
 *
 * 对比 Flutter:
 *   Navigator.push(context, MaterialPageRoute(
 *     builder: (_) => DetailPage(itemId: item.id),
 *   ));
 *   ← Flutter 直接在构造函数传参，不用字符串拼接
 *
 * 【参数说明】
 * @param onNavigate 跳转回调（传完整路由，如 "detail/123"）
 * @param onBack 返回回调（popBackStack 回到首页）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("列表 (第 2 级)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // onBack = { navController.popBackStack() }
                        // ← 系统返回键也会自动调 popBackStack
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { padding ->
        // 模拟 5 条数据
        val items = (1..5).map { it.toString() }

        // LazyColumn = RecyclerView
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items) { itemId ->
                ListItem(
                    headlineContent = { Text("条目 $itemId") },
                    supportingContent = { Text("点击查看详情") },
                    modifier = Modifier.clickable {
                        // ═══════════ 关键：带参数跳转 ═══════════
                        //
                        // "detail/$itemId" = 字符串模板拼接
                        // 如果 itemId = "1"，路由就是 "detail/1"
                        // NavHost 的 "detail/{itemId}" 会匹配并提取 itemId="1"
                        //
                        // 对标传统 Java:
                        //   Bundle args = new Bundle();
                        //   args.putString("itemId", itemId);
                        //   navController.navigate(R.id.to_detail, args);
                        onNavigate("detail/$itemId")
                    }
                )
                HorizontalDivider()
            }
        }
    }
}
