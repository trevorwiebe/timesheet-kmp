package com.trevorwiebe.timesheet.core.domain.dto

import com.trevorwiebe.timesheet.core.domain.model.TimeOffRequestModel
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class TimeOffRequestDto(
    val employeeId: String?,
    val employeeName: String?,
    val requestOffTime: LocalDate,
    val timeOffRequestApproveTime: LocalDate?,
) {
    fun toTimeOffRequestModel(id: String): TimeOffRequestModel {
        return TimeOffRequestModel(
            id = id,
            employeeId = employeeId,
            employeeName = employeeName,
            requestOffTime = requestOffTime,
            timeOffRequestApproveTime = timeOffRequestApproveTime
        )
    }
}
