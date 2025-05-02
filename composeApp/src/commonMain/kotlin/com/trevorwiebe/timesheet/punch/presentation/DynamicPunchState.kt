package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.LocalDate

data class DynamicPunchState(
    val punches: Map<LocalDate, List<UiPunch>> = emptyMap(),
    val timeSheet: TimeSheet? = null,
    val punchToEdit: Punch? = null,
    val clockMessage: String? = null,
) {
    fun isClockedIn(): Boolean {
        val mutablePunchList = mutableListOf<UiPunch>()
        punches.values.forEach { punchList ->
            mutablePunchList.addAll(punchList)
        }
        return mutablePunchList.any { it.punchOut == null }
    }
}
