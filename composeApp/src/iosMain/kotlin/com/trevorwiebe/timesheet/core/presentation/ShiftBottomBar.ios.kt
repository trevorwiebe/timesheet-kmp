package com.trevorwiebe.timesheet.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
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
    Row(
        verticalAlignment = CenterVertically,
        modifier = Modifier.background(primary).fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
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
        IconButton(
            modifier = Modifier.padding(8.dp),
            onClick = { if (status.contains(TimeSheetStatus.CONFIRM_HOURS_NOW)) onConfirmPayPeriod() },
            enabled = status.contains(TimeSheetStatus.CONFIRM_HOURS_NOW)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Localized description",
                tint = if (status.contains(TimeSheetStatus.CONFIRM_HOURS_NOW)) tertiary
                else disabledButtonTertiaryColor
            )
        }
    }
}