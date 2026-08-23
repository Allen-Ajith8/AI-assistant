package com.example.gigshield.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "safe_rider_scores")
data class SafeRiderScore(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: Long,
    val score: Int,
    val totalDistanceKm: Float,
    val totalDurationMinutes: Int,
    val harshBrakingCount: Int,
    val rapidAccelCount: Int,
    val sharpCorneringCount: Int,
    val laneChangeCount: Int,
    val streakDays: Int
)
