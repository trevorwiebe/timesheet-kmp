package com.trevorwiebe.timesheet.authentication.domain

import com.trevorwiebe.timesheet.core.domain.TSResult

interface Authenticator {

    suspend fun signIn(email: String, password: String): TSResult

    suspend fun resetPassword(email: String): TSResult

}