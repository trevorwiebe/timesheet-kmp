package com.trevorwiebe.timesheet.more.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoreViewModel(
    private val coreRepository: CoreRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MoreState())
    val state = _state.asStateFlow()

    private val _onSignOutChannel = Channel<Unit>()
    val onSignOutChannel = _onSignOutChannel.receiveAsFlow()

    init {
        getCurrentUser()
    }

    fun onEvent(event: MoreEvents) {
        when (event) {
            is MoreEvents.OnShowConfirmSignOutSheet -> {
                _state.update { it.copy(confirmSignOutSheet = event.show) }
            }

            is MoreEvents.OnSignOut -> {
                signOut()
            }
        }
    }

    private fun signOut() {
        _state.update { it.copy(signOutLoading = true) }
        viewModelScope.launch {
            FirestoreListenerRegistry.clearAllListeners()
            val result = coreRepository.signOut()
            if (result.error == null) {
                _state.update { it.copy(confirmSignOutSheet = false) }
                _onSignOutChannel.send(Unit)
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            val result = coreRepository.getFirebaseUser()
            if (result.error == null) {
                try {
                    val user = result.data as FirebaseUser
                    _state.update { it.copy(currentUser = user) }
                } catch (e: Exception) {
                    _state.update { it.copy(currentUser = null) }
                }
            }
        }
    }
}