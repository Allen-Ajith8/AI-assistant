import os

base_path = "app/src/main/java/com/example/gigshield/data/repository"

files = {
    "WorkSessionRepository.kt": """package com.example.gigshield.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

data class WorkSession(val id: String, val date: String, val duration: String, val premium: String, val verified: Boolean)

interface WorkSessionRepository {
    fun getWorkHistory(): Flow<List<WorkSession>>
    suspend fun startWorkSession(): Result<Boolean>
    suspend fun endWorkSession(): Result<Boolean>
}

class MockWorkSessionRepository : WorkSessionRepository {
    override fun getWorkHistory(): Flow<List<WorkSession>> = flow {
        emit(listOf(
            WorkSession("1", "24 Aug", "8h 55m", "₹25", true),
            WorkSession("2", "23 Aug", "6h 20m", "₹25", true),
            WorkSession("3", "22 Aug", "9h 10m", "₹25", true)
        ))
    }

    override suspend fun startWorkSession(): Result<Boolean> {
        delay(1500)
        return Result.success(true)
    }

    override suspend fun endWorkSession(): Result<Boolean> {
        delay(1000)
        return Result.success(true)
    }
}
""",
    "ClaimRepository.kt": """package com.example.gigshield.data.repository

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
""",
    "PolicyRepository.kt": """package com.example.gigshield.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class PaymentTx(val date: String, val desc: String, val amount: String, val status: String)
data class PolicyDetails(val policyNumber: String, val status: String, val coverage: String)

interface PolicyRepository {
    fun getPolicyDetails(): Flow<PolicyDetails>
    fun getPayments(): Flow<List<PaymentTx>>
}

class MockPolicyRepository : PolicyRepository {
    override fun getPolicyDetails(): Flow<PolicyDetails> = flow {
        emit(PolicyDetails("POL-882910", "ACTIVE", "₹2,00,000"))
    }

    override fun getPayments(): Flow<List<PaymentTx>> = flow {
        emit(listOf(
            PaymentTx("25 Aug 2026", "Daily Protection Premium", "₹25", "SUCCESS"),
            PaymentTx("24 Aug 2026", "Daily Protection Premium", "₹25", "SUCCESS")
        ))
    }
}
"""
}

for filename, content in files.items():
    with open(os.path.join(base_path, filename), "w", encoding="utf-8") as f:
        f.write(content)

print("Repository files generated successfully.")
