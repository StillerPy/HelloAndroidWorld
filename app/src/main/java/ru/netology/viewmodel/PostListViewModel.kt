package ru.netology.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemory

val emptyPost = Post(
    0L,
    "",
    "",
    "",
    0,
    0,
    0,
    false,
    false,
)

class PostListViewModel: ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemory()
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
    fun cancelEditing() {
        edited.value = emptyPost
    }
}