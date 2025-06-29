//файл с определением маршрутов навигации
package com.example.myapplicationtryagain.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object DailyLog : Screen("daily_log")
    object Recipes : Screen("recipes")
    object ProductsList : Screen("products_list")
    object RecipesList : Screen("recipes_list")
    object ChildProfile : Screen("child_profile")
    object VaccineCard : Screen("vaccine_card")
    object GrowthTable : Screen("growth_table")



    // 👇 Эти два маршрута обязательно
    object SelectProduct : Screen("select_product")
    object SelectRecipe : Screen("select_recipe")
}