package com.clockingcloud.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clockingcloud.ui.screens.HomeScreen
import com.clockingcloud.ui.screens.LoginScreen
import com.clockingcloud.utils.SessionManager

@Composable
fun AppNavHost(navController: NavHostController, paddingValues: PaddingValues) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }

    var isLoggedIn by remember { mutableStateOf(session.getAccessToken() != null) }

    LaunchedEffect(Unit) {
        snapshotFlow { session.getAccessToken() }
            .collect { token ->
                isLoggedIn = token != null
            }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login",
        modifier = Modifier.padding(paddingValues)
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }
    }
}