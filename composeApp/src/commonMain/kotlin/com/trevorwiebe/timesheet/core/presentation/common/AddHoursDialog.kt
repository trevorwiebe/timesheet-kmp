package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.Util.plusHours
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.PunchType
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.theme.errorRedText
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.secondary
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun AddHoursDialog(
    currentDate: LocalDate?,
    onDismiss: () -> Unit,
    onConfirm: (Punch, Punch) -> Unit,
    rateList: List<Rate>,
) {

    if (currentDate != null) {

        var punchToEdit by remember { mutableStateOf<Punch?>(null) }
        var editingPunchIn by remember { mutableStateOf(false) }
        var editingPunchOut by remember { mutableStateOf(false) }

        val currentDateTime = LocalDateTime(
            year = currentDate.year,
            monthNumber = currentDate.monthNumber,
            dayOfMonth = currentDate.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0
        )

        var punchIn by remember {
            mutableStateOf(
                Punch(
                    punchId = "",
                    dateTime = currentDateTime,
                    rateId = rateList.firstOrNull()?.id ?: "",
                    type = PunchType.IN
                )
            )
        }

        var punchOut by remember {
            mutableStateOf(
                Punch(
                    punchId = "",
                    dateTime = currentDateTime.plusHours(1),
                    rateId = rateList.firstOrNull()?.id ?: "",
                    type = PunchType.OUT
                )
            )
        }

        val error = remember { mutableStateOf("") }

        val selectedRate = remember(rateList) { mutableStateOf(rateList.firstOrNull()) }

        Dialog(
            onDismissRequest = onDismiss,
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Text(
                        fontSize = 20.sp,
                        text = "Add Hours",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        text = "Start Time",
                        fontSize = 14.sp,
                        color = secondary,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primary)
                            .clickable {
                                editingPunchIn = true
                                punchToEdit = punchIn
                            }
                    ) {

                        val initialTimeString = Util.toFriendlyTime(punchIn.dateTime)
                        val timeString by remember(initialTimeString) {
                            mutableStateOf(
                                initialTimeString
                            )
                        }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                            text = timeString,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        text = "End Time",
                        fontSize = 14.sp,
                        color = secondary,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primary)
                            .clickable {
                                editingPunchOut = true
                                punchToEdit = punchOut
                            }
                    ) {

                        val outTimeString = Util.toFriendlyTime(punchOut.dateTime)
                        val timeString by remember(outTimeString) { mutableStateOf(outTimeString) }

                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                            text = timeString,
                            textAlign = TextAlign.Center
                        )
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        text = "Rate",
                        fontSize = 14.sp,
                        color = secondary,
                        textAlign = TextAlign.Center
                    )

                    RateSelector(
                        modifier = Modifier.width(150.dp),
                        rateList = rateList,
                        selectedRate = selectedRate.value,
                        onRateSelected = {
                            selectedRate.value = it
                        },
                        isEditable = true
                    )

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        text = error.value,
                        fontSize = 14.sp,
                        color = errorRedText,
                        textAlign = TextAlign.Center
                    )

                    DialogButtonRow(
                        confirmButtonText = "Save",
                        onConfirmClick = {
                            if (punchOut.dateTime < punchIn.dateTime) {
                                error.value = "End time must be after start time"
                            } else if (selectedRate.value == null) {
                                error.value = "Rate is required but not available"
                            } else {
                                punchIn = punchIn.copy(rateId = selectedRate.value!!.id)
                                punchOut = punchOut.copy(rateId = selectedRate.value!!.id)
                                onConfirm(punchIn, punchOut)
                            }
                        },
                        dismissButtonText = "Cancel",
                        onDismissClick = onDismiss,
                    )
                }
            }
        }

        NativeTimePicker(
            punch = punchToEdit,
            onDismiss = { punchToEdit = null },
        ) { updatedPunch ->
            if (editingPunchIn) {
                punchIn = updatedPunch
                editingPunchIn = false
            } else {
                punchOut = updatedPunch
                editingPunchOut = false
            }
        }

    }
}