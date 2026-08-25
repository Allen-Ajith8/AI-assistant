import os

base_path = "app/src/main/java/com/example/gigshield/data/local"

files = {
    "WorkSessionEntity.kt": """package com.example.gigshield.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_sessions")
data class WorkSessionEntity(
    @PrimaryKey val id: String,
    val date: String,
    val duration: String,
    val premium: String,
    val verified: Boolean,
    val isSynced: Boolean = false
)
""",
    "WorkSessionDao.kt": """package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkSessionDao {
    @Query("SELECT * FROM work_sessions ORDER BY date DESC")
    fun getAllSessions(): Flow<List<WorkSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkSessionEntity)

    @Query("SELECT * FROM work_sessions WHERE isSynced = 0")
    suspend fun getUnsyncedSessions(): List<WorkSessionEntity>
    
    @Query("UPDATE work_sessions SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: String)
}
""",
    "ClaimEntity.kt": """package com.example.gigshield.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "claims")
data class ClaimEntity(
    @PrimaryKey val id: String,
    val date: String,
    val type: String,
    val amount: String,
    val status: String,
    val isSynced: Boolean = false
)
""",
    "ClaimDao.kt": """package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClaimDao {
    @Query("SELECT * FROM claims ORDER BY date DESC")
    fun getAllClaims(): Flow<List<ClaimEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClaim(claim: ClaimEntity)
}
""",
    "TransactionEntity.kt": """package com.example.gigshield.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val txId: String,
    val date: String,
    val description: String,
    val amount: String,
    val status: String
)
""",
    "TransactionDao.kt": """package com.example.gigshield.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)
}
"""
}

for filename, content in files.items():
    with open(os.path.join(base_path, filename), "w", encoding="utf-8") as f:
        f.write(content)

print("Room DAOs and Entities generated successfully.")
