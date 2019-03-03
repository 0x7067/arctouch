package com.arctouch.codechallenge.ui.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.di.ServiceLocator
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.progressBar
import kotlinx.android.synthetic.main.home_activity.recyclerView

class HomeActivity : AppCompatActivity(), HomeContract.HomeView {

    private val homePresenter = ServiceLocator.homePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        homePresenter.homeView = this
        homePresenter.getMovieListWithGenres()
    }

    override fun populateMovieList(movies: List<Movie>) {
        recyclerView.adapter = HomeAdapter(movies)
        hideProgress()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
