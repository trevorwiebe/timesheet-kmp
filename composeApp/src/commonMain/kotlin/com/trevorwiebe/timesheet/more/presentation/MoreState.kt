package com.trevorwiebe.timesheet.more.presentation

data class MoreState(
    val confirmSignOutSheet: Boolean = false,
    val signOutLoading: Boolean = false
)