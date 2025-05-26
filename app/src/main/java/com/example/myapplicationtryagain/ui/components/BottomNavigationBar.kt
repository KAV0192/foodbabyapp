package com.example.myapplicationtryagain.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplicationtryagain.R
import com.example.myapplicationtryagain.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Оборачиваем NavigationBar в Box с кастомной высотой
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(92.dp)) {

        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            NavigationBarItem(
                selected = currentRoute == Screen.Welcome.route,
                onClick = { navController.navigate(Screen.Welcome.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = "Главный экран",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                },
                label = { Text("Главный", fontSize = 10.sp) }
            )

            NavigationBarItem(
                selected = currentRoute == Screen.DailyLog.route,
                onClick = { navController.navigate(Screen.DailyLog.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_daily_log),
                        contentDescription = "Дневник",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                },
                label = { Text("Дневник", fontSize = 10.sp) }
            )

            NavigationBarItem(
                selected = currentRoute == Screen.Recipes.route,
                onClick = { navController.navigate(Screen.Recipes.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_recipe),
                        contentDescription = "Рецепты",
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                },
                label = { Text("Рецепты", fontSize = 10.sp) }
            )
        }
    }
}
