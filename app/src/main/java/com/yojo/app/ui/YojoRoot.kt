package com.yojo.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YojoRoot() {
    var selectedTab by remember { mutableStateOf(0) }
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                    label = { Text("Explore") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AddBox, contentDescription = "Upload") },
                    label = { Text("Upload") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Gamepad, contentDescription = "Play") },
                    label = { Text("Play") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> com.yojo.app.ui.home.HomeScreen(navController = navController)
                1 -> com.yojo.app.ui.explore.ExploreScreen(navController = navController)
                2 -> com.yojo.app.ui.upload.UploadScreen(navController = navController)
                3 -> com.yojo.app.ui.play.PlayScreen(
                    onNavigateToLudo = {},
                    onNavigateToRooms = {}
                )
                4 -> com.yojo.app.ui.profile.ProfileScreen(navController = navController)
            }
        }
    }
}
