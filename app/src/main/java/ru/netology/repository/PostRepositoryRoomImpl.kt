package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.R
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity
import ru.netology.util.getDateTime


class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {


    override fun getAll(): LiveData<List<Post>> = dao.getAll().map{list -> list.map{it.toPost()}}

    override fun save(post: Post) {
        if (post.id == 0L) {
            dao.save(PostEntity.fromPost(post.copy(author = "Me",
                published = getDateTime())))
        } else {
            dao.save(PostEntity.fromPost(post))
        }
    }

    override fun likeById(id: Long) = dao.likeById(id)

    override fun shareById(id: Long) = dao.shareById(id)

    override fun removeById(id: Long) = dao.removeById(id)
}
