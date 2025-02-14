package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.core.model.Rate

// This state isn't necessarily static, it just contains data this
// is initialized at start up and then probable will never change.

data class StaticPunchState(
    val rateList: List<Rate> = emptyList()
)
