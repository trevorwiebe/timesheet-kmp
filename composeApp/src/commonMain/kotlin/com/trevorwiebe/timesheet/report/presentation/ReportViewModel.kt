package com.trevorwiebe.timesheet.report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        viewModelScope.launch {
            val result = reportRepository.getTimeSheets()
            if (result.error.isNullOrEmpty()) {
                val timeSheets = result.data as List<TimeSheet>
                initiateTimeSheetStatusProcessing(timeSheets)
            }
        }
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

                // Check if the time sheet is in the current pay period
                if(currentPayPeriodStart == it.payPeriodStart && currentPayPeriodEnd == it.payPeriodEnd){
                    val uiTimeSheet = UiTimeSheet(
                        timeSheet = it,
                        status = TimeSheetStatus.CURRENT_PERIOD
                    )
                    mutableTimeSheet.add(uiTimeSheet)
                }else if(it.confirmedByUser.not()){
                    // Check if the time sheet needs to be confirmed
                    val uiTimeSheet = UiTimeSheet(
                        timeSheet = it,
                        status = TimeSheetStatus.CONFIRM_HOURS_NOW
                    )
                    mutableTimeSheet.add(uiTimeSheet)
                }else if(it.submitted.not() && it.confirmedByUser){
                    val uiTimeSheet = UiTimeSheet(
                        timeSheet = it,
                        status = TimeSheetStatus.CONFIRMED
                    )
                    mutableTimeSheet.add(uiTimeSheet)
                }else{
                    val uiTimeSheet = UiTimeSheet(
                        timeSheet = it,
                        status = TimeSheetStatus.PERIOD_CLOSED
                    )
                    mutableTimeSheet.add(uiTimeSheet)
                }
            }

            _staticReportState.value = _staticReportState.value.copy(
                timeSheets = mutableTimeSheet.toList().sortedBy { it.timeSheet.payPeriodStart }
            )
        }
    }
}