package com.trevorwiebe.timesheet.core.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object Util {
    fun instantToFriendlyTime(instant: Instant?): String {

        val dateTime = instant?.toLocalDateTime(TimeZone.UTC)
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

    fun instantToFriendlyDate(instant: Instant?): String {
        val dateTime = instant?.toLocalDateTime(TimeZone.UTC)
        val year = dateTime?.year
        val month = dateTime?.monthNumber
        val day = dateTime?.dayOfMonth
        return "$month-$day-$year"
    }

    fun instantToFriendlyDayOfWeek(instant: Instant?): String {
        val dateTime = instant?.toLocalDateTime(TimeZone.UTC)
        val dayOfWeek = dateTime?.dayOfWeek
        return "$dayOfWeek"
    }

    fun parseTimeToInstant(
        timeString: String,
        contextDate: Instant
    ): Instant {

        // Validate input first
        require(isValidTimeFormat(timeString)) { "Invalid time format" }

        // Split time components
        val (timeWithoutPeriod, period) = timeString.split(" ")
        val (hours, minutes) = timeWithoutPeriod.split(":").map { it.toInt() }

        // Adjust hours for 12-hour clock
        val adjustedHours = when {
            period.equals("PM", ignoreCase = true) && hours != 12 -> hours + 12
            period.equals("AM", ignoreCase = true) && hours == 12 -> 0
            else -> hours
        }


        println(adjustedHours)

        // Create LocalDateTime for the current date with specified time
        val now = contextDate.toLocalDateTime(TimeZone.UTC)
        val localDateTime = LocalDateTime(
            year = now.year,
            monthNumber = now.monthNumber,
            dayOfMonth = now.dayOfMonth,
            hour = adjustedHours,
            minute = minutes
        )

        // Convert to Instant in the specified time zone
        return localDateTime.toInstant(TimeZone.UTC)
    }

    fun isValidTimeFormat(timeString: String): Boolean {
        // Updated regular expression to enforce space between time and AM/PM
        val timeFormatRegex =
            """^(1[0-2]|0?[1-9]):([0-5][0-9])\s+(AM|PM)$""".toRegex(RegexOption.IGNORE_CASE)

        // Check if the string matches the expected pattern
        return timeFormatRegex.matches(timeString)
    }

    fun roundToTwoDecimals(value: Double): Double {
        return kotlin.math.round(value * 100) / 100
    }
}