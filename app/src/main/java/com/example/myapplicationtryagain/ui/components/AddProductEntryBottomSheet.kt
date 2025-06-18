package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.myapplicationtryagain.data.Reactions
import com.example.myapplicationtryagain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductEntryBottomSheet(
    products: List<Product>,
    selectedDate: String,
    onDismiss: () -> Unit,
    onAdd: (
        product: Product,
        amount: String,
        reaction: String,
        note: String,
        mealType: String
    ) -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var amount by remember { mutableStateOf("") }
    var reaction by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val reactions = listOf("Нет", "Сыпь", "Газики", "Плохой сон")
    val mealTypes = listOf(
        "Завтрак" to "breakfast",
        "Обед" to "lunch",
        "Ужин" to "dinner",
        "Перекус" to "snack"
    )
    var selectedMealType by remember { mutableStateOf(mealTypes.first()) }
    var expandedMealType by remember { mutableStateOf(false) }

    // Для выпадающего списка продуктов
    var expandedProduct by remember { mutableStateOf(false) }

    // Для выпадающего списка реакций
    var expandedReaction by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Добавить продукт", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            // Тип приема пищи
            ExposedDropdownMenuBox(
                expanded = expandedMealType,
                onExpandedChange = { expandedMealType = !expandedMealType }
            ) {
                OutlinedTextField(
                    value = selectedMealType.first,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Тип приёма пищи") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedMealType) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedMealType,
                    onDismissRequest = { expandedMealType = false }
                ) {
                    mealTypes.forEach {
                        DropdownMenuItem(
                            text = { Text(it.first) },
                            onClick = {
                                selectedMealType = it
                                expandedMealType = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Выбор продукта
            ExposedDropdownMenuBox(
                expanded = expandedProduct,
                onExpandedChange = { expandedProduct = !expandedProduct }
            ) {
                OutlinedTextField(
                    value = selectedProduct?.name ?: "Выберите продукт",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Продукт") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedProduct) }
                )
                ExposedDropdownMenu(
                    expanded = expandedProduct,
                    onDismissRequest = { expandedProduct = false }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                selectedProduct = product
                                expandedProduct = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Количество
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Количество (г/мл)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Реакция
            ExposedDropdownMenuBox(
                expanded = expandedReaction,
                onExpandedChange = { expandedReaction = !expandedReaction }
            ) {
                OutlinedTextField(
                    value = reaction,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Реакция") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedReaction) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedReaction,
                    onDismissRequest = { expandedReaction = false }
                ) {
                    Reactions.list.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                reaction = it
                                expandedReaction = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Заметки
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Заметки") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedProduct != null && amount.isNotBlank()) {
                        onAdd(
                            selectedProduct!!,
                            amount,
                            reaction,
                            note,
                            selectedMealType.second
                        )
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedProduct != null && amount.isNotBlank()
            ) {
                Text("Сохранить")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
