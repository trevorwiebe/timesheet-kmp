package com.trevorwiebe.timesheet.calendar.presentation

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType

sealed class CalendarEvent {
    data class OnSetCalendarType(val type: CalendarType) : CalendarEvent()
}