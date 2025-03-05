package ru.netology.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post
import ru.netology.util.getDateTime


class PostRepositoryFiles(private val context: Context) : PostRepository {
    companion object {
        private val gson = Gson()
        private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
        private const val FILE_NAME = "posts.json"
    }

    private var nextId: Long = 1
    private var posts: List<Post> = emptyList()
        set(value) {
            field = value
            sync()
        }
    private val data = MutableLiveData(posts)

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, typeToken)
                data.value = posts
                nextId = (posts.maxOfOrNull {post -> post.id} ?: 0) + 1
            }
        }
    }
    private fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

    override fun getAll(): LiveData<List<Post>> {
        return data
    }

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id == id) {
                likePost(it)
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id == id) {
                sharePost(it)
            } else {
                it
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = getDateTime()
                )
            ) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) {
                    it
                } else {
                    it.copy(content = post.content)
                }
            }
        }
        data.value = posts
    }

    private fun likePost(post: Post): Post {
        return post.copy(
            likedByMe = !post.likedByMe,
            likes = if (post.likedByMe) {
                post.likes - 1
            } else {
                post.likes + 1
            }
        )
    }

    private fun sharePost(post: Post): Post {
        return post.copy(
            sharedByMe = true,
            shared = post.shared + 1
        )
    }
}