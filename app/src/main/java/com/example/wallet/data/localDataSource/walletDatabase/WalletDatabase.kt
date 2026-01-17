package com.example.wallet.data.localDataSource.walletDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wallet.data.localDataSource.dao.TransactionDao
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity

@Database(
    entities = [LocalTransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WalletDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}