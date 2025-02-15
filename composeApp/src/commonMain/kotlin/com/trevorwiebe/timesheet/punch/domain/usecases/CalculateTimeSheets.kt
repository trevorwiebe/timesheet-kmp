package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.model.Organization
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class CalculateTimeSheets {

    operator fun invoke(
        startDate: Instant,
        organization: Organization,
        currentDate: Instant
    ): List<Instant> {
        val timeZone = TimeZone.currentSystemDefault()
        val startLocalDate = startDate.toLocalDateTime(timeZone).date
        val currentLocalDate = currentDate.toLocalDateTime(timeZone).date

        // Determine end date based on duration and unit
        val periodEndDate = when (organization.payPeriodUnit.lowercase()) {
            "day" -> startLocalDate.plus(organization.payPeriodDuration, DateTimeUnit.DAY)
            "week" -> startLocalDate.plus(organization.payPeriodDuration, DateTimeUnit.WEEK)
            "month" -> startLocalDate.plus(organization.payPeriodDuration, DateTimeUnit.MONTH)
            else -> throw IllegalArgumentException("Unsupported pay period unit: ${organization.payPeriodUnit}")
        }

        // Generate list of days in the pay period, up to current date
        return generateSequence(startLocalDate) { it.plus(1, DateTimeUnit.DAY) }
            .takeWhile { it < periodEndDate && it <= currentLocalDate }
            .map { it.atStartOfDayIn(timeZone) }
            .toList()
    }
}