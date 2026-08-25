package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkSessionDao {
    @Query("SELECT * FROM work_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<WorkSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkSessionEntity)

    @Query("SELECT * FROM work_sessions WHERE isSynced = 0")
    suspend fun getUnsyncedSessions(): List<WorkSessionEntity>
    
    @Query("UPDATE work_sessions SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)
}
