package com.example.taskmanager.presentation.tasks


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.presentation.theme.mediumAccent
import com.example.taskmanager.presentation.theme.textColor

@Composable
fun EditTaskScreen(
    task: Task,
    onSave: (Task) -> Unit,
    onDelete: () -> Unit,
    onBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description ?: "") }
    var deadline by remember { mutableStateOf(task.deadline) }
    var selectedPriority by remember { mutableStateOf(task.priority) }
    var selectedStatus by remember { mutableStateOf(task.status) }
    val context = LocalContext.current
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Изменить задачу", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = textColor,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor,
                focusedIndicatorColor = textColor,
                unfocusedIndicatorColor = textColor,
                focusedContainerColor = mediumAccent,
                unfocusedContainerColor = mediumAccent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Описание") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                cursorColor = textColor,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor,
                focusedIndicatorColor = textColor,
                unfocusedIndicatorColor = textColor,
                focusedContainerColor = mediumAccent,
                unfocusedContainerColor = mediumAccent
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                showDateTimePicker(context) { millis -> deadline = millis }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = mediumAccent,
                contentColor = textColor
            )
        ) {
            Text(text = deadline?.let { formatter.format(Date(it)) } ?: "Выбрать дедлайн")
        }

        Spacer(modifier = Modifier.height(8.dp))

        PriorityDropdown(selected = selectedPriority, onSelected = { selectedPriority = it })
        Spacer(modifier = Modifier.height(8.dp))
        StatusDropdown(selected = selectedStatus, onSelected = { selectedStatus = it })

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onSave(
                    task.copy(
                        title = title,
                        description = if (description.isNotBlank()) description else null,
                        deadline = deadline,
                        status = selectedStatus,
                        priority = selectedPriority
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = mediumAccent, contentColor = textColor)
        ) {
            Text("Сохранить")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                viewModel.deleteTask(task)
                onDelete()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0xFFDD4444),
                contentColor = Color.White
            )
        ) {
            Text("Удалить задачу")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = mediumAccent,
                contentColor = textColor
            )
        ) {
            Text("Назад")
        }
    }
}

