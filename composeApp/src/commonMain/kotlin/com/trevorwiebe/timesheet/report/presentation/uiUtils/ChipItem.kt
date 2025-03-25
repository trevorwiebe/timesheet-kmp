package com.trevorwiebe.timesheet.report.presentation.uiUtils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChipItem(
    text: String,
    color: Color,
) {
    Text(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .clip(CircleShape)
            .background(color)
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
        text = text,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 14.sp
    )
}