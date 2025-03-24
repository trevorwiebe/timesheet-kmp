package com.trevorwiebe.timesheet.report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import com.trevorwiebe.timesheet.report.presentation.uiUtils.TimeSheetStatus
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
                val currentPayPeriodStart = payPeriodStartAndEnd.first
                val currentPayPeriodEnd = payPeriodStartAndEnd.second

                val statusList = mutableListOf<TimeSheetStatus>()

                // Check if the time sheet is in the current pay period
                if(currentPayPeriodStart == it.payPeriodStart && currentPayPeriodEnd == it.payPeriodEnd){
                    statusList.add(TimeSheetStatus.CURRENT_PERIOD)
                }
                if (it.confirmedByUser.not()) {
                    // Check if the time sheet needs to be confirmed
                    statusList.add(TimeSheetStatus.CONFIRM_HOURS_NOW)
                }
                if (it.submitted.not() && it.confirmedByUser) {
                    statusList.add(TimeSheetStatus.CONFIRMED)
                }

                if (it.submitted && it.confirmedByUser) {
                    statusList.add(TimeSheetStatus.PERIOD_CLOSED)
                }
                mutableTimeSheet.add(UiTimeSheet(it, statusList))
            }

            _staticReportState.value = _staticReportState.value.copy(
                timeSheets = mutableTimeSheet.toList().sortedBy { it.timeSheet.payPeriodStart }
            )
        }
    }
}