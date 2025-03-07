package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.db.AppDb
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryRoomImpl

val emptyPost = Post(
    0L,
    "",
    "",
    "",
    null,
    0,
    0,
    0,
    false,
    false,
)

class PostListViewModel(application: Application): AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryRoomImpl(AppDb.getInstance(application).postDao)
    val data: LiveData<List<Post>> = repository.getAll()
    val edited = MutableLiveData(emptyPost)
    fun likeById(id: Long) {
        repository.likeById(id)
    }
    fun shareById(id: Long) {
        repository.shareById(id)
    }
    fun removeById(id: Long) {
        repository.removeById(id)
    }
    fun edit(post: Post) {
        edited.value = post
    }
    fun saveContent(content: String) {
        edited.value?.let { editPost ->
            repository.save(editPost.copy(content = content))
        }
        edited.value = emptyPost
    }
    fun getById(id: Long): Post? {
        for (post in data.value!!) {
            if (post.id == id) {
                return post
            }
        }
        return null
    }
}