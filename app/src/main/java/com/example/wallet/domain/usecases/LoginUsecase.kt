package com.example.wallet.domain.usecases

import com.example.wallet.domain.models.Customer
import com.example.wallet.domain.repository.CustomerRepository

class LoginUseCase(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(customerId: String, pin: String): Result<Customer> {
        return repository.login(customerId, pin)
    }
}
