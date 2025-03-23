package com.trevorwiebe.timesheet.punch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.Util.localDateTime
import com.trevorwiebe.timesheet.core.domain.Util.roundToTwoDecimals
import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import com.trevorwiebe.timesheet.punch.domain.usecases.CalculateTimeSheets
import com.trevorwiebe.timesheet.punch.domain.usecases.ProcessPunchesForUi
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlin.time.Duration

class PunchViewModel(
    private val startDate: String?,
    private val endDate: String?,
    private val punchRepository: PunchRepository,
    private val coreRepository: CoreRepository,
    private val calculateTimeSheets: CalculateTimeSheets,
    private val processPunchesForUi: ProcessPunchesForUi,
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd
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
            val payPeriod = if (startDate == null && endDate == null) {
                getCurrentPayPeriodStartAndEnd(
                    organization = _staticPunchState.value.organization ?: return@getRates
                )
            } else {
                LocalDate.parse(startDate!!) to LocalDate.parse(endDate!!)
            }
            getPunches(
                start = payPeriod.first,
                end = payPeriod.second
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
            is PunchEvents.OnShowConfirmDeletePunchesSheet -> {
                _elementVisibilityState.update {
                    it.copy(showConfirmDeletePunchesSheetUiPunch = event.uiPunch)
                }
            }
            is PunchEvents.OnDeletePunches -> {
                val uiPunch = _elementVisibilityState.value.showConfirmDeletePunchesSheetUiPunch
                val punchIds = listOf(uiPunch?.punchIn?.punchId, uiPunch?.punchOut?.punchId)
                initiateDeletePunches(punchIds)
                _elementVisibilityState.update {
                    it.copy(showConfirmDeletePunchesSheetUiPunch = null)
                }
            }
            is PunchEvents.OnShowAddHoursDialog -> {
                _elementVisibilityState.update { it.copy(showAddHourDialogTime = event.addHoursDialogTime) }
            }
            is PunchEvents.OnUpdatePunch -> {
                updatePunch(event.punch)
            }
            is PunchEvents.OnAddHours -> {
                initiateAddingHours(event.punchIn, event.punchOut)
            }

            is PunchEvents.OnUpdateRate -> {
                initiateUpdatePunchesWithNewRate(event.punchIn, event.punchOut)
            }
        }
    }

    private fun getTimeSheet() {
        val result = if (startDate == null && endDate == null) {
            calculateTimeSheets(
                organization = _staticPunchState.value.organization ?: return
            )
        } else {
            val startLocalDate = LocalDate.parse(startDate!!)
            val endLocalDate = LocalDate.parse(endDate!!)

            val result = mutableListOf<LocalDate>()

            // Calculate number of days between the dates
            val daysBetween = startLocalDate.daysUntil(endLocalDate)

            // Add all dates to the list
            for (i in 0..daysBetween) {
                result.add(startLocalDate.plus(i, DateTimeUnit.DAY))
            }
            result.toList().reversed()
        }
        _staticPunchState.update { it.copy(timeSheetDateList = result) }
    }

    private fun getOrganization() {
        viewModelScope.launch {
            val result = coreRepository.getOrganization()
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
        start: LocalDate,
        end: LocalDate
    ) {
        val job = viewModelScope.launch {
            punchRepository.getPunches(start, end).collect { result ->
                if (result.error.isNullOrEmpty()) {
                    val punchList = result.data as List<Punch>
                    val datesList = _staticPunchState.value.timeSheetDateList
                    initiatePunchProcessing(datesList, punchList)
                } else {
                    println(result.error)
                }
            }
        }
        FirestoreListenerRegistry.registerJob(job)
    }

    private fun sendPunch(rateId: String) {
        _elementVisibilityState.update { it.copy(punchLoading = true) }
        viewModelScope.launch {
            val punch = Punch(
                punchId = "",
                dateTime = localDateTime(),
                rateId = rateId
            )
            println(punch)
            punchRepository.addPunch(punch)
            _elementVisibilityState.update { it.copy(punchLoading = false) }
        }
    }

    private fun initiatePunchProcessing(
        datesList: List<LocalDate>,
        punchList: List<Punch>
    ) {
        val punchMap = processPunchesForUi(datesList, punchList)
        _dynamicPunchState.update { it.copy(punches = punchMap) }
    }

    private fun initiateDeletePunches(
        punchIds: List<String?>
    ) {
        viewModelScope.launch {
            punchRepository.deletePunches(punchIds)
        }
    }

    private fun updatePunch(punch: Punch) {
        viewModelScope.launch {
            punchRepository.updatePunch(punch)
        }
    }

    private fun initiateAddingHours(
        punchIn: Punch,
        punchOut: Punch
    ) {
        viewModelScope.launch {
            val response = punchRepository.addHours(punchIn, punchOut)
            if (response.error.isNullOrEmpty()) {
                _elementVisibilityState.update { it.copy(showAddHourDialogTime = null) }
            }
        }
    }

    private fun initiateUpdatePunchesWithNewRate(
        punchIn: Punch,
        punchOut: Punch?
    ) {
        viewModelScope.launch {
            val response = punchRepository.updatePunchesWithNewRate(punchIn, punchOut)
        }
    }

    fun getHoursWorkedForDay(
        uiPunchList: List<UiPunch>,
        rateList: List<Rate>
    ): List<Pair<String, Double>> {
        if (uiPunchList.isEmpty()) return emptyList()

        val totalHours = mutableListOf<Pair<String, Double>>()

        rateList.forEach { rate ->
            val ratePunchList = uiPunchList.filter { it.getRate(rateList)?.id == rate.id }

            var rateTotalMinutes = 0L

            ratePunchList.forEach { uiPunch ->
                val punchIn = uiPunch.punchIn
                val punchOut = uiPunch.punchOut ?: return@forEach

                // Calculate duration in minutes
                val durationMinutes =
                    Duration.between(punchIn.dateTime, punchOut.dateTime).inWholeMinutes
                rateTotalMinutes += durationMinutes
            }

            // Convert total minutes to hours and round to nearest integer
            val totalRateHours = (rateTotalMinutes / 60.0)

            val ratePair = Pair("${rate.description} Hours", roundToTwoDecimals(totalRateHours))

            totalHours.add(ratePair)
        }

        return totalHours
    }

    // Extension function to calculate duration between two Instants
    private fun Duration.Companion.between(start: LocalDateTime, end: LocalDateTime): Duration {
        return end.toInstant(TimeZone.currentSystemDefault()) - start.toInstant(TimeZone.currentSystemDefault())
    }
}