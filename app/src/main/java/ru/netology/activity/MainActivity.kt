package ru.netology.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.R
import ru.netology.adapter.OnInteractionListener
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.util.AndroidUtils
import ru.netology.util.focusAndShowKeyboard
import ru.netology.viewmodel.PostListViewModel
import ru.netology.viewmodel.emptyPost

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostListViewModel by viewModels()
        val newPostLauncher = registerForActivityResult(NewPostContract) { content ->
            if (content == null) {
                viewModel.edit(emptyPost)
                return@registerForActivityResult
            }
            viewModel.saveContent(content)
        }
        applyInset(binding.root)
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
                newPostLauncher.launch(post.content)
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
        })
        binding.main.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val isNewPost = (posts.size > adapter.currentList.size)
            adapter.submitList(posts) {
                if (isNewPost) {
                    binding.main.smoothScrollToPosition(0)
                }
            }

        }
        binding.addPostButton.setOnClickListener {
            newPostLauncher.launch(null)
        }
//        viewModel.edited.observe(this) { editedPost ->
//            if (editedPost.id == 0L) {
//                return@observe
//            } else {
//                binding.postContentInput.setText(editedPost.content)
//                binding.postContentInput.focusAndShowKeyboard()
//                binding.editPostGroup.visibility = View.VISIBLE
//                binding.originalPostText.text = editedPost.content
//            }
//        }
//        binding.saveButton.setOnClickListener {
//            val input = binding.postContentInput.text.toString()
//            if (input.isNullOrBlank()) {
//                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }
//            viewModel.saveContent(input)
//            binding.postContentInput.clearFocus()
//            binding.postContentInput.setText("")
//            binding.editPostGroup.visibility = View.GONE
//            AndroidUtils.hideKeyboard(it)
//            binding.main.smoothScrollToPosition(0)
//        }
//        binding.cancelEditPostButton.setOnClickListener {
//            binding.postContentInput.clearFocus()
//            binding.postContentInput.setText("")
//            binding.editPostGroup.visibility = View.GONE
//            AndroidUtils.hideKeyboard(it)
//            viewModel.cancelEditing()
//        }
    }

    private fun applyInset(main: View) {
        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Для клавиатуры:
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isImeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            v.setPadding(
                v.paddingLeft,
                systemBars.top,
                v.paddingRight,
                if (isImeVisible) imeInsets.bottom else systemBars.bottom
            )
            insets
        }
    }
}