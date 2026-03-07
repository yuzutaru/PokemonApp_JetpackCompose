package com.yustar.pokeapp_jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yustar.auth.presentation.screen.LoginScreen
import com.yustar.auth.session.SessionManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val loggedUser by sessionManager.loggedUser
                .collectAsState(initial = null)

            val startDestination = if (loggedUser != null) "home" else "login"

            PokeAppNavHost(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}

@Composable
fun PokeAppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("home") {
            // Placeholder for HomeScreen
        }
    }
}
