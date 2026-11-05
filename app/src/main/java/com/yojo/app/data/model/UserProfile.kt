package com.yojo.app.data.model

data class UserProfile(
    val uid: String = "",
    val username: String = "",
    val displayName: String = "",
    val bio: String = "YOJO Creator & Gamer",
    val profileImageUrl: String = "",
    val level: Int = 1,
    val xp: Long = 0,
    val coins: Long = 1000, // Shuruati virtual coins preview
    val diamonds: Long = 100, // Shuruati virtual diamonds
    val followersCount: Int = 0,
    val followingCount: Int = 0
)
