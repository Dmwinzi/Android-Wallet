package com.example.wallet.data.remoteDataSource.dto

data class TransactionResponse(
    val id: Long,
    val transactionId: String,
    val customerId: String,
    val accountNo: String,
    val amount: Double,
    val balance: Double,
    val transactionType: String,
    val debitOrCredit: String
)

data class StatementRequest(val customerId: String)