package com.example.greg.challenge.search

import android.util.Log
import com.example.greg.challenge.Repo
import com.example.greg.challenge.mvi.BaseViewModel
import com.example.greg.challenge.search.SearchProcessor.Companion.SEARCH_TAG
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel : BaseViewModel<SearchScreenView> {

    //todo use dependency injection
    private val searchProcessor = SearchProcessor()

    private var compositeDisposable = CompositeDisposable()

    private lateinit var view : SearchScreenView
    private lateinit var searchQueryAction : Observable<CharSequence>

    override fun bind(view : SearchScreenView) {
        Log.d(SEARCH_TAG, "view model bind")

        this.view = view

        mapIntentsToActions() //subscribes to View Intent and emits an Action to the Processor

        searchProcessor.bind(this)
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        searchProcessor.unbind()
    }

    private fun mapIntentsToActions() {
        Log.d(SEARCH_TAG, "mapIntentsToActions")

        searchQueryAction = view.searchQueryIntent()
            .doOnSubscribe {
                Log.d(SEARCH_TAG, "subscribed to observeSearchQueryIntent")
            }

        //we don't need to modify anything about the search query so this action
        // is just the original search query intent observable being passed along
    }

    fun subscribeToSearchResult() {
        val stateDisposable = searchProcessor.searchResult()
            .doOnSubscribe {
                Log.d(SEARCH_TAG, "subscribed to searchResultObservable")
                view.render(SearchScreenViewState.LoadingState)
            }
            .subscribe({
                Log.d(SEARCH_TAG, "onNext searchResultObservable: $it")
                view.render(reduceResultToState(it))
            }, {
                Log.d(SEARCH_TAG, "onError searchResultObservable: $it")
                view.render(SearchScreenViewState.ErrorState(it.message.toString()))
            })
        compositeDisposable.add(stateDisposable)
    }

    fun searchQueryAction(): Observable<CharSequence> {
        return searchQueryAction
    }

    private fun reduceResultToState(result : ArrayList<Repo>): SearchScreenViewState{
        Log.d(SEARCH_TAG, "reduceResultToState")
        return if (result.isNotEmpty()) SearchScreenViewState.DataState(result) else SearchScreenViewState.EmptyDataState
    }
}