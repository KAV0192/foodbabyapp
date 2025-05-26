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
import com.example.myapplicationtryagain.data.CompactCalendar
import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.example.myapplicationtryagain.model.Product
import com.example.myapplicationtryagain.model.Recipe
import com.example.myapplicationtryagain.repository.FoodDiaryRepository
import com.example.myapplicationtryagain.ui.components.AddProductEntryBottomSheet
import com.example.myapplicationtryagain.ui.components.AddRecipeEntryBottomSheet
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun FoodDiaryScreen() {
    val repository = remember { FoodDiaryRepository() }
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var diaryEntries by remember { mutableStateOf<List<FoodDiaryEntry>>(emptyList()) }
    var showProductSheet by remember { mutableStateOf(false) }
    var showRecipeSheet by remember { mutableStateOf(false) }

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }

    val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

    // Загружаем продукты
    LaunchedEffect(Unit) {
        products = loadProductsFromFirebase()
        recipes = loadRecipesFromFirebase()
    }

    // Загружаем записи
    LaunchedEffect(selectedDate) {
        diaryEntries = repository.getAllEntriesForDate("test_user", selectedDate.toString())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showProductSheet = true },
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

            Button(
                onClick = { showRecipeSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Добавить блюдо")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                if (diaryEntries.isEmpty()) {
                    item { Text("Нет записей на выбранную дату") }
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
                                if (entry.imageurl.isNotBlank()) {
                                    // Можно добавить изображение
                                }
                            }
                        }
                    }
                }
            }

            // Показываем Bottom Sheet
            if (showProductSheet) {
                AddProductEntryBottomSheet(
                    products = products,
                    selectedDate = selectedDate.toString(),
                    onDismiss = { showProductSheet = false },
                    onAdd = { product, amount, reaction, note ->
                        coroutineScope.launch {
                            val entry = FoodDiaryEntry(
                                id = "entry_" + System.currentTimeMillis(),
                                date = selectedDate.toString(),
                                mealtype = "breakfast", // временно фиксируем
                                productId = product.id.toString(),
                                name = product.name,
                                amount = amount,
                                imageurl = product.imageurl ?: ""
                            )
                            repository.addEntry("test_user", selectedDate.toString(), "breakfast", entry)
                            diaryEntries = repository.getAllEntriesForDate("test_user", selectedDate.toString())
                        }
                    }
                )
            }
            if (showRecipeSheet) {
                AddRecipeEntryBottomSheet(
                    recipes = recipes,
                    selectedDate = selectedDate.toString(),
                    onDismiss = { showRecipeSheet = false },
                    onAdd = { recipe, amount, reaction ->
                        coroutineScope.launch {
                            val entry = FoodDiaryEntry(
                                id = "id_" + System.currentTimeMillis(),
                                date = selectedDate.toString(),
                                mealtype = "dinner", // или спросить у пользователя
                                productId = recipe.id.toString(),
                                name = recipe.name,
                                amount = amount,
                                imageurl = recipe.imgurl ?: ""
                                // можно сохранить и reaction отдельно
                            )
                            repository.addEntry("test_user", selectedDate.toString(), entry.mealtype, entry)
                            diaryEntries = repository.getAllEntriesForDate("test_user", selectedDate.toString())
                        }
                    }
                )
            }

        }
    }
}

// Загрузка продуктов из Firebase
suspend fun loadProductsFromFirebase(): List<Product> = withContext(Dispatchers.IO) {
    val ref = FirebaseDatabase.getInstance().getReference("products")
    return@withContext try {
        val snapshot = ref.get().await()
        snapshot.children.mapNotNull { it.getValue(Product::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
suspend fun loadRecipesFromFirebase(): List<Recipe> = withContext(Dispatchers.IO) {
    val ref = FirebaseDatabase.getInstance().getReference("recipes")
    return@withContext try {
        val snapshot = ref.get().await()
        snapshot.children.mapNotNull { it.getValue(Recipe::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

