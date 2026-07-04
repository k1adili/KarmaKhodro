package ir.keyvanadili.karmakhodro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.keyvanadili.karmakhodro.data.Car

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarListScreen(
    cars: List<Car>,
    onAddCarClick: () -> Unit,
    onCarClick: (Car) -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("کارما خودرو") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, contentDescription = "تنظیمات")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCarClick) {
                Icon(Icons.Filled.Add, contentDescription = "افزودن خودرو")
            }
        }
    ) { padding ->
        if (cars.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("هنوز خودرویی ثبت نکرده‌اید")
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("برای شروع، از دکمه + یک خودرو اضافه کنید")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(cars) { car ->
                    CarCard(car = car, onClick = { onCarClick(car) })
                }
            }
        }
    }
}

@Composable
private fun CarCard(car: Car, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.DirectionsCar,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(car.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(car.brandModel, style = MaterialTheme.typography.bodyMedium)
                Text("پلاک: ${car.plateNumber}", style = MaterialTheme.typography.bodyMedium)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${car.currentMileage} کیلومتر", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
