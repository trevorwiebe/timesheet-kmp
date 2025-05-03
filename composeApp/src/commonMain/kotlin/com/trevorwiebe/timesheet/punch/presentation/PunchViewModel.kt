package com.trevorwiebe.timesheet.punch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.Util.localDateTime
import com.trevorwiebe.timesheet.core.domain.model.Holiday
import com.trevorwiebe.timesheet.core.domain.model.Organization
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.PunchType
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.core.domain.usecases.GetCurrentPayPeriodStartAndEnd
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import com.trevorwiebe.timesheet.punch.domain.usecases.AddUpHours
import com.trevorwiebe.timesheet.punch.domain.usecases.CalculateTimeSheets
import com.trevorwiebe.timesheet.punch.domain.usecases.ProcessPunchesForUi
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class PunchViewModel(
    private val startDate: String?,
    private val endDate: String?,
    private val timeSheetId: String?,
    private val punchRepository: PunchRepository,
    private val coreRepository: CoreRepository,
    private val calculateTimeSheets: CalculateTimeSheets,
    private val processPunchesForUi: ProcessPunchesForUi,
    private val addUpHours: AddUpHours,
    private val getCurrentPayPeriodStartAndEnd: GetCurrentPayPeriodStartAndEnd,
) : ViewModel() {

    private val _staticPunchState = MutableStateFlow(StaticPunchState())
    val staticPunchState = _staticPunchState.asStateFlow()

    private val _dynamicPunchState = MutableStateFlow(DynamicPunchState())
    val dynamicPunchState = _dynamicPunchState.asStateFlow()

    private val _elementVisibilityState = MutableStateFlow(ElementVisibilityState())
    val elementVisibilityState = _elementVisibilityState.asStateFlow()

    fun getHoursWorked(
        currentDate: LocalDate,
        punches: List<UiPunch>,
    ): List<Pair<String, Double>> {
        val punchMap = mapOf(currentDate to punches)
        val hours = addUpHours(
            punchMap,
            _staticPunchState.value.rateList
        )
        return hours
    }

    init {
        getOrganization {

            // Calculate the current date list
            calculatePayPeriodDateList()

            getRates {
                val payPeriod = if (startDate == null && endDate == null) {
                    getCurrentPayPeriodStartAndEnd(
                        organization = _staticPunchState.value.organization
                            ?: throw Exception("Organization is null")
                    )
                } else {
                    LocalDate.parse(startDate!!) to LocalDate.parse(endDate!!)
                }
                _staticPunchState.update { it.copy(currentPeriod = payPeriod) }
                getPunches(
                    start = payPeriod.first,
                    end = payPeriod.second
                )
                if (timeSheetId != null) {
                    initiateGetTimeSheet(timeSheetId)
                }

                val currentYear = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .year
                    .toString()
                val countryCode = "US"
                getHolidays(currentYear, countryCode)
            }
        }
    }

    fun onEvent(event: PunchEvents) {
        when (event) {
            is PunchEvents.OnPunch -> {
                _staticPunchState.value.rateList.firstOrNull { it.userFacing }?.id?.let {
                    sendPunch(it, event.punchType)
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
            is PunchEvents.OnSetSubmitPayPeriodDialog -> {
                _elementVisibilityState.update {
                    it.copy(submitPayPeriodDialog = event.visible)
                }
            }

            is PunchEvents.OnConfirmPayPeriod -> {
                val timeSheet = _dynamicPunchState.value.timeSheet?.copy(
                    confirmedByUser = true
                )
                updateTimeSheet(timeSheet)
            }
            is PunchEvents.OnShowInfo -> {
                _elementVisibilityState.update {
                    it.copy(showPayPeriodInfoSheet = event.showInfoSheet)
                }
            }
            is PunchEvents.OnSetTimePickerVisibility -> {
                _dynamicPunchState.update {
                    it.copy(punchToEdit = event.punch)
                }
            }
            is PunchEvents.OnAddPTO -> {
                val date = event.date
                val rateList = _staticPunchState.value.rateList
                val ptoId = Util.getRateIDFromName("PTO", rateList)

                val punchIn = Punch(
                    punchId = "",
                    dateTime = date.atTime(8, 0),
                    rateId = ptoId,
                    type = PunchType.IN
                )

                val punchOut = Punch(
                    punchId = "",
                    dateTime = date.atTime(16, 0),
                    rateId = ptoId,
                    type = PunchType.OUT
                )

                initiateAddingHours(punchIn, punchOut)
            }
        }
    }

    private fun calculatePayPeriodDateList() {
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

    private fun getOrganization(onReceived: () -> Unit) {
        viewModelScope.launch {
            val result = coreRepository.getOrganization()
            if (result.error.isNullOrEmpty()) {
                val organization = result.data as Organization
                _staticPunchState.update { it.copy(organization = organization) }
                onReceived()
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
                }
            }
        }
        FirestoreListenerRegistry.registerJob(job)
    }

    private fun sendPunch(rateId: String, punchType: PunchType) {
        _elementVisibilityState.update { it.copy(punchLoading = true) }
        viewModelScope.launch {
            val punch = Punch(
                punchId = "",
                dateTime = localDateTime(),
                rateId = rateId,
                type = punchType
            )
            punchRepository.addPunch(punch)
            _elementVisibilityState.update { it.copy(punchLoading = false) }
        }
    }

    private fun initiatePunchProcessing(
        datesList: List<LocalDate>,
        punchList: List<Punch>
    ) {
        val mostRecentPunch = punchList.maxByOrNull { it.dateTime }
        if (mostRecentPunch != null && mostRecentPunch.dateTime > localDateTime()) {
            val message = if (mostRecentPunch.type == PunchType.IN) {
                "You have a clock in time that is ahead of the current time"
            } else {
                "You have a clock out time that is ahead of the current time"
            }
            _dynamicPunchState.update { it.copy(clockMessage = message) }
        } else {
            _dynamicPunchState.update { it.copy(clockMessage = null) }
        }
        val punchMap = processPunchesForUi(datesList, punchList)
        val totalHours = addUpHours(punchMap, _staticPunchState.value.rateList)
        _dynamicPunchState.update { it.copy(punches = punchMap) }
        _staticPunchState.update { it.copy(hoursMap = totalHours) }
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

    private fun initiateGetTimeSheet(id: String) {
        viewModelScope.launch {
            punchRepository.getTimeSheetById(id).collect { result ->
                if (result.error.isNullOrEmpty()) {
                    val timeSheet = result.data as TimeSheet
                    _dynamicPunchState.update { it.copy(timeSheet = timeSheet) }
                }
            }
        }
    }

    private fun updateTimeSheet(timeSheet: TimeSheet?) {
        viewModelScope.launch {
            if (timeSheet != null) {
                val result = punchRepository.updateTimeSheet(timeSheet)
                if (result.error.isNullOrEmpty()) {
                    _elementVisibilityState.update { it.copy(submitPayPeriodDialog = false) }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getHolidays(year: String, countryCode: String) {
        viewModelScope.launch {
            val result = coreRepository.getHolidays(year, countryCode)
            if (result.error.isNullOrEmpty()) {
                val holidays = result.data as List<Holiday>
                _staticPunchState.update { it.copy(holidays = holidays) }
            }
        }
    }
}