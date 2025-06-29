package com.example.myapplicationtryagain.ui.test

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material3.OutlinedTextField

@Composable
fun KeyboardTest() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        label = { androidx.compose.material3.Text("Test") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

