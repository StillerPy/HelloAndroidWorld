package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryServer


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
        _data.value = FeedModel(loading = true)

        repository.getAllAsync(
            object : PostRepository.MyCallback<List<Post>> {
                override fun onSuccess(data: List<Post>) {
                    _data.postValue(FeedModel(posts = data, empty = data.isEmpty()))
                }

                override fun onError(error: Exception) {
                    _data.postValue(FeedModel(error = true))
                }

            }
        )
//        thread {
//            _data.postValue(FeedModel(loading = true))
//            val result = try {
//                val posts = repository.getAll()
//                FeedModel(posts = posts, empty = posts.isEmpty())
//            } catch (e: Exception) {
//                FeedModel(error = true)
//            }
//            _data.postValue(result)
//        }
    }

    fun likeById(id: Long) {
        val post = getById(id) ?: return
        _data.postValue(FeedModel(loading = true))
        val callback = object : PostRepository.MyCallback<Unit> {
            override fun onSuccess(data: Unit) {
                _postCreated.postValue(Unit)
            }
            override fun onError(error: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
        if (post.likedByMe) {
            repository.unlikeByIdAsync(id, callback)
        } else {
            repository.likeByIdAsync(id, callback)
        }
        _postCreated.postValue(Unit)
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
        val callback = object: PostRepository.MyCallback<Unit> {
            override fun onSuccess(data: Unit) {}

            override fun onError(error: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
        repository.removeByIdAsync(id, callback)
//        thread {
//            repository.removeById(id)
//            //_postCreated.postValue(Unit)
//        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun saveContent(content: String) {
        val post = edited.value ?: return
        val callback = object: PostRepository.MyCallback<Post> {
            override fun onSuccess(data: Post) {
                _postCreated.postValue(Unit)
            }
            override fun onError(error: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }
        repository.saveAsync(post.copy(content = content), callback)
//        edited.value?.let { editPost ->
//            thread {
//                repository.save(editPost.copy(content = content))
//                _postCreated.postValue(Unit)
//            }
//        }
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