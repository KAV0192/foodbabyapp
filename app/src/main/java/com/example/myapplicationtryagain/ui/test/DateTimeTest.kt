package com.example.myapplicationtryagain.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDatePicker() {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val datePickerState = rememberDatePickerState()
    Column {
        Button(onClick = { showDialog = true }) {
            Text("Выбрать дату")
        }
        Text("Выбрано: ${selectedDate?.toString() ?: "-"}")
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }
                    showDialog = false
                }) { Text("ОК") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) { Text("Отмена") }
            },
            text = { DatePicker(state = datePickerState) }
        )
    }
}
