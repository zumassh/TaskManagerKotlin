package com.example.taskmanager.data.local

import androidx.room.*
import com.example.taskmanager.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY createdAt DESC")
    fun getTasksByStatus(status: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE priority = :priority ORDER BY createdAt DESC")
    fun getTasksByPriority(priority: String): Flow<List<Task>>

    @Query(
        """
      SELECT * FROM tasks
      WHERE deadline BETWEEN :start AND :end
      ORDER BY createdAt DESC
      """
    )
    fun getTasksByDeadlineDay(start: Long, end: Long): Flow<List<Task>>

    @Query("SELECT deadline FROM tasks WHERE deadline IS NOT NULL")
    fun getAllDeadlines(): Flow<List<Long>>

}
