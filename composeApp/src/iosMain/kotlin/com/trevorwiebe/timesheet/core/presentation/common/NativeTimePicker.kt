package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import com.trevorwiebe.timesheet.core.domain.model.Punch

@Composable
actual fun NativeTimePicker(
    punch: Punch?,
    onDismiss: () -> Unit,
    onTimeSelected: (Punch) -> Unit,
) {

}