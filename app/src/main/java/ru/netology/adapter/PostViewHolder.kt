package ru.netology.adapter

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.PostCardBinding
import ru.netology.dto.Post
import ru.netology.view.PostUiModel

class PostViewHolder(
    private val binding: PostCardBinding,
    private val listener: OnInteractionListener
): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        val postModel = PostUiModel.fromPost(post)
        with(binding) {
//            likeButton.setImageResource(
//                if (postModel.likedByMe) {
//                    R.drawable.ic_liked
//                } else {
//                    R.drawable.ic_like
//                }
//            )
            binding.likeButton.isChecked = post.likedByMe
            author.text = postModel.author
            published.text = postModel.published
            content.text = postModel.content
            likes.text = postModel.likesFormatted
            shared.text = postModel.sharedFormatted
            views.text = postModel.viewsFormatted
            menuButton.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.edit -> {
                                listener.onEdit(post)
                                true
                            }
                            R.id.remove -> {
                                listener.onRemove(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            likeButton.setOnClickListener {
                listener.onLike(post)
            }
            shareButton.setOnClickListener {
                listener.onShare(post)
            }
        }
    }
}
