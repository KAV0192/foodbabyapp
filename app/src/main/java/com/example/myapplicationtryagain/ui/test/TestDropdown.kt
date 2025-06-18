package com.example.myapplicationtest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestFunction() {
    val products = listOf("Яблоко", "Банан", "Апельсин")
    var expanded by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf("") }

    // Используем ExposedDropdownMenuBox и ExposedDropdownMenu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedProduct,
            onValueChange = {},
            label = { Text("Продукт") },
            readOnly = true,
            modifier = Modifier
                .menuAnchor() // Обязательно для корректного позиционирования меню
                .fillMaxWidth(),
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            products.forEach { product ->
                DropdownMenuItem(
                    text = { Text(product) },
                    onClick = {
                        selectedProduct = product
                        expanded = false
                    }
                )
            }
        }
    }
}