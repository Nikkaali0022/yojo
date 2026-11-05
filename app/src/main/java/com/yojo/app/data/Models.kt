package com.yojo.app.data

data class YojoPost(
    val id: Int,
    val creator: String,
    val handle: String,
    val caption: String,
    val category: String,
    val likes: Int,
    val comments: Int,
    val liked: Boolean = false,
    val saved: Boolean = false
)

data class YojoStory(val name: String, val initials: String, val seen: Boolean = false)

object DemoContent {
    val stories = listOf(
        YojoStory("Your story", "+"), YojoStory("Maya", "M", true),
        YojoStory("Alex", "A"), YojoStory("Noor", "N"), YojoStory("Leo", "L")
    )
    val posts = listOf(
        YojoPost(1, "Maya Chen", "@mayacreates", "Little moments, big memories. ✨", "Travel", 1240, 86),
        YojoPost(2, "Alex Rivera", "@alex.r", "A new creative project is on the way.", "Creative", 892, 43),
        YojoPost(3, "Noor Khan", "@noor.visuals", "Finding beauty in everyday details.", "Photography", 2310, 112)
    )
}
