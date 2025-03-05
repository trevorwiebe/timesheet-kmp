package com.trevorwiebe.timesheet.punch.presentation.uiUtils

import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.Rate

data class UiPunch(
    val punchIn: Punch,
    val punchOut: Punch?
) {
    fun getRateName(rateList: List<Rate>): String {
        return rateList.find { it.id == punchIn.rateId }?.description ?: ""
    }
}
