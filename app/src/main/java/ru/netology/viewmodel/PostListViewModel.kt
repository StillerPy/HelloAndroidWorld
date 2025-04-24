package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryServer
import kotlin.concurrent.thread

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

class PostListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryServer()

    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
//    private val _changed = SingleLiveEvent<Unit>()
//    val changed: LiveData<Unit>
//        get() = _changed
    val edited = MutableLiveData(emptyPost)

    fun load() {
        thread {
            _data.postValue(FeedModel(loading = true))
            val result = try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: Exception) {
                FeedModel(error = true)
            }
            _data.postValue(result)
        }
    }

    fun likeById(id: Long) {
        val post = getById(id) ?: return
        thread {
            _data.postValue(FeedModel(loading = true))
            if (post.likedByMe) {
                repository.unlikeById(id)
            } else {
                repository.likeById(id)
            }
            _postCreated.postValue(Unit)
        }
    }

    fun shareById(id: Long) {
        //FIXME
        println("Share: $id")
    }

    fun removeById(id: Long) {
        val feedModel = data.value ?: return
        val posts = feedModel.posts.filter {
            it.id != id
        }
        _data.postValue(FeedModel(posts=posts))
        thread {
            repository.removeById(id)
            //_postCreated.postValue(Unit)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun saveContent(content: String) {
        edited.value?.let { editPost ->
            thread {
                repository.save(editPost.copy(content = content))
                _postCreated.postValue(Unit)
            }
        }
    }

    fun getById(id: Long): Post? {
        val feedModel = data.value ?: return null
        for (post in feedModel.posts) {
            if (post.id == id) {
                return post
            }
        }
        return null
    }
}