package com.example.gigshield.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "claims")
data class ClaimEntity(
    @PrimaryKey val id: String,
    val date: String,
    val type: String,
    val amount: String,
    val status: String,
    val isSynced: Boolean = false
)
