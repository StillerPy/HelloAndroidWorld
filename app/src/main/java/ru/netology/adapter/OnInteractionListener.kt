package ru.netology.adapter

import ru.netology.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onEdit(post: Post)
    fun onRemove(post: Post)
    fun onVideoClick(post: Post)
    fun onPostClick(post: Post)
}