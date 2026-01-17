package com.example.wallet.presentation.sendmoney
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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


@Composable
fun SendMoneyScreen(
    navController: NavHostController
) {
    var accountTo by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showQueuedMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Send Money",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = accountTo,
            onValueChange = { accountTo = it },
            label = { Text("Account To") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                val amt = amount.toDoubleOrNull()

                error = when {
                    accountTo.isBlank() -> "Account To is required"
                    amt == null || amt <= 0 -> "Amount must be greater than 0"
                    else -> null
                }

                if (error == null) {
                    // 🔴 TODO (later):
                    // 1. Create local Room transaction with:
                    //    - UUID
                    //    - accountFrom
                    //    - accountTo
                    //    - amount
                    //    - createdAt
                    //    - syncStatus = QUEUED
                    //
                    // 2. Enqueue WorkManager with network constraint

                    showQueuedMessage = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }

    if (showQueuedMessage) {
        LaunchedEffect(Unit) {
            // Simple UX feedback
            delay(800)
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.SendMoney.route) { inclusive = true }
            }
        }

        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Queued for sync") },
            text = { Text("Transaction has been queued and will sync when online.") }
        )
    }
}

