package com.trevorwiebe.timesheet.core.domain

interface CoreRepository {

    suspend fun getSignedInUser(): TSResult

    suspend fun getOrganization(): TSResult

    suspend fun getOrganizationId(): TSResult

    suspend fun getUserId(): TSResult

    suspend fun signOut(): TSResult
}