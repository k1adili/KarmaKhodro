package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.VehicleType
import ir.keyvanadili.karmakhodro.ui.components.FormSection
import ir.keyvanadili.karmakhodro.ui.theme.KarmaSpacing
import ir.keyvanadili.karmakhodro.ui.theme.vehicleTypeIcon
import ir.keyvanadili.karmakhodro.ui.theme.vehicleTypeLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarFormScreen(
    existingCar: Car?,
    onBack: () -> Unit,
    onSave: (Car) -> Unit,
    onDelete: ((Car) -> Unit)? = null
) {
    var name by remember { mutableStateOf(existingCar?.name ?: "") }
    var brandModel by remember { mutableStateOf(existingCar?.brandModel ?: "") }
    var plateNumber by remember { mutableStateOf(existingCar?.plateNumber ?: "") }
    var year by remember { mutableStateOf(existingCar?.year?.toString() ?: "") }
    var color by remember { mutableStateOf(existingCar?.color ?: "") }
    var mileage by remember { mutableStateOf(existingCar?.currentMileage?.toString() ?: "0") }
    var notes by remember { mutableStateOf(existingCar?.notes ?: "") }
    var vehicleType by remember {
        mutableStateOf(existingCar?.vehicleType ?: VehicleType.CAR.name)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingCar == null) "افزودن خودرو" else "ویرایش خودرو") },
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
            FormSection(title = "نوع وسیله نقلیه") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(KarmaSpacing.sm)
                ) {
                    VehicleType.values().forEach { type ->
                        VehicleTypeOption(
                            selected = vehicleType == type.name,
                            typeName = type.name,
                            label = vehicleTypeLabel(type.name),
                            onClick = { vehicleType = type.name }
                        )
                    }
                }
            }

            FormSection(title = "مشخصات خودرو") {
                Column(verticalArrangement = Arrangement.spacedBy(KarmaSpacing.md)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("نام دلخواه خودرو (مثلا: پژوی من)") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = brandModel,
                        onValueChange = { brandModel = it },
                        label = { Text("برند و مدل") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = plateNumber,
                        onValueChange = { plateNumber = it },
                        label = { Text("شماره پلاک") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(KarmaSpacing.md)) {
                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it.filter { c -> c.isDigit() } },
                            label = { Text("سال ساخت") },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = color,
                            onValueChange = { color = it },
                            label = { Text("رنگ") },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            FormSection(title = "کارکرد و یادداشت") {
                Column(verticalArrangement = Arrangement.spacedBy(KarmaSpacing.md)) {
                    OutlinedTextField(
                        value = mileage,
                        onValueChange = { mileage = it.filter { c -> c.isDigit() } },
                        label = { Text("کیلومتر فعلی") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("یادداشت") },
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }

            Button(
                onClick = {
                    val car = Car(
                        id = existingCar?.id ?: 0,
                        name = name.ifBlank { "خودرو" },
                        brandModel = brandModel,
                        plateNumber = plateNumber,
                        year = year.toIntOrNull(),
                        color = color,
                        currentMileage = mileage.toIntOrNull() ?: 0,
                        notes = notes,
                        vehicleType = vehicleType
                    )
                    onSave(car)
                },
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ذخیره", style = MaterialTheme.typography.titleSmall, maxLines = 1)
            }

            if (existingCar != null && onDelete != null) {
                OutlinedButton(
                    onClick = { onDelete(existingCar) },
                    shape = MaterialTheme.shapes.large,
                    contentPadding = PaddingValues(vertical = 14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("حذف این خودرو", maxLines = 1)
                }
            }

            Spacer(modifier = Modifier.height(KarmaSpacing.lg))
        }
    }
}

@Composable
private fun VehicleTypeOption(
    selected: Boolean,
    typeName: String,
    label: String,
    onClick: () -> Unit
) {
    val containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant

    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = containerColor,
        contentColor = contentColor,
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null,
        modifier = Modifier.width(84.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = KarmaSpacing.sm, horizontal = KarmaSpacing.xs),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(vehicleTypeIcon(typeName), contentDescription = null)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
        }
    }
}
