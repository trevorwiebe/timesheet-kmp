package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.core.model.Punch

data class DynamicPunchState(
    val punchList: List<Punch> = emptyList()
)
