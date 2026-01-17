package com.example.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.wallet.data.datastore.UserPreferenceManager
import kotlinx.coroutines.flow.first
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.wallet.presentation.navigate.Screen
import com.example.wallet.presentation.navigate.WalletNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userPrefs = UserPreferenceManager(this)

        setContent {
               WalletApp(userPrefs)
        }
    }
}


@Composable
fun WalletApp(userPrefs: UserPreferenceManager) {
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf("login") }

    LaunchedEffect(Unit) {
        startDestination = if (userPrefs.isLoggedInFlow.first()) {
            Screen.Home.route
        } else {
            Screen.Login.route
        }
    }

    if (startDestination.isNotEmpty()) {
        WalletNavHost(navController = navController, startDestination = startDestination)
    }
}
