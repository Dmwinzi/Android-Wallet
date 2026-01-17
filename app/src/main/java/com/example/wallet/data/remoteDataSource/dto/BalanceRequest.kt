package com.example.wallet.data.remoteDataSource.dto

data class BalanceRequest(
    val customerId: String
)

data class BalanceResponse(
    val balance: Double,
    val accountNo: String,
    val status: String
)