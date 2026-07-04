package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.ui.components.PersianDatePickerDialog
import ir.keyvanadili.karmakhodro.util.DateUtils
import ir.keyvanadili.karmakhodro.util.PersianDateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceFormScreen(
    carId: Long,
    existingRecord: ServiceRecord?,
    onBack: () -> Unit,
    onSave: (ServiceRecord) -> Unit,
    onDelete: ((ServiceRecord) -> Unit)? = null
) {
    var title by remember { mutableStateOf(existingRecord?.title ?: "") }
    var description by remember { mutableStateOf(existingRecord?.description ?: "") }
    var mileage by remember { mutableStateOf(existingRecord?.mileage?.toString() ?: "") }
    var cost by remember { mutableStateOf(existingRecord?.cost?.toString() ?: "") }
    var garageName by remember { mutableStateOf(existingRecord?.garageName ?: "") }
    var dateMillis by remember { mutableStateOf(existingRecord?.dateMillis ?: DateUtils.nowMillis()) }
    var nextMileage by remember { mutableStateOf(existingRecord?.nextServiceMileage?.toString() ?: "") }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        PersianDatePickerDialog(
            initialMillis = dateMillis,
            onDismiss = { showDatePicker = false },
            onConfirm = { newMillis ->
                dateMillis = newMillis
                showDatePicker = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingRecord == null) "افزودن رویداد سرویس" else "ویرایش رویداد") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان (مثلا: تعویض روغن، باتری، لاستیک)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedCard(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("تاریخ سرویس (شمسی)", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            PersianDateUtils.formatMillis(dateMillis),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Icon(Icons.Filled.CalendarMonth, contentDescription = "انتخاب تاریخ")
                }
            }

            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter { c -> c.isDigit() } },
                label = { Text("کیلومتر در زمان سرویس") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cost,
                onValueChange = { cost = it.filter { c -> c.isDigit() } },
                label = { Text("هزینه (تومان)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = garageName,
                onValueChange = { garageName = it },
                label = { Text("نام تعمیرگاه / مکانیک") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("توضیحات") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            OutlinedTextField(
                value = nextMileage,
                onValueChange = { nextMileage = it.filter { c -> c.isDigit() } },
                label = { Text("یادآوری سرویس بعدی در چه کیلومتری (اختیاری)") },
                supportingText = { Text("وقتی کیلومتر فعلی خودرو به این عدد برسد، نوتیف یادآوری ارسال می‌شود") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val record = ServiceRecord(
                        id = existingRecord?.id ?: 0,
                        carId = carId,
                        dateMillis = dateMillis,
                        title = title.ifBlank { "رویداد سرویس" },
                        description = description,
                        mileage = mileage.toIntOrNull() ?: 0,
                        cost = cost.toLongOrNull() ?: 0,
                        garageName = garageName,
                        nextServiceMileage = nextMileage.toIntOrNull(),
                        nextServiceDateMillis = existingRecord?.nextServiceDateMillis,
                        nextServiceNotified = if (existingRecord != null &&
                            existingRecord.nextServiceMileage == nextMileage.toIntOrNull()
                        ) existingRecord.nextServiceNotified else false
                    )
                    onSave(record)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ذخیره")
            }

            if (existingRecord != null && onDelete != null) {
                OutlinedButton(
                    onClick = { onDelete(existingRecord) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف این رویداد")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
