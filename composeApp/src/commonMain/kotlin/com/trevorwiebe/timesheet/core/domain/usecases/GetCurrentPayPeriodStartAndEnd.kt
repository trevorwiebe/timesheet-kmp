package com.trevorwiebe.timesheet.core.domain.usecases

import com.trevorwiebe.timesheet.core.domain.Util.localDateTime
import com.trevorwiebe.timesheet.core.domain.model.Organization
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class GetCurrentPayPeriodStartAndEnd {
    operator fun invoke(
        organization: Organization
    ): Pair<LocalDate, LocalDate> {

        val currentDate = localDateTime(atTopOfDay = true).date

        val goLiveLocalDate = organization.goLiveDate

        currentDate.minus(goLiveLocalDate)

        // Calculate days since go-live date in local time
        val daysSinceGoLive = currentDate.minus(goLiveLocalDate)
        val daysIntoNewPayPeriod = daysSinceGoLive.days % 14

        // Calculate start date in local time
        val startLocalDate = currentDate.minus(daysIntoNewPayPeriod, DateTimeUnit.DAY)

        // Calculate end date in local time (13 days later)
        val endLocalDate = startLocalDate.plus(13, DateTimeUnit.DAY)

        return Pair(startLocalDate, endLocalDate)
    }
}