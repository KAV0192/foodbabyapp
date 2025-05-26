package com.example.myapplicationtryagain.ui.selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.model.Product
import com.example.myapplicationtryagain.model.FoodDiaryEntry
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectProductScreen(
    navController: NavController,
    userId: String = "test_user",
    selectedDate: LocalDate,
    mealType: String
) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        products = loadProductsFromFirebase()
        isLoading = false
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Выбор продукта") })
    }) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(products.size) { index ->
                    val product = products[index]
                    ListItem(
                        headlineContent = { Text(product.name) },
                        supportingContent = { Text("${product.agemin}+ мес") },
                        modifier = Modifier.clickable {
                            // Сохраняем в Firebase
                            saveProductToDiary(userId, selectedDate.toString(), mealType, product)
                            navController.popBackStack()
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

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

fun saveProductToDiary(userId: String, date: String, mealType: String, product: Product) {
    val entryId = "entry_${System.currentTimeMillis()}"
    val entry = FoodDiaryEntry(
        productId = product.id.toString(),
        name = product.name,
        amount = "100 г", // по умолчанию, можно будет менять
        mealtype = mealType
    )
    val ref = FirebaseDatabase.getInstance()
        .getReference("food_diary/$userId/$date/$mealType/$entryId")
    ref.setValue(entry)
}
