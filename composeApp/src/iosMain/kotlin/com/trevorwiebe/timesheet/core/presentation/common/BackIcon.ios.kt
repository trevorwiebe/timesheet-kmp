package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import timesheet.composeapp.generated.resources.Res
import timesheet.composeapp.generated.resources.baseline_arrow_back_ios_new_24

@Composable
actual fun BackIcon() {
    Icon(
        painter = painterResource(Res.drawable.baseline_arrow_back_ios_new_24),
        contentDescription = "back"
    )
}