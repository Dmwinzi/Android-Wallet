package com.example.wallet.domain.usecases

import com.example.wallet.data.remoteDataSource.dto.BalanceResponse
import com.example.wallet.domain.repository.CustomerRepository
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(customerId: String): Result<BalanceResponse> {
        return repository.fetchBalance(customerId)
    }
}