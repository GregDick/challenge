package com.example.greg.challenge.Search

import android.util.Log
import com.example.greg.challenge.MVI.BaseViewModel
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchViewModel : BaseViewModel {
    //todo use dependency injection
    private val searchProcessor = SearchProcessor()

    private var compositeDisposable = CompositeDisposable()

    private lateinit var view : SearchScreenView
    private lateinit var searchQueryAction : Observable<CharSequence>

    override fun bind(view : SearchScreenView) {
        this.view = view

        mapIntentsToActions()

        compositeDisposable.add(observeSearchQueryIntent())

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
                Log.d(SEARCH_VIEW_MODEL_TAG, "subscribed to observeSearchQueryIntent")
            }
            .doOnNext {
                Log.d(SEARCH_VIEW_MODEL_TAG, "observeSearchQueryIntent $it")
            }
    }

    fun searchQueryAction(): ObservableSource<CharSequence> {
        Log.d(SEARCH_VIEW_MODEL_TAG, "searchQueryAction observable")
        return searchQueryAction
    }

    private fun observeSearchQueryIntent(): Disposable {
        return searchQueryAction.subscribe()
    }

    companion object{
        const val SEARCH_VIEW_MODEL_TAG = "searchViewModelTag"
    }
}