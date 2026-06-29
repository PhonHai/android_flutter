// ignore_for_file: unintended_html_in_doc_comment
import 'dart:async';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'timer_state.dart';

/// ═══════════════════════════════════════════════════════════
/// 番茄钟核心业务逻辑 — 对标 Android ViewModel
/// ═══════════════════════════════════════════════════════════
///
/// ===== 🔥 最重要的 Android 概念映射 🔥 =====
/// 这个文件就是你的 ViewModel！你所有 Android 经验在这里直接对应。
///
/// | Flutter (Riverpod)     | Android (MVVM)                    | 说明                     |
/// |------------------------|------------------------------------|--------------------------|
/// | `Notifier<TimerState>` | `ViewModel`                       | 持有业务逻辑 + 状态      |
/// | `TimerNotifier`        | `class PomodoroViewModel`          | 具体 ViewModel 实现       |
/// | `build()`              | `init {}` + 初始 state             | ViewModel 初始化          |
/// | `state`                | `_uiState.value`                   | 当前 UiState             |
/// | `state = newState`     | `_uiState.value = newState`         | 更新状态（通知 UI）       |
/// | `ref.onDispose()`      | `onCleared()`                      | 清理资源                 |
/// | `NotifierProvider`     | `@HiltViewModel` + `@Inject`       | DI 注册                  |
/// | `timerProvider`        | `viewModels.pomodoro`               | 全局获取 ViewModel        |
///
/// ===== Flutter 语法速查 =====
/// Timer            ≈ Android CountDownTimer / Handler.postDelayed
/// Timer.periodic   ≈ Timer.scheduleAtFixedRate (每秒执行)
/// _timer?.cancel() ≈ CountDownTimer.cancel()
/// ~/               ≈ Kotlin Int 除法 (整除)
///
/// ===== 面试话术 =====
/// "TimerNotifier 是番茄钟的 ViewModel，用 Riverpod 的 Notifier API 实现。
///  对标 Android MVVM + ViewModel + StateFlow：
///  - state 对应 StateFlow<TimerUiState>
///  - copyWith 对应 data class 的 copy 方法
///  - build() 对应 ViewModel init 块
///  - ref.onDispose 对应 ViewModel.onCleared()
///  Timer 周期任务对标 CountDownTimer。
///  这种映射关系说明 Riverpod 和 Jetpack ViewModel 概念等价，只是 API 不同。"
class TimerNotifier extends Notifier<TimerState> {
  // ===== 私有成员 =====
  // Dart 下划线前缀 = Kotlin private
  // Timer = Dart 的计时器（≈ Android CountDownTimer / Timer）
  Timer? _timer;

  // ===== Notifier 生命周期 =====

  /// build() — 初始化函数（≈ ViewModel init {}）
  ///
  /// 被 Riverpod 框架在第一次创建 Notifier 时调用。
  /// 返回初始 state。
  ///
  /// ref.onDispose(() { ... }) = ≈ ViewModel.onCleared()
  ///   当这个 Notifier 被销毁时自动调用（如 Widget 树移除 ProviderScope）
  @override
  TimerState build() {
    // 注册清理回调（Dart 没有 dispose() 方法，用这个代替）
    // 取消计时器，防止内存泄漏
    ref.onDispose(() => _timer?.cancel());
    return const TimerState();  // 初始状态: idle / 25分钟 / 0个番茄
  }

  // ===== 用户操作：开始 =====

  /// 开始 / 恢复计时
  ///
  /// ≈ ViewModel 中的 onStartClicked()
  ///
  /// 状态机逻辑:
  ///   idle/finished → 开始计时（已完成时重新读满时长）
  ///   paused        → 恢复计时（保持剩余秒数不变）
  ///   running       → 忽略（已经在计时）
  void start() {
    // 互斥：已经在跑了就别再启动
    if (state.status == TimerStatus.running) return;

    // 取消旧计时器（防止多个 Timer 同时跑）
    _timer?.cancel();

    // 如果上次已经完成了（remainingSeconds <= 0），重新读满时长
    // 这是修复的 bug：结束后点开始，应重新计时
    if (state.remainingSeconds <= 0) {
      state = state.copyWith(remainingSeconds: state.totalSeconds);
    }

    // ===== 创建周期性计时器 =====
    // Timer.periodic = 每隔指定时间执行回调
    // ≈ Android: object : CountDownTimer(total, 1000) { onTick { ... } }
    // Duration(seconds: 1) = 1 秒间隔
    // (_) => _tick() = 匿名回调，_ 表示不用的参数
    _timer = Timer.periodic(const Duration(seconds: 1), (_) => _tick());

    // 更新状态为 running
    // state = ... 会触发 Riverpod 通知所有 watcher 重绘 UI
    // ≈ _uiState.value = _uiState.value.copy(status = RUNNING)
    state = state.copyWith(status: TimerStatus.running);
  }

