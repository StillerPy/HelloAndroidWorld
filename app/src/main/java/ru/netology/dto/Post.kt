package ru.netology.dto

import ru.netology.util.getDateTime

data class Post(
    val id: Long = -1,
    val author: String = "=== Post author ===",
    val published: String = "=== Publishing date ===",
    val content: String = "=== Content text ===",
    val videoUrl: String? = null,
    val likes: Int = 1099,
    val shared: Int = 999,
    val views: Int = 12345,
    val likedByMe: Boolean = false,
    val sharedByMe: Boolean = false
)
