package com.example.myapplicationtryagain.ui

import AddGrowthRecordDialog
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.data.GrowthDataStore
import com.example.myapplicationtryagain.vidgets.GrowthChart
import com.example.myapplicationtryagain.data.ProfileDataStore
import com.example.myapplicationtryagain.data.VaccineDataStore
import com.example.myapplicationtryagain.data.VaccineCategory
import com.example.myapplicationtryagain.data.VaccineItem
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun WelcomeScreen(navController: NavController) {
    // --- STATE ---
    val articles = listOf(
        ArticleCardUi("5 первых продуктов", "Что ввести на прикорм? Краткий гид для родителей."),
        ArticleCardUi("Режим питания?", "Психология и биология первого года жизни."),
        ArticleCardUi("Рецепт недели", "Пюре из кабачка: как приготовить?"),
        ArticleCardUi("Заглушка окна №4", "")
    )

    val foodReminders = emptyList<String>()

    val context = LocalContext.current
    val growthRecords by GrowthDataStore.getRecords(context).collectAsState(initial = emptyList())
    val childProfile by ProfileDataStore.getProfileFlow(context).collectAsState(initial = null)

    val childName = childProfile?.name ?: "Имя не указано"
    val childAge = calcAge(childProfile?.birthDate)

    // --- Прививки: категория по возрасту ---
    val categories by VaccineDataStore.getCategories(context).collectAsState(initial = emptyList())
    val ageInMonths = remember(childProfile) { calcAgeInMonths(childProfile?.birthDate) }
    val currentCategory = remember(categories, ageInMonths) {
        findVaccineCategoryByAge(categories, ageInMonths)
    }
    val vaccinesThisMonth = currentCategory?.items?.filter { it.date.isNullOrEmpty() } ?: emptyList()

    var showAddDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // --- UI ---
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF7F9FA))
    ) {
        // --- Горизонтальный скролл со статьями ---
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, start = 8.dp, end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(articles) { article ->
                ArticleCardSmall(article)
            }
        }

        // --- Аватарка и имя ---
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDDE6EF)),
                contentAlignment = Alignment.Center
            ) {
                Text("👶", fontSize = 48.sp, color = Color(0xFF90A4AE))
            }
            Spacer(Modifier.height(10.dp))
            Text(childName, fontWeight = FontWeight.Medium, fontSize = 19.sp)
            Text(childAge, color = Color(0xFF1976D2), fontSize = 15.sp)
        }

        // --- Рекомендации по прививкам ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            shape = RoundedCornerShape(11.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFFBEA)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(13.dp)) {
                Text(
                    "В этом месяце по плану:",
                    color = Color(0xFFFBC02D),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
                Spacer(Modifier.height(6.dp))
                if (vaccinesThisMonth.isEmpty()) {
                    Text(
                        "Нет запланированных прививок",
                        fontSize = 15.sp,
                        color = Color(0xFF72560A),
                        modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                    )
                } else {
                    vaccinesThisMonth.forEach {
                        Text(
                            "• ${it.name}",
                            fontSize = 15.sp,
                            color = Color(0xFF72560A),
                            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                        )
                    }
                }
            }
        }

        // --- Напоминания о приёмах пищи ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 0.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(Color(0xFFE3F2FD)),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(Modifier.padding(13.dp)) {
                Text(
                    "Напоминания на сегодня:",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
                Spacer(Modifier.height(4.dp))
                if (foodReminders.isEmpty()) {
                    Text("На сегодня напоминаний нет", color = Color(0xFF90A4AE), fontSize = 15.sp)
                } else {
                    foodReminders.forEach {
                        Text(
                            "• $it",
                            fontSize = 15.sp,
                            color = Color(0xFF263238),
                            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                        )
                    }
                }
            }
        }

        // --- ВИДЖЕТ ГРАФИКА + кнопка ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .background(
                    color = Color(0xFFEAF3FB),
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Динамика роста и веса",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .size(34.dp)
                    .padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить запись",
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        GrowthChart(
            records = growthRecords,
            chartType = "Рост и вес",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        )




        // --- Диалог добавления записи ---
        if (showAddDialog) {
            AddGrowthRecordDialog(
                onDismiss = { showAddDialog = false },
                onSave = { newRecord ->
                    val updated = growthRecords + newRecord
                    scope.launch {
                        GrowthDataStore.saveRecords(context, updated)
                    }
                    showAddDialog = false
                }
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

// --- Вспомогательные классы и функции ---

data class ArticleCardUi(val title: String, val desc: String)

@Composable
fun ArticleCardSmall(article: ArticleCardUi) {
    Card(
        modifier = Modifier
            .width(108.dp)
            .height(62.dp),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(Color(0xFFEEF6FA)),
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = article.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 7.dp)
            )
        }
    }
}

// --- ВЫЧИСЛЯЕМ возраст в месяцах ---
fun calcAgeInMonths(birth: String?): Int {
    if (birth == null) return 0
    return try {
        val birthDate = LocalDate.parse(birth)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val years = now.year - birthDate.year
        val months = now.monthNumber - birthDate.monthNumber
        val totalMonths = years * 12 + months
        if (now.dayOfMonth < birthDate.dayOfMonth) totalMonths - 1 else totalMonths
    } catch (_: Exception) { 0 }
}

// --- Находим подходящую категорию по возрасту ---
fun findVaccineCategoryByAge(categories: List<VaccineCategory>, ageMonths: Int): VaccineCategory? {
    val ageCategoryMap = mapOf(
        0 to "Новорожденные в первые 24 часа жизни",
        0 to "Новорожденные на 3 — 7 день жизни",
        1 to "Дети 1 месяц",
        2 to "Дети 2 месяца",
        3 to "Дети 3 месяца",
        4 to "Дети 4,5 месяцев",
        6 to "Дети 6 месяцев",
        12 to "Дети 12 месяцев",
        15 to "Дети 15 месяцев",
        18 to "Дети 18 месяцев",
        20 to "Дети 20 месяцев",
        72 to "Дети 6 лет",
        78 to "Дети 6 — 7 лет",
        168 to "Дети 14 лет",
    )
    val matchedKey = ageCategoryMap.keys.filter { it <= ageMonths }.maxOrNull() ?: 0
    val catTitle = ageCategoryMap[matchedKey]
    return categories.firstOrNull { it.title == catTitle }
}

// --- Строка возраста для UI ---
fun calcAge(birth: String?): String {
    if (birth == null) return ""
    return try {
        val birthDate = LocalDate.parse(birth)
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val years = now.year - birthDate.year
        val months = now.monthNumber - birthDate.monthNumber +
                if (now.dayOfMonth < birthDate.dayOfMonth) -1 else 0
        val showYears = if (years > 0) "$years ${plural(years, "год", "года", "лет")}" else ""
        val showMonths = if (months > 0) "$months мес" else ""
        listOf(showYears, showMonths).filter { it.isNotEmpty() }.joinToString(" ")
    } catch (_: Exception) { "" }
}

fun plural(n: Int, one: String, two: String, many: String): String =
    when {
        n % 10 == 1 && n % 100 != 11 -> one
        n % 10 in 2..4 && (n % 100 !in 12..14) -> two
        else -> many
    }
