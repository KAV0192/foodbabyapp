package com.example.myapplicationtryagain.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getVaccinesThisMonth(vaccines: List<VaccineItem>): List<VaccineItem> {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return vaccines.filter { item ->
        item.date?.let {
            try {
                val date = LocalDate.parse(it)
                date.year == now.year && date.month == now.month
            } catch (_: Exception) { false }
        } ?: false
    }
}
// Соответствие категории и возраста (в месяцах)
fun findVaccineCategoryByAge(categories: List<VaccineCategory>, ageMonths: Int): VaccineCategory? {
    val ageCategoryMap = mapOf(
        0 to "Новорожденные в первые 24 часа жизни",
        0 to "Новорожденные на 3 — 7 день жизни", // при желании сделай через диапазоны
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
    // Самое близкое меньшое значение
    val matchedKey = ageCategoryMap.keys.filter { it <= ageMonths }.maxOrNull() ?: 0
    val catTitle = ageCategoryMap[matchedKey]
    return categories.firstOrNull { it.title == catTitle }
}