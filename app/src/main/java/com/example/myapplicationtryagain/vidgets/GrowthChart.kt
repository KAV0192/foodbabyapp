package com.example.myapplicationtryagain.vidgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import com.example.myapplicationtryagain.data.GrowthDataStore
import com.example.myapplicationtryagain.data.GrowthRecord

@Composable
fun GrowthChart(
    records: List<GrowthRecord>,
    chartType: String,
    modifier: Modifier = Modifier
) {
    if (records.isEmpty()) {
        Box(
            modifier = modifier
                .background(Color(0xFFDDE6EF), shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Нет данных для графика", color = Color.Gray, fontSize = 13.sp)
        }
        return
    }

    val heightPoints = records.mapIndexed { idx, r -> idx to (r.height.toFloatOrNull() ?: 0f) }
    val weightPoints = records.mapIndexed { idx, r -> idx to (r.weight.toFloatOrNull() ?: 0f) }

    val chartLines = when (chartType) {
        "Рост и вес" -> listOf("Рост" to heightPoints, "Вес" to weightPoints)
        "Только рост" -> listOf("Рост" to heightPoints)
        "Только вес" -> listOf("Вес" to weightPoints)
        else -> emptyList()
    }

    Box(
        modifier
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val leftPad = 26.dp.toPx()
            val bottomPad = 22.dp.toPx()
            val topPad = 12.dp.toPx()
            val chartW = w - leftPad
            val chartH = h - bottomPad - topPad

            // Find min/max
            val allValues = chartLines.flatMap { it.second.map { it.second } }
            val minV = allValues.minOrNull() ?: 0f
            val maxV = (allValues.maxOrNull() ?: 1f).coerceAtLeast(minV + 1f)

            // Axis
            drawLine(Color(0xFFB0BEC5), Offset(leftPad, topPad), Offset(leftPad, h - bottomPad), strokeWidth = 3f)
            drawLine(Color(0xFFB0BEC5), Offset(leftPad, h - bottomPad), Offset(w, h - bottomPad), strokeWidth = 3f)

            chartLines.forEachIndexed { lineIdx, (name, pts) ->
                val path = Path()
                pts.forEachIndexed { i, pair ->
                    val x = leftPad + (pair.first / (pts.size - 1).coerceAtLeast(1).toFloat()) * chartW
                    val y = topPad + (maxV - pair.second) / (maxV - minV).coerceAtLeast(1f) * chartH
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                val color = if (name == "Рост") Color(0xFF1976D2) else Color(0xFF43A047)
                drawPath(path, color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
                pts.forEach { (idx, v) ->
                    val x = leftPad + (idx / (pts.size - 1).coerceAtLeast(1).toFloat()) * chartW
                    val y = topPad + (maxV - v) / (maxV - minV).coerceAtLeast(1f) * chartH
                    drawCircle(color, radius = 5.dp.toPx(), center = Offset(x, y))
                }
            }
        }
        // Легенда
        Row(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 8.dp)
        ) {
            if (chartType != "Только вес") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).background(Color(0xFF1976D2), RoundedCornerShape(4.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Рост", fontSize = 10.sp, color = Color.Gray)
                }
            }
            if (chartType != "Только рост") {
                Spacer(Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(8.dp).background(Color(0xFF43A047), RoundedCornerShape(4.dp)))
                    Spacer(Modifier.width(4.dp))
                    Text("Вес", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}
