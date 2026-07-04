package ir.keyvanadili.karmakhodro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.util.PersianDateUtils

/**
 * دیالوگ ساده انتخاب تاریخ شمسی (سال، ماه، روز) بدون نیاز به کتابخانه خارجی.
 */
@Composable
fun PersianDatePickerDialog(
    initialMillis: Long,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val initial = remember(initialMillis) { PersianDateUtils.millisToJalali(initialMillis) }

    var year by remember { mutableStateOf(initial.year.toString()) }
    var month by remember { mutableStateOf(initial.month.toString()) }
    var day by remember { mutableStateOf(initial.day.toString()) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("انتخاب تاریخ (شمسی)") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { year = it.filter { c -> c.isDigit() }.take(4) },
                        label = { Text("سال") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1.2f)
                    )
                    OutlinedTextField(
                        value = month,
                        onValueChange = { month = it.filter { c -> c.isDigit() }.take(2) },
                        label = { Text("ماه") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = day,
                        onValueChange = { day = it.filter { c -> c.isDigit() }.take(2) },
                        label = { Text("روز") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                val jy = year.toIntOrNull()
                val jm = month.toIntOrNull()
                val jd = day.toIntOrNull()
                if (jy != null && jm != null && jd != null && jm in 1..12 && jd in 1..31) {
                    Text("${PersianDateUtils.toPersianDigits(jd)} ${PersianDateUtils.monthName(jm)} ${PersianDateUtils.toPersianDigits(jy)}")
                }

                error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val jy = year.toIntOrNull()
                val jm = month.toIntOrNull()
                val jd = day.toIntOrNull()
                if (jy == null || jm == null || jd == null || jm !in 1..12 || jd !in 1..31) {
                    error = "تاریخ واردشده معتبر نیست"
                } else {
                    try {
                        val millis = PersianDateUtils.jalaliToMillis(jy, jm, jd)
                        onConfirm(millis)
                    } catch (e: Exception) {
                        error = "تاریخ واردشده معتبر نیست"
                    }
                }
            }) {
                Text("تایید")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}
