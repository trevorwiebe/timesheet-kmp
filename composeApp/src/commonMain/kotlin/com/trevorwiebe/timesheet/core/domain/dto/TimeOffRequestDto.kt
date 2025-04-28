package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.datetime.LocalDate

data class TimeOffRequestDto(
    val employeeId: Int,
    val employeeName: String,
    val requestOffTime: LocalDate,
    val timeOffRequestApproveTime: LocalDate?,
)
