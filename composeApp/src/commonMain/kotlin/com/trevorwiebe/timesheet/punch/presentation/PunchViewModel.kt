package com.trevorwiebe.timesheet.punch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.Rate
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class PunchViewModel(
    private val punchRepository: PunchRepository,
) : ViewModel() {

    private val _staticPunchState = MutableStateFlow(StaticPunchState())
    val staticPunchState = _staticPunchState.asStateFlow()


    init {
        getRates()
    }

    fun onEvent(event: PunchEvents) {
        when (event) {
            is PunchEvents.OnPunch -> {
                sendPunch("ESNJ5lnEgTcOCn1e8Sye")
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRates() {
        viewModelScope.launch {
            val result = punchRepository.getRates()
            if (result.error.isNullOrEmpty()) {
                val rates = result.data as List<Rate>
                _staticPunchState.update { it.copy(rateList = rates) }
            } else {
                println(result.error)
            }
        }
    }

    private fun sendPunch(rateId: String) {
        viewModelScope.launch {
            val punch = Punch(
                punchId = "",
                dateTime = Clock.System.now(),
                rateId = rateId
            )
            val result = punchRepository.addPunch(punch)
            if (result.error.isNullOrEmpty()) {
                println(result.data)
            } else {
                println(result.error)
            }
        }
    }
}