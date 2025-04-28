package com.trevorwiebe.timesheet.calendar.domain.usecases

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetCalendarStructure {

    private val daysToGoForward = 1000
    private val daysToGoBackward = 1000

    operator fun invoke(): List<DayUi> {

        val dateUiList = mutableListOf<DayUi>()
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        var backwardsDate = currentDate
        repeat(daysToGoBackward) {
            backwardsDate = backwardsDate.minus(1, DateTimeUnit.DAY)
            val dayUi = DayUi(backwardsDate, false, emptyList())
            dateUiList.add(dayUi)
        }

        dateUiList.add(DayUi(currentDate, false, emptyList()))

        var forwardsDate = currentDate
        repeat(daysToGoForward) {
            forwardsDate = forwardsDate.plus(1, DateTimeUnit.DAY)
            val dayUi = DayUi(forwardsDate, false, emptyList())
            dateUiList.add(dayUi)
        }

        return dateUiList.sortedBy { it.date }
    }
}