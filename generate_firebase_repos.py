import os

base_path = "app/src/main/java/com/example/gigshield/data/repository/firebase"

files = {
    "FirebaseAuthManager.kt": """package com.example.gigshield.data.repository.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthManager(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun login(email: String, pass: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, pass).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
""",
    "FirebaseWorkSessionRepository.kt": """package com.example.gigshield.data.repository.firebase

import com.example.gigshield.data.local.WorkSessionDao
import com.example.gigshield.data.local.WorkSessionEntity
import com.example.gigshield.data.repository.WorkSession
import com.example.gigshield.data.repository.WorkSessionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FirebaseWorkSessionRepository(
    private val dao: WorkSessionDao,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : WorkSessionRepository {

    override fun getWorkHistory(): Flow<List<WorkSession>> {
        return dao.getAllSessions().map { list ->
            list.map { WorkSession(it.id, it.date, it.duration, it.premium, it.verified) }
        }
    }

    override suspend fun startWorkSession(): Result<Boolean> {
        val id = UUID.randomUUID().toString()
        val entity = WorkSessionEntity(id, "Today", "0h 0m", "₹25", false, false)
        dao.insertSession(entity)
        
        // Attempt sync
        return try {
            db.collection("work_sessions").document(id).set(entity)
            dao.markAsSynced(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.success(false) // Cached locally
        }
    }

    override suspend fun endWorkSession(): Result<Boolean> {
        return Result.success(true)
    }
}
""",
    "FirebaseClaimRepository.kt": """package com.example.gigshield.data.repository.firebase

import com.example.gigshield.data.local.ClaimDao
import com.example.gigshield.data.local.ClaimEntity
import com.example.gigshield.data.repository.Claim
import com.example.gigshield.data.repository.ClaimRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FirebaseClaimRepository(
    private val dao: ClaimDao,
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ClaimRepository {

    override fun getClaims(): Flow<List<Claim>> {
        return dao.getAllClaims().map { list ->
            list.map { Claim(it.id, it.date, it.type, it.amount, it.status) }
        }
    }

    override suspend fun submitVoiceClaim(audioPath: String): Result<Boolean> {
        val id = "GS-" + UUID.randomUUID().toString().take(6).uppercase()
        val entity = ClaimEntity(id, "Today", "Voice", "Pending", "SUBMITTED", false)
        dao.insertClaim(entity)
        
        // In real app, upload to Firebase Storage here
        return try {
            db.collection("claims").document(id).set(entity)
            Result.success(true)
        } catch (e: Exception) {
            Result.success(false)
        }
    }

    override suspend fun uploadEvidence(filePath: String): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun triggerEmergencySos(location: String): Result<Boolean> {
        return Result.success(true)
    }
}
"""
}

for filename, content in files.items():
    with open(os.path.join(base_path, filename), "w", encoding="utf-8") as f:
        f.write(content)

print("Firebase repositories generated successfully.")
