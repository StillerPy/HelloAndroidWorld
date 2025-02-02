package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

private val SAMPLE_POSTS = listOf(
    Post(
        1L,
        "Анна К.",
        "13.09.2024",
        "Сегодня я решила приготовить что-то новое на ужин. В итоге, вместо куриного филе, я купила куриные ноги. Теперь у меня в холодильнике целая семья куриных ног, и я не знаю, как их уговорить стать ужином. Может, устрою им вечерний чай?",
        0,
        0,
        0,
        false,
        false
    ),
    Post(
        2L,
        "Сергей П.",
        "15.10.2024",
        "Недавно решил заняться спортом и купил абонемент в спортзал. Первое занятие: 10 минут разминки, 5 минут на беговой дорожке и 45 минут на диване. Зато я точно знаю, что у меня отличная форма… для отдыха!",
        0,
        0,
        0,
        false,
        false
    ),
    Post(
        2L,
        "Мария Л.",
        "13.01.2025",
        "Пошла в магазин за молоком, а вернулась с тремя новыми книгами и шоколадкой. Теперь у меня есть все для идеального вечера: чтение, шоколад и… молоко? Ах да, оно осталось в магазине. Ну, ничего, может, книги тоже хорошо сочетаются с чаем!",
        0,
        0,
        0,
        false,
        false
    )
)
private fun makePostList(n: Int): List<Post> {
    val out = mutableListOf<Post>()
    for (i in 0..<n) {
        val post = SAMPLE_POSTS[i % SAMPLE_POSTS.size]
        val id = i + 1L
        out.add(post.copy(id=id, author = post.author + " ($id)"))
    }
    return out.reversed()
}

class PostRepositoryInMemory : PostRepository {
    private var posts: List<Post> = makePostList(100)
    private val data = MutableLiveData(posts)
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
            shared = post.shared + 1,
            content = post.content + "\nShared!"
        )
    }
}