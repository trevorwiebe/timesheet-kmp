package com.trevorwiebe.timesheet.authentication.data

import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import com.trevorwiebe.timesheet.core.domain.TSResult
import dev.gitlive.firebase.auth.FirebaseAuth

class AuthImpl(
    private val firebaseAuth: FirebaseAuth
): Authenticator {
    override suspend fun signIn(email: String, password: String): TSResult {
        val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).user
        return if(firebaseUser == null) {
            TSResult(error = "User was null")
        }else{
            TSResult(data = firebaseUser)
        }
    }
}