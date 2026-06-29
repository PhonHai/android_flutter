package com.pomodoro.app.ui.comment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第 5 级：总结页 — 导航链条终点
 *
 * 对标 legacy-android 的 CommentFragment
 * 对标 jetpack-android 的 CommentScreen
 */
@Composable
fun CommentScreen(
    itemId: String,
    onBack: () -> Unit,
    onBackToHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("✅", fontSize = 64.sp)
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "条目 $itemId 已完成",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "导航演示结束，按返回键可逐级回溯",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "返回键路径: 总结 → 详情 → 列表 → 首页 → 退出",
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(48.dp))

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

        Text(
            "回到首页后可以启动 Flutter 番茄钟",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
