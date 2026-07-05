package ir.keyvanadili.karmakhodro.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class VehicleType {
    CAR,        // خودرو سواری
    MOTORCYCLE, // موتورسیکلت
    TRUCK,      // وانت / کامیون
    VAN         // ون / مینی‌بوس
}

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,          // مثلا: پژو ۲۰۶ من
    val brandModel: String,    // برند و مدل
    val plateNumber: String,   // شماره پلاک
    val year: Int? = null,     // سال ساخت
    val color: String = "",
    val currentMileage: Int = 0,
    val notes: String = "",
    val vehicleType: String = VehicleType.CAR.name
)
