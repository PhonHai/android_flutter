package com.pomodoro.jetpack.ui.detail

import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第 3 级：详情页 — 接收参数 + 跳转评论页
 *
 * 【传统 Java 思维 → Compose 映射】
 *
 * 传统 Java:
 *   public class DetailFragment extends Fragment {
 *     public View onCreateView(...) {
 *       String itemId = getArguments().getString("itemId");  ← 取参数
 *       Button btnComment = root.findViewById(R.id.btn_comment);
 *       btnComment.setOnClickListener(v -> {
 *         Bundle args = new Bundle();
 *         args.putString("itemId", itemId);  ← 传给下一级
 *         navController.navigate(R.id.to_comment, args);
 *       });
 *     }
 *   }
 *
 * Jetpack Compose:
 *   fun DetailScreen(itemId: String, ...) {
 *     // itemId 已经是参数了，直接用
 *     Button(onClick = { onNavigate("comment/$itemId") })
 *   }
 *
 * 对比 Flutter:
 *   class DetailPage extends StatelessWidget {
 *     final String itemId;
 *     DetailPage({required this.itemId});  ← 构造函数传参
 *     build(context) {
 *       ElevatedButton(onPressed: () {
 *         Navigator.push(context, MaterialPageRoute(
 *           builder: (_) => CommentPage(itemId: itemId),
 *         ));
 *       });
 *     }
 *   }
 *
 * 【参数说明】
 * @param itemId 从路由 "detail/{itemId}" 提取的参数
 * @param onNavigate 跳转回调（传 "comment/{itemId}"）
 * @param onBack 返回回调（popBackStack 回到列表）
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    itemId: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("详情 (第 3 级)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ═══════════ 显示接收到的参数 ═══════════
            //
            // itemId 是从路由 "detail/{itemId}" 提取的
            // 比如从列表点"条目 1"进来，itemId = "1"
            Text("条目 ID: $itemId", fontSize = 28.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("这是详情页内容...", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(48.dp))

            // ═══════════ 按钮：跳转到总结页（第 4 级 — 链条终点）═══════════
            Button(
                onClick = { onNavigate("comment/$itemId") },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("完成总结 (第 4 级)",
                    fontSize = 18.sp)
            }
        }
    }
}
