package com.example.wavetable.helpers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun getAuthState() = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    suspend fun login(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Login failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String): Result<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Registration failed"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        val user = auth.currentUser
        val email = user?.email

        return if (user != null && email != null) {
            try {
                val credential = EmailAuthProvider.getCredential(email, currentPassword)

                // Re-authenticate the user before changing the password
                user.reauthenticate(credential).await()

                // Update the password
                user.updatePassword(newPassword).await()

                Result.success(Unit) // Password changed successfully
            } catch (e: FirebaseAuthException) {
                Result.failure(Exception(e.localizedMessage ?: "Authentication failed"))
            } catch (e: Exception) {
                Result.failure(Exception("Failed to change password"))
            }
        } else {
            Result.failure(Exception("User not authenticated"))
        }
    }

    suspend fun forgotPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
} 