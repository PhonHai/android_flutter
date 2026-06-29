package com.pomodoro.app

import android.app.Application
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

/**
 * Application 层 — FlutterEngine 预热 + MethodChannel 注册
 *
 * ===== 为什么预热引擎？ =====
 * FlutterEngine 创建成本 ~150-200ms（初始化 Dart VM、加载 kernel、创建 Isolate）
 * 如果在 Activity.onCreate 里创建 → 页面白屏 200ms
 * 在 Application.onCreate 里创建 → 页面秒开（从缓存取现成的引擎）
 *
 * ===== Android 概念速查 =====
 * Application              ≈ 传统 Application（生命周期最长，进程级单例）
 * FlutterEngine            ≈ Dart 虚拟机实例（一个 FlutterEngine = 一个 Dart Isolate）
 * FlutterEngineCache       ≈ LruCache<String, FlutterEngine>（内存缓存）
 * DartExecutor             ≈ Dart 代码执行器（加载 main() 入口）
 * MethodChannelHandler     ≈ AIDL Stub（原生侧暴露给 Flutter 的接口实现）
 *
 * ===== 对标绿联云架构 =====
 * 绿联云中 PomodoroApplication 对应主 App 的 Application：
 *   - 这里预加载 FlutterEngine（绿联云预加载所有 Flutter 模块）
 *   - 这里注册 MethodChannel（绿联云注册 NAS 连接、下载引擎等原生能力）
 *   - 这里管理引擎生命周期（绿联云统一管理多模块引擎）
 *
 * ===== 面试话术 =====
 * "FlutterEngine 创建成本高，我在 Application onCreate 里预热并缓存。
 *  后续 Activity 通过 FlutterEngineCache 获取，避免重复初始化。
 *  同时在这里注册 MethodChannel，让 Flutter 模块可以调用原生 API。
 *  这种模式对标绿联云中 Flutter 影视中心模块的加载方式。"
 */
class PomodoroApplication : Application() {

    // lateinit = Kotlin 延迟初始化（运行时保证赋值，省去 null 检查）
    lateinit var flutterEngine: FlutterEngine
        private set  // 外部只读，内部可写

    /**
     * Application.onCreate — 进程启动时执行一次
     *
     * 执行顺序: Application.onCreate → Activity.onCreate → Flutter 页面显示
     */
    override fun onCreate() {
        super.onCreate()

        // ===== 步骤 1: 创建并预热 FlutterEngine =====
        // apply {} = Kotlin 作用域函数（builder 模式），在对象上执行代码块
        flutterEngine = FlutterEngine(this).apply {
            // executeDartEntrypoint = 运行 main() 函数，启动 Dart VM
            // 必须执行！FlutterFragment.withCachedEngine 不会自动创建 DartVM，
            // 它只是把已有引擎附加到 FlutterView 上渲染
            dartExecutor.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
        }
        // 引擎创建在 Application 层，Dart 执行延迟到 View 附加时。

        // ===== 步骤 2: 注册 MethodChannel =====
        // Flutter 侧通过 MethodChannel('com.pomodoro/history') 调用这里的方法
        // 相当于 AIDL 的 Stub 注册：把原生能力暴露给 Flutter
        MethodChannelHandler.register(flutterEngine)

        // ===== 步骤 3: 缓存引擎 =====
        // FlutterEngineCache = 全局单例 HashMap<String, FlutterEngine>
        // FlutterContainerActivity 通过 ENGINE_ID = "pomodoro_engine" 取出
        // ≈ 把 ViewModel 放到全局缓存，Activity 从缓存取
        FlutterEngineCache
            .getInstance()
            .put(ENGINE_ID, flutterEngine)
    }

    companion object {
        // companion object = Kotlin 的静态成员（≈ Java static）
        // 作为引擎缓存的 key，后续 Activity 通过这个 ID 取引擎
        const val ENGINE_ID = "pomodoro_engine"
    }
}
