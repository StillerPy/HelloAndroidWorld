package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

class PostRepositoryInMemory: PostRepository {
    private var post = Post()
    private val data = MutableLiveData(post)
    override fun get(): LiveData<Post> = data
    override fun like() {
        post = if (post.likedByMe) {
            post.copy(likedByMe = false, likes = post.likes - 1)
        } else {
            post.copy(likedByMe = true, likes = post.likes + 1)
        }
        data.value = post
    }
    override fun share() {
        post = post.copy(shared = post.shared + 1, sharedByMe = true)
        data.value = post
    }
}