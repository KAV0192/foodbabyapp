package com.example.myapplicationtryagain.ui.diary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.data.CompactCalendar
import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.example.myapplicationtryagain.repository.FoodDiaryRepository
import com.example.myapplicationtryagain.ui.components.AddFoodEntryBottomSheet
import com.example.myapplicationtryagain.navigation.Screen
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FoodDiaryScreen(navController: NavController) {
    val repository = remember { FoodDiaryRepository() }
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var diaryEntries by remember { mutableStateOf<List<FoodDiaryEntry>>(emptyList()) }
    var showBottomSheet by remember { mutableStateOf(false) }

    val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

    LaunchedEffect(selectedDate) {
        diaryEntries = repository.getAllEntriesForDate("test_user", selectedDate.toString())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = Color(0xFFFFE0B2)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            CompactCalendar(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Записи на $formattedDate",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (diaryEntries.isEmpty()) {
                    item {
                        Text("Нет записей на выбранную дату")
                    }
                } else {
                    items(diaryEntries.size) { index ->
                        val entry = diaryEntries[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Прием пищи: ${entry.mealtype}")
                                Text("Продукт: ${entry.name}")
                                Text("Количество: ${entry.amount}")
                            }
                        }
                    }
                }
            }

            if (showBottomSheet) {
                AddFoodEntryBottomSheet(
                    selectedDate = selectedDate,
                    onDismiss = { showBottomSheet = false },
                    onProductClick = {
                        showBottomSheet = false
                        navController.navigate(Screen.SelectProduct.route)
                    },
                    onRecipeClick = {
                        showBottomSheet = false
                        navController.navigate(Screen.SelectRecipe.route)
                    },
                    onReactionClick = {
                        // TODO: переход на экран реакций
                    }
                )
            }
        }
    }
}
