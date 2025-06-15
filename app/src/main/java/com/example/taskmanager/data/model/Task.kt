package com.example.taskmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
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
            timeLeft <= 24 * 60 * 60 * 1000 -> TaskUrgency.URGENT   // меньше суток
            else -> TaskUrgency.NOT_URGENT
        }
    }

    fun isOverdue(currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        return deadline != null && deadline < currentTimeMillis && !isDone
    }
}


val Task.isDone: Boolean
    get() = status == TaskStatus.DONE

data class TaskDto(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String? = null,
    val deadline: Long? = null,
    val status: String = "TO_DO",
    val priority: String = "MEDIUM",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toTask(): Task = Task(
        id = id,
        title = title,
        description = description,
        deadline = deadline,
        status = TaskStatus.valueOf(status),
        priority = TaskPriority.valueOf(priority),
        createdAt = createdAt
    )

    companion object {
        fun fromTask(task: Task): TaskDto = TaskDto(
            id = task.id,
            title = task.title,
            description = task.description,
            deadline = task.deadline,
            status = task.status.name,
            priority = task.priority.name,
            createdAt = task.createdAt
        )
    }
}
