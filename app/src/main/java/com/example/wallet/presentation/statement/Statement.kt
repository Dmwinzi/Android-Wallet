import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wallet.data.remoteDataSource.dto.TransactionResponse
import com.example.wallet.presentation.statement.StatementUiState
import com.example.wallet.presentation.statement.StatementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatementScreen(
    navController: NavHostController,
    viewModel: StatementViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statement", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick ={
                        navController.popBackStack()
                    } ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val state = uiState) {
                is StatementUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is StatementUiState.Error -> {
                    ErrorMessage(state.message, onRetry = { viewModel.loadStatement() })
                }
                is StatementUiState.Success -> {
                    if (state.transactions.isEmpty()) {
                        // REQUIREMENT: Empty State
                        EmptyStateView()
                    } else {
                        // REQUIREMENT: Scrollable List & Bottom Summary
                        StatementListWithSummary(state.transactions)
                    }
                }
            }
        }
    }
}

@Composable
fun StatementListWithSummary(transactions: List<TransactionResponse>) {
    val totalMovement = transactions.sumOf {
        if (it.debitOrCredit.equals("CREDIT", true)) it.amount else -it.amount
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(transactions) { txn ->
                TransactionRow(txn)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
        }

        // REQUIREMENT: Total amount summary at bottom
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(24.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Net Movement", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "KES ${String.format("%,.2f", totalMovement)}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (totalMovement >= 0) Color(0xFF2E7D32) else Color.Red
                )
            }
        }
    }
}

@Composable
fun TransactionRow(txn: TransactionResponse) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // Formatting: Description (Type) and Subtitle (ID)
            Text(txn.transactionType, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
            Text("Ref: ${txn.transactionId}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Text("Acc: ${txn.accountNo}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        val isCredit = txn.debitOrCredit.equals("CREDIT", true)
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (isCredit) "+" else "-"} KES ${String.format("%,.2f", txn.amount)}",
                color = if (isCredit) Color(0xFF2E7D32) else Color.Red,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            Text("Bal: ${String.format("%,.2f", txn.balance)}", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No synced transactions found", color = Color.Gray)
    }
}

@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, textAlign = TextAlign.Center, color = Color.Red)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 16.dp)) {
            Text("Retry")
        }
    }
}