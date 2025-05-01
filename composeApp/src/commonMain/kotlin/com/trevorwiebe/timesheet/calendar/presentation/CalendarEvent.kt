package com.trevorwiebe.timesheet.calendar.presentation

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType
import com.trevorwiebe.timesheet.core.domain.model.TimeOffRequestModel
import kotlinx.datetime.LocalDate

sealed class CalendarEvent {
    data class OnSetCalendarType(val type: CalendarType) : CalendarEvent()
    data class OnSetAddTimeOffMode(val mode: Boolean, val saving: Boolean) : CalendarEvent()
    data class OnSetSelectedTimeOff(val date: LocalDate) : CalendarEvent()
    data class OnTimeOffSelected(val timeOffRequest: TimeOffRequestModel?) : CalendarEvent()
    data class OnDeleteTimeOffRequest(val timeOffRequestModel: TimeOffRequestModel?) :
        CalendarEvent()
}