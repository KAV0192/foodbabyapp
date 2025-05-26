package com.example.myapplicationtryagain.ui.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.model.Product
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.example.myapplicationtryagain.ui.components.ProductBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val bottomSheetState = rememberModalBottomSheetState()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Загрузка данных из Firebase один раз при старте экрана
    LaunchedEffect(Unit) {
        products = loadProductsFromFirebase()
        isLoading = false
    }

    val filteredProducts = products.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Продукты") },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                }
            }
        )

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск продукта...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredProducts) { product ->
                        ProductCard(product = product, onClick = {
                            selectedProduct = it
                            coroutineScope.launch {
                                bottomSheetState.show()
                            }
                        })
                    }
                }
            }
        }
    }

    // BottomSheet с информацией о продукте
    if (selectedProduct != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedProduct = null },
            sheetState = bottomSheetState
        ) {
            ProductBottomSheet(
                product = selectedProduct!!,
                onDismiss = { selectedProduct = null }
            )
        }
    }
}

// Загрузка продуктов из Firebase
suspend fun loadProductsFromFirebase(): List<Product> = withContext(Dispatchers.IO) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("products")

    return@withContext try {
        val snapshot = ref.get().await()
        snapshot.children.mapNotNull { it.getValue(Product::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
