package com.example.wallet.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wallet.presentation.navigate.Screen
import java.text.SimpleDateFormat
import java.util.*

data class LocalTransaction(
    val id: String,
    val accountTo: String,
    val amount: Double,
    val syncStatus: String, // QUEUED / SYNCING / SYNCED / FAILED
    val createdAt: Long,
    val lastError: String? = null
)

@Composable
fun LocalTransactionsScreen(navController: NavHostController) {

    // 🔹 Placeholder local transactions
    val transactions = remember {
        listOf(
            LocalTransaction("1", "ACC987654321", 100.0, "QUEUED", System.currentTimeMillis()),
            LocalTransaction("2", "ACC123456789", 50.0, "FAILED", System.currentTimeMillis() - 3600000, "Network error"),
            LocalTransaction("3", "ACC111222333", 200.0, "SYNCED", System.currentTimeMillis() - 7200000)
        ).sortedByDescending { it.createdAt } // newest first
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Local Transactions",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text("No local transactions found.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transactions) { txn ->
                    LocalTransactionItem(txn)
                    Divider()
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(Screen.Home.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}

@Composable
fun LocalTransactionItem(transaction: LocalTransaction) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        Text(
            text = dateFormat.format(Date(transaction.createdAt)),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "To: ${transaction.accountTo}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Amount: $${transaction.amount}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Status: ${transaction.syncStatus}",
            style = MaterialTheme.typography.bodyMedium,
            color = when (transaction.syncStatus) {
                "FAILED" -> MaterialTheme.colorScheme.error
                "SYNCED" -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
        transaction.lastError?.let {
            Text(
                text = "Error: $it",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Retry button for FAILED transactions
        if (transaction.syncStatus == "FAILED") {
            Button(onClick = { /* TODO: enqueue WorkManager retry */ }) {
                Text("Retry")
            }
        }
    }
}
