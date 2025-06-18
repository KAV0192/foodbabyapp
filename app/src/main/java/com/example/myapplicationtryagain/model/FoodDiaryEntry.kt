package com.example.myapplicationtryagain.model

data class FoodDiaryEntry(
    val id: String = "",
    val date: String = "", // Формат yyyy-MM-dd
    val mealtype: String = "", // Завтрак, Обед, Ужин и т.д.
    val productId: String = "", // ID продукта (если используется)
    val name: String = "", // Название продукта
    val amount: String = "", // Количество, например "100 г"
    val reaction: String = "", // Реакции
    val imageurl: String = "" // Картинка продукта
)
