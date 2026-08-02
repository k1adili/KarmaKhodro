package ir.keyvanadili.karmakhodro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.util.PersianDateUtils

/**
 * دیالوگ انتخاب تاریخ شمسی با سه منوی کشویی (روز، ماه، سال) — بدون نیاز به تایپ.
 * روزهای قابل انتخاب به‌صورت خودکار بر اساس ماه/سال انتخاب‌شده تنظیم می‌شوند
 * (مثلا اسفند بسته به کبیسه بودن سال، ۲۹ یا ۳۰ روز نشان داده می‌شود).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersianDatePickerDialog(
    initialMillis: Long,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit
) {
    val initial = remember(initialMillis) { PersianDateUtils.millisToJalali(initialMillis) }

    var selectedYear by remember { mutableStateOf(initial.year) }
    var selectedMonth by remember { mutableStateOf(initial.month) }
    var selectedDay by remember { mutableStateOf(initial.day) }

    // وقتی ماه یا سال تغییر می‌کند، اگر روز انتخاب‌شده از تعداد روزهای ماه جدید بیشتر باشد، تنظیم می‌شود
    val maxDayInSelectedMonth = remember(selectedYear, selectedMonth) {
        PersianDateUtils.daysInJalaliMonth(selectedYear, selectedMonth)
    }
    LaunchedEffect(maxDayInSelectedMonth) {
        if (selectedDay > maxDayInSelectedMonth) {
            selectedDay = maxDayInSelectedMonth
        }
    }

    val currentYear = remember { PersianDateUtils.currentJalali().year }
    val yearRange = remember(currentYear) { (currentYear - 15)..(currentYear + 5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("انتخاب تاریخ (شمسی)") },
        text = {
            Column {
                Text(
                    "${PersianDateUtils.toPersianDigits(selectedDay)} ${PersianDateUtils.monthName(selectedMonth)} ${PersianDateUtils.toPersianDigits(selectedYear)}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SimpleDropdown(
                        modifier = Modifier.weight(0.8f),
                        label = "روز",
                        selectedText = PersianDateUtils.toPersianDigits(selectedDay),
                        options = (1..maxDayInSelectedMonth).map { it.toString() to PersianDateUtils.toPersianDigits(it) },
                        onOptionSelected = { selectedDay = it.toInt() }
                    )
                    SimpleDropdown(
                        modifier = Modifier.weight(1.3f),
                        label = "ماه",
                        selectedText = PersianDateUtils.monthName(selectedMonth),
                        options = PersianDateUtils.monthNames().mapIndexed { index, name -> (index + 1).toString() to name },
                        onOptionSelected = { selectedMonth = it.toInt() }
                    )
                    SimpleDropdown(
                        modifier = Modifier.weight(1f),
                        label = "سال",
                        selectedText = PersianDateUtils.toPersianDigits(selectedYear),
                        options = yearRange.map { it.toString() to PersianDateUtils.toPersianDigits(it) },
                        onOptionSelected = { selectedYear = it.toInt() }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val millis = PersianDateUtils.jalaliToMillis(selectedYear, selectedMonth, selectedDay)
                onConfirm(millis)
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

/**
 * یک منوی کشویی ساده و فقط‌خواندنی (بدون امکان تایپ) برای انتخاب از میان گزینه‌ها.
 * options: لیستی از (مقدار خام, متن نمایشی)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDropdown(
    modifier: Modifier = Modifier,
    label: String,
    selectedText: String,
    options: List<Pair<String, String>>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (value, display) ->
                DropdownMenuItem(
                    text = { Text(display) },
                    onClick = {
                        onOptionSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}
