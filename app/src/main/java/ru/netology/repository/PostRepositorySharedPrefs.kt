package ru.netology.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post


class PostRepositorySharedPrefs(context: Context) : PostRepository {
    companion object {
        private val gson = Gson()
        private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
        private const val KEY = "key"
    }
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var nextId: Long = 1
    private var posts: List<Post> = emptyList()
    private val data = MutableLiveData(posts)

    init {
        prefs.getString(KEY, null)?.let {
            posts = gson.fromJson(it, typeToken)
            data.value = posts
            nextId = (posts.maxOfOrNull {post -> post.id} ?: 0) + 1
        }
    }
    private fun sync() {
        with(prefs.edit()) {
            putString(KEY, gson.toJson(posts))
            apply()
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
        sync()
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
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "Now"
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
        sync()
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