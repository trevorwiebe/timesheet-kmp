package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class CalculateTimeSheets(
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd
) {

    operator fun invoke(
        organization: Organization,
    ): List<Instant> {

        val currentDate = Clock.System.now()
        val timeZone = TimeZone.UTC

        val payPeriodStartDate = getCurrentPayPeriodStartAndEnd(organization).first

        val truncatedCurrentDate =
            currentDate.toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)

        return generateSequence(payPeriodStartDate) { it.plus(DateTimePeriod(days = 1), timeZone) }
            .takeWhile { it <= truncatedCurrentDate }
            .toList()
    }
}