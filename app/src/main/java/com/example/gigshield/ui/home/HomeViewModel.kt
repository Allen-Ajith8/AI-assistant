package com.example.gigshield.ui.home

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.gigshield.data.model.InsuranceTier
import com.example.gigshield.telematics.TelematicsForegroundService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ShiftSummary(
    val totalTimeMs: Long = 0L,
    val distanceKm: Float = 0f,
    val estimatedEarnings: Float = 0f,
    val safeRiderScore: Int = 0,
    val harshBrakingCount: Int = 0,
    val rapidAccelCount: Int = 0,
    val sharpCorneringCount: Int = 0,
    val topSpeedKmh: Float = 0f
)

data class UiState(
    val isOnline: Boolean = false,
    val currentTier: InsuranceTier? = InsuranceTier.SHIFT_SHIELD,
    val shiftStartTime: Long? = null,
    val todayScore: Int? = 85,
    val weeklyShiftCount: Int = 12,
    val streakDays: Int = 5,
    val earnedRewards: Int = 2,
    val distanceKm: Float = 0f,
    val currentEarnings: Float = 0f,
    val shiftSummary: ShiftSummary? = null,
    val showSummary: Boolean = false
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun toggleOnlineStatus() {
        _uiState.update { state ->
            if (state.isOnline) {
                // Going offline — generate shift summary instead of instant reset
                val shiftDuration = state.shiftStartTime?.let {
                    System.currentTimeMillis() - it
                } ?: 0L

                val durationHours = shiftDuration / 3_600_000f
                val estimatedDistance = state.distanceKm.takeIf { it > 0f } ?: (durationHours * 22f) // ~22 km/hr avg
                val estimatedEarnings = estimatedDistance * 12f // ~₹12 per km estimate

                val summary = ShiftSummary(
                    totalTimeMs = shiftDuration,
                    distanceKm = estimatedDistance,
                    estimatedEarnings = estimatedEarnings,
                    safeRiderScore = state.todayScore ?: 85,
                    harshBrakingCount = 2,
                    rapidAccelCount = 1,
                    sharpCorneringCount = 3,
                    topSpeedKmh = 68f
                )

                // TODO: Wire up once runtime permissions flow is built
                // TelematicsForegroundService.stop(getApplication())

                state.copy(
                    isOnline = false,
                    shiftSummary = summary,
                    showSummary = true
                )
            } else {
                // Going online — start shift

                // TODO: Wire up once runtime permissions flow is built
                // TelematicsForegroundService.start(getApplication())

                state.copy(
                    isOnline = true,
                    shiftStartTime = System.currentTimeMillis(),
                    distanceKm = 0f,
                    currentEarnings = 0f,
                    shiftSummary = null,
                    showSummary = false
                )
            }
        }
    }

    fun dismissSummary() {
        _uiState.update { state ->
            state.copy(
                showSummary = false,
                shiftSummary = null,
                shiftStartTime = null,
                distanceKm = 0f,
                currentEarnings = 0f
            )
        }
    }

    /**
     * Called when the notification "Stop Shift" action is received.
     * This triggers the summary display.
     */
    fun handleNotificationStop() {
        if (_uiState.value.isOnline) {
            toggleOnlineStatus()
        }
    }
}
