package com.trevorwiebe.timesheet.calendar.presentation.uiHelper

import kotlinx.datetime.LocalDate

data class DayUi(
    val date: LocalDate,
    var employeesOff: List<String> = emptyList(),
)