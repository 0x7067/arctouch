package com.arctouch.codechallenge.ui.details

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.MovieDetails
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MovieDetailsPresenter(private val tmdbApi: TmdbApi) : MovieDetailsContract.MovieDetailsPresenter {

    override var movieView: MovieDetailsContract.MovieView? = null
    private val compositeDisposable = CompositeDisposable()

    override fun attachView(movieView: MovieDetailsContract.MovieView) {
        this.movieView = movieView
    }

    override fun getMovieDetailsById(id: Int) {
        val disposable = tmdbApi.movie(id, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Movie>() {
                    override fun onSuccess(movie: Movie) {
                        val movieDetails = MovieDetails(movie.title,
                                movie.overview,
                                movie.genres?.joinToString(separator = ", ") { it.name },
                                movie.backdropPath?.let { MovieImageUrlBuilder.buildBackdropUrl(it) },
                                movie.releaseDate)
                        movieView?.setMovieDetails(movieDetails)
                    }

                    override fun onError(e: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                })
        compositeDisposable.add(disposable)
    }

    override fun detachView() {
        this.movieView = null
        compositeDisposable.dispose()
    }
}