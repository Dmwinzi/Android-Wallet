package com.example.wallet.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.data.datastore.UserPreferenceManager
import com.example.wallet.domain.usecases.GetBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager,
    private val getBalanceUseCase: GetBalanceUseCase
) : ViewModel() {

    private val _isBalanceVisible = MutableStateFlow(false)
    private val _balance = MutableStateFlow("0.00")
    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<HomeUiState> = combine(
        combine(
            userPrefs.customerNameFlow,
            userPrefs.accountNumberFlow,
            userPrefs.customerIdFlow
        ) { name, acc, id -> Triple(name, acc, id) },
        _isBalanceVisible,
        _balance,
        _isLoading
    ) { prefs, isVisible, bal, loading ->
        val (name, accNo, custId) = prefs
        HomeUiState(
            customerName = name.ifBlank { "Customer" },
            accountNo = accNo.ifBlank { "N/A" },
            customerId = custId,
            balance = bal,
            isBalanceVisible = isVisible,
            isLoading = loading
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun toggleBalanceVisibility() {
        _isBalanceVisible.value = !_isBalanceVisible.value
    }

    fun fetchBalance() {
        val currentId = uiState.value.customerId

        if (currentId.isBlank()) {
            _errorMessage.value = "User session invalid. Please log in again."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = getBalanceUseCase(currentId)

            result.onSuccess { response ->
                _balance.value = "%,.2f".format(response.balance)
                _isBalanceVisible.value = true
                _isLoading.value = false
            }.onFailure { error ->
                _isLoading.value = false
                _errorMessage.value = error.message ?: "Could not connect to server"
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPrefs.logout()
            onSuccess()
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}