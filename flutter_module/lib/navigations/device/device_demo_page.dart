import 'package:flutter/material.dart';
import '../../channels/device_channel.dart';

/// ═══════════════════════════════════════════════════════════
/// 设备能力 Demo 页 — 演示 Flutter 调原生：权限/存储/WiFi/蓝牙
/// ═══════════════════════════════════════════════════════════
///
/// 对标 NAS 客户端启动流程：授权 → 看网络 → 发现设备 → 配对外设。
/// 每个按钮触发一次 MethodChannel 调用，结果实时显示在下方日志区。
class DeviceDemoPage extends StatefulWidget {
  const DeviceDemoPage({super.key});

  @override
  State<DeviceDemoPage> createState() => _DeviceDemoPageState();
}

class _DeviceDemoPageState extends State<DeviceDemoPage> {
  final _log = <String>[];

  void _add(String line) {
    setState(() => _log.insert(0, line));
  }

  Future<void> _run(String label, Future<dynamic> Function() action) async {
    _add('▶ $label');
    try {
      final r = await action();
      _add('  ✅ $r');
    } catch (e) {
      _add('  ❌ $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('设备能力 Demo'), centerTitle: true),
      body: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          // ───── 权限请求 ─────
          _section('① 权限请求 + 存储访问', [
            _btn('请求权限(位置+蓝牙+附近WiFi)', () => _run('requestPermissions', () {
              return DeviceChannel.requestPermissions([
                DeviceChannel.permAccessFineLocation,
                DeviceChannel.permBluetoothScan,
                DeviceChannel.permBluetoothConnect,
                DeviceChannel.permNearbyWifiDevices,
              ]);
            })),
            _btn('位置权限是否已授予', () => _run('isPermissionGranted',
                () => DeviceChannel.isPermissionGranted(DeviceChannel.permAccessFineLocation))),
            _btn('打开 App 设置页', () => _run('openAppSettings',
                () => DeviceChannel.openAppSettings())),
            _btn('SAF 选目录', () => _run('pickDirectory',
                () => DeviceChannel.pickDirectory())),
            _btn('存储可用空间', () => _run('getStorageStats',
                () => DeviceChannel.getStorageStats())),
          ]),

          // ───── WiFi ─────
          _section('② WiFi 控制', [
            _btn('WiFi 是否开启', () => _run('isWifiEnabled',
                () => DeviceChannel.isWifiEnabled())),
            _btn('当前 WiFi 连接信息', () => _run('getWifiInfo',
                () => DeviceChannel.getWifiInfo())),
            _btn('打开 WiFi 设置面板', () => _run('openWifiSettings',
                () => DeviceChannel.openWifiSettings())),
            _btn('扫描周边 WiFi', () => _run('startWifiScan',
                () => DeviceChannel.startWifiScan())),
          ]),

          // ───── 蓝牙 ─────
          _section('③ 蓝牙控制', [
            _btn('蓝牙是否开启', () => _run('isBluetoothEnabled',
                () => DeviceChannel.isBluetoothEnabled())),
            _btn('请求开启蓝牙', () => _run('requestEnableBluetooth',
                () => DeviceChannel.requestEnableBluetooth())),
            _btn('已配对设备', () => _run('getPairedDevices',
                () => DeviceChannel.getPairedDevices())),
            _btn('开始扫描', () => _run('startDiscovery',
                () => DeviceChannel.startDiscovery())),
          ]),

          const SizedBox(height: 8),
          // ───── 日志区 ─────
          Text('调用日志', style: Theme.of(context).textTheme.titleSmall),
          const Divider(),
          ..._log.map((l) => Padding(
            padding: const EdgeInsets.symmetric(vertical: 2),
            child: Text(l, style: const TextStyle(fontFamily: 'monospace', fontSize: 12)),
          )),
        ],
      ),
    );
  }

  Widget _section(String title, List<Widget> children) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const SizedBox(height: 12),
        Text(title, style: Theme.of(context).textTheme.titleMedium?.copyWith(
              fontWeight: FontWeight.bold)),
        const SizedBox(height: 8),
        Wrap(spacing: 8, runSpacing: 8, children: children),
      ],
    );
  }

  Widget _btn(String label, Future<void> Function() onTap) {
    return FilledButton.tonal(onPressed: onTap, child: Text(label));
  }
}
