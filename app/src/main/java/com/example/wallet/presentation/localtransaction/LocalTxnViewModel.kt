package com.example.wallet.presentation.localtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import com.example.wallet.domain.usecases.GetLocalTransactionsUseCase
import com.example.wallet.domain.usecases.RetryLocalTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalTxnViewModel @Inject constructor(
    private val getLocalTransactionsUseCase: GetLocalTransactionsUseCase,
    private val retryLocalTransactionUseCase: RetryLocalTransactionUseCase
) : ViewModel() {
    val uiState: StateFlow<LocalTxnUiState> = getLocalTransactionsUseCase()
        .map { transactions ->
            LocalTxnUiState(transactions = transactions, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = LocalTxnUiState(isLoading = true) // Start with loading
        )

    fun retryTransaction(id: String) {
        viewModelScope.launch {
            retryLocalTransactionUseCase(id)
        }
    }
}