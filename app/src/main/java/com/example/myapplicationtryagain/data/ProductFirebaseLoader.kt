package com.example.myapplicationtryagain.data

import com.example.myapplicationtryagain.model.Product
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

suspend fun loadProductsFromFirebase(): List<Product> = withContext(Dispatchers.IO) {
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("products")

    return@withContext try {
        val snapshot = ref.get().await()
        snapshot.children.mapNotNull { it.getValue(Product::class.java) }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
