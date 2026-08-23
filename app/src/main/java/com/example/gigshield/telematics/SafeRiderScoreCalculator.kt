package com.example.gigshield.telematics

import com.example.gigshield.data.model.DrivingEvent
import com.example.gigshield.data.model.DrivingEventType
import com.example.gigshield.data.model.SafeRiderScore
import java.util.Calendar

class SafeRiderScoreCalculator {

    fun calculateScore(
        shiftStartTime: Long,
        shiftEndTime: Long,
        events: List<DrivingEvent>,
        distanceDrivenKm: Float,
        streakDays: Int
    ): SafeRiderScore {
        val harshBrakingCount = events.count { it.type == DrivingEventType.HARSH_BRAKING }
        val rapidAccelCount = events.count { it.type == DrivingEventType.RAPID_ACCELERATION }
        val sharpCorneringCount = events.count { it.type == DrivingEventType.SHARP_CORNERING }
        val laneChangeCount = events.count { it.type == DrivingEventType.LANE_CHANGE }

        var baseScore = 100
        baseScore -= (harshBrakingCount * 5)
        baseScore -= (rapidAccelCount * 3)
        baseScore -= (sharpCorneringCount * 4)
        baseScore -= (laneChangeCount * 2)

        val finalScore = baseScore.coerceAtLeast(0)
        
        val durationMinutes = ((shiftEndTime - shiftStartTime) / 60000).toInt()
        
        val calendar = Calendar.getInstance().apply {
            timeInMillis = shiftStartTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return SafeRiderScore(
            date = calendar.timeInMillis,
            score = finalScore,
            totalDistanceKm = distanceDrivenKm,
            totalDurationMinutes = durationMinutes,
            harshBrakingCount = harshBrakingCount,
            rapidAccelCount = rapidAccelCount,
            sharpCorneringCount = sharpCorneringCount,
            laneChangeCount = laneChangeCount,
            streakDays = if (finalScore >= 70) streakDays + 1 else 0
        )
    }
}
