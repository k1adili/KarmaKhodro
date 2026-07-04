package ir.keyvanadili.karmakhodro.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ir.keyvanadili.karmakhodro.R

/**
 * فونت اصلی برنامه: Vazirmatn
 *
 * توجه مهم:
 * فایل‌های فونت به دلیل حجم و مجوز، در این پروژه قرار داده نشده‌اند.
 * قبل از بیلد گرفتن، فایل‌های زیر را از
 * https://github.com/rastikerdar/vazirmatn (پوشه fonts/ttf) دانلود کرده
 * و دقیقاً با همین نام‌ها داخل پوشه app/src/main/res/font/ قرار دهید:
 *
 *   vazirmatn_regular.ttf
 *   vazirmatn_medium.ttf
 *   vazirmatn_bold.ttf
 *
 * اگر این فایل‌ها موجود نباشند، بیلد اندروید (Gradle) با خطای
 * "unresolved reference: vazirmatn_regular" متوقف می‌شود، چون
 * این پروژه تعمداً برای استفاده قطعی از این فونت تنظیم شده است.
 */
val VazirmatnFontFamily: FontFamily = FontFamily(
    Font(R.font.vazirmatn_regular, FontWeight.Normal),
    Font(R.font.vazirmatn_medium, FontWeight.Medium),
    Font(R.font.vazirmatn_bold, FontWeight.Bold)
)

val KarmaKhodroTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = VazirmatnFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = VazirmatnFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = VazirmatnFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = VazirmatnFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle(
        fontFamily = VazirmatnFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp
    )
)
