package com.example.greg.challenge

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.greg.challenge.search.SearchProcessor.Companion.SEARCH_TAG
import com.example.greg.challenge.search.SearchScreenView
import com.example.greg.challenge.search.SearchScreenViewState
import com.example.greg.challenge.search.SearchViewModel
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(), SearchScreenView {

    //replace with dependency injection
    private val searchViewModel = SearchViewModel()

    private lateinit var searchQueryIntent : Observable<CharSequence>
    private var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.unbind()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    text_view.text = query //just for testing
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean { return false }
            })

            searchQueryIntent = queryTextChanges()
                .skipInitialValue()
                .filter{ queryString -> queryString.length > 3 || queryString.isEmpty() }
                .debounce(500, TimeUnit.MILLISECONDS)

            queryHint = getString(R.string.search_hint)
        }

        searchViewModel.bind(this)  //todo: does this really have to be here? ideally this should go in onCreate()

        return true
    }

    override fun onResume() {
        super.onResume()

//        val viewStateDisposable = searchViewModel.searchViewStateObservable()
//            .doOnSubscribe { Log.d(SEARCH_TAG, "subscribed  to ViewStateObservable") }
//            .subscribe(
//                { render(it) },
//                { Log.d(SEARCH_TAG, "view state observable error: $it") }
//            )
//
//        compositeDisposable.add(viewStateDisposable)

    }

    override fun searchQueryIntent(): Observable<CharSequence> {
        Log.d(SEARCH_TAG, "emit searchQueryIntent observable")
        return searchQueryIntent
    }

    override fun render(viewState: SearchScreenViewState) {
        Log.d(SEARCH_TAG, "SearchActivity render: $viewState")

        when (viewState) {
            is SearchScreenViewState.ClearState -> renderClearState()
            is SearchScreenViewState.LoadingState -> renderLoadingState()
            is SearchScreenViewState.DataState -> renderDataState(viewState.repoList)
            is SearchScreenViewState.ErrorState -> renderErrorState(viewState.error)
            is SearchScreenViewState.EmptyDataState -> renderEmptyState()
        }
    }

    private fun renderEmptyState() {
        Log.d(SEARCH_TAG, "rendering empty state ")
    }

    private fun renderErrorState(error: String) {
        Log.d(SEARCH_TAG, "rendering error state $error")
    }

    private fun renderDataState(repoList: List<Repo>) {
        Log.d(SEARCH_TAG, "rendering data state size: ${repoList.size}")
    }

    private fun renderLoadingState() {
        Log.d(SEARCH_TAG, "rendering loading state")
    }

    private fun renderClearState() {
        Log.d(SEARCH_TAG, "rendering clear state")
    }

}
