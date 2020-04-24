package com.vastalisy.syncit.util

import java.text.SimpleDateFormat
import java.util.*

object CalendarUtil {
    private val calendar = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("d-MMMM-YYYY", Locale.US)
    private val currentDate = applyStyle(simpleDateFormat.format(calendar.time))

    fun getCurrentDate(): String {
        return currentDate
    }

    private fun applyStyle(date: String): String {
        return date.replace('-', '.')
    }


}