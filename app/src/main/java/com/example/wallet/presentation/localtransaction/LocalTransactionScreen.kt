import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wallet.data.localDataSource.entity.LocalTransactionEntity
import com.example.wallet.presentation.localtransaction.LocalTxnViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalTransactionsScreen(
    navController: NavHostController,
    viewModel: LocalTxnViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Local Transactions", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.transactions.isEmpty() -> {
                    // REQUIREMENT: Empty State
                    EmptyOutboxView()
                }
                else -> {
                    // REQUIREMENT: Display all locally queued / newest first
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.transactions,
                            key = { it.clientTransactionId }
                        ) { txn ->
                            LocalTransactionCard(
                                txn = txn,
                                onRetry = { viewModel.retryTransaction(txn.clientTransactionId) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocalTransactionCard(
    txn: LocalTransactionEntity,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("To: ${txn.accountTo}", fontWeight = FontWeight.ExtraBold)
                SyncStatusChip(txn.syncStatus)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "KES ${String.format("%,.2f", txn.amount)}",
                style = MaterialTheme.typography.titleMedium
            )
            val dateStr = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date(txn.createdAt))
            Text(text = dateStr, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            if (txn.syncStatus == "FAILED") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Error: ${txn.lastError ?: "Unknown connection error"}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )

                Button(
                    onClick = onRetry,
                    modifier = Modifier.align(Alignment.End).padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Retry & Sync Now")
                }
            }
        }
    }
}

@Composable
fun SyncStatusChip(status: String) {
    val color = when (status) {
        "SYNCED" -> Color(0xFF2E7D32)
        "FAILED" -> Color.Red
        "SYNCING" -> Color(0xFF1976D2)
        else -> Color.DarkGray // QUEUED
    }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmptyOutboxView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No pending transactions", color = Color.Gray)
    }
}