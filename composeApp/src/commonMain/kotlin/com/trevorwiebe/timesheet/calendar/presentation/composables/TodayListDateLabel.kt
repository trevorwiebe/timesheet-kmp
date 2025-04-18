package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.theme.tertiary

@Composable
fun TodayListDateLabel(
    day: String,
    date: String,
) {
    Row {
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(3.dp)
                .background(tertiary, RoundedCornerShape(8.dp))
                .padding(3.dp),
            text = day,
            textAlign = TextAlign.Center,
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier
                .wrapContentSize()
                .padding(3.dp)
                .background(tertiary, RoundedCornerShape(8.dp))
                .padding(3.dp),
            text = date,
            textAlign = TextAlign.Center,
            color = Color.White
        )
    }

}