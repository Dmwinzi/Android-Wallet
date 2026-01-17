package com.example.wallet.presentation.sendmoney

sealed class TransactionUiState {
    object Idle : TransactionUiState()
    object Loading : TransactionUiState()
    object Success : TransactionUiState()
    data class Error(val message: String) : TransactionUiState()
}