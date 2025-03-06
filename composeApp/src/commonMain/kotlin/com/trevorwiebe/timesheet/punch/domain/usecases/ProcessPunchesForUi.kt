package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.minus
import kotlinx.datetime.plus

class ProcessPunchesForUi {

    operator fun invoke(
        dateList: List<Instant>,
        punchList: List<Punch>
    ): Map<Instant, List<UiPunch>> {

        val punchMap: MutableMap<Instant, List<UiPunch>> = mutableMapOf()

        val punchIn = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 == 0 }

        val punchOut = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 != 0 }

        dateList.forEachIndexed { index, today ->

            val mutablePunchList: MutableList<UiPunch> = mutableListOf()

            val tomorrow = today.plus(24, DateTimeUnit.HOUR).minus(1, DateTimeUnit.SECOND)
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