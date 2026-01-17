package com.example.wallet.presentation.localtransaction

import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity

data class LocalTxnUiState(
    val transactions: List<LocalTransactionEntity> = emptyList(),
    val isLoading: Boolean = false
)