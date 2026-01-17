package com.example.wallet.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wallet.presentation.home.HomeViewModel
import com.example.wallet.presentation.navigate.Screen


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            Button(
                onClick = { viewModel.logout { navController.navigate(Screen.Login.route) } },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Logout")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Welcome back,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = uiState.customerName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Available Balance", style = MaterialTheme.typography.labelMedium)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = if (uiState.isBalanceVisible) "KES ${uiState.balance}" else "KES ••••••",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        IconButton(onClick = { viewModel.toggleBalanceVisibility() }) {
                            Icon(
                                imageVector = if (uiState.isBalanceVisible) Icons.Default.Done else Icons.Default.Lock,
                                contentDescription = null
                            )
                        }
                    }
                    Text("Acc: ${uiState.accountNo}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            val actions = listOf(
                HomeAction("Send", Icons.Default.Send, Screen.SendMoney.route),
                HomeAction("Statement", Icons.Default.List, Screen.Statement.route),
                HomeAction("Profile", Icons.Default.Person, Screen.Profile.route),
                HomeAction("History", Icons.Default.Refresh, Screen.LocalTransactions.route)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(actions) { action ->
                    ActionCard(action) { navController.navigate(action.route) }
                }
            }
        }
    }
}

@Composable
fun ActionCard(action: HomeAction, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(action.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(action.label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

data class HomeAction(val label: String, val icon: ImageVector, val route: String)
