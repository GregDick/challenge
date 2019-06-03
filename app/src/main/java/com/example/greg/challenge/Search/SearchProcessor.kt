package com.example.greg.challenge.Search

import android.util.Log
import com.example.greg.challenge.GithubApiService
import com.example.greg.challenge.GithubSearchResponse
import com.example.greg.challenge.MVI.BaseProcessor
import com.example.greg.challenge.Repo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

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

        mapActionToResult()
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    private fun mapActionToResult() {
        Log.d(SEARCH_TAG, "mapActionToResult")

//        searchResult = viewModel.searchQueryAction()
//            .doOnSubscribe {
//                Log.d(SEARCH_TAG, "subscribed to searchQueryAction")  //this is essentially subscribing to the View Intent
//                compositeDisposable.add(it)
//            }
//            .doOnError {Log.d(SEARCH_TAG, "onError searchQueryAction: $it")}
//            .flatMap { searchGithubRepos(it) }

        val actionDisposable = viewModel.searchQueryAction()
            .subscribe({
                Log.d(SEARCH_TAG, "onNext searchQueryAction: $it")
                searchResult = searchGithubRepos(it)
                viewModel.subscribeToSearchResult()
            }, {
                Log.d(SEARCH_TAG, "onError searchQueryAction: $it")
            }, {
                Log.d(SEARCH_TAG, "onComplete searchQueryAction")
            }, {
                Log.d(SEARCH_TAG, "subscribed to searchQueryAction")  //this is essentially subscribing to the View Intent
            })

        compositeDisposable.add(actionDisposable)
    }


//    private fun observeSearchQueryAction() {
////        return viewModel.searchQueryAction()
////            .doOnNext { compositeDisposable.add(searchGithubRepos(it)) }
////            .doOnSubscribe { Log.d(SEARCH_TAG, "subscribed to searchQueryAction") }
////            .subscribe() //this is essentially subscribing to the View Intent
//
//        viewModel.searchQueryAction()
//            .doOnSubscribe {
//                Log.d(SEARCH_TAG, "subscribed to searchQueryAction")
//                compositeDisposable.add(it)
//            }//this is essentially subscribing to the View Intent
//            .doOnError {Log.d(SEARCH_TAG, "onError searchQueryAction: $it")}
//            .flatMap { searchGithubRepos(it) }
//            .subscribe()
//    }

    private fun searchGithubRepos(query : CharSequence) : Observable<ArrayList<Repo>>{
        Log.d(SEARCH_TAG, "searchGithubRepos $query")

        return githubApiService.searchRepos(query as String)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                    response -> Observable.just(generateListFromResponse(response))
            }
    }

    private fun generateListFromResponse(response : GithubSearchResponse) : ArrayList<Repo>{
        var responseList = arrayListOf<Repo>()
        if (response.items.isNotEmpty()) {
             responseList = response.items.filterIsInstance<Repo>() as ArrayList<Repo>
        }
        return responseList
    }

    fun searchResult() : Observable<ArrayList<Repo>> {
        return searchResult
    }


    private fun logSubscribed() {
        Log.d(SEARCH_TAG, "onSubscribe subscribed to searchQueryResult")
    }

    private fun logComplete() {
        Log.d(SEARCH_TAG, "searchQueryResult onComplete")
    }

    private fun logResponse(result: GithubSearchResponse) {
        Log.d(SEARCH_TAG, result.toString())
        if(result.items.isNotEmpty()){
            val item : Repo? = result.items[0]
            Log.d(SEARCH_TAG, "has item name: " + item?.name)

        }
    }

    private fun logError(message: String?) {
        Log.d(SEARCH_TAG, "error : $message")
    }


    companion object {
        const val SEARCH_PROCESSOR_TAG = "searchProcessorTag"
        const val SEARCH_TAG = "[SEARCH]"
    }

}
