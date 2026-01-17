package com.example.wallet.data.localDataSource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "local_transactions")
data class LocalTransactionEntity(
    @PrimaryKey
    val clientTransactionId: String = UUID.randomUUID().toString(),
    val accountFrom: String,
    val accountTo: String,
    val amount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val syncStatus: String = "QUEUED", // QUEUED, SYNCING, SYNCED, FAILED
    val lastError: String? = null,
    val attemptCount: Int = 0
)
