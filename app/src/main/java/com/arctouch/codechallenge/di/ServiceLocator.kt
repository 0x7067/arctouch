package com.arctouch.codechallenge.di

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.ui.details.MovieDetailsPresenter
import com.arctouch.codechallenge.ui.home.HomePresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceLocator {

    private val baseUrl: String
        get() = TmdbApi.URL

    val tmdbApi: TmdbApi
        get() = getRetrofit().create(TmdbApi::class.java)

    val movieDetailsPresenter: MovieDetailsPresenter
        get() = MovieDetailsPresenter(tmdbApi)

    val homePresenter: HomePresenter
        get() = HomePresenter(tmdbApi)

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = BASIC

        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
                .client(getOkHttpClient())
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}