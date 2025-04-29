package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable

@Composable
actual fun NativeTimePicker(
    initialHour: Int,
    initialMinute: Int,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {

}