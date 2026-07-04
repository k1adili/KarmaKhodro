package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.Car
import ir.keyvanadili.karmakhodro.data.ServiceRecord
import ir.keyvanadili.karmakhodro.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    car: Car,
    records: List<ServiceRecord>,
    onBack: () -> Unit,
    onEditCar: () -> Unit,
    onAddRecord: () -> Unit,
    onRecordClick: (ServiceRecord) -> Unit
) {
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
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRecord) {
                Icon(Icons.Filled.Add, contentDescription = "افزودن سرویس")
            }
        }
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
                    Text(car.brandModel, style = MaterialTheme.typography.titleMedium)
                    Text("پلاک: ${car.plateNumber}")
                    if (car.year != null) Text("سال ساخت: ${car.year}")
                    if (car.color.isNotBlank()) Text("رنگ: ${car.color}")
                    Text("کیلومتر فعلی: ${car.currentMileage}")
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
private fun ServiceRecordCard(record: ServiceRecord, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth(), onClick = onClick) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Build, contentDescription = null)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(record.title, fontWeight = FontWeight.Bold)
                Text(DateUtils.formatMillis(record.dateMillis), style = MaterialTheme.typography.bodyMedium)
                if (record.garageName.isNotBlank()) {
                    Text("تعمیرگاه: ${record.garageName}", style = MaterialTheme.typography.bodyMedium)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${record.mileage} کیلومتر", style = MaterialTheme.typography.bodyMedium)
                if (record.cost > 0) {
                    Text("${record.cost} تومان", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
