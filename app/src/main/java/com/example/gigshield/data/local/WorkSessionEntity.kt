package com.example.gigshield.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_sessions")
data class WorkSessionEntity(
    @PrimaryKey val id: String,
    val date: String,
    val duration: String,
    val premium: String,
    val verified: Boolean,
    val isSynced: Boolean = false
)
