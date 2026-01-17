package com.example.wallet.data.remoteDataSource
import com.example.wallet.data.remoteDataSource.dto.BalanceRequest
import com.example.wallet.data.remoteDataSource.dto.BalanceResponse
import com.example.wallet.data.remoteDataSource.dto.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WalletApiService {
    @POST("api/v1/customers/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("api/v1/accounts/balance")
    suspend fun getAccountBalance(
        @Body request: BalanceRequest
    ): Response<BalanceResponse>

}