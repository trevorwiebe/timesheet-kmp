package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.Instant

data class ElementVisibilityState(
    val punchLoading: Boolean = false,
    val showConfirmDeletePunchesSheetUiPunch: UiPunch? = null,
    val showAddHourDialogTime: Instant? = null,
)
