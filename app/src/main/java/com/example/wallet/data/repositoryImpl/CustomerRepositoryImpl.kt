package com.example.wallet.data.repository

import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.data.remoteDataSource.WalletApiService
import com.example.wallet.data.remoteDataSource.dto.toDomain
import com.example.wallet.domain.models.Customer
import com.example.wallet.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val api: WalletApiService,
    private val userPrefs: UserPreferenceManager
) : CustomerRepository {

    override suspend fun login(customerId: String, pin: String): Result<Customer> {
        return try {
            val response = api.login(mapOf("customerId" to customerId, "pin" to pin))

            if (response.isSuccessful) {
                val loginDto = response.body()

                if (loginDto != null) {
                    val customer = loginDto.toDomain()

                    userPrefs.saveLoginDetails(
                        name = customer.name,
                        id = customer.id,
                        email = customer.email,
                        accountNo = customer.accountNo
                    )
                    Result.success(customer)
                } else {
                    Result.failure(Exception("Server returned an empty profile"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Login failed"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}