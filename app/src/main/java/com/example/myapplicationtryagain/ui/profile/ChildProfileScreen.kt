package com.example.myapplicationtryagain.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDate
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import java.time.LocalDate as JavaLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavController

// NEW: Импорт для DataStore
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.myapplicationtryagain.data.ProfileDataStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditChildProfileScreen(
    navController: NavController,
    initialProfile: ChildProfile? = null,
    onSave: (ChildProfile) -> Unit,
    onCancel: () -> Unit,
    onOpenVaccineCard: () -> Unit
) {
    var name by remember { mutableStateOf(initialProfile?.name ?: "") }
    var birthDate by remember {
        mutableStateOf(
            try {
                LocalDate.parse(initialProfile?.birthDate ?: JavaLocalDate.now().toString())
            } catch (_: Exception) {
                JavaLocalDate.now().toKotlinLocalDate()
            }
        )
    }
    var gender by remember { mutableStateOf(initialProfile?.gender ?: "Мальчик") }
    var weight by remember { mutableStateOf(initialProfile?.weight ?: "") }
    var height by remember { mutableStateOf(initialProfile?.height ?: "") }
    var notes by remember { mutableStateOf(initialProfile?.notes ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    // DataStore
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isProfileLoaded by remember { mutableStateOf(false) }

    // Аллергии
    val possibleAllergies = listOf(
        "Молоко", "Яйцо", "Орехи", "Рыба", "Морепродукты", "Пшеница", "Арахис", "Соя"
    )
    var allergies by remember { mutableStateOf(initialProfile?.allergies ?: emptyList<String>()) }
    var expandedAllergy by remember { mutableStateOf(false) }

    // Пол
    val genders = listOf("Мальчик", "Девочка")
    var expandedGender by remember { mutableStateOf(false) }

    // Стили
    val containerColor = Color(0xFFF7F9FA)
    val cardShape = RoundedCornerShape(16.dp)
    val fieldBg = Color(0xFFF7F9FA)

    LaunchedEffect(Unit) {
        if (!isProfileLoaded && initialProfile == null) {
            val savedProfile = ProfileDataStore.loadProfile(context)
            if (savedProfile != null) {
                name = savedProfile.name
                birthDate = LocalDate.parse(savedProfile.birthDate)
                gender = savedProfile.gender
                weight = savedProfile.weight
                height = savedProfile.height
                notes = savedProfile.notes
                allergies = savedProfile.allergies
            }
            isProfileLoaded = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor)
    ) {
        // Аватарка вне карточки
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFDDE6EF),
                shadowElevation = 7.dp,
                modifier = Modifier.size(90.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "\uD83D\uDC76",
                        fontSize = 44.sp,
                        color = Color(0xFF90A4AE)
                    )
                }
            }
        }

        // Карточка формы
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 140.dp, bottom = 0.dp)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                shape = cardShape,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Имя (Кастомное поле)
                    ProfileFormRow(
                        label = "Имя",
                        input = {
                            CustomInputField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = "Имя ребёнка",
                                keyboardType = KeyboardType.Text,
                                modifier = Modifier.fillMaxWidth().height(36.dp)
                            )
                        }
                    )

                    // Пол (DropdownField)
                    ProfileFormRow(
                        label = "Пол",
                        input = {
                            DropdownField(
                                options = genders,
                                selectedOption = gender,
                                onOptionSelected = { gender = it },
                                placeholder = "Выберите пол",
                                expanded = expandedGender,
                                onExpandedChange = { expandedGender = it }
                            )
                        }
                    )

                    // Дата рождения
                    ProfileFormRow(
                        label = "Дата рождения",
                        input = {
                            Button(
                                onClick = { showDatePicker = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = fieldBg),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "${
                                        birthDate.dayOfMonth.toString().padStart(2, '0')
                                    }.${
                                        birthDate.monthNumber.toString().padStart(2, '0')
                                    }.${birthDate.year}",
                                    color = Color(0xFF263238),
                                    fontSize = 15.sp
                                )
                            }
                        }
                    )

                    // Рост (Кастомное поле)
                    ProfileFormRow(
                        label = "Рост",
                        input = {
                            CustomInputField(
                                value = height,
                                onValueChange = { height = it },
                                placeholder = "см",
                                keyboardType = KeyboardType.Number
                            )
                        }
                    )

                    // Вес (Кастомное поле)
                    ProfileFormRow(
                        label = "Вес",
                        input = {
                            CustomInputField(
                                value = weight,
                                onValueChange = { weight = it },
                                placeholder = "кг",
                                keyboardType = KeyboardType.Number
                            )
                        }
                    )

                    // Аллергии (DropdownField, множественный выбор)
                    ProfileFormRow(
                        label = "Аллергии",
                        input = {
                            MultiSelectDropdownField(
                                options = possibleAllergies,
                                selectedOptions = allergies,
                                onOptionToggled = { allergy ->
                                    allergies = if (allergy in allergies)
                                        allergies - allergy
                                    else
                                        allergies + allergy
                                },
                                expanded = expandedAllergy,
                                onExpandedChange = { expandedAllergy = it }
                            )
                        }
                    )

                    // Список выбранных аллергий — справа в столбик
                    if (allergies.isNotEmpty()) {
                        Row(
                            Modifier
                                .padding(top = 0.dp, start = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                allergies.forEach { allergy ->
                                    Surface(
                                        color = Color(0xFFE3F2FD),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .padding(vertical = 2.dp)
                                            .widthIn(min = 72.dp)
                                            .height(28.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier.padding(horizontal = 10.dp)
                                        ) {
                                            Text(
                                                allergy,
                                                color = Color(0xFF1976D2),
                                                fontSize = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    ProfileFormRow(
                        label = "Карта прививок",
                        input = {
                            Button(
                                onClick = { navController.navigate("vaccine_card") },
                                modifier = Modifier.height(38.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xFF1976D2
                                    )
                                )
                            ) {
                                Text("Открыть", color = Color.White, fontSize = 15.sp)
                            }
                        }
                    )

                    ProfileFormRow(
                        label = "Таблица роста и веса",
                        input = {
                            Button(
                                onClick = { navController.navigate("growth_table") },
                                modifier = Modifier.height(38.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                            ) {
                                Text("Открыть", color = Color.White, fontSize = 15.sp)
                            }
                        }
                    )

                    // Заметки (можно оставить обычное поле, либо сделать кастомным, если нужно)
                    ProfileFormRow(
                        label = "Заметки",
                        input = {
                            CustomInputField(
                                value = notes,
                                onValueChange = { notes = it },
                                placeholder = "Особенности, предпочтения...",
                                keyboardType = KeyboardType.Text,
                                singleLine = false
                            )
                        }
                    )

                    Spacer(Modifier.weight(1f)) // чтобы кнопка прижалась к низу карточки

                    // Кнопка сохранить — теперь всегда внизу карточки и по центру
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val newProfile = ChildProfile(
                                    name = name,
                                    birthDate = birthDate.toString(),
                                    gender = gender,
                                    weight = weight,
                                    height = height,
                                    allergies = allergies,
                                    notes = notes
                                )
                                // NEW: Сохраняем в DataStore
                                scope.launch {
                                    ProfileDataStore.saveProfile(context, newProfile)
                                }
                                onSave(newProfile)
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(44.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                        ) {
                            Text("Сохранить", color = Color.White, fontSize = 18.sp)
                        }
                    }
                }
            }
        }

        // WheelDatePicker — отдельный диалог
        if (showDatePicker) {
            AlertDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    Button(onClick = { showDatePicker = false }) { Text("ОК") }
                },
                dismissButton = {
                    OutlinedButton(onClick = { showDatePicker = false }) { Text("Отмена") }
                },
                text = {
                    WheelDatePicker(
                        startDate = birthDate,
                        minDate = LocalDate(2010, 1, 1),
                        maxDate = today,
                        onSnappedDate = { newDate ->
                            birthDate = newDate
                        }
                    )
                }
            )
        }
    }
}

