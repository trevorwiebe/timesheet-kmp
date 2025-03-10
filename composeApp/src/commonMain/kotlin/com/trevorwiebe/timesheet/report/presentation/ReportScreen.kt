package com.trevorwiebe.timesheet.report.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util.toFriendlyDate
import com.trevorwiebe.timesheet.report.presentation.uiUtils.TimeSheetStatus
import com.trevorwiebe.timesheet.report.presentation.uiUtils.UiTimeSheet
import com.trevorwiebe.timesheet.theme.secondary
import com.trevorwiebe.timesheet.theme.successGreenText
import com.trevorwiebe.timesheet.theme.tertiary
import com.trevorwiebe.timesheet.theme.warningYellowText
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportScreen(
    viewModel: ReportViewModel = koinViewModel()
) {

    val staticState by viewModel.staticReportState.collectAsState()

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Reports",
                    color = tertiary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            items(staticState.timeSheets) { timeSheet ->
                ReportItem(timeSheet)
            }
        }
    }
}

@Composable
private fun ReportItem(
    uitTimeSheet: UiTimeSheet
){

    val timeSheet = uitTimeSheet.timeSheet

    val rowScrollState = rememberScrollState()

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
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
                when(uitTimeSheet.status){
                    TimeSheetStatus.CURRENT_PERIOD -> {
                        ChipItem(
                            text = "Current Period",
                            color = tertiary
                        )
                    }
                    TimeSheetStatus.CONFIRMED -> {
                        ChipItem(
                            text = "Confirmed",
                            color = successGreenText
                        )
                    }
                    TimeSheetStatus.CONFIRM_HOURS_NOW -> {
                        ChipItem(
                            text = "Unconfirmed",
                            color = warningYellowText
                        )
                    }
                    TimeSheetStatus.PERIOD_CLOSED -> {
                        ChipItem(
                            text = "Period Closed",
                            color = secondary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

@Composable
private fun ChipItem(
    text: String,
    color: Color
){
    Text(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .clip(CircleShape)
            .background(color)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 14.sp
    )
}