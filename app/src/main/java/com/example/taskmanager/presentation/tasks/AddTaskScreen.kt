package com.example.taskmanager.presentation.tasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.model.Task
import com.example.taskmanager.data.model.TaskPriority
import com.example.taskmanager.data.model.TaskStatus
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskmanager.presentation.theme.mediumAccent
import com.example.taskmanager.presentation.theme.textColor

@Composable
fun AddTaskScreen(
    onSave: (Task) -> Unit,
    onBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var deadline by remember { mutableStateOf<Long?>(null) }
    val context = LocalContext.current
    val formatter = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var selectedStatus by remember { mutableStateOf(TaskStatus.TO_DO) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Добавить задачу", style = MaterialTheme.typography.titleLarge)

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
                val task = Task(
                    title = title,
                    description = if (description.isNotBlank()) description else null,
                    deadline = deadline,
                    status = selectedStatus,
                    priority = selectedPriority
                )
                onSave(task)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = mediumAccent, contentColor = textColor)
        ) {
            Text("Сохранить")
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

@Composable
fun PriorityDropdown(selected: TaskPriority, onSelected: (TaskPriority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = mediumAccent, contentColor = textColor)
        ) {
            Text("Важность: ${selected.name}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            TaskPriority.values().forEach {
                DropdownMenuItem(
                    text = { Text(it.name, modifier = Modifier.fillMaxWidth()) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun StatusDropdown(selected: TaskStatus, onSelected: (TaskStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = mediumAccent, contentColor = textColor)
        ) {
            Text("Статус: ${selected.name}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            TaskStatus.values().forEach {
                DropdownMenuItem(
                    text = { Text(it.name, modifier = Modifier.fillMaxWidth()) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

fun showDateTimePicker(context: Context, onDateTimeSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance()

    DatePickerDialog(context, { _, year, month, day ->
        TimePickerDialog(context, { _, hour, minute ->
            calendar.set(year, month, day, hour, minute)
            onDateTimeSelected(calendar.timeInMillis)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
}

