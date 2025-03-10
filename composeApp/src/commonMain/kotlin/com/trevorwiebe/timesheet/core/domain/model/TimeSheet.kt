package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.TimeSheetDto
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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
    payPeriodStart = Instant.parse(payPeriodStart).toLocalDateTime(TimeZone.currentSystemDefault()),
    payPeriodEnd = Instant.parse(payPeriodEnd).toLocalDateTime(TimeZone.currentSystemDefault()),
)