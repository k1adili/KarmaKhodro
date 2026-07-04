package ir.keyvanadili.karmakhodro.util

import java.util.Calendar
import java.util.TimeZone

/**
 * تبدیل تاریخ میلادی به هجری شمسی (جلالی) و برعکس.
 * الگوریتم مبتنی بر روش استاندارد و شناخته‌شده تبدیل تقویم (بدون نیاز به کتابخانه خارجی).
 */
object PersianDateUtils {

    private val persianMonthNames = arrayOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    private val gDaysInMonth = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)

    data class JalaliDate(val year: Int, val month: Int, val day: Int)

    fun gregorianToJalali(gy: Int, gm: Int, gd: Int): JalaliDate {
        var jy: Int
        var gy2 = gy
        if (gy2 > 1600) {
            jy = 979
            gy2 -= 1600
        } else {
            jy = 0
            gy2 -= 621
        }
        val gy3 = if (gm > 2) gy2 + 1 else gy2
        var days = (365 * gy2) +
                ((gy3 + 3) / 4) -
                ((gy3 + 99) / 100) +
                ((gy3 + 399) / 400) -
                80 + gd + gDaysInMonth[gm - 1]

        jy += 33 * (days / 12053)
        days %= 12053

        jy += 4 * (days / 1461)
        days %= 1461

        if (days > 365) {
            jy += (days - 1) / 365
            days = (days - 1) % 365
        }

        val jm: Int
        val jd: Int
        if (days < 186) {
            jm = 1 + days / 31
            jd = 1 + (days % 31)
        } else {
            jm = 7 + (days - 186) / 30
            jd = 1 + ((days - 186) % 30)
        }
        return JalaliDate(jy, jm, jd)
    }

    fun jalaliToGregorian(jy: Int, jm: Int, jd: Int): Triple<Int, Int, Int> {
        var gy: Int
        var jy2 = jy
        if (jy2 > 979) {
            gy = 1600
            jy2 -= 979
        } else {
            gy = 621
        }

        var days = (365 * jy2) + ((jy2 / 33) * 8) + (((jy2 % 33) + 3) / 4) + 78 + jd +
                if (jm < 7) (jm - 1) * 31 else ((jm - 7) * 30) + 186

        gy += 400 * (days / 146097)
        days %= 146097

        if (days > 36524) {
            gy += 100 * ((days - 1) / 36524)
            days = (days - 1) % 36524
            if (days >= 365) days += 1
        }

        gy += 4 * (days / 1461)
        days %= 1461

        if (days > 365) {
            gy += (days - 1) / 365
            days = (days - 1) % 365
        }

        var gd = days + 1
        val salA = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 366)
        var gm = 0
        while (gm < 13 && gd > salA[gm]) {
            gm++
        }
        gd -= salA[gm - 1]
        return Triple(gy, gm, gd)
    }

    fun millisToJalali(millis: Long): JalaliDate {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.timeInMillis = millis
        return gregorianToJalali(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    fun jalaliToMillis(jy: Int, jm: Int, jd: Int): Long {
        val (gy, gm, gd) = jalaliToGregorian(jy, jm, jd)
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.clear()
        cal.set(gy, gm - 1, gd, 12, 0, 0)
        return cal.timeInMillis
    }

    fun monthName(month: Int): String = persianMonthNames.getOrElse(month - 1) { "" }

    fun formatMillis(millis: Long, withMonthName: Boolean = true): String {
        val j = millisToJalali(millis)
        return if (withMonthName) {
            "${toPersianDigits(j.day)} ${monthName(j.month)} ${toPersianDigits(j.year)}"
        } else {
            "${toPersianDigits(j.year)}/${toPersianDigits(pad2(j.month))}/${toPersianDigits(pad2(j.day))}"
        }
    }

    private fun pad2(n: Int): String = if (n < 10) "0$n" else n.toString()

    fun toPersianDigits(input: Any): String {
        val text = input.toString()
        val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
        val sb = StringBuilder()
        for (c in text) {
            if (c in '0'..'9') {
                sb.append(persianDigits[c - '0'])
            } else {
                sb.append(c)
            }
        }
        return sb.toString()
    }

    fun currentJalali(): JalaliDate = millisToJalali(System.currentTimeMillis())
}
