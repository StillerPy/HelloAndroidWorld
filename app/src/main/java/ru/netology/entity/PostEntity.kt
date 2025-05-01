package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dao.PostDao_Impl
import ru.netology.db.AppDb_Impl
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val published: String,
    val content: String,
    val videoUrl: String?,
    val likes: Int,
    val shared: Int,
    val views: Int,
    val likedByMe: Boolean,
    val sharedByMe: Boolean
) {
    fun toPost(): Post {
        return Post (
            id,
            author,
            authorAvatar,
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
                post.authorAvatar,
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