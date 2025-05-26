package com.example.myapplicationtryagain.ui.test

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.myapplicationtryagain.model.Product
import com.example.myapplicationtryagain.repository.ProductRepository

@Composable
fun TestProductsScreen() {
    val repository = remember { ProductRepository() }
    var products by remember { mutableStateOf(emptyList<Product>()) }

    LaunchedEffect(Unit) {
        repository.getProducts { fetched ->
            products = fetched
        }
    }

    LazyColumn {
        items(products) { product ->
            ListItem(
                headlineContent = { Text(product.name) },
                supportingContent = { Text("Возраст: ${product.agemin}") }
            )
            Divider()
        }
    }
}
