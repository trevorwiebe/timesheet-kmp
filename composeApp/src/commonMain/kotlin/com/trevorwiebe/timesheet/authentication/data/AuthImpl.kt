package com.trevorwiebe.timesheet.authentication.data

import com.trevorwiebe.timesheet.authentication.domain.Authenticator
import com.trevorwiebe.timesheet.core.domain.TSResult
import dev.gitlive.firebase.auth.FirebaseAuth

class AuthImpl(
    private val firebaseAuth: FirebaseAuth
): Authenticator {

    override suspend fun getSignedInUser(): TSResult {
        try{
            val firebaseUser = firebaseAuth.currentUser
            return if(firebaseUser == null) {
                TSResult(error = "User was null")
            }else {
                TSResult(data = firebaseUser)
            }
        }catch (e: Exception){
            return TSResult(error = e.message)
        }
    }

    override suspend fun signIn(email: String, password: String): TSResult {
        try{
            val firebaseUser = firebaseAuth.signInWithEmailAndPassword(email, password).user
            return if(firebaseUser == null) {
                TSResult(error = "User was null")
            }else{
                TSResult(data = firebaseUser)
            }
        }catch (e: Exception){
            return TSResult(error = e.message)
        }
    }

    override suspend fun resetPassword(email: String): TSResult {
        try{
            firebaseAuth.sendPasswordResetEmail(email)
            return TSResult(data = "Reset password email sent")
        }catch (e: Exception) {
            return TSResult(error = e.message)
        }
    }
}