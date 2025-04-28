package com.trevorwiebe.timesheet.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class CalendarViewModel(
    val postTimeOffRequest: PostTimeOffRequest,
    val getTimeOff: GetTimeOffRequests,
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

                    val newDayUiList = _state.value.calendarStructure.map { dayUi ->
                        val request = timeOffList.find { timeOffRequestModel ->
                            timeOffRequestModel.requestOffTime == dayUi.date
                        }
                        if (request != null) {
                            val currentList = dayUi.employeesOff.toMutableList()
                            val existingRequestIndex = currentList
                                .indexOfFirst { it.employeeId == request.employeeId }
                            if (existingRequestIndex != -1) {
                                currentList[existingRequestIndex] = request
                            } else {
                                currentList.add(request)
                            }
                            dayUi.copy(employeesOff = currentList)
                        } else {
                            dayUi
                        }
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
}