package com.trevorwiebe.timesheet.authentication.presentation.auth

sealed interface SignInEvents {
    data class OnEmailChange(val email: String) : SignInEvents
    data class OnPasswordChange(val password: String) : SignInEvents
    data object OnSignInClick : SignInEvents
}