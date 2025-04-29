package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable

@Composable
expect fun NativeTimePicker(
    initialHour: Int = 12,
    initialMinute: Int = 0,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
)
