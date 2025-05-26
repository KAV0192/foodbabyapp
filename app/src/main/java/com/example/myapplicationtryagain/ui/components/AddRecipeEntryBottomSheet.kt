package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplicationtryagain.model.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeEntryBottomSheet(
    recipes: List<Recipe>,
    selectedDate: String,
    onDismiss: () -> Unit,
    onAdd: (Recipe, String, String) -> Unit
) {
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var amount by remember { mutableStateOf("") }
    var reaction by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "Добавить блюдо на $selectedDate",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Выбор рецепта
            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = {}
            ) {
                TextField(
                    value = selectedRecipe?.name ?: "Выберите блюдо",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                )
                DropdownMenu(
                    expanded = false,
                    onDismissRequest = {}
                ) {
                    // заглушка — если надо, можно реализовать выпадающий список
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Количество
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Количество (например, 150 г)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Реакция
            OutlinedTextField(
                value = reaction,
                onValueChange = { reaction = it },
                label = { Text("Реакция (по желанию)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    selectedRecipe?.let {
                        onAdd(it, amount, reaction)
                        onDismiss()
                    }
                },
                enabled = selectedRecipe != null && amount.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить")
            }
        }
    }
}
