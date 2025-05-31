package com.example.taskmanager.data.model

enum class TaskStatus {
    TO_DO,
    IN_PROGRESS,
    DONE;

    companion object {
        fun getDisplayName(status: TaskStatus): String {
            return when (status) {
                TO_DO -> "К выполнению"
                IN_PROGRESS -> "В работе"
                DONE -> "Выполнено"
            }
        }
    }
}