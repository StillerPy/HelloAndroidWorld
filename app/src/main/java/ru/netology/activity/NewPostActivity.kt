package ru.netology.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.R
import ru.netology.databinding.ActivityNewPostBinding
import ru.netology.dto.Post

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val content = intent.getStringExtra("content")
        binding.postContentInput.setText(content)
        binding.okButton.setOnClickListener {
            val text = binding.postContentInput.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, text)
                })
            }
            finish()
        }
    }
}

object NewPostContract: ActivityResultContract<Post?, Post?>() {
    override fun createIntent(context: Context, input: Post?): Intent {
        return Intent(context, NewPostActivity::class.java).apply {
            if (input != null) {
                putExtra("id", input.id)
                putExtra("author", input.author)
                putExtra("published", input.published)
                putExtra("content", input.content)
                putExtra("likes", input.likes)
                putExtra("shared", input.shared)
                putExtra("views", input.views)
                putExtra("likedByMe", input.likedByMe)
                putExtra("sharedByMe", input.sharedByMe)
            }
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Post? {
        if (intent == null) {
            return null
        }
        return Post(
            intent.getLongExtra("id", 0),
            intent.getStringExtra("author") ?: "",
            intent.getStringExtra("published") ?: "",
            intent.getStringExtra("content") ?: "11111",
            intent.getIntExtra("likes", 0),
            intent.getIntExtra("shared", 0),
            intent.getIntExtra("views", 0),
            intent.getBooleanExtra("likedByMe", false),
            intent.getBooleanExtra("sharedByMe", false)
        )
    }
}