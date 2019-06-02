package com.example.greg.challenge

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.greg.challenge.Search.SearchViewModel
import com.example.greg.challenge.Search.SearchScreenView
import com.example.greg.challenge.Search.SearchScreenViewState
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(), SearchScreenView {

    //replace with dependency injection
    private val searchViewModel = SearchViewModel()

    private lateinit var searchQueryIntent : Observable<CharSequence>

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

    override fun searchQueryIntent(): Observable<CharSequence> {
        Log.d(SEARCH_ACTIVITY_LOG_TAG, "searchQueryIntent")
        return searchQueryIntent
    }

    override fun render(viewState: SearchScreenViewState) {
        when (viewState) {
            is SearchScreenViewState.ClearState -> renderClearState()
            is SearchScreenViewState.LoadingState -> renderLoadingState()
            is SearchScreenViewState.DataState -> renderDataState(viewState.repoList)
            is SearchScreenViewState.ErrorState -> renderErrorState(viewState.error)
        }
    }

    private fun renderErrorState(error: String) {
        Log.d(SEARCH_ACTIVITY_LOG_TAG, "rendering error state $error")
    }

    private fun renderDataState(repoList: List<Repo>) {
        Log.d(SEARCH_ACTIVITY_LOG_TAG, "rendering data state size: ${repoList.size}")
    }

    private fun renderLoadingState() {
        Log.d(SEARCH_ACTIVITY_LOG_TAG, "rendering loading state")
    }

    private fun renderClearState() {
        Log.d(SEARCH_ACTIVITY_LOG_TAG, "rendering clear state")
    }

    companion object {
        private const val SEARCH_ACTIVITY_LOG_TAG = "SearchActivity"
    }

}
