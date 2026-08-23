package com.example.gigshield.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "policies")
data class Policy(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tier: InsuranceTier,
    val startTime: Long,
    val endTime: Long?,
    val isActive: Boolean,
    val dateCreated: Long
)
