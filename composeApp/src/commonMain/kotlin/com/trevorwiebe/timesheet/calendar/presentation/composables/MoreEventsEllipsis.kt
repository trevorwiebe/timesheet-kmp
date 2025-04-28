package com.trevorwiebe.timesheet.calendar.presentation.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import timesheet.composeapp.generated.resources.Res
import timesheet.composeapp.generated.resources.baseline_more_horiz_24

@Composable
fun MoreEventsEllipsis() {

    Icon(
        modifier = Modifier.padding(2.dp, 0.dp, 2.dp, 2.dp),
        painter = painterResource(Res.drawable.baseline_more_horiz_24),
        contentDescription = "more employees"
    )
}