package ru.netology.view

import android.os.Bundle
import android.text.Editable
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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostListViewModel by viewModels()
        applyInset(binding.root)
        val adapter = PostAdapter(object: OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }
            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
            }
            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }
            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }
        })
        binding.main.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
//            {
//                binding.main.smoothScrollToPosition(0)
//            }
        }
        viewModel.edited.observe(this) { editedPost ->
            if (editedPost.id == 0L) {
                return@observe
            } else {
                binding.postContentInput.setText(editedPost.content)
                binding.postContentInput.focusAndShowKeyboard()
            }
        }
        binding.saveButton.setOnClickListener {
            val input = binding.postContentInput.text.toString()
            if (input.isNullOrBlank()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.saveContent(input)
            binding.postContentInput.clearFocus()
            binding.postContentInput.setText("")
            AndroidUtils.hideKeyboard(it)
            binding.main.smoothScrollToPosition(0)
        }
    }

    private fun applyInset(main: View) {
        ViewCompat.setOnApplyWindowInsetsListener(main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                v.paddingLeft + systemBars.left,
                v.paddingTop + systemBars.top,
                v.paddingRight + systemBars.right,
                v.paddingBottom + systemBars.bottom
            )
            insets
        }
    }
}