package com.trevorwiebe.timesheet.signin.data

import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.core.domain.Util.getReadableErrorMessage
import com.trevorwiebe.timesheet.signin.domain.Authenticator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.auth

class AuthImpl(
    private val firebase: Firebase,
    private val coreRepository: CoreRepository
): Authenticator {

    override suspend fun getSignedInUser(): TSResult {
        return coreRepository.getSignedInUser()
    }

    override suspend fun signIn(email: String, password: String): TSResult {
        try{
            val firebaseAuth = firebase.auth
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).user
            return if(firebaseUser == null) {
                TSResult(error = "User was null")
            }else{
                TSResult(data = firebaseUser)
            }
        } catch (e: FirebaseException) {
            val errorMessage = getReadableErrorMessage(e)
            return TSResult(error = errorMessage)
        }
    }

    override suspend fun resetPassword(email: String): TSResult {
        try{
            val firebaseAuth = firebase.auth
            firebaseAuth.sendPasswordResetEmail(email)
            return TSResult(data = "Reset password email sent")
        } catch (e: FirebaseException) {
            val errorMessage = getReadableErrorMessage(e)
            return TSResult(error = errorMessage)
        }
    }
}