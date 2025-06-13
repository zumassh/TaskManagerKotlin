package com.example.taskmanager.presentation.calendar

import CustomCalendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.taskmanager.presentation.tasks.TaskItem
import com.example.taskmanager.presentation.tasks.TaskViewModel
import com.example.taskmanager.presentation.theme.mediumAccent
import com.example.taskmanager.presentation.theme.textColor
import com.example.taskmanager.ui.calendar.CalendarViewModel
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    navController: NavHostController,
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    taskViewModel: TaskViewModel = hiltViewModel()
) {
    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val events by calendarViewModel.deadlineDates.collectAsState()
    val tasks by calendarViewModel.tasksForDate.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Календарь",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(16.dp))
            CustomCalendar(
                selectedDate = selectedDate,
                onDateSelected = calendarViewModel::onDateSelected,
                eventDates = events
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Задачи на ${selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(8.dp))
            if (tasks.isEmpty()) {
                Text("Нет задач", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onToggleDone = { taskViewModel.updateTask(it) },
                            onClick = { navController.navigate("edit_task/${task.id}") }
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = { navController.navigate("add_task") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp)
                .background(color = mediumAccent, shape = CircleShape)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить задачу", tint = textColor)
        }
    }
}
