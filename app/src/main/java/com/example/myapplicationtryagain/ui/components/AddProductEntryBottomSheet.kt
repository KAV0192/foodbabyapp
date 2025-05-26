package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
        note: String
    ) -> Unit
) {
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var amount by remember { mutableStateOf("") }
    var reaction by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val reactions = listOf("Нет", "Сыпь", "Газики", "Плохой сон")

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Добавить продукт", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(12.dp))

            // Выбор продукта
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {}
            ) {
                var expanded by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = selectedProduct?.name ?: "Выберите продукт",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Продукт") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    }
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    products.forEach { product ->
                        DropdownMenuItem(
                            text = { Text(product.name) },
                            onClick = {
                                selectedProduct = product
                                expanded = false
                            }
                        )
                    }
                }
                LaunchedEffect(Unit) { expanded = true } // автораскрытие
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кол-во
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Количество (г/мл)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Реакция
            var expandedReaction by remember { mutableStateOf(false) }

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
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedReaction,
                    onDismissRequest = { expandedReaction = false }
                ) {
                    reactions.forEach {
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

            // Заметка
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Заметки") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (selectedProduct != null && amount.isNotBlank()) {
                        onAdd(selectedProduct!!, amount, reaction, note)
                        onDismiss()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Сохранить")
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
