package com.example.gigshield.data.repository.firebase

import com.example.gigshield.data.local.WorkSessionDao
import com.example.gigshield.data.local.WorkSessionEntity
import com.example.gigshield.data.repository.WorkSession
import com.example.gigshield.data.repository.WorkSessionRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class FirebaseWorkSessionRepository(
    private val dao: WorkSessionDao
) : WorkSessionRepository {

    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun getWorkHistory(): Flow<List<WorkSession>> {
        return dao.getAllSessions().map { list ->
            list.map { WorkSession(it.id, it.date, it.duration, it.premium, it.verified) }
        }
    }

    override suspend fun startWorkSession(): Result<Boolean> {
        val id = UUID.randomUUID().toString()
        val entity = WorkSessionEntity(id, "Today", "0h 0m", "₹25", false, false)
        dao.insertSession(entity)
        
        // Attempt sync
        return try {
            db.collection("work_sessions").document(id).set(entity)
            dao.markAsSynced(id)
            Result.success(true)
        } catch (e: Exception) {
            Result.success(false) // Cached locally
        }
    }

    override suspend fun endWorkSession(): Result<Boolean> {
        return Result.success(true)
    }
}
