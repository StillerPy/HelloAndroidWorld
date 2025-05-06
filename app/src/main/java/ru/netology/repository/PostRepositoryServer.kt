package ru.netology.repository

import retrofit2.Callback
import ru.netology.api.PostsApi
import ru.netology.dto.Post


class PostRepositoryServer: PostRepository {
    override fun getAllAsync(callback: PostRepository.MyCallback<List<Post>>) {
        PostsApi.service.getAll().enqueue(object: Callback<List<Post>> {
            override fun onResponse(
                call: retrofit2.Call<List<Post>>,
                response: retrofit2.Response<List<Post>>
            ) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                } else {
                    callback.onError(RuntimeException(response.code().toString()))
                }
            }
            override fun onFailure(call: retrofit2.Call<List<Post>>, error: Throwable) {
                callback.onError(Exception(error))
            }

        })
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.MyCallback<Post>) {
        PostsApi.service.likeById(id).enqueue(object: Callback<Post> {
            override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                } else {
                    callback.onError(RuntimeException(response.code().toString()))
                }
            }
            override fun onFailure(call: retrofit2.Call<Post>, error: Throwable) {
                callback.onError(Exception(error))
            }
        })
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.MyCallback<Post>) {
        PostsApi.service.unlikeById(id).enqueue(object: Callback<Post> {
            override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                } else {
                    callback.onError(RuntimeException(response.code().toString()))
                }
            }
            override fun onFailure(call: retrofit2.Call<Post>, error: Throwable) {
                callback.onError(Exception(error))
            }
        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.MyCallback<Unit>) {
        PostsApi.service.removeById(id).enqueue(object: Callback<Unit> {
            override fun onResponse(call: retrofit2.Call<Unit>, response: retrofit2.Response<Unit>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                } else {
                    callback.onError(RuntimeException(response.code().toString()))
                }
            }

            override fun onFailure(call: retrofit2.Call<Unit>, error: Throwable) {
                callback.onError(Exception(error))
            }

        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.MyCallback<Post>) {
        PostsApi.service.save(post).enqueue(object: Callback<Post> {
            override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("Empty body"))
                } else {
                    callback.onError(RuntimeException(response.code().toString()))
                }
            }

            override fun onFailure(call: retrofit2.Call<Post>, error: Throwable) {
                callback.onError(Exception(error))
            }

        })
    }
}























