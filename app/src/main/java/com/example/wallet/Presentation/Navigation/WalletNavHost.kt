package com.example.wallet.Presentation.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wallet.Presentation.HomeScreen
import com.example.wallet.Presentation.LocalTransactionsScreen
import com.example.wallet.Presentation.ProfileScreen
import com.example.wallet.Presentation.SendMoney
import com.example.wallet.Presentation.StatementScreen
import com.example.wallet.presentation.login.LoginScreen


@Composable
fun WalletNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        }

        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.SendMoney.route) { SendMoney(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Statement.route) { StatementScreen(navController) }
        composable(Screen.LocalTransactions.route) { LocalTransactionsScreen(navController) }
    }
}

