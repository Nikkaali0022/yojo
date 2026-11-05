package com.yojo.app.data.model

data class LiveVoiceRoom(
    val roomId: String = "",
    val roomTitle: String = "",
    val hostId: String = "",
    val hostName: String = "",
    val isPrivate: Boolean = false,
    val roomPassword: String = "",
    val currentListeners: Int = 0,
    val seats: Map<Int, VoiceSeat> = emptyMap() // Seat index (0-7) se VoiceSeat ki mapping
)

data class VoiceSeat(
    val seatIndex: Int = 0,
    val userId: String = "",
    val userName: String = "",
    val isMuted: Boolean = false,
    val isOccupied: Boolean = false
)
