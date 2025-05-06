package ru.netology.adapter

import android.app.Activity
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.BuildConfig
import ru.netology.R
import ru.netology.activity.PostFragment
import ru.netology.activity.load
import ru.netology.api.PostsApi
import ru.netology.databinding.PostCardBinding
import ru.netology.dto.Post
import ru.netology.repository.PostRepositoryServer
import ru.netology.view.PostUiModel

class PostViewHolder(
    private val binding: PostCardBinding,
    private val listener: OnInteractionListener

): RecyclerView.ViewHolder(binding.root) {
    val URL: String = BuildConfig.BASE_URL
    fun bind(post: Post) {
        val postModel = PostUiModel.fromPost(post)
        println(post.attachment)
        with(binding) {
            binding.likeButton.isChecked = post.likedByMe
            author.text = postModel.author
            published.text = postModel.published
            content.text = postModel.content
            likeButton.text = postModel.likesFormatted
            shareButton.text = postModel.sharedFormatted
            viewButton.text = postModel.viewsFormatted
            binding.root.setOnClickListener {
                listener.onPostClick(post)
            }
            if (post.attachment != null) {
                if (post.attachment.type == "IMAGE") {
                    binding.attachmentImage.visibility = View.VISIBLE
                    val url = "${URL}/images/${post.attachment.url}"
                    Glide.with(binding.attachmentImage)
                        .load(url)
                        .placeholder(R.drawable.video_placeholder)
                        .timeout(10_000)
                        .into(binding.attachmentImage)
                    binding.attachmentImage.contentDescription = post.attachment.description
                }
            } else if (post.videoUrl != null) {
                binding.attachmentImage.setOnClickListener {
                    binding.attachmentImage.visibility = View.VISIBLE
                    listener.onVideoClick(post)
                }
            }

            if (post.authorAvatar.isNotEmpty()) {
                val url = "${URL}/avatars/${post.authorAvatar}"
                binding.avatar.load(url)
            }

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
