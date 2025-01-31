package ru.netology.view

import ru.netology.dto.Post

data class PostUiModel (
    val id: Long = -1,
    val author: String,
    val published: String,
    val content: String,
    val likesFormatted: String,
    val sharedFormatted: String,
    val viewsFormatted: String,
    val likedByMe: Boolean,
    val sharedByMe: Boolean
) {
   companion object {
       fun fromPost(post: Post): PostUiModel {
           return PostUiModel(
               post.id,
               post.author,
               post.published,
               post.content,
               formatNumber(post.likes),
               formatNumber(post.shared),
               formatNumber(post.views),
               post.likedByMe,
               post.sharedByMe
           )
       }
       private fun formatNumber(n: Int): String {
           if (n < 0) {
               throw IllegalArgumentException("Cannot be negative!")
           }
           if (n < 1000) {
               return n.toString()
           }
           if (n < 1_000_000) {
               val t = n / 1000
               val h = (n % 1000) / 100
               if (h > 0 && n < 10_000) {
                   return "$t.$h" + "K"
               }
               return "$t" + "K"
           }
           val m = n / 1_000_000
           val h = (n % 1_000_000) / 100_000
           if (h > 0 && n < 10_000_0000) {
               return "$m.$h" + "M"
           }
           return "$m" + "M"
       }
   }
}