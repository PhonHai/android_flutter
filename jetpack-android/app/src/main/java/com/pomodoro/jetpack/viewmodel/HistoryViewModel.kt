package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.data.PomodoroDao
import com.pomodoro.jetpack.model.PomodoroEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * 历史记录 ViewModel
 *
 * 【传统 Java 思维 → Jetpack 映射】
 *
 * 传统 Java 写法（HistoryActivity.java）：
 *   private void loadHistory() {
 *       List<PomodoroRecord> records = DatabaseHelper.getInstance(this).getAllRecords();
 *       SimpleAdapter adapter = new SimpleAdapter(this, dataList, ...);
 *       listView.setAdapter(adapter);
 *   }
 *   问题：数据变了不会自动刷新，要手动再调 loadHistory()
 *
 * Jetpack 写法：
 *   dao.getAllRecords() 返回 Flow<List<PomodoroEntity>>
 *   stateIn() 把 Flow 转成 StateFlow（有初始值，可缓存最新值）
 *   UI collectAsState() 后，数据库一变 → Flow 发射 → UI 自动更新
 *
 * 对比 Flutter 版：
 *   Flutter 没有自动更新机制，需要手动调 setState 或用 StreamBuilder
 */
class HistoryViewModel(private val dao: PomodoroDao) : ViewModel() {

    // stateIn 参数说明：
    //   viewModelScope = 协程作用域（ViewModel 销毁时自动取消）
    //   SharingStarted.WhileSubscribed(5000) = 有订阅者时才开始收集，5秒后停止
    //   emptyList() = 初始值
    val records: StateFlow<List<PomodoroEntity>> = dao.getAllRecords()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}
