package ru.netology.repository
import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostRepository {
//    fun get(): LiveData<Post>
//    fun like()
//    fun share()
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}