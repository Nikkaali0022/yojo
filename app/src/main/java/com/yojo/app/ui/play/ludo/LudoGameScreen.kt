package com.yojo.app.ui.play.ludo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LudoGameScreen(navController: NavController, roomId: String) {
    var showChatSheet by remember { mutableStateOf(false) }
    var chatMessageInput by remember { mutableStateOf("") }
    
    // Dummy messages list for testing real-time chat preview inside Ludo match
    val chatMessages = remember { mutableStateListOf(
        "Welcome to YOJO Ludo!",
        "Good luck, have fun!",
        "Hurry up and roll the dice!"
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "🎲 YOJO Ludo Match (Room: $roomId)") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            // Floating Chat Button inside Ludo Match
            FloatingActionButton(
                onClick = { showChatSheet = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Chat, contentDescription = "In-Game Chat")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Ludo Board & Real-time Turn Engine",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tap the chat icon on the bottom right to chat with other players while playing!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // In-Game Chat Bottom Sheet Overlay
            if (showChatSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showChatSheet = false }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "💬 Match Live Chat",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Messages List
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(chatMessages) { msg ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = msg,
                                        modifier = Modifier.padding(12.dp),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Message Input Field & Send Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = chatMessageInput,
                                onValueChange = { chatMessageInput = it },
                                placeholder = { Text("Type a message...") },
                                modifier = Modifier.weight(1f),
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (chatMessageInput.isNotBlank()) {
                                        chatMessages.add(chatMessageInput)
                                        chatMessageInput = ""
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send Message",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
