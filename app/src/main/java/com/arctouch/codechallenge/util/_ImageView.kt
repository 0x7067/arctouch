package com.arctouch.codechallenge.util

import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_movie_details.iv_movie_banner

fun ImageView.loadUrl(url: String) {
    Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
            .into(this)
}