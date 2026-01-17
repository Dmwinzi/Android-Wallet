package com.example.wallet.presentation.sendmoney
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wallet.presentation.navigate.Screen
import kotlinx.coroutines.delay
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(
    navController: NavHostController,
    viewModel: SendMoneyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var accountTo by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var showQueuedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is TransactionUiState.Success -> {
                showQueuedDialog = true
            }
            is TransactionUiState.Error -> {
                validationError = (uiState as TransactionUiState.Error).message
            }
            else -> {}
        }
    }

    if (showQueuedDialog) {
        AlertDialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            confirmButton = {
                Button(onClick = {
                    showQueuedDialog = false
                    viewModel.resetState()
                    navController.popBackStack()
                }) {
                    Text("Return to Dashboard")
                }
            },
            title = { Text("Transaction Queued") },
            text = { Text("Transfer of KES $amount to $accountTo saved locally and will sync when online.") },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Send Money", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon Header
            Surface(
                modifier = Modifier.size(80.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(36.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = accountTo,
                onValueChange = { accountTo = it; validationError = null },
                label = { Text("Recipient Account") },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is TransactionUiState.Loading // Disable during save
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it; validationError = null },
                label = { Text("Amount") },
                prefix = { Text("KES ") },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is TransactionUiState.Loading
            )

            if (validationError != null) {
                Text(
                    text = validationError!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull()
                    if (accountTo.isBlank()) validationError = "Recipient required"
                    else if (amt == null || amt <= 0) validationError = "Invalid amount"
                    else viewModel.sendMoney(accountTo, amt)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = uiState !is TransactionUiState.Loading,
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState is TransactionUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Confirm Transfer")
                }
            }
        }
    }
}

