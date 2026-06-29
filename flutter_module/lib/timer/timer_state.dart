// ignore_for_file: unintended_html_in_doc_comment

/// ═══════════════════════════════════════════════════════════
/// 番茄钟状态定义 — 对标 Android UiState data class
/// ═══════════════════════════════════════════════════════════
///
/// ===== Android 概念映射 =====
/// 这个文件 ≈ Android MVVM 中的 UiState + Status 枚举
///
/// | Flutter           | Android 对应                          | 说明                     |
/// |-------------------|---------------------------------------|--------------------------|
/// | `TimerStatus`     | `enum PomodoroStatus { IDLE, RUNNING...}` | 状态枚举               |
/// | `TimerState`      | `data class TimerUiState(...)`        | 不可变 UiState           |
/// | `copyWith()`      | `data class` 自动生成的 `.copy()`      | 部分更新（不改原始对象）   |
/// | `get progress`    | `val progress: Float get() = ...`     | 计算属性                 |
/// | `get formattedTime`| `val formattedTime: String get()`    | 格式化输出               |
/// | `const` 构造函数   | `data class` 的 `val` 属性             | 不可变保证               |
///
/// ===== 为什么状态要不可变？ =====
/// 1. 状态变化可追溯（每次 copyWith 生成新实例）
/// 2. UI 重绘判断精准（Flutter 比较新旧 Widget 引用）
/// 3. 线程安全（Dart 单线程模型 + Isolate，不可变避免竞态）
/// ≈ Kotlin data class + StateFlow 的组合
///
/// ===== 面试话术 =====
/// "TimerState 是不可变的 UiState 类，用 copyWith 方法实现部分状态更新。
///  对标 Android 中 data class 的 copy 方法，
///  每次状态变化生成新实例，配合 Riverpod 的状态通知机制驱动 UI 重绘。"
///
/// 番茄钟状态枚举 — 对标 Android 的状态 Sealed Class
///
/// 4 种状态 = 单状态机的全部可能状态
/// 传统 Android 可能用 Sealed Class 或 Enum + when 表达式实现
enum TimerStatus {
  idle,     // 空闲（初始/重置后）
  running,  // 运行中
  paused,   // 已暂停
  finished, // 已完成（倒计时归零）
}

/// TimerState — 不可变 UI 状态类
///
/// 对标 Kotlin:
/// ```kotlin
/// data class TimerUiState(
///   val status: TimerStatus = TimerStatus.IDLE,
///   val totalSeconds: Int = 1500,           // 25分钟
///   val remainingSeconds: Int = 1500,
///   val completedSessions: Int = 0,
/// ) {
///   val progress: Float get() = if (totalSeconds > 0) remainingSeconds.toFloat()/totalSeconds else 1f
///   val formattedTime: String get() = "${"%02d".format(remainingSeconds/60)}:${"%02d".format(remainingSeconds%60)}"
/// }
/// ```
class TimerState {
  final TimerStatus status;       // 当前状态（枚举）
  final int totalSeconds;         // 总时长（秒），默认 25*60 = 1500
  final int remainingSeconds;     // 剩余秒数（倒计时递减）
  final int completedSessions;    // 已完成番茄钟数（完成一次 +1）

  /// 默认构造函数
  ///
  /// - Dart 用 const 标记编译时常量（≈ Kotlin val）
  /// - 所有字段都是 final = 不可变
  /// - 参数默认值 ≈ Kotlin 函数默认参数
  const TimerState({
    this.status = TimerStatus.idle,
    this.totalSeconds = 25 * 60,        // 默认 25 分钟 = 番茄钟标准时长
    this.remainingSeconds = 25 * 60,    // 初始剩余 = 总时长
    this.completedSessions = 0,
  });

  // ---- 计算属性（getter） ----

  /// 倒计时进度 [0.0, 1.0]
  ///
  /// 用于 CustomPainter 画环形进度条
  /// 1.0 = 满（刚开始），0.0 = 空（已完成）
  double get progress =>
      totalSeconds > 0 ? remainingSeconds / totalSeconds : 1.0;

  /// 格式化时间显示  MM:SS
  ///
  /// ~/ 是 Dart 的整除运算符（≈ Kotlin / 在 Int 上）
  /// %  是取余（和 Kotlin 一样）
  /// padLeft(2, '0') ≈ Kotlin "%02d".format()
  String get formattedTime {
    final minutes = remainingSeconds ~/ 60;  // ~/ = 整除（截断）
    final seconds = remainingSeconds % 60;
    return '${minutes.toString().padLeft(2, '0')}:'
        '${seconds.toString().padLeft(2, '0')}';
  }

  /// 部分状态更新 — 对标 Kotlin data class 的 copy()
  ///
  /// 用法: state.copyWith(status: TimerStatus.running)
  ///       → 返回新 TimerState(status=running, 其他字段不变)
  ///
  /// 为什么不用 setter？
  ///   - final 字段不可写（不可变设计）
  ///   - copyWith 生成新对象，触发 Riverpod 状态通知
  ///   - Flutter 的 StatefulWidget.setState() 是另一套模式，本项目用 Riverpod 替代
  TimerState copyWith({
    TimerStatus? status,
    int? totalSeconds,
    int? remainingSeconds,
    int? completedSessions,
  }) =>
      TimerState(
        // ?? 运算符 = 如果左侧为 null 则用右侧
        // ≈ Kotlin ?: (Elvis operator)
        status: status ?? this.status,
        totalSeconds: totalSeconds ?? this.totalSeconds,
        remainingSeconds: remainingSeconds ?? this.remainingSeconds,
        completedSessions: completedSessions ?? this.completedSessions,
      );
}
