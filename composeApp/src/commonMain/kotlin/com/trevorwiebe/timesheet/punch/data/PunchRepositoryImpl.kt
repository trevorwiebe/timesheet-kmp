package com.trevorwiebe.timesheet.punch.data

import com.trevorwiebe.timesheet.core.data.FirestoreListenerRegistry
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import com.trevorwiebe.timesheet.core.domain.dto.RateDto
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.TimeSheet
import com.trevorwiebe.timesheet.core.domain.model.toPunch
import com.trevorwiebe.timesheet.core.domain.model.toPunchDto
import com.trevorwiebe.timesheet.core.domain.model.toRate
import com.trevorwiebe.timesheet.core.domain.model.toTimeSheetDto
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

class PunchRepositoryImpl(
    private val firebaseDatabase: Firebase,
    private val coreRepository: CoreRepository
) : PunchRepository {

    override suspend fun getRates(): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String
        val ratesList = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("rates")
            .get()
            .documents
            .map { document -> document.data<RateDto>().copy(id = document.id).toRate() }

        return TSResult(data = ratesList)
    }

    override suspend fun updatePunch(punch: Punch): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return userIdResult
        val userId = userIdResult.data as String

        firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("punches")
            .document(punch.punchId)
            .set(punch.toPunchDto(), merge = true)

        return TSResult(data = "Punch updated successfully")
    }

    override suspend fun updatePunchesWithNewRate(punchIn: Punch, punchOut: Punch?): TSResult {
        val updatePunchResult = updatePunch(punchIn)
        if (punchOut != null) {
            updatePunch(punchOut)
        }
        return updatePunchResult
    }

    override suspend fun getPunches(startDate: LocalDate, endDate: LocalDate): Flow<TSResult> {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return flow { emit(organizationIdResult) }
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return flow { emit(userIdResult) }
        val userId = userIdResult.data as String

        val startTimeStamp = Instant.parse(startDate.toString() + "T00:00:00Z")
        val endTimeStamp = Instant.parse(endDate.toString() + "T23:59:59Z")

        val flow = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("punches")
            .where {
                "dateTime" greaterThanOrEqualTo startTimeStamp.toString()
                "dateTime" lessThanOrEqualTo endTimeStamp.toString()
            }
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { documentSnapshot ->
                documentSnapshot.data<PunchDto>().toPunch(punchId = documentSnapshot.id)
            }
            }.map { TSResult(data = it) }

        return FirestoreListenerRegistry.registerFlow(flow)
    }

    override suspend fun addPunch(punch: Punch): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return userIdResult
        val userId = userIdResult.data as String

        val result = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("punches")
            .add(punch.toPunchDto())

        return TSResult(data = result.id)
    }

    override suspend fun addHours(
        startPunch: Punch,
        endPunch: Punch
    ): TSResult {
        val addPunchResult = addPunch(startPunch)
        val addPunch2Result = addPunch(endPunch)
        return addPunchResult
    }

    override suspend fun deletePunches(punchIds: List<String?>): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return userIdResult
        val userId = userIdResult.data as String

        val resultList = mutableListOf<TSResult>()

        for (punchId in punchIds) {
            if (punchId != null) {
                try {
                    firebaseDatabase.firestore
                        .collection("organizations")
                        .document(organizationId)
                        .collection("users")
                        .document(userId)
                        .collection("punches")
                        .document(punchId)
                        .delete()

                    resultList.add(TSResult(data = "Punch $punchId deleted successfully"))
                } catch (e: Exception) {
                    resultList.add(TSResult(error = "Error deleting punch $punchId: ${e.message}"))
                }
            }
        }

        // Return the first error if there is one, otherwise return the first success result
        return resultList.firstOrNull { it.error != null } ?: resultList.firstOrNull() ?: TSResult(
            error = "No punches processed"
        )
    }

    override suspend fun updateTimeSheet(timeSheet: TimeSheet): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return userIdResult
        val userId = userIdResult.data as String

        firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("timeSheets")
            .document(timeSheet.id)
            .set(timeSheet.toTimeSheetDto(), merge = true)

        return TSResult(data = "Timesheet updated successfully")
    }
}