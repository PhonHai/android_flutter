package com.pomodoro.app

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/**
 * 原生端 MethodChannel 处理 — Flutter 调原生能力的桥梁
 *
 * ===== 通信模型（对标 Android IPC） =====
 *
 * Flutter 侧                     MethodChannel            原生侧（本文件）
 * ────────────                  ─────────────            ────────────────
 * channel.invokeMethod(         ──异步消息序列化──→       setMethodCallHandler {
 *   'saveRecord', args)          (二进制编码传输)          call.method → 匹配处理
 *                                                        result.success()  ← 返回
 *
 * ===== Android 概念速查 =====
 * MethodChannel        ≈ AIDL / Binder（跨端通信）
 * channel name         ≈ AIDL interface 的包名（唯一标识）
 * setMethodCallHandler ≈ Stub.onTransact()（注册方法处理器）
 * call.method          ≈ AIDL 方法名（决定调用哪个方法）
 * call.argument<T>()   ≈ 从 Bundle 取值（反序列化参数）
 * result.success()     ≈ 返回调用结果
 * result.notImplemented() ≈ 方法未实现（UnsupportedOperationException）
 *
 * ===== object vs class =====
 * Kotlin object = 单例（Java 的 static + 私有构造）
 * 不需要 PomodoroApplication 持有引用，直接通过 MethodChannel 注册
 *
 * ===== 对标绿联云 =====
 * 绿联云中 MethodChannelHandler 会实现更复杂的原生能力：
 *   - NAS 文件下载/上传进度回调
 *   - 播放器状态控制
 *   - 相册权限请求
 *
 * ===== 面试话术 =====
 * "我在原生侧用 Kotlin object 实现 MethodChannelHandler，
 *  在 Application 层通过 engine.dartExecutor.binaryMessenger 注册，
 *  这样 Flutter 模块可以调用原生 Room 数据库、文件管理等能力。"
 */
object MethodChannelHandler {

    // Channel 名称 = 唯一标识，Flutter 侧必须完全一致
    // 相当于 AIDL 接口的包名：com.pomodoro.history
    private const val CHANNEL_NAME = "com.pomodoro/history"

    /**
     * 注册 MethodChannel 处理器
     *
     * @param engine FlutterEngine 实例（从 PomodoroApplication 传入）
     *
     * 注册时机: Application.onCreate() — 确保引擎创建时就绪
     * binaryMessenger = 二进制消息通道（MethodChannel 的底层传输层）
     */
    fun register(engine: FlutterEngine) {
        // 1. 创建 MethodChannel（指定通道名 + binary messenger）
        val channel = MethodChannel(
            engine.dartExecutor.binaryMessenger,  // 底层消息通道
            CHANNEL_NAME                          // 通道标识
        )

        // 2. 设置方法调用处理器
        // Lambda 参数: call = Flutter 发来的调用, result = 返回结果给 Flutter
        channel.setMethodCallHandler { call, result ->
            when (call.method) {  // when = Java switch / Kotlin 增强版

                // ===== 方法 1: saveRecord — 保存番茄记录 =====
                // Flutter 侧: channel.invokeMethod('saveRecord', {startTime, durationMinutes, endTime})
                "saveRecord" -> {
                    // 从 call 中提取参数（类似从 Bundle 取值）
                    val startTime = call.argument<String>("startTime")
                    val durationMinutes = call.argument<Int>("durationMinutes")
                    val endTime = call.argument<String>("endTime")

                    // ===== TODO: Week 2 接入 Room =====
                    // 当前策略: Week 1 用 sqflite 在 Flutter 侧直接持久化
                    // Week 2 改为:
                    //   val record = PomodoroRecord(startTime, durationMinutes, endTime)
                    //   database.pomodoroDao().insert(record)
                    //
                    // ===== 面试话术 =====
                    // "这里用 Room 做持久化，Flutter 只负责 UI 和计时逻辑，
                    //  数据统一归原生管理，方便后续在原生列表、鸿蒙壳中复用。"

                    // 暂时返回成功（实际持久化在 Flutter sqflite 完成）
                    result.success(true)
                }

                // ===== 方法 2: getRecords — 查询历史记录 =====
                // Flutter 侧: channel.invokeMethod('getRecords')
                "getRecords" -> {
                    // ===== TODO: Week 2 接入 Room =====
                    // val records = database.pomodoroDao().getAll()
                    // result.success(records.map { it.toMap() })

                    // 暂时返回空列表
                    result.success(emptyList<Map<String, String>>())
                }

                // ===== 兜底: 未知方法 =====
                // 相当于 AIDL 的 UNKNOWN_TRANSACTION
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}
