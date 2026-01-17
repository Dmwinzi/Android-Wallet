package com.example.wallet.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.localDataSource.dao.TransactionDao
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import com.example.wallet.data.remoteDataSource.WalletApiService
import com.example.wallet.data.remoteDataSource.dto.SendMoneyRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SyncTransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val api: WalletApiService,
    private val dao: TransactionDao,
    private val userPrefs: UserPreferenceManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val transactionId = inputData.getString("transactionId") ?: return Result.failure()
        val transaction = dao.getTransactionById(transactionId) ?: return Result.failure()

        return try {
            val customerId = userPrefs.customerIdFlow.first()

            val response = api.sendMoney(
                SendMoneyRequest(
                    customerId = customerId,
                    accountFrom = transaction.accountFrom,
                    accountTo = transaction.accountTo,
                    amount = transaction.amount
                )
            )

            if (response.isSuccessful) {
                val body = response.body()

                if (body?.responseStatus == true) {
                    dao.updateTransaction(transaction.copy(syncStatus = "SYNCED", lastError = null))
                    Result.success()
                } else {
                    val errorMsg = body?.responseMessage ?: "Server declined transaction"
                    handleRetry(transaction, errorMsg)
                }
            } else {
                handleRetry(transaction, "Network Error: ${response.code()}")
            }

        } catch (e: Exception) {
            handleRetry(transaction, e.localizedMessage)
        }
    }

    private suspend fun handleRetry(tx: LocalTransactionEntity, error: String?): Result {
        val nextAttempt = tx.attemptCount + 1

        return if (nextAttempt >= 3) {
            dao.updateTransaction(tx.copy(syncStatus = "FAILED", lastError = error))
            Result.failure()
        } else {

            dao.updateTransaction(tx.copy(attemptCount = nextAttempt, lastError = error))
            Result.retry()
        }
    }
}