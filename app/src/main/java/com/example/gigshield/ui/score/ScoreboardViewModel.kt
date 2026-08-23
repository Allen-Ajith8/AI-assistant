package com.example.gigshield.ui.score

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ScoreState(
    val todayScore: Int = 85,
    val streakCount: Int = 5,
    val history: List<Int> = listOf(70, 75, 80, 82, 85, 90, 85),
    val rewards: List<String> = listOf("1 Free Premium Day")
)

class ScoreboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ScoreState())
    val uiState: StateFlow<ScoreState> = _uiState.asStateFlow()
}
