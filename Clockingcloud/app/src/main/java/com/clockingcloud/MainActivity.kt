package com.clockingcloud

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.clockingcloud.navigation.AppNavHost
import com.clockingcloud.ui.theme.ClockingcloudTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("APP_CRASH", "🔥 Error no controlado en hilo ${thread.name}", throwable)
        }

        enableEdgeToEdge()
        setContent {
            ClockingcloudTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    AppNavHost(navController = navController, paddingValues = innerPadding)
                }
            }
        }
    }
}
