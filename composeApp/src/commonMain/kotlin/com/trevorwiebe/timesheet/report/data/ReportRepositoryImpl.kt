package com.trevorwiebe.timesheet.report.data

import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.TimeSheetDto
import com.trevorwiebe.timesheet.core.domain.model.toTimeSheet
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class ReportRepositoryImpl(
    private val firebaseDatabase: Firebase,
    private val coreRepository: CoreRepository
) : ReportRepository {

    override suspend fun getTimeSheets(): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return userIdResult
        val userId = userIdResult.data as String

        val timeSheets = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("timeSheets")
            .get()
            .documents
            .map { document -> document.data<TimeSheetDto>().toTimeSheet(document.id) }

        return TSResult(data = timeSheets)
    }

}