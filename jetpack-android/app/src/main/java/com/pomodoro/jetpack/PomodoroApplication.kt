package com.pomodoro.jetpack

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application 入口 — Hilt 依赖注入容器
 *
 * 【面试题34：@HiltAndroidApp 背后的原理】
 *   - 触发 Hilt 代码生成（Hilt_* 基类）
 *   - 创建 Application 级别的 DI 容器（SingletonComponent）
 *   - 所有 @AndroidEntryPoint 标记的组件（Activity/Fragment/ViewModel）
 *     从该容器获取依赖
 *
 * 注意：AndroidManifest.xml 中需将 android:name 指向此类
 */
@HiltAndroidApp
class PomodoroApplication : Application()