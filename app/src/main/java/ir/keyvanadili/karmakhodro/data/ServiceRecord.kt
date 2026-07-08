package ir.keyvanadili.karmakhodro.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_records",
    foreignKeys = [
        ForeignKey(
            entity = Car::class,
            parentColumns = ["id"],
            childColumns = ["carId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("carId")]
)
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val carId: Long,
    val dateMillis: Long,          // تاریخ سرویس/تعمیر
    val title: String,             // مثلا: تعویض روغن
    val description: String = "",
    val mileage: Int = 0,          // کیلومتر در زمان سرویس
    val cost: Long = 0,            // هزینه به تومان
    val garageName: String = "",   // نام تعمیرگاه
    val nextServiceMileage: Int? = null, // یادآوری کیلومتر بعدی
    val nextServiceDateMillis: Long? = null, // یادآوری تاریخ بعدی
    val nextServiceNotified: Boolean = false // آیا نوتیف سررسید این رویداد قبلا ارسال شده
)
