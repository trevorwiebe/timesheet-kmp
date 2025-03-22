package com.trevorwiebe.timesheet.punch.domain

import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.model.Punch
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PunchRepository {

    suspend fun getRates(): TSResult

    suspend fun updatePunch(punch: Punch): TSResult

    suspend fun updatePunchesWithNewRate(punchIn: Punch, punchOut: Punch?): TSResult

    suspend fun getPunches(startDate: LocalDate, endDate: LocalDate): Flow<TSResult>

    suspend fun addPunch(punch: Punch): TSResult

    suspend fun addHours(startPunch: Punch, endPunch: Punch): TSResult

    suspend fun deletePunches(punchIds: List<String?>): TSResult
}