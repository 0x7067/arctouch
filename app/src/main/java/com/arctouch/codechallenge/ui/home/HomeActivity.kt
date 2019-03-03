package com.arctouch.codechallenge.ui.home

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.di.ServiceLocator
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.progressBar
import kotlinx.android.synthetic.main.home_activity.recyclerView
import kotlinx.android.synthetic.main.home_activity.retryButton

class HomeActivity : AppCompatActivity(), HomeContract.HomeView {
    private val homePresenter = ServiceLocator.homePresenter

    private lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        homePresenter.homeView = this
        homePresenter.getMovieListWithGenres()
    }

    override fun showRetryButton() {
        hideProgress()
        retryButton.visibility = View.VISIBLE
        retryButton.setOnClickListener {
            homePresenter.getMovieListWithGenres()
            retryButton.visibility = View.GONE
        }
    }

    override fun hideRetryButton() {
        retryButton.visibility = View.GONE
    }

    override fun showMessage(message: Int) {
        Snackbar.make(window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun addNewPage(movies: List<Movie>) {
        homeAdapter.addMoreMovies(movies)
    }

    override fun populateMovieList(movies: List<Movie>) {
        homeAdapter = HomeAdapter(movies)
        recyclerView.adapter = homeAdapter
        recyclerView.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    homePresenter.getNextPage()
                }
            }
        })
        hideProgress()
        hideRetryButton()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}
