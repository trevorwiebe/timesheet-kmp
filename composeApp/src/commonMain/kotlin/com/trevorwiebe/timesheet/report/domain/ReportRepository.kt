package com.trevorwiebe.timesheet.report.domain

import com.trevorwiebe.timesheet.core.domain.TSResult
import kotlinx.coroutines.flow.Flow

interface ReportRepository {

    suspend fun getTimeSheets(): Flow<TSResult>
}