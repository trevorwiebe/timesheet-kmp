package com.trevorwiebe.timesheet.punch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.model.Organization
import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.Rate
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import com.trevorwiebe.timesheet.punch.domain.usecases.CalculateTimeSheets
import com.trevorwiebe.timesheet.punch.domain.usecases.ProcessPunchesForUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class PunchViewModel(
    private val punchRepository: PunchRepository,
    private val calculateTimeSheets: CalculateTimeSheets,
    private val processPunchesForUi: ProcessPunchesForUi
) : ViewModel() {

    private val _staticPunchState = MutableStateFlow(StaticPunchState())
    val staticPunchState = _staticPunchState.asStateFlow()

    private val _dynamicPunchState = MutableStateFlow(DynamicPunchState())
    val dynamicPunchState = _dynamicPunchState.asStateFlow()

    private val _elementVisibilityState = MutableStateFlow(ElementVisibilityState())
    val elementVisibilityState = _elementVisibilityState.asStateFlow()

    init {
        getOrganization()
        getRates {
            getPunches(
                start = Clock.System.now(),
                end = Clock.System.now()
            )
        }
    }

    fun onEvent(event: PunchEvents) {
        when (event) {
            is PunchEvents.OnPunch -> {
                _staticPunchState.value.rateList.firstOrNull()?.id?.let {
                    sendPunch(it)
                }
            }
            is PunchEvents.OnSetShowConfirmDeletePunchesSheet -> {
                _elementVisibilityState.update {
                    it.copy(showConfirmDeletePunchesSheet = event.show)
                }
            }
            is PunchEvents.OnDeletePunches -> {
                initiateDeletePunches(event.punchIds)
            }
        }
    }

    private fun getTimeSheet() {
        val result = calculateTimeSheets(
            goLiveDate = Instant.parse("2024-12-30T00:00:00.000Z"),
            currentDate = Clock.System.now(),
        )
        _staticPunchState.update { it.copy(timeSheetDateList = result) }
    }

    private fun getOrganization() {
        viewModelScope.launch {
            val result = punchRepository.getOrganization()
            if (result.error.isNullOrEmpty()) {
                val organization = result.data as Organization
                _staticPunchState.update { it.copy(organization = organization) }
                getTimeSheet()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getRates(received: () -> Unit = {}) {
        viewModelScope.launch {
            val result = punchRepository.getRates()
            if (result.error.isNullOrEmpty()) {
                val rates = result.data as List<Rate>
                _staticPunchState.update { it.copy(rateList = rates) }
                received()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getPunches(
        start: Instant,
        end: Instant
    ) {
        viewModelScope.launch {
            punchRepository.getPunches(start, end).collect { result ->
                if (result.error.isNullOrEmpty()) {

                    val punchList = result.data as List<Punch>
                    val rateList = _staticPunchState.value.rateList
                    val datesList = _staticPunchState.value.timeSheetDateList
                    initiatePunchProcessing(datesList, rateList, punchList)

                } else {
                    println(result.error)
                }
            }
        }
    }

    private fun sendPunch(rateId: String) {
        _elementVisibilityState.update { it.copy(punchLoading = true) }
        viewModelScope.launch {
            val punch = Punch(
                punchId = "",
                dateTime = Clock.System.now(),
                rateId = rateId
            )
            punchRepository.addPunch(punch)
            _elementVisibilityState.update { it.copy(punchLoading = false) }
        }
    }

    private fun initiatePunchProcessing(
        datesList: List<Instant>,
        rateList: List<Rate>,
        punchList: List<Punch>
    ) {
        val punchMap = processPunchesForUi(datesList, rateList, punchList)
        println("Punch Map: $punchMap")
        _dynamicPunchState.update { it.copy(punches = punchMap) }
    }

    private fun initiateDeletePunches(
        punchIds: List<String?>
    ) {
        viewModelScope.launch {
            punchRepository.deletePunches(punchIds)
        }
    }
}