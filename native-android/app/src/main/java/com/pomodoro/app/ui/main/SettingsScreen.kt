package com.pomodoro.app.ui.main

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pomodoro.app.DeviceCapabilities
import com.pomodoro.app.DeviceChannelHandler

/**
 * 设置页（Tab 2）—— 设备能力入口
 *
 * 对标 NAS 客户端「设置 → 设备」页：权限/存储/WiFi/蓝牙。
 *
 * 架构亮点：本页与 Flutter 模块共用 [DeviceCapabilities] 同一份能力实现。
 * 需要弹系统框拿结果的用 Compose 的 rememberLauncherForActivityResult；
 * 其余直接调 [DeviceCapabilities]。
 */
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val ctx = LocalContext.current
    val log = remember { mutableStateListOf<String>() }
    fun add(line: String) { log.add(0, line) }

    // ===== ActivityResultLauncher（Compose 版） =====
    // 权限请求
    val permLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        add("✅ 权限结果: $result")
    }
    // SAF 选目录
    val dirLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        add("✅ 选目录: $uri")
    }
    // 蓝牙开启确认
    val btEnableLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { r ->
        add("✅ 蓝牙开启: ${if (r.resultCode == android.app.Activity.RESULT_OK) "同意" else "拒绝"}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("设备能力", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // ───── ① 权限 ─────
        Section("① 权限申请") {
            ActionButton("请求权限(位置+蓝牙+附近WiFi)") {
                permLauncher.launch(arrayOf(
                    DeviceChannelHandler.Permissions.ACCESS_FINE_LOCATION,
                    DeviceChannelHandler.Permissions.BLUETOOTH_SCAN,
                    DeviceChannelHandler.Permissions.BLUETOOTH_CONNECT,
                    DeviceChannelHandler.Permissions.NEARBY_WIFI_DEVICES
                ))
            }
            ActionButton("位置权限是否已授予") {
                val ok = DeviceCapabilities.isPermissionGranted(
                    ctx, DeviceChannelHandler.Permissions.ACCESS_FINE_LOCATION
                )
                add("位置权限授予: $ok")
            }
            ActionButton("打开 App 设置页") { DeviceCapabilities.openAppSettings(ctx) }
        }

        Spacer(Modifier.height(12.dp))
        // ───── ② 存储 ─────
        Section("② 存储访问") {
            ActionButton("SAF 选目录") { dirLauncher.launch(null) }
            ActionButton("查询可用空间") {
                val s = DeviceCapabilities.getStorageStats(ctx)
                add("存储: 总${s["totalBytes"]} 可用${s["availableBytes"]}")
            }
        }

        Spacer(Modifier.height(12.dp))
        // ───── ③ WiFi ─────
        Section("③ WiFi") {
            ActionButton("WiFi 是否开启") {
                add("WiFi 开启: ${DeviceCapabilities.isWifiEnabled(ctx)}")
            }
            ActionButton("当前 WiFi 连接信息") {
                add("WiFi: ${DeviceCapabilities.getWifiInfo(ctx)}")
            }
            ActionButton("打开 WiFi 设置面板") { DeviceCapabilities.openWifiSettings(ctx) }
            ActionButton("扫描周边 WiFi") {
                add("▶ 扫描中（最多 5 秒）...")
                DeviceCapabilities.startWifiScan(ctx) { list ->
                    add("✅ 扫描到 ${list.size} 个网络")
                    list.take(3).forEach { add("   - ${it["ssid"]} (${it["level"]}dBm)") }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        // ───── ④ 蓝牙 ─────
        Section("④ 蓝牙") {
            ActionButton("蓝牙是否开启") {
                add("蓝牙开启: ${DeviceCapabilities.isBluetoothEnabled(ctx)}")
            }
            ActionButton("请求开启蓝牙") {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                btEnableLauncher.launch(intent)
            }
            ActionButton("已配对设备") {
                val list = DeviceCapabilities.getPairedDevices(ctx)
                add("已配对 ${list.size} 个: ${list.joinToString { it["name"].toString() }}")
            }
            ActionButton("开始扫描") {
                add("扫描启动: ${DeviceCapabilities.startDiscovery(ctx)}")
            }
        }

        Spacer(Modifier.height(16.dp))
        // ───── 日志区 ─────
        Text("调用日志", style = MaterialTheme.typography.titleSmall)
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
        log.forEach { l ->
            Text(
                l,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable ColumnScope.() -> Unit) {
    Text(title, style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(6.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        content = content
    )
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun ColumnScope.ActionButton(text: String, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(48.dp)
    ) { Text(text) }
}
