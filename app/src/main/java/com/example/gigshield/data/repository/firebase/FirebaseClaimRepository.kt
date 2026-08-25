package com.example.gigshield.data.repository.firebase

import com.example.gigshield.data.local.ClaimDao
import com.example.gigshield.data.local.ClaimEntity
import com.example.gigshield.data.repository.Claim
import com.example.gigshield.data.repository.ClaimRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FirebaseClaimRepository(
    private val dao: ClaimDao
) : ClaimRepository {

    private val db by lazy { FirebaseFirestore.getInstance() }

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
