package com.trevorwiebe.timesheet.calendar.presentation

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi

data class CalendarState(
    val calendarStructure: List<DayUi> = emptyList(),
    val calendarType: CalendarType = CalendarType.GRID,
    val addingTimeOff: Boolean = false,
    val timeOffMode: Boolean = false,
)
