package com.example.myapplicationtryagain.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplicationtryagain.ui.SplashScreen
import com.example.myapplicationtryagain.ui.WelcomeScreen
import com.example.myapplicationtryagain.ui.recipes.RecipesScreen
import com.example.myapplicationtryagain.ui.components.BottomNavigationBar
import com.example.myapplicationtryagain.ui.recipes.ProductsListScreen
import com.example.myapplicationtryagain.ui.recipes.RecipesListScreen
import com.example.myapplicationtryagain.ui.diary.FoodDiaryScreen
import com.example.myapplicationtryagain.data.CompactCalendar
import com.example.myapplicationtryagain.ui.selection.SelectProductScreen
import com.example.myapplicationtryagain.ui.selection.SelectRecipeScreen
import java.time.LocalDate


@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    val bottomNavItems = listOf(
        BottomNavScreenContent.Home,
        BottomNavScreenContent.DailyLog,
        BottomNavScreenContent.Recipes
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = Screen.Welcome.route
            ) { // Начальный экран – Welcome
                composable(Screen.Splash.route) {
                    SplashScreen(navController)
                }
                composable(Screen.Welcome.route) {
                    WelcomeScreen(navController = navController)
                }
                composable(Screen.DailyLog.route) {
                    FoodDiaryScreen()
                }
                composable(Screen.Recipes.route) {
                    RecipesScreen(navController = navController)
                }
                composable(Screen.ProductsList.route) {
                    ProductsListScreen(navController = navController)
                }
                composable(Screen.RecipesList.route) {
                    RecipesListScreen(navController = navController)
                }
                composable("select_product_screen/{date}/{mealType}") { backStackEntry ->
                    val dateStr = backStackEntry.arguments?.getString("date")!!
                    val mealType = backStackEntry.arguments?.getString("mealType")!!
                    val date = LocalDate.parse(dateStr)
                    SelectProductScreen(navController, selectedDate = date, mealType = mealType)
                }


                composable(Screen.SelectRecipe.route) {
                    SelectRecipeScreen(
                        navController = navController,
                        onRecipeSelected = { recipe ->
                            // TODO: добавить логику добавления рецепта
                            navController.popBackStack()
                        }
                    )
                }

            }
        }
    }
}
