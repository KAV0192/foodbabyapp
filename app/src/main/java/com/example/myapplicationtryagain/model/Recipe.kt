package com.example.myapplicationtryagain.model

data class Recipe(
    val id: Int = 0,
    val name: String = "",
    val agemin: Int = 0,
    val allergen: Boolean = false,
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val mealtype: String = "",
    val ingredients: String = "",
    val howtocook: String = "",
    val cooktime: Int = 0,
    val imgurl: String? = ""
)