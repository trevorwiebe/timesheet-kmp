package com.trevorwiebe.timesheet.signin.presentation.auth

sealed interface SignInEvents {
    data class OnEmailChange(val email: String) : SignInEvents
    data class OnPasswordChange(val password: String) : SignInEvents
    data object OnSignInClick : SignInEvents

    // reset password events
    data class OnSetSendPasswordResetDialog(val show: Boolean) : SignInEvents
    data object OnSendPasswordResetEmail : SignInEvents

}