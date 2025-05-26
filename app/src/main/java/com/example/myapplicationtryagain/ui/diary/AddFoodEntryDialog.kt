package com.example.myapplicationtryagain.ui.diary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import com.example.myapplicationtryagain.model.FoodDiaryEntry
import java.util.UUID
import com.example.myapplicationtryagain.ui.diary.AddFoodEntryDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodEntryDialog(
    selectedDate: LocalDate,
    onAdd: (FoodDiaryEntry) -> Unit,
    onDismiss: () -> Unit
) {
    var mealtype by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val entry = FoodDiaryEntry(
                    id = "diary_${UUID.randomUUID()}",
                    date = selectedDate.toString(),
                    mealtype = mealtype,
                    productId = "", // если нужно
                    name = name,
                    amount = amount
                )
                onAdd(entry)
                onDismiss()
            }) {
                Text("Добавить")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        title = { Text("Новая запись в дневник") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = mealtype,
                    onValueChange = { mealtype = it },
                    label = { Text("Тип приема пищи") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название продукта") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Количество") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
