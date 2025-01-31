package ru.netology.view
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.R
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        applyInset(binding.main)
        viewModel.data.observe(this) { post ->
            val uiModel = PostUiModel.fromPost(post)
            with(binding) {
                likeButton.setImageResource(
                    if (uiModel.likedByMe) {
                        R.drawable.ic_liked
                    } else {
                        R.drawable.ic_like
                    }
                )
                author.text = uiModel.author
                published.text = uiModel.published
                content.text = uiModel.content
                likes.text = uiModel.likesFormatted
                shared.text = uiModel.sharedFormatted
                views.text = uiModel.viewsFormatted
            }
        }
        binding.likeButton.setOnClickListener {
            viewModel.like()
        }
        binding.sharedIcon.setOnClickListener {
            viewModel.share()
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