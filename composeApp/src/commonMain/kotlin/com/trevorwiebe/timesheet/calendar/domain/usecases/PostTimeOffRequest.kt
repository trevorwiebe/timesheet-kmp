package com.trevorwiebe.timesheet.calendar.domain.usecases

import com.trevorwiebe.timesheet.calendar.domain.CalendarRepository
import com.trevorwiebe.timesheet.core.domain.model.TimeOffRequestModel

class PostTimeOffRequest(
    private val calendarRepository: CalendarRepository,
) {

    suspend operator fun invoke(timeOffList: List<TimeOffRequestModel>) {

        val timeOffListDto = timeOffList.map {
            it.toTimeOffRequestDto()
        }

        calendarRepository.postTimeOffList(timeOffListDto)

    }
}