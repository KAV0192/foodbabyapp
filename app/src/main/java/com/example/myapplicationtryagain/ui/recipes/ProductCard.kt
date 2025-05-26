package com.example.myapplicationtryagain.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapplicationtryagain.model.Product
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale


@Composable
fun ProductCard(product: Product, onClick: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick(product) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = product.imageurl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "${product.agemin}+ мес", style = MaterialTheme.typography.bodySmall)
            Text(text = if (product.allergen) "Аллерген: Да" else "Аллерген: Нет", style = MaterialTheme.typography.bodySmall)
        }
    }
}


