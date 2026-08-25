package com.example.gigshield.data.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager {
    
    private val auth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUserId(): String? = try { auth.currentUser?.uid } catch (e: Exception) { null }

    suspend fun login(email: String, pass: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
