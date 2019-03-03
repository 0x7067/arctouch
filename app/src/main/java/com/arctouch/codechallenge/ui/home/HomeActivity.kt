package com.arctouch.codechallenge.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.di.ServiceLocator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.progressBar
import kotlinx.android.synthetic.main.home_activity.recyclerView

class HomeActivity : AppCompatActivity() {

    private val tmdbApi = ServiceLocator.tmdbApi

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        tmdbApi.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    tmdbApi.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { it1 ->
                                val moviesWithGenres = it1.results.map { movie ->
                                    movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                                }
                                recyclerView.adapter = HomeAdapter(moviesWithGenres)
                                progressBar.visibility = View.GONE
                            }
                }
    }
}
