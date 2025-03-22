package com.trevorwiebe.timesheet.more.presentation

import dev.gitlive.firebase.auth.FirebaseUser

data class MoreState(
    val currentUser: FirebaseUser? = null,
    val confirmSignOutSheet: Boolean = false,
    val signOutLoading: Boolean = false
)