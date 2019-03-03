package com.arctouch.codechallenge.ui.home

import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.ui.home.HomeContract.HomeView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val api: TmdbApi) : HomeContract.HomePresenter {
    override var homeView: HomeView? = null

    private val compositeDisposable = CompositeDisposable()
    private var pageNumber: Long = 1

    override fun getMovieListWithGenres() {
        homeView?.showProgress()

        val disposable = api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<GenreResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: GenreResponse) {
                        Cache.cacheGenres(t.genres)
                        getFirstPage()
                    }

                    override fun onError(e: Throwable) {
                        homeView?.showMessage(R.string.general_connection_message)
                        homeView?.showRetryButton()
                    }
                })
        compositeDisposable.add(disposable)
    }

    private fun getFirstPage() {
        val disposable = api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<UpcomingMoviesResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: UpcomingMoviesResponse) {
                        val moviesWithGenres = t.results.map { movie ->
                            movie.copy(genres = Cache.genres.filter { genre ->
                                movie.genreIds?.contains(genre.id) == true
                            })
                        }
                        homeView?.populateMovieList(moviesWithGenres)
                        homeView?.hideProgress()
                    }

                    override fun onError(e: Throwable) {
                        homeView?.showMessage(R.string.general_connection_message)
                        homeView?.showRetryButton()
                    }
                })
        compositeDisposable.add(disposable)
    }

    override fun getNextPage() {
        val disposable = api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, ++pageNumber, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<UpcomingMoviesResponse>() {
                    override fun onComplete() {
                    }

                    override fun onNext(t: UpcomingMoviesResponse) {
                        if (t.results.isEmpty()) {
                            homeView?.showMessage(R.string.no_new_data)
                            pageNumber = 1
                        } else {
                            val moviesWithGenres = t.results.map { movie ->
                                movie.copy(genres = Cache.genres.filter { genre ->
                                    movie.genreIds?.contains(genre.id) == true
                                })
                            }
                            homeView?.addNewPage(moviesWithGenres)
                        }
                    }

                    override fun onError(e: Throwable) {
                        homeView?.showMessage(R.string.general_connection_message)
                        homeView?.showRetryButton()
                    }
                })

        compositeDisposable.add(disposable)
    }

    override fun detachView() {
        homeView = null
        compositeDisposable.dispose()
    }
}