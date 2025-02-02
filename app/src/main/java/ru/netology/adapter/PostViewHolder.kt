package ru.netology.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.databinding.PostCardBinding
import ru.netology.dto.Post
import ru.netology.view.PostUiModel

class PostViewHolder(
    private val binding: PostCardBinding,
    private val onLikeClick: (Post) -> Unit,
    private val onShareClick: (Post) -> Unit
): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        val postModel = PostUiModel.fromPost(post)
        with(binding) {
            likeButton.setImageResource(
                if (postModel.likedByMe) {
                    ru.netology.R.drawable.ic_liked
                } else {
                    ru.netology.R.drawable.ic_like
                }
            )
            author.text = postModel.author
            published.text = postModel.published
            content.text = postModel.content
            likes.text = postModel.likesFormatted
            shared.text = postModel.sharedFormatted
            views.text = postModel.viewsFormatted
            likeButton.setOnClickListener {
                onLikeClick(post)
            }
            shareButton.setOnClickListener {
                onShareClick(post)
            }
        }
    }
}
