package com.example.taskmanager.data.model

enum class TaskPriority {
    VERY_HIGH,
    HIGH,
    MEDIUM,
    LOW;

    companion object {
        fun getDisplayName(priority: TaskPriority): String {
            return when (priority) {
                VERY_HIGH -> "Очень высокая"
                HIGH -> "Высокая"
                MEDIUM -> "Средняя"
                LOW -> "Низкая"
            }
        }
    }
}
