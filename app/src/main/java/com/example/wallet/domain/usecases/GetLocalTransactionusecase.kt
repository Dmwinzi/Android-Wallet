package com.example.wallet.domain.usecases

import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import com.example.wallet.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalTransactionsUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    operator fun invoke(): Flow<List<LocalTransactionEntity>> {
        return repository.getLocalTransactions()
    }
}