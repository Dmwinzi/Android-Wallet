package com.example.wallet.Presentation.Login

data class LoginUiState(
    val customerId: String = "",
    val pin: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val loginSuccess: Boolean = false
)

