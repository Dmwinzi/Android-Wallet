package com.example.wallet.Domain.Usecases

import com.example.wallet.Domain.Repository.CustomerRepository

class LoginUseCase(
    private val repository: CustomerRepository
) {
    suspend operator fun invoke(customerId: String, pin: String): Result<Unit> {
        return repository.login(customerId, pin)
    }
}
