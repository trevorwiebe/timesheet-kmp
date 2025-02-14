package com.trevorwiebe.timesheet.punch.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.presentation.common.TimesheetButton
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
            item {
                PunchItem()
                PunchItem()
                PunchItem()
            }
            item {
                AddPunch(
                    loadingPunch = elementVisibilityState.punchLoading,
                    onPunch = { viewModel.onEvent(PunchEvents.OnPunch) },
                    onAddToPTO = {}
                )
            }
        }
    }
}

@Composable
private fun AddPunch(
    loadingPunch: Boolean,
    onPunch: () -> Unit,
    onAddToPTO: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        TimesheetButton(
            modifier = Modifier.width(150.dp),
            text = "In/Out",
            onClick = onPunch,
            loading = loadingPunch
        )
        Spacer(modifier = Modifier.weight(1f))
        TimesheetButton(
            modifier = Modifier.width(150.dp),
            text = "Add PTO",
            onClick = onAddToPTO,
            loading = false
        )
    }
}