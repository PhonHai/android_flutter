package com.pomodoro.app.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第 4 级：详情页
 */
@Composable
fun DetailScreen(
    itemId: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "详情 (第 4 级)",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "当前条目: $itemId",
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onNavigate("comment/$itemId") },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("进入总结 (第 5 级)", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("返回列表", fontSize = 16.sp)
        }
    }
}
