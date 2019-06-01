package com.example.greg.challenge.Search

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

class SearchPresenter {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var view : SearchScreenView

    fun bind(view : SearchScreenView) {
        this.view = view

        compositeDisposable.add(observeSearchQuery())
    }

    private fun observeSearchQuery(): Disposable {
        return view.emitSearchQueryIntent()
            // subscribe to searchIntent
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onNext(query: String) {
                    //accept a search string and
                        //internally process the search string - make network call and return results or error
                    Log.d(SEARCH_PRESENTER_TAG, "onNext $query")
                }

                override fun onComplete() {
                    //emit an observable with SearchScreenViewState.DataState
                    Log.d(SEARCH_PRESENTER_TAG, "onComplete")
                }

                override fun onError(e: Throwable) {
                    //emit an observable with SearchScreenViewState.ErrorState
                    Log.d(SEARCH_PRESENTER_TAG, "onError $e")
                }
            })
    }

    fun unbind() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    companion object{
        const val SEARCH_PRESENTER_TAG = "searchPresenterTag"
    }
}