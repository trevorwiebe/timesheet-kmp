package com.trevorwiebe.timesheet.authentication.presentation.auth

data class SignInState (
    val email: String = "",
    val password: String = "",
    val signInError: String? = null
)