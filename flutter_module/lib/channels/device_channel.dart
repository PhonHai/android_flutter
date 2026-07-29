// ignore_for_file: unintended_html_in_doc_comment
import 'package:flutter/foundation.dart';   // debugPrint
import 'package:flutter/services.dart';      // MethodChannel, PlatformException

/// ═══════════════════════════════════════════════════════════
/// Flutter ↔ 原生「设备能力」桥梁 — MethodChannel
/// ═══════════════════════════════════════════════════════════
///
/// 通道名 `com.pomodoro/device`，和原生 [DeviceChannelHandler.kt] 一致。
/// 对标 NAS 客户端（绿联云）真实场景：配网 → 局域网发现 → 蓝牙配对外设。
///
/// 三组能力：
///   1. 权限请求 + 存储访问（SAF 选目录、可用空间）
///   2. WiFi 控制（开关/当前连接信息/扫描周边网络）
///   3. 蓝牙控制（开关/已配对设备/开始扫描）
///
/// 与 [PomodoroChannel] 的区别：那个不需要 Activity（挂 Application），
/// 这个需要 Activity 来弹系统框（权限/SAF/蓝牙开启），所以通道在
/// FlutterContainerActivity 里注册，拿到的结果通过 pendingXxx 回填。
class DeviceChannel {
  static const _channel = MethodChannel('com.pomodoro/device');

  // ────────────────────────────────────────────────
  // 权限常量（和原生 Manifest 一一对应，避免拼错）
  // ────────────────────────────────────────────────
  static const permAccessFineLocation = 'android.permission.ACCESS_FINE_LOCATION';
  static const permBluetoothScan = 'android.permission.BLUETOOTH_SCAN';
  static const permBluetoothConnect = 'android.permission.BLUETOOTH_CONNECT';
  static const permNearbyWifiDevices = 'android.permission.NEARBY_WIFI_DEVICES';

  /// 请求一组运行时权限，返回 {权限名: 是否授予}。
  /// 对标 NAS App 启动时的「授权引导页」：一次性请求位置+蓝牙+附近WiFi。
  static Future<Map<String, bool>> requestPermissions(List<String> permissions) async {
    try {
      final result = await _channel.invokeMethod<Map>('requestPermissions', {
        'permissions': permissions,
      });
      return result?.map((k, v) => MapEntry(k.toString(), v == true)) ?? {};
    } on PlatformException catch (e) {
      debugPrint('requestPermissions error: ${e.message}');
      return {};
    } on MissingPluginException {
      debugPrint('原生端未实现 requestPermissions');
      return {};
    }
  }

  /// 单个权限是否已授予。
  static Future<bool> isPermissionGranted(String permission) async {
    try {
      final r = await _channel.invokeMethod<bool>('isPermissionGranted', {
        'permission': permission,
      });
      return r ?? false;
    } catch (e) {
      debugPrint('isPermissionGranted error: $e');
      return false;
    }
  }

  /// 打开当前 App 的系统设置页（用户手动改权限用）。
  static Future<bool> openAppSettings() async {
    try {
      return await _channel.invokeMethod<bool>('openAppSettings') ?? false;
    } catch (e) {
      debugPrint('openAppSettings error: $e');
      return false;
    }
  }

  /// SAF 选目录：弹系统文件选择器，返回 content:// 树 URI（不需要存储权限）。
  /// 对标 NAS App 选「本地备份目录」。
  static Future<String?> pickDirectory() async {
    try {
      return await _channel.invokeMethod<String>('pickDirectory');
    } catch (e) {
      debugPrint('pickDirectory error: $e');
      return null;
    }
  }

  /// 存储可用空间：{totalBytes, freeBytes, availableBytes}。
  static Future<Map<String, int>> getStorageStats() async {
    try {
      final r = await _channel.invokeMethod<Map>('getStorageStats');
      return r?.map((k, v) => MapEntry(k.toString(), (v as num).toInt())) ?? {};
    } catch (e) {
      debugPrint('getStorageStats error: $e');
      return {};
    }
  }

  // ─────────────── WiFi ───────────────

  /// WiFi 是否开启。
  static Future<bool> isWifiEnabled() async {
    try {
      return await _channel.invokeMethod<bool>('isWifiEnabled') ?? false;
    } catch (e) {
      debugPrint('isWifiEnabled error: $e');
      return false;
    }
  }

  /// 当前 WiFi 连接信息：{ssid, bssid, ip, rssi, linkSpeed}。
  /// SSID 在无权限/未连接时返回空或 "<unknown ssid>"。
  static Future<Map<String, dynamic>> getWifiInfo() async {
    try {
      final r = await _channel.invokeMethod<Map>('getWifiInfo');
      return r?.map((k, v) => MapEntry(k.toString(), v)) ?? {};
    } catch (e) {
      debugPrint('getWifiInfo error: $e');
      return {};
    }
  }

  /// 跳系统 WiFi 设置面板（Android 10+ 不能直接开关 WiFi）。
  static Future<bool> openWifiSettings() async {
    try {
      return await _channel.invokeMethod<bool>('openWifiSettings') ?? false;
    } catch (e) {
      debugPrint('openWifiSettings error: $e');
      return false;
    }
  }

  /// 触发一次 WiFi 扫描，返回周边网络列表 [{ssid,bssid,level,frequency,capabilities}]。
  /// Android 9+ startScan 被限流，可能返回缓存的旧结果。
  static Future<List<Map<String, dynamic>>> startWifiScan() async {
    try {
      final r = await _channel.invokeListMethod('startWifiScan');
      return r?.map((e) => Map<String, dynamic>.from(e as Map)).toList() ?? [];
    } catch (e) {
      debugPrint('startWifiScan error: $e');
      return [];
    }
  }

  // ─────────────── 蓝牙 ───────────────

  /// 蓝牙是否开启。
  static Future<bool> isBluetoothEnabled() async {
    try {
      return await _channel.invokeMethod<bool>('isBluetoothEnabled') ?? false;
    } catch (e) {
      debugPrint('isBluetoothEnabled error: $e');
      return false;
    }
  }

  /// 弹系统「请求开启蓝牙」框，返回用户是否同意。
  static Future<bool> requestEnableBluetooth() async {
    try {
      return await _channel.invokeMethod<bool>('requestEnableBluetooth') ?? false;
    } catch (e) {
      debugPrint('requestEnableBluetooth error: $e');
      return false;
    }
  }

  /// 已配对设备列表 [{name, address}]。
  static Future<List<Map<String, dynamic>>> getPairedDevices() async {
    try {
      final r = await _channel.invokeListMethod('getPairedDevices');
      return r?.map((e) => Map<String, dynamic>.from(e as Map)).toList() ?? [];
    } catch (e) {
      debugPrint('getPairedDevices error: $e');
      return [];
    }
  }

  /// 启动经典蓝牙扫描，返回是否成功启动。
  /// 注意：发现的新设备通过 ACTION_FOUND 广播，本 demo 不展开推送（实战可用 EventChannel）。
  static Future<bool> startDiscovery() async {
    try {
      return await _channel.invokeMethod<bool>('startDiscovery') ?? false;
    } catch (e) {
      debugPrint('startDiscovery error: $e');
      return false;
    }
  }
}
