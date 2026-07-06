package com.pomodoro.jetpack.network

import com.google.gson.annotations.SerializedName

/**
 * NAS 文件列表响应 — 模拟 NAS 私有云 API 返回结构
 *
 * 绿联 NAS 私有云 API 典型返回格式：
 * {
 *   "code": 0,
 *   "msg": "success",
 *   "data": {
 *     "files": [...],
 *     "total": 100,
 *     "page": 1,
 *     "pageSize": 20
 *   }
 * }
 */
data class NasFileListResponse(
    val code: Int,
    val msg: String,
    val data: FileListData?
)

data class FileListData(
    val files: List<NasFile>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)

/**
 * NAS 文件实体
 */
data class NasFile(
    // 去 JSON 里找 key 为 file_id 的值，然后塞进 fileId 这个变量里。
    @SerializedName("file_id") val fileId: String,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_size") val fileSize: Long,       // 字节
    @SerializedName("file_type") val fileType: String,     // image/video/document/audio/other
    // 支持备用名（alternate），可以同时兼容新旧两种字段名，防止 App 因接口升级而崩溃。
    @SerializedName(value = "modified_time", alternate = ["modified_date"]) val modifiedTime: String,
    // 如果 JSON 里没有返回 thumbnail_url 这个字段，Gson 就不会报错，而是直接给这个变量赋值为 null。
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null
)

/**
 * 上传进度事件
 */
data class TransferProgress(
    val fileId: String,
    val fileName: String,
    val progress: Float,          // 0.0 ~ 1.0
    val bytesTransferred: Long,
    val totalBytes: Long,
    val status: TransferStatus
)

enum class TransferStatus { UPLOADING, DOWNLOADING, COMPLETED, FAILED, PAUSED }