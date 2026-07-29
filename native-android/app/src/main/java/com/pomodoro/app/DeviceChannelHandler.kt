package com.pomodoro.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * 原生端「设备能力」MethodChannel — 把 Flutter 的 invokeMethod 转发到 [DeviceCapabilities]。
 *
 * 职责只做两件事：
 *   1. 需要弹系统框拿结果的（权限/SAF/蓝牙开启）—— 用 ActivityResultLauncher 桥接，
 *      通过 pendingXxx 暂存 MethodChannel.Result，结果回来时回填。
 *   2. 其余能力（WiFi 信息/扫描、存储、已配对设备等）—— 直接调 [DeviceCapabilities]。
 *
 * 通道名 `com.pomodoro/device`，需在 FlutterContainerActivity 里 register（要 Activity）。
 *
 * ===== Android 版本现实（必须知道，否则面试翻车） =====
 *   1. WiFi 开关：Android 10(API29)+ 起 WifiManager.setWifiEnabled() 失效，只能跳设置面板
 *   2. WiFi 扫描结果/SSID：8.1+ 需 ACCESS_FINE_LOCATION；13+ 可用 NEARBY_WIFI_DEVICES
 *   3. 蓝牙扫描：11- 需 ACCESS_FINE_LOCATION；12+ 需 BLUETOOTH_SCAN
 *   4. 蓝牙连接/开启确认：12+ 需 BLUETOOTH_CONNECT
 */
object DeviceChannelHandler {

    private const val TAG = "DeviceChannel"
    private const val CHANNEL_NAME = "com.pomodoro/device"

    private var channel: MethodChannel? = null
    private var activity: AppCompatActivity? = null

    // ===== ActivityResultLauncher（必须在 Activity STARTED 前注册） =====
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var openDocTreeLauncher: ActivityResultLauncher<Uri?>? = null
    private var btEnableLauncher: ActivityResultLauncher<Intent>? = null

    // ===== 暂存的 MethodChannel.Result：launcher 结果异步回来时回填 =====
    private var pendingPermission: ((Map<String, Boolean>) -> Unit)? = null
    private var pendingPickDir: ((String?) -> Unit)? = null
    private var pendingBtEnable: ((Boolean) -> Unit)? = null

    /** 注册通道 + ActivityResultLauncher。调用时机：FlutterContainerActivity.onCreate。 */
    fun register(engine: FlutterEngine, host: AppCompatActivity) {
        this.activity = host

        permissionLauncher = host.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            pendingPermission?.invoke(result); pendingPermission = null
        }
        openDocTreeLauncher = host.registerForActivityResult(
            ActivityResultContracts.OpenDocumentTree()
        ) { uri ->
            pendingPickDir?.invoke(uri?.toString()); pendingPickDir = null
        }
        btEnableLauncher = host.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { r ->
            pendingBtEnable?.invoke(r.resultCode == Activity.RESULT_OK); pendingBtEnable = null
        }

        channel = MethodChannel(engine.dartExecutor.binaryMessenger, CHANNEL_NAME).also {
            it.setMethodCallHandler { call, result -> handle(call, result) }
        }
    }

    /** 注销：Activity 销毁时调用，避免泄漏 Activity 引用。 */
    fun unregister() {
        channel?.setMethodCallHandler(null)
        channel = null
        activity = null
        permissionLauncher = null
        openDocTreeLauncher = null
        btEnableLauncher = null
        pendingPermission = null
        pendingPickDir = null
        pendingBtEnable = null
    }

    private fun handle(call: MethodCall, result: MethodChannel.Result) {
        val ctx = activity ?: run {
            result.error("NO_ACTIVITY", "Activity 已销毁，无法处理设备能力请求", null)
            return
        }
        try {
            when (call.method) {
                // ───── 权限（需 Activity 结果）─────
                "requestPermissions" -> {
                    val perms = call.argument<List<String>>("permissions") ?: emptyList()
                    if (perms.isEmpty()) { result.success(emptyMap<String, Boolean>()); return }
                    pendingPermission = { result.success(it) }
                    permissionLauncher?.launch(perms.toTypedArray()) ?: run {
                        pendingPermission = null
                        result.error("NO_LAUNCHER", "权限 launcher 未注册", null)
                    }
                }
                "isPermissionGranted" -> {
                    val p = call.argument<String>("permission")
                        ?: return result.error("ARG", "缺少 permission 参数", null)
                    result.success(DeviceCapabilities.isPermissionGranted(ctx, p))
                }
                "openAppSettings" -> {
                    DeviceCapabilities.openAppSettings(ctx); result.success(true)
                }

                // ───── 存储 ─────
                "pickDirectory" -> {
                    // SAF：系统级目录选择器，返回 content://... 树 URI，不需要运行时权限
                    pendingPickDir = { result.success(it) }
                    openDocTreeLauncher?.launch(null) ?: run {
                        pendingPickDir = null
                        result.error("NO_LAUNCHER", "SAF launcher 未注册", null)
                    }
                }
                "getStorageStats" -> result.success(DeviceCapabilities.getStorageStats(ctx))

                // ───── WiFi ─────
                "isWifiEnabled" -> result.success(DeviceCapabilities.isWifiEnabled(ctx))
                "getWifiInfo" -> result.success(DeviceCapabilities.getWifiInfo(ctx))
                "openWifiSettings" -> {
                    DeviceCapabilities.openWifiSettings(ctx); result.success(true)
                }
                "startWifiScan" -> DeviceCapabilities.startWifiScan(ctx) { list ->
                    result.success(list)
                }

                // ───── 蓝牙 ─────
                "isBluetoothEnabled" -> result.success(DeviceCapabilities.isBluetoothEnabled(ctx))
                "requestEnableBluetooth" -> {
                    val adapter = ctx.getSystemService(android.bluetooth.BluetoothManager::class.java)?.adapter
                    if (adapter == null) { result.success(false); return }
                    if (adapter.isEnabled) { result.success(true); return }
                    val intent = Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    pendingBtEnable = { result.success(it) }
                    btEnableLauncher?.launch(intent) ?: run {
                        pendingBtEnable = null
                        result.error("NO_LAUNCHER", "蓝牙 launcher 未注册", null)
                    }
                }
                "getPairedDevices" -> result.success(DeviceCapabilities.getPairedDevices(ctx))
                "startDiscovery" -> result.success(DeviceCapabilities.startDiscovery(ctx))

                else -> result.notImplemented()
            }
        } catch (e: SecurityException) {
            // 12+ 缺 BLUETOOTH_CONNECT/SCAN 时会抛 SecurityException
            Log.e(TAG, "设备能力调用失败: ${call.method}", e)
            result.error("SECURITY", "${e.message}", null)
        } catch (e: Exception) {
            Log.e(TAG, "设备能力调用失败: ${call.method}", e)
            result.error("ERROR", "${e.message}", null)
        }
    }

    /** Flutter 侧可请求的权限名常量（便于 demo 引用，避免拼错）。 */
    object Permissions {
        const val ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION
        const val BLUETOOTH_SCAN = android.Manifest.permission.BLUETOOTH_SCAN
        const val BLUETOOTH_CONNECT = android.Manifest.permission.BLUETOOTH_CONNECT
        const val NEARBY_WIFI_DEVICES = android.Manifest.permission.NEARBY_WIFI_DEVICES
    }
}
