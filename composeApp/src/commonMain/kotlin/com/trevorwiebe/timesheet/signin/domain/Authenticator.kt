package com.trevorwiebe.timesheet.signin.domain

import com.trevorwiebe.timesheet.core.domain.TSResult

interface Authenticator {

    suspend fun getSignedInUser(): TSResult

    suspend fun signIn(email: String, password: String): TSResult

    suspend fun resetPassword(email: String): TSResult

}