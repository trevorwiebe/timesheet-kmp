package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.datetime.LocalDateTime

data class EmployeeOffDto(
    val employeeId: Int,
    val employeeName: String,
    val requestOffTime: LocalDateTime,
    val timeOffRequestApproveTime: LocalDateTime?,
)
