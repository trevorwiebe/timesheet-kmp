package com.trevorwiebe.timesheet.authentication.presentation.auth

data class SignInState (
    val email: String = "",
    val password: String = "",
    val loadingSignIn: Boolean = true,
    val signInError: String? = null,
    val hidePasswordText: Boolean = true,
    val showSendPasswordResetDialog: Boolean = false,
    val loadingPasswordReset: Boolean = false
)