package com.example.myapplicationtryagain.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.example.myapplicationtryagain.repository.FoodDiaryRepository
import com.example.myapplicationtryagain.ui.components.AddFoodEntryDialog
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyLogScreen() {
    val repository = remember { FoodDiaryRepository() }
    val userId = "test_user"
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val mealTypes = listOf("breakfast", "lunch", "dinner", "snack")
    val mealTitles = mapOf(
        "breakfast" to "Завтрак",
        "lunch" to "Обед",
        "dinner" to "Ужин",
        "snack" to "Перекус"
    )

    val scope = rememberCoroutineScope()
    val entriesMap = remember { mutableStateMapOf<String, List<FoodDiaryEntry>>() }

    // Для добавления записи
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedMealType by remember { mutableStateOf("breakfast") }

    LaunchedEffect(Unit) {
        mealTypes.forEach { mealType ->
            val entries = repository.getEntries(userId, date, mealType)
            entriesMap[mealType] = entries
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Дневник питания") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            mealTypes.forEach { mealType ->
                Text(
                    text = mealTitles[mealType] ?: mealType,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                val entries = entriesMap[mealType] ?: emptyList()

                if (entries.isEmpty()) {
                    Text("Нет записей", style = MaterialTheme.typography.bodySmall)
                } else {
                    entries.forEach { entry ->
                        Text("- ${entry.name} (${entry.amount})")
                    }
                }

                Button(
                    onClick = {
                        selectedMealType = mealType
                        showAddDialog = true
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Добавить")
                }

                Divider(thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        if (showAddDialog) {
            AddFoodEntryDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { name, amount ->
                    scope.launch {
                        val entry = FoodDiaryEntry(
                            productId = UUID.randomUUID().toString(),
                            name = name,
                            amount = amount
                        )
                        repository.addEntry(userId, date, selectedMealType, entry)
                        val updated = repository.getEntries(userId, date, selectedMealType)
                        entriesMap[selectedMealType] = updated
                        showAddDialog = false
                    }
                }
            )
        }
    }
}
