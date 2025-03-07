package com.trevorwiebe.timesheet.report.domain

import com.trevorwiebe.timesheet.core.domain.TSResult

interface ReportRepository {

    suspend fun getTimeSheets(): TSResult
}