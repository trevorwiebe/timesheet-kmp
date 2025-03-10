package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class ProcessPunchesForUi {

    operator fun invoke(
        dateList: List<LocalDate>,
        punchList: List<Punch>
    ): Map<LocalDate, List<UiPunch>> {

        val punchMap: MutableMap<LocalDate, List<UiPunch>> = mutableMapOf()

        val punchIn = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 == 0 }

        val punchOut = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 != 0 }

        dateList.forEachIndexed { index, today ->

            val dateTimeTodayStart = LocalDateTime(
                year = today.year,
                monthNumber = today.monthNumber,
                dayOfMonth = today.dayOfMonth,
                hour = 0,
                minute = 0,
                second = 0
            )

            val dateTimeTodayEnd = LocalDateTime(
                year = today.year,
                monthNumber = today.monthNumber,
                dayOfMonth = today.dayOfMonth,
                hour = 23,
                minute = 59,
                second = 59
            )

            val mutablePunchList: MutableList<UiPunch> = mutableListOf()

            val todayPunchesIn =
                punchIn.filter { it.dateTime in dateTimeTodayStart..dateTimeTodayEnd }
            val todayPunchesOut =
                punchOut.filter { it.dateTime in dateTimeTodayStart..dateTimeTodayEnd }

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