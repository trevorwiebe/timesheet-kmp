package com.trevorwiebe.timesheet.core.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object Util {
    fun instantToFriendlyTime(instant: Instant?): String {

        val dateTime = instant?.toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = dateTime?.hour ?: return ""
        val minute = dateTime.minute

        val formattedHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            hour == 12 -> 12
            else -> hour
        }

        val meridiem = if (hour >= 12) "PM" else "AM"

        return "${formattedHour}:${minute.toString().padStart(2, '0')} $meridiem"
    }
}