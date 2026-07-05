package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.ui.theme.serviceIconFor
import ir.keyvanadili.karmakhodro.ui.theme.vehicleTypeIcon
import ir.keyvanadili.karmakhodro.ui.theme.vehicleTypeLabel
import ir.keyvanadili.karmakhodro.util.NumberFormatUtils
import ir.keyvanadili.karmakhodro.util.PersianDateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    car: Car,
    records: List<ServiceRecord>,
    onBack: () -> Unit,
    onEditCar: () -> Unit,
    onAddRecord: () -> Unit,
    onRecordClick: (ServiceRecord) -> Unit,
    onUpdateMileage: (Int) -> Unit
) {
    var showMileageDialog by remember { mutableStateOf(false) }

    if (showMileageDialog) {
        MileageUpdateDialog(
            currentMileage = car.currentMileage,
            onDismiss = { showMileageDialog = false },
            onConfirm = { newMileage ->
                onUpdateMileage(newMileage)
                showMileageDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(car.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                },
                actions = {
                    IconButton(onClick = onEditCar) {
                        Icon(Icons.Filled.Edit, contentDescription = "ویرایش خودرو")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddRecord,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "افزودن سرویس")
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(vehicleTypeIcon(car.vehicleType), contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(vehicleTypeLabel(car.vehicleType), style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(car.brandModel, style = MaterialTheme.typography.titleMedium)
                    Text("پلاک: ${car.plateNumber}")
                    if (car.year != null) Text("سال ساخت: ${PersianDateUtils.toPersianDigits(car.year)}")
                    if (car.color.isNotBlank()) Text("رنگ: ${car.color}")

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("کیلومتر فعلی", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "${NumberFormatUtils.formatThousands(car.currentMileage)} کیلومتر",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        FilledTonalButton(onClick = { showMileageDialog = true }) {
                            Icon(Icons.Filled.Speed, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ثبت کیلومتر جدید")
                        }
                    }

                    if (car.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("یادداشت: ${car.notes}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Text(
                "تاریخچه سرویس و تعمیرات",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (records.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("هنوز رویدادی برای این خودرو ثبت نشده است")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(records) { record ->
                        ServiceRecordCard(record = record, onClick = { onRecordClick(record) })
                    }
                }
            }
        }
    }
}

@Composable
private fun MileageUpdateDialog(
    currentMileage: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var value by remember { mutableStateOf(currentMileage.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("ثبت کیلومتر فعلی") },
        text = {
            Column {
                Text("کیلومتر فعلی خودرو را وارد کنید. در صورت رسیدن به کیلومتر سرویسِ ثبت‌شده، یادآوری ارسال می‌شود.")
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it.filter { c -> c.isDigit() } },
                    label = { Text("کیلومتر جدید") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newMileage = value.toIntOrNull() ?: currentMileage
                onConfirm(newMileage)
            }) {
                Text("ثبت")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("انصراف") }
        }
    )
}

@Composable
private fun ServiceRecordCard(record: ServiceRecord, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    serviceIconFor(record.iconKey),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    record.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(PersianDateUtils.formatMillis(record.dateMillis), style = MaterialTheme.typography.bodyMedium)
            if (record.garageName.isNotBlank()) {
                Text("تعمیرگاه: ${record.garageName}", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${NumberFormatUtils.formatThousands(record.mileage)} کیلومتر",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (record.cost > 0) {
                    Text(
                        "${NumberFormatUtils.formatThousands(record.cost)} تومان",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
