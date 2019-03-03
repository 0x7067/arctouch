package com.arctouch.codechallenge.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.R.string
import com.arctouch.codechallenge.di.ServiceLocator
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MovieDetails
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.loadUrl
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_movie_details.iv_movie_banner
import kotlinx.android.synthetic.main.activity_movie_details.tv_movie_genres
import kotlinx.android.synthetic.main.activity_movie_details.tv_movie_summary
import kotlinx.android.synthetic.main.activity_movie_details.tv_movie_title

class MovieDetailsActivity : AppCompatActivity(), MovieDetailsContract.MovieView {

    private val movieDetailsPresenter = ServiceLocator.movieDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        movieDetailsPresenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        movieDetailsPresenter.getMovieDetailsById(intent.getIntExtra(EXTRA_MOVIE_ID, 0))
    }

    override fun setMovieDetails(movieDetails: MovieDetails) {
        tv_movie_title.text = movieDetails.title
        tv_movie_summary.text = if (movieDetails.overview.isNullOrBlank()) getString(
                string.no_overview_message) else movieDetails.overview
        tv_movie_genres.text = movieDetails.genres

        movieDetails.backdropURL?.let {
            iv_movie_banner.loadUrl(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieDetailsPresenter.dettachView()
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID"

        fun openMovieDetails(id: Int, context: Context) {
            val movieDetailsIntent = Intent(context, MovieDetailsActivity::class.java).apply {
                putExtra(EXTRA_MOVIE_ID, id)
            }
            context.startActivity(movieDetailsIntent)
        }
    }
}
