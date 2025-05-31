package com.example.taskmanager.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.TaskPriority
import com.example.taskmanager.data.model.TaskStatus
import com.example.taskmanager.data.model.TaskUrgency
import com.example.taskmanager.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
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

    data class TaskFilters(
        val priorities: Set<TaskPriority> = TaskPriority.values().toSet(),
        val urgencies: Set<TaskUrgency> = TaskUrgency.values().toSet(),
        val statuses: Set<TaskStatus> = TaskStatus.values().toSet()
    )

    data class TaskSort(
        val field: SortField = SortField.URGENCY,
        val order: SortOrder = SortOrder.DESCENDING
    )

    enum class SortField { URGENCY, PRIORITY, STATUS, CREATION_DATE }
    enum class SortOrder { ASCENDING, DESCENDING }

    fun getSortFieldDisplayName(field: SortField): String {
        return when (field) {
            SortField.URGENCY -> "Срочность"
            SortField.PRIORITY -> "Приоритет"
            SortField.STATUS -> "Статус"
            SortField.CREATION_DATE -> "Дата создания"
        }
    }

    fun getSortOrderDisplayName(order: SortOrder): String {
        return when (order) {
            SortOrder.ASCENDING -> "По возрастанию"
            SortOrder.DESCENDING -> "По убыванию"
        }
    }

    private val _filters = MutableStateFlow(TaskFilters())
    val filters: StateFlow<TaskFilters> = _filters.asStateFlow()

    private val _sort = MutableStateFlow(TaskSort())
    val sort: StateFlow<TaskSort> = _sort.asStateFlow()

    val filteredAndSortedTasks: StateFlow<List<Task>> = combine(
        _tasks,
        _filters,
        _sort
    ) { tasks, filters, sort ->
        tasks
            .filter { task ->
                filters.priorities.contains(task.priority) &&
                        filters.statuses.contains(task.status) &&
                        filters.urgencies.contains(task.calculateUrgency())
            }
            .sortedWith(getComparator(sort))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun updateFilters(
        priorities: Set<TaskPriority>? = null,
        urgencies: Set<TaskUrgency>? = null,
        statuses: Set<TaskStatus>? = null
    ) {
        _filters.update { current ->
            current.copy(
                priorities = priorities ?: current.priorities,
                urgencies = urgencies ?: current.urgencies,
                statuses = statuses ?: current.statuses
            )
        }
    }

    fun updateSort(field: SortField? = null, order: SortOrder? = null) {
        _sort.update { current ->
            current.copy(
                field = field ?: current.field,
                order = order ?: current.order
            )
        }
    }

    private fun getComparator(sort: TaskSort): Comparator<Task> {
        val comparator = when (sort.field) {
            SortField.URGENCY -> compareBy<Task> { it.calculateUrgency().ordinal }
            SortField.PRIORITY -> compareBy<Task> { it.priority.ordinal }
            SortField.STATUS -> compareBy<Task> { it.status.ordinal }
            SortField.CREATION_DATE -> compareBy<Task> { it.createdAt }
        }

        return if (sort.order == SortOrder.DESCENDING) {
            comparator.reversed()
        } else {
            comparator
        }
    }

    fun applyFilters() {
        _filters.value = _filters.value.copy()
        _sort.value = _sort.value.copy()
    }

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
            loadAllTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            loadAllTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            loadAllTasks()
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
