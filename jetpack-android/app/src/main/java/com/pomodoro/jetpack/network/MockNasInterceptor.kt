package com.pomodoro.jetpack.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * 模拟 NAS API 响应拦截器
 *
 * 拦截所有请求，返回模拟 JSON 数据。
 * 好处：不需要真实服务器，专注学习 Android 端架构。
 * 切换到真实 API 时，只需删除此拦截器，改 Retrofit baseUrl 即可。
 */
class MockNasInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val path = request.url.encodedPath

        return when {
            // GET /api/v1/files → 模拟文件列表
            path.contains("/api/v1/files") -> {
                val page = request.url.queryParameter("page")?.toIntOrNull() ?: 1
                val type = request.url.queryParameter("type")
                mockFileListResponse(request, page, type)
            }
            // 其他请求 pass through
            else -> chain.proceed(request)
        }
    }

    private fun mockFileListResponse(
        request: okhttp3.Request,
        page: Int,
        fileType: String?
    ): Response {
        val allFiles = buildMockFiles()
        val filtered = if (fileType != null) {
            allFiles.filter { it.fileType == fileType }
        } else {
            allFiles
        }

        val pageSize = 20
        val total = filtered.size
        val start = (page - 1) * pageSize
        val pagedFiles = filtered.drop(start).take(pageSize)

        val filesJson = pagedFiles.joinToString(",\n    ") { file ->
            """
            {
                "file_id": "${file.fileId}",
                "file_name": "${file.fileName}",
                "file_size": ${file.fileSize},
                "file_type": "${file.fileType}",
                "modified_time": "${file.modifiedTime}",
                "thumbnail_url": "${file.thumbnailUrl ?: ""}"
            }
            """.trimIndent()
        }

        val json = """
        {
            "code": 0,
            "msg": "success",
            "data": {
                "files": [
                    $filesJson
                ],
                "total": $total,
                "page": $page,
                "pageSize": $pageSize
            }
        }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(json.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun buildMockFiles(): List<NasFile> = listOf(
        NasFile("1", "2024年度总结报告.pdf", 2_560_000, "document", "2024-12-20 10:30", null),
        NasFile("2", "家庭旅行照片.jpg", 8_200_000, "image", "2024-11-15 14:22", null),
        NasFile("3", "会议录音_20240115.mp3", 15_800_000, "audio", "2024-01-15 09:00", null),
        NasFile("4", "产品演示视频.mp4", 156_000_000, "video", "2024-10-08 16:45", null),
        NasFile("5", "项目需求文档_v2.docx", 1_280_000, "document", "2024-09-01 11:20", null),
        NasFile("6", "宝宝周岁照_001.jpg", 6_500_000, "image", "2024-08-20 08:15", null),
        NasFile("7", "宝宝周岁照_002.jpg", 7_100_000, "image", "2024-08-20 08:16", null),
        NasFile("8", "周杰伦 - 晴天.flac", 28_000_000, "audio", "2024-07-10 20:00", null),
        NasFile("9", "NAS使用手册.pdf", 4_500_000, "document", "2024-06-25 13:00", null),
        NasFile("10", "系统备份镜像_2024.iso", 4_200_000_000, "other", "2024-12-31 23:59", null),
        NasFile("11", "Python学习笔记.md", 85_000, "document", "2024-05-12 17:30", null),
        NasFile("12", "年会节目排练.mp4", 320_000_000, "video", "2024-04-28 19:00", null),
        NasFile("13", "旅游攻略_日本.pdf", 3_200_000, "document", "2024-03-15 10:00", null),
        NasFile("14", "猫咪照片.jpg", 4_800_000, "image", "2024-02-14 14:14", null),
        NasFile("15", "陈奕迅 - 十年.mp3", 9_500_000, "audio", "2024-01-01 00:00", null),
        NasFile("16", "财务数据_2023.xlsx", 890_000, "document", "2023-12-31 18:00", null),
        NasFile("17", "无人机航拍.mp4", 580_000_000, "video", "2023-11-20 07:00", null),
        NasFile("18", "结婚照_精选.jpg", 12_000_000, "image", "2023-10-01 10:00", null),
        NasFile("19", "毕业设计论文.pdf", 6_700_000, "document", "2023-06-20 15:00", null),
        NasFile("20", "五月天 - 倔强.flac", 25_000_000, "audio", "2023-05-05 05:05", null),
        NasFile("21", "户外烧烤视频.mp4", 180_000_000, "video", "2023-04-10 12:00", null),
        NasFile("22", "全家福_2023.jpg", 9_200_000, "image", "2023-01-01 08:00", null),
        NasFile("23", "简历_2024.pdf", 420_000, "document", "2024-01-02 09:00", null),
        NasFile("24", "邓紫棋 - 光年之外.mp3", 8_800_000, "audio", "2023-08-08 20:08", null),
        NasFile("25", "世界杯决赛回放.mp4", 2_100_000_000, "video", "2022-12-18 23:00", null),
        NasFile("26", "春天花园.jpg", 5_400_000, "image", "2024-03-20 06:00", null),
        NasFile("27", "进出口合同.pdf", 1_500_000, "document", "2024-01-10 14:00", null),
        NasFile("28", "林俊杰 - 不为谁而作的歌.mp3", 10_200_000, "audio", "2023-09-12 21:00", null),
        NasFile("29", "教学视频_第1讲.mp4", 450_000_000, "video", "2024-02-01 10:00", null),
        NasFile("30", "夕阳照片.jpg", 3_900_000, "image", "2024-04-05 18:30", null),
    )
}