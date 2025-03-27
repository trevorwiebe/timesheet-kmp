package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun TodayBlockDateLabel(
    date: String,
) {
    Text(
        modifier = Modifier
            .width(26.dp)
            .padding(1.dp)
            .background(
                tertiary,
                RoundedCornerShape(6.dp)
            ),
        text = date,
        textAlign = TextAlign.Center,
        fontSize = 14.sp,
        color = Color.White
    )
}