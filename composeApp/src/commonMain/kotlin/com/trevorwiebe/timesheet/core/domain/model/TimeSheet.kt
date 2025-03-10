package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.TimeSheetDto
import kotlinx.datetime.LocalDateTime

data class TimeSheet(
    val id: String,
    val confirmedByUser: Boolean,
    val submitted: Boolean,
    val holidayHours: Int,
    val vacationHours: Int,
    val payPeriodStart: LocalDateTime,
    val payPeriodEnd: LocalDateTime,
)

fun TimeSheetDto.toTimeSheet(id: String) = TimeSheet(
    id = id,
    confirmedByUser = confirmedByUser,
    submitted = submitted,
    holidayHours = holidayHours,
    vacationHours = vacationHours,
    payPeriodStart = LocalDateTime.parse(payPeriodStart),
    payPeriodEnd = LocalDateTime.parse(payPeriodEnd),
)