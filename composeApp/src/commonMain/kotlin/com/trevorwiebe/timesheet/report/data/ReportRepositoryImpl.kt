package com.trevorwiebe.timesheet.report.data

import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import dev.gitlive.firebase.Firebase

class ReportRepositoryImpl(
    private val firebaseDatabase: Firebase
) : ReportRepository {

    override suspend fun getTimeSheetsByUserId(userId: String): List<TimeSheet> {
        TODO("Not yet implemented")
    }

}