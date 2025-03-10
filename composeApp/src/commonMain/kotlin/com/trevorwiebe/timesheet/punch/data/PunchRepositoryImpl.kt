package com.trevorwiebe.timesheet.punch.data

import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import com.trevorwiebe.timesheet.core.domain.dto.RateDto
import com.trevorwiebe.timesheet.core.domain.model.Punch
import com.trevorwiebe.timesheet.core.domain.model.toPunch
import com.trevorwiebe.timesheet.core.domain.model.toPunchDto
import com.trevorwiebe.timesheet.core.domain.model.toRate
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant

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

        val isoFormattedDateTime = punch.toString()

        println(isoFormattedDateTime)
        println(punch.punchId)

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

    override suspend fun getPunches(startDate: Instant, endDate: Instant): Flow<TSResult> {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return flow { emit(organizationIdResult) }
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return flow { emit(userIdResult) }
        val userId = userIdResult.data as String

        return firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("punches")
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { documentSnapshot ->
                    documentSnapshot.data<PunchDto>().toPunch(punchId = documentSnapshot.id)
                }
            }.map { TSResult(data = it) }

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
        println(startPunch.rateId)
        println(endPunch.rateId)
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
}