package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.Rate
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class ProcessPunchesForUi {

    operator fun invoke(
        dateList: List<Instant>,
        ratesList: List<Rate>,
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

            val tomorrow = dateList.getOrNull(index + 1) ?: Clock.System.now()
            val todayPunchesIn = punchIn.filter { it.dateTime in today..tomorrow }
            val todayPunchesOut = punchOut.filter { it.dateTime in today..tomorrow }

            todayPunchesIn.forEachIndexed { todayIndex, todayPunchIn ->
                val todayPunchOut = todayPunchesOut.getOrNull(todayIndex)
                val uiPunch = buildPunchTriple(todayPunchIn, todayPunchOut, ratesList)
                mutablePunchList.add(uiPunch)
            }

            punchMap[today] = mutablePunchList
        }

        return punchMap
    }

    private fun buildPunchTriple(
        punchIn: Punch,
        punchOut: Punch?,
        rates: List<Rate>
    ): UiPunch {

//        if(punchIn.rateId != punchOut?.rateId){
//            throw Exception("Punch in and out rates do not match")
//        }

        val rate = rates.find { it.id == punchIn.rateId }

        return UiPunch(
            punchIn.dateTime,
            punchOut?.dateTime,
            rate?.description ?: "unavailable",
            punchIn.punchId,
            punchOut?.punchId
        )
    }
}