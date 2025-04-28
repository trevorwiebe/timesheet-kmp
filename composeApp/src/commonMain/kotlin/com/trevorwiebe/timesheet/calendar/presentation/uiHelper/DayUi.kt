package com.trevorwiebe.timesheet.calendar.presentation.uiHelper

import com.trevorwiebe.timesheet.core.domain.model.EmployeeModel
import kotlinx.datetime.LocalDate

data class DayUi(
    val date: LocalDate,
    val selectedForTimeOff: Boolean,
    var employeesOff: List<EmployeeModel> = emptyList(),
) {
    val toggleTimeOff: DayUi
        get() = copy(selectedForTimeOff = !selectedForTimeOff)
}