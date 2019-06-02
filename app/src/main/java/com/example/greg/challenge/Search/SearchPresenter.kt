package com.example.greg.challenge.Search

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class SearchPresenter {

    private var compositeDisposable = CompositeDisposable()
    private lateinit var view : SearchScreenView

    fun bind(view : SearchScreenView) {
        this.view = view

        compositeDisposable.add(observeSearchQueryIntent())
    }

    private fun observeSearchQueryIntent(): Disposable {
        return view.emitSearchQueryIntent()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.render(SearchScreenViewState.LoadingState) } //this state is actually for during the network request
            .doOnNext {
                Log.d(SEARCH_PRESENTER_TAG, "observeSearchQueryIntent $it")
            }
            .subscribe()
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