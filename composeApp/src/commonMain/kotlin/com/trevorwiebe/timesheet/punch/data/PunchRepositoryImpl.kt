package com.trevorwiebe.timesheet.punch.data

import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.OrganizationDto
import com.trevorwiebe.timesheet.core.domain.dto.PunchDto
import com.trevorwiebe.timesheet.core.domain.dto.RateDto
import com.trevorwiebe.timesheet.core.model.Punch
import com.trevorwiebe.timesheet.core.model.toOrganization
import com.trevorwiebe.timesheet.core.model.toPunch
import com.trevorwiebe.timesheet.core.model.toPunchDto
import com.trevorwiebe.timesheet.core.model.toRate
import com.trevorwiebe.timesheet.punch.domain.PunchRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class PunchRepositoryImpl(
    private val firebaseDatabase: Firebase
) : PunchRepository {

    override suspend fun getSignedInUser(): TSResult {
        try {
            val firebaseAuth = firebaseDatabase.auth
            val firebaseUser = firebaseAuth.currentUser
            return if (firebaseUser == null) {
                TSResult(error = "User was null")
            } else {
                TSResult(data = firebaseUser)
            }
        } catch (e: Exception) {
            return TSResult(error = e.message)
        }
    }

    override suspend fun getRates(): TSResult {
        val organizationIdResult = getOrganizationId()
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

    override suspend fun getOrganization(): TSResult {
        val organizationIdResult = getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val organization = firebaseDatabase.firestore
            .collection("organizations")
            .document(organizationId)
            .get()

        return TSResult(
            data = organization.data<OrganizationDto>().toOrganization(id = organizationId)
        )

    }

    override suspend fun updatePunch(punch: Punch): TSResult {
        val organizationIdResult = getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = getUserId()
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

    override suspend fun getPunches(startDate: Instant, endDate: Instant): Flow<TSResult> {
        val organizationIdResult = getOrganizationId()
        if (organizationIdResult.error != null) return flow { emit(organizationIdResult) }
        val organizationId = organizationIdResult.data as String

        val userIdResult = getUserId()
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
        val organizationIdResult = getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = getUserId()
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
        startPunch: Instant,
        endPunch: Instant,
        rateId: String
    ): TSResult {
        TODO("Not yet implemented")
    }

    override suspend fun deletePunches(punchIds: List<String?>): TSResult {
        val organizationIdResult = getOrganizationId()
        if (organizationIdResult.error != null) return organizationIdResult
        val organizationId = organizationIdResult.data as String

        val userIdResult = getUserId()
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

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun getOrganizationId(): TSResult {
        val tsResult = getSignedInUser()
        if (tsResult.error != null) return tsResult
        val firebaseUser = tsResult.data as FirebaseUser
        val idTokenResult = firebaseUser.getIdTokenResult(false)
        val idToken = idTokenResult.token ?: return TSResult(error = "Token was null")

        val parts = idToken.split(".")
        if (parts.size < 2) throw IllegalArgumentException("Invalid JWT token")

        var payloadBase64 = parts[1]
        payloadBase64 = payloadBase64
            .replace('-', '+')  // Convert URL-safe base64 to standard base64
            .replace('_', '/')  // Convert URL-safe base64 to standard base64
            .padEnd((payloadBase64.length + 3) / 4 * 4, '=') // Fix padding

        val payload = Base64.decode(payloadBase64).decodeToString()

        val claims = Json.parseToJsonElement(payload)
        val organizationId = claims.jsonObject["organizationId"]?.toString()

        return if (organizationId != null)
            TSResult(
                data = organizationId
                    .removePrefix("\"")
                    .removeSuffix("\"")
            )
        else TSResult(error = "Organization ID was null")
    }

    private suspend fun getUserId(): TSResult {
        val tsResult = getSignedInUser()
        if (tsResult.error != null) return tsResult
        val firebaseUser = tsResult.data as FirebaseUser

        return TSResult(data = firebaseUser.uid)
    }
}