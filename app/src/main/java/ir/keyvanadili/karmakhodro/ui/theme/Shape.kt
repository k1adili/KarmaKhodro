package ir.keyvanadili.karmakhodro.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * یک اسکیل واحد برای گردی گوشه‌ها در کل اپ — به‌جای مقادیر پراکنده و دستی.
 * extraSmall/small: چیپ‌ها و عناصر کوچک
 * medium: کارت‌ها (پیش‌فرض بیشتر سطح‌ها)
 * large: دیالوگ‌ها و کارت‌های برجسته (کارت کیلومتر، کارت خودرو)
 * extraLarge: دکمه‌های اصلی و شیت‌ها
 */
val KarmaKhodroShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)
