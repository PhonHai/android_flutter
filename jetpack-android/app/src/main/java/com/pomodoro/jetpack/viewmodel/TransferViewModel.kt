package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.network.TransferProgress
import com.pomodoro.jetpack.network.TransferStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 传输列表 ViewModel — 模拟文件上传/下载进度
 *
 * 【面试题58：文件上传下载进度监听】
 *   OkHttp RequestBody 包装 → 重写 writeTo 统计已发送字节 → Flow 发射进度。
 *   面试话术："用 Flow 替代回调，避免回调地狱，天然支持背压。"
 *
 * 生产环境：结合 WorkManager 做后台传输，支持断点续传（Range 请求头）。
 */
@HiltViewModel
class TransferViewModel @Inject constructor() : ViewModel() {

    private val _transfers = MutableStateFlow<List<TransferProgress>>(emptyList())
    val transfers: StateFlow<List<TransferProgress>> = _transfers.asStateFlow()

    fun mockUpload() {
        val mockFiles = listOf(
            TransferProgress("1", "全家福_2024.jpg", 0f, 0, 9_200_000, TransferStatus.UPLOADING),
            TransferProgress("2", "年终总结报告.pdf", 0f, 0, 2_560_000, TransferStatus.UPLOADING),
            TransferProgress("3", "产品演示视频.mp4", 0f, 0, 156_000_000, TransferStatus.UPLOADING),
        )
        _transfers.value = mockFiles

        viewModelScope.launch {
            var progress = 0f
            while (progress < 1f) {
                delay(300)
                progress += 0.05f
                val p = progress.coerceAtMost(1f)
                _transfers.value = _transfers.value.map {
                    if (it.status != TransferStatus.COMPLETED) {
                        it.copy(
                            progress = p,
                            bytesTransferred = (it.totalBytes * p).toLong(),
                            status = if (p >= 1f) TransferStatus.COMPLETED else TransferStatus.UPLOADING
                        )
                    } else it
                }
            }
        }
    }

    fun clearCompleted() {
        _transfers.value = _transfers.value.filter { it.status != TransferStatus.COMPLETED }
    }
}