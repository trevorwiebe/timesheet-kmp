package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trevorwiebe.timesheet.core.domain.Util
import com.trevorwiebe.timesheet.core.domain.Util.isValidTimeFormat
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.theme.errorRedBackground
import com.trevorwiebe.timesheet.theme.primary

@Composable
fun PunchPuckTime(
    modifier: Modifier = Modifier,
    initialTime: Punch,
    onTimeSelected: (Punch) -> Unit,
) {

    val initialTimeString = Util.instantToFriendlyTime(initialTime.dateTime)
    var timeString by remember(initialTimeString) { mutableStateOf(initialTimeString) }
    var error by remember { mutableStateOf(false) }

    BasicTextField(
        modifier = modifier
            .background(
                color = if (error) errorRedBackground else primary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
            .clickable { },
        value = timeString,
        onValueChange = {
            timeString = it
            if (isValidTimeFormat(timeString)) {
                error = false
                val newPunch = initialTime.copy(
                    dateTime = Util.parseTimeToInstant(
                        timeString = timeString,
                        contextDate = initialTime.dateTime
                    )
                )
                onTimeSelected(newPunch)
            } else {
                error = true
            }
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    )
}