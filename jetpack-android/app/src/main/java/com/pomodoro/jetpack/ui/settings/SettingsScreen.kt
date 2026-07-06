package com.pomodoro.jetpack.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pomodoro.jetpack.viewmodel.SettingsViewModel

/**
 * Tab 3: 设置页 — DataStore 数据持久化
 *
 * 覆盖面试题：
 *   题37：DataStore 替代 SharedPreferences
 *   题6：协程异常处理（CoroutineExceptionHandler）
 *
 * 功能：
 *   - 番茄钟时长设置（25/30/45/60 分钟）
 *   - 短休息/长休息时长设置
 *   - 主题模式（系统/浅色/深色）
 *   - 所有设置通过 DataStore 持久化
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val pomodoroDuration by viewModel.pomodoroDuration.collectAsState()
    val shortBreakDuration by viewModel.shortBreakDuration.collectAsState()
    val longBreakDuration by viewModel.longBreakDuration.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("设置") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ===== 番茄钟时长 =====
            SettingSection(title = "番茄钟时长") {
                DurationOption("10 秒", 10, pomodoroDuration, { viewModel.setPomodoroDuration(it) })
                DurationOption("20 秒", 20, pomodoroDuration, { viewModel.setPomodoroDuration(it) })
                DurationOption("30 秒", 30, pomodoroDuration, { viewModel.setPomodoroDuration(it) })
            }

            Divider()

            // ===== 短休息时长 =====
            SettingSection(title = "短休息时长") {
                DurationOption("3 分钟", 3 * 60, shortBreakDuration, { viewModel.setShortBreakDuration(it) })
                DurationOption("5 分钟", 5 * 60, shortBreakDuration, { viewModel.setShortBreakDuration(it) })
                DurationOption("10 分钟", 10 * 60, shortBreakDuration, { viewModel.setShortBreakDuration(it) })
            }

            Divider()

            // ===== 长休息时长 =====
            SettingSection(title = "长休息时长") {
                DurationOption("10 分钟", 10 * 60, longBreakDuration, { viewModel.setLongBreakDuration(it) })
                DurationOption("15 分钟", 15 * 60, longBreakDuration, { viewModel.setLongBreakDuration(it) })
                DurationOption("30 分钟", 30 * 60, longBreakDuration, { viewModel.setLongBreakDuration(it) })
            }

            Divider()

            // ===== 主题模式 =====
            SettingSection(title = "主题模式") {
                ThemeOption("跟随系统", 0, themeMode, { viewModel.setThemeMode(it) })
                ThemeOption("浅色模式", 1, themeMode, { viewModel.setThemeMode(it) })
                ThemeOption("深色模式", 2, themeMode, { viewModel.setThemeMode(it) })
            }
        }
    }
}

@Composable
private fun SettingSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun DurationOption(
    label: String,
    value: Int,
    currentValue: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(value) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentValue == value,
            onClick = { onSelect(value) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun ThemeOption(
    label: String,
    value: Int,
    currentValue: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(value) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = currentValue == value,
            onClick = { onSelect(value) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}