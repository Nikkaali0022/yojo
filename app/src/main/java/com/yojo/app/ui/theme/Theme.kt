package com.yojo.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Purple = Color(0xFF7457E8)
private val Light = lightColorScheme(
    primary = Purple, secondary = Color(0xFF625B71),
    background = Color(0xFFF8F7FC), surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEDEAF5)
)
private val Dark = darkColorScheme(
    primary = Color(0xFFC5B8FF), secondary = Color(0xFFCCC2DC),
    background = Color(0xFF12111A), surface = Color(0xFF1D1B27),
    surfaceVariant = Color(0xFF302D3B)
)

@Composable
fun YojoTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) Dark else Light, content = content)
}
