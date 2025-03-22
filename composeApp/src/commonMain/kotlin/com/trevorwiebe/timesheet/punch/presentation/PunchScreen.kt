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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.presentation.common.AddHoursDialog
import com.trevorwiebe.timesheet.core.presentation.common.DeletePunchDialog
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton
import com.trevorwiebe.timesheet.punch.presentation.composables.PunchItem
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PunchScreen(
    viewModel: PunchViewModel = koinViewModel()
) {

    val staticState by viewModel.staticPunchState.collectAsState()
    val dynamicState by viewModel.dynamicPunchState.collectAsState()
    val elementVisibilityState by viewModel.elementVisibilityState.collectAsState()

    val listVisible = remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    val contentUnavailable = staticState.timeSheetDateList.isEmpty()
    LaunchedEffect(contentUnavailable) {
        if (!contentUnavailable) {
            delay(100)
            listVisible.value = true
        }
    }


    Scaffold(
        topBar = {
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Punch",
                    color = tertiary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
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
                LazyColumn(
                    modifier = Modifier.padding(it).fillMaxSize(),
                    state = listState
                ) {
                    items(staticState.timeSheetDateList) { todayDate ->
                        val punchList: List<UiPunch> =
                            dynamicState.punches[todayDate] ?: emptyList()
                        PunchItem(
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
                            hoursWorked = viewModel.getHoursWorkedForDay(
                                punchList,
                                staticState.rateList
                            )
                        )
                    }
                    item {
                        AddPunch(
                            loadingPunch = elementVisibilityState.punchLoading,
                            onPunch = { viewModel.onEvent(PunchEvents.OnPunch) },
                            onAddToPTO = {},
                            buttonText = if (dynamicState.isClockedIn()) "Punch Out" else "Punch In"
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
}

@Composable
private fun AddPunch(
    loadingPunch: Boolean,
    buttonText: String,
    onPunch: () -> Unit,
    onAddToPTO: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        TimeSheetButton(
            modifier = Modifier.width(150.dp).height(50.dp),
            text = buttonText,
            onClick = onPunch,
        )
        Spacer(modifier = Modifier.weight(1f))
        TimeSheetButton(
            modifier = Modifier.width(150.dp).height(50.dp),
            text = "Add PTO",
            onClick = onAddToPTO,
        )
    }
}