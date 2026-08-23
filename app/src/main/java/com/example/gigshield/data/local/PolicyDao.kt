package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gigshield.data.model.Policy
import kotlinx.coroutines.flow.Flow

@Dao
interface PolicyDao {
    @Insert
    suspend fun insert(policy: Policy)

    @Query("SELECT * FROM policies WHERE isActive = 1 LIMIT 1")
    fun getActivePolicy(): Flow<Policy?>

    @Query("UPDATE policies SET isActive = 0, endTime = :endTime WHERE isActive = 1")
    suspend fun deactivatePolicy(endTime: Long)

    @Query("SELECT * FROM policies WHERE startTime >= :start AND (endTime IS NULL OR endTime <= :end)")
    suspend fun getPoliciesForWeek(start: Long, end: Long): List<Policy>
}
