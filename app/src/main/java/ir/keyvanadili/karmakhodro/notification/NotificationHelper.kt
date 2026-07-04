package ir.keyvanadili.karmakhodro.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ir.keyvanadili.karmakhodro.R
import ir.keyvanadili.karmakhodro.util.NumberFormatUtils

/**
 * مسئول ساخت کانال نوتیفیکیشن و نمایش یادآوری سررسید سرویس بر اساس کیلومتر.
 */
object NotificationHelper {

    private const val CHANNEL_ID = "service_reminders"
    private const val CHANNEL_NAME = "یادآوری سرویس خودرو"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (manager.getNotificationChannel(CHANNEL_ID) == null) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "یادآوری زمان سرویس خودرو بر اساس کیلومتر طی‌شده"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    fun showServiceDueNotification(
        context: Context,
        notificationId: Int,
        carName: String,
        currentMileage: Int,
        dueMileage: Int
    ) {
        ensureChannel(context)

        val title = "سررسید سرویس: $carName"
        val text = "کیلومتر فعلی ${NumberFormatUtils.formatThousands(currentMileage)} به کیلومتر سرویس " +
                "${NumberFormatUtils.formatThousands(dueMileage)} رسیده یا گذشته است."

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // مجوز نوتیفیکیشن داده نشده؛ نادیده گرفته می‌شود.
        }
    }
}
