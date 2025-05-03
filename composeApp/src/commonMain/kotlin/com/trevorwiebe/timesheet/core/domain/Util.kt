package com.trevorwiebe.timesheet.core.domain

import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.report.presentation.uiUtils.TimeSheetStatus
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
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

    fun LocalDateTime.plusHours(hours: Int): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        return try {
            this.toInstant(timeZone)
                .plus(hours.hours)
                .toLocalDateTime(timeZone)
        } catch (e: Exception) {
            defaultDate()
        }
    }

    fun convertStringToLocalDateTime(dateTimeString: String): LocalDateTime {
        if (dateTimeString.isEmpty() || dateTimeString == "null") return defaultDate()
        return try {
            Instant.parse(dateTimeString)
                .toLocalDateTime(TimeZone.currentSystemDefault())
        } catch (e: Exception) {
            defaultDate()
        }
    }

    fun convertStringToLocalDate(dateString: String): LocalDate {
        if (dateString.isEmpty() || dateString == "null") return defaultDate().date
        return try {
            LocalDate.parse(dateString)
        } catch (e: Exception) {
            defaultDate().date
        }
    }

    // Extension function to calculate duration between two Instants
    fun Duration.Companion.between(start: LocalDateTime, end: LocalDateTime): Duration {
        return end.toInstant(TimeZone.currentSystemDefault()) - start.toInstant(TimeZone.currentSystemDefault())
    }

    fun getReadableErrorMessage(e: FirebaseException): String {
        return when {
            e is FirebaseAuthInvalidUserException -> "No account exists with this email. Please contact your manager."
            e is FirebaseAuthInvalidCredentialsException -> "Invalid username or password. Please try again."
            e is FirebaseAuthUserCollisionException -> "An account already exists with this email."
            e is FirebaseNetworkException -> "Network error. Please check your connection and try again."
            e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true -> "Invalid email or password. Please check your credentials."
            e.message?.contains("TOO_MANY_ATTEMPTS_TRY_LATER") == true -> "Too many unsuccessful login attempts. Please try again later."
            e.message?.contains("EMAIL_NOT_FOUND") == true -> "No account exists with this email address."
            e.message?.contains("INVALID_PASSWORD") == true -> "The password is invalid for this account."
            e.message?.contains("USER_DISABLED") == true -> "This account has been disabled."
            else -> "Authentication failed: ${e.message}"
        }
    }

    fun getTimeSheetStatus(
        timeSheet: TimeSheet?,
        currentPeriod: Boolean,
    ): List<TimeSheetStatus> {

        if (timeSheet == null) return emptyList()

        val statusList = mutableListOf<TimeSheetStatus>()

        // Check if the time sheet is in the current pay period
        if (currentPeriod) {
            statusList.add(TimeSheetStatus.CURRENT_PERIOD)
        }
        if (timeSheet.confirmedByUser.not()) {
            // Check if the time sheet needs to be confirmed
            statusList.add(TimeSheetStatus.CONFIRM_HOURS_NOW)
        }
        if (timeSheet.submitted.not() && timeSheet.confirmedByUser) {
            statusList.add(TimeSheetStatus.CONFIRMED)
        }

        if (timeSheet.submitted && timeSheet.confirmedByUser) {
            statusList.add(TimeSheetStatus.PERIOD_CLOSED)
        }

        return statusList
    }

    private fun defaultDate(): LocalDateTime {
        // date when android was released, because it's better than apple
        return LocalDateTime(2008, 9, 23, 0, 0, 0)
    }

    fun getRateIDFromName(name: String, rates: List<Rate>): String {
        return rates.filter { it.description == name }.map { it.id }.first()
    }
}