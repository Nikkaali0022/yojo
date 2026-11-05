package com.yojo.app.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.yojo.app.ui.home.HomeScreen
import com.yojo.app.ui.explore.ExploreScreen
import com.yojo.app.ui.upload.UploadScreen
import com.yojo.app.ui.play.PlayScreen
import com.yojo.app.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Explore", "Upload", "Play", "Profile")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Explore,
        Icons.Default.AddBox,
        Icons.Default.Gamepad,
        Icons.Default.Person
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> HomeScreen()
                1 -> ExploreScreen()
                2 -> UploadScreen()
                3 -> PlayScreen(
                    onNavigateToLudo = {},
                    onNavigateToRooms = {}
                )
                4 -> ProfileScreen()
            }
        }
    }
}
