package com.pomodoro.jetpack.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * OkHttpClient 配置 — 拦截器链 + 模拟数据
 *
 * 【面试题51：OkHttp 的拦截器链机制】
 *   责任链模式：请求 → 应用拦截器 → 网络拦截器 → 服务器
 *   addInterceptor(): 应用拦截器（重定向/缓存/日志）
 *   addNetworkInterceptor(): 网络拦截器（网络层，可修改请求/响应）
 *
 * 【面试题54：HTTPS SSL Pinning】
 *   生产环境用 CertificatePinner 绑定服务器证书，防中间人攻击。
 *   本项目模拟数据，暂不配置真实证书。
 */
object OkHttpClientProvider {

    fun create(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

            // ═══════════ 拦截器 1: 日志拦截器 ═══════════
            // 打印请求/响应详情，方便调试
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )

            // ═══════════ 拦截器 2: 模拟 NAS API 响应 ═══════════
            // 因为没有真实 NAS 服务，用拦截器 mock 数据
            // 生产环境替换为真实 API 地址即可
            .addInterceptor(MockNasInterceptor())

            // ═══════════ 拦截器 3: 公共 Header 拦截器 ═══════════
            // 添加 Token、设备信息等公共参数
            .addInterceptor(
                Interceptor { chain ->
                    val request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer mock_token")
                        .addHeader("X-Device-Id", "android_nas_client")
                        .build()
                    chain.proceed(request)
                }
            )
            .build()
    }
}