package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.Util.localDateTime
import com.trevorwiebe.timesheet.core.domain.Util.plusDays
import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import kotlinx.datetime.LocalDateTime

class CalculateTimeSheets(
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd
) {

    operator fun invoke(
        organization: Organization,
    ): List<LocalDateTime> {

        val currentDate = localDateTime()
        val payPeriodStartDate = getCurrentPayPeriodStartAndEnd(organization).first

        return generateSequence(payPeriodStartDate) { it.plusDays(1) }
            .takeWhile { it <= currentDate }
            .toList()
    }
}