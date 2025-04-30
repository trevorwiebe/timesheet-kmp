package com.trevorwiebe.timesheet.core.presentation.common

import androidx.compose.runtime.Composable
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.Rate
import kotlinx.datetime.LocalDate

@Composable
actual fun AddHoursDialog(
    currentDate: LocalDate?,
    onDismiss: () -> Unit,
    onConfirm: (Punch, Punch) -> Unit,
    rateList: List<Rate>,
) {

}