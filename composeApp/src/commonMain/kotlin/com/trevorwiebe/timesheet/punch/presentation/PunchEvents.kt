package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch

sealed class PunchEvents {
    data object OnPunch : PunchEvents()
    data class OnShowConfirmDeletePunchesSheet(val uiPunch: UiPunch?) : PunchEvents()
    data object OnDeletePunches : PunchEvents()
    data class OnShowAddHoursDialog(val visible: Boolean) : PunchEvents()
    data class OnUpdatePunch(val punch: Punch) : PunchEvents()
    data class OnAddHours(val punchIn: Punch, val punchOut: Punch) : PunchEvents()
    data class OnUpdateRate(val punchIn: Punch, val punchOut: Punch?) : PunchEvents()
}