package com.trevorwiebe.timesheet.calendar.presentation

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.CalendarType
import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import dev.gitlive.firebase.auth.FirebaseUser

data class CalendarState(
    val calendarStructure: List<DayUi> = emptyList(),
    val calendarType: CalendarType = CalendarType.GRID,
    val addingTimeOff: Boolean = false,
    val timeOffMode: Boolean = false,
    val user: FirebaseUser? = null,
)
