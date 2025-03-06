package com.trevorwiebe.timesheet.punch.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.presentation.common.AddHoursDialog
import com.trevorwiebe.timesheet.core.presentation.common.DeletePunchDialog
import com.trevorwiebe.timesheet.core.presentation.common.TimeSheetButton
import com.trevorwiebe.timesheet.punch.presentation.composables.PunchItem
import com.trevorwiebe.timesheet.theme.tertiary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PunchScreen(
    viewModel: PunchViewModel = koinViewModel()
) {

    val staticState by viewModel.staticPunchState.collectAsState()
    val dynamicState by viewModel.dynamicPunchState.collectAsState()
    val elementVisibilityState by viewModel.elementVisibilityState.collectAsState()

    Scaffold(
        topBar = {
            Row (
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Time Sheet",
                    color = tertiary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it).fillMaxSize()
        ) {
            items(staticState.timeSheetDateList) { todayDate ->
                val punchList = dynamicState.punches[todayDate] ?: emptyList()
                PunchItem(
                    date = todayDate,
                    punches = punchList,
                    onShowConfirmDelete = { uiPunch ->
                        viewModel.onEvent(PunchEvents.OnShowConfirmDeletePunchesSheet(uiPunch))
                    },
                    onShowAddHours = { viewModel.onEvent(PunchEvents.OnShowAddHoursDialog(true)) },
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
                    hoursWorked = viewModel.getHoursWorkedForDay(punchList, staticState.rateList)
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
        visible = elementVisibilityState.showAddHoursDialog,
        onConfirm = { punchIn, punchOut ->
            viewModel.onEvent(PunchEvents.OnAddHours(punchIn, punchOut))
        },
        onDismiss = {
            viewModel.onEvent(PunchEvents.OnShowAddHoursDialog(false))
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