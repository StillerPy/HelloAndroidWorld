package ru.netology.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_AUTHOR
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_CONTENT
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_ID
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_LIKED_BY_ME
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_LIKES
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_PUBLISHED
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_SHARED
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_SHARED_BY_ME
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_VIDEO_URL
import ru.netology.dao.PostDaoImpl.PostColumns.COLUMN_VIEWS
import ru.netology.dao.PostDaoImpl.PostColumns.TABLE
import ru.netology.dto.Post


class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE $TABLE (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            
            $COLUMN_AUTHOR TEXT NOT NULL,
            $COLUMN_CONTENT TEXT NOT NULL,
            $COLUMN_PUBLISHED TEXT NOT NULL,
            $COLUMN_VIDEO_URL TEXT,
            
            $COLUMN_LIKES INTEGER NOT NULL DEFAULT 0,
            $COLUMN_SHARED INTEGER NOT NULL DEFAULT 0,
            $COLUMN_VIEWS INTEGER NOT NULL DEFAULT 0,
            
            $COLUMN_LIKED_BY_ME BOOLEAN NOT NULL DEFAULT 0,
            $COLUMN_SHARED_BY_ME BOOLEAN NOT NULL DEFAULT 0
            
        );
        """.trimIndent()
    }
    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"

        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_VIDEO_URL = "videoUrl"

        const val COLUMN_LIKES = "likes"
        const val COLUMN_SHARED = "shared"
        const val COLUMN_VIEWS = "views"

        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_SHARED_BY_ME = "sharedByMe"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,

            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_VIDEO_URL,

            COLUMN_LIKES,
            COLUMN_SHARED,
            COLUMN_VIEWS,

            COLUMN_LIKED_BY_ME,
            COLUMN_SHARED_BY_ME
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "$COLUMN_ID DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            // TODO: remove hardcoded values
            put(COLUMN_AUTHOR, "Me")
            put(COLUMN_CONTENT, post.content)
            put(COLUMN_PUBLISHED, "now")
            put(COLUMN_VIDEO_URL, post.videoUrl)

            put(COLUMN_LIKES, post.likes)
            put(COLUMN_SHARED, post.shared)
            put(COLUMN_VIEWS, post.views)

            put(COLUMN_LIKED_BY_ME, post.likedByMe)
            put(COLUMN_SHARED_BY_ME, post.sharedByMe)
        }
        val id = if (post.id != 0L) {
            db.update(
                TABLE,
                values,
                "$COLUMN_ID = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(TABLE, null, values)
        }
        db.query(
            TABLE,
            PostColumns.ALL_COLUMNS,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE $TABLE SET
               $COLUMN_LIKES = $COLUMN_LIKES + CASE WHEN $COLUMN_LIKED_BY_ME THEN -1 ELSE 1 END,
               $COLUMN_LIKED_BY_ME = CASE WHEN $COLUMN_LIKED_BY_ME THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            TABLE,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
           UPDATE $TABLE SET
               $COLUMN_SHARED = $COLUMN_SHARED + 1,
               $COLUMN_SHARED_BY_ME = 1
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(COLUMN_PUBLISHED)),
                videoUrl = getStringOrNull(getColumnIndexOrThrow(COLUMN_VIDEO_URL)),

                likes = getInt(getColumnIndexOrThrow(COLUMN_LIKES)),
                shared = getInt(getColumnIndexOrThrow(COLUMN_SHARED)),
                views = getInt(getColumnIndexOrThrow(COLUMN_VIEWS)),

                likedByMe = getInt(getColumnIndexOrThrow(COLUMN_LIKED_BY_ME)) != 0,
                sharedByMe = getInt(getColumnIndexOrThrow(COLUMN_SHARED_BY_ME)) != 0,
            )
        }
    }
}