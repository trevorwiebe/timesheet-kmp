package com.trevorwiebe.timesheet.more.presentation

import com.trevorwiebe.timesheet.core.domain.model.DatabaseUserModel

data class MoreState(
    val currentUser: DatabaseUserModel? = null,
    val confirmSignOutSheet: Boolean = false,
    val signOutLoading: Boolean = false,
)