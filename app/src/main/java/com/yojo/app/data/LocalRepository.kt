package com.yojo.app.data

/**
 * Local demo state only. Replace with authenticated repository implementations
 * when a separate YOJO backend/Firebase project is configured.
 */
class LocalRepository {
    var displayName: String = "YOJO Creator"
    var bio: String = "Creating and sharing moments."
    var darkMode: Boolean = false
    var privateAccount: Boolean = false
    var notificationsEnabled: Boolean = true
    val drafts = mutableListOf<String>()
}
