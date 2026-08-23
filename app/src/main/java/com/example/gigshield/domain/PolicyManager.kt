package com.example.gigshield.domain

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.gigshield.data.model.InsuranceTier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar

val Context.dataStore by preferencesDataStore("policy_prefs")

class TierLockedException(message: String) : Exception(message)

class PolicyManager(private val context: Context) {
    private val TIER_KEY = stringPreferencesKey("selected_tier")
    private val LOCK_IN_TIMESTAMP_KEY = longPreferencesKey("lock_in_timestamp")
    private val LOCK_IN_EXPIRY_TIMESTAMP_KEY = longPreferencesKey("lock_in_expiry_timestamp")

    suspend fun selectTier(tier: InsuranceTier) {
        val currentTime = System.currentTimeMillis()
        
        // Check if locked
        context.dataStore.edit { prefs ->
            val expiry = prefs[LOCK_IN_EXPIRY_TIMESTAMP_KEY] ?: 0L
            if (currentTime < expiry) {
                throw TierLockedException("Cannot change tier during lock-in period")
            }
            
            val calendar = Calendar.getInstance().apply {
                timeInMillis = currentTime
                set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            
            val monday = calendar.timeInMillis
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
            val nextMonday = calendar.timeInMillis
            
            prefs[TIER_KEY] = tier.name
            prefs[LOCK_IN_TIMESTAMP_KEY] = monday
            prefs[LOCK_IN_EXPIRY_TIMESTAMP_KEY] = nextMonday
        }
    }

    fun getSelectedTier(): Flow<InsuranceTier?> {
        return context.dataStore.data.map { prefs ->
            prefs[TIER_KEY]?.let { enumValueOf<InsuranceTier>(it) }
        }
    }

    fun isLocked(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            val expiry = prefs[LOCK_IN_EXPIRY_TIMESTAMP_KEY] ?: 0L
            System.currentTimeMillis() < expiry
        }
    }

    fun getLockExpiry(): Flow<Long?> {
        return context.dataStore.data.map { prefs ->
            prefs[LOCK_IN_EXPIRY_TIMESTAMP_KEY]
        }
    }

    suspend fun canChangeTier(): Boolean {
        val prefs = context.dataStore.data.first()
        val expiry = prefs[LOCK_IN_EXPIRY_TIMESTAMP_KEY] ?: 0L
        return System.currentTimeMillis() >= expiry
    }

    fun getRemainingLockDays(): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            val expiry = prefs[LOCK_IN_EXPIRY_TIMESTAMP_KEY] ?: 0L
            val diff = expiry - System.currentTimeMillis()
            if (diff > 0) (diff / (1000 * 60 * 60 * 24)).toInt() else 0
        }
    }
}
