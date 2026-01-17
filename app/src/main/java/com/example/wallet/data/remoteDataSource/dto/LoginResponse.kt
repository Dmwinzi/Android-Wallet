package com.example.wallet.data.remoteDataSource.dto

import com.example.wallet.domain.models.Customer

data class LoginResponse(
    val customerName: String,
    val customerId: String,
    val email: String,
    val customerAccount: String
)

fun LoginResponse.toDomain(): Customer {
    return Customer(
        name = customerName,
        id = customerId,
        email = email,
        accountNo = customerAccount

    )
}