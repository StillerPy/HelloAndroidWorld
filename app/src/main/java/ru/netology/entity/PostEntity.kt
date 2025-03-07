package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val published: String,
    val content: String,
    val videoUrl: String?,
    val likes: Int = 1099,
    val shared: Int = 999,
    val views: Int = 12345,
    val likedByMe: Boolean,
    val sharedByMe: Boolean
) {
    fun toPost(): Post {
        return Post (
            id,
            author,
            published,
            content,
            videoUrl,
            likes,
            shared,
            views,
            likedByMe,
            sharedByMe
        )
    }
    companion object {
        fun fromPost(post: Post): PostEntity {
            return PostEntity(
                post.id,
                post.author,
                post.published,
                post.content,
                post.videoUrl,
                post.likes,
                post.shared,
                post.views,
                post.likedByMe,
                post.sharedByMe
            )
        }
    }
}