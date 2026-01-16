package com.example.wallet.data.repository

import com.example.wallet.Data.Datastore.UserPreferenceManager
import com.example.wallet.Data.RemoteDataSource.WalletApiService
import com.example.wallet.Domain.Repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val api: WalletApiService,
    private val userPrefs: UserPreferenceManager
) : CustomerRepository {

    override suspend fun login(customerId: String, pin: String): Result<Unit> {
        return try {
            val response = api.login(mapOf("customerId" to customerId, "pin" to pin))

            if (response.isSuccessful) {
                userPrefs.setLoggedIn(true)

                // TODO: store any extra profile info after inspecting API response
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
