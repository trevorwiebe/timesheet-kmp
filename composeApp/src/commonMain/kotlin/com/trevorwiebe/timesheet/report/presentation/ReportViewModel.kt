package com.trevorwiebe.timesheet.report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.Util.getTimeSheetStatus
import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import com.trevorwiebe.timesheet.report.presentation.uiUtils.UiTimeSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportViewModel(
    private val reportRepository: ReportRepository,
    private val coreRepository: CoreRepository,
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd
) : ViewModel() {

    private val _staticReportState = MutableStateFlow(StaticReportState())
    val staticReportState = _staticReportState.asStateFlow()

    init { getTimeSheets() }

    @Suppress("UNCHECKED_CAST")
    private fun getTimeSheets() {
        val job = viewModelScope.launch {
            reportRepository.getTimeSheets().collect { result ->
                if (result.error.isNullOrEmpty()) {
                    val timeSheets = result.data as List<TimeSheet>
                    initiateTimeSheetStatusProcessing(timeSheets)
                }
            }
        }
        FirestoreListenerRegistry.registerJob(job)
    }

    private suspend fun initiateTimeSheetStatusProcessing(timeSheets: List<TimeSheet>){
        val result = coreRepository.getOrganization()
        if (result.error.isNullOrEmpty()) {

            val mutableTimeSheet = mutableListOf<UiTimeSheet>()

            val organization = result.data as Organization
            val payPeriodStartAndEnd = getCurrentPayPeriodStartAndEnd(organization)

            timeSheets.forEach {
                val timeSheetStatus = getTimeSheetStatus(it, payPeriodStartAndEnd)
                mutableTimeSheet.add(UiTimeSheet(it, timeSheetStatus))
            }

            _staticReportState.value = _staticReportState.value.copy(
                timeSheets = mutableTimeSheet.toList().sortedBy { it.timeSheet.payPeriodStart }
            )
        }
    }
}