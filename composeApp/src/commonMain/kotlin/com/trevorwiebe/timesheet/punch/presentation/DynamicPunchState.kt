package com.trevorwiebe.timesheet.punch.presentation

import kotlinx.datetime.Instant

data class DynamicPunchState(
    val punches: Map<Instant, List<Triple<String, String, String>>> = emptyMap()
)
