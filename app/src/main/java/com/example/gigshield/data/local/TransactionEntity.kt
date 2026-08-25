package com.example.gigshield.data.local

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
