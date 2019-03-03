package com.arctouch.codechallenge.ui.details

import com.arctouch.codechallenge.model.MovieDetails

interface MovieDetailsContract {

    interface MovieView {
        fun setMovieDetails(movieDetails: MovieDetails)
    }

    interface MovieDetailsPresenter {
        var movieView: MovieView?
        fun attachView(movieView: MovieView)
        fun getMovieDetailsById(id: Int)
        fun detachView()
    }
}