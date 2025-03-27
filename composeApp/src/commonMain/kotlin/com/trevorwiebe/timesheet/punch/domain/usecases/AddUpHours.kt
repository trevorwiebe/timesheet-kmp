package com.trevorwiebe.timesheet.punch.domain.usecases

import com.trevorwiebe.timesheet.core.domain.Util.between
import com.trevorwiebe.timesheet.core.domain.Util.roundToTwoDecimals
import com.trevorwiebe.timesheet.core.domain.model.Rate
import com.trevorwiebe.timesheet.punch.presentation.uiUtils.UiPunch
import kotlinx.datetime.LocalDate
import kotlin.time.Duration

class AddUpHours {

    operator fun invoke(
        punchMap: Map<LocalDate, List<UiPunch>>,
        rateList: List<Rate>,
    ): List<Pair<String, Double>> {

        val hoursMap = getHoursWorkedForDay(punchMap.values.flatten(), rateList)

        return hoursMap
    }

    private fun getHoursWorkedForDay(
        uiPunchList: List<UiPunch>,
        rateList: List<Rate>,
    ): List<Pair<String, Double>> {
        if (uiPunchList.isEmpty()) return emptyList()

        val totalHours = mutableListOf<Pair<String, Double>>()

        rateList.forEach { rate ->
            val ratePunchList = uiPunchList.filter { it.getRate(rateList)?.id == rate.id }

            var rateTotalMinutes = 0L

            ratePunchList.forEach { uiPunch ->
                val punchIn = uiPunch.punchIn
                val punchOut = uiPunch.punchOut ?: return@forEach

                // Calculate duration in minutes
                val durationMinutes =
                    Duration.Companion.between(punchIn.dateTime, punchOut.dateTime).inWholeMinutes
                rateTotalMinutes += durationMinutes
            }

            // Convert total minutes to hours and round to nearest integer
            val totalRateHours = (rateTotalMinutes / 60.0)

            val ratePair = Pair("${rate.description} Hours", roundToTwoDecimals(totalRateHours))

            totalHours.add(ratePair)
        }

        return totalHours
    }
}