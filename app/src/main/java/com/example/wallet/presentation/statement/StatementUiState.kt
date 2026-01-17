package com.example.wallet.presentation.statement

import com.example.wallet.data.remoteDataSource.dto.TransactionResponse

sealed interface StatementUiState {
    object Loading : StatementUiState

    data class Success(
        val transactions: List<TransactionResponse>
    ) : StatementUiState

    data class Error(
        val message: String
    ) : StatementUiState
}