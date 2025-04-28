package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.successGreenBackground
import com.trevorwiebe.timesheet.theme.successGreenText

@Composable
fun DayListEventItem(
    employeeName: String,
    approved: Boolean,
) {

    val backgroundColor = if (approved) successGreenBackground else primary
    val textColor = if (approved) successGreenText else Color.Black

    Text(
        text = employeeName,
        fontSize = 13.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(2.dp, 0.dp, 2.dp, 0.dp),
        maxLines = 1,
        color = textColor,
        overflow = TextOverflow.Ellipsis
    )
}