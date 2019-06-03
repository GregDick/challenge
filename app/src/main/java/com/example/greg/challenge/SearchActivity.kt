package com.example.greg.challenge

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.greg.challenge.results.ResultsFragment
import com.example.greg.challenge.results.ResultsFragment.Companion.RESULTS_FRAGMENT_TAG
import com.example.greg.challenge.search.SearchProcessor.Companion.SEARCH_TAG
import com.example.greg.challenge.search.SearchScreenView
import com.example.greg.challenge.search.SearchScreenViewState
import com.example.greg.challenge.search.SearchViewModel
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(), SearchScreenView {

    //todo replace with dependency injection
    private val searchViewModel = SearchViewModel()

    private lateinit var searchQueryIntent : Observable<CharSequence>
    private lateinit var toolbarSearchView : SearchView

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
        toolbarSearchView = (menu.findItem(R.id.search).actionView as SearchView).apply {

            searchQueryIntent = queryTextChanges()
                .skipInitialValue()
                .filter{ queryString -> queryString.length > 2} //todo: allow empty string to clear results?
                .debounce(500, TimeUnit.MILLISECONDS)

            queryHint = getString(R.string.search_hint)
        }

        searchViewModel.bind(this)  //todo: does this really have to be here? ideally this should go in onCreate()

        return true
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

    private fun renderDataState(repoList: ArrayList<Repo>) {
        Log.d(SEARCH_TAG, "rendering data state size: ${repoList.size}")

        toolbarSearchView.hideKeyboard()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ResultsFragment.newInstance(repoList), RESULTS_FRAGMENT_TAG)
            .commit()
    }

    private fun renderLoadingState() {
        Log.d(SEARCH_TAG, "rendering loading state")
    }

    private fun renderClearState() {
        Log.d(SEARCH_TAG, "rendering clear state")
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}
