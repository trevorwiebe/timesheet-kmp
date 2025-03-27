package com.trevorwiebe.timesheet.calendar.domain.usecases

import com.trevorwiebe.timesheet.calendar.presentation.uiHelper.DayUi
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class GetCalendarStructure {

    private val daysToGoForward = 100
    private val daysToGoBackward = 100

    operator fun invoke(): List<DayUi> {

        val dateUiList = mutableListOf<DayUi>()
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        var backwardsDate = currentDate
        repeat(daysToGoBackward) {
            backwardsDate = backwardsDate.minus(1, DateTimeUnit.DAY)
            val dayUi = DayUi(backwardsDate, emptyList())
            dateUiList.add(dayUi)
        }

        dateUiList.add(DayUi(currentDate, emptyList()))

        var forwardsDate = currentDate
        repeat(daysToGoForward) {
            forwardsDate = forwardsDate.plus(1, DateTimeUnit.DAY)
            val dayUi = DayUi(forwardsDate, emptyList())
            dateUiList.add(dayUi)
        }

        println(dateUiList.sortedBy { it.date }.map { it.date })
        return dateUiList.sortedBy { it.date }

//        val listToRemove = mutableListOf<DayUi>()
//        for(dayUi in dateUiList){
//            if(dayUi.date.dayOfWeek.value == 7){
//                break
//            }else{
//                listToRemove.add(dayUi)
//            }
//        }
//        return (dateUiList - listToRemove.toSet()).toList()
    }
}