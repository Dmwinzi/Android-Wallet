package com.example.wallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.wallet.Data.Datastore.UserPreferenceManager
import com.example.wallet.Presentation.Navigation.Screen
import com.example.wallet.Presentation.Navigation.WalletNavHost
import com.example.wallet.ui.theme.WalletTheme
import kotlinx.coroutines.flow.first
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf


class MainActivity : ComponentActivity() {

    private lateinit var userPrefs: UserPreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        userPrefs = UserPreferenceManager(this)

        setContent {
            WalletTheme {
               WalletApp(userPrefs)
            }

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
