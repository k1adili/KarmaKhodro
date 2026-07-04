package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.Car

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingCar == null) "افزودن خودرو" else "ویرایش خودرو") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "بازگشت")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("نام دلخواه خودرو (مثلا: پژوی من)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = brandModel,
                onValueChange = { brandModel = it },
                label = { Text("برند و مدل") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = plateNumber,
                onValueChange = { plateNumber = it },
                label = { Text("شماره پلاک") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it.filter { c -> c.isDigit() } },
                    label = { Text("سال ساخت") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("رنگ") },
                    modifier = Modifier.weight(1f)
                )
            }
            OutlinedTextField(
                value = mileage,
                onValueChange = { mileage = it.filter { c -> c.isDigit() } },
                label = { Text("کیلومتر فعلی") },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("یادداشت") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                        notes = notes
                    )
                    onSave(car)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("ذخیره")
            }

            if (existingCar != null && onDelete != null) {
                OutlinedButton(
                    onClick = { onDelete(existingCar) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("حذف این خودرو")
                }
            }
        }
    }
}
