package com.trevorwiebe.timesheet.punch.presentation.uiUtils

import kotlinx.datetime.Instant

data class UiPunch(
    val punchIn: Instant,
    val punchOut: Instant?,
    val rate: String,
    val punchInId: String,
    val punchOutId: String?
)
