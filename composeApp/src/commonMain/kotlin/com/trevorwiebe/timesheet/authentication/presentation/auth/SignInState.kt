package com.trevorwiebe.timesheet.authentication.presentation.auth

data class SignInState (
    val email: String = "",
    val password: String = "",
    val loadingSignIn: Boolean = false,
    val signInError: String? = null,
    val hidePasswordText: Boolean = false
)