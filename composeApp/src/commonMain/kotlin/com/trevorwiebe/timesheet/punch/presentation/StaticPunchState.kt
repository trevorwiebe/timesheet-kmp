package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.model.Rate
import kotlinx.datetime.LocalDateTime

// This state isn't necessarily static, it just contains data this
// is initialized at start up and then probable will never change.

data class StaticPunchState(
    val organization: Organization? = null,
    val timeSheetDateList: List<LocalDateTime> = emptyList(),
    val rateList: List<Rate> = emptyList()
)
