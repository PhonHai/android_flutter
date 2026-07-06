package com.pomodoro.jetpack.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.pomodoro.jetpack.ui.settings.SettingsScreen
import com.pomodoro.jetpack.ui.timer.TimerScreen
import com.pomodoro.jetpack.ui.transfer.TransferListScreen
import com.pomodoro.jetpack.viewmodel.TimerViewModel

/**
 * 主界面 — 底部导航栏 + 4 个 Tab
 *
 * Tab 1: 番茄钟（TimerScreen）— 增强：SavedStateHandle + Channel
 * Tab 2: NAS 文件管理（FileListScreen）— 新增：Retrofit + Repository + sealed class Result
 * Tab 3: 设置（SettingsScreen）— 新增：DataStore
 * Tab 4: 传输列表（TransferListScreen）— 新增：Flow 进度
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabScreen(
    navController: NavController,
    onExitApp: () -> Unit
) {
    // 使用 Hilt 注入的 ViewModel（不再手动传 DAO）
    val timerViewModel: TimerViewModel = hiltViewModel()

    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf(
        TabItem("番茄钟", Icons.Filled.Timer),
        TabItem("文件", Icons.Filled.Folder),
        TabItem("设置", Icons.Filled.Settings),
        TabItem("传输", Icons.Filled.CloudUpload),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> TimerScreen(
                viewModel = timerViewModel,
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
            1 -> FileListScreen(
                modifier = Modifier.padding(paddingValues)
            )
            2 -> SettingsScreen(
                modifier = Modifier.padding(paddingValues)
            )
            3 -> TransferListScreen(
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

private data class TabItem(val label: String, val icon: ImageVector)