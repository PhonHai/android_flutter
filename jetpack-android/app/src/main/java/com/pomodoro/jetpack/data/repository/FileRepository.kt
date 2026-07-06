package com.pomodoro.jetpack.data.repository

import com.pomodoro.jetpack.network.NasApiService
import com.pomodoro.jetpack.network.NasFile
import com.pomodoro.jetpack.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 文件 Repository — 封装 NAS API 调用
 *
 * 【面试题42：Repository 模式 + 离线优先策略】
 *   离线优先：先返回本地缓存（Room），后台静默刷新网络数据。
 *   当前版本：先做网络直连，后续迭代加入 Room 本地缓存。
 *
 * 【面试题43：Retrofit + 协程 + Flow 封装】
 *   suspend 函数用 withContext(IO) 确保网络请求在 IO 线程执行。
 *   ViewModel 用 viewModelScope.launch 触发，自动管理生命周期。
 *
 * @param api 由 Hilt 注入的 NasApiService
 */
@Singleton
class FileRepository @Inject constructor(
    private val api: NasApiService
) {

    /**
     * 获取文件列表（分页）
     *
     * 返回 sealed class Result，UI 层用 when 处理三种状态：
     *   Loading → 显示骨架屏
     *   Success → 显示文件列表
     *   Error → 显示错误提示 + 重试按钮
     */
    suspend fun getFileList(page: Int, pageSize: Int = 20, fileType: String? = null): Result<List<NasFile>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getFileList(page, pageSize, fileType)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.code == 0 && body.data != null) {
                        Result.Success(body.data.files)
                    } else {
                        Result.Error(body.msg)
                    }
                } else {
                    Result.Error("请求失败: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("网络异常: ${e.message}", e)
            }
        }
    }
}