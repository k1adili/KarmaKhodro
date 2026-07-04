package ir.keyvanadili.karmakhodro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ir.keyvanadili.karmakhodro.ui.navigation.KarmaKhodroNavGraph
import ir.keyvanadili.karmakhodro.ui.theme.KarmaKhodroTheme

class MainActivity : ComponentActivity() {

    private val app: KarmaKhodroApp by lazy { application as KarmaKhodroApp }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KarmaKhodroTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    KarmaKhodroNavGraph(repository = app.repository)
                }
            }
        }
    }
}
