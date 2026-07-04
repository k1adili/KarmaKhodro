package ir.keyvanadili.karmakhodro.util

/**
 * جداکننده سه‌رقمی برای اعداد (کیلومتر، هزینه و ...) جهت خوانایی بهتر.
 * مثال: 1234567 -> 1,234,567
 */
object NumberFormatUtils {

    fun formatThousands(number: Long): String {
        val isNegative = number < 0
        val s = kotlin.math.abs(number).toString()
        val sb = StringBuilder()
        for ((index, c) in s.reversed().withIndex()) {
            if (index != 0 && index % 3 == 0) sb.append(',')
            sb.append(c)
        }
        val result = sb.reverse().toString()
        return if (isNegative) "-$result" else result
    }

    fun formatThousands(number: Int): String = formatThousands(number.toLong())
}
