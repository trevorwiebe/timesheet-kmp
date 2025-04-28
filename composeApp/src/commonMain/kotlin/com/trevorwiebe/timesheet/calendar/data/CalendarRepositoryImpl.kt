package com.trevorwiebe.timesheet.calendar.data

import com.trevorwiebe.timesheet.calendar.domain.CalendarRepository
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.TimeOffRequestDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CalendarRepositoryImpl(
    private val firebaseDatabase: Firebase,
    private val coreRepository: CoreRepository,
) : CalendarRepository {

    override suspend fun postTimeOffList(timeOffList: List<TimeOffRequestDto>): TSResult {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = coreRepository.getUserId()
        if (userIdResult.error != null) return userIdResult
        val userId = userIdResult.data as String

        val timeOffRequestsCollection = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .collection("users")
            .document(userId)
            .collection("timeOffRequests")

        timeOffList.forEach {
            timeOffRequestsCollection.add(it)
        }

        return TSResult(data = "success")

    }

    override suspend fun getTimeOffList(): Flow<TSResult> {
        val organizationIdResult = coreRepository.getOrganizationId()
        if (organizationIdResult.error != null) return flow { emit(organizationIdResult) }
        val organizationId = organizationIdResult.data as String

        return try {
            // Use the collectionGroup query feature from GitLive SDK
            firebaseDatabase.firestore
                .collectionGroup("timeOffRequests")
                .snapshots
                .map { snapshot ->
                    val requests = snapshot.documents.mapNotNull { document ->
                        try {
                            document.data<TimeOffRequestDto>()
                        } catch (e: Exception) {
                            null
                        }
                    }
                    TSResult(data = requests)
                }
                .catch { e ->
                    emit(TSResult(error = e.message ?: "Unknown error fetching time off requests"))
                }
        } catch (e: Exception) {
            flow { emit(TSResult(error = e.message ?: "Unknown error")) }
        }
    }

}