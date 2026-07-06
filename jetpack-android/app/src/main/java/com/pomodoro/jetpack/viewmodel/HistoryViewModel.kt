package com.pomodoro.jetpack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pomodoro.jetpack.data.repository.TimerRepository
import com.pomodoro.jetpack.model.PomodoroEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * 历史记录 ViewModel — Hilt 注入 Repository
 *
 * 之前：直接依赖 PomodoroDao
 * 现在：通过 TimerRepository 获取数据（解耦）
 */
@HiltViewModel
class HistoryViewModel @Inject constructor(
    repository: TimerRepository
) : ViewModel() {

    val records: StateFlow<List<PomodoroEntity>> = repository.getAllRecords()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
}