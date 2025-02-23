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
        binding.undoButton.setOnClickListener {
            binding.postContentInput.setText(content)
        }
        binding.okButton.setOnClickListener {
            val text = binding.postContentInput.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("content", text)
                })
            }
            finish()
        }
    }
}

object NewPostContract: ActivityResultContract<String?, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, NewPostActivity::class.java).apply {
            putExtra("content", input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        return intent?.getStringExtra("content")
    }
}