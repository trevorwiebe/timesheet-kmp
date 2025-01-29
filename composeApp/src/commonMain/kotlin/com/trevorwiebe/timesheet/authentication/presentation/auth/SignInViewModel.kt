package com.trevorwiebe.timesheet.authentication.presentation.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignInViewModel(

): ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onEvent(event: SignInEvents){
        when(event) {
            is SignInEvents.OnEmailChange -> {
                _state.value = _state.value.copy(
                    email = event.email
                )
            }
            is SignInEvents.OnPasswordChange -> {
                _state.value = _state.value.copy(
                    password = event.password
                )
            }
            is SignInEvents.OnSignInClick -> {

            }
        }
    }

}