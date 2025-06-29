import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import com.example.myapplicationtryagain.data.GrowthRecord

@Composable
fun AddGrowthRecordDialog(
    onDismiss: () -> Unit,
    onSave: (GrowthRecord) -> Unit
) {
    val formatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    var inputDate by remember { mutableStateOf(LocalDate.now().format(formatter)) }
    var inputHeight by remember { mutableStateOf("") }
    var inputWeight by remember { mutableStateOf("") }
    val context = LocalContext.current

    // --- Для выбора даты через DatePickerDialog ---
    fun openDatePicker() {
        val now = LocalDate.now()
        val initial = try {
            LocalDate.parse(inputDate, formatter)
        } catch (_: Exception) {
            now
        }
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = LocalDate.of(year, month + 1, dayOfMonth)
                inputDate = selected.format(formatter)
            },
            initial.year,
            initial.monthValue - 1,
            initial.dayOfMonth
        ).show()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(13.dp),
            color = Color.White,
            tonalElevation = 10.dp,
        ) {
            Column(
                Modifier
                    .padding(24.dp)
                    .widthIn(min = 260.dp)
            ) {
                Text(
                    "Новая запись",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color(0xFF1976D2),
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                // --- Поле даты с иконкой календаря ---
                OutlinedTextField(
                    value = inputDate,
                    onValueChange = { inputDate = it },
                    label = { Text("Дата (дд.мм.гггг)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { openDatePicker() }) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = "Выбрать дату"
                            )
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputHeight,
                    onValueChange = { inputHeight = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Рост (см)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = inputWeight,
                    onValueChange = { inputWeight = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Вес (кг)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(18.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Отмена") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (inputDate.isNotBlank() && inputHeight.isNotBlank() && inputWeight.isNotBlank()) {
                                onSave(
                                    GrowthRecord(
                                        date = inputDate,
                                        height = inputHeight,
                                        weight = inputWeight
                                    )
                                )
                            }
                        }
                    ) { Text("Сохранить") }
                }
            }
        }
    }
}
