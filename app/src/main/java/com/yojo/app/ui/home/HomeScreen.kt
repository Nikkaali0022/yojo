package com.yojo.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "YOJO Feed (@9t9vlogz)", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(3) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // User Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = androidx.compose.foundation.shape.CircleShape,
                                color = MaterialTheme.colorScheme.primary
                            ) {}
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Muhammad Ali Khan", fontWeight = FontWeight.Bold)
                                Text(text = "2 hours ago", style = androidx.compose.material3.MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Post Caption
                        Text(text = "Exploring new features in YOJO platform! Short video & gaming integration coming alive. 🚀 #YOJO #Android #Vlogz")

                        Spacer(modifier = Modifier.height(12.dp))

                        // Video / Media Placeholder Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .background(Color.DarkGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "▶️ Short Video / Photo Feed Item", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action Buttons (Like, Comment, Share)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            IconButton(onClick = { /* Like action */ }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color.Red)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "124")
                                }
                            }
                            IconButton(onClick = { /* Comment action */ }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ModeComment, contentDescription = "Comments")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "18")
                                }
                            }
                            IconButton(onClick = { /* Share action */ }) {
                                Icon(Icons.Default.Share, contentDescription = "Share")
                            }
                        }
                    }
                }
            }
        }
    }
}
