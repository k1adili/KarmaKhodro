package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.ui.components.FormSection
import ir.keyvanadili.karmakhodro.ui.components.PersianDatePickerDialog
import ir.keyvanadili.karmakhodro.ui.theme.KarmaSpacing
import ir.keyvanadili.karmakhodro.ui.theme.ServiceIconType
import ir.keyvanadili.karmakhodro.ui.theme.serviceIconFor
import ir.keyvanadili.karmakhodro.ui.theme.serviceIconLabel
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
    var iconKey by remember { mutableStateOf(existingRecord?.iconKey ?: ServiceIconType.GENERAL.name) }
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(KarmaSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(KarmaSpacing.xl)
        ) {
            FormSection(title = "نوع سرویس") {
                Column(verticalArrangement = Arrangement.spacedBy(KarmaSpacing.sm)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("عنوان (مثلا: تعویض روغن، باتری، لاستیک)") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(KarmaSpacing.sm)
                    ) {
                        ServiceIconType.values().forEach { type ->
                            ServiceIconOption(
                                selected = iconKey == type.name,
                                typeName = type.name,
                                label = serviceIconLabel(type.name),
                                onClick = { iconKey = type.name }
                            )
                        }
                    }
                }
            }

            FormSection(title = "تاریخ و کارکرد") {
                Column(verticalArrangement = Arrangement.spacedBy(KarmaSpacing.md)) {
                    OutlinedCard(
                        onClick = { showDatePicker = true },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(KarmaSpacing.md),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "تاریخ سرویس (شمسی)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    PersianDateUtils.formatMillis(dateMillis),
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                            Icon(
                                Icons.Filled.CalendarMonth,
                                contentDescription = "انتخاب تاریخ",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    OutlinedTextField(
                        value = mileage,
                        onValueChange = { mileage = it.filter { c -> c.isDigit() } },
                        label = { Text("کیلومتر در زمان سرویس") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = cost,
                        onValueChange = { cost = it.filter { c -> c.isDigit() } },
                        label = { Text("هزینه (تومان)") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = garageName,
                        onValueChange = { garageName = it },
                        label = { Text("نام تعمیرگاه / مکانیک") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("توضیحات") },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }

            FormSection(title = "یادآوری سرویس بعدی") {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(KarmaSpacing.md)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.NotificationsActive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "وقتی کیلومتر فعلی خودرو به این عدد برسد، نوتیف یادآوری ارسال می‌شود",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(KarmaSpacing.sm))
                        OutlinedTextField(
                            value = nextMileage,
                            onValueChange = { nextMileage = it.filter { c -> c.isDigit() } },
                            label = { Text("کیلومتر سرویس بعدی (اختیاری)") },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

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
                        ) existingRecord.nextServiceNotified else false,
                        iconKey = iconKey
                    )
                    onSave(record)
                },
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ذخیره", style = MaterialTheme.typography.titleSmall, maxLines = 1)
            }

            if (existingRecord != null && onDelete != null) {
                OutlinedButton(
                    onClick = { onDelete(existingRecord) },
                    shape = MaterialTheme.shapes.large,
                    contentPadding = PaddingValues(vertical = 14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("حذف این رویداد", maxLines = 1)
                }
            }

            Spacer(modifier = Modifier.height(KarmaSpacing.lg))
        }
    }
}

@Composable
private fun ServiceIconOption(
    selected: Boolean,
    typeName: String,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        FilledIconToggleButton(
            checked = selected,
            onCheckedChange = { onClick() },
            shape = CircleShape,
            colors = IconButtonDefaults.filledIconToggleButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                checkedContainerColor = MaterialTheme.colorScheme.secondary,
                checkedContentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(serviceIconFor(typeName), contentDescription = label)
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
