package com.example.myapplicationtryagain.navigation

sealed class BottomNavScreenContent(val route: String, val title: String) {
    object Home : BottomNavScreenContent("home", "Главная")
    object DailyLog : BottomNavScreenContent("daily_log", "Дневник")
    object Recipes : BottomNavScreenContent("recipes", "Рецепты")
    object ChildProfile : BottomNavScreenContent("child_profile", "Профиль")
}
