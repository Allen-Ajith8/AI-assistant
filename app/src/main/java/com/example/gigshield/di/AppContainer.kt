package com.example.gigshield.di

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
