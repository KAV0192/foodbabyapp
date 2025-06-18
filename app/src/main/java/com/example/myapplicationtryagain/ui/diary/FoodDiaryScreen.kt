package com.example.myapplicationtryagain.ui.diary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplicationtryagain.data.CompactCalendar
import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.example.myapplicationtryagain.model.Product
import com.example.myapplicationtryagain.model.Recipe
import com.example.myapplicationtryagain.repository.FoodDiaryRepository
import com.example.myapplicationtryagain.ui.components.AddProductEntryBottomSheet
import com.example.myapplicationtryagain.ui.components.AddRecipeEntryBottomSheet
import com.example.myapplicationtryagain.ui.recipes.loadProductsFromFirebase
import com.example.myapplicationtryagain.ui.recipes.loadRecipesFromFirebase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDiaryScreen() {
    val repository = remember { FoodDiaryRepository() }
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var diaryEntries by remember { mutableStateOf<List<FoodDiaryEntry>>(emptyList()) }

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var productsLoading by remember { mutableStateOf(true) }

    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var recipesLoading by remember { mutableStateOf(true) }

    var showProductSheet by remember { mutableStateOf(false) }
    var showRecipeSheet by remember { mutableStateOf(false) }

    val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

    // Логика для ограничения внесения записей только на +- 3 дня
    val canAddEntries = kotlin.math.abs(
        ChronoUnit.DAYS.between(LocalDate.now(), selectedDate)
    ) <= 3

    // Загрузка записей дневника на выбранную дату
    LaunchedEffect(selectedDate) {
        diaryEntries = repository.getAllEntriesForDate("test_user", selectedDate.toString())
    }

    // Загрузка продуктов и рецептов один раз при старте экрана
    LaunchedEffect(Unit) {
        products = loadProductsFromFirebase()
        productsLoading = false

        recipes = loadRecipesFromFirebase()
        recipesLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Дневник питания",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (canAddEntries) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { showProductSheet = true },
                        enabled = !productsLoading && products.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE0B2))
                    ) { Text("Добавить продукт") }
                    Button(
                        onClick = { showRecipeSheet = true },
                        enabled = !recipesLoading && recipes.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE0B2))
                    ) { Text("Добавить блюдо") }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Вносить записи можно только за 3 дня до и после текущей даты",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Календарь
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

            if (productsLoading || recipesLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    if (diaryEntries.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Нет записей на выбранную дату",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(32.dp)
                                )
                            }
                        }
                    } else {
                        items(diaryEntries, key = { it.id }) { entry ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                elevation = CardDefaults.cardElevation(1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = entry.mealtype.mealTypeToRussian(),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            "Название: ${entry.name}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            "Количество: ${entry.amount} г",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        if (!entry.reaction.isNullOrBlank()) {
                                            Text(
                                                "Реакция: ${entry.reaction}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    IconButton(
                                        onClick = {
                                            coroutineScope.launch {
                                                repository.deleteEntry(
                                                    userId = "test_user",
                                                    date = entry.date,
                                                    mealType = entry.mealtype,
                                                    entryId = entry.id
                                                )
                                                diaryEntries = repository.getAllEntriesForDate(
                                                    "test_user",
                                                    selectedDate.toString()
                                                )
                                            }
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Delete,
                                            contentDescription = "Удалить",
                                            tint = Color.Red,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // --- Шторка продуктов
                if (showProductSheet && !productsLoading && products.isNotEmpty()) {
                    AddProductEntryBottomSheet(
                        products = products,
                        selectedDate = selectedDate.toString(),
                        onDismiss = { showProductSheet = false },
                        onAdd = { prod, amount, reaction, note, mealType ->
                            coroutineScope.launch {
                                val entry = FoodDiaryEntry(
                                    id = "id_${System.currentTimeMillis()}",
                                    date = selectedDate.toString(),
                                    mealtype = mealType,
                                    productId = prod.id.toString(),
                                    name = prod.name,
                                    amount = amount,
                                    reaction = reaction,
                                    imageurl = prod.imageurl.orEmpty()
                                )
                                repository.addEntry(
                                    userId = "test_user",
                                    date = selectedDate.toString(),
                                    mealType = mealType,
                                    entry = entry
                                )
                                diaryEntries = repository.getAllEntriesForDate(
                                    "test_user",
                                    selectedDate.toString()
                                )
                                showProductSheet = false
                            }
                        }
                    )
                }

                // --- Шторка рецептов
                if (showRecipeSheet && !recipesLoading && recipes.isNotEmpty()) {
                    AddRecipeEntryBottomSheet(
                        recipes = recipes,
                        selectedDate = selectedDate.toString(),
                        onDismiss = { showRecipeSheet = false },
                        onAdd = { rec, amount, reaction, mealType ->
                            coroutineScope.launch {
                                val entry = FoodDiaryEntry(
                                    id = "id_${System.currentTimeMillis()}",
                                    date = selectedDate.toString(),
                                    mealtype = mealType,
                                    productId = rec.id.toString(),
                                    name = rec.name,
                                    amount = amount,
                                    reaction = reaction,
                                    imageurl = rec.imgurl.orEmpty()
                                )
                                repository.addEntry(
                                    userId = "test_user",
                                    date = selectedDate.toString(),
                                    mealType = mealType,
                                    entry = entry
                                )
                                diaryEntries = repository.getAllEntriesForDate(
                                    "test_user",
                                    selectedDate.toString()
                                )
                                showRecipeSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}

// Функция для конвертации типа приема пищи в русский
fun String.mealTypeToRussian(): String = when (this) {
    "breakfast" -> "Завтрак"
    "lunch"     -> "Обед"
    "dinner"    -> "Ужин"
    "snack"     -> "Перекус"
    else        -> this
}
