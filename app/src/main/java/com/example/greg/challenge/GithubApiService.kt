package com.example.greg.challenge

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET(GITHUB_SEARCH_ENDPOINT)
    fun searchRepos(@Query("q") query: String): Observable<Model.Repo>

    companion object {
        fun create(): GithubApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    MoshiConverterFactory.create())
                .baseUrl(GITHUB_API_BASE_URL)
                .build()

            return retrofit.create(GithubApiService::class.java)
        }

        private const val GITHUB_API_BASE_URL = "https://api.github.com"
        const val GITHUB_SEARCH_ENDPOINT = "/search/repositories"
    }

}