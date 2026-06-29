// ignore_for_file: unintended_html_in_doc_comment
import 'package:flutter/foundation.dart';   // debugPrint
import 'package:flutter/services.dart';      // MethodChannel, PlatformException

/// ═══════════════════════════════════════════════════════════
/// Flutter ↔ 原生 Android 通信桥梁 — MethodChannel
/// ═══════════════════════════════════════════════════════════
///
/// ===== 🔥 最重要的跨端通信概念 🔥 =====
///
/// 通信模型（对标 AIDL/Binder）：
///
///   Flutter (本文件)              原生 Android (MethodChannelHandler.kt)
///   ─────────────────              ─────────────────────────────────────
///   channel.invokeMethod(    ──→   call.method → when 匹配
///     'saveRecord', {args})  ←──   result.success(data) / result.error(...)
///
/// ===== Android 概念映射 =====
///
/// | Flutter                 | Android                         | 说明                     |
/// |-------------------------|---------------------------------|--------------------------|
/// | `MethodChannel(name)`   | `aidl interface IHistoryService`| 通道定义（唯一标识）       |
/// | `invokeMethod()`        | `service.call(method, args)`    | 调用原生方法              |
/// | `PlatformException`     | `RemoteException`               | 远程调用异常              |
/// | `MissingPluginException`| `ServiceNotRegisteredException` | 原生端未实现              |
/// | `debugPrint()`          | `Log.d()`                       | 调试日志                 |
/// | `const _channel`        | `companion object { val aidl }`  | 静态通道引用              |
///
/// ===== 对标绿联云架构 =====
/// 绿联云用 MethodChannel 实现 Flutter 调用原生 API:
///   - 文件管理: Flutter UI → MethodChannel → 原生下载引擎
///   - NAS 连接: Flutter → MethodChannel → 原生 Socket 连接
///   - 设备发现: Flutter → MethodChannel → 原生 mDNS 扫描
/// 本项目的 saveRecord/getRecords 就是这个模式的缩影。
///
/// ===== 面试话术 =====
/// "PomodoroChannel 封装了 Flutter 侧 MethodChannel 调用逻辑。
///  对标 Android AIDL 接口的客户端 Stub，
///  Flutter 通过 invokeMethod 异步调用原生方法，
///  原生端通过 setMethodCallHandler 注册处理函数。
///  异常处理用 PlatformException 捕获通信失败，
///  MissingPluginException 兜底原生端未实现的情况。"
class PomodoroChannel {
  // ===== 静态通道实例 =====
  // const = 编译时常量（≈ Kotlin companion object val）
  // 通道名 'com.pomodoro/history' 必须和原生端 (MethodChannelHandler.kt) 完全一致
  // 相当于 AIDL 接口的包名: com.pomodoro.history
  static const _channel = MethodChannel('com.pomodoro/history');

  /// 调用原生保存番茄记录到 Room 数据库
  ///
  /// 混合架构通信场景:
  ///   Flutter 负责 UI + 计时逻辑
  ///   原生负责数据持久化（Room 数据库）
  ///
  /// @param startTime      开始时间（ISO8601 字符串）
  /// @param durationMinutes 实际完成分钟数
  /// @param endTime        结束时间（ISO8601 字符串）
  /// @return 保存是否成功
  ///
  /// ===== 异常处理策略 =====
  /// 1. PlatformException → 通信失败（原生端 crash / 超时）→ 返回 false
  /// 2. MissingPluginException → 原生端未实现 → 静默降级（用 sqflite 兜底）
  ///
  /// ===== 面试话术 =====
  /// "MethodChannel 调用是异步的（async/await），和 AIDL 的跨进程调用一样有延迟。
  ///  异常分三类处理：PlatformException 是原生端执行出错（返回 false），
  ///  MissingPluginException 是原生端未实现（用 sqflite 兜底），
  ///  其他异常通过 try-catch 全局兜底。
  ///  Future<bool> 对标 Kotlin 的 suspend fun saveRecord(): Boolean。"
  static Future<bool> saveRecordToNative({
    required String startTime,     // required = 必传参数
    required int durationMinutes,
    required String endTime,
  }) async {
    try {
      // invokeMethod = 发送消息到原生端，等待返回
      // 第一个参数 = 方法名（原生端通过 call.method 匹配）
      // 第二个参数 = Map 传参（原生端通过 call.argument<T>("key") 取值）
      // 泛型 <bool> = 期望返回类型
      final result = await _channel.invokeMethod<bool>('saveRecord', {
        'startTime': startTime,
        'durationMinutes': durationMinutes,
        'endTime': endTime,
      });
      // result 是可空的（原生端可能返回 null）
      return result ?? false;  // ?? = Kotlin ?: (Elvis)
    } on PlatformException catch (e) {
      // PlatformException = 原生端处理失败
      // 比如: Room 写入失败、参数校验不通过
      // debugPrint = Flutter 的日志输出（≈ Log.d）
      debugPrint('MethodChannel error: ${e.message}');
      return false;
    } on MissingPluginException {
      // MissingPluginException = 原生端完全没有实现这个方法
      // 这是正常的 Week 1 行为（sqflite 兜底策略）
      debugPrint('原生端未实现 saveRecord，使用 Flutter sqflite 兜底');
      return false;
    }
    // 其他未捕获异常会向上冒泡，由调用方处理
  }

  /// 从原生获取历史记录列表
  ///
  /// @return 记录列表（JSON 格式的 Map 列表），失败返回空列表
  ///
  /// invokeListMethod = invokeMethod 的特殊版本
  ///   期望返回 List 类型（≈ invokeMethod<List<Map>>）
  static Future<List<Map<String, dynamic>>> getRecordsFromNative() async {
    try {
      final result = await _channel.invokeListMethod('getRecords');
      // cast<Map<String, dynamic>> = 类型转换
      // result 是 List<dynamic>?，需要转为具体类型
      return result?.cast<Map<String, dynamic>>() ?? [];
    } on MissingPluginException {
      // Week 1: 原生端未实现，返回空列表
      return [];
    }
  }
}
