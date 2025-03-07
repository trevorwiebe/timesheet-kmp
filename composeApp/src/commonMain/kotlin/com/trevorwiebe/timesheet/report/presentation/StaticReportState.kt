package com.trevorwiebe.timesheet.report.presentation

import com.trevorwiebe.timesheet.core.domain.model.TimeSheet

data class StaticReportState(
    val timeSheets: List<TimeSheet> = emptyList()
)