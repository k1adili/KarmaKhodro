package ir.keyvanadili.karmakhodro.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalCarWash
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TripOrigin
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * انواع آیکون قابل انتخاب برای هر رویداد سرویس (تعویض روغن، تسمه، لاستیک و ...)
 */
enum class ServiceIconType {
    GENERAL,     // عمومی (آچار)
    OIL,         // تعویض روغن
    TIRE,        // لاستیک
    BATTERY,     // باتری
    BELT,        // تسمه
    FILTER,      // فیلتر (هوا، بنزین، کابین)
    BRAKE,       // ترمز / لنت
    ELECTRICAL,  // برق‌کاری
    AC,          // کولر
    LIGHTS,      // چراغ‌ها
    WASH,        // کارواش / نظافت
    FUEL         // سوخت‌رسانی / باک
}

fun serviceIconFor(key: String?): ImageVector {
    return when (key) {
        ServiceIconType.OIL.name -> Icons.Filled.Opacity
        ServiceIconType.TIRE.name -> Icons.Filled.TripOrigin
        ServiceIconType.BATTERY.name -> Icons.Filled.BatteryChargingFull
        ServiceIconType.BELT.name -> Icons.Filled.Sync
        ServiceIconType.FILTER.name -> Icons.Filled.FilterAlt
        ServiceIconType.BRAKE.name -> Icons.Filled.Warning
        ServiceIconType.ELECTRICAL.name -> Icons.Filled.Bolt
        ServiceIconType.AC.name -> Icons.Filled.AcUnit
        ServiceIconType.LIGHTS.name -> Icons.Filled.Lightbulb
        ServiceIconType.WASH.name -> Icons.Filled.LocalCarWash
        ServiceIconType.FUEL.name -> Icons.Filled.LocalGasStation
        else -> Icons.Filled.Build
    }
}

fun serviceIconLabel(key: String?): String {
    return when (key) {
        ServiceIconType.OIL.name -> "روغن"
        ServiceIconType.TIRE.name -> "لاستیک"
        ServiceIconType.BATTERY.name -> "باتری"
        ServiceIconType.BELT.name -> "تسمه"
        ServiceIconType.FILTER.name -> "فیلتر"
        ServiceIconType.BRAKE.name -> "ترمز"
        ServiceIconType.ELECTRICAL.name -> "برق‌کاری"
        ServiceIconType.AC.name -> "کولر"
        ServiceIconType.LIGHTS.name -> "چراغ‌ها"
        ServiceIconType.WASH.name -> "کارواش"
        ServiceIconType.FUEL.name -> "سوخت"
        else -> "عمومی"
    }
}
