package com.trevorwiebe.timesheet.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.calendar.domain.usecases.DeleteTimeOffRequest
import com.trevorwiebe.timesheet.calendar.domain.usecases.GetCalendarStructure
import com.trevorwiebe.timesheet.calendar.domain.usecases.GetTimeOffRequests
import com.trevorwiebe.timesheet.calendar.domain.usecases.PostTimeOffRequest
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.model.TimeOffRequestModel
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class CalendarViewModel(
    val postTimeOffRequest: PostTimeOffRequest,
    val getTimeOff: GetTimeOffRequests,
    val deleteTimeOffRequest: DeleteTimeOffRequest,
    private val coreRepository: CoreRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    init {
        buildCalendarStructure()
        getSignedInUser()
        getTimeOffRequests()
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnSetCalendarType -> {
                _state.update { it.copy(calendarType = event.type) }
            }

            is CalendarEvent.OnSetAddTimeOffMode -> {
                _state.update { it.copy(timeOffMode = event.mode) }
                if (!event.mode) {
                    // finished selecting time off dates

                    // submit time off request
                    submitTimeOffRequest()
                }
            }

            is CalendarEvent.OnSetSelectedTimeOff -> {
                val dayUiList = _state.value.calendarStructure.toMutableList()
                val dayIndex = dayUiList.indexOfFirst { it.date == event.date }

                if (dayIndex != -1) {
                    val updatedDay = dayUiList[dayIndex].toggleTimeOff
                    dayUiList[dayIndex] = updatedDay

                    _state.update {
                        it.copy(calendarStructure = dayUiList)
                    }
                }
            }
            is CalendarEvent.OnTimeOffSelected -> {
                _state.update { it.copy(timeOffModel = event.timeOffRequest) }
            }

            is CalendarEvent.OnDeleteTimeOffRequest -> {
                viewModelScope.launch {
                    if (event.timeOffRequestModel?.id != null) {
                        removeTimeOffRequest(event.timeOffRequestModel.id)
                    }
                }
            }
        }
    }

    private fun getSignedInUser() {
        viewModelScope.launch {
            val response = coreRepository.getSignedInUser()
            val user = response.data as FirebaseUser
            _state.update {
                it.copy(user = user)
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

    private fun submitTimeOffRequest() {
        viewModelScope.launch {
            val user = _state.value.user
            val timeOffList = _state.value.calendarStructure
                .filter { it.selectedForTimeOff }
                .map { dayUi ->
                    TimeOffRequestModel(
                        id = null,
                        employeeId = user?.uid,
                        employeeName = user?.displayName,
                        requestOffTime = dayUi.date,
                        timeOffRequestApproveTime = null
                    )
                }

            if (timeOffList.isEmpty()) return@launch
            postTimeOffRequest(timeOffList)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getTimeOffRequests() {
        viewModelScope.launch {
            getTimeOff().collect {
                if (it.error == null) {
                    val timeOffList = it.data as List<TimeOffRequestModel>

                    // Group requests by date
                    val requestsByDate: Map<LocalDate, List<TimeOffRequestModel>> =
                        timeOffList.groupBy { it.requestOffTime }

                    val newDayUiList = _state.value.calendarStructure.map { dayUi ->
                        val requestsForDay = requestsByDate[dayUi.date].orEmpty()

                        dayUi.copy(employeesOff = requestsForDay)
                    }

                    _state.update { it.copy(calendarStructure = newDayUiList) }
                }

                // clear selected dates
                unselectAllDates()
            }
        }
    }

    private fun unselectAllDates() {
        val dayUiList = _state.value.calendarStructure.map { it.copy(selectedForTimeOff = false) }
        _state.update { it.copy(calendarStructure = dayUiList) }
    }

    private fun removeTimeOffRequest(timeOffId: String) {
        viewModelScope.launch {
            val response = deleteTimeOffRequest(timeOffId)
        }
    }
}