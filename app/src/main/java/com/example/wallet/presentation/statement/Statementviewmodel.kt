package com.example.wallet.presentation.statement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wallet.domain.usecases.GetStatementUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatementViewModel @Inject constructor(
    private val getStatementUseCase: GetStatementUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatementUiState>(StatementUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadStatement()
    }

    fun loadStatement() {
        viewModelScope.launch {
            _uiState.value = StatementUiState.Loading
            getStatementUseCase().onSuccess { transactions ->
                _uiState.value = StatementUiState.Success(transactions)
            }.onFailure { error ->
                _uiState.value = StatementUiState.Error(error.localizedMessage ?: "Unknown Error")
            }
        }
    }
}