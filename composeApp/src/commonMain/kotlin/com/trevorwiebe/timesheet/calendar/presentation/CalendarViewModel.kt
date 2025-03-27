package com.trevorwiebe.timesheet.calendar.presentation

import androidx.lifecycle.ViewModel
import com.trevorwiebe.timesheet.calendar.domain.usecases.GetCalendarStructure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalendarViewModel : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    init {
        buildCalendarStructure()
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnSetCalendarType -> {
                _state.update { it.copy(calendarType = event.type) }
            }
        }
    }

    private fun buildCalendarStructure() {
        val getCalendarStructure = GetCalendarStructure()

        val calendarStructure = getCalendarStructure()
        _state.update {
            it.copy(calendarStructure = calendarStructure)
        }
    }

}