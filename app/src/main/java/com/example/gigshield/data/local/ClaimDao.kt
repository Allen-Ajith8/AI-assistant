package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClaimDao {
    @Query("SELECT * FROM claims ORDER BY date DESC")
    fun getAllClaims(): Flow<List<ClaimEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: ClaimEntity)
}
