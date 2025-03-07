package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.TimeSheetDto
import kotlinx.datetime.Instant

data class TimeSheet(
    val confirmedByUser: Boolean,
    val holidayHours: Int,
    val vacationHours: Int,
    val payPeriodStart: Instant,
    val payPeriodEnd: Instant,
)

fun TimeSheetDto.toTimeSheet() = TimeSheet(
    confirmedByUser = confirmedByUser,
    holidayHours = holidayHours,
    vacationHours = vacationHours,
    payPeriodStart = Instant.parse(payPeriodStart),
    payPeriodEnd = Instant.parse(payPeriodEnd),
)