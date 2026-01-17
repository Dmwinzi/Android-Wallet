package com.example.wallet.presentation.navigate

import LocalTransactionsScreen
import ProfileScreen
import StatementScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.wallet.presentation.login.HomeScreen
import com.example.wallet.presentation.login.LoginScreen
import com.example.wallet.presentation.sendmoney.SendMoneyScreen


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
        composable(Screen.SendMoney.route) { SendMoneyScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Statement.route) { StatementScreen(navController) }
        composable(Screen.LocalTransactions.route) { LocalTransactionsScreen(navController) }
    }
}

