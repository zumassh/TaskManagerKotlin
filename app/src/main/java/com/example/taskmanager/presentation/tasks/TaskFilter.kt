import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.taskmanager.presentation.tasks.TaskViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.unit.dp
import com.example.taskmanager.data.model.TaskPriority
import com.example.taskmanager.data.model.TaskStatus
import com.example.taskmanager.data.model.TaskUrgency
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.ui.Alignment
import com.example.taskmanager.presentation.theme.mediumAccent
import com.example.taskmanager.presentation.theme.textColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFilter(
    viewModel: TaskViewModel,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    val currentSort by viewModel.sort.collectAsState()
    val currentFilters by viewModel.filters.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = mediumAccent
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Сортировать по...",
                style = MaterialTheme.typography.titleMedium.copy(color = textColor),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Column {
                CheckboxWithText(
                    text = "важности",
                    checked = currentSort.field == TaskViewModel.SortField.PRIORITY,
                    onCheckedChange = {
                        viewModel.updateSort(field = TaskViewModel.SortField.PRIORITY)
                    }
                )
                CheckboxWithText(
                    text = "срочности",
                    checked = currentSort.field == TaskViewModel.SortField.URGENCY,
                    onCheckedChange = {
                        viewModel.updateSort(field = TaskViewModel.SortField.URGENCY)
                    }
                )
                CheckboxWithText(
                    text = "статусу",
                    checked = currentSort.field == TaskViewModel.SortField.STATUS,
                    onCheckedChange = {
                        viewModel.updateSort(field = TaskViewModel.SortField.STATUS)
                    }
                )
            }

            Column(modifier = Modifier.padding(top = 8.dp)) {
                CheckboxWithText(
                    text = "убыванию",
                    checked = currentSort.order == TaskViewModel.SortOrder.DESCENDING,
                    onCheckedChange = {
                        viewModel.updateSort(order = TaskViewModel.SortOrder.DESCENDING)
                    }
                )
                CheckboxWithText(
                    text = "возрастанию",
                    checked = currentSort.order == TaskViewModel.SortOrder.ASCENDING,
                    onCheckedChange = {
                        viewModel.updateSort(order = TaskViewModel.SortOrder.ASCENDING)
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Фильтры",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "важность",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    CheckboxWithText(
                        text = "очень высокая",
                        checked = currentFilters.priorities.contains(TaskPriority.VERY_HIGH),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                priorities = if (checked) {
                                    currentFilters.priorities + TaskPriority.VERY_HIGH
                                } else {
                                    currentFilters.priorities - TaskPriority.VERY_HIGH
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "высокая",
                        checked = currentFilters.priorities.contains(TaskPriority.HIGH),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                priorities = if (checked) {
                                    currentFilters.priorities + TaskPriority.HIGH
                                } else {
                                    currentFilters.priorities - TaskPriority.HIGH
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "средняя",
                        checked = currentFilters.priorities.contains(TaskPriority.MEDIUM),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                priorities = if (checked) {
                                    currentFilters.priorities + TaskPriority.MEDIUM
                                } else {
                                    currentFilters.priorities - TaskPriority.MEDIUM
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "низкая",
                        checked = currentFilters.priorities.contains(TaskPriority.LOW),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                priorities = if (checked) {
                                    currentFilters.priorities + TaskPriority.LOW
                                } else {
                                    currentFilters.priorities - TaskPriority.LOW
                                }
                            )
                        }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "срочность",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    CheckboxWithText(
                        text = "критическая",
                        checked = currentFilters.urgencies.contains(TaskUrgency.CRITICAL),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                urgencies = if (checked) {
                                    currentFilters.urgencies + TaskUrgency.CRITICAL
                                } else {
                                    currentFilters.urgencies - TaskUrgency.CRITICAL
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "высокая",
                        checked = currentFilters.urgencies.contains(TaskUrgency.HIGH),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                urgencies = if (checked) {
                                    currentFilters.urgencies + TaskUrgency.HIGH
                                } else {
                                    currentFilters.urgencies - TaskUrgency.HIGH
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "средняя",
                        checked = currentFilters.urgencies.contains(TaskUrgency.MEDIUM),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                urgencies = if (checked) {
                                    currentFilters.urgencies + TaskUrgency.MEDIUM
                                } else {
                                    currentFilters.urgencies - TaskUrgency.MEDIUM
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "без срока",
                        checked = currentFilters.urgencies.contains(TaskUrgency.WITHOUT_DEADLINE),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                urgencies = if (checked) {
                                    currentFilters.urgencies + TaskUrgency.WITHOUT_DEADLINE
                                } else {
                                    currentFilters.urgencies - TaskUrgency.WITHOUT_DEADLINE
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "просроченная",
                        checked = currentFilters.urgencies.contains(TaskUrgency.OVERDUE),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                urgencies = if (checked) {
                                    currentFilters.urgencies + TaskUrgency.OVERDUE
                                } else {
                                    currentFilters.urgencies - TaskUrgency.OVERDUE
                                }
                            )
                        }
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "статус",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    CheckboxWithText(
                        text = "к выполнению",
                        checked = currentFilters.statuses.contains(TaskStatus.TO_DO),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                statuses = if (checked) {
                                    currentFilters.statuses + TaskStatus.TO_DO
                                } else {
                                    currentFilters.statuses - TaskStatus.TO_DO
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "в работе",
                        checked = currentFilters.statuses.contains(TaskStatus.IN_PROGRESS),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                statuses = if (checked) {
                                    currentFilters.statuses + TaskStatus.IN_PROGRESS
                                } else {
                                    currentFilters.statuses - TaskStatus.IN_PROGRESS
                                }
                            )
                        }
                    )
                    CheckboxWithText(
                        text = "выполнено",
                        checked = currentFilters.statuses.contains(TaskStatus.DONE),
                        onCheckedChange = { checked ->
                            viewModel.updateFilters(
                                statuses = if (checked) {
                                    currentFilters.statuses + TaskStatus.DONE
                                } else {
                                    currentFilters.statuses - TaskStatus.DONE
                                }
                            )
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = onDismiss, colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = textColor,
                    containerColor = mediumAccent
                )) {
                    Text("Отмена")
                }
                Button(onClick = {
                    viewModel.applyFilters()
                    onApply()
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = textColor,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )) {
                    Text("Применить")
                }
            }
        }
    }
}

@Composable
private fun CheckboxWithText(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            colors = CheckboxDefaults.colors(
                checkedColor = textColor,
                uncheckedColor = textColor.copy(alpha = 0.6f)
            ),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(color = textColor),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}