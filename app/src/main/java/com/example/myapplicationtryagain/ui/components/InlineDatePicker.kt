package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InlineDatePicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateMillis = remember { mutableLongStateOf(selectedDate.toEpochDay() * 24 * 60 * 60 * 1000) }

    DatePicker(
        state = rememberDatePickerState(
            initialSelectedDateMillis = dateMillis.longValue
        ),
        modifier = modifier,
        showModeToggle = false, // скрываем переключение между календарём и вводом даты
        title = null
    )

    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )

    LaunchedEffect(state.selectedDateMillis) {
        state.selectedDateMillis?.let { millis ->
            val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
            if (date != selectedDate) {
                onDateSelected(date)
            }
        }
    }
}
