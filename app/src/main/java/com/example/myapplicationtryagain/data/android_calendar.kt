@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplicationtryagain.data

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun CompactCalendar(
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, Color.LightGray)
            .padding(12.dp)
    ) {
        // Заголовок
        CalendarHeader(
            currentMonth = currentMonth,
            onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
            onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Дни недели
        WeekDaysHeader()

        Spacer(modifier = Modifier.height(4.dp))

        // Сетка дней
        CalendarGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected
        )
    }
}

@Composable
private fun CalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Предыдущий месяц"
            )
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Следующий месяц"
            )
        }
    }
}

@Composable
private fun WeekDaysHeader() {
    val weekDays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        weekDays.forEach { day ->
            Text(
                text = day,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
    val daysInMonth = currentMonth.lengthOfMonth()
    val today = LocalDate.now()

    val calendarDays = mutableListOf<CalendarDay>()

    repeat(firstDayOfWeek - 1) {
        calendarDays.add(CalendarDay.Empty)
    }

    for (day in 1..daysInMonth) {
        val date = currentMonth.atDay(day)
        calendarDays.add(CalendarDay.Day(date))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier
            .fillMaxWidth()
            .height((26.dp + 4.dp) * 6), // 6 строк максимум
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(calendarDays) { day ->
            when (day) {
                is CalendarDay.Empty -> Box(modifier = Modifier.size(26.dp))
                is CalendarDay.Day -> DayCell(
                    date = day.date,
                    isSelected = day.date == selectedDate,
                    isToday = day.date == today,
                    onClick = { onDateSelected(day.date) },
                    size = 26.dp
                )
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit,
    size: Dp
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> Color(0xFFFFE0B2) // персиковый для сегодня
        else -> Color.Transparent
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimary
        isToday -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
    }
}

sealed class CalendarDay {
    data object Empty : CalendarDay()
    data class Day(val date: LocalDate) : CalendarDay()
}
