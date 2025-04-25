package ru.netology.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.dto.Post
import ru.netology.viewmodel.emptyPost
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryServer: PostRepository {
    companion object {
        const val BASE_URL = "http://10.0.2.2:9999/"
        val jsonType = "application/json".toMediaType()
        val postsType = object: TypeToken<List<Post>>(){}.type
        //val slow = "/slow"
        val slow = ""
    }
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    override fun getAll(): List<Post> {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts")
            .build()
        val response = client.newCall(request).execute()
        val responseString = response.body?.string()
        val posts = gson.fromJson<List<Post>>(responseString, postsType)
        return posts
    }

    override fun likeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts/$id/likes")
            .post(gson.toJson(emptyPost).toRequestBody(jsonType))
            .build()
        client.newCall(request).execute()
    }

    override fun unlikeById(id: Long) {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts/$id/likes")
            .delete(gson.toJson(emptyPost).toRequestBody(jsonType))
            .build()
        client.newCall(request).execute()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}api$slow/posts/$id")
            .build()
        client.newCall(request)
            .execute()
    }

    override fun save(post: Post): Post {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        val response = client.newCall(request).execute()
        val responseString = response.body?.string()
        return gson.fromJson(responseString, Post::class.java)
    }

    override fun getAllAsync(callback: PostRepository.MyCallback<List<Post>>) {
        //TODO("Not yet implemented")
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts")
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(gson.fromJson(response.body?.string(), postsType))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

            })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.MyCallback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts/$id/likes")
            .post(gson.toJson(emptyPost).toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }

            })
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.MyCallback<Unit>) {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts/$id/likes")
            .delete(gson.toJson(emptyPost).toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }

            })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.MyCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}api$slow/posts/$id")
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(Unit)
                }

            })
    }

    override fun saveAsync(post: Post, callback: PostRepository.MyCallback<Post>) {
        val request = Request.Builder()
            .url("${BASE_URL}api$slow/posts")
            .post(gson.toJson(post).toRequestBody(jsonType))
            .build()
        client.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
                override fun onResponse(call: Call, response: Response) {
                    callback.onSuccess(post)
                }

            })
    }
}























