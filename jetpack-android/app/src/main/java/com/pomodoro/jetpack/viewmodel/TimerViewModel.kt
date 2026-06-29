package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.data.PomodoroDao
import com.pomodoro.jetpack.model.PomodoroEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 番茄钟 ViewModel — MVVM 架构核心
 *
 * ═══════════════════════════════════════════════════════════
 * 【🔥 传统 Java 思维 → Jetpack MVVM 完整映射 🔥】
 * ═══════════════════════════════════════════════════════════
 *
 * 这是你最需要理解的一个文件。对照看：
 *
 * ┌──────────────────────────┬──────────────────────────────────────────┐
 * │ 传统 Java (MainActivity) │ Jetpack MVVM (TimerViewModel)            │
 * ├──────────────────────────┼──────────────────────────────────────────┤
 * │ int remainingSeconds     │ TimerUiState.remainingSeconds            │
 * │ boolean isRunning        │ TimerUiState.status                      │
 * │ 成员变量散落在 Activity   │ 全部封装在 UiState 里                     │
 * ├──────────────────────────┼──────────────────────────────────────────┤
 * │ tvTime.setText("25:00")  │ _uiState.value = newState                │
 * │ 手动更新 UI              │ StateFlow 自动通知 UI 更新                │
 * ├──────────────────────────┼──────────────────────────────────────────┤
 * │ Handler.postDelayed()    │ viewModelScope.launch { delay(1000) }    │
 * │ Runnable 循环            │ Coroutines 协程循环                       │
 * ├──────────────────────────┼──────────────────────────────────────────┤
 * │ handler.removeCallbacks  │ timerJob?.cancel()                       │
 * │ 在 onDestroy 里调        │ 在 onCleared 里调（自动）                 │
 * ├──────────────────────────┼──────────────────────────────────────────┤
 * │ DatabaseHelper.insert()  │ dao.insert() (suspend, IO线程)           │
 * │ 直接在主线程调           │ 协程自动切线程                            │
 * └──────────────────────────┴──────────────────────────────────────────┘
 *
 * 对比 Flutter 版（timer_notifier.dart）：
 *   TimerNotifier extends Notifier<TimerState>
 *   - state 对应 _uiState.value
 *   - ref.onDispose 对应 viewModel.onCleared
 *   - Timer.periodic 对应 launch { while + delay }
 *   - copyWith 概念完全一样
 *
 * ═══════════════════════════════════════════════════════════
 */
class TimerViewModel(
    private val dao: PomodoroDao  // 通过构造函数注入 DAO（对标 Hilt @Inject）
) : ViewModel() {

    // ===== 状态管理 =====
    // MutableStateFlow = 可写的状态流（对标 Flutter 的 state 变量）
    // StateFlow = 只读的状态流（暴露给 UI 订阅）
    //
    // 传统 Java 对应：
    //   private int remainingSeconds = 1500;  // 成员变量
    //   tvTime.setText(...);                  // 手动更新 UI
    // Jetpack 对应：
    //   _uiState.value = newState;            // 改状态
    //   UI collect 后自动更新                  // 不用手动调 setText
    private val _uiState = MutableStateFlow(TimerUiState())
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    // ===== 协程 Job（对标传统 Handler + Runnable） =====
    // 传统 Java：Handler handler; Runnable tickRunnable;
    // Jetpack：Job timerJob; (协程作业，可以 cancel)
    private var timerJob: Job? = null

    // ===== 开始/继续计时 =====
    //
    // 传统 Java 写法：
    //   private void startTimer() {
    //       isRunning = true;
    //       handler.postDelayed(tickRunnable, 1000);
    //   }
    //
    // Jetpack 写法：
    //   viewModelScope.launch { while(true) { delay(1000); tick() } }
    //   viewModelScope = 绑定 ViewModel 生命周期的协程作用域
    //   ViewModel 销毁时自动取消所有协程
    fun start() {
        val current = _uiState.value
        if (current.status == TimerStatus.RUNNING) return

        // 如果已完成，重新读满时长（对标 Flutter timer_notifier.dart start()）
        var newState = current
        if (current.remainingSeconds <= 0) {
            newState = current.copy(remainingSeconds = current.totalSeconds)
        }
        _uiState.value = newState.copy(status = TimerStatus.RUNNING)

        // 启动协程做倒计时
        // 传统 Java: handler.postDelayed(tickRunnable, 1000)
        // Jetpack: launch { while { delay(1000); tick() } }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)  // 挂起 1 秒（不阻塞线程！）
                tick()
            }
        }
    }

    // ===== 暂停计时 =====
    // 传统 Java: handler.removeCallbacks(tickRunnable); isRunning = false;
    fun pause() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(status = TimerStatus.PAUSED)
    }

    // ===== 重置计时器 =====
    fun reset() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            status = TimerStatus.IDLE,
            remainingSeconds = _uiState.value.totalSeconds
        )
    }

    // ===== 设置时长 =====
    fun setDuration(minutes: Int) {
        val seconds = minutes * 10
        _uiState.value = TimerUiState(
            totalSeconds = seconds,
            remainingSeconds = seconds,
            completedSessions = _uiState.value.completedSessions
        )
    }

    // ===== 每秒滴答 =====
    //
    // 传统 Java 写法：
    //   private void run() {
    //       remainingSeconds--;
    //       tvTime.setText(formatTime());
    //       if (remainingSeconds > 0) handler.postDelayed(this, 1000);
    //       else onFinished();
    //   }
    //
    // Jetpack 写法：改 _uiState.value → UI 自动更新
    private fun tick() {
        val current = _uiState.value
        if (current.remainingSeconds <= 1) {
            timerJob?.cancel()

            // 倒计时结束 → 保存记录到 Room 数据库
            // 传统 Java: DatabaseHelper.insert(record) 直接主线程调
            // Jetpack: dao.insert(record) 在协程里自动切 IO 线程
            val now = System.currentTimeMillis()
            viewModelScope.launch {
                dao.insert(PomodoroEntity(
                    startTime = now - current.totalSeconds * 1000L,
                    durationMinutes = current.totalSeconds / 60,
                    endTime = now
                ))
            }

            _uiState.value = current.copy(
                status = TimerStatus.FINISHED,
                remainingSeconds = 0,
                completedSessions = current.completedSessions + 1
            )
        } else {
            _uiState.value = current.copy(
                remainingSeconds = current.remainingSeconds - 1
            )
        }
    }

    // ===== ViewModel 销毁时清理 =====
    //
    // 传统 Java: 在 Activity.onDestroy() 里 handler.removeCallbacks()
    // Jetpack: ViewModel.onCleared() 自动调用（Activity 销毁 → ViewModel 销毁）
    // 对比 Flutter: ref.onDispose(() => _timer?.cancel())
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
