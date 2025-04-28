package com.trevorwiebe.timesheet.core.domain.model

import kotlinx.datetime.LocalDate

data class TimeOffRequestModel(
    val employeeId: String?,
    val employeeName: String?,
    val requestOffTime: LocalDate,
    val timeOffRequestApproveTime: LocalDate?,
)