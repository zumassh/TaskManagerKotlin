import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    eventDates: Set<LocalDate>
) {
    val month = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }

    val days by remember(month.value) {
        mutableStateOf(generateMonthDays(month.value))
    }

    Column {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { month.value = month.value.minusMonths(1) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Prev")
            }
            Text(month.value.format(DateTimeFormatter.ofPattern("LLLL yyyy")), style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = { month.value = month.value.plusMonths(1) }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            days.forEach { date ->
                item {
                    val isSelected = date == selectedDate
                    val hasEvent = eventDates.contains(date)
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable { onDateSelected(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = date.dayOfMonth.toString(), color = if (isSelected) Color.White else Color.Black)
                            if (hasEvent) {
                                Box(
                                    Modifier
                                        .padding(top = 2.dp)
                                        .size(4.dp)
                                        .background(Color.Red, CircleShape)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun generateMonthDays(firstDay: LocalDate): List<LocalDate> {
    val days = mutableListOf<LocalDate>()
    val startDow = firstDay.dayOfWeek.value % 7
    for (i in startDow downTo 1) {
        days.add(firstDay.minusDays(i.toLong()))
    }
    for (i in 0 until firstDay.lengthOfMonth()) {
        days.add(firstDay.plusDays(i.toLong()))
    }
    return days
}
