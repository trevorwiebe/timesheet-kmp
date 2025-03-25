package com.trevorwiebe.timesheet.report.presentation.uiUtils

import androidx.compose.ui.graphics.Color
import com.trevorwiebe.timesheet.theme.secondary
import com.trevorwiebe.timesheet.theme.successGreenText
import com.trevorwiebe.timesheet.theme.tertiary
import com.trevorwiebe.timesheet.theme.warningYellowText

enum class TimeSheetStatus(val title: String, val color: Color) {
    CURRENT_PERIOD("Current Period", tertiary),
    CONFIRM_HOURS_NOW("Unsubmitted", warningYellowText),
    CONFIRMED("Submitted", successGreenText),
    PERIOD_CLOSED("Pay Period Closed", secondary)
}