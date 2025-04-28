package com.trevorwiebe.timesheet.calendar.presentation

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType
import kotlinx.datetime.LocalDate

sealed class CalendarEvent {
    data class OnSetCalendarType(val type: CalendarType) : CalendarEvent()
    data class OnSetAddTimeOffMode(val mode: Boolean) : CalendarEvent()
    data object OnAddTimeOff : CalendarEvent()
    data class OnSetSelectedTimeOff(val date: LocalDate) : CalendarEvent()
}