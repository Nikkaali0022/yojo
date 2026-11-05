package com.yojo.app.ui.home

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
import androidx.compose.ui.platform.LocalContext
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
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            ReelItem(videoUrl = videoList[page], isCurrentPage = pagerState.currentPage == page)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Following",
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(
                text = " | ",
                color = Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = "For You",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ReelItem(videoUrl: String, isCurrentPage: Boolean) {
    val context = LocalContext.current
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
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                IconButton(onClick = { isLiked = !isLiked }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Text(
                    text = "14",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ModeComment,
                        contentDescription = "Comments",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Text(
                    text = "Add 1st",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Text(
                    text = "Share",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
