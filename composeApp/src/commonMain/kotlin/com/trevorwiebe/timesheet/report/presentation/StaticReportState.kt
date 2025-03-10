package com.trevorwiebe.timesheet.report.presentation

import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.report.presentation.uiUtils.UiTimeSheet

data class StaticReportState(
    val timeSheets: List<UiTimeSheet> = emptyList(),
    val organization: Organization? = null
)