package com.yojo.app.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Upload : Screen("upload")
    object Play : Screen("play")
    object Profile : Screen("profile")
}
