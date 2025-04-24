package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.dto.Post
import ru.netology.viewmodel.emptyPost
import java.util.concurrent.TimeUnit

class PostRepositoryServer: PostRepository {
    companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        val jsonType = "application/json".toMediaType()
        val postsType = object: TypeToken<List<Post>>(){}.type
    }
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .build()
        val response = client.newCall(request).execute()
        val responseString = response.body?.string()
        val posts = gson.fromJson<List<Post>>(responseString, postsType)
        return posts
    }

    override fun likeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts/$id/likes")
            .post(gson.toJson(emptyPost).toRequestBody(jsonType))
            .build()
        client.newCall(request).execute()
    }

    override fun unlikeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts/$id/likes")
            .delete(gson.toJson(emptyPost).toRequestBody(jsonType))
            .build()
        client.newCall(request).execute()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api/slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        val response = client.newCall(request).execute()
        val responseString = response.body?.string()
        return gson.fromJson(responseString, Post::class.java)
    }
}