package com.arctouch.codechallenge.ui.details

import com.arctouch.codechallenge.model.Movie

interface MovieDetailsContract {

    interface MovieView {
        fun setMovieDetails(movie: Movie)
    }

    interface MovieDetailsPresenter {
        var movieView: MovieView?
        fun attachView(movieView: MovieView)
        fun getMovieDetailsById(id: Int)
        fun dettachView()
    }
}