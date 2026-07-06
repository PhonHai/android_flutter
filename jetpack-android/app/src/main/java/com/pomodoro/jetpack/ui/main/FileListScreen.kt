package com.pomodoro.jetpack.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pomodoro.jetpack.network.NasFile
import com.pomodoro.jetpack.network.Result
import com.pomodoro.jetpack.viewmodel.FileViewModel

/**
 * Tab 2: 文件管理页 — NAS 文件列表
 *
 * 覆盖面试题：
 *   题4：sealed class Result 三类状态 UI
 *   题43：Retrofit + Flow 数据获取
 *   题42：Repository 模式
 *   题34：Hilt 注入 ViewModel
 *
 * 功能：
 *   - 模拟 NAS 文件列表（图片/视频/文档/音频/其他）
 *   - 按类型筛选（顶部 Tab）
 *   - 加载更多（LazyColumn 滚动到底部触发）
 *   - 显示文件大小、修改时间
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileListScreen(
    modifier: Modifier = Modifier,
    viewModel: FileViewModel = hiltViewModel()
) {
    val files by viewModel.files.collectAsState()
    var selectedType by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    // 滚动到底部加载更多
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleItem >= totalItems - 3 && totalItems > 0
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && files is Result.Success) {
            viewModel.loadMore()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // ===== 顶部标题栏 =====
        TopAppBar(
            title = { Text("NAS 文件管理") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        // ===== 类型筛选 Tab =====
        FileTypeFilterRow(
            selectedType = selectedType,
            onTypeSelected = { type ->
                selectedType = type
                viewModel.loadFiles(type)
            }
        )

        // ===== 内容区 =====
        when (val result = files) {
            is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Result.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.CloudOff,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            result.message,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadFiles(selectedType) }) {
                            Text("重试")
                        }
                    }
                }
            }

            is Result.Success -> {
                if (result.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("暂无文件", style = MaterialTheme.typography.bodyLarge)
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(result.data, key = { it.fileId }) { file ->
                            FileListItem(file = file)
                        }

                        // 加载更多指示器
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "已加载 ${result.data.size} 个文件",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 文件类型筛选行 */
@Composable
private fun FileTypeFilterRow(
    selectedType: String?,
    onTypeSelected: (String?) -> Unit
) {
    val types = listOf(
        null to "全部",
        "image" to "图片",
        "video" to "视频",
        "document" to "文档",
        "audio" to "音频",
    )

    ScrollableTabRow(
        selectedTabIndex = types.indexOfFirst { it.first == selectedType },
        modifier = Modifier.fillMaxWidth(),
        edgePadding = 16.dp,
        divider = {}
    ) {
        types.forEach { (type, label) ->
            Tab(
                selected = type == selectedType,
                onClick = { onTypeSelected(type) },
                text = { Text(label) }
            )
        }
    }
}

/** 文件列表项 */
@Composable
private fun FileListItem(file: NasFile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 后续迭代：点击进入文件详情 */ },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 文件类型图标
            Icon(
                imageVector = getFileIcon(file.fileType),
                contentDescription = file.fileType,
                tint = getFileIconColor(file.fileType),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    file.fileName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text(
                        formatFileSize(file.fileSize),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        " · ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        file.modifiedTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** 根据文件类型返回图标 */
private fun getFileIcon(fileType: String): ImageVector = when (fileType) {
    "image" -> Icons.Filled.Image
    "video" -> Icons.Filled.VideoFile
    "document" -> Icons.Filled.Description
    "audio" -> Icons.Filled.AudioFile
    else -> Icons.Filled.InsertDriveFile
}

/** 根据文件类型返回图标颜色 */
private fun getFileIconColor(fileType: String): Color = when (fileType) {
    "image" -> Color(0xFF4CAF50)
    "video" -> Color(0xFFF44336)
    "document" -> Color(0xFF2196F3)
    "audio" -> Color(0xFFFF9800)
    else -> Color(0xFF9E9E9E)
}

/**
 * 文件大小格式化
 *
 * 【面试题5：Kotlin 扩展函数】
 *   可以用扩展函数简化：fun Long.toFileSize(): String = when { ... }
 *   这里先写普通函数，后续可提为扩展函数演示。
 */
private fun formatFileSize(bytes: Long): String = when {
    bytes < 1024 -> "$bytes B"
    bytes < 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0)} KB"
    bytes < 1024 * 1024 * 1024 -> "${"%.1f".format(bytes / (1024.0 * 1024))} MB"
    else -> "${"%.2f".format(bytes / (1024.0 * 1024 * 1024))} GB"
}