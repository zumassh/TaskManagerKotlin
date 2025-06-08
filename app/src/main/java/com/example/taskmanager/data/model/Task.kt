package com.example.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String?,
    val deadline: Long?,
    val status: TaskStatus = TaskStatus.TO_DO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val createdAt: Long = System.currentTimeMillis(),
) {
    fun calculateUrgency(currentTimeMillis: Long = System.currentTimeMillis()): TaskUrgency {
        if (deadline == null) return TaskUrgency.WITHOUT_DEADLINE
        val timeLeft = deadline - currentTimeMillis

        return when {
            timeLeft < 0 -> TaskUrgency.OVERDUE
            timeLeft <= 24 * 60 * 60 * 1000 -> TaskUrgency.URGENT   // меньше суток (мс)
            else -> TaskUrgency.NOT_URGENT
        }
    }

    fun isOverdue(currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        return deadline != null && deadline < currentTimeMillis && !isDone
    }
}


val Task.isDone: Boolean
    get() = status == TaskStatus.DONE
