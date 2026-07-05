package ir.keyvanadili.karmakhodro.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = KarmaBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD3E4FD),
    onPrimaryContainer = Color(0xFF0D3B75),
    secondary = KarmaOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDCCF),
    onSecondaryContainer = Color(0xFF7A2900),
    background = KarmaBackground,
    surface = Color.White,
    surfaceVariant = Color(0xFFE9F1FB),
    onSurfaceVariant = Color(0xFF3C4A5C),
    error = KarmaError
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF08315C),
    primaryContainer = KarmaBlueDark,
    onPrimaryContainer = Color(0xFFD3E4FD),
    secondary = KarmaOrange,
    onSecondary = Color(0xFF3E1400),
    background = Color(0xFF10151C),
    surface = Color(0xFF1A2027),
    surfaceVariant = Color(0xFF1F2A36),
    error = KarmaError
)

@Composable
fun KarmaKhodroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KarmaKhodroTypography,
        content = content
    )
}
