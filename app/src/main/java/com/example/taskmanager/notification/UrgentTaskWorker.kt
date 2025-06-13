import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.TaskUrgency
import com.example.taskmanager.data.repository.TaskRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class UrgentTaskWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TaskRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val tasks = repository.getAllTasks().first()

        tasks.filter { task: Task -> task.calculateUrgency() == TaskUrgency.URGENT }
            .forEach { task: Task ->
                notificationHelper.showUrgentNotification(task.id, task.title)
            }

        return Result.success()
    }
}
