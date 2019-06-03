package com.example.greg.challenge.search

import android.util.Log
import com.example.greg.challenge.GithubApiService
import com.example.greg.challenge.GithubSearchResponse
import com.example.greg.challenge.Repo
import com.example.greg.challenge.mvi.BaseProcessor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchProcessor : BaseProcessor<SearchViewModel> {

    private val githubApiService by lazy {
        GithubApiService.create()
    }
    private var compositeDisposable = CompositeDisposable()
    private lateinit var viewModel : SearchViewModel
    private lateinit var searchResult : Observable<ArrayList<Repo>>

    override fun bind(viewModel: SearchViewModel) {
        Log.d(SEARCH_TAG, "processor bind")

        this.viewModel = viewModel

        subscribeToViewModelAction()
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun subscribeToViewModelAction() {
        Log.d(SEARCH_TAG, "subscribeToViewModelAction")

        val actionDisposable = viewModel.searchQueryAction()
            .subscribe({
                Log.d(SEARCH_TAG, "onNext searchQueryAction: $it")
                searchResult = searchGithubRepos(it)
                viewModel.subscribeToSearchResult() //todo: can this be called from a better place? searchResult must exist before viewModel can subscribe to it...
            }, {
                Log.d(SEARCH_TAG, "onError searchQueryAction: $it")
            }, {
                Log.d(SEARCH_TAG, "onComplete searchQueryAction")
            }, {
                Log.d(SEARCH_TAG, "subscribed to searchQueryAction")  //this is essentially subscribing to the View Intent
            })

        compositeDisposable.add(actionDisposable)
    }

    private fun searchGithubRepos(query : CharSequence) : Observable<ArrayList<Repo>>{
        Log.d(SEARCH_TAG, "searchGithubRepos $query")

        return githubApiService.searchRepos(query as String)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                    response -> Observable.just(generateListFromResponse(response))
            }
    }

    private fun generateListFromResponse(response: GithubSearchResponse): ArrayList<Repo> {
        val responseList = arrayListOf<Repo>()
        if (response.items.isNotEmpty()) {
            val tempList = response.items.filterIsInstance<Repo>() as ArrayList<Repo>
            responseList.addAll(tempList.subList(0, 20)) //todo: can I tell the API to only give me 20 items in the response?
        }
        return responseList
    }

    fun searchResult() : Observable<ArrayList<Repo>> {
        return searchResult
    }

    companion object {
        const val SEARCH_TAG = "[SEARCH]"
    }

}
