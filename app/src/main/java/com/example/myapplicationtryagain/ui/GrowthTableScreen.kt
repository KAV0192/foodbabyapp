package com.example.myapplicationtryagain.ui

import AddGrowthRecordDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplicationtryagain.data.GrowthDataStore
import com.example.myapplicationtryagain.data.GrowthRecord
import java.time.LocalDate as JavaLocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myapplicationtryagain.vidgets.GrowthChart // <-- если у тебя GrowthChart в vidgets пакете


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrowthTableScreen() {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val records by GrowthDataStore.getRecords(context).collectAsState(initial = emptyList())

    // --- Формат даты дд.мм.гггг
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    var inputDate by remember { mutableStateOf(JavaLocalDate.now().format(formatter)) }
    var inputHeight by remember { mutableStateOf("") }
    var inputWeight by remember { mutableStateOf("") }

    // --- ГРАФИК (оставь как есть) ---
    var chartType by remember { mutableStateOf("Рост и вес") }
    val chartTypes = listOf("Рост и вес", "Только рост", "Только вес")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FA))
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Таблица роста и веса",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(16.dp))

            // --- Переключатель графика ---
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                chartTypes.forEach { type ->
                    Button(
                        onClick = { chartType = type },
                        colors = if (chartType == type) ButtonDefaults.buttonColors(
                            containerColor = Color(
                                0xFF1976D2
                            )
                        )
                        else ButtonDefaults.buttonColors(),
                        modifier = Modifier
                            .padding(horizontal = 2.dp)
                            .height(32.dp)
                    ) {
                        Text(type, fontSize = 13.sp)
                    }
                }
            }
            Spacer(Modifier.height(10.dp))

            // --- Сам график (ОГРАНИЧИВАЕМ ВЫСОТУ!) ---
            GrowthChart(
                records, chartType,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // <-- задай нужную высоту
            )
            Spacer(Modifier.height(16.dp))

            // Заголовки таблицы
            Row(Modifier.fillMaxWidth().padding(bottom = 4.dp)) {
                Text("Дата", Modifier.weight(1f), fontWeight = FontWeight.Medium)
                Text("Рост, см", Modifier.weight(1f), fontWeight = FontWeight.Medium)
                Text("Вес, кг", Modifier.weight(1f), fontWeight = FontWeight.Medium)
            }
            Divider()

            // --- Таблица --- (занимает остаток места)
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(records.size) { idx ->
                    val record = records[idx]
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(record.date, Modifier.weight(1f))
                        Text(record.height, Modifier.weight(1f))
                        Text(record.weight, Modifier.weight(1f))

                        // --- Крестик удаления ---
                        IconButton(
                            onClick = {
                                // Удалить запись
                                val updated = records.toMutableList().apply { removeAt(idx) }
                                scope.launch {
                                    GrowthDataStore.saveRecords(context, updated)
                                }
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Удалить",
                                tint = Color.Red
                            )
                        }
                    }
                    Divider(color = Color(0xFFDDE6EF), thickness = 1.dp)
                }
            }

            // Кнопка добавить запись
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Добавить запись", color = Color.White, fontSize = 17.sp)
            }
        }

        // Универсальный диалог
        if (showDialog) {
            val scope = rememberCoroutineScope()
            val context = LocalContext.current
            AddGrowthRecordDialog(
                onDismiss = { showDialog = false },
                onSave = { newRecord ->
                    val updated = records + newRecord
                    scope.launch {
                        GrowthDataStore.saveRecords(context, updated)
                    }
                    showDialog = false
                }
            )
        }
    }
}

