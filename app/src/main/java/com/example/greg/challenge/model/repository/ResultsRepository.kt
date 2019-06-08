package com.example.greg.challenge.model.repository

import android.util.Log
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.model.repository.GithubApiService.Companion.MAX_RESULTS_PER_PAGE
import com.example.greg.challenge.view.SearchActivity.Companion.SEARCH_TAG
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ResultsRepository {

    private val githubApiService by lazy {
        GithubApiService.create()
    }


    fun getResults(query: String): Observable<List<Repo>> {
        Log.d(SEARCH_TAG, "searchGithubRepos $query")

        return githubApiService.searchRepos(query, MAX_RESULTS_PER_PAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> response.items }
    }

}