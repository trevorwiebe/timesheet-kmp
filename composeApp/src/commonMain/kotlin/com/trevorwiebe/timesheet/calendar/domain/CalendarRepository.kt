package com.trevorwiebe.timesheet.calendar.domain

import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.TimeOffRequestDto
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    suspend fun postTimeOffList(timeOffList: List<TimeOffRequestDto>): TSResult

    suspend fun getTimeOffList(): Flow<TSResult>

}