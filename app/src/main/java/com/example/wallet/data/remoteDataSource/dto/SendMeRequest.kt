package com.example.wallet.data.remoteDataSource.dto

import com.google.gson.annotations.SerializedName

data class SendMoneyRequest(
    val customerId: String,
    val accountFrom: String,
    val accountTo: String,
    val amount: Double
)

data class SendMoneyResponse(
    @SerializedName("response_status")
    val responseStatus: Boolean,
    @SerializedName("response_message")
    val responseMessage: String
)