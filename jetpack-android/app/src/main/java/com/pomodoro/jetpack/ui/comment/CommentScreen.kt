package com.pomodoro.jetpack.ui.comment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第 4 级：总结页 — 导航链条终点
 *
 * ═══════════════════════════════════════════════════════════
 * 【为什么第 4 级是总结页而不是番茄钟？】
 * ═══════════════════════════════════════════════════════════
 *
 * 设计逻辑:
 *   首页 → 列表 → 详情 → 总结页 ← 链条终点
 *   用户从首页出发，经过列表和详情，到达总结页。
 *   点击「为此条目开始番茄钟」→ 回到首页开始计时，
 *   避免「首页番茄钟 → 点 4 级 → 又是番茄钟」的循环。
 *
 * 对标绿联云场景:
 *   首页（App 首页）→ 文件列表（NAS 文件）→ 文件详情（预览）
 *   → 操作完成页（下载完成/分享完成）
 *   每个层级职责清晰，不重复
 *
 * ═══════════════════════════════════════════════════════════
 * 【传统 Java 思维 → Compose 映射】
 * ═══════════════════════════════════════════════════════════
 *
 * 传统 Java:
 *   public class SummaryFragment extends Fragment {
 *     public View onCreateView(...) {
 *       String itemId = getArguments().getString("itemId");
 *       Button btnStart = root.findViewById(R.id.btn_start_pomodoro);
 *       btnStart.setOnClickListener(v -> {
 *         // 回到首页并开始计时
 *         getActivity().finish();
 *       });
 *     }
 *   }
 *
 * Compose:
 *   Button(onClick = { navController.popBackStack("main", false) })
 *   ← popBackStack 到首页 Tab 容器，清空中间所有页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    itemId: String,
    onBack: () -> Unit,
    onBackToHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("总结 (第 4 级)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回详情")
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
            // ===== 完成图标 =====
            Text("✅", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(24.dp))

            // ===== 总结信息 =====
            Text(
                "条目 $itemId 已完成",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "导航演示结束，按返回键可逐级回溯",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "返回键路径: 总结 → 详情 → 列表 → 首页 → 退出",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // ===== 导航演示按钮 =====
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("返回详情页 (popBackStack)", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("回到首页 (pop to root)", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== 想开始计时 → 回到首页 =====
            Text(
                "回到首页后可以开始计时",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
