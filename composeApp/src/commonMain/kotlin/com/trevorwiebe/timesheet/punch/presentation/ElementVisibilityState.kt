package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch

data class ElementVisibilityState(
    val punchLoading: Boolean = false,
    val showConfirmDeletePunchesSheetUiPunch: UiPunch? = null,
)
