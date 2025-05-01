package com.trevorwiebe.timesheet.core.domain

interface CoreRepository {

    suspend fun getFirebaseUser(): TSResult

    suspend fun getDatabaseUser(): TSResult

    suspend fun getOrganization(): TSResult

    suspend fun getOrganizationId(): TSResult

    suspend fun getUserId(): TSResult

    suspend fun signOut(): TSResult

    suspend fun getHolidays(year: String, countryCode: String): TSResult
}