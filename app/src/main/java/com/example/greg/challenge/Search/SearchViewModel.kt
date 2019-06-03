package com.example.greg.challenge.Search

import android.util.Log
import androidx.annotation.MainThread
import com.example.greg.challenge.MVI.BaseViewModel
import com.example.greg.challenge.Repo
import com.example.greg.challenge.Search.SearchProcessor.Companion.SEARCH_TAG
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel : BaseViewModel<SearchScreenView> {

    //todo use dependency injection
    private val searchProcessor = SearchProcessor()

    private var compositeDisposable = CompositeDisposable()

    private lateinit var view : SearchScreenView
    private lateinit var searchQueryAction : Observable<CharSequence>
    private lateinit var searchViewStateObservable : Observable<SearchScreenViewState>

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

//    fun mapResultToState() {
//        Log.d(SEARCH_TAG, "mapResultToState")
//
//        val stateDisposable = searchProcessor.searchResult()
//            .map { result -> reduceResultToState(result) }
//            .onErrorReturn { SearchScreenViewState.ErrorState(it.message.toString()) }
//            .startWith { SearchScreenViewState.LoadingState }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                Log.d(SEARCH_TAG, "onNext searchViewStateObservable: $it")
//                view.render(it)
//            }, {
//                Log.d(SEARCH_TAG, "onError searchViewStateObservable: $it")
//            }, {
//                Log.d(SEARCH_TAG, "onComplete searchViewStateObservable")
//            }, {
//                Log.d(
//                    SEARCH_TAG,
//                    "subscribed to searchViewStateObservable"
//                )
//            })
//
//        compositeDisposable.add(stateDisposable)
//    }

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
            })
        compositeDisposable.add(stateDisposable)
    }

    fun searchQueryAction(): Observable<CharSequence> {
        return searchQueryAction
    }

    fun searchViewStateObservable(): Observable<SearchScreenViewState> {
        return searchViewStateObservable
    }

    private fun reduceResultToState(result : ArrayList<Repo>): SearchScreenViewState{
        Log.d(SEARCH_TAG, "reduceResultToState")
        return if (result.isNotEmpty()) SearchScreenViewState.DataState(result) else SearchScreenViewState.EmptyDataState
    }

    companion object{
        const val SEARCH_VIEW_MODEL_TAG = "searchViewModelTag"
    }
}