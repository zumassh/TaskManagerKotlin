package com.example.taskmanager.data.model

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    VERY_HIGH;

    companion object {
        fun getDisplayName(priority: TaskPriority): String {
            return when (priority) {
                LOW -> "Низкая"
                MEDIUM -> "Средняя"
                HIGH -> "Высокая"
                VERY_HIGH -> "Очень высокая"
            }
        }
    }
}
