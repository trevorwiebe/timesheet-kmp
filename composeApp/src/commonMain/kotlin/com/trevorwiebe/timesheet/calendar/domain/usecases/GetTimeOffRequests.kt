package com.trevorwiebe.timesheet.calendar.domain.usecases

import com.trevorwiebe.timesheet.calendar.domain.CalendarRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.TimeOffRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTimeOffRequests(
    private val calendarRepository: CalendarRepository,
) {

    @Suppress("UNCHECKED_CAST")
    suspend operator fun invoke(): Flow<TSResult> {
        return calendarRepository.getTimeOffList().map { tsResult ->
            if (tsResult.error == null) {
                val timeOffDtos = tsResult.data as List<TimeOffRequestDto>
                val timeOffModels = timeOffDtos.map { it.toTimeOffRequestModel() }
                tsResult.copy(data = timeOffModels)
            } else {
                tsResult
            }
        }
    }
}