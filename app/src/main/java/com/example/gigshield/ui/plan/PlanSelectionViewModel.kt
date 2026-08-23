package com.example.gigshield.ui.plan

import androidx.lifecycle.ViewModel
import com.example.gigshield.data.model.InsuranceTier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PlanState(
    val currentTier: InsuranceTier? = InsuranceTier.SHIFT_SHIELD,
    val isLocked: Boolean = false,
    val lockExpiryDate: String? = null,
    val selectedTier: InsuranceTier? = null
)

class PlanSelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PlanState())
    val uiState: StateFlow<PlanState> = _uiState.asStateFlow()

    fun selectTier(tier: InsuranceTier) {
        if (!_uiState.value.isLocked) {
            _uiState.update { it.copy(selectedTier = tier) }
        }
    }

    fun confirmSelection() {
        val selected = _uiState.value.selectedTier ?: return
        _uiState.update {
            it.copy(
                currentTier = selected,
                isLocked = true,
                lockExpiryDate = "Next Week" // Mock
            )
        }
    }
}
