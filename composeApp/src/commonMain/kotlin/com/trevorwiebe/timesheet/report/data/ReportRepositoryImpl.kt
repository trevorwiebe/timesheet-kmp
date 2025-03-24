package com.trevorwiebe.timesheet.report.data

import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.TimeSheetDto
import com.trevorwiebe.timesheet.core.domain.model.toTimeSheet
import com.trevorwiebe.timesheet.report.domain.ReportRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class ReportRepositoryImpl(
    private val firebaseDatabase: Firebase,
    private val coreRepository: CoreRepository
) : ReportRepository {

    override suspend fun getTimeSheets(): Flow<TSResult> {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return flow { emit(organizationIdResult) }
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return flow { emit(userIdResult) }
        val userId = userIdResult.data as String

        val flow = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("timeSheets")
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<TimeSheetDto>().toTimeSheet(documentSnapshot.id)
                }
            }
            .map { TSResult(data = it) }

        return FirestoreListenerRegistry.registerFlow(flow)
    }

}