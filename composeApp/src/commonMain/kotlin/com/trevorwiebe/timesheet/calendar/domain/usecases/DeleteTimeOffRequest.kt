package com.trevorwiebe.timesheet.calendar.domain.usecases

import com.trevorwiebe.timesheet.calendar.domain.CalendarRepository
import com.trevorwiebe.timesheet.core.domain.TSResult

class DeleteTimeOffRequest(
    private val calendarRepository: CalendarRepository,
) {

    suspend operator fun invoke(id: String): TSResult {
        return calendarRepository.deleteTimeOffRequest(id)
    }

}