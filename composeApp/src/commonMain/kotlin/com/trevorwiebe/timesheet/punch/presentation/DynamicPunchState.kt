package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.Instant

data class DynamicPunchState(
    val punches: Map<Instant, List<UiPunch>> = emptyMap()
)
