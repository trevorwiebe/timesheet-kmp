package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun DayOfWeekText(
    dayInitial: String,
    modifier: Modifier,
) {
    Text(
        modifier = modifier,
        text = dayInitial,
        textAlign = TextAlign.Center,
        fontSize = 14.sp
    )
}