package com.example.taskmanager.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.presentation.tasks.TaskViewModel

@Composable
fun StatsScreen(
    viewModel: TaskViewModel = hiltViewModel()
) {
    val totalTasks = viewModel.pendingTasks.collectAsState().value
    val tasksInProgress = viewModel.tasks.collectAsState().value.count { it.status.name == "IN_PROGRESS" }
    val completedTasks = viewModel.completedTasks.collectAsState().value
    val overdueTasks = viewModel.tasks.collectAsState().value.count { it.isOverdue() }
    val noDeadlineTasks = viewModel.tasks.collectAsState().value.count { it.deadline == null }
    val criticalTasks = viewModel.tasks.collectAsState().value.count { it.calculateUrgency().name == "CRITICAL" }
    val urgentTasks = viewModel.tasks.collectAsState().value.count { it.calculateUrgency().name == "URGENT" }
    val veryImportantTasks = viewModel.tasks.collectAsState().value.count { it.priority.name == "VERY_IMPORTANT" }
    val importantTasks = viewModel.tasks.collectAsState().value.count { it.priority.name == "IMPORTANT" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Статистика",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF222244),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        StatBox(text = "Задач к выполнению: $totalTasks", backgroundColor = Color(0xFFE0FFF8))
        StatBox(text = "Задач в работе: $tasksInProgress", backgroundColor = Color(0xFFC8FFE0))
        StatBox(text = "Выполнено задач: $completedTasks", backgroundColor = Color(0xFFC8F7C5))
        StatBox(text = "Критических задач: $criticalTasks", backgroundColor = Color(0xFFFFC9C9))
        StatBox(text = "Срочных задач: $urgentTasks", backgroundColor = Color(0xFFFFD1B3))
        StatBox(text = "Бессрочных задач: $noDeadlineTasks", backgroundColor = Color(0xFFCCE7FF))
        StatBox(text = "Очень важных задач: $veryImportantTasks", backgroundColor = Color(0xFFF0C8FF))
        StatBox(text = "Важных задач: $importantTasks", backgroundColor = Color(0xFFE8F8C8))
        StatBox(text = "Просроченные задачи: $overdueTasks", backgroundColor = Color(0xFFFFD2D2))
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
