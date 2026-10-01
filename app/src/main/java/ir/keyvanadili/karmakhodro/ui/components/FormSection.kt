package ir.keyvanadili.karmakhodro.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.keyvanadili.karmakhodro.ui.theme.KarmaSpacing

/**
 * یک بخش عنوان‌دار برای فرم‌ها — برای یکدست‌سازی ظاهر بخش‌های مختلف فرم‌های
 * خودرو و سرویس به‌جای تکرار جداگانه در هر صفحه.
 */
@Composable
fun FormSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(KarmaSpacing.sm))
        content()
    }
}
