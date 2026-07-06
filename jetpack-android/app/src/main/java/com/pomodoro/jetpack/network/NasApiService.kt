package com.pomodoro.jetpack.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * NAS 私有云 API 接口 — Retrofit 定义
 *
 * 【面试题52：Retrofit 的动态代理实现原理】
 *   编译期不生成实现类，运行时通过 Proxy.newProxyInstance 动态代理。
 *   调用 nasApi.getFileList() → 拦截器链 → OkHttp 发请求 → Gson 解析 → 返回结果。
 *
 * 【面试题43：Retrofit + 协程 + Flow 封装】
 *   suspend 函数自动在 IO 线程执行，协程自动管理线程切换。
 */
interface NasApiService {

    /**
     * 获取文件列表（分页）
     *
     * 模拟 NAS API: GET /api/v1/files?page=1&pageSize=20
     * 绿联 NAS 实际 API 类似此结构
     */
    @GET("api/v1/files")
    suspend fun getFileList(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("type") fileType: String? = null
    ): Response<NasFileListResponse>

    /**
     * 下载文件（流式，用于进度监听）
     *
     * @Streaming 注解：不一次性读入内存，边读边写文件
     * 这是面试题58「文件下载断点续传」的基础
     */
    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl: String): Response<ResponseBody>
}