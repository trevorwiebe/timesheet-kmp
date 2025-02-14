package com.trevorwiebe.timesheet.punch.presentation

sealed class PunchEvents {
    data object OnPunch : PunchEvents()
}