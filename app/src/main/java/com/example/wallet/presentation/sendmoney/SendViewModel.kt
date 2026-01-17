package com.example.wallet.presentation.sendmoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.localDataSource.dao.TransactionDao
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import com.example.wallet.data.worker.SyncTransactionWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.work.workDataOf
import androidx.work.BackoffPolicy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
@HiltViewModel
class SendMoneyViewModel @Inject constructor(
    private val dao: TransactionDao,
    private val userPrefs: UserPreferenceManager,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionUiState>(TransactionUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun sendMoney(accountTo: String, amount: Double) {
        viewModelScope.launch {
            _uiState.value = TransactionUiState.Loading

            try {
                val accountFrom = userPrefs.accountNumberFlow.first()

                val transaction = LocalTransactionEntity(
                    accountFrom = accountFrom,
                    accountTo = accountTo,
                    amount = amount
                )
                dao.insertTransaction(transaction)

                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                val syncRequest = OneTimeWorkRequestBuilder<SyncTransactionWorker>()
                    .setConstraints(constraints)
                    .setInputData(workDataOf("transactionId" to transaction.clientTransactionId))
                    .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                    .build()

                workManager.enqueueUniqueWork(
                    transaction.clientTransactionId,
                    ExistingWorkPolicy.REPLACE,
                    syncRequest
                )

                _uiState.value = TransactionUiState.Success

            } catch (e: Exception) {
                _uiState.value = TransactionUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun resetState() {
        _uiState.value = TransactionUiState.Idle
    }
}

