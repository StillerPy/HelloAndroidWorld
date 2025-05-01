package ru.netology.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.FragmentPostBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostListViewModel
import ru.netology.viewmodel.emptyPost

class PostFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val postId = arguments?.getLong("post_id") ?: 0L
        val viewModel: PostListViewModel by activityViewModels()
        val post = viewModel.getById(postId) ?: emptyPost
        val binding = FragmentPostBinding.inflate(layoutInflater, container, false)
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
                findNavController().navigate(
                    R.id.action_postFragment_to_newPostFragment,
                    Bundle().apply {
                        putString("post_content", post.content)
                    })
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
                findNavController().navigateUp()
            }

            override fun onVideoClick(post: Post) {
                val videoIntent = Intent(Intent.ACTION_VIEW).apply {
                    setData(Uri.parse(post.videoUrl))
                }
                startActivity(videoIntent)
            }

            override fun onPostClick(post: Post) {}
        })
        binding.main.adapter = adapter
        adapter.submitList(listOf(post))
        println(post)
        viewModel.data.observe(viewLifecycleOwner) { feedModel ->
            adapter.submitList(feedModel.posts.filter { it.id == postId })
        }
        viewModel.needsRefreshing.observe(viewLifecycleOwner) {
            viewModel.load()
            findNavController().navigateUp()
        }
        return binding.root
    }
}