package com.trevorwiebe.timesheet.punch.domain.usecases

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class CalculateTimeSheets {

    operator fun invoke(
        goLiveDate: Instant,
        currentDate: Instant
    ): List<Instant> {

        val elapsedTime = currentDate.minus(goLiveDate)
        val daysIntoNewPayPeriod: Int = (elapsedTime.inWholeDays % 14).toInt() * 24
        val payPeriodStartDate = currentDate.minus(daysIntoNewPayPeriod, DateTimeUnit.HOUR)

        return generateSequence(payPeriodStartDate) { it.plus(24, DateTimeUnit.HOUR) }
            .takeWhile { it <= currentDate }
            .toList()
    }
}