package com.example.wallet.data.remoteDataSource
import com.example.wallet.data.remoteDataSource.dto.LoginResponse
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WalletApiService {
    @POST("api/v1/customers/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>
}