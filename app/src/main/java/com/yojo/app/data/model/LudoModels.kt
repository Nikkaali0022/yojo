package com.yojo.app.data.model

data class LudoRoom(
    val roomId: String = "",
    val hostId: String = "",
    val status: String = "waiting", // waiting, playing, finished
    val players: Map<String, LudoPlayer> = emptyMap(),
    val currentTurnPlayerId: String = "",
    val diceRoll: Int = 1,
    val lastRolledBy: String = ""
)

data class LudoPlayer(
    val uid: String = "",
    val name: String = "",
    val color: String = "", // red, green, yellow, blue
    val tokens: List<Int> = listOf(0, 0, 0, 0), // Token positions (0 means yard)
    val isReady: Boolean = false
)

data class LudoChatMessage(
    val messageId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
