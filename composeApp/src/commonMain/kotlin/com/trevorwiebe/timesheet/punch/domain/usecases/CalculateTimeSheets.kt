package com.trevorwiebe.timesheet.punch.domain.usecases

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class CalculateTimeSheets {

    operator fun invoke(
        goLiveDate: Instant,
        currentDate: Instant,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): List<Instant> {

        val truncatedCurrentDate =
            currentDate.toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)

        val elapsedTime = truncatedCurrentDate - goLiveDate
        val daysIntoNewPayPeriod: Int = (elapsedTime.inWholeDays % 14).toInt()
        val payPeriodStartDate = truncatedCurrentDate.toLocalDateTime(timeZone).date.minus(
            daysIntoNewPayPeriod,
            DateTimeUnit.DAY
        ).atStartOfDayIn(timeZone)

        return generateSequence(payPeriodStartDate) { it.plus(DateTimePeriod(days = 1), timeZone) }
            .takeWhile { it <= truncatedCurrentDate }
            .toList()
    }
}