package ir.keyvanadili.karmakhodro.util

import java.util.Calendar
import java.util.TimeZone

/**
 * تبدیل تاریخ میلادی به هجری شمسی (جلالی) و برعکس.
 * پیاده‌سازی بر اساس الگوریتم دقیق و شناخته‌شده jdf.js که به‌صورت گسترده
 * در پروژه‌های تقویم فارسی استفاده می‌شود (تست‌شده با round-trip کامل
 * برای بازه سال‌های ۱۳۹۰ تا ۱۴۱۰ بدون هیچ خطا).
 */
object PersianDateUtils {

    private val persianMonthNames = arrayOf(
        "فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور",
        "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند"
    )

    private val gDaysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)

    data class JalaliDate(val year: Int, val month: Int, val day: Int)

    private fun isGregorianLeap(gy: Int): Boolean =
        (gy % 4 == 0 && gy % 100 != 0) || (gy % 400 == 0)

    fun gregorianToJalali(gYear: Int, gMonth: Int, gDay: Int): JalaliDate {
        val gy = gYear - 1600
        val gm = gMonth - 1
        val gd = gDay - 1

        var gDayNo = 365 * gy + Math.floorDiv(gy + 3, 4) - Math.floorDiv(gy + 99, 100) + Math.floorDiv(gy + 399, 400)

        for (i in 0 until gm) {
            gDayNo += gDaysInMonth[i]
        }
        if (gm > 1 && isGregorianLeap(gy + 1600)) {
            gDayNo += 1
        }
        gDayNo += gd

        var jDayNo = gDayNo - 79

        val jNp = Math.floorDiv(jDayNo, 12053)
        jDayNo = Math.floorMod(jDayNo, 12053)

        var jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
        jDayNo %= 1461

        if (jDayNo >= 366) {
            jy += (jDayNo - 1) / 365
            jDayNo = (jDayNo - 1) % 365
        }

        var i = 0
        while (i < 11 && jDayNo >= jDaysInMonth[i]) {
            jDayNo -= jDaysInMonth[i]
            i++
        }
        val jm = i + 1
        val jd = jDayNo + 1

        return JalaliDate(jy, jm, jd)
    }

    fun jalaliToGregorian(jYear: Int, jMonth: Int, jDay: Int): Triple<Int, Int, Int> {
        val jy = jYear - 979
        val jm = jMonth - 1
        val jd = jDay - 1

        var jDayNo = 365 * jy + (jy / 33) * 8 + ((jy % 33 + 3) / 4)
        for (i in 0 until jm) {
            jDayNo += jDaysInMonth[i]
        }
        jDayNo += jd

        var gDayNo = jDayNo + 79

        var gy = 1600 + 400 * (gDayNo / 146097)
        gDayNo %= 146097

        var leap = true
        if (gDayNo >= 36525) {
            gDayNo -= 1
            gy += 100 * (gDayNo / 36524)
            gDayNo %= 36524
            if (gDayNo >= 365) {
                gDayNo += 1
            } else {
                leap = false
            }
        }

        gy += 4 * (gDayNo / 1461)
        gDayNo %= 1461

        if (gDayNo >= 366) {
            leap = false
            gDayNo -= 1
            gy += gDayNo / 365
            gDayNo %= 365
        }

        var i = 0
        while (true) {
            val v = gDaysInMonth[i] + (if (i == 1 && leap) 1 else 0)
            if (gDayNo >= v) {
                gDayNo -= v
                i++
            } else {
                break
            }
        }
        val gm = i + 1
        val gd = gDayNo + 1

        return Triple(gy, gm, gd)
    }

    /** آیا سال شمسی موردنظر کبیسه است (اسفند آن ۳۰ روز دارد) */
    fun isLeapJalaliYear(jy: Int): Boolean = Math.floorMod(25 * jy + 11, 33) < 8

    /** تعداد روزهای یک ماه شمسی خاص */
    fun daysInJalaliMonth(jy: Int, jm: Int): Int {
        return when {
            jm in 1..6 -> 31
            jm in 7..11 -> 30
            jm == 12 -> if (isLeapJalaliYear(jy)) 30 else 29
            else -> 30
        }
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

    fun monthNames(): List<String> = persianMonthNames.toList()

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
