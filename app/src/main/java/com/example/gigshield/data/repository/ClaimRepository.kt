package com.example.gigshield.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

data class Claim(val id: String, val date: String, val type: String, val amount: String, val status: String)

interface ClaimRepository {
    fun getClaims(): Flow<List<Claim>>
    suspend fun submitVoiceClaim(audioPath: String): Result<Boolean>
    suspend fun uploadEvidence(filePath: String): Result<Boolean>
    suspend fun triggerEmergencySos(location: String): Result<Boolean>
}

class MockClaimRepository : ClaimRepository {
    override fun getClaims(): Flow<List<Claim>> = flow {
        emit(listOf(
            Claim("GS1024", "20 Aug", "Accident", "₹50,000", "PROCESSING"),
            Claim("GS0911", "15 Jul", "Medical", "₹5,000", "PAID")
        ))
    }

    override suspend fun submitVoiceClaim(audioPath: String): Result<Boolean> {
        delay(2000)
        return Result.success(true)
    }

    override suspend fun uploadEvidence(filePath: String): Result<Boolean> {
        delay(1500)
        return Result.success(true)
    }
    
    override suspend fun triggerEmergencySos(location: String): Result<Boolean> {
        delay(1000)
        return Result.success(true)
    }
}
