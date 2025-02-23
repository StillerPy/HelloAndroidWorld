package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post


class PostRepositoryInMemory : PostRepository {
    private var nextId = 1L
    private var posts: List<Post> = listOf(
        Post(
            nextId++,
            "Анна К.",
            "13.09.2024",
            "Сегодня я решила приготовить что-то новое на ужин. В итоге, вместо куриного филе, я купила куриные ноги. Теперь у меня в холодильнике целая семья куриных ног, и я не знаю, как их уговорить стать ужином. Может, устрою им вечерний чай?",
            "https://www.youtube.com/watch?v=pLjdmlv3lGU",
            2,
            0,
            10,
            true,
            false
        ),
        Post(
            nextId++,
            "Сергей П.",
            "15.10.2024",
            "Недавно решил заняться спортом и купил абонемент в спортзал. Первое занятие: 10 минут разминки, 5 минут на беговой дорожке и 45 минут на диване. Зато я точно знаю, что у меня отличная форма… для отдыха!",
            "https://www.youtube.com/watch?v=-2gALnVTbEc&ab_channel=%D0%A2%D1%80%D0%B8%D0%B1%D1%83%D0%BD",
            0,
            0,
            0,
            false,
            false
        ),
        Post(
            nextId++,
            "Мария Л.",
            "13.01.2025",
            "Пошла в магазин за молоком, а вернулась с тремя новыми книгами и шоколадкой. Теперь у меня есть все для идеального вечера: чтение, шоколад и… молоко? Ах да, оно осталось в магазине. Ну, ничего, может, книги тоже хорошо сочетаются с чаем!",
            null,
            0,
            0,
            0,
            false,
            false
        )
    )
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
            //content = post.content + "\nShared!"
        )
    }
}