package com.yojo.app.ui.play.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yojo.app.ui.wallet.GiftStoreBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveRoomsScreen(navController: NavController) {
    var showGiftStore by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "🎙️ Live Voice Rooms Hub") },
                actions = {
                    IconButton(onClick = { showGiftStore = true }) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = "Gift Store", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    // Naya Voice Room create karne ka action
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Create Room")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Active Voice Rooms",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    onClick = { 
                        // Room join karne ka action
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "🔥 Late Night Gaming & Music Chill",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Host: Muhammad Ali • 6 Seats Occupied",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Gift Store Bottom Sheet Dialog
            if (showGiftStore) {
                GiftStoreBottomSheet(
                    onDismiss = { showGiftStore = false },
                    onSendGift = { giftName, price ->
                        // Gift send karne ki logic yahan execute hogi
                        showGiftStore = false
                    }
                )
            }
        }
    }
}
