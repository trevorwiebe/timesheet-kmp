package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.theme.primary

@Composable
fun PunchPuckTime(
    modifier: Modifier,
    initialTimeString: String,
    onTimeSelected: (String) -> Unit,
) {
    Text(
        modifier = Modifier
            .background(
                color = primary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
            .width(75.dp)
            .clickable { },
        text = initialTimeString,
        fontSize = 14.sp,
        maxLines = 1,
        textAlign = TextAlign.Center
    )
}