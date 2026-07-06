package com.pomodoro.jetpack.data.repository

import com.pomodoro.jetpack.data.PomodoroDao
import com.pomodoro.jetpack.model.PomodoroEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 番茄钟记录 Repository
 *
 * 【面试题42：Repository 模式】
 *   Repository 是数据层的统一入口，封装数据来源（本地/网络/缓存）。
 *   ViewModel 不直接依赖 DAO，而是通过 Repository 获取数据。
 *   好处：
 *     1. 单一数据源原则：ViewModel 不知道数据来自 Room 还是网络
 *     2. 可测试：Mock Repository 比 Mock DAO 更简单（Repository 是纯接口）
 *     3. 可替换：未来换数据源只需改 Repository 实现
 *
 * 项目架构变化：
 *   之前：ViewModel → DAO（直接依赖）
 *   现在：ViewModel → Repository → DAO（解耦一层）
 */
@Singleton
class TimerRepository @Inject constructor(
    private val dao: PomodoroDao
) {
    fun getAllRecords(): Flow<List<PomodoroEntity>> = dao.getAllRecords()

    suspend fun insert(record: PomodoroEntity) = dao.insert(record)
}