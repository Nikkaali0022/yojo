package com.yojo.app.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yojo.app.data.DemoContent
import com.yojo.app.data.LocalRepository
import com.yojo.app.data.YojoPost

private enum class Tab(val label: String) { Home("Home"), Explore("Explore"), Create("Create"), Inbox("Inbox"), Profile("Profile") }

@Composable
fun YojoRoot() {
    val repo = remember { LocalRepository() }
    var tab by remember { mutableStateOf(Tab.Home) }
    var route by remember { mutableStateOf<String?>(null) }
    var dark by remember { mutableStateOf(false) }
    var posts by remember { mutableStateOf(DemoContent.posts) }
    var search by remember { mutableStateOf("") }
    var draftCaption by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Video") }
    var showCommentsFor by remember { mutableStateOf<Int?>(null) }
    var toast by remember { mutableStateOf<String?>(null) }

    MaterialTheme(colorScheme = if (dark) darkColorScheme(primary = androidx.compose.ui.graphics.Color(0xFFC5B8FF)) else lightColorScheme(primary = androidx.compose.ui.graphics.Color(0xFF7457E8))) {
        Scaffold(
            topBar = {
                if (route == null && tab != Tab.Create) {
                    TopAppBarYojo(
                        title = if (tab == Tab.Home) "YOJO" else tab.label,
                        onAction = { route = "notifications" }
                    )
                }
            },
            bottomBar = {
                if (route == null) {
                    NavigationBar {
                        val icons = listOf(Icons.Default.Home, Icons.Default.Explore, Icons.Default.AddCircle, Icons.Default.ChatBubble, Icons.Default.Person)
                        Tab.values().forEachIndexed { i, item ->
                            NavigationBarItem(
                                selected = tab == item,
                                onClick = { tab = item },
                                icon = { Icon(icons[i], contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            },
            snackbarHost = { SnackbarHost(remember { SnackbarHostState() }) }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                AnimatedContent(
                    targetState = route ?: tab.name,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "yojo_screen_transition"
                ) { target ->
                    if (route != null) {
                        DetailRouter(
                            route = target, repo = repo, dark = dark,
                            onBack = { route = null },
                            onDark = { dark = it },
                            onToast = { toast = it }
                        )
                    } else {
                        when (tab) {
                            Tab.Home -> HomeFeed(posts, onLike = { id -> posts = posts.map { if (it.id == id) it.copy(liked = !it.liked, likes = it.likes + if (it.liked) -1 else 1) else it } }, onComment = { showCommentsFor = it }, onProfile = { route = "creator" })
                            Tab.Explore -> ExploreScreen(search, { search = it }, onOpen = { route = "hashtag" })
                            Tab.Create -> CreateScreen(selectedCategory, { selectedCategory = it }, draftCaption, { draftCaption = it }, onPublish = {
                                posts = listOf(YojoPost((posts.maxOfOrNull { p -> p.id } ?: 0) + 1, repo.displayName, "@you", draftCaption.ifBlank { "A new moment on YOJO." }, selectedCategory, 0, 0)) + posts
                                draftCaption = ""; tab = Tab.Home; toast = "Demo post added to your local feed"
                            }, onDraft = { repo.drafts.add(draftCaption); toast = "Draft saved on this device" })
                            Tab.Inbox -> InboxScreen(onOpen = { route = it })
                            Tab.Profile -> ProfileScreen(repo, onOpen = { route = it })
                        }
                    }
                }
                showCommentsFor?.let { id ->
                    AlertDialog(
                        onDismissRequest = { showCommentsFor = null },
                        title = { Text("Comments") },
                        text = { Text("Comments will be synced after backend connection. This demo shows the comments entry point.") },
                        confirmButton = { TextButton(onClick = { showCommentsFor = null }) { Text("Close") } }
                    )
                }
                toast?.let { message ->
                    Surface(Modifier.align(Alignment.BottomCenter).padding(16.dp), shape = RoundedCornerShape(14.dp), color = MaterialTheme.colorScheme.inverseSurface) {
                        Text(message, Modifier.padding(horizontal = 18.dp, vertical = 12.dp), color = MaterialTheme.colorScheme.inverseOnSurface)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarYojo(title: String, onAction: () -> Unit) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.ExtraBold) },
        actions = {
            IconButton(onClick = onAction) { Icon(Icons.Default.NotificationsNone, "Notifications") }
        }
    )
}

@Composable
private fun HomeFeed(posts: List<YojoPost>, onLike: (Int) -> Unit, onComment: (Int) -> Unit, onProfile: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(bottom = 20.dp)) {
        item {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text("Your world, your moments.", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Stories", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    DemoContent.stories.forEach { story ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.size(58.dp).clip(CircleShape).background(if (story.seen) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                                Text(story.initials, color = if (story.seen) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                            Text(story.name, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
        items(posts, key = { it.id }) { post ->
            PostCard(post, onLike = { onLike(post.id) }, onComment = { onComment(post.id) }, onProfile = onProfile)
        }
    }
}

@Composable
private fun PostCard(post: YojoPost, onLike: () -> Unit, onComment: () -> Unit, onProfile: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 7.dp), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Avatar(post.creator.take(1))
                Column(Modifier.weight(1f).padding(start = 10.dp)) {
                    Text(post.creator, fontWeight = FontWeight.Bold)
                    Text(post.handle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = {}) { Icon(Icons.Default.MoreHoriz, "More options") }
            }
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().height(205.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(if (post.category == "Video") Icons.Default.PlayCircle else Icons.Default.Image, null, Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
                    Text("${post.category} preview area", style = MaterialTheme.typography.labelMedium)
                    Text("Media will load from your connected storage", style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(post.caption)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onLike) { Icon(if (post.liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Like", tint = if (post.liked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface) }
                Text(post.likes.toString(), style = MaterialTheme.typography.labelMedium)
                IconButton(onClick = onComment) { Icon(Icons.Default.ChatBubbleOutline, "Comments") }
                Text(post.comments.toString(), style = MaterialTheme.typography.labelMedium)
                IconButton(onClick = {}) { Icon(Icons.Default.Send, "Share") }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.BookmarkBorder, "Save") }
            }
        }
    }
}

@Composable
private fun ExploreScreen(search: String, onSearch: (String) -> Unit, onOpen: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        OutlinedTextField(search, onSearch, Modifier.fillMaxWidth(), label = { Text("Search creators, topics and hashtags") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true)
        Spacer(Modifier.height(18.dp))
        Text("Discover", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        listOf("Trending videos", "Popular photos", "Creators to follow", "Music & sounds", "Hashtags", "Suggested topics").forEach { title ->
            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp).clickable { onOpen() }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary)
                    Text(title, Modifier.weight(1f).padding(start = 12.dp), fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ChevronRight, null)
                }
            }
        }
    }
}

@Composable
private fun CreateScreen(category: String, onCategory: (String) -> Unit, caption: String, onCaption: (String) -> Unit, onPublish: () -> Unit, onDraft: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp)) {
        Text("Create something", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Share videos, photos and stories with your audience.")
        Spacer(Modifier.height(18.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Video", "Photo", "Story").forEach { item ->
                FilterChip(selected = category == item, onClick = { onCategory(item) }, label = { Text(item) }, leadingIcon = { Icon(if (item == "Video") Icons.Default.Videocam else if (item == "Photo") Icons.Default.Image else Icons.Default.Circle, null) })
            }
        }
        Spacer(Modifier.height(16.dp))
        Card(Modifier.fillMaxWidth().height(180.dp), shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.AddPhotoAlternate, null, Modifier.size(42.dp), tint = MaterialTheme.colorScheme.primary)
                Text("Media picker integration point", fontWeight = FontWeight.SemiBold)
                Text("Choose media after device permissions are connected.", style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(caption, onCaption, Modifier.fillMaxWidth(), label = { Text("Write a caption") }, minLines = 3)
        Spacer(Modifier.height(10.dp))
        Text("Audience and safety", fontWeight = FontWeight.Bold)
        Text("Privacy, comments and audience controls will be applied before cloud publishing.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onPublish, Modifier.fillMaxWidth()) { Icon(Icons.Default.Publish, null); Spacer(Modifier.width(8.dp)); Text("Publish demo post") }
        OutlinedButton(onClick = onDraft, Modifier.fillMaxWidth()) { Text("Save draft locally") }
    }
}

@Composable
private fun InboxScreen(onOpen: (String) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Inbox", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Messages, requests and activity.")
        Spacer(Modifier.height(12.dp))
        listOf("Messages" to Icons.Default.Chat, "Message requests" to Icons.Default.MarkEmailUnread, "Activity" to Icons.Default.FavoriteBorder, "Notifications" to Icons.Default.Notifications).forEach { (label, icon) ->
            ListTileCard(label, icon) { onOpen(if (label == "Notifications") "notifications" else "messages") }
        }
        Text("Private messaging requires authenticated accounts and a secure backend.", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ProfileScreen(repo: LocalRepository, onOpen: (String) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Avatar(repo.displayName.take(1), 76)
            Column(Modifier.padding(start = 14.dp).weight(1f)) {
                Text(repo.displayName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("@yojo_creator", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            OutlinedButton(onClick = { onOpen("edit_profile") }) { Text("Edit") }
        }
        Text(repo.bio, Modifier.padding(vertical = 12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Stat("0", "Posts"); Stat("0", "Followers"); Stat("0", "Following")
        }
        Spacer(Modifier.height(18.dp))
        listOf("Creator Studio", "Saved posts", "Drafts (${repo.drafts.size})", "Settings & privacy", "Safety center", "Help & support", "Admin panel (demo)").forEach { label ->
            ListTileCard(label, Icons.Default.ChevronRight) {
                onOpen(when {
                    label.startsWith("Creator") -> "creator_studio"
                    label.startsWith("Saved") -> "saved"
                    label.startsWith("Drafts") -> "drafts"
                    label.startsWith("Settings") -> "settings"
                    label.startsWith("Safety") -> "safety"
                    label.startsWith("Help") -> "support"
                    else -> "admin"
                })
            }
        }
    }
}

@Composable
private fun DetailRouter(route: String, repo: LocalRepository, dark: Boolean, onBack: () -> Unit, onDark: (Boolean) -> Unit, onToast: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
            Text(route.replace("_", " ").replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        when (route) {
            "settings" -> SettingsScreen(repo, dark, onDark, onToast)
            "edit_profile" -> EditProfileScreen(repo, onToast)
            "notifications" -> InfoListScreen("Notifications", listOf("New follower alerts", "Likes and comments", "Creator updates", "Security notices"), "Notification delivery connects after backend setup.")
            "messages" -> InfoListScreen("Messages", listOf("Message requests", "Direct conversations", "Group conversations", "Media sharing"), "Real-time private messaging will be connected to the secure backend.")
            "creator_studio" -> InfoListScreen("Creator Studio", listOf("Content overview", "Audience insights", "Engagement analytics", "Post performance"), "Creator analytics require real account and content data.")
            "saved" -> InfoListScreen("Saved posts", listOf("Your saved videos", "Your saved photos", "Collections"), "Saved content will sync to your account after backend integration.")
            "drafts" -> InfoListScreen("Drafts", repo.drafts.map { it.ifBlank { "Untitled draft" } }.ifEmpty { listOf("No local drafts yet") }, "Drafts are currently stored only in this app session.")
            "safety" -> InfoListScreen("Safety Center", listOf("Privacy controls", "Blocked accounts", "Muted words", "Report content", "Community guidelines", "Account security"), "Safety controls must be enforced by backend rules before public launch.")
            "support" -> InfoListScreen("Help & Support", listOf("Help center", "Report a problem", "Community guidelines", "Contact support"), "Support ticket submission will be connected to the admin system.")
            "admin" -> InfoListScreen("Admin Panel", listOf("Dashboard", "User management", "Content moderation", "Reports & appeals", "Announcements", "Roles & permissions", "System health"), "Admin actions are intentionally demo-only until secure server-side roles are configured.")
            "creator", "hashtag" -> InfoListScreen(if (route == "creator") "Creator profile" else "Discover topic", listOf("Posts", "Videos", "Followers", "Follow"), "Discovery data will be populated by the backend.")
            else -> InfoListScreen("YOJO", listOf("Feature module"), "This module is prepared for backend integration.")
        }
    }
}

@Composable
private fun SettingsScreen(repo: LocalRepository, dark: Boolean, onDark: (Boolean) -> Unit, onToast: (String) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Appearance", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        SettingSwitch("Dark theme", dark, onDark)
        Text("More themes and accent customization can be added to the theme engine.")
        HorizontalDivider(Modifier.padding(vertical = 12.dp))
        Text("Privacy & account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        SettingSwitch("Private account (demo)", repo.privateAccount) { repo.privateAccount = it; onToast("Demo setting changed") }
        SettingSwitch("Notifications enabled (demo)", repo.notificationsEnabled) { repo.notificationsEnabled = it; onToast("Demo setting changed") }
        listOf("Blocked accounts", "Muted words", "Password & security", "Language", "Accessibility", "About YOJO").forEach { ListItem(headlineContent = { Text(it) }) }
    }
}

@Composable
private fun EditProfileScreen(repo: LocalRepository, onToast: (String) -> Unit) {
    var name by remember { mutableStateOf(repo.displayName) }
    var bio by remember { mutableStateOf(repo.bio) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Avatar(name.take(1), 72)
        OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth().padding(top = 16.dp), label = { Text("Display name") })
        OutlinedTextField(bio, { bio = it }, Modifier.fillMaxWidth().padding(top = 10.dp), label = { Text("Bio") }, minLines = 3)
        Button(onClick = { repo.displayName = name; repo.bio = bio; onToast("Profile saved locally") }, Modifier.fillMaxWidth().padding(top = 16.dp)) { Text("Save changes") }
    }
}

@Composable
private fun InfoListScreen(title: String, items: List<String>, note: String) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        items.forEach { ListItem(headlineContent = { Text(it) }, leadingContent = { Icon(Icons.Default.ChevronRight, null) }) }
        Spacer(Modifier.height(12.dp))
        Card { Text(note, Modifier.padding(16.dp), style = MaterialTheme.typography.bodyMedium) }
    }
}

@Composable
private fun ListTileCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick)) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Text(title, Modifier.weight(1f).padding(start = 12.dp))
            Icon(Icons.Default.ChevronRight, null)
        }
    }
}

@Composable
private fun Avatar(text: String, size: Int = 42) {
    Box(Modifier.size(size.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
        Text(text.ifBlank { "Y" }.take(1).uppercase(), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun SettingSwitch(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.weight(1f))
        Switch(checked, onChange)
    }
}
