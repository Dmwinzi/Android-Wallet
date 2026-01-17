package com.example.wallet.presentation.navigate

sealed class Screen(val route : String){
    object Login : Screen("login")
    object Home : Screen("home")
    object SendMoney : Screen("send_money")
    object Profile : Screen("profile")
    object Statement : Screen("statement")
    object LocalTransactions : Screen("local_transactions")
}