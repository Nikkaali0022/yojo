package com.yojo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yojo.app.ui.auth.AuthScreen
import com.yojo.app.ui.main.MainScreen
import com.yojo.app.ui.theme.YOJOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YOJOTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    YojoNavGraph()
                }
            }
        }
    }
}

@Composable
fun YojoNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "auth") {
        // Login & Signup Screen
        composable("auth") {
            AuthScreen(navController = navController)
        }
        // Main App with Bottom Navigation (Home, Explore, Upload, Play, Profile)
        composable("main_app") {
            MainScreen()
        }
    }
}
