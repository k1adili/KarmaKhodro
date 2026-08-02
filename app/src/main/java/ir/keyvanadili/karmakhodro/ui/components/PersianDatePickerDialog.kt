package ir.keyvanadili.karmakhodro.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.util.PersianDateUtils

/**
 * دیالوگ انتخاب تاریخ شمسی با سه منوی کشویی (روز، ماه، سال) — بدون نیاز به تایپ.
 *
 * توجه: عمداً از OutlinedTextField/ExposedDropdownMenuBox استفاده نشده، چون در برخی
 * ترکیب‌های عرض محدود + RTL باعث شکستن عمودی متن می‌شد. به‌جایش از یک دکمه ساده
 * (OutlinedButton) با متن تک‌خطی اجباری (maxLines = 1) و یک DropdownMenu استفاده شده
 * که این مشکل را کاملاً برطرف می‌کند.
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
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SimpleDropdown(
                        modifier = Modifier.weight(0.8f),
                        label = "روز",
                        selectedText = PersianDateUtils.toPersianDigits(selectedDay),
                        options = (1..maxDayInSelectedMonth).map { it.toString() to PersianDateUtils.toPersianDigits(it) },
                        onOptionSelected = { selectedDay = it.toInt() }
                    )
                    SimpleDropdown(
                        modifier = Modifier.weight(1.4f),
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
 * یک منوی کشویی ساده برای انتخاب از میان گزینه‌ها — بدون TextField، فقط یک دکمه
 * با متن اجباراً تک‌خط (maxLines = 1) به‌علاوه یک DropdownMenu.
 * options: لیستی از (مقدار خام, متن نمایشی)
 */
@Composable
private fun SimpleDropdown(
    modifier: Modifier = Modifier,
    label: String,
    selectedText: String,
    options: List<Pair<String, String>>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        selectedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (value, display) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                display,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        onClick = {
                            onOptionSelected(value)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
