package com.trevorwiebe.timesheet.more.presentation

sealed class MoreEvents {
    data class OnShowConfirmSignOutSheet(val show: Boolean) : MoreEvents()
    data object OnSignOut : MoreEvents()
}