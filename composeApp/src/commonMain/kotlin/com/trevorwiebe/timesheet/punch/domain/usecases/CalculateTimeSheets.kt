package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus

class CalculateTimeSheets(
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd
) {

    operator fun invoke(
        organization: Organization,
    ): List<Instant> {

        val currentDate = Clock.System.now()
        val payPeriodStartDate = getCurrentPayPeriodStartAndEnd(organization).first

        return generateSequence(payPeriodStartDate) { it.plus(1, DateTimeUnit.DAY, TimeZone.UTC) }
            .takeWhile { it <= currentDate }
            .toList()
    }
}