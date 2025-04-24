package ru.netology.repository
import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostRepository {
    //fun getAll(): LiveData<List<Post>>
    fun getAll(): List<Post>
    fun likeById(id: Long)
    //fun shareById(id: Long)
    fun unlikeById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(error: Exception)
    }
}