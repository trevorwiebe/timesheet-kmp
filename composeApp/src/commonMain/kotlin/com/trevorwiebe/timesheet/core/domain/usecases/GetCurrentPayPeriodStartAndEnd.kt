package com.trevorwiebe.timesheet.core.domain.usecases

import com.trevorwiebe.timesheet.core.domain.model.Organization
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class GetCurrentPayPeriodStartAndEnd {
    operator fun invoke(
        organization: Organization
    ): Pair<Instant, Instant> {
        val timeZone = TimeZone.UTC

        val currentDate = Clock.System.now().toLocalDateTime(timeZone).date

        val goLiveLocalDate = organization.goLiveDate.toLocalDateTime(timeZone).date

        // Calculate days since go-live date in local time
        val daysSinceGoLive = currentDate.minus(goLiveLocalDate).days
        val daysIntoNewPayPeriod = daysSinceGoLive % 14

        // Calculate start date in local time
        val startLocalDate = currentDate.minus(daysIntoNewPayPeriod, DateTimeUnit.DAY)
        val payPeriodStartDate = startLocalDate.atStartOfDayIn(timeZone)

        // Calculate end date in local time (13 days later)
        val endLocalDate = startLocalDate.plus(13, DateTimeUnit.DAY)
        // End date is at the last moment of the 13th day (just before midnight)
        val endLocalDateTime = endLocalDate
            .atTime(23, 59, 59, 999_000_000)
            .toInstant(timeZone)

        return Pair(payPeriodStartDate, endLocalDateTime)
    }
}