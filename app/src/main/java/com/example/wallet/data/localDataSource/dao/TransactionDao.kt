package com.example.wallet.data.localDataSource.dao
import androidx.room.*
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: LocalTransactionEntity)

    @Update
    suspend fun updateTransaction(transaction: LocalTransactionEntity)

    @Query("SELECT * FROM local_transactions WHERE clientTransactionId = :id")
    suspend fun getTransactionById(id: String): LocalTransactionEntity?

    @Query("SELECT * FROM local_transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<LocalTransactionEntity>>

    @Query("SELECT * FROM local_transactions WHERE syncStatus = 'FAILED'")
    suspend fun getFailedTransactions(): List<LocalTransactionEntity>
}