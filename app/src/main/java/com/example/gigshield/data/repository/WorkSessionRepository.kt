package com.example.gigshield.data.repository

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
