package com.example.gigshield.ui.home

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import com.example.gigshield.data.model.InsuranceTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UiState(
    val isOnline: Boolean = false,
    val currentTier: InsuranceTier? = InsuranceTier.SHIFT_SHIELD,
    val shiftStartTime: Long? = null,
    val todayScore: Int? = 85,
    val weeklyShiftCount: Int = 12,
    val streakDays: Int = 5,
    val earnedRewards: Int = 2
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun toggleOnlineStatus() {
        _uiState.update { state ->
            val nowOnline = !state.isOnline
            // Note: Actual service intent logic goes here
            state.copy(
                isOnline = nowOnline,
                shiftStartTime = if (nowOnline) System.currentTimeMillis() else null
            )
        }
    }
}
