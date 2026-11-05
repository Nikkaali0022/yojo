package com.yojo.app.ui.play

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayScreen(onNavigateToLudo: () -> Unit, onNavigateToRooms: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎮 YOJO Play & Games", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Choose Your Fun",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateToLudo,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("🎲 Play Ludo Star", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateToRooms,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("🎙️ Live Voice Rooms", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
