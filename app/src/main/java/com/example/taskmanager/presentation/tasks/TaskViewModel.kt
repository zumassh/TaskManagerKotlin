package com.example.taskmanager.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.TaskPriority
import com.example.taskmanager.data.model.TaskStatus
import com.example.taskmanager.data.model.TaskUrgency
import com.example.taskmanager.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _totalTasks = MutableStateFlow(0)
    val totalTasks: StateFlow<Int> = _totalTasks.asStateFlow()

    private val _completedTasks = MutableStateFlow(0)
    val completedTasks: StateFlow<Int> = _completedTasks.asStateFlow()

    private val _pendingTasks = MutableStateFlow(0)
    val pendingTasks: StateFlow<Int> = _pendingTasks.asStateFlow()

    init {
        loadAllTasks()
        loadStats()
    }

    fun loadAllTasks() {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                _tasks.value = taskList
            }
        }
    }

    fun filterTasksByStatus(status: TaskStatus) {
        viewModelScope.launch {
            repository.getTasksByStatus(status.name).collect { filteredList ->
                _tasks.value = filteredList
            }
        }
    }

    fun filterTasksByPriority(priority: TaskPriority) {
        viewModelScope.launch {
            repository.getTasksByPriority(priority.name).collect { filteredList ->
                _tasks.value = filteredList
            }
        }
    }

    fun filterTasksByUrgency(urgency: TaskUrgency) {
        viewModelScope.launch {
            repository.getAllTasks().collect { taskList ->
                val filtered = taskList.filter { it.calculateUrgency() == urgency }
                _tasks.value = filtered
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            loadAllTasks() // перезагружаем список
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadAllTasks() // перезагружаем список
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadAllTasks() // перезагружаем список
        }
    }

    private fun loadStats() {
        viewModelScope.launch {
            repository.getTotalTasksCount().collect { count ->
                _totalTasks.value = count
            }
        }
        viewModelScope.launch {
            repository.getCompletedTasksCount().collect { count ->
                _completedTasks.value = count
            }
        }
        viewModelScope.launch {
            repository.getPendingTasksCount().collect { count ->
                _pendingTasks.value = count
            }
        }
    }
}
