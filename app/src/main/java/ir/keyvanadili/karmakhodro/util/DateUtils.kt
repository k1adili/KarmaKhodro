package ir.keyvanadili.karmakhodro.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val formatter = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    fun formatMillis(millis: Long): String {
        return formatter.format(Date(millis))
    }

    fun nowMillis(): Long = System.currentTimeMillis()
}
