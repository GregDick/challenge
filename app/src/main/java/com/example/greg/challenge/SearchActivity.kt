package com.example.greg.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import com.example.greg.challenge.Search.SearchScreenView
import com.example.greg.challenge.Search.SearchState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchScreenView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    text_view.text = query
                    //todo emit search query intent
                    searchIntent(query?: "")
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean { return true }
            })

            queryHint = getString(R.string.search_hint)
        }
        return true
    }

    override fun searchIntent(searchQuery: String): Observable<String> {
        return Observable.just(searchQuery)
    }

    override fun render(searchState: SearchState) {
        when (searchState) {
            is SearchState.ClearState -> renderClearState()
            is SearchState.LoadingState -> renderLoadingState()
            is SearchState.DataState -> renderDataState(searchState.repoList)
            is SearchState.ErrorState -> renderErrorState(searchState.error)
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
