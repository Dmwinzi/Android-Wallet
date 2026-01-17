package com.example.wallet.domain.repository

import com.example.wallet.domain.models.Customer

interface CustomerRepository {
    suspend fun login(customerId: String, pin: String): Result<Customer>
}
