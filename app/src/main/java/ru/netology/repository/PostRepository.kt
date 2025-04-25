package ru.netology.repository
import ru.netology.dto.Post

interface PostRepository {
    //fun getAll(): LiveData<List<Post>>
    fun getAll(): List<Post>
    fun likeById(id: Long)
    //fun shareById(id: Long)
    fun unlikeById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post): Post

    fun getAllAsync(callback: MyCallback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: MyCallback<Unit>)
    fun unlikeByIdAsync(id: Long, callback: MyCallback<Unit>)
    fun removeByIdAsync(id: Long, callback: MyCallback<Unit>)
    fun saveAsync(post: Post, callback: MyCallback<Post>)

    interface MyCallback<T> {
        fun onSuccess(data: T)
        fun onError(error: Exception)
    }
}