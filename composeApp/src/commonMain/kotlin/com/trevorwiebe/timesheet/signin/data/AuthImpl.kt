package com.trevorwiebe.timesheet.signin.data

import com.trevorwiebe.timesheet.core.domain.CoreRepository
import com.trevorwiebe.timesheet.core.domain.TSResult
import com.trevorwiebe.timesheet.signin.domain.Authenticator
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.FirebaseNetworkException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseAuthInvalidUserException
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
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

    private fun getReadableErrorMessage(e: FirebaseException): String {
        return when {
            e is FirebaseAuthInvalidUserException -> "No account exists with this email. Please contact your manager."
            e is FirebaseAuthInvalidCredentialsException -> "Invalid username or password. Please try again."
            e is FirebaseAuthUserCollisionException -> "An account already exists with this email."
            e is FirebaseNetworkException -> "Network error. Please check your connection and try again."
            e.message?.contains("INVALID_LOGIN_CREDENTIALS") == true -> "Invalid email or password. Please check your credentials."
            e.message?.contains("TOO_MANY_ATTEMPTS_TRY_LATER") == true -> "Too many unsuccessful login attempts. Please try again later."
            e.message?.contains("EMAIL_NOT_FOUND") == true -> "No account exists with this email address."
            e.message?.contains("INVALID_PASSWORD") == true -> "The password is invalid for this account."
            e.message?.contains("USER_DISABLED") == true -> "This account has been disabled."
            else -> "Authentication failed: ${e.message}"
        }
    }
}