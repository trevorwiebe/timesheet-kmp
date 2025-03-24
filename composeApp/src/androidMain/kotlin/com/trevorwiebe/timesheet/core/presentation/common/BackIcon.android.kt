package com.trevorwiebe.timesheet.core.presentation.common


import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import timesheet.composeapp.generated.resources.Res
import timesheet.composeapp.generated.resources.baseline_arrow_back_24

@Composable
actual fun BackIcon() {
    Icon(
        painter = painterResource(Res.drawable.baseline_arrow_back_24),
        contentDescription = "back"
    )
}