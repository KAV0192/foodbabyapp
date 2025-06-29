package com.example.myapplicationtryagain.ui.test

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TestDropdownFields() {
    var name by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    val genders = listOf("Мальчик", "Девочка")
    var gender by remember { mutableStateOf(genders.first()) }
    var genderDropdownExpanded by remember { mutableStateOf(false) }

    // Для автопоказа клавиатуры на поле "Имя"
    val nameFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Цвета
    val fieldBg = Color(0xFFF7F9FA)
    val borderColor = Color(0xFFCFD8DC)
    val textColor = Color(0xFF263238)
    val labelColor = Color(0xFF455A64)

    // Показываем клавиатуру при старте
    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
        keyboardController?.show()
    }

    // Карточка формы
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FA)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .background(Color(0xFFE8E0EC), RoundedCornerShape(20.dp))
                .padding(24.dp)
                .widthIn(max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Имя
            FormRow(label = "Имя") {
                CustomInputField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Имя ребёнка",
                    modifier = Modifier.focusRequester(nameFocusRequester),
                    keyboardType = KeyboardType.Text
                )
            }
            // Пол (дропдаун)
            FormRow(label = "Пол") {
                Box {
                    CustomInputField(
                        value = gender,
                        onValueChange = {},
                        placeholder = "Выберите пол",
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                tint = Color(0xFF1976D2),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable { genderDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier.clickable { genderDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = genderDropdownExpanded,
                        onDismissRequest = { genderDropdownExpanded = false }
                    ) {
                        genders.forEach {
                            DropdownMenuItem(
                                text = { Text(it, fontSize = 15.sp) },
                                onClick = {
                                    gender = it
                                    genderDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            // Рост
            FormRow(label = "Рост") {
                CustomInputField(
                    value = height,
                    onValueChange = { height = it },
                    placeholder = "см",
                    keyboardType = KeyboardType.Number
                )
            }
            // Вес
            FormRow(label = "Вес") {
                CustomInputField(
                    value = weight,
                    onValueChange = { weight = it },
                    placeholder = "кг",
                    keyboardType = KeyboardType.Number
                )
            }
        }
    }
}

@Composable
fun FormRow(
    label: String,
    content: @Composable () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF455A64), fontSize = 15.sp, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(10.dp))
        Box(modifier = Modifier.weight(2f)) { content() }
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    trailingIcon: (@Composable (() -> Unit))? = null,
) {
    val fieldBg = Color(0xFFF7F9FA)
    val borderColor = Color(0xFFCFD8DC)
    val textColor = Color(0xFF263238)

    Box(
        modifier = modifier
            .background(fieldBg, RoundedCornerShape(8.dp))
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .height(40.dp)
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                enabled = enabled,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = LocalTextStyle.current.copy(
                    color = textColor,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFFB0BEC5),
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )
            if (trailingIcon != null) {
                Box(modifier = Modifier.padding(start = 2.dp)) {
                    trailingIcon()
                }
            }
        }
    }
}
