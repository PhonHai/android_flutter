package com.pomodoro.app

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StatFs
import android.provider.Settings
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * 设备能力共享层 —— 原生 Compose 设置页 与 Flutter MethodChannel 共用同一份逻辑。
 *
 * 设计意图（面试点）：能力实现只写一遍，消费方有两个：
 *   1. 原生设置页（SettingsScreen）—— 直接调本 object 的函数
 *   2. Flutter 模块 —— 通过 DeviceChannelHandler 转发到本 object
 *
 * 这样避免「原生写一遍、Flutter 再写一遍」的重复，符合 DRY。
 *
 * 注意：本类只封装「不需要 Activity 结果」的能力（直接调系统 API）。
 * 需要弹系统框拿结果的（权限请求/SAF 选目录/蓝牙开启确认）由调用方用
 * ActivityResultLauncher 自行处理，因为 Compose 用 rememberLauncherForActivityResult、
 * MethodChannel 用 Activity 注册的 launcher，机制不同，但拿到结果后都可调本类继续处理。
 */
object DeviceCapabilities {

    // ─────────────── 权限 ───────────────
    fun isPermissionGranted(ctx: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED

    /** 打开当前 App 的系统详情设置页。 */
    fun openAppSettings(ctx: Context) {
        ctx.startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", ctx.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    // ─────────────── 存储 ───────────────
    /** data 分区可用空间：{totalBytes, freeBytes, availableBytes}。 */
    fun getStorageStats(ctx: Context): Map<String, Long> {
        val stat = StatFs(Environment.getDataDirectory().path)
        return mapOf(
            "totalBytes" to stat.totalBytes,
            "freeBytes" to stat.freeBytes,
            "availableBytes" to stat.availableBytes
        )
    }

    // ─────────────── WiFi ───────────────
    fun isWifiEnabled(ctx: Context): Boolean {
        val wm = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wm.isWifiEnabled
    }

    /** 当前 WiFi 连接信息：{ssid, bssid, ip, rssi, linkSpeed}。 */
    @Suppress("DEPRECATION")
    fun getWifiInfo(ctx: Context): Map<String, Any> {
        val wm = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wm.connectionInfo
        return mapOf(
            "ssid" to (info.ssid ?: ""),
            "bssid" to (info.bssid ?: ""),
            "ip" to intToIp(info.ipAddress),
            "rssi" to info.rssi,
            "linkSpeed" to info.linkSpeed
        )
    }

    /**
     * 跳系统 WiFi 设置面板（Android 10+ 不能直接 setWifiEnabled）。
     * API 29+ 用 Settings.Panel.ACTION_WIFI，旧版降级 ACTION_WIFI_SETTINGS。
     */
    fun openWifiSettings(ctx: Context) {
        val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Settings.Panel.ACTION_WIFI else Settings.ACTION_WIFI_SETTINGS
        ctx.startActivity(Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    /**
     * 触发一次 WiFi 扫描，[onResult] 回调周边网络列表（每项 {ssid,bssid,level,frequency,capabilities}）。
     * Android 9+ startScan 被限流，5 秒后用缓存结果兜底。
     */
    @Suppress("DEPRECATION")
    fun startWifiScan(ctx: Context, onResult: (List<Map<String, Any>>) -> Unit) {
        val wm = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val handler = Handler(Looper.getMainLooper())
        var done = false
        var receiver: BroadcastReceiver? = null

        fun finish() {
            if (done) return
            done = true
            receiver?.let { runCatching { ctx.unregisterReceiver(it) } }
            val list = wm.scanResults.map {
                mapOf(
                    "ssid" to (it.SSID ?: ""),
                    "bssid" to (it.BSSID ?: ""),
                    "level" to it.level,
                    "frequency" to it.frequency,
                    "capabilities" to (it.capabilities ?: "")
                )
            }
            onResult(list)
        }

        val r = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) finish()
            }
        }
        receiver = r
        ctx.registerReceiver(
            r,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Context.RECEIVER_NOT_EXPORTED else 0
        )
        wm.startScan()
        handler.postDelayed({ finish() }, 5000)
    }

    // ─────────────── 蓝牙 ───────────────
    private fun adapter(ctx: Context): BluetoothAdapter? =
        ctx.getSystemService(BluetoothManager::class.java)?.adapter

    fun isBluetoothEnabled(ctx: Context): Boolean = adapter(ctx)?.isEnabled == true

    /** 已配对设备列表（经典蓝牙）：[{name, address}]。 */
    fun getPairedDevices(ctx: Context): List<Map<String, String>> {
        val paired = adapter(ctx)?.bondedDevices ?: emptySet()
        return paired.map { mapOf("name" to (it.name ?: ""), "address" to it.address) }
    }

    /** 启动经典蓝牙扫描，返回是否成功启动。 */
    fun startDiscovery(ctx: Context): Boolean = adapter(ctx)?.startDiscovery() ?: false

    // ─────────────── 工具 ───────────────
    private fun intToIp(ip: Int): String =
        String.format(
            "%d.%d.%d.%d",
            ip and 0xff,
            (ip shr 8) and 0xff,
            (ip shr 16) and 0xff,
            (ip shr 24) and 0xff
        )
}
