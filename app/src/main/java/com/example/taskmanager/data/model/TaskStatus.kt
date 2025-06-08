package com.example.taskmanager.data.model

enum class TaskStatus {
    DONE,
    IN_PROGRESS,
    TO_DO;

    companion object {
        fun getDisplayName(status: TaskStatus): String {
            return when (status) {
                DONE -> "Выполнено"
                IN_PROGRESS -> "В работе"
                TO_DO -> "К выполнению"
            }
        }
    }
}