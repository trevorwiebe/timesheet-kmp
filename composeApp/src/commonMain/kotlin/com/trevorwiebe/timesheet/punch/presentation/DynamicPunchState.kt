package com.trevorwiebe.timesheet.punch.presentation

import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.LocalDateTime

data class DynamicPunchState(
    val punches: Map<LocalDateTime, List<UiPunch>> = emptyMap()
) {
    fun isClockedIn(): Boolean {
        val mutablePunchList = mutableListOf<UiPunch>()
        punches.values.forEach { punchList ->
            mutablePunchList.addAll(punchList)
        }
        return mutablePunchList.any { it.punchOut == null }
    }
}