// --- КАСТОМНОЕ ПОЛЕ ВВОДА --- //
@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    modifier: Modifier = Modifier
) {
    val fieldBg = Color(0xFFF7F9FA)
    val borderColor = Color(0xFFCFD8DC)
    val textColor = Color(0xFF263238)
    val placeholderColor = Color(0xFFB0BEC5)

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
                singleLine = singleLine,
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
                            color = placeholderColor,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

// Dropdown для одиночного выбора (оставь свой кастомный)
@Composable
fun DropdownField(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    placeholder: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFFF7F9FA), RoundedCornerShape(8.dp))
            .clickable { onExpandedChange(!expanded) }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (selectedOption.isBlank()) placeholder else selectedOption,
                fontSize = 15.sp,
                color = if (selectedOption.isBlank()) Color(0xFFB0BEC5) else Color(0xFF263238),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    }
                )
            }
        }
    }
}

// Мультивыбор для аллергий (оставь свой кастомный)
@Composable
fun MultiSelectDropdownField(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionToggled: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color(0xFFF7F9FA), RoundedCornerShape(8.dp))
            .clickable { onExpandedChange(!expanded) }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Нет",
                fontSize = 15.sp,
                color = Color(0xFFB0BEC5),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = option in selectedOptions,
                                onCheckedChange = { onOptionToggled(option) }
                            )
                            Text(option)
                        }
                    },
                    onClick = { onOptionToggled(option) }
                )
            }
        }
    }
}

// Вспомогательная строка
@Composable
private fun ProfileFormRow(
    label: String,
    input: @Composable () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f), color = Color(0xFF455A64), fontSize = 15.sp)
        Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.weight(2f)) {
            input()
        }
    }
}
