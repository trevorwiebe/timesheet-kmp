package com.trevorwiebe.timesheet.core.presentation

import androidx.compose.runtime.Composable
import com.trevorwiebe.timesheet.report.presentation.uiUtils.TimeSheetStatus

@Composable
expect fun ShiftBottomBar(
    onConfirmPayPeriod: () -> Unit,
    onShowInfo: () -> Unit,
    status: List<TimeSheetStatus>,
)