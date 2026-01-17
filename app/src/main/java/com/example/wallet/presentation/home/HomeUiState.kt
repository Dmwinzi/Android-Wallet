package com.example.wallet.presentation.home

data class HomeUiState(
    val customerName: String = "Customer",
    val accountNo: String = "",
    val balance: String = "XXXX",
    val isBalanceVisible: Boolean = false
)