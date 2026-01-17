package com.example.wallet.domain.usecases

import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.remoteDataSource.dto.TransactionResponse
import com.example.wallet.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetStatementUseCase @Inject constructor(
    private val repository: CustomerRepository,
    private val userPrefs: UserPreferenceManager
) {
    suspend operator fun invoke(): Result<List<TransactionResponse>> {
        val customerId = userPrefs.customerIdFlow.firstOrNull()
            ?: return Result.failure(Exception("User not logged in"))

        return repository.fetchStatement(customerId)
    }
}