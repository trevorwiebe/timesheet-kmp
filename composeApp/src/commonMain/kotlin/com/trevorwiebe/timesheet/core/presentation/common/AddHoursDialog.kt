package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.theme.errorRedText
import com.trevorwiebe.timesheet.theme.secondary
import com.trevorwiebe.timesheet.theme.tertiary
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus

@Composable
fun AddHoursDialog(
    currentDate: Instant?,
    onDismiss: () -> Unit,
    onConfirm: (Punch, Punch) -> Unit,
    rateList: List<Rate>,
) {

    if (currentDate != null) {

        var punchIn = remember {
            Punch(
                punchId = "",
                dateTime = currentDate,
                rateId = rateList.firstOrNull()?.id ?: ""
            )
        }

        var punchOut = remember {
            Punch(
                punchId = "",
                dateTime = currentDate.plus(1, DateTimeUnit.HOUR),
                rateId = rateList.firstOrNull()?.id ?: ""
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
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
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

                    PunchPuckTime(
                        modifier = Modifier.width(150.dp),
                        initialTime = punchIn,
                        onTimeSelected = {
                            punchIn = it
                        }
                    )

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

                    PunchPuckTime(
                        modifier = Modifier.width(150.dp),
                        initialTime = punchOut,
                        onTimeSelected = {
                            punchOut = it
                        }
                    )

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
                        }
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = tertiary),
                            onClick = onDismiss
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = tertiary),
                            onClick = {
                                println("Punch In: $punchIn")
                                println("Punch Out: $punchOut")

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
                        ) {
                            Text(
                                text = "Save"
                            )
                        }
                    }
                }
            }
        }
    }
}