package ru.netology.api
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import ru.netology.BuildConfig
import ru.netology.dto.Post

interface PostApiService {
    @GET("posts")
    fun getAll(): Call<List<Post>>
    @GET("posts/{id}")
    fun getById(@Path("id") id: Long): Call<Post>
    @DELETE("posts/{id}")
    fun removeById(@Path("id") id: Long): Call<Unit>
    @POST("posts/{id}/likes")
    fun likeById(@Path("id") id: Long): Call<Post>
    @DELETE("posts/{id}/likes")
    fun unlikeById(@Path("id") id: Long): Call<Post>
    @POST("posts")
    fun save(@Body post: Post): Call<Post>
}

object PostsApi {
    val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"
    private val logger = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service by lazy {
        retrofit.create<PostApiService>()
    }
}