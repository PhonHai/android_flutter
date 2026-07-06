package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.data.repository.FileRepository
import com.pomodoro.jetpack.network.NasFile
import com.pomodoro.jetpack.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 文件列表 ViewModel — 管理 NAS 文件列表的数据加载和分页
 *
 * ═══════════════════════════════════════════════════════════
 * 【完整数据流】
 * ═══════════════════════════════════════════════════════════
 *
 *   用户点击类型筛选 → FileViewModel.loadFiles("image")
 *                                │
 *                                ▼
 *   FileRepository.getFileList(page=1, fileType="image")
 *                                │
 *                                ▼
 *   NasApiService.getFileList(page=1, fileType="image")
 *   （Retrofit 动态代理生成 HTTP 请求）
 *                                │
 *                                ▼
 *   OkHttpClient 拦截器链
 *   ├── 日志拦截器（打印请求/响应）
 *   ├── MockNasInterceptor（返回模拟 JSON）
 *   └── Header 拦截器（加 Token）
 *                                │
 *                                ▼
 *   Gson 解析 JSON → NasFileListResponse
 *                                │
 *                                ▼
 *   FileRepository 包装为 Result.Success(...)
 *                                │
 *                                ▼
 *   FileViewModel._files.value = Result.Success(files)
 *                                │
 *                                ▼
 *   UI collectAsState() → when 分支 → 渲染文件列表
 *
 * ═══════════════════════════════════════════════════════════
 * 【sealed class Result 三种状态】
 * ═══════════════════════════════════════════════════════════
 *
 *   Result.Loading        → 正在请求中，UI 显示 CircularProgressIndicator
 *   Result.Success(data)  → 请求成功，UI 显示文件列表
 *   Result.Error(msg)     → 请求失败，UI 显示错误提示 + 重试按钮
 *
 *   对比传统 Java 写法：
 *     boolean isLoading = true;     // 三个独立变量
 *     List<File> files = null;
 *     String errorMsg = null;
 *     // 容易忘记判空，状态不一致
 *
 *   sealed class 写法：
 *     val files: StateFlow<Result<List<File>>>  // 一个变量，三种互斥状态
 *     // 编译器强制 when 穷举，不会漏处理
 *
 * ═══════════════════════════════════════════════════════════
 * 【注入链路】
 * ═══════════════════════════════════════════════════════════
 *
 *   Hilt AppModule
 *   ├── OkHttpClient → Retrofit → NasApiService → FileRepository → FileViewModel
 *   └────────────────────────────── @Provides ────── @Inject ────── @HiltViewModel
 *
 *   FileViewModel 不需要知道 OkHttp 的存在，只需要声明依赖 FileRepository。
 *   这就是依赖注入的好处：每一层只关心自己的依赖，容器负责组装。
 */
@HiltViewModel
class FileViewModel @Inject constructor(
    private val repository: FileRepository  // ← Hilt 自动注入
) : ViewModel() {

    // ═══════════════════════════════════════════════════════════
    // 核心状态：用一个 sealed class 同时表达"加载中/成功/失败"
    // ═══════════════════════════════════════════════════════════
    //
    // MutableStateFlow = 可写的状态流（ViewModel 内部写）
    // StateFlow = 只读的状态流（暴露给 UI 只读）
    // asStateFlow() = 把 Mutable 转为只读，防止 UI 外部修改
    //
    // 初始值 Result.Loading：打开页面立刻显示加载动画
    // init 块里会立刻调 loadFiles() 拉数据
    private val _files = MutableStateFlow<Result<List<NasFile>>>(Result.Loading)
    val files: StateFlow<Result<List<NasFile>>> = _files.asStateFlow()

    // ═══════════════════════════════════════════════════════════
    // 分页状态（ViewModel 内部维护，UI 不需要知道）
    // ═══════════════════════════════════════════════════════════
    //
    // currentPage：当前已加载到第几页
    //   loadFiles() 时重置为 1（重新加载第一页）
    //   loadMore() 时 +1（追加下一页）
    //
    // currentType：当前筛选的文件类型
    //   null = 全部文件
    //   "image" = 只看图片
    //   "video" = 只看视频
    //   等等
    private var currentPage = 1
    private var currentType: String? = null

    // ═══════════════════════════════════════════════════════════
    // init 块：ViewModel 创建后立即加载数据
    // ═══════════════════════════════════════════════════════════
    //
    // 对标传统 Java：在 Activity.onCreate() 里调 loadData()
    // 对标 Flutter：在 initState() 或 build() 里调 loadData()
    //
    // ViewModel 的 init 只在创建时执行一次，屏幕旋转不会重建 ViewModel。
    init {
        loadFiles()
    }

    /**
     * 加载 / 重新加载文件列表
     *
     * 调用时机：
     *   1. ViewModel 初始化（init 块自动调用）
     *   2. 用户切换类型筛选 Tab（"全部" → "图片"）
     *   3. 用户在错误页点击"重试"按钮
     *
     * 执行步骤：
     *   1. 记录当前筛选类型
     *   2. 重置页码为第 1 页
     *   3. 设置状态为 Loading → UI 显示加载动画
     *   4. viewModelScope.launch 启动协程 → 调用 FileRepository
     *   5. 协程返回后，_files.value 更新为 Success 或 Error
     *   6. StateFlow 通知 UI 重组 → when 分支自动切换
     *
     * @param type 文件类型筛选，null 表示全部
     */
    fun loadFiles(type: String? = null) {
        currentType = type   // 记下当前筛选类型，loadMore 时需要
        currentPage = 1      // 重置到第一页
        _files.value = Result.Loading  // 立刻显示加载动画

        // viewModelScope.launch：在 ViewModel 生命周期内启动协程
        // ViewModel 销毁时自动取消，不会泄漏
        // 对标传统 Java：AsyncTask.execute() + onDestroy 里 cancel()
        viewModelScope.launch {
            // suspend 函数在 IO 线程执行，不会阻塞主线程
            // 协程返回后，自动切回主线程更新 _files.value
            _files.value = repository.getFileList(
                page = currentPage,
                fileType = type
            )
        }
    }

    /**
     * 加载更多（分页追加）
     *
     * 调用时机：用户滚动 LazyColumn 到底部
     *
     * 执行步骤：
     *   1. 检查当前状态必须是 Success（不是 Loading 或 Error）
     *   2. 页码 +1
     *   3. 请求下一页数据
     *   4. 成功 → 把新数据追加到现有列表末尾
     *   5. 失败 → 页码回退（-1），通知 UI 显示错误
     *
     * 为什么不直接替换 _files.value？
     *   因为需要保留之前加载的数据，实现"下滑加载更多"效果。
     *   如果替换，之前的数据就丢失了。
     *
     * 为什么失败要回退页码？
     *   如果第 3 页请求失败但页码已经是 3，下次触底会再次请求第 4 页（跳过第 3 页）。
     *   回退页码确保下次重试还是请求第 3 页。
     */
    fun loadMore() {
        // 只有当前是 Success 状态才能加载更多
        // Loading 中说明上次请求还没返回
        // Error 说明上次请求失败了，用户应该点重试
        if (_files.value !is Result.Success) return

        currentPage++  // 页码 +1（比如从第 1 页变第 2 页）

        viewModelScope.launch {
            when (val result = repository.getFileList(
                page = currentPage,
                fileType = currentType  // 保持当前筛选类型不变
            )) {
                // ✅ 成功：追加新数据
                is Result.Success -> {
                    // 取出当前已有的数据
                    val existing = (_files.value as? Result.Success)?.data ?: emptyList()
                    // 把新数据拼到已有数据后面
                    _files.value = Result.Success(existing + result.data)
                    // 示例：第 1 页有 20 条，第 2 页返回 10 条
                    //        existing = [文件1..文件20], result.data = [文件21..文件30]
                    //        → [文件1..文件30] 共 30 条
                }

                // ❌ 失败：回退页码，通知 UI
                is Result.Error -> {
                    currentPage--  // 回退，下次重试还是请求这一页
                    _files.value = result  // 通知 UI 显示错误
                }

                // Loading 状态不会出现（Repository 已经返回了结果）
                is Result.Loading -> {}
            }
        }
    }
}