package com.buffup.app.utils

import android.text.format.DateFormat
import org.apache.commons.lang3.StringUtils
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDateDescription(millis: Long, locale: Locale = Locale.getDefault()): String {
        val date = Date(millis)
        val stringBuilder = StringBuilder()
        val time = DateFormat.format(TIME_FORMAT, date).toString()
        val dayOfWeek = getDayOfWeek(millis, locale)
        stringBuilder.append(StringUtils.capitalize(dayOfWeek))
        stringBuilder.append(", ")
        stringBuilder.append(time)
        return stringBuilder.toString()
    }

    fun getDayOfWeek(millis: Long, locale: Locale = Locale.getDefault()): String {
        val dayOfWeekFormatter = SimpleDateFormat(DAY_OF_WEEK_FORMAT, locale)
        return dayOfWeekFormatter.format(Date(millis))
    }

    private const val DAY_OF_WEEK_FORMAT = "EEEE"
    private const val TIME_FORMAT = "hh:mm aaa"
}