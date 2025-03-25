package com.trevorwiebe.timesheet.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trevorwiebe.timesheet.report.presentation.uiUtils.ChipItem
import com.trevorwiebe.timesheet.report.presentation.uiUtils.TimeSheetStatus
import com.trevorwiebe.timesheet.theme.disabledButtonTertiaryColor
import com.trevorwiebe.timesheet.theme.primary
import com.trevorwiebe.timesheet.theme.tertiary
import org.jetbrains.compose.resources.painterResource
import timesheet.composeapp.generated.resources.Res
import timesheet.composeapp.generated.resources.baseline_info_outline_24

@Composable
actual fun ShiftBottomBar(
    onConfirmPayPeriod: () -> Unit,
    onShowInfo: () -> Unit,
    status: List<TimeSheetStatus>,
) {
    BottomAppBar(
        containerColor = primary,
        contentColor = tertiary,
        modifier = Modifier.fillMaxWidth(),
        actions = {
            IconButton(
                onClick = onShowInfo
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    painter = painterResource(Res.drawable.baseline_info_outline_24),
                    contentDescription = ""
                )
            }
            status.forEach {
                ChipItem(text = it.title, color = it.color)
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { if (status.contains(TimeSheetStatus.CONFIRM_HOURS_NOW)) onConfirmPayPeriod() },
                containerColor = if (status.contains(TimeSheetStatus.CONFIRM_HOURS_NOW)) tertiary
                else disabledButtonTertiaryColor,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(Icons.Filled.Check, "Localized description")
            }
        }
    )
}