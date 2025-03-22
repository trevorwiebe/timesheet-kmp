package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.Util.localDateTime
import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class CalculateTimeSheets(
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd
) {

    operator fun invoke(
        organization: Organization,
    ): List<LocalDate> {

        val currentDate = localDateTime().date
        val payPeriodStartDate = getCurrentPayPeriodStartAndEnd(organization).first

        return generateSequence(payPeriodStartDate) { it.plus(1, DateTimeUnit.DAY) }
            .takeWhile { it <= currentDate }
            .toList()
            .sortedDescending()
    }
}