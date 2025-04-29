package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.theme.calendarBackground
import com.trevorwiebe.timesheet.theme.tertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NativeTimePicker(
    initialHour: Int,
    initialMinute: Int,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit,
) {

    val timeState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    if (showDialog) {
        Dialog(
            onDismissRequest = { onDismiss() },
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ) {
                TimePicker(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White),
                    state = timeState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = calendarBackground,
                        clockDialSelectedContentColor = Color.Unspecified,
                        clockDialUnselectedContentColor = Color.Unspecified,
                        selectorColor = tertiary,
                        containerColor = Color.White,
                        periodSelectorBorderColor = Color.Unspecified,
                        periodSelectorSelectedContainerColor = tertiary,
                        periodSelectorUnselectedContainerColor = calendarBackground,
                        periodSelectorSelectedContentColor = calendarBackground,
                        periodSelectorUnselectedContentColor = Color.Black,
                        timeSelectorSelectedContentColor = calendarBackground,
                        timeSelectorUnselectedContainerColor = calendarBackground,
                        timeSelectorSelectedContainerColor = tertiary,
                        timeSelectorUnselectedContentColor = Color.Black
                    )
                )
            }
        }
    }
}