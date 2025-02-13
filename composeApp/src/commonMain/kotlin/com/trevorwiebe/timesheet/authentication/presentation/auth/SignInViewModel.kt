package com.trevorwiebe.timesheet.authentication.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            is SignInEvents.OnSetSendPasswordResetDialog -> {
                _state.update { it.copy(
                    showSendPasswordResetDialog = event.show
                ) }
            }
            is SignInEvents.OnSendPasswordResetEmail -> {
                sentPasswordResetEmail()
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

    private fun sentPasswordResetEmail(){
        _state.value = _state.value.copy(loadingPasswordReset = true)
        viewModelScope.launch {
            state.value.let {
                authenticator.resetPassword(email = it.email)
                _state.value = _state.value.copy(
                    showSendPasswordResetDialog = false,
                    loadingPasswordReset = false
                )
            }
        }
    }

    private fun getCurrentlySignedInUser(){
        viewModelScope.launch {
            val result = authenticator.getSignedInUser()
            if(result.error.isNullOrEmpty()){
                try {
                    val firebaseUser = result.data as FirebaseUser
                    _onSignInSuccessful.send(Unit)
                }catch (e: Exception){
                    _state.value = _state.value.copy(signInError = result.error)
                }
            }
        }
    }
}