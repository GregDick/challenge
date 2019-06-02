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
    }

    private fun mapIntentsToActions() {
        searchQueryAction = view.searchQueryIntent()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Log.d(SEARCH_TAG, "subscribed to observeSearchQueryIntent")
            }
            .doOnNext {
                Log.d(SEARCH_TAG, "observeSearchQueryAction $it")
                //todo: should I emit something here? currently this does nothing
            }
    }

    fun searchQueryAction(): Observable<CharSequence> {
        Log.d(SEARCH_TAG, "searchQueryAction observable")
        return searchQueryAction
    }

    private fun observeSearchQueryAction(): Disposable {
        Log.d(SEARCH_TAG, "subscribed to searchQueryAction observable")
        return searchQueryAction.subscribe()
    }

    companion object{
        const val SEARCH_VIEW_MODEL_TAG = "searchViewModelTag"
    }
}