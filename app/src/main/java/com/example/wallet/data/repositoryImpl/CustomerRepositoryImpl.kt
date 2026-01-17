package com.example.wallet.data.repository

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.localDataSource.dao.TransactionDao
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import com.example.wallet.data.remoteDataSource.WalletApiService
import com.example.wallet.data.remoteDataSource.dto.BalanceRequest
import com.example.wallet.data.remoteDataSource.dto.BalanceResponse
import com.example.wallet.data.remoteDataSource.dto.StatementRequest
import com.example.wallet.data.remoteDataSource.dto.TransactionResponse
import com.example.wallet.data.remoteDataSource.dto.toDomain
import com.example.wallet.data.worker.SyncTransactionWorker
import com.example.wallet.domain.models.Customer
import com.example.wallet.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val api: WalletApiService,
    private val userPrefs: UserPreferenceManager,
    private val transactionDao: TransactionDao, // Room DAO
    private val workManager: WorkManager
) : CustomerRepository {

    override suspend fun login(customerId: String, pin: String): Result<Customer> {
        return try {
            val response = api.login(mapOf("customerId" to customerId, "pin" to pin))

            if (response.isSuccessful) {
                val loginDto = response.body()

                if (loginDto != null) {
                    val customer = loginDto.toDomain()

                    userPrefs.saveLoginDetails(
                        name = customer.name,
                        id = customer.id,
                        email = customer.email,
                        accountNo = customer.accountNo
                    )
                    Result.success(customer)
                } else {
                    Result.failure(Exception("Server returned an empty profile"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Login failed"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchBalance(customerId: String): Result<BalanceResponse> {
        return try {
            val response = api.getAccountBalance(BalanceRequest(customerId))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun fetchStatement(customerId: String): Result<List<TransactionResponse>> {
        return try {
            val response = api.getLast100Transactions(StatementRequest(customerId))
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLocalTransactions(): Flow<List<LocalTransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun retryLocalTransaction(clientTransactionId: String) {
        val txn = transactionDao.getTransactionById(clientTransactionId)
        txn?.let {
            transactionDao.updateTransaction(it.copy(
                syncStatus = "QUEUED",
                lastError = null
            ))
        }

        val syncRequest = OneTimeWorkRequestBuilder<SyncTransactionWorker>()
            .setInputData(workDataOf("transactionId" to clientTransactionId))
            .build()

        workManager.enqueueUniqueWork(
            "manual_sync_$clientTransactionId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

}