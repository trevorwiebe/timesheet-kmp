package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.Rate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ProcessPunchesForUi {

    operator fun invoke(
        dateList: List<Instant>,
        ratesList: List<Rate>,
        punchList: List<Punch>
    ): Map<Instant, List<Triple<String, String, String>>> {

        val punchMap: MutableMap<Instant, List<Triple<String, String, String>>> = mutableMapOf()

        val punchIn = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 == 0 }

        val punchOut = punchList
            .sortedBy { it.dateTime }
            .filterIndexed { index, _ -> index % 2 != 0 }

        dateList.forEachIndexed { index, today ->

            val mutablePunchList: MutableList<Triple<String, String, String>> = mutableListOf()

            val tomorrow = dateList.getOrNull(index + 1) ?: Clock.System.now()
            val todayPunchesIn = punchIn.filter { it.dateTime in today..tomorrow }
            val todayPunchesOut = punchOut.filter { it.dateTime in today..tomorrow }

            todayPunchesIn.forEachIndexed { todayIndex, todayPunchIn ->
                val todayPunchOut = todayPunchesOut.getOrNull(todayIndex)
                val punchTriple = buildPunchTriple(todayPunchIn, todayPunchOut, ratesList)
                mutablePunchList.add(punchTriple)
            }

            punchMap.put(today, mutablePunchList)
        }

        return punchMap
    }

    private fun buildPunchTriple(
        punchIn: Punch,
        punchOut: Punch?,
        rates: List<Rate>
    ): Triple<String, String, String> {

//        if(punchIn.rateId != punchOut?.rateId){
//            throw Exception("Punch in and out rates do not match")
//        }

        val rate = rates.find { it.id == punchIn.rateId }
        val punchInTime = punchIn.dateTime.toLocalDateTime(TimeZone.currentSystemDefault()).time
        val punchOutTime =
            punchOut?.dateTime?.toLocalDateTime(TimeZone.currentSystemDefault())?.time

        return Triple(
            formatLocalTime(punchInTime),
            formatLocalTime(punchOutTime),
            rate?.description ?: "unavailable"
        )
    }

    private fun formatLocalTime(localTime: LocalTime?): String {
        if (localTime == null) return ""
        val hour = localTime.hour
        val minute = localTime.minute

        val period = if (hour < 12) "AM" else "PM"
        val hour12 = when {
            hour == 0 -> 12  // Midnight case
            hour > 12 -> hour - 12  // Convert to 12-hour format
            else -> hour
        }

        return "${hour12}:${if (minute < 10) "0$minute" else minute} $period"
    }
}