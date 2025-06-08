package com.example.taskmanager.data.model

enum class TaskUrgency {
    WITHOUT_DEADLINE,
    NOT_URGENT,
    URGENT,
    OVERDUE;

    companion object {
        fun getDisplayName(urgency: TaskUrgency): String {
            return when (urgency) {
                WITHOUT_DEADLINE -> "Без срока"
                NOT_URGENT -> "Не срочная"
                URGENT -> "Срочная"
                OVERDUE -> "Просроченная"
            }
        }
    }
}

