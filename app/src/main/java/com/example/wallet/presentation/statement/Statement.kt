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
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class Transaction(
    val id: String,
    val date: Long,
    val description: String,
    val amount: Double,
    val direction: String
)

@Composable
fun StatementScreen(navController: NavHostController) {


    val transactions = remember {
        listOf(
            Transaction("1", System.currentTimeMillis(), "Salary", 1200.0, "IN"),
            Transaction("2", System.currentTimeMillis() - 86400000, "Rent", 500.0, "OUT"),
            Transaction("3", System.currentTimeMillis() - 172800000, "Coffee Shop", 8.5, "OUT")
        )
    }

    val totalAmount = transactions.sumOf { if (it.direction == "IN") it.amount else -it.amount }
    val currencyFormat = NumberFormat.getCurrencyInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "Statement (Last 100)",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isEmpty()) {
            Text("No transactions found.", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(transactions) { txn ->
                    TransactionItem(txn)
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Total: ${currencyFormat.format(totalAmount)}",
                style = MaterialTheme.typography.bodyLarge
            )
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
fun TransactionItem(transaction: Transaction) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
    ) {
        Text(
            text = dateFormat.format(Date(transaction.date)),
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "${transaction.description} (${transaction.direction})",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "$${transaction.amount}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
