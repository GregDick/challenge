package com.example.greg.challenge.Search

import android.util.Log
import com.example.greg.challenge.MVI.BaseViewModel
import com.example.greg.challenge.Search.SearchProcessor.Companion.SEARCH_TAG
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchViewModel : BaseViewModel<SearchScreenView> {
    //todo use dependency injection
    private val searchProcessor = SearchProcessor()

    private var compositeDisposable = CompositeDisposable()

    private lateinit var view : SearchScreenView
    private lateinit var searchQueryAction : Observable<CharSequence>

    override fun bind(view : SearchScreenView) {
        this.view = view

        mapIntentsToActions()

//        compositeDisposable.add(observeSearchQueryAction())

        searchProcessor.bind(this)
    }

    override fun reduce() {
        view.render(SearchScreenViewState.LoadingState) //should wait for result from PROCESSOR
    }

    override fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        searchProcessor.unbind()
    }

    private fun mapIntentsToActions() {
        searchQueryAction = view.searchQueryIntent()
            .doOnSubscribe {
                Log.d(SEARCH_TAG, "subscribed to observeSearchQueryIntent")
            }

        //we don't need to modify anything about the search query so this action
        // is just the original search query intent observable being passed along
    }

    fun searchQueryAction(): Observable<CharSequence> {
        return searchQueryAction
    }

    companion object{
        const val SEARCH_VIEW_MODEL_TAG = "searchViewModelTag"
    }
}