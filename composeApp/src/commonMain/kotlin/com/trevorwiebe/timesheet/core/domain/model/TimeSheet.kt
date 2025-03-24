package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.Util.convertStringToLocalDate
import com.trevorwiebe.timesheet.core.domain.dto.TimeSheetDto
import kotlinx.datetime.LocalDate

data class TimeSheet(
    val id: String,
    val confirmedByUser: Boolean,
    val submitted: Boolean,
    val holidayHours: Int,
    val vacationHours: Int,
    val payPeriodStart: LocalDate,
    val payPeriodEnd: LocalDate,
)

fun TimeSheetDto.toTimeSheet(id: String) = TimeSheet(
    id = id,
    confirmedByUser = confirmedByUser,
    submitted = submitted,
    holidayHours = holidayHours,
    vacationHours = vacationHours,
    payPeriodStart = convertStringToLocalDate(payPeriodStart),
    payPeriodEnd = convertStringToLocalDate(payPeriodEnd),
)

fun TimeSheet.toTimeSheetDto() = TimeSheetDto(
    confirmedByUser = confirmedByUser,
    submitted = submitted,
    holidayHours = holidayHours,
    vacationHours = vacationHours,
    payPeriodStart = payPeriodStart.toString(),
    payPeriodEnd = payPeriodEnd.toString()
)