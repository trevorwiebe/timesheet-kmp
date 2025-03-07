package com.trevorwiebe.timesheet.core.domain.dto

data class TimeSheetDto(
    val confirmedByUser: Boolean,
    val holidayHours: Int,
    val vacationHours: Int,
    val payPeriodStart: String,
    val payPeriodEnd: String,
)
