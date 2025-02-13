package com.trevorwiebe.timesheet.authentication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authenticator: Authenticator
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
                signIn()
            }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            state.value.let {
                val result = authenticator.signIn(
                    email = it.email,
                    password = it.password
                )
            }
        }
    }

}