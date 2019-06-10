package com.example.greg.challenge.model.repository

import com.example.greg.challenge.model.GithubSearchResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApiService {

    @GET(GITHUB_SEARCH_ENDPOINT)
    fun searchRepos(@Query("q") query: String, @Query("per_page") numResults : Int): Observable<GithubSearchResponse>

    companion object {

        const val GITHUB_API_BASE_URL = "https://api.github.com"
        const val GITHUB_SEARCH_ENDPOINT = "/search/repositories"
        const val MAX_RESULTS_PER_PAGE = 20
    }

}