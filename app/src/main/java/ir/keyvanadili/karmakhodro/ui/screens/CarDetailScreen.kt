package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.ui.theme.KarmaNumericLarge
import ir.keyvanadili.karmakhodro.ui.theme.KarmaSpacing
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
                title = {
                    Text(car.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
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
            ExtendedFloatingActionButton(
                onClick = onAddRecord,
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("سرویس جدید") }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(KarmaSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(KarmaSpacing.lg)
        ) {
            item {
                MileageHeroCard(
                    car = car,
                    onUpdateMileageClick = { showMileageDialog = true }
                )
            }

            item { VehicleInfoRow(car = car) }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "تاریخچه سرویس و تعمیرات",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (records.isEmpty()) {
                item {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "هنوز رویدادی برای این خودرو ثبت نشده — با دکمه «سرویس جدید» شروع کنید",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(KarmaSpacing.lg)
                        )
                    }
                }
            } else {
                items(records, key = { it.id }) { record ->
                    ServiceRecordCard(record = record, onClick = { onRecordClick(record) })
                }
            }

            item { Spacer(modifier = Modifier.height(72.dp)) }
        }
    }
}

@Composable
private fun MileageHeroCard(car: Car, onUpdateMileageClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(KarmaSpacing.lg)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        vehicleTypeIcon(car.vehicleType),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(KarmaSpacing.sm))
                Column {
                    Text(
                        vehicleTypeLabel(car.vehicleType),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Text(
                        car.brandModel,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(KarmaSpacing.lg))

            Text(
                "کیلومتر فعلی",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    NumberFormatUtils.formatThousands(car.currentMileage),
                    style = KarmaNumericLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "کیلومتر",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(KarmaSpacing.md))

            FilledTonalButton(
                onClick = onUpdateMileageClick,
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.15f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Speed, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("ثبت کیلومتر جدید", maxLines = 1)
            }
        }
    }
}

@Composable
private fun VehicleInfoRow(car: Car) {
    val chips = buildList {
        if (car.plateNumber.isNotBlank()) add("پلاک" to car.plateNumber)
        if (car.year != null) add("سال" to PersianDateUtils.toPersianDigits(car.year))
        if (car.color.isNotBlank()) add("رنگ" to car.color)
    }
    if (chips.isEmpty() && car.notes.isBlank()) return

    Column {
        if (chips.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(KarmaSpacing.sm)) {
                chips.forEach { (label, value) ->
                    InfoChip(label = label, value = value, modifier = Modifier.weight(1f))
                }
            }
        }
        if (car.notes.isNotBlank()) {
            Spacer(modifier = Modifier.height(KarmaSpacing.sm))
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    car.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(KarmaSpacing.md)
                )
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = KarmaSpacing.sm, vertical = KarmaSpacing.sm),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
                Text(
                    "کیلومتر فعلی خودرو را وارد کنید. در صورت رسیدن به کیلومتر سرویسِ ثبت‌شده، یادآوری ارسال می‌شود.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(KarmaSpacing.md))
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it.filter { c -> c.isDigit() } },
                    label = { Text("کیلومتر جدید") },
                    singleLine = true,
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
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(KarmaSpacing.md)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    serviceIconFor(record.iconKey),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(KarmaSpacing.md))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    record.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.EventNote,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        PersianDateUtils.formatMillis(record.dateMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (record.garageName.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.Storefront,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            record.garageName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "${NumberFormatUtils.formatThousands(record.mileage)} کیلومتر",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (record.cost > 0) {
                        Text(
                            "${NumberFormatUtils.formatThousands(record.cost)} تومان",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
