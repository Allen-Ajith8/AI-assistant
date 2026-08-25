package com.example.gigshield.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gigshield.data.repository.WorkSession
import com.example.gigshield.data.repository.Claim
import com.example.gigshield.data.repository.firebase.FirebaseWorkSessionRepository
import com.example.gigshield.data.repository.firebase.FirebaseClaimRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SharedViewModel(
    private val workRepo: FirebaseWorkSessionRepository,
    private val claimRepo: FirebaseClaimRepository
) : ViewModel() {

    val workHistory: StateFlow<List<WorkSession>> = workRepo.getWorkHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
    val claims: StateFlow<List<Claim>> = claimRepo.getClaims()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val activePolicy = kotlinx.coroutines.flow.MutableStateFlow(com.example.gigshield.data.model.InsuranceTier.INCOME_PROTECTOR)
    val safeRiderScore = kotlinx.coroutines.flow.MutableStateFlow(92)
    val earningsToday = kotlinx.coroutines.flow.MutableStateFlow("₹1,250")
    val isWorking = kotlinx.coroutines.flow.MutableStateFlow(false)

    fun startWork() {
        isWorking.value = true

        viewModelScope.launch { workRepo.startWorkSession() }
    }
    
    fun submitClaim() {

        viewModelScope.launch { claimRepo.submitVoiceClaim("dummy_path") }
    }

    companion object {
        fun provideFactory(
            workRepo: FirebaseWorkSessionRepository,
            claimRepo: FirebaseClaimRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SharedViewModel(workRepo, claimRepo) as T
            }
        }
    }
}
