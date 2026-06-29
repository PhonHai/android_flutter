package com.pomodoro.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 第 2 级：导航演示首页
 *
 * 对标 legacy-android 的 HomeFragment
 * 对标 jetpack-android 的 HomeScreen
 *
 * 原生壳版本不连 Flutter，纯 Compose 演示 4 级导航链路。
 */
@Composable
fun HomeScreen(
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
            text = "导航演示首页 (第 2 级)",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "点击下方按钮进入下一级",
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onNavigate("list") },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("进入列表 (第 3 级)", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("返回首页 (popBackStack)", fontSize = 16.sp)
        }
    }
}
