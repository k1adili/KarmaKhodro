package ir.keyvanadili.karmakhodro.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.ui.graphics.vector.ImageVector
import ir.keyvanadili.karmakhodro.data.VehicleType

/**
 * انتخاب آیکون مناسب بر اساس نوع وسیله نقلیه (خودرو، موتورسیکلت، وانت/کامیون، ون)
 */
fun vehicleTypeIcon(vehicleType: String?): ImageVector {
    return when (vehicleType) {
        VehicleType.MOTORCYCLE.name -> Icons.Filled.TwoWheeler
        VehicleType.TRUCK.name -> Icons.Filled.LocalShipping
        VehicleType.VAN.name -> Icons.Filled.AirportShuttle
        else -> Icons.Filled.DirectionsCar
    }
}

fun vehicleTypeLabel(vehicleType: String?): String {
    return when (vehicleType) {
        VehicleType.MOTORCYCLE.name -> "موتورسیکلت"
        VehicleType.TRUCK.name -> "وانت / کامیون"
        VehicleType.VAN.name -> "ون / مینی‌بوس"
        else -> "خودرو سواری"
    }
}
