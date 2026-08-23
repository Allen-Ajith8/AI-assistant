package com.example.gigshield.telematics

import com.example.gigshield.data.model.DrivingEvent
import com.example.gigshield.data.model.DrivingEventType
import kotlin.math.abs

class DrivingEventDetector(
    private val onEventDetected: (DrivingEvent) -> Unit
) {
    private var lastEventTime: Long = 0
    private val cooldownMs = 3000L
    
    private var currentSpeedKmh: Float? = null
    private var sustainedYawStartTime: Long? = null

    fun updateSpeed(speedKmh: Float) {
        currentSpeedKmh = speedKmh
    }

    fun processAcceleration(x: Float, y: Float, z: Float, timestamp: Long) {
        if (timestamp - lastEventTime < cooldownMs) return

        val longitudinalAccel = y
        
        if (longitudinalAccel < -8.0f) {
            val severity = (abs(longitudinalAccel) - 8.0f) / 10.0f
            emitEvent(DrivingEventType.HARSH_BRAKING, severity.coerceIn(0f, 1f), timestamp)
            return
        }
        
        if (longitudinalAccel > 6.0f) {
            val severity = (longitudinalAccel - 6.0f) / 10.0f
            emitEvent(DrivingEventType.RAPID_ACCELERATION, severity.coerceIn(0f, 1f), timestamp)
        }
    }

    fun processGyroscope(x: Float, y: Float, z: Float, timestamp: Long) {
        if (timestamp - lastEventTime < cooldownMs) return

        val yawRate = abs(z)
        
        if (yawRate > 0.5f) {
            if (sustainedYawStartTime == null) {
                sustainedYawStartTime = timestamp
            } else if (timestamp - sustainedYawStartTime!! >= 500) {
                val severity = (yawRate - 0.5f) / 2.0f
                emitEvent(DrivingEventType.SHARP_CORNERING, severity.coerceIn(0f, 1f), timestamp)
                sustainedYawStartTime = null
            }
        } else {
            sustainedYawStartTime = null
        }
        
        if (yawRate > 1.2f && sustainedYawStartTime == null) {
            emitEvent(DrivingEventType.LANE_CHANGE, 0.5f, timestamp)
        }
    }

    private fun emitEvent(type: DrivingEventType, severity: Float, timestamp: Long) {
        lastEventTime = timestamp
        val event = DrivingEvent(
            type = type,
            severity = severity,
            timestamp = timestamp,
            latitude = null,
            longitude = null,
            speedKmh = currentSpeedKmh
        )
        onEventDetected(event)
    }
}
