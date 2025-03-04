package com.trevorwiebe.timesheet.punch.domain.usecases

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class CalculateTimeSheets {

    operator fun invoke(
        goLiveDate: Instant,
        currentDate: Instant
    ): List<Instant> {
        val timeZone = TimeZone.currentSystemDefault()
        val goLiveLocalDate = goLiveDate.toLocalDateTime(timeZone).date
        val currentLocalDate = currentDate.toLocalDateTime(timeZone).date

        val timeElapsedSinceStart = goLiveLocalDate.daysUntil(currentLocalDate)
        val daysIntoNewPayPeriod = (timeElapsedSinceStart + 1) % 13
        val payPeriodStartDate = currentLocalDate.minus(daysIntoNewPayPeriod, DateTimeUnit.DAY)

        // Generate list of days in the pay period, up to current date
        return generateSequence(payPeriodStartDate) { it.plus(1, DateTimeUnit.DAY) }
            .takeWhile { it <= currentLocalDate }
            .map { it.atStartOfDayIn(timeZone) }
            .toList()
    }
}