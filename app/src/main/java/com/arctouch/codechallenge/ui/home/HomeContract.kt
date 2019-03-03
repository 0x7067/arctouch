package com.arctouch.codechallenge.ui.home

import com.arctouch.codechallenge.model.Movie

interface HomeContract {

    interface HomeView {
        fun populateMovieList(movies: List<Movie>)
        fun showProgress()
        fun hideProgress()
    }

    interface HomePresenter {
        var homeView: HomeView?
        fun dettachView()
        fun getMovieListWithGenres()
    }
}