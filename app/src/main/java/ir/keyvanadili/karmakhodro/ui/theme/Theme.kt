package ir.keyvanadili.karmakhodro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PetrolBlue40,
    onPrimary = Color.White,
    primaryContainer = PetrolBlue90,
    onPrimaryContainer = PetrolBlue20,
    secondary = Amber50,
    onSecondary = Color.White,
    secondaryContainer = Amber90,
    onSecondaryContainer = Amber30,
    tertiary = MossGreen40,
    onTertiary = Color.White,
    tertiaryContainer = MossGreen90,
    onTertiaryContainer = MossGreen30,
    background = Neutral95,
    onBackground = Neutral20,
    surface = Neutral99,
    onSurface = Neutral20,
    surfaceVariant = PetrolBlue95,
    onSurfaceVariant = Neutral30,
    outline = Neutral50,
    outlineVariant = Neutral90,
    error = ErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight
)

private val DarkColors = darkColorScheme(
    primary = PetrolBlue80,
    onPrimary = PetrolBlue20,
    primaryContainer = PetrolBlue30,
    onPrimaryContainer = PetrolBlue90,
    secondary = Amber80,
    onSecondary = Amber20,
    secondaryContainer = Amber30,
    onSecondaryContainer = Amber90,
    tertiary = MossGreen80,
    onTertiary = MossGreen30,
    tertiaryContainer = MossGreen30,
    onTertiaryContainer = MossGreen90,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral20,
    onSurface = Neutral90,
    surfaceVariant = Neutral30,
    onSurfaceVariant = Neutral80,
    outline = Neutral50,
    outlineVariant = Neutral30,
    error = ErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark
)

@Composable
fun KarmaKhodroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = KarmaKhodroTypography,
        shapes = KarmaKhodroShapes,
        content = content
    )
}
