package com.pomodoro.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterFragment

/**
 * Flutter 容器 Activity — 原生壳嵌入 Flutter 模块的核心
 *
 * ===== 这是什么？ =====
 * 这是一个「壳」Activity，它内部不写 UI，而是把整个屏幕交给 Flutter 渲染。
 * 你把 Flutter 模块理解为一个「超级 View」，这个 Activity 只是它的容器。
 *
 * ===== Android 概念速查 =====
 * AppCompatActivity                ≈ 普通 Activity（需要 Fragment 容器）
 * setContentView(R.layout.xxx)     ≈ 加载一个 FrameLayout 作为 Flutter 的挂载点
 * FlutterFragment                  ≈ Fragment（嵌入 Flutter UI 的容器）
 * FlutterFragment.withCachedEngine ≈ 取 Application 层预热的引擎（避免重建）
 * shouldAttachEngineToActivity(false) ≈ 引擎生命周期 ≠ Activity 生命周期
 *                                          （这样切页面不销毁引擎，秒回）
 *
 * ===== 为什么用 AppCompatActivity 而不是 ComponentActivity？ =====
 * ComponentActivity 不支持 FragmentManager（需要 onAttachFragment 回调）
 * AppCompatActivity 继承了 FragmentActivity，支持传统 Fragment 生命周期
 * FlutterFragment 是 Fragment 子类，必须用 AppCompatActivity 容器
 *
 * ===== 对标绿联云架构 =====
 * 绿联云中的 FlutterContainerActivity：
 *   - 同样用 FlutterFragment 加载预热的 FlutterEngine
 *   - 负责影视中心、设备发现等 Flutter 模块的宿主
 *   - shouldAttachEngineToActivity(false) 让引擎生命周期独立
 *
 * ===== 面试话术 =====
 * "FlutterContainerActivity 内部使用 FlutterFragment 加载 Application 层预热的
 *  FlutterEngine，引擎复用避免重复初始化。
 *  shouldAttachEngineToActivity(false) 让引擎生命周期与 Activity 解耦，
 *  页面切换时不销毁引擎，返回时秒开。
 *  对标绿联云中 FlutterContainerActivity 加载影视中心模块的方案。"
 *
 * ===== FlutterFragment vs FlutterActivity =====
 * FlutterActivity  = 全屏 Flutter 页面（简单场景用，但引擎内建）
 * FlutterFragment  = 可嵌套在原生 Activity 中（灵活，可混排原生组件）
 * 本项目用 FlutterFragment = 演示更复杂的嵌入方式
 */
class FlutterContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // activity_flutter_container.xml  = 只有个 FrameLayout（id=flutter_container）
        // 这个 FrameLayout 作为 FlutterFragment 的挂载点
        setContentView(R.layout.activity_flutter_container)

        // savedInstanceState == null = 首次创建，非屏幕旋转重建
        if (savedInstanceState == null) {
            // ===== 加载预热的 FlutterEngine =====
            // 从 FlutterEngineCache 取出 Application 层缓存的引擎
            // 相当于: engine = cache.get("pomodoro_engine")
            //
            // shouldAttachEngineToActivity(false) — 关键参数：
            //   false = 引擎生命周期归 Application 管，Activity 销毁不影响引擎
            //   true  = 引擎随 Activity 一起销毁（每次都重新创建，~200ms 白屏）
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.flutter_container,  // 挂载到 FrameLayout
                    FlutterFragment.withCachedEngine(PomodoroApplication.ENGINE_ID)
                        .shouldAttachEngineToActivity(false)
                        .build()
                )
                .commit()
        }
        // 如果不是首次创建（如屏幕旋转），FlutterFragment 自动恢复，不需要重新添加
    }

    // ===== 返回键处理 =====
    // Flutter 模块内部没有导航（项目设计如此），所以直接 finish()
    // 如果有 Flutter 内部导航（Navigator.push），需要改为：
    //   flutterEngine.navigationChannel.popRoute()  让 Flutter 处理返回
}
