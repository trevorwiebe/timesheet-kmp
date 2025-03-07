package com.trevorwiebe.timesheet.report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _staticReportState = MutableStateFlow(StaticReportState())
    val staticReportState = _staticReportState.asStateFlow()

    init {
        getTimeSheets()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getTimeSheets() {
        viewModelScope.launch {
            val result = reportRepository.getTimeSheets()
            if (result.error.isNullOrEmpty()) {
                val timeSheets = result.data as List<TimeSheet>
                _staticReportState.update { it.copy(timeSheets = timeSheets) }
            }
        }
    }
}