package com.trevorwiebe.timesheet.punch.presentation

sealed class PunchEvents {
    data object OnPunch : PunchEvents()
    data class OnSetShowConfirmDeletePunchesSheet(val show: Boolean) : PunchEvents()
    data class OnDeletePunches(val punchIds: List<String?>) : PunchEvents()
}