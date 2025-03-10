package com.trevorwiebe.timesheet.core.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.hours

object Util {
    fun toFriendlyTime(dateTime: LocalDateTime?): String {

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

    fun toFriendlyDate(dateTime: LocalDate?): String {
        val year = dateTime?.year
        val month = dateTime?.monthNumber
        val day = dateTime?.dayOfMonth
        return "$month-$day-$year"
    }

    fun toFriendlyDayOfWeek(dateTime: LocalDate?): String {
        val dayOfWeek = dateTime?.dayOfWeek
        return "$dayOfWeek"
    }

    fun parseTimeToLocalDate(
        timeString: String,
        contextDate: LocalDateTime
    ): LocalDateTime {

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

        // Create LocalDateTime for the current date with specified time
        return LocalDateTime(
            year = contextDate.year,
            monthNumber = contextDate.monthNumber,
            dayOfMonth = contextDate.dayOfMonth,
            hour = adjustedHours,
            minute = minutes
        )
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

    fun localDateTime(atTopOfDay: Boolean = false): LocalDateTime {
        val dateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val topHour = LocalDateTime(
            year = dateTime.year,
            monthNumber = dateTime.monthNumber,
            dayOfMonth = dateTime.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0
        )
        return if (atTopOfDay) topHour else dateTime
    }

    fun LocalDateTime.minusSeconds(seconds: Int): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return this.toInstant(timeZone)
            .minus(seconds, DateTimeUnit.SECOND, timeZone)
            .toLocalDateTime(timeZone)
    }

    fun LocalDateTime.minusDays(days: Int): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return this.toInstant(timeZone)
            .minus(days, DateTimeUnit.DAY, timeZone)
            .toLocalDateTime(timeZone)
    }

    fun LocalDateTime.plusDays(days: Int): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return this.toInstant(timeZone)
            .plus(days, DateTimeUnit.DAY, timeZone)
            .toLocalDateTime(timeZone)
    }

    fun LocalDateTime.plusHours(hours: Int): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return this.toInstant(timeZone)
            .plus(hours.hours)
            .toLocalDateTime(timeZone)
    }

    fun convertStringToLocalDateTime(dateTimeString: String): LocalDateTime {
        return Instant.parse(dateTimeString)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun convertStringToLocalDate(dateTimeString: String): LocalDate {
        return Instant.parse(dateTimeString)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }
}