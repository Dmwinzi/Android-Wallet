package com.example.wallet.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.Domain.Usecases.LoginUseCase
import com.example.wallet.Presentation.Login.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onCustomerIdChange(newId: String) {
        _uiState.value = _uiState.value.copy(customerId = newId)
    }

    fun onPinChange(newPin: String) {
        _uiState.value = _uiState.value.copy(pin = newPin)
    }

    fun login() {
        val customerId = _uiState.value.customerId
        val pin = _uiState.value.pin

        if (customerId.isBlank() || pin.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Customer ID and PIN cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = loginUseCase(customerId, pin)

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginSuccess = true
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
                )
            }
        }
    }

    fun resetLoginSuccess() {
        _uiState.value = _uiState.value.copy(loginSuccess = false)
    }

}
