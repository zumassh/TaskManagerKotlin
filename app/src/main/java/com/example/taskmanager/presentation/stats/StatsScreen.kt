package com.example.taskmanager.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.presentation.tasks.TaskViewModel
import androidx.compose.runtime.getValue

@Composable
fun StatsScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val allTasks = tasks.size
    val todoTasks = viewModel.tasks.collectAsState().value.count { it.status.name == "TO_DO" }
    val tasksInProgress = viewModel.tasks.collectAsState().value.count { it.status.name == "IN_PROGRESS" }
    val completedTasks = viewModel.tasks.collectAsState().value.count { it.status.name == "DONE" }
    val overdueTasks = viewModel.tasks.collectAsState().value.count { it.isOverdue() }
    val noDeadlineTasks = viewModel.tasks.collectAsState().value.count { it.deadline == null }
    val urgentTasks = viewModel.tasks.collectAsState().value.count { it.calculateUrgency().name == "URGENT" }
    val notUrgentTasks = viewModel.tasks.collectAsState().value.count { it.calculateUrgency().name == "NOT_URGENT" }
    val veryImportantTasks = viewModel.tasks.collectAsState().value.count { it.priority.name == "VERY_HIGH" }
    val importantTasks = viewModel.tasks.collectAsState().value.count { it.priority.name == "HIGH" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = "Статистика",
            style = MaterialTheme.typography.titleLarge
        )

        StatBox(text = "Всего задач: $allTasks", backgroundColor = Color(0xFFFFFEC4))
        StatBox(text = "Задач к выполнению: $todoTasks", backgroundColor = Color(0xFFE0FFF8))
        StatBox(text = "Задач в работе: $tasksInProgress", backgroundColor = Color(0xFFC8FFE0))
        StatBox(text = "Выполнено задач: $completedTasks", backgroundColor = Color(0xFFC8F7C5))
        StatBox(text = "Просроченных задач: $overdueTasks", backgroundColor = Color(0xFFFFD2D2))
        StatBox(text = "Срочных задач: $urgentTasks", backgroundColor = Color(0xFFFFD1B3))
        StatBox(text = "Очень важных задач: $veryImportantTasks", backgroundColor = Color(0xFFF0C8FF))
    }
}

@Composable
fun StatBox(
    text: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF222244),
            fontSize = 16.sp
        )
    }
}
