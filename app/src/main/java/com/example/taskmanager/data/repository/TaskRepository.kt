package com.example.taskmanager.data.repository

import android.util.Log
import com.example.taskmanager.data.local.TaskDao
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.TaskDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    private val firestore = FirebaseFirestore.getInstance()
    private val tasksCollection = firestore.collection("tasks")

    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task)
        syncTaskToFirestore(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
        syncTaskToFirestore(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
        deleteTaskFromFirestore(task)
    }

    private fun syncTaskToFirestore(task: Task) {
        val dto = TaskDto.fromTask(task)
        tasksCollection.document(task.id.toString())
            .set(dto)
    }

    private fun deleteTaskFromFirestore(task: Task) {
        tasksCollection.document(task.id.toString())
            .delete()
    }

    fun fetchTasksFromFirestore() {
        tasksCollection.get()
            .addOnSuccessListener { snapshot ->
                val tasks = snapshot.documents.mapNotNull {
                    it.toObject(TaskDto::class.java)?.toTask()
                }

                CoroutineScope(Dispatchers.IO).launch {
                    taskDao.insertTasks(tasks)
                }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Ошибка загрузки задач", it)
            }
    }
}
