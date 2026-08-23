package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.gigshield.data.model.SafeRiderScore
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: SafeRiderScore)

    @Query("SELECT * FROM safe_rider_scores WHERE date = :date LIMIT 1")
    suspend fun getScoreForDate(date: Long): SafeRiderScore?

    @Query("SELECT * FROM safe_rider_scores ORDER BY date DESC LIMIT :limit")
    fun getRecentScores(limit: Int): Flow<List<SafeRiderScore>>

    @Query("SELECT COUNT(*) FROM (SELECT score FROM safe_rider_scores ORDER BY date DESC) WHERE score >= 70")
    suspend fun getCurrentStreak(): Int

    @Query("SELECT AVG(score) FROM (SELECT score FROM safe_rider_scores ORDER BY date DESC LIMIT :days)")
    suspend fun getAverageScore(days: Int): Float?
}
