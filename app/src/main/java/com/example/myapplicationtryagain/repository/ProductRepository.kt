package com.example.myapplicationtryagain.repository

import com.example.myapplicationtryagain.model.Product
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProductRepository {

    private val database = Firebase.database.reference.child("products")

    fun getProducts(callback: (List<Product>) -> Unit) {
        database.get()
            .addOnSuccessListener { snapshot ->
                val productList = snapshot.children.mapNotNull { it.getValue(Product::class.java) }
                callback(productList)
            }
            .addOnFailureListener {
                it.printStackTrace()
                callback(emptyList()) // Если ошибка — вернем пустой список
            }
    }
}
