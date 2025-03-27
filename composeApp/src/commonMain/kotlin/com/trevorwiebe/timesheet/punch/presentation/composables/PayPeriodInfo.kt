package com.trevorwiebe.timesheet.punch.presentation.composables

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate

@Composable
expect fun PayPeriodInfo(
    show: Boolean,
    onDismiss: () -> Unit,
    currentPayPeriod: Pair<LocalDate, LocalDate>?,
    hoursMap:
    List<Pair<String, Double>>,
)