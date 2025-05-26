package com.example.myapplicationtryagain.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = { /* ничего не делаем — календарь не скрывается */ },
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let {
                    val selected = Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(selected)
                }
            }) {
                Text("Выбрать")
            }
        },
        dismissButton = {}
    ) {
        DatePicker(state = datePickerState)
    }
}
