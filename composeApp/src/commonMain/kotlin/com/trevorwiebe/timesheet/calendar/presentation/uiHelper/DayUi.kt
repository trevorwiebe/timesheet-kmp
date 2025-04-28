package com.trevorwiebe.timesheet.calendar.presentation.uiHelper

import com.trevorwiebe.timesheet.core.domain.model.TimeOffRequestModel
import kotlinx.datetime.LocalDate

data class DayUi(
    val date: LocalDate,
    val selectedForTimeOff: Boolean,
    var employeesOff: List<TimeOffRequestModel> = emptyList(),
) {
    val toggleTimeOff: DayUi
        get() = copy(selectedForTimeOff = !selectedForTimeOff)
}