package ru.netology.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.R
import ru.netology.databinding.ActivityIntentHandlerBinding


// https://github.com/StillerPy/HelloAndroidWorld
class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityIntentHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                Snackbar.make(binding.root, R.string.error_empty_content, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        finish()
                    }.show()
                return@let
            } else {
                findNavController(R.id.fragment_container)
                    .navigate(R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            putString("post_content", text)
                        }
                        )
            }
        }
    }
}