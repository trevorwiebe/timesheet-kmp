package com.trevorwiebe.timesheet.punch.domain

import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.model.Punch
import kotlinx.datetime.Instant

interface PunchRepository {

    suspend fun getSignedInUser(): TSResult

    suspend fun getRates(): TSResult

    suspend fun updatePunch(
        punch: Instant,
        punchId: String
    ): TSResult

    suspend fun getPunches(
        startDate: Instant,
        endDate: Instant
    ): TSResult

    suspend fun addPunch(punch: Punch): TSResult

    suspend fun addHours(
        startPunch: Instant,
        endPunch: Instant,
        rateId: String,
    ): TSResult

    suspend fun deletePunches(
        punchIds: List<String>
    ): TSResult
}