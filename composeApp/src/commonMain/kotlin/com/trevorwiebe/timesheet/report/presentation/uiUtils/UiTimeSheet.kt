package com.trevorwiebe.timesheet.report.presentation.uiUtils

import com.trevorwiebe.timesheet.core.domain.model.TimeSheet

data class UiTimeSheet(
    val timeSheet: TimeSheet,
    val status: TimeSheetStatus
)
