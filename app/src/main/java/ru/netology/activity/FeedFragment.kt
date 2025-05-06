package ru.netology.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.FragmentFeedBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostListViewModel


//https://github.com/StillerPy/HelloAndroidWorld
class FeedFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater, container, false)
        val viewModel: PostListViewModel by activityViewModels()

        val adapter = PostAdapter(object: OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = ACTION_SEND
                    type = "text/plain"
                    putExtra(EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        putString("post_content", post.content)
                    })
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
            override fun onVideoClick(post: Post) {
                val videoIntent = Intent(Intent.ACTION_VIEW).apply {
                    setData(Uri.parse(post.videoUrl))
                }
                startActivity(videoIntent)
            }
            override fun onPostClick(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_postFragment,
                    Bundle().apply {
                        putLong("post_id", post.id)
                    })
            }
        })

        binding.main.adapter = adapter
        viewModel.load()
        viewModel.data.observe(viewLifecycleOwner) { feedModel ->
            binding.errorGroup.isVisible = feedModel.errorMessage != null
            if (feedModel.errorMessage != null) {
                binding.retryTitle.text = resources.getString(R.string.retry_title) + " " + feedModel.errorMessage
            }
            binding.loadingProgressBar.isVisible = feedModel.loading
            binding.empty.isVisible = feedModel.empty
            val isNewPost = (feedModel.posts.size > adapter.currentList.size)
            adapter.submitList(feedModel.posts) {
                if (isNewPost) {
                    binding.main.smoothScrollToPosition(0)
                }
            }
        }
        viewModel.needsRefreshing.observe(viewLifecycleOwner) {
            viewModel.load()
            findNavController().navigateUp()
        }
        binding.addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        binding.retryButton.setOnClickListener {
            viewModel.load()
        }
        return binding.root
    }
}