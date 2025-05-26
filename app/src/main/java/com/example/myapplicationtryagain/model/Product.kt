package com.example.myapplicationtryagain.model
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val name: String = "",
    val agemin: Int = 0,
    val calories: Int = 0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val carbs: Double = 0.0,
    val foodtype: String = "",
    val mealtype: String = "",
    val allergen: Boolean = false,
    val preparationmethod: String = "",
    val servingsuggestion: String = "",
    val imageurl: String? = "",
    val nutrition: String? = ""
)
