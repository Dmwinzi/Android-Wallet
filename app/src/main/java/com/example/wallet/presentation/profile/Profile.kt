package com.example.wallet.presentation.sendmoney
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wallet.presentation.navigate.Screen


@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    // TODO: Replace these placeholders with actual data from login
    val customerName = "John Doe"
    val customerId = "CUST1001"
    val customerAccount = "ACC123456789"
    val email = "john.doe@example.com"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Name: $customerName", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Customer ID: $customerId", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Account: $customerAccount", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Email: $email", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { navController.navigate(Screen.Home.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}
