package com.trevorwiebe.timesheet.authentication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import dev.gitlive.firebase.auth.FirebaseUser
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
                val email = _state.value.email
                val password = _state.value.password
                if(email.isBlank() || password.isBlank()) {
                    _state.value = _state.value.copy(
                        signInError = "Email or password cannot be blank"
                    )
                    return
                }
                signIn()
            }
        }
    }

    private fun signIn() {
        _state.value = _state.value.copy(loadingSignIn = true)
        viewModelScope.launch {
            state.value.let {
                val result = authenticator.signIn(
                    email = it.email,
                    password = it.password
                )

                if(result.error.isNullOrEmpty()) {
                    try {
                        val firebaseUser = result.data as FirebaseUser

                    } catch (e: Exception) {
                        _state.value = _state.value.copy(signInError = result.error)
                    }
                }else{
                    _state.value = _state.value.copy(signInError = result.error)
                }
                _state.value = _state.value.copy(loadingSignIn = false)
            }
        }
    }

}