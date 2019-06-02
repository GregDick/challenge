package com.example.greg.challenge.Search

import android.util.Log
import com.example.greg.challenge.GithubApiService
import com.example.greg.challenge.GithubSearchResponse
import com.example.greg.challenge.MVI.BaseProcessor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SearchProcessor : BaseProcessor<SearchViewModel> {

    private val githubApiService by lazy {
        GithubApiService.create()
    }
    private var compositeDisposable = CompositeDisposable()
    private lateinit var viewModel : SearchViewModel

    override fun bind(viewModel: SearchViewModel) {
        this.viewModel = viewModel

        compositeDisposable.add(observeSearchQueryAction())
    }

    private fun observeSearchQueryAction(): Disposable {
        return viewModel.searchQueryAction()
            .doOnNext { compositeDisposable.add(searchGithubRepos(it)) }
            .doOnSubscribe { Log.d(SEARCH_TAG, "subscribed to searchQueryAction") }
            .subscribe()
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun searchGithubRepos(query : CharSequence) : Disposable{
        Log.d(SEARCH_TAG, "searchGithubRepos $query")
        return githubApiService.searchRepos(query as String)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> logResponse(response) },
                { error -> logError(error.message) },
                {logComplete()},
                {logSubscribed()}
            )
    }

    private fun logSubscribed() {
        Log.d(SEARCH_TAG, "onSubscribe subscribed to searchQueryResult")
    }

    private fun logComplete() {
        Log.d(SEARCH_TAG, "searchQueryResult onComplete")
    }

    private fun logResponse(result: GithubSearchResponse) {
        Log.d(SEARCH_TAG, result.toString())
    }

    private fun logError(message: String?) {
        Log.d(SEARCH_TAG, "error : $message")
    }


    companion object {
        const val SEARCH_PROCESSOR_TAG = "searchProcessorTag"
        const val SEARCH_TAG = "[SEARCH]"
    }

}
