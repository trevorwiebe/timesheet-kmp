package com.trevorwiebe.timesheet.core.data

import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.dto.OrganizationDto
import com.trevorwiebe.timesheet.core.domain.model.toOrganization
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CoreRepositoryImpl(
    private val firebaseDatabase: Firebase
) : CoreRepository {

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

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun getOrganizationId(): TSResult {
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

    override suspend fun getUserId(): TSResult {
        val tsResult = getSignedInUser()
        if (tsResult.error != null) return tsResult
        val firebaseUser = tsResult.data as FirebaseUser

        return TSResult(data = firebaseUser.uid)
    }
}