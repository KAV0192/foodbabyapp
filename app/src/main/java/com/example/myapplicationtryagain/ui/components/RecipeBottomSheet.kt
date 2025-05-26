package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.myapplicationtryagain.model.Recipe
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeBottomSheet(
    recipe: Recipe,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight()
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .background(Color(0xFFFFF1E6))
                .padding(16.dp)
        ) {
            AsyncImage(
                model = recipe.imgurl,
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = recipe.name, style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Возраст:", fontWeight = FontWeight.Medium)
                Text("${recipe.agemin}+ мес")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Время приготовления:", fontWeight = FontWeight.Medium)
                Text("${recipe.cooktime} мин")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Аллерген:", fontWeight = FontWeight.Medium)
                Text(if (recipe.allergen) "Да" else "Нет")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("Ингредиенты", modifier = Modifier.padding(12.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("Приготовление", modifier = Modifier.padding(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (selectedTab == 0) {
                // Ингредиенты
                val ingredientList = recipe.ingredients.split(",").map { it.trim() }

                Column {
                    ingredientList.forEach { item ->
                        val parts = item.split("-").map { it.trim() }
                        if (parts.size >= 2) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = parts[0],
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = parts[1],
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            // Если нет количества — просто показываем текст
                            Text(text = item, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            } else {
                Text(
                    text = recipe.howtocook,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 20.sp
                )
            }


            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

