package com.arctouch.codechallenge.ui.home

import com.arctouch.codechallenge.model.Movie

interface HomeContract {

    interface HomeView {
        fun populateMovieList(movies: List<Movie>)
        fun showProgress()
        fun hideProgress()
        fun addNewPage(movies: List<Movie>)
        fun showMessage(message: Int)
        fun showRetryButton()
        fun hideRetryButton()
    }

    interface HomePresenter {
        var homeView: HomeView?
        fun detachView()
        fun getMovieListWithGenres()
        fun getNextPage()
    }
}