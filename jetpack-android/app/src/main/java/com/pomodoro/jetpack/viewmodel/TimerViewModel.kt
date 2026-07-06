package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.data.repository.TimerRepository
import com.pomodoro.jetpack.model.PomodoroEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 番茄钟 ViewModel — 计时器的"大脑"
 *
 * ═══════════════════════════════════════════════════════════
 * 【状态机：4 种状态 + 转换规则】
 * ═══════════════════════════════════════════════════════════
 *
 *           ┌──────────────────────────────────┐
 *           │                                  │
 *    ┌──────┴──────┐    start()    ┌───────────┴──────────┐
 *    │    IDLE     │ ───────────→  │       RUNNING        │
 *    │   (初始)     │               │  每秒 tick() 倒计时    │
 *    └──────┬──────┘               └───┬───────┬──────────┘
 *           │                          │       │
 *           │ reset()              pause()    │  倒计时到 0
 *           │                          │       │
 *           │                    ┌─────▼──┐    │
 *           │                    │ PAUSED │    │
 *           │                    │ (暂停)  │    │
 *           │                    └───┬────┘    │
 *           │                 start()│         │
 *           │                       │         │
 *           │    ┌──────────────────┘         │
 *           │    ▼                            ▼
 *           │  ┌──────────────────┐    ┌──────────────┐
 *           └──│    FINISHED      │◄───│ 保存到 Room   │
 *              │  (计时完成)       │    │ 发射 Snackbar │
 *              └──────────────────┘    └──────────────┘
 *
 * 关键点：RUNNING 状态时，setDuration() 不允许修改时长（TimerScreen 里 Chip 点不了）
 *        只有 IDLE 和 FINISHED 状态才能改时长。
 *
 * ═══════════════════════════════════════════════════════════
 * 【三大面试核心技术】
 * ═══════════════════════════════════════════════════════════
 *
 * 1. SavedStateHandle（题22）— 进程死亡恢复
 *    问题：屏幕旋转 ViewModel 不会销毁，但系统杀进程 ViewModel 就没了。
 *    解决：SavedStateHandle 把 remainingSeconds/status 序列化到系统 Bundle，
 *         进程重生后自动恢复。类比：游戏的"存档"功能。
 *
 * 2. Channel（题44）— 一次性事件，解决 LiveData 粘性事件
 *    问题：LiveData 在订阅前发射的值，新订阅者（如旋转屏幕后）会重复收到。
 *         比如 Snackbar "计时完成！"，旋转屏幕又弹一次。
 *    解决：Channel 是"消费即消失"，collect 一次就没了，不会粘性重播。
 *         类比：微信消息，读了就没了 vs 公告栏，贴上去一直能看到。
 *
 * 3. viewModelScope + delay（题1-2）— 协程替代 Handler
 *    传统 Java：Handler handler; handler.postDelayed(runnable, 1000);
 *    现在：viewModelScope.launch { while(true) { delay(1000); tick() } }
 *    优势：delay 不阻塞线程，只挂起协程；ViewModel 销毁时自动取消。
 *
 * ═══════════════════════════════════════════════════════════
 * 【数据流：UI 交互全链路】
 * ═══════════════════════════════════════════════════════════
 *
 *   用户点"开始"
 *     → TimerScreen: viewModel.start()
 *       → TimerViewModel: _uiState.value = state.copy(status = RUNNING)
 *         → StateFlow 发射新值
 *           → TimerScreen: collectAsStateWithLifecycle() 收到新值
 *             → Compose 自动重组 UI（按钮文字从"开始"变"暂停"）
 *               → viewModelScope.launch { while(true) { delay(1000); tick() } }
 *                 → 每秒 tick()：_uiState.value = state.copy(remainingSeconds = 14 → 13)
 *                   → Canvas 环形进度重绘、时间数字从"00:14"变"00:13"
 *                     → 倒计时到 0：
 *                       1. _uiState.value = state.copy(status = FINISHED)
 *                       2. repository.insert(record)  ← 保存到 Room
 *                       3. _events.trySend(Finished)   ← 发射一次性事件
 *                          → TimerScreen: LaunchedEffect 收集到事件
 *                            → snackbarHostState.showSnackbar("计时完成！")
 *
 * ═══════════════════════════════════════════════════════════
 * 【注入链路】
 * ═══════════════════════════════════════════════════════════
 *
 *   Hilt AppModule
 *   ├── Room Database → PomodoroDao → TimerRepository → TimerViewModel
 *   └── SavedStateHandle  ← 框架自动注入，不需要 Module 声明
 *
 *   之前：TimerViewModel(dao: PomodoroDao) 手动 new
 *   现在：@HiltViewModel + @Inject constructor，Hilt 自动组装
 */
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: TimerRepository,        // ← Hilt 注入
    private val savedStateHandle: SavedStateHandle  // ← 框架自动注入
) : ViewModel() {

    // ═══════════════════════════════════════════════════════════
    // 保存到 SavedStateHandle 的 Key
    // 就像数据库的列名，读/写都用同一个 Key
    // ═══════════════════════════════════════════════════════════
    companion object {
        private const val KEY_REMAINING_SECONDS = "remaining_seconds"
        private const val KEY_STATUS = "status"
        private const val KEY_TOTAL_SECONDS = "total_seconds"
        private const val KEY_COMPLETED_SESSIONS = "completed_sessions"
    }

    // ═══════════════════════════════════════════════════════════
    // 核心状态：倒计时数据
    // ═══════════════════════════════════════════════════════════
    //
    // 为什么用 MutableStateFlow 而不是普通变量？
    //   普通变量改了 UI 不知道，StateFlow 改了自动通知 UI 重组。
    //
    // 为什么用 _uiState（私有的 Mutable）和 uiState（公开的只读）？
    //   防止 UI 层直接修改状态。UI 只能读，ViewModel 才能写。
    //   类比：餐厅厨房（ViewModel）做菜，服务员（UI）只能端菜不能炒菜。
    //
    // 初始值从哪里来？
    //   优先从 SavedStateHandle 恢复（进程死亡后还原），
    //   没有保存过就用默认值 10 秒。
    private val _uiState = MutableStateFlow(
        TimerUiState(
            remainingSeconds = savedStateHandle.get<Int>(KEY_REMAINING_SECONDS) ?: 10,
            totalSeconds = savedStateHandle.get<Int>(KEY_TOTAL_SECONDS) ?: 10,
            status = savedStateHandle.get<String>(KEY_STATUS)?.let {
                try { TimerStatus.valueOf(it) } catch (_: Exception) { TimerStatus.IDLE }
            } ?: TimerStatus.IDLE,
            completedSessions = savedStateHandle.get<Int>(KEY_COMPLETED_SESSIONS) ?: 0
        )
    )
    val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()

    // ═══════════════════════════════════════════════════════════
    // Channel：一次性事件通道
    // ═══════════════════════════════════════════════════════════
    //
    // 为什么用 Channel 而不是 StateFlow？
    //   StateFlow 有初始值 + 会缓存最新值 → 新订阅者收到旧值（粘性）
    //   Channel 不缓存 → 新订阅者只收到订阅后发射的数据
    //
    // Channel.BUFFERED：缓冲区大小由系统决定，防止事件丢失
    // receiveAsFlow()：把 Channel 转成 Flow，UI 用 collectLatest 收集
    //
    // 实战场景：
    //   计时完成 → trySend(Finished) → UI 弹 Snackbar
    //   用户旋转屏幕 → UI 重新 subscribe → 不会再弹 Snackbar（因为 Finished 已被消费）
    private val _events = Channel<TimerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    // ═══════════════════════════════════════════════════════════
    // timerJob：协程作业句柄
    // ═══════════════════════════════════════════════════════════
    //
    // 传统 Java 对应：Handler handler; Runnable tickRunnable;
    // 作用：持有协程的引用，可以 cancel() 来停止倒计时。
    //
    // 为什么是 Job? 而不是 null？
    //   初始没有在运行，所以是 null。
    //   start() 时创建新 Job，pause() 时 cancel() 并置 null。
    private var timerJob: Job? = null

    // ═══════════════════════════════════════════════════════════
    // 用户操作：开始计时
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：用户点击"开始"按钮
    //
    // 执行步骤：
    //   1. 读当前状态，如果已经在 RUNNING，直接返回（防重复点击）
    //   2. 如果倒计时到 0 了（FINISHED 状态点开始），重新读满时长
    //   3. 改状态为 RUNNING
    //   4. 保存到 SavedStateHandle（进程死亡恢复用）
    //   5. 取消旧 timerJob（如果有），启动新协程
    //   6. 协程里：while(true) { delay(1000); tick() }
    //
    // 为什么先 cancel 旧的再创建新的？
    //   防止同时有两个协程在跑（比如快速连点两次开始）。
    fun start() {
        val current = _uiState.value

        // 已经在运行 → 什么都不做
        if (current.status == TimerStatus.RUNNING) return

        // 如果倒计时已经到 0（FINISHED），重新读满时长
        // 比如 10 秒计时结束，用户再点"开始"，重新从 10 秒开始
        var newState = current
        if (current.remainingSeconds <= 0) {
            newState = current.copy(remainingSeconds = current.totalSeconds)
        }

        // 改状态 → StateFlow 通知 UI → 按钮文字从"开始"变"暂停"
        _uiState.value = newState.copy(status = TimerStatus.RUNNING)
        saveState()  // 存档

        // 取消旧协程（如果有），启动新协程
        // 类比：把旧的闹钟关了，设一个新的每秒响一次的闹钟
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            // 无限循环 + delay(1000) = 每秒执行一次 tick()
            // delay 不阻塞线程，只挂起当前协程
            // 传统 Java 的等价写法：handler.postDelayed(runnable, 1000)
            while (true) {
                delay(1000)  // 挂起 1 秒
                tick()       // 倒计时 -1
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 用户操作：暂停计时
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：用户在 RUNNING 状态点"暂停"按钮
    //
    // 执行步骤：
    //   1. cancel 协程（停止 tick）
    //   2. 改状态为 PAUSED
    //   3. 存档
    //
    // 暂停后 remainingSeconds 保持不变，再点"开始"会从当前秒数继续。
    fun pause() {
        timerJob?.cancel()  // 停止倒计时
        _uiState.value = _uiState.value.copy(status = TimerStatus.PAUSED)
        saveState()
    }

    // ═══════════════════════════════════════════════════════════
    // 用户操作：重置计时器
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：用户点"重置"或"跳过"按钮
    //
    // 执行步骤：
    //   1. cancel 协程
    //   2. 改状态为 IDLE，remainingSeconds 恢复为 totalSeconds
    //   3. 存档
    //
    // 注意：completedSessions 不重置，保持累计番茄数。
    fun reset() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            status = TimerStatus.IDLE,
            remainingSeconds = _uiState.value.totalSeconds  // 回到满时长
        )
        saveState()
    }

    // ═══════════════════════════════════════════════════════════
    // 用户操作：切换时长（10秒 / 20秒 / 30秒）
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：用户点击 TimerScreen 底部的时长 Chip
    //          只有在 IDLE 或 FINISHED 状态才能改（UI 层控制）
    //
    // 参数：
    //   minutes = 1 → seconds = 10（10 秒）
    //   minutes = 2 → seconds = 20（20 秒）
    //   minutes = 3 → seconds = 30（30 秒）
    //
    // 注意：直接创建新的 TimerUiState，而不是 copy()。
    //       因为 totalSeconds 变了，这是一个全新的配置。
    //       如果只需要改 remainingSeconds，用 copy() 就够了。
    fun setDuration(minutes: Int) {
        val seconds = minutes * 10  // 1→10, 2→20, 3→30
        _uiState.value = TimerUiState(
            totalSeconds = seconds,
            remainingSeconds = seconds,
            completedSessions = _uiState.value.completedSessions  // 保留累计番茄数
        )
        saveState()
    }

    // ═══════════════════════════════════════════════════════════
    // 每秒执行的逻辑：倒计时 -1
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：协程里每秒调用一次
    //
    // 逻辑分两条路：
    //   A. remainingSeconds > 1 → 正常减 1
    //   B. remainingSeconds <= 1 → 计时结束
    //
    // 计时结束后的操作：
    //   1. cancel 协程（停止倒计时）
    //   2. 异步保存记录到 Room 数据库（不阻塞 UI）
    //   3. 改状态为 FINISHED，completedSessions +1
    //   4. 存档
    //   5. 发射 Finished 事件 → UI 弹 Snackbar
    private fun tick() {
        val current = _uiState.value

        if (current.remainingSeconds <= 1) {
            // ═══════════ 倒计时结束 ═══════════
            timerJob?.cancel()

            // 保存记录到 Room 数据库
            // 异步执行，不阻塞 UI 线程
            val now = System.currentTimeMillis()
            viewModelScope.launch {
                repository.insert(PomodoroEntity(
                    // 开始时间 = 当前时间 - 总时长
                    // 比如总时长 10 秒，现在 12:00:10，开始时间就是 12:00:00
                    startTime = now - current.totalSeconds * 1000L,
                    durationMinutes = current.totalSeconds / 60,
                    endTime = now
                ))
            }

            // 更新状态
            _uiState.value = current.copy(
                status = TimerStatus.FINISHED,
                remainingSeconds = 0,
                completedSessions = current.completedSessions + 1
            )
            saveState()

            // 发射一次性事件 → UI 弹 Snackbar "计时完成！"
            // trySend：非阻塞式发送，Channel 满了就丢弃（不会卡住协程）
            _events.trySend(TimerEvent.Finished)

        } else {
            // ═══════════ 正常倒计时：秒数 -1 ═══════════
            _uiState.value = current.copy(
                remainingSeconds = current.remainingSeconds - 1
            )
            saveState()
        }
    }

    // ═══════════════════════════════════════════════════════════
    // 存档：把当前状态写入 SavedStateHandle
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：每次状态变化后都调用（start/pause/reset/tick/setDuration）
    //
    // 为什么每次都要存？
    //   系统随时可能杀进程（低内存），你不知道它什么时候动手。
    //   每次状态变化后立刻存档，确保杀进程时数据不丢。
    //
    // 存了什么？
    //   remainingSeconds: 当前剩余秒数（如 7）
    //   status: 当前状态（如 "RUNNING"）
    //   totalSeconds: 总时长（如 10）
    //   completedSessions: 累计番茄数（如 5）
    //
    // 类比：玩游戏时每走一步就按 F5 快速存档。
    private fun saveState() {
        val state = _uiState.value
        savedStateHandle[KEY_REMAINING_SECONDS] = state.remainingSeconds
        savedStateHandle[KEY_STATUS] = state.status.name  // enum → 字符串
        savedStateHandle[KEY_TOTAL_SECONDS] = state.totalSeconds
        savedStateHandle[KEY_COMPLETED_SESSIONS] = state.completedSessions
    }

    // ═══════════════════════════════════════════════════════════
    // ViewModel 销毁时的清理
    // ═══════════════════════════════════════════════════════════
    //
    // 调用时机：Activity 彻底销毁（不是旋转屏幕，是 finish() 或系统杀进程）
    //
    // 做的事情：
    //   cancel 协程 → 停止倒计时（防止 ViewModel 销毁后协程还在跑）
    //
    // 传统 Java 对应：Activity.onDestroy() 里 handler.removeCallbacks()
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

/**
 * 一次性事件（Channel 分发）
 *
 * 目前只有一种事件：计时完成。
 * 后续可以扩展：如 PauseEvent（暂停）、ErrorEvent（错误）等。
 *
 * 为什么是 sealed class 而不是 enum？
 *   未来不同事件可能携带不同数据，比如：
 *     data class Error(val message: String) : TimerEvent()
 *   enum 无法携带数据，sealed class 可以。
 */
sealed class TimerEvent {
    /** 计时完成 */
    data object Finished : TimerEvent()
}