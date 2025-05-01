package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TodayBlockDateLabel(
    date: String,
    backgroundColor: Color,
    textColor: Color,
    fontWeight: FontWeight,
) {
    Text(
        modifier = Modifier
            .background(
                backgroundColor,
                RoundedCornerShape(6.dp)
            )
            .padding(start = 4.dp, end = 4.dp),
        text = date,
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        color = textColor,
        fontWeight = fontWeight
    )
}