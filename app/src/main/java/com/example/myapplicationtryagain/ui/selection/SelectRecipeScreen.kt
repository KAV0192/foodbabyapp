package com.example.myapplicationtryagain.ui.selection

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplicationtryagain.model.Recipe
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectRecipeScreen(
    navController: NavController,
    onRecipeSelected: (Recipe) -> Unit
) {
    var recipes by remember { mutableStateOf<List<Recipe>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        recipes = loadRecipesFromFirebase()
        isLoading = false
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Выбор рецепта") })
    }) { padding ->
        if (isLoading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                items(recipes.size) { index ->
                    val recipe = recipes[index]
                    ListItem(
                        headlineContent = { Text(recipe.name) },
                        supportingContent = { Text("${recipe.agemin}+ мес") },
                        modifier = Modifier
                            .clickable {
                                onRecipeSelected(recipe)
                                navController.popBackStack()
                            }
                    )
                    Divider()
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
