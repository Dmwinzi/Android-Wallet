package com.example.wallet.domain.usecases

import com.example.wallet.domain.repository.CustomerRepository


class RetryLocalTransactionUseCase(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(transactionId: String) {
        repository.retryLocalTransaction(transactionId)
    }
}