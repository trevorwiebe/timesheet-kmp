package com.trevorwiebe.timesheet.punch.presentation.uiUtils

import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.Rate

data class UiPunch(
    val punchIn: Punch,
    val punchOut: Punch?,
    val error: String?,
) {
    fun getRateName(rateList: List<Rate>): String {
        return rateList.find { it.id == punchIn.rateId }?.description ?: ""
    }
    fun getRate(rateList: List<Rate>): Rate? {
        return rateList.find { it.id == punchIn.rateId }
    }
}
