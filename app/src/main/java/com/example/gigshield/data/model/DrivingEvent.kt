package com.example.gigshield.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class DrivingEventType {
    HARSH_BRAKING, RAPID_ACCELERATION, SHARP_CORNERING, LANE_CHANGE
}

@Entity(tableName = "driving_events")
data class DrivingEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: DrivingEventType,
    val severity: Float,
    val timestamp: Long,
    val latitude: Double?,
    val longitude: Double?,
    val speedKmh: Float?
)
