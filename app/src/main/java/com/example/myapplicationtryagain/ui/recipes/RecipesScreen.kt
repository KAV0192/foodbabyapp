package com.example.myapplicationtryagain.ui.recipes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.R
import com.example.myapplicationtryagain.navigation.Screen

@Composable
fun RecipesScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Фоновое изображение
        Image(
            painter = painterResource(id = R.drawable.background_image3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Слой с кнопками поверх
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Кнопка "Продукты"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
                    .clickable {
                        navController.navigate(Screen.ProductsList.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Продукты", fontSize = 24.sp, color = Color.Black)
                    }
                }
            }

            // Кнопка "Рецепты"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 8.dp)
                    .clickable {
                        navController.navigate(Screen.RecipesList.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Рецепты", fontSize = 24.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}
