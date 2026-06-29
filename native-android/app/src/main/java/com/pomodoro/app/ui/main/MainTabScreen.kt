package com.pomodoro.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 首页底部 4 Tab 容器 — 原生壳版本
 *
 * 对标 legacy-android 的 TabContainerFragment
 * 对标 jetpack-android 的 MainTabScreen
 *
 * 区别：
 *   Tab 1 不包含自己的计时器，而是通过「启动 Flutter 番茄钟」按钮
 *   跳转 FlutterContainerActivity 加载 Flutter 模块的计时器。
 *   同时提供「进入 4 级导航演示」入口，演示原生 Compose 侧的多级导航。
 */
@Composable
fun MainTabScreen(
    onStartFlutter: () -> Unit,
    onEnterNavDemo: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "番茄钟") },
                    label = { Text("番茄钟") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Star, contentDescription = "统计") },
                    label = { Text("统计") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "设置") },
                    label = { Text("设置") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Filled.Info, contentDescription = "关于") },
                    label = { Text("关于") }
                )
            }
        }
    ) { padding ->
        when (selectedTab) {
            // Tab 1: Flutter 番茄钟入口 + 4 级导航演示入口
            0 -> FlutterTabContent(
                onStartFlutter = onStartFlutter,
                onEnterNavDemo = onEnterNavDemo,
                modifier = Modifier.padding(padding)
            )
            1 -> PlaceholderContent("统计页", modifier = Modifier.padding(padding))
            2 -> PlaceholderContent("设置页", modifier = Modifier.padding(padding))
            3 -> PlaceholderContent("关于页", modifier = Modifier.padding(padding))
        }
    }
}

/**
 * Tab 1 内容：Flutter 番茄钟入口 + 导航演示入口
 *
 * 对标 legacy-android 的 TimerFragment（番茄钟 Tab）
 * 对标 jetpack-android 的 TimerScreen（番茄钟 Tab）
 *
 * 区别：原生壳没有自己的计时器，点击「启动 Flutter 番茄钟」
 * 跳转 FlutterContainerActivity，由 Flutter 模块渲染计时器。
 */
@Composable
private fun FlutterTabContent(
    onStartFlutter: () -> Unit,
    onEnterNavDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 标题
        Text(
            text = "番茄钟",
            fontSize = 32.sp,
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "原生 + Flutter 混合架构",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        // ===== 启动 Flutter 番茄钟 =====
        Button(
            onClick = onStartFlutter,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("启动 Flutter 番茄钟", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== 4 级导航演示入口 =====
        OutlinedButton(
            onClick = onEnterNavDemo,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("进入 4 级导航演示", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 架构说明卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "混合架构示意",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = """
                        • 本页面: 原生 Kotlin + Compose
                        • 番茄计时: Flutter (AAR 嵌入)
                        • 导航演示: 原生 Compose NavHost
                        • 通信层: MethodChannel
                    """.trimIndent(),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

/**
 * 空白占位 Tab 内容
 */
@Composable
private fun PlaceholderContent(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "功能开发中...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
