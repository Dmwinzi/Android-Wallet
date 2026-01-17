package com.example.wallet.domain.repository

import com.example.wallet.data.remoteDataSource.dto.BalanceResponse
import com.example.wallet.domain.models.Customer

interface CustomerRepository {
    suspend fun login(customerId: String, pin: String): Result<Customer>
    suspend fun fetchBalance(customerId: String): Result<BalanceResponse>
}
