package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.LocalDateTime

sealed class PunchEvents {
    data object OnPunch : PunchEvents()
    data class OnShowConfirmDeletePunchesSheet(val uiPunch: UiPunch?) : PunchEvents()
    data object OnDeletePunches : PunchEvents()
    data class OnShowAddHoursDialog(val addHoursDialogTime: LocalDateTime?) : PunchEvents()
    data class OnUpdatePunch(val punch: Punch) : PunchEvents()
    data class OnAddHours(val punchIn: Punch, val punchOut: Punch) : PunchEvents()
    data class OnUpdateRate(val punchIn: Punch, val punchOut: Punch?) : PunchEvents()
}