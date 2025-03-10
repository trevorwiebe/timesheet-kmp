package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun RateSelector(
    modifier: Modifier = Modifier,
    rateList: List<Rate>,
    selectedRate: Rate?,
    onRateSelected: (Rate) -> Unit
) {

    var showRateSelector by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Text(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(primary)
            .padding(start = 8.dp, end = 8.dp, top = 5.dp, bottom = 5.dp)
            .clickable {
                showRateSelector = true
            },
        text = selectedRate?.description ?: "unavailable",
        textAlign = TextAlign.Center,
    )

    if (showRateSelector) {
        Dialog(
            onDismissRequest = {
                showRateSelector = false
            },
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        text = "Select Rate",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                    ) {
                        rateList.forEach { rate ->
                            Text(
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        onRateSelected(rate)
                                        showRateSelector = false
                                    }
                                    .border(
                                        border = BorderStroke(1.dp, Color.Black),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp),
                                text = rate.description
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = tertiary),
                            onClick = { showRateSelector = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}