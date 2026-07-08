package ir.keyvanadili.karmakhodro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import ir.keyvanadili.karmakhodro.notification.NotificationHelper
import ir.keyvanadili.karmakhodro.ui.navigation.KarmaKhodroNavGraph
import ir.keyvanadili.karmakhodro.ui.theme.KarmaKhodroTheme

class MainActivity : ComponentActivity() {

    private val app: KarmaKhodroApp by lazy { application as KarmaKhodroApp }

    private val requestNotificationPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* نتیجه نیازی به پردازش خاصی ندارد؛ کاربر می‌تواند بعدا از تنظیمات سیستم فعال کند */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        NotificationHelper.ensureChannel(this)
        askNotificationPermissionIfNeeded()

        setContent {
            // برنامه همیشه راست‌چین نمایش داده می‌شود (فارغ از زبان سیستم)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                KarmaKhodroTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        KarmaKhodroNavGraph(repository = app.repository)
                    }
                }
            }
        }
    }

    private fun askNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
