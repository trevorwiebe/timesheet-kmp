package com.trevorwiebe.timesheet.more.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MoreViewModel : ViewModel() {

    private val _state = MutableStateFlow(MoreState())
    val state = _state.asStateFlow()

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

    }
}