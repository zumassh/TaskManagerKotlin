package com.example.taskmanager.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.local.TaskDao
import com.example.taskmanager.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dao: TaskDao
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _deadlineDates = dao.getAllDeadlines()
        .map { list ->
            list.mapNotNull { Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            }.toSet()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())
    val deadlineDates = _deadlineDates

    val tasksForDate = _selectedDate.flatMapLatest { date ->
        val zone = ZoneId.systemDefault()
        val start = date.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli() - 1
        dao.getTasksByDeadlineDay(start, end)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
}
