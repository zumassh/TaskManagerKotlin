package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.TaskDao
import com.example.taskmanager.data.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    fun getTasksByStatus(status: String): Flow<List<Task>> {
        return taskDao.getTasksByStatus(status)
    }

    fun getTasksByPriority(priority: String): Flow<List<Task>> {
        return taskDao.getTasksByPriority(priority)
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}
