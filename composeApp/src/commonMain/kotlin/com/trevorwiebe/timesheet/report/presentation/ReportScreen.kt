package com.trevorwiebe.timesheet.report.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
                .padding(start = 16.dp, end = 16.dp)
        ) {
            items(staticState.timeSheets) { timeSheet ->
                Text(
                    text = timeSheet
                        .payPeriodStart
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                        .toString()
                )
            }
        }
    }
}