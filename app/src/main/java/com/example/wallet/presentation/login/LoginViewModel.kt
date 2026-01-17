package com.example.wallet.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.domain.usecases.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onCustomerIdChange(newId: String) {
        _uiState.update { it.copy(customerId = newId, error = null) }
    }

    fun onPinChange(newPin: String) {
        _uiState.update { it.copy(pin = newPin, error = null) }
    }

    fun login() {
        val currentState = _uiState.value
        val customerId = currentState.customerId
        val pin = currentState.pin

        if (customerId.isBlank() || pin.isBlank()) {
            _uiState.update { it.copy(error = "Customer ID and PIN cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = loginUseCase(customerId, pin)

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = exception.localizedMessage ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(loginSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
