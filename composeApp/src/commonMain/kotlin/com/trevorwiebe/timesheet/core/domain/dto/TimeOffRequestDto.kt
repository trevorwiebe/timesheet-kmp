package com.trevorwiebe.timesheet.core.domain.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class TimeOffRequestDto(
    val employeeId: String?,
    val employeeName: String?,
    val requestOffTime: LocalDate,
    val timeOffRequestApproveTime: LocalDate?,
)
