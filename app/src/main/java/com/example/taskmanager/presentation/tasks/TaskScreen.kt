package com.example.taskmanager.presentation.tasks

import TaskFilter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.taskmanager.R
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.TaskStatus
import com.example.taskmanager.data.model.TaskUrgency
import com.example.taskmanager.data.model.isDone
import com.example.taskmanager.presentation.theme.mediumAccent
import com.example.taskmanager.presentation.theme.textColor

@Composable
fun TaskScreen(navController: NavHostController, viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.filteredAndSortedTasks.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Задачи",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск задач") },
                modifier = Modifier
                    .weight(0.5f)
                    .height(56.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = mediumAccent,
                    unfocusedContainerColor = mediumAccent,
                    cursorColor = textColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    unfocusedPlaceholderColor = textColor.copy(alpha = 0.6f)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { showFilters = true },
                modifier = Modifier
                    .background(
                        color = mediumAccent,
                        shape = RoundedCornerShape(12.dp),
                    ),
            )  {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = "Фильтр",
                    modifier = Modifier
                        .width(40.dp)
                        .height(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { navController.navigate("add_task")},
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = mediumAccent,
                        shape = CircleShape
                    )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить задачу", tint = textColor)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tasks.filter { it.title.contains(searchQuery, ignoreCase = true) }) { task ->
                TaskItem(
                    task = task,
                    onToggleDone = { viewModel.updateTask(it) },
                    onClick = { navController.navigate("edit_task/${task.id}") }
                )
            }
        }
    }
    if (showFilters) {
        TaskFilter(
            viewModel = viewModel,
            onApply = {
                showFilters = false
            },
            onDismiss = { showFilters = false }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleDone: (Task) -> Unit,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        task.isDone -> Color(0xFFDDDDDD)
        task.calculateUrgency() == TaskUrgency.OVERDUE -> Color(0xFFEE99B7)
        task.calculateUrgency() == TaskUrgency.URGENT -> Color(0xFFFEBDA1)
        task.calculateUrgency() == TaskUrgency.NOT_URGENT -> Color(0xFFB9FBE4)
        task.calculateUrgency() == TaskUrgency.WITHOUT_DEADLINE -> Color(0xFFB7E6F8)
        else -> mediumAccent
    }

    val textStyle = if (task.isDone) {
        MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough)
    } else {
        MaterialTheme.typography.titleMedium
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = textStyle,
                    color = textColor
                )
                Text(
                    text = task.description ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = if (task.isDone) TextDecoration.LineThrough else null
                    ),
                    color = textColor
                )
            }

            Checkbox(
                checked = task.isDone,
                onCheckedChange = { checked ->
                    val updatedTask = task.copy(
                        status = if (checked) TaskStatus.DONE else TaskStatus.TO_DO
                    )
                    onToggleDone(updatedTask)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = textColor,
                    uncheckedColor = textColor
                )
            )
        }
    }
}
