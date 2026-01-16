package com.example.wallet.Domain.Repository

interface CustomerRepository {
    suspend fun login(customerId: String, pin: String): Result<Unit>
}
