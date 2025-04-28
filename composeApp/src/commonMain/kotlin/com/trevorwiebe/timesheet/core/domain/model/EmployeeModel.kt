package com.trevorwiebe.timesheet.core.domain.model

import kotlinx.datetime.LocalDateTime

data class EmployeeModel(
    val employeeId: Int,
    val employeeName: String,
    val requestOffTime: LocalDateTime,
    val timeOffRequestApproveTime: LocalDateTime?,
)