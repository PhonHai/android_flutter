package com.pomodoro.jetpack.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavController
import com.pomodoro.jetpack.ui.timer.TimerScreen
import com.pomodoro.jetpack.viewmodel.TimerViewModel

/**
 * 首页底部 4 Tab 容器 — Jetpack Compose 版
 *
 * 【对标 legacy-android 的 TabContainerFragment】
 *
 * legacy-android 用 FragmentTransaction hide/show 切换 4 个 Fragment。
 * Compose 版没有 Fragment，直接用 mutableIntStateOf 记录当前 Tab，
 * when 分支切不同的 Composable 内容。
 *
 * 【为什么不用 NavHost 管理 Tab？】
 * Tab 切换是「状态切换」，不是「页面导航」。
 * 用 NavHost 管理 Tab 会导致每个 Tab 都有独立的返回栈，体验差。
 * 用简单状态变量切换，四个 Tab 共享一个父页面 ("main") 的生命周期。
 *
 * 【四级导航如何实现？】
 * TabContainer 是 NavHost 的 "main" 路由。
 * TimerScreen 里点击「进入 4 级导航演示」→ navController.navigate("home")
 * → NavHost 用 HomeScreen 替换整个 MainTabScreen（全屏进入四级导航）。
 * 返回时 popBackStack("main", false) → 恢复 MainTabScreen。
 *
 * ═══════════════════════════════════════════════════════════
 * 【传统 Java 思维 → Compose 映射】
 * ═══════════════════════════════════════════════════════════
 *
 * 传统:
 *   TabContainerFragment
 *     ├── ChildFragmentManager
 *     │     ├── TimerFragment (show/hide)
 *     │     ├── StatsFragment (show/hide)
 *     │     ├── SettingsFragment (show/hide)
 *     │     └── AboutFragment (show/hide)
 *     └── LinearLayout (底部 4 个 TextView Tab 按钮)
 *
 * Compose:
 *   MainTabScreen
 *     ├── var selectedTab by remember { mutableIntStateOf(0) }
 *     └── Scaffold(bottomBar = NavigationBar { ... })
 *           └── when(selectedTab) {
 *                 0 -> TimerTab（含 TimerScreen）
 *                 1 -> Placeholder("统计")
 *                 2 -> Placeholder("设置")
 *                 3 -> Placeholder("关于")
 *               }
 *
 * 对比 Flutter:
 *   Scaffold(bottomNavigationBar: BottomNavigationBar(
 *     onTap: (index) => setState(() => _currentIndex = index),
 *     items: [BottomNavigationBarItem(...), ...]
 *   ))
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabScreen(
    navController: NavController,
    timerViewModel: TimerViewModel
) {
    // ═══════════════════════════════════════════════════════════
    // Compose ✨方法！点击筛选按钮后重新请求数据
    // ═══════════════════════════════════════════════════════════
    //
    // mutableIntStateOf = Compose 的可观察状态（只能存 Int）
    // selectedTab 变了 → Compose 自动重组 → when 分支自动切页面
    //
    // 对标传统 Java:
    //   private int currentTabIndex = 0;
    //   currentTabIndex = 1;  // 手动切换
    //   FragmentTransaction.show(statsFragment).hide(timerFragment).commit();
    //
    // 对比 legacy 的 FragmentTransaction:
    //   Compose 不需要 hide/show，只要改 selectedTab 的值
    //   系统自动重组 when 分支，旧 Composable 被移除、新 Composable 被插入
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        // ═══════════ 底部导航栏 ═══════════
        //
        // NavigationBar = Material3 底部导航栏
        // 对标传统: LinearLayout (horizontal) + 4 个 TextView
        // 对标 Material2: BottomNavigation (已废弃)
        bottomBar = {
            NavigationBar {
                // Tab 1: 番茄钟
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "番茄钟") },
                    label = { Text("番茄钟") }
                )
                // Tab 2: 统计
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Filled.Star, contentDescription = "统计") },
                    label = { Text("统计") }
                )
                // Tab 3: 设置
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "设置") },
                    label = { Text("设置") }
                )
                // Tab 4: 关于
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Filled.Info, contentDescription = "关于") },
                    label = { Text("关于") }
                )
            }
        }
    ) { padding ->
        // ═══════════ Tab 内容 ═══════════
        //
        // Scaffold 的 padding 包含底部导航栏和顶部状态栏的高度
        // 对标传统 Java: 不需要手动 padding，因为 Fragment 容器已经有 insets
        //
        // .padding(padding) = 把内容推到导航栏上方
        // 对标 legacy: activity_main.xml 的 android:fitsSystemWindows="true"
        when (selectedTab) {
            // Tab 1: 番茄钟（复用 TimerScreen）
            0 -> TimerScreen(
                navController = navController,
                viewModel = timerViewModel,
                modifier = Modifier.padding(padding)
            )

            // Tab 2-4: 空白占位
            1 -> PlaceholderContent("统计页", modifier = Modifier.padding(padding))
            2 -> PlaceholderContent("设置页", modifier = Modifier.padding(padding))
            3 -> PlaceholderContent("关于页", modifier = Modifier.padding(padding))
        }
    }
}

/**
 * 空白占位 Tab 内容
 *
 * 对标 legacy-android 的 StatsFragment / SettingsFragment / AboutFragment
 * 实际项目中这里放各自的功能页面。
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
