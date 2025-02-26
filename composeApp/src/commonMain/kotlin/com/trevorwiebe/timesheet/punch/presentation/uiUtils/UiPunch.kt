package com.trevorwiebe.timesheet.punch.presentation.uiUtils

data class UiPunch(
    val punchIn: String,
    val punchOut: String,
    val rate: String,
    val punchInId: String,
    val punchOutId: String?
)
