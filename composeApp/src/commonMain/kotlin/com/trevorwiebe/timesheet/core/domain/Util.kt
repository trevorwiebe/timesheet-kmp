package com.trevorwiebe.timesheet.core.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

object Util {

    fun parseStringToInstant(timestampString: String?): Instant? = when {
        timestampString.isNullOrEmpty() -> {
            null
        }
        // Strategy 1: Explicit format parsing
        timestampString.contains('T') -> {
            try {
                LocalDateTime.parse(timestampString)
                    .toInstant(TimeZone.UTC)
            } catch (e: Exception) {
                // Fallback parsing with explicit timezone handling
                Instant.parse("$timestampString${if (!timestampString.endsWith('Z')) "Z" else ""}")
            }
        }

        // Strategy 2: Numeric timestamp parsing
        timestampString.all { it.isDigit() } -> {
            Instant.fromEpochMilliseconds(timestampString.toLong())
        }

        // Catch-all error handling
        else -> throw IllegalArgumentException("Unsupported timestamp format: $timestampString")
    }

    fun instantToFriendlyTime(instant: Instant?): String {
        val customFormat = DateTimeComponents.Format {
            amPmHour()
            char(':')
            minute()
            char(' ')
            amPmMarker(am = "AM", pm = "PM")
        }
        return instant?.format(customFormat) ?: ""
    }
}