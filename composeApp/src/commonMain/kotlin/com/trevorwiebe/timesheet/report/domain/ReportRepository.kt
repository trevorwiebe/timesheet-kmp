package com.trevorwiebe.timesheet.report.domain

import com.trevorwiebe.timesheet.core.domain.model.TimeSheet

interface ReportRepository {

    suspend fun getTimeSheetsByUserId(userId: String): List<TimeSheet>
}