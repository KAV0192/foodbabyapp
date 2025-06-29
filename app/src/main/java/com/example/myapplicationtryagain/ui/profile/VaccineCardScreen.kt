package com.example.myapplicationtryagain.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.myapplicationtryagain.data.VaccineDataStore
import com.example.myapplicationtryagain.data.VaccineCategory
import com.example.myapplicationtryagain.data.VaccineItem
import java.time.Instant
import java.time.ZoneId
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VaccineCardScreen(navController: NavController? = null) {
    val containerColor = Color(0xFFF7F9FA)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Загружаем категории из DataStore
    val loadedCategories by VaccineDataStore.getCategories(context).collectAsState(initial = emptyList())
    var categories by remember { mutableStateOf(loadedCategories) }

    // Если в DataStore пусто, инициализируем дефолтным списком и сразу сохраняем
    LaunchedEffect(loadedCategories) {
        if (loadedCategories.isEmpty()) {
            val defaultCategories = listOf(
                VaccineCategory(
                    "Новорожденные в первые 24 часа жизни",
                    listOf(VaccineItem("Первая вакцинация против вирусного гепатита B"))
                ),
                VaccineCategory(
                    "Новорожденные на 3 — 7 день жизни",
                    listOf(VaccineItem("Вакцинация против туберкулеза"))
                ),
                VaccineCategory(
                    "Дети 1 месяц",
                    listOf(VaccineItem("Вторая вакцинация против вирусного гепатита B"))
                ),
                VaccineCategory(
                    "Дети 2 месяца",
                    listOf(
                        VaccineItem("Третья вакцинация против вирусного гепатита B (группы риска)"),
                        VaccineItem("Первая вакцинация против пневмококковой инфекции"))
                ),
                VaccineCategory(
                    "Дети 3 месяца",
                    listOf(
                        VaccineItem("Первая вакцинация против дифтерии, коклюша, столбняка"),
                        VaccineItem("Первая вакцинация против полиомиелита"),
                        VaccineItem("Первая вакцинация против гемофильной инфекции типа b")
                    )
                ),
                VaccineCategory(
                    "Дети 4,5 месяцев",
                    listOf(
                        VaccineItem("Вторая вакцинация против дифтерии, коклюша, столбняка"),
                        VaccineItem("Вторая вакцинация против гемофильной инфекции типа b"),
                        VaccineItem("Вторая вакцинация против полиомиелита"),
                        VaccineItem("Вторая вакцинация против пневмококковой инфекции")
                    )
                ),
                VaccineCategory(
                    "Дети 6 месяцев",
                    listOf(
                        VaccineItem("Третья вакцинация против дифтерии, коклюша, столбняка"),
                        VaccineItem("Третья вакцинация против вирусного гепатита B"),
                        VaccineItem("Третья вакцинация против полиомиелита Третья вакцинация против гемофильной инфекции типа b")
                    )
                ),
                VaccineCategory(
                    "Дети 12 месяцев",
                    listOf(
                        VaccineItem("Вакцинация против кори, краснухи, эпидемического паротита"),
                        VaccineItem("Четвертая вакцинация против вирусного гепатита B (группы риска)")
                    )
                ),
                VaccineCategory(
                    "Дети 15 месяцев",
                    listOf(VaccineItem("Ревакцинация против пневмококковой инфекции"))
                ),
                VaccineCategory(
                    "Дети 18 месяцев",
                    listOf(
                        VaccineItem("Первая ревакцинация против дифтерии, коклюша, столбняка"),
                        VaccineItem("Первая ревакцинация против полиомиелита"),
                        VaccineItem("Ревакцинация против гемофильной инфекции типа b")
                    )
                ),
                VaccineCategory(
                    "Дети 20 месяцев",
                    listOf(VaccineItem("Вторая ревакцинация против полиомиелита"))
                ),
                VaccineCategory(
                    "Дети 6 лет",
                    listOf(
                        VaccineItem("Ревакцинация против кори, краснухи, эпидемического паротита"),
                        VaccineItem("Ревакцинация против туберкулеза"),
                        VaccineItem("Третья ревакцинация против полиомиелита"),
                    )
                ),
                VaccineCategory(
                    "Дети 6 — 7 лет",
                    listOf(
                        VaccineItem("Вторая ревакцинация против дифтерии, столбняка"),
                        VaccineItem("Ревакцинация против туберкулеза"))
                ),
                VaccineCategory(
                    "Дети 14 лет",
                    listOf(VaccineItem("Третья ревакцинация против дифтерии, столбняка"))
                ),
            )
            categories = defaultCategories
            scope.launch {
                VaccineDataStore.saveCategories(context, defaultCategories)
            }
        } else {
            categories = loadedCategories
        }
    }

    // Для открытия диалога выбора даты
    var dateDialogCatIndex by remember { mutableStateOf<Int?>(null) }
    var dateDialogVaccineIndex by remember { mutableStateOf<Int?>(null) }
    var datePickerState by remember { mutableStateOf<androidx.compose.material3.DatePickerState?>(null) }

    // Диалог календаря
    if (dateDialogCatIndex != null && dateDialogVaccineIndex != null) {
        if (datePickerState == null) {
            datePickerState = rememberDatePickerState()
        }
        DatePickerDialog(
            onDismissRequest = {
                dateDialogCatIndex = null
                dateDialogVaccineIndex = null
                datePickerState = null
            },
            confirmButton = {
                Button(
                    onClick = {
                        val millis = datePickerState?.selectedDateMillis
                        if (millis != null) {
                            val localDate = Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            val catIdx = dateDialogCatIndex!!
                            val vaxIdx = dateDialogVaccineIndex!!

                            // Обновляем копию списка, чтобы Jetpack Compose заметил изменения
                            val newCategories = categories.mapIndexed { i, cat ->
                                if (i == catIdx) {
                                    cat.copy(items = cat.items.mapIndexed { j, item ->
                                        if (j == vaxIdx) item.copy(date = localDate.toString()) else item
                                    })
                                } else cat
                            }
                            categories = newCategories
                            // Сохраняем!
                            scope.launch {
                                VaccineDataStore.saveCategories(context, newCategories)
                            }
                        }
                        dateDialogCatIndex = null
                        dateDialogVaccineIndex = null
                        datePickerState = null
                    }
                ) { Text("ОК") }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    dateDialogCatIndex = null
                    dateDialogVaccineIndex = null
                    datePickerState = null
                }) { Text("Отмена") }
            }
        ) {
            datePickerState?.let { DatePicker(state = it) }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(containerColor)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Центрируем заголовок
            Text(
                text = "Карта прививок",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                modifier = Modifier
                    .padding(bottom = 18.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            categories.forEachIndexed { catIdx, category ->
                // Категория (заголовок с фоном)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    color = Color(0xFFF1F8FF),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        category.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
                // Прививки
                category.items.forEachIndexed { idx, item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.name,
                            Modifier.weight(1f),
                            fontSize = 15.sp,
                            color = Color(0xFF263238)
                        )
                        Spacer(Modifier.width(8.dp))
                        if (item.date.isNullOrEmpty()) {
                            OutlinedButton(
                                onClick = {
                                    dateDialogCatIndex = catIdx
                                    dateDialogVaccineIndex = idx
                                    datePickerState = null
                                },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Укажите дату", fontSize = 13.sp)
                            }
                        } else {
                            Text(
                                item.date!!,
                                color = Color(0xFF1976D2),
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Divider(color = Color(0xFFDDE6EF), thickness = 1.dp)
                }
            }
        }
    }
}
