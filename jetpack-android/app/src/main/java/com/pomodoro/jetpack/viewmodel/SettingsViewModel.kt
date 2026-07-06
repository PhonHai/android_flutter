package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.data.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页 ViewModel — DataStore 的"翻译官"
 *
 * ═══════════════════════════════════════════════════════════
 * 【这个 ViewModel 的角色】
 * ═══════════════════════════════════════════════════════════
 *
 * SettingsDataStore 暴露的是 Flow<T>，但 Compose UI 需要 StateFlow<T>。
 * SettingsViewModel 就是"翻译官"：把 Flow 转成 StateFlow，同时把 suspend 写操作
 * 包装成普通函数，让 UI 层不需要关心协程。
 *
 *   数据层（SettingsDataStore）     ViewModel 层（SettingsViewModel）     UI 层（SettingsScreen）
 *   ┌─────────────────────┐         ┌──────────────────────────┐         ┌─────────────────────┐
 *   │ Flow<Int>            │  ──→   │ stateIn() → StateFlow<Int>│  ──→   │ collectAsState()     │
 *   │ suspend fun setXxx() │  ←──   │ launch { setXxx() }       │  ←──   │ viewModel.setXxx()   │
 *   └─────────────────────┘         └──────────────────────────┘         └─────────────────────┘
 *       磁盘读写 + Flow                 转换 + 协程包装                    声明式 UI
 *
 * ═══════════════════════════════════════════════════════════
 * 【stateIn()：把冷流 Flow 转成热流 StateFlow】
 * ═══════════════════════════════════════════════════════════
 *
 * 语法：
 *   dataStore.pomodoroDuration          ← Flow<Int>（冷流，没人订阅就不工作）
 *       .stateIn(                        ← 转成 StateFlow<Int>（热流，始终持有最新值）
 *           viewModelScope,              ← 在这个作用域内运行
 *           SharingStarted.WhileSubscribed(5000),  ← 共享策略
 *           10                           ← 初始值（StateFlow 必须有初始值）
 *       )
 *
 * SharingStarted.WhileSubscribed(5000) 的含义：
 *   - "WhileSubscribed"：当有订阅者（UI 在屏幕上）时，上游 Flow 才工作
 *   - 5000 毫秒 = 5 秒：最后一个订阅者离开后，再等 5 秒才停止上游
 *   - 为什么是 5 秒？防止屏幕旋转时短暂取消订阅又立即重新订阅，停止再重启有开销
 *
 * 为什么用 stateIn 而不是 collectAsState？
 *   - collectAsState 在 Composable 里调用，每次重组都会重新 collect
 *   - stateIn 在 ViewModel 里调用一次，所有 Composable 共享同一个 StateFlow
 *   - 多个 Composable 读取同一个 StateFlow 时，只有一个上游订阅，省资源
 *
 * ═══════════════════════════════════════════════════════════
 * 【写操作：为什么用 viewModelScope.launch 包装？】
 * ═══════════════════════════════════════════════════════════
 *
 * SettingsDataStore 的 setXxx() 是 suspend 函数（因为要写磁盘）。
 * 但 UI 层的 onClick 是普通函数，不能直接调 suspend 函数。
 *
 * 所以 ViewModel 包装一层：
 *   fun setPomodoroDuration(seconds: Int) {           ← 普通函数，UI 可直接调用
 *       viewModelScope.launch {                        ← 启动协程
 *           dataStore.setPomodoroDuration(seconds)     ← suspend 写磁盘
 *       }
 *   }
 *
 * 为什么用 viewModelScope 而不是 GlobalScope？
 *   viewModelScope 在 ViewModel.onCleared() 时自动取消所有协程。
 *   如果用户点了"写"然后立刻退出页面，写操作会被取消，不会泄漏。
 *   GlobalScope 会一直运行到进程结束，容易泄漏。
 *
 * ═══════════════════════════════════════════════════════════
 * 【完整数据流：用户改设置 → 磁盘 → UI 自动更新】
 * ═══════════════════════════════════════════════════════════
 *
 * 写路径（用户点击）：
 *   SettingsScreen: viewModel.setPomodoroDuration(20)
 *     → SettingsViewModel: viewModelScope.launch { dataStore.setPomodoroDuration(20) }
 *       → SettingsDataStore: context.dataStore.edit { prefs -> prefs[KEY] = 20 }
 *         → 写入磁盘文件
 *
 * 读路径（自动更新）：
 *   磁盘文件变化 → dataStore.data Flow 发射新 Preferences
 *     → SettingsDataStore.pomodoroDuration Flow 的 map 拿到新值 → 发射 20
 *       → SettingsViewModel 的 stateIn 收到 20 → StateFlow 更新
 *         → SettingsScreen 的 collectAsState 收到 20 → RadioButton 重绘
 *
 * 全程不需要手动 notifyDataSetChanged()，Flow 自动串联每一步。
 *
 * ═══════════════════════════════════════════════════════════
 * 【注入链路】
 * ═══════════════════════════════════════════════════════════
 *
 *   Hilt AppModule → @ApplicationContext
 *         ↓
 *   SettingsDataStore (@Inject constructor + @Singleton)
 *         ↓
 *   SettingsViewModel (@HiltViewModel + @Inject constructor)
 *         ↓
 *   SettingsScreen (hiltViewModel())
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore  // ← Hilt 自动注入
) : ViewModel() {

    // ═══════════════════════════════════════════════════════════
    // 读：Flow → StateFlow，UI 直接 collectAsState()
    // ═══════════════════════════════════════════════════════════
    //
    // 每个属性都是 StateFlow<Int>，UI 用 collectAsState() 订阅即可。
    // 当 DataStore 里的值变化时，StateFlow 自动更新，UI 自动重组。
    //
    // 初始值（stateIn 的第三个参数）：
    //   - pomodoroDuration: 10（10 秒，演示用）
    //   - shortBreakDuration: 5 * 60（5 分钟）
    //   - longBreakDuration: 15 * 60（15 分钟）
    //   - themeMode: 0（跟随系统）
    //
    // 初始值只在 DataStore 还没读到数据时使用（首次启动）。
    // 一旦 DataStore 读到磁盘数据，就会用磁盘数据替换初始值。

    val pomodoroDuration: StateFlow<Int> = dataStore.pomodoroDuration
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 10)

    val shortBreakDuration: StateFlow<Int> = dataStore.shortBreakDuration
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 5 * 60)

    val longBreakDuration: StateFlow<Int> = dataStore.longBreakDuration
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 15 * 60)

    val themeMode: StateFlow<Int> = dataStore.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // ═══════════════════════════════════════════════════════════
    // 写：普通函数包装 suspend 写操作
    // ═══════════════════════════════════════════════════════════
    //
    // 每个 setXxx() 都是普通函数，UI 层直接调用，不需要关心协程。
    // 内部用 viewModelScope.launch 启动协程，ViewModel 销毁时自动取消。
    //
    // 为什么不用 runBlocking？
    //   runBlocking 会阻塞当前线程，如果 UI 线程调 runBlocking 会 ANR。
    //   viewModelScope.launch 是异步的，不阻塞调用方。

    fun setPomodoroDuration(seconds: Int) {
        viewModelScope.launch { dataStore.setPomodoroDuration(seconds) }
    }

    fun setShortBreakDuration(seconds: Int) {
        viewModelScope.launch { dataStore.setShortBreakDuration(seconds) }
    }

    fun setLongBreakDuration(seconds: Int) {
        viewModelScope.launch { dataStore.setLongBreakDuration(seconds) }
    }

    fun setThemeMode(mode: Int) {
        viewModelScope.launch { dataStore.setThemeMode(mode) }
    }
}