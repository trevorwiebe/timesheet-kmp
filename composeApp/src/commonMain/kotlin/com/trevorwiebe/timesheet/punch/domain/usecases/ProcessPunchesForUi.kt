package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.Util.minusSeconds
import com.trevorwiebe.timesheet.core.domain.Util.plusDays
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.LocalDateTime

class ProcessPunchesForUi {

    operator fun invoke(
        dateList: List<LocalDateTime>,
        punchList: List<Punch>
    ): Map<LocalDateTime, List<UiPunch>> {

        val punchMap: MutableMap<LocalDateTime, List<UiPunch>> = mutableMapOf()

        val punchIn = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 == 0 }

        val punchOut = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 != 0 }

        dateList.forEachIndexed { index, today ->

            val mutablePunchList: MutableList<UiPunch> = mutableListOf()

            val tomorrow = today.plusDays(1).minusSeconds(1)
            val todayPunchesIn = punchIn.filter { it.dateTime in today..tomorrow }
            val todayPunchesOut = punchOut.filter { it.dateTime in today..tomorrow }

            todayPunchesIn.forEachIndexed { todayIndex, todayPunchIn ->
                val todayPunchOut = todayPunchesOut.getOrNull(todayIndex)
                val uiPunch = UiPunch(todayPunchIn, todayPunchOut)
                mutablePunchList.add(uiPunch)
            }

            punchMap[today] = mutablePunchList
        }

        return punchMap
    }
}