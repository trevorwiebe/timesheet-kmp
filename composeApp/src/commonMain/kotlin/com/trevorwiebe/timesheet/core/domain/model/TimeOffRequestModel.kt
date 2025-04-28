package com.trevorwiebe.timesheet.core.domain.model

import com.trevorwiebe.timesheet.core.domain.dto.TimeOffRequestDto
import kotlinx.datetime.LocalDate

data class TimeOffRequestModel(
    val employeeId: String?,
    val employeeName: String?,
    val requestOffTime: LocalDate,
    val timeOffRequestApproveTime: LocalDate?,
) {
    fun toTimeOffRequestDto(): TimeOffRequestDto {
        return TimeOffRequestDto(
            employeeId = employeeId,
            employeeName = employeeName,
            requestOffTime = requestOffTime,
            timeOffRequestApproveTime = timeOffRequestApproveTime
        )
    }
}