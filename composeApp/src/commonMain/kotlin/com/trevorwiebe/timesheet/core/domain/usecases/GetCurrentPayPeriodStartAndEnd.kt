package com.trevorwiebe.timesheet.core.domain.usecases

import com.trevorwiebe.timesheet.core.domain.model.Organization
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetCurrentPayPeriodStartAndEnd {
    operator fun invoke(
        organization: Organization
    ): Pair<LocalDateTime, LocalDateTime> {
        val timeZone = TimeZone.UTC

        val currentDate = Clock.System.now().toLocalDateTime(timeZone).date

        val goLiveLocalDate = organization.goLiveDate.toLocalDateTime(timeZone).date

        // Calculate days since go-live date in local time
        val daysSinceGoLive = currentDate.minus(goLiveLocalDate).days
        val daysIntoNewPayPeriod = daysSinceGoLive % 14

        // Calculate start date in local time
        val startLocalDate = currentDate.minus(daysIntoNewPayPeriod, DateTimeUnit.DAY)
        // Convert to LocalDateTime at start of day
        val payPeriodStartDateTime = LocalDateTime(startLocalDate, LocalTime(0, 0, 0))

        // Calculate end date in local time (13 days later)
        val endLocalDate = startLocalDate.plus(13, DateTimeUnit.DAY)
        // End date is at the last moment of the 13th day (just before midnight)
        val payPeriodEndDateTime = LocalDateTime(endLocalDate, LocalTime(23, 59, 59, 999_000_000))

        return Pair(payPeriodStartDateTime, payPeriodEndDateTime)
    }
}