package com.example.taskmanager.data.model

enum class TaskUrgency {
    CRITICAL,
    HIGH,
    MEDIUM,
    WITHOUT_DEADLINE,
    OVERDUE;

    companion object {
        fun getDisplayName(urgency: TaskUrgency): String {
            return when (urgency) {
                CRITICAL -> "Критическая"
                HIGH -> "Высокая"
                MEDIUM -> "Средняя"
                WITHOUT_DEADLINE -> "Без дедлайна"
                OVERDUE -> "Просроченная"
            }
        }
    }
}
