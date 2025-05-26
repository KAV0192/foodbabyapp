package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplicationtryagain.model.Product
import com.example.myapplicationtryagain.data.AllergyInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    product: Product,
    onDismiss: () -> Unit
) {
    val peach = Color(0xFFFFF0E6) // Нежно-персиковый фон

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxHeight(),
        containerColor = peach // фон bottom sheet
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // 1. Изображение и название
            AsyncImage(
                model = product.imageurl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray)

            // 2. Основная информация
            Text("Возраст: ${product.agemin}+ месяцев", style = MaterialTheme.typography.bodyMedium)
            Text("Тип пищи: ${product.foodtype}", style = MaterialTheme.typography.bodyMedium)
            Text("Прием пищи: ${product.mealtype}", style = MaterialTheme.typography.bodyMedium)
            Text("Аллерген: ${if (product.allergen) "Да" else "Нет"}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            // Таблица КБЖУ
            Text("Пищевая ценность:", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(6.dp))
            Column {
                InfoRow(label = "Калории", value = "${product.calories} ккал")
                InfoRow(label = "Белки", value = "${product.protein} г")
                InfoRow(label = "Жиры", value = "${product.fat} г")
                InfoRow(label = "Углеводы", value = "${product.carbs} г")
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray)

            // 3. Приготовление и подача
            Text("Способ приготовления:", style = MaterialTheme.typography.titleSmall)
            Text(product.preparationmethod, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Подача:", style = MaterialTheme.typography.titleSmall)
            Text(product.servingsuggestion, style = MaterialTheme.typography.bodyMedium)

            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray)

            // 4. Польза продукта
            Text("Польза продукта:", style = MaterialTheme.typography.titleSmall)
            Text(text = product.nutrition ?: "", style = MaterialTheme.typography.bodyMedium)

            Divider(modifier = Modifier.padding(vertical = 12.dp), thickness = 1.dp, color = Color.LightGray)

            // 5. Информация по аллергии
            Text("Информация по аллергии:", style = MaterialTheme.typography.titleSmall)
            Text(AllergyInfo.info, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
