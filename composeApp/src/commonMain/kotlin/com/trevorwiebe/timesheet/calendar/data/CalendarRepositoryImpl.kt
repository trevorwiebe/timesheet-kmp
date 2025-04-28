package com.trevorwiebe.timesheet.calendar.data

import com.trevorwiebe.timesheet.calendar.domain.CalendarRepository
import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.TimeOffRequestDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

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

}