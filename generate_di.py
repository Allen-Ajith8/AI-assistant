import os

di_path = "app/src/main/java/com/example/gigshield/di"
os.makedirs(di_path, exist_ok=True)

files = {
    "app/src/main/java/com/example/gigshield/di/AppContainer.kt": """package com.example.gigshield.di

import android.content.Context
import androidx.room.Room
import com.example.gigshield.data.local.GigShieldDatabase
import com.example.gigshield.data.repository.firebase.*

class AppContainer(private val context: Context) {
    val database: GigShieldDatabase by lazy {
        Room.databaseBuilder(context, GigShieldDatabase::class.java, "gigshield_db")
            .fallbackToDestructiveMigration()
            .build()
    }
    
    val authManager by lazy { FirebaseAuthManager() }
    val workSessionRepository by lazy { FirebaseWorkSessionRepository(database.workSessionDao()) }
    val claimRepository by lazy { FirebaseClaimRepository(database.claimDao()) }
}
""",
    "app/src/main/java/com/example/gigshield/GigShieldApplication.kt": """package com.example.gigshield

import android.app.Application
import com.example.gigshield.di.AppContainer

class GigShieldApplication : Application() {
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
""",
    "app/src/main/java/com/example/gigshield/ui/SharedViewModel.kt": """package com.example.gigshield.ui

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

    fun startWork() {
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
"""
}

for filepath, content in files.items():
    with open(filepath, "w", encoding="utf-8") as f:
        f.write(content)

print("DI and SharedViewModel created.")
