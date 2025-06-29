package com.example.myapplicationtryagain.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun NutritionBarChart(
    calories: Int,
    protein: Int,
    fat: Int,
    carbs: Int,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        "Калории" to calories,
        "Белки" to protein,
        "Жиры" to fat,
        "Углеводы" to carbs
    )
    val maxValue = items.maxOf { it.second }.coerceAtLeast(1)
    val barColors = listOf(
        Color(0xFF1976D2),
        Color(0xFF43A047),
        Color(0xFFFFB300),
        Color(0xFFE53935)
    )

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("График КБЖУ за день", fontSize = 18.sp, color = Color(0xFF263238))
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val barWidth = size.width / (items.size * 2)
                items.forEachIndexed { index, item ->
                    val value = item.second
                    val barHeight = (value.toFloat() / maxValue) * size.height
                    drawRect(
                        color = barColors[index],
                        topLeft = Offset(
                            x = index * 2 * barWidth + barWidth / 2,
                            y = size.height - barHeight
                        ),
                        size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEachIndexed { index, item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${item.second}", color = barColors[index], fontSize = 15.sp)
                    Text(item.first, fontSize = 13.sp, color = Color(0xFF263238))
                }
            }
        }
    }
}

// --- Пример использования (например, на экране питания) --- //
@Composable
fun DiaryScreenWithChart() {
    NutritionBarChart(
        calories = 520,
        protein = 12,
        fat = 9,
        carbs = 70
    )
}