  // ===== 用户操作：暂停 =====

  /// 暂停计时
  ///
  /// cancel() 后 remainingSeconds 保持不变，等用户点「继续」
  void pause() {
    _timer?.cancel();  // 停止 Timer
    state = state.copyWith(status: TimerStatus.paused);
  }

  // ===== 用户操作：重置 =====

  /// 重置计时器
  ///
  /// 回到 idle 状态，剩余时间恢复为总时长
  void reset() {
    _timer?.cancel();  // 停止 Timer
    state = state.copyWith(
      status: TimerStatus.idle,
      remainingSeconds: state.totalSeconds,  // 恢复满时长
    );
  }

  // ===== 用户操作：变更时长 =====

  /// 选择番茄时长（15/25/45 分钟）
  ///
  /// 只在 idle 或 finished 状态下可变更（运行中/暂停中不可变）
  /// 创建全新 TimerState（因为 totalSeconds 变了）
  void setDuration(int minutes) {
    final seconds = minutes * 60;
    // 注意: 这里不调 copyWith，因为 totalSeconds 也变了
    // 保留 completedSessions 不归零
    state = TimerState(
      totalSeconds: seconds,
      remainingSeconds: seconds,
      completedSessions: state.completedSessions,
    );
  }

  // ===== 内部逻辑：每秒滴答 =====

  /// 每秒回调 — 倒计时核心
  ///
  /// ≈ CountDownTimer.onTick()
  void _tick() {
    // 如果倒数到最后一秒 → 完成
    if (state.remainingSeconds <= 1) {
      _timer?.cancel();  // 停止 Timer
      state = state.copyWith(
        status: TimerStatus.finished,
        remainingSeconds: 0,
        completedSessions: state.completedSessions + 1,  // 番茄数 +1
      );
      return;
    }
    // 否则：剩余秒数 -1
    state = state.copyWith(remainingSeconds: state.remainingSeconds - 1);
  }
}

// ===== ═══════════════════════════════════════════════════ =====
// 全局 Provider 注册 — 对标 Android Hilt DI
// ===== ═══════════════════════════════════════════════════ =====

/// timerProvider — 全局单例 ViewModel Provider
///
/// 对标 Android:
/// ```kotlin
/// @HiltViewModel
/// class PomodoroViewModel @Inject constructor(...) : ViewModel() {
///   private val _uiState = MutableStateFlow(TimerUiState())
///   val uiState: StateFlow<TimerUiState> = _uiState.asStateFlow()
/// }
/// ```
///
/// 使用方式:
/// ```dart
/// // 在 Widget 中读取:
/// final state = ref.watch(timerProvider);      // ≈ collectAsState()
/// final notifier = ref.read(timerProvider.notifier);  // ≈ viewModel
///
/// // 调用方法:
/// notifier.start();   // ≈ viewModel.onStartClicked()
/// notifier.pause();   // ≈ viewModel.onPauseClicked()
/// ```
///
/// ===== 面试话术 =====
/// "timerProvider 是全局 NotifierProvider，对标 Hilt 单例 ViewModel。
///  ConsumerWidget 通过 ref.watch 订阅状态变化，
///  对标 Compose 中 collectAsState + StateFlow。
///  这种模式让 Flutter 的状态管理和 Android ViewModel 概念一一对应。"
final timerProvider = NotifierProvider<TimerNotifier, TimerState>(
  TimerNotifier.new,  // Dart 构造函数引用（≈ Kotlin ::new）
);
