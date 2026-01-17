package com.example.wallet.presentation.home

data class HomeUiState(
    val customerName: String = "",
    val accountNo: String = "",
    val customerId: String = "",
    val balance: String = "0.00",
    val isBalanceVisible: Boolean = false,
    val isLoading: Boolean = false
)