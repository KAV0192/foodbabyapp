package com.example.myapplicationtryagain.model

data class DailyLogEntry(
    val date: String = "", // Например, "2025-05-20"
    val breakfast: List<MealItem> = emptyList(),
    val lunch: List<MealItem> = emptyList(),
    val dinner: List<MealItem> = emptyList(),
    val snack: List<MealItem> = emptyList()
)

data class MealItem(
    val name: String = "",
    val quantity: String = "", // Например, "50 г"
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0
)
