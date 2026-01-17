package com.example.wallet.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.data.datastore.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPrefs: UserPreferenceManager
) : ViewModel() {

    val userProfileState = combine(
        userPrefs.customerNameFlow,
        userPrefs.customerIdFlow,
        userPrefs.accountNumberFlow,
        userPrefs.emailFlow
    ) { name, id, account, email ->
        ProfileUiState(
            name = name ?: "N/A",
            id = id ?: "N/A",
            account = account ?: "N/A",
            email = email ?: "N/A"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState()
    )
}

data class ProfileUiState(
    val name: String = "",
    val id: String = "",
    val account: String = "",
    val email: String = ""
)