package com.trevorwiebe.timesheet.punch.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.Util.getTimeSheetStatus
import com.trevorwiebe.timesheet.core.presentation.ShiftBottomBar
import com.trevorwiebe.timesheet.core.presentation.TopBar
import com.trevorwiebe.timesheet.core.presentation.common.AddHoursDialog
import com.trevorwiebe.timesheet.core.presentation.common.BackIcon
import com.trevorwiebe.timesheet.core.presentation.common.DeletePunchDialog
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton
import com.trevorwiebe.timesheet.punch.presentation.composables.PayPeriodInfo
import com.trevorwiebe.timesheet.punch.presentation.composables.PunchItem
import com.trevorwiebe.timesheet.punch.presentation.composables.SubmitPayPeriodDialog
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PunchScreen(
    startDate: String? = null,
    endDate: String? = null,
    timeSheetId: String? = null,
    onBack: () -> Unit = {},
    viewModel: PunchViewModel = koinViewModel { parametersOf(startDate, endDate, timeSheetId) },
) {

    val staticState by viewModel.staticPunchState.collectAsState()
    val dynamicState by viewModel.dynamicPunchState.collectAsState()
    val elementVisibilityState by viewModel.elementVisibilityState.collectAsState()

    val listVisible = remember { mutableStateOf(false) }
    val shiftBottomBarVisible = remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    val contentUnavailable = staticState.timeSheetDateList.isEmpty()
    LaunchedEffect(contentUnavailable) {
        if (!contentUnavailable) {
            delay(100)
            listVisible.value = true
        }
    }

    val startDateString = startDate?.let {
        Util.toFriendlyDate(LocalDate.parse(it))
    }
    val endDateString = endDate?.let {
        Util.toFriendlyDate(LocalDate.parse(it))
    }
    val title = if (startDate == null) "Current Pay Period" else "$startDateString - $endDateString"

    Scaffold(
        topBar = {
            if (startDate == null) {
                TopBar(title)
            } else {
                TopBar(
                    title = title,
                    backIcon = { BackIcon() },
                    onBack = {
                        onBack()
                        shiftBottomBarVisible.value = false
                    }
                )
            }
        }
    ) {

        if (contentUnavailable) {
            Box(
                modifier = Modifier.padding(it).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = tertiary)
            }
        } else {
            AnimatedVisibility(
                visible = listVisible.value,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    LazyColumn(
                        reverseLayout = true,
                        modifier = Modifier.padding(it).fillMaxSize(),
                        state = listState
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(if (timeSheetId == null) 20.dp else 100.dp))
                        }
                        if (dynamicState.timeSheet == null) {
                            item {
                                AddPunch(
                                    loadingPunch = elementVisibilityState.punchLoading,
                                    onPunch = { viewModel.onEvent(PunchEvents.OnPunch) },
                                    onAddToPTO = {},
                                    buttonText = if (dynamicState.isClockedIn()) "Punch Out" else "Punch In",
                                    showPunchButton = (startDate == null && endDate == null)
                                )
                            }
                        }
                        items(staticState.timeSheetDateList) { todayDate ->
                            val punchList: List<UiPunch> =
                                dynamicState.punches[todayDate] ?: emptyList()
                            val holiday =
                                staticState.holidays.firstOrNull { holiday -> holiday.date == todayDate }
                            PunchItem(
                                editable = dynamicState.timeSheet == null || dynamicState.timeSheet?.submitted == false,
                                date = todayDate,
                                punches = punchList,
                                onShowConfirmDelete = { uiPunch ->
                                    viewModel.onEvent(
                                        PunchEvents.OnShowConfirmDeletePunchesSheet(
                                            uiPunch
                                        )
                                    )
                                },
                                onShowAddHours = {
                                    viewModel.onEvent(
                                        PunchEvents.OnShowAddHoursDialog(
                                            todayDate
                                        )
                                    )
                                },
                                onTimeSelected = { viewModel.onEvent(PunchEvents.OnUpdatePunch(it)) },
                                onUpdateRate = {
                                    viewModel.onEvent(
                                        PunchEvents.OnUpdateRate(
                                            it.punchIn,
                                            it.punchOut
                                        )
                                    )
                                },
                                rateList = staticState.rateList,
                                hoursWorked = viewModel.getHoursWorked(
                                    todayDate,
                                    punchList,
                                ),
                                holiday = holiday?.name
                            )
                        }
                    }

                    if (timeSheetId != null && shiftBottomBarVisible.value) {
                        val currentPayPeriodStartAndEnd = staticState.currentPeriod
                        val isCurrentPeriod =
                            currentPayPeriodStartAndEnd?.first.toString() == startDate
                                    && currentPayPeriodStartAndEnd?.second.toString() == endDate
                        ShiftBottomBar(
                            onConfirmPayPeriod = {
                                viewModel.onEvent(PunchEvents.OnSetSubmitPayPeriodDialog(true))
                            },
                            onShowInfo = {
                                viewModel.onEvent(PunchEvents.OnShowInfo(true))
                            },
                            status = getTimeSheetStatus(
                                dynamicState.timeSheet,
                                isCurrentPeriod
                            )
                        )
                    }
                }
            }
        }
    }

    DeletePunchDialog(
        modifier = Modifier,
        visible = elementVisibilityState.showConfirmDeletePunchesSheetUiPunch != null,
        onConfirm = {
            viewModel.onEvent(PunchEvents.OnDeletePunches)
        },
        onDismiss = {
            viewModel.onEvent(PunchEvents.OnShowConfirmDeletePunchesSheet(null))
        }
    )

    AddHoursDialog(
        currentDate = elementVisibilityState.showAddHourDialogTime,
        onConfirm = { punchIn, punchOut ->
            viewModel.onEvent(PunchEvents.OnAddHours(punchIn, punchOut))
        },
        onDismiss = {
            viewModel.onEvent(PunchEvents.OnShowAddHoursDialog(null))
        },
        rateList = staticState.rateList
    )

    SubmitPayPeriodDialog(
        visible = elementVisibilityState.submitPayPeriodDialog,
        onDismiss = { viewModel.onEvent(PunchEvents.OnSetSubmitPayPeriodDialog(false)) },
        onConfirm = { viewModel.onEvent(PunchEvents.OnConfirmPayPeriod) }
    )

    PayPeriodInfo(
        show = elementVisibilityState.showPayPeriodInfoSheet,
        onDismiss = { viewModel.onEvent(PunchEvents.OnShowInfo(false)) },
        currentPayPeriod = staticState.currentPeriod,
        hoursMap = staticState.hoursMap
    )

}

@Composable
private fun AddPunch(
    showPunchButton: Boolean,
    loadingPunch: Boolean,
    buttonText: String,
    onPunch: () -> Unit,
    onAddToPTO: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        TimeSheetButton(
            modifier = Modifier.width(150.dp).height(50.dp).alpha(0.0f),
            text = "Add PTO",
            onClick = onAddToPTO,
            loading = false
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showPunchButton) {
            TimeSheetButton(
                modifier = Modifier.width(150.dp).height(50.dp),
                text = buttonText,
                onClick = onPunch,
                loading = loadingPunch
            )
        }
    }
}