package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.myapplicationtryagain.model.Recipe
import com.example.myapplicationtryagain.data.Reactions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeEntryBottomSheet(
    recipes: List<Recipe>,
    selectedDate: String,
    onDismiss: () -> Unit,
    onAdd: (Recipe, String, String, String) -> Unit
) {
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var amount by remember { mutableStateOf("") }
    var reaction by remember { mutableStateOf("") }

    val mealTypes = listOf(
        "Завтрак" to "breakfast",
        "Обед" to "lunch",
        "Ужин" to "dinner",
        "Перекус" to "snack"
    )
    var selectedMealType by remember { mutableStateOf(mealTypes.first()) }
    var expandedMealType by remember { mutableStateOf(false) }
    var expandedRecipe by remember { mutableStateOf(false) }
    var expandedReaction by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Добавить блюдо на $selectedDate",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

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

            Spacer(modifier = Modifier.height(24.dp))

            // Выбор рецепта
            ExposedDropdownMenuBox(
                expanded = expandedRecipe,
                onExpandedChange = { expandedRecipe = !expandedRecipe }
            ) {
                OutlinedTextField(
                    value = selectedRecipe?.name ?: "Выберите блюдо",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text("Блюдо") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedRecipe) }
                )
                ExposedDropdownMenu(
                    expanded = expandedRecipe,
                    onDismissRequest = { expandedRecipe = false }
                ) {
                    recipes.forEach { recipe ->
                        DropdownMenuItem(
                            text = { Text(recipe.name) },
                            onClick = {
                                selectedRecipe = recipe
                                expandedRecipe = false
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
                label = { Text("Количество (например, 150 г)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Реакция — теперь из централизованного Reactions.list
            ExposedDropdownMenuBox(
                expanded = expandedReaction,
                onExpandedChange = { expandedReaction = !expandedReaction }
            ) {
                OutlinedTextField(
                    value = reaction,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Реакция (по желанию)") },
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

            Button(
                onClick = {
                    selectedRecipe?.let {
                        onAdd(
                            it,
                            amount,
                            reaction,
                            selectedMealType.second
                        )
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
