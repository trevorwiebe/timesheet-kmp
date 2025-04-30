package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.theme.calendarBackground
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun NativeTimePicker(
    punch: Punch?,
    onDismiss: () -> Unit,
    onTimeSelected: (Punch) -> Unit,
) {

    val showDialog = punch != null

    if (showDialog) {

        fun updatePunch(timeState: TimePickerState, punch: Punch): Punch {
            val hour = timeState.hour
            val minute = timeState.minute
            val currentDateTime = punch.dateTime

            // Create a new LocalDateTime with the same date but updated time
            val newDateTime = LocalDateTime(
                year = currentDateTime.year,
                monthNumber = currentDateTime.monthNumber,
                dayOfMonth = currentDateTime.dayOfMonth,
                hour = hour,
                minute = minute,
                second = currentDateTime.second,
                nanosecond = currentDateTime.nanosecond
            )

            // Return a copy of the punch with the updated dateTime
            return punch.copy(
                dateTime = newDateTime
            )
        }

        val timeState = rememberTimePickerState(
            initialHour = punch?.dateTime?.hour ?: 12,
            initialMinute = punch?.dateTime?.minute ?: 0,
            is24Hour = false
        )

        Dialog(
            onDismissRequest = { onDismiss() },
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                )
            ) {
                Column {
                    TimePicker(
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(
                                contentColor = Color.Black,
                                containerColor = Color.White
                            )
                        ) {
                            Text("Cancel")
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                if (punch == null) {
                                    onDismiss()
                                    return@Button
                                }
                                // Call the onTimeSelected callback with the current time values
                                val updatedPunch = updatePunch(timeState, punch)
                                onTimeSelected(updatedPunch)
                                onDismiss() // Close the dialog after selection
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tertiary
                            )
                        ) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    }
}