package com.pomodoro.jetpack.viewmodel

/**
 * 计时器状态 — UiState
 *
 * 【传统 Java 思维 → Jetpack 映射】
 *
 * 传统 Java 写法：
 *   private int totalSeconds = 25 * 60;
 *   private int remainingSeconds = 25 * 60;
 *   private int completedSessions = 0;
 *   private boolean isRunning = false;
 *   // 散落在 Activity 里的成员变量，没有封装
 *
 * Jetpack MVVM 写法：
 *   把所有 UI 状态封装在一个 data class 里
 *   配合 StateFlow 实现自动通知（UI 订阅变化）
 *
 * 对比 Flutter 版（timer_state.dart）：
 *   class TimerState {
 *     final TimerStatus status;
 *     final int totalSeconds;
 *     final int remainingSeconds;
 *     final int completedSessions;
 *     TimerState copyWith({...}) => TimerState(...);
 *   }
 *   概念完全一样！都是不可变状态 + copyWith
 *
 * 【为什么用 enum 而不是 boolean isRunning？】
 * 有 4 种状态：idle / running / paused / finished
 * boolean 只能表达 2 种，不够用。
 * 传统 Java 可能用 int 状态码或 String，enum 更安全。
 */
enum class TimerStatus { IDLE, RUNNING, PAUSED, FINISHED }

/**
 * 不可变 UI 状态
 *
 * data class 自动生成：
 *   equals() / hashCode() / toString() / copy()
 * 对比 Flutter TimerState.copyWith() — 手写的 copy
 * Kotlin data class 的 copy 是编译器自动生成的，更省事
 */
data class TimerUiState(
    val status: TimerStatus = TimerStatus.IDLE,
    val totalSeconds: Int = 10,                 // 默认 10 秒（演示用）
    val remainingSeconds: Int = 10,
    val completedSessions: Int = 0
) {
    /** 倒计时进度 [0.0, 1.0] — 对标 Flutter TimerState.progress */
    val progress: Float
        get() = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds else 1f

    /** 格式化时间 MM:SS — 对标 Flutter TimerState.formattedTime */
    val formattedTime: String
        get() = String.format("%02d:%02d", remainingSeconds / 60, remainingSeconds % 60)
}
