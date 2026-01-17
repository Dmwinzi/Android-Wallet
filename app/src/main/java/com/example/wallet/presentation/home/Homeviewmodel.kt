package com.example.wallet.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.data.datastore.UserPreferenceManager
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
    private val userPrefs: UserPreferenceManager
) : ViewModel() {

    private val _isBalanceVisible = MutableStateFlow(false)
    private val _balance = MutableStateFlow("0.00") // Will be updated by API later

    val uiState: StateFlow<HomeUiState> = combine(
        userPrefs.customerNameFlow,
        userPrefs.accountNumberFlow,
        _isBalanceVisible,
        _balance
    ) { name, accNo, isVisible, balanceAmt ->
        HomeUiState(
            customerName = name.ifBlank { "Customer" },
            accountNo = accNo.ifBlank { "N/A" },
            balance = balanceAmt,
            isBalanceVisible = isVisible
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun toggleBalanceVisibility() {
        _isBalanceVisible.value = !_isBalanceVisible.value
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            userPrefs.logout()
            onSuccess()
        }
    }
}