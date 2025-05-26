package com.example.myapplicationtryagain.ui.recipes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.model.Recipe
import com.example.myapplicationtryagain.ui.components.RecipeBottomSheet
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipesListScreen(navController: NavController) {
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }

    LaunchedEffect(Unit) {

        recipes = loadRecipesFromFirebase()
        isLoading = false
    }

    if (selectedRecipe != null) {
        RecipeBottomSheet(recipe = selectedRecipe!!) {
            selectedRecipe = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Рецепты") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(recipes) { recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        selectedRecipe = it
                    })
                }
            }
        }
    }
}

suspend fun loadRecipesFromFirebase(): List<Recipe> = withContext(Dispatchers.IO) {
    val ref = FirebaseDatabase.getInstance().getReference("recipes")
    return@withContext try {
        val snapshot = ref.get().await()
        snapshot.children.mapNotNull { it.getValue(Recipe::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
