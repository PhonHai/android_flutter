package com.pomodoro.jetpack.ui.transfer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pomodoro.jetpack.network.TransferProgress
import com.pomodoro.jetpack.network.TransferStatus
import com.pomodoro.jetpack.viewmodel.TransferViewModel

/**
 * Tab 4: 传输列表页 — 文件上传/下载进度
 *
 * 覆盖面试题：
 *   题58：文件上传下载进度（Flow 发射进度 → UI 实时更新）
 *   题51：OkHttp 拦截器链（日志、Header、Mock 三级拦截器）
 *   题54：SSL Pinning（生产环境配置，本项目预留给面试讲解）
 *
 * 功能：
 *   - 模拟上传任务列表
 *   - 实时进度条（LinearProgressIndicator）
 *   - 显示已传输/总大小
 *   - 完成/进行中 状态区分
 *   - 清除已完成任务
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferListScreen(
    modifier: Modifier = Modifier,
    viewModel: TransferViewModel = hiltViewModel()
) {
    val transfers by viewModel.transfers.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("传输列表") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            actions = {
                // 清除已完成
                if (transfers.any { it.status == TransferStatus.COMPLETED }) {
                    TextButton(onClick = { viewModel.clearCompleted() }) {
                        Text("清除已完成")
                    }
                }
            }
        )

        if (transfers.isEmpty()) {
            // 空状态 → 引导用户模拟上传
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "暂无传输任务",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.mockUpload() }) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("模拟上传任务")
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(transfers, key = { it.fileId }) { transfer ->
                    TransferItem(transfer = transfer)
                }
            }
        }
    }
}

/** 单个传输项 */
@Composable
private fun TransferItem(transfer: TransferProgress) {
    val animatedProgress by animateFloatAsState(
        targetValue = transfer.progress,
        label = "transfer_progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 文件名 + 状态
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (transfer.status) {
                        TransferStatus.UPLOADING -> Icons.Filled.CloudUpload
                        TransferStatus.DOWNLOADING -> Icons.Filled.CloudDownload
                        TransferStatus.COMPLETED -> Icons.Filled.CheckCircle
                        TransferStatus.FAILED -> Icons.Filled.Error
                        TransferStatus.PAUSED -> Icons.Filled.PauseCircle
                    },
                    contentDescription = null,
                    tint = when (transfer.status) {
                        TransferStatus.COMPLETED -> Color(0xFF4CAF50)
                        TransferStatus.FAILED -> Color(0xFFF44336)
                        else -> MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    transfer.fileName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    "${(transfer.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 进度条
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxWidth(),
                color = when (transfer.status) {
                    TransferStatus.COMPLETED -> Color(0xFF4CAF50)
                    TransferStatus.FAILED -> Color(0xFFF44336)
                    else -> MaterialTheme.colorScheme.primary
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // 已传输 / 总大小
            Text(
                "${formatFileSize(transfer.bytesTransferred)} / ${formatFileSize(transfer.totalBytes)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatFileSize(bytes: Long): String = when {
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0)} KB"
    bytes < 1024 * 1024 * 1024 -> "${"%.1f".format(bytes / (1024.0 * 1024))} MB"
    else -> "${"%.2f".format(bytes / (1024.0 * 1024 * 1024))} GB"
}