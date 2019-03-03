package com.arctouch.codechallenge.ui.home

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.ui.home.HomeContract.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val api: TmdbApi) : HomeContract.HomePresenter {
    override var homeView: HomeView? = null

    private val compositeDisposable = CompositeDisposable()

    override fun getMovieListWithGenres() {
        homeView?.showProgress()

        val disposable = api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { it1 ->
                                val moviesWithGenres = it1.results.map { movie ->
                                    movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                                }
                                homeView?.populateMovieList(moviesWithGenres)
                                homeView?.hideProgress()
                            }
                }
        compositeDisposable.add(disposable)
    }

    override fun detachView() {
        homeView = null
        compositeDisposable.dispose()
    }
}