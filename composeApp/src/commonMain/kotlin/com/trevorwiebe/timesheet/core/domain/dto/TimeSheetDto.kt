package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class TimeSheetDto(
    val confirmedByUser: Boolean,
    val submitted: Boolean,
    val holidayHours: Int,
    val vacationHours: Int,
    val payPeriodStart: String,
    val payPeriodEnd: String,
)
