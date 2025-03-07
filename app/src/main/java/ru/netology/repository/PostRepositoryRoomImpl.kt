package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity


class PostRepositoryRoomImpl(
    private val dao: PostDao
) : PostRepository {


    override fun getAll(): LiveData<List<Post>> = dao.getAll().map{list -> list.map{it.toPost()}}

    override fun save(post: Post) = dao.save(PostEntity.fromPost(post))

    override fun likeById(id: Long) = dao.likeById(id)

    override fun shareById(id: Long) = dao.shareById(id)

    override fun removeById(id: Long) = dao.removeById(id)
}
