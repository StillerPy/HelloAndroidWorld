package ru.netology.activity

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.R

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .circleCrop()
        .error(R.drawable.ic_error)
        .placeholder(R.drawable.ic_loading)
        .timeout(10_000)
        .into(this)
}