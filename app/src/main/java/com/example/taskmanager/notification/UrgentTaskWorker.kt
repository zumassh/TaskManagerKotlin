package com.example.taskmanager.worker


import NotificationHelper
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskmanager.data.local.TaskDatabase
import com.example.taskmanager.data.model.TaskUrgency
import kotlinx.coroutines.flow.first

class UrgentTaskWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val taskDao = TaskDatabase.getInstance(context).taskDao()
    private val notificationHelper = NotificationHelper(context)

    override suspend fun doWork(): Result {
        val tasks = taskDao.getAllTasks().first()
        tasks.filter { it.calculateUrgency() == TaskUrgency.URGENT }
            .forEach { notificationHelper.showUrgentNotification(it.id, it.title) }
        return Result.success()
    }
}
