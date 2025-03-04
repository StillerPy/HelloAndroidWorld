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

fun getLongPost(): Post {
    var text = ""
    repeat(600) {
        text += "post "
    }
    return Post(
        0,
        "Long post",
        getDateTime(),
        text,
        "https://www.youtube.com/watch?v=sGigzOanhhc&ab_channel=%D0%A6%D0%B8%D1%84%D1%80%D0%BE%D0%B2%D0%B0%D1%8F%D0%B8%D1%81%D1%82%D0%BE%D1%80%D0%B8%D1%8F",
        100500,
        600,
        200700
    )
}