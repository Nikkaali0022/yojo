\package com.yojo.app.ui.home

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    // Sample Video URLs (Aap yahan apni Firebase ya API ki videos dalenge)
    val videoList = listOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"
    )

    val pagerState = rememberPagerState(pageCount = { videoList.size })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Vertical Pager for TikTok style scrolling
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ReelItem(videoUrl = videoList[page], isCurrentPage = pagerState.currentPage == page)
        }

        // Top Header Tabs (For You / Following / STEM)[cite: 4]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Following", color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
            Text(" | ", color = Color.White.copy(alpha = 0.5f))
            Text("For You", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

@Composable
fun ReelItem(videoUrl: String, isCurrentPage: Boolean) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var isLiked by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            prepare()
        }
    }

    DisposableEffect(isCurrentPage) {
        if (isCurrentPage) {
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
        onDispose {
            exoPlayer.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Video View
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay UI (Captions & Action Buttons like TikTok)[cite: 4]
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Left Side: Captions & Username[cite: 4]
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "@toqeer62636",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Cute baby moments playing with toys! 🌟 #yojo #vlogz",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Right Side: Action Buttons (Like, Comment, Share)[cite: 4]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                // Like Button[cite: 4]
                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text("14", color = Color.White, style = MaterialTheme.typography.bodySmall)[cite: 4]

                Spacer(modifier = Modifier.height(16.dp))

                // Comments Button[cite: 4]
                IconButton(onClick = { /* Open Comments */ }) {
                    Icon(
                        imageVector = Icons.Default.ModeComment,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Text("Add 1st", color = Color.White, style = MaterialTheme.typography.bodySmall)[cite: 4]

                Spacer(modifier = Modifier.height(16.dp))

                // Share Button[cite: 4]
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Text("Share", color = Color.White, style = MaterialTheme.typography.bodySmall)[cite: 4]
            }
        }
    }
}
