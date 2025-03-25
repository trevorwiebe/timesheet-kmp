package com.trevorwiebe.timesheet.report.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util.toFriendlyDate
import com.trevorwiebe.timesheet.core.presentation.TopBar
import com.trevorwiebe.timesheet.report.presentation.uiUtils.ChipItem
import com.trevorwiebe.timesheet.report.presentation.uiUtils.UiTimeSheet
import com.trevorwiebe.timesheet.theme.secondary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportScreen(
    viewModel: ReportViewModel = koinViewModel(),
    onReportClick: (startTime: String, endTime: String, timesheetId: String) -> Unit,
) {

    val staticState by viewModel.staticReportState.collectAsState()

    Scaffold(
        topBar = { TopBar(title = "Pay Periods") }
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            items(staticState.timeSheets) { timeSheet ->
                ReportItem(timeSheet) { startTime, endTime, timeSheetId ->
                    onReportClick(startTime, endTime, timeSheetId)
                }
            }
        }
    }
}

@Composable
private fun ReportItem(
    uitTimeSheet: UiTimeSheet,
    onReportClick: (startTime: String, endTime: String, timesheetId: String) -> Unit,
){

    val timeSheet = uitTimeSheet.timeSheet

    val rowScrollState = rememberScrollState()

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth().clickable {
                onReportClick(
                    timeSheet.payPeriodStart.toString(),
                    timeSheet.payPeriodEnd.toString(),
                    timeSheet.id
                )
            }
        ){

            val payPeriodString =
                toFriendlyDate(timeSheet.payPeriodStart) + " - " + toFriendlyDate(timeSheet.payPeriodEnd)
            Text(
                modifier = Modifier.padding(8.dp),
                text = payPeriodString,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = secondary
            )
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth().horizontalScroll(rowScrollState)
            ) {
                Spacer(modifier = Modifier.width(4.dp))
                uitTimeSheet.status.forEach { status ->
                    ChipItem(status.title, status.color)
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}