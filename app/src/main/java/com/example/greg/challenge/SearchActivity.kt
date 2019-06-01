package com.example.greg.challenge

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import com.example.greg.challenge.Search.SearchPresenter
import com.example.greg.challenge.Search.SearchScreenView
import com.example.greg.challenge.Search.SearchScreenViewState
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchScreenView {

    private var searchQuery = ""

    //replace with dependency injection
    private val searchPresenter = SearchPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchPresenter.bind(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.unbind()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    text_view.text = query
                    searchQuery = query ?: ""
                    emitSearchQueryIntent()
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean { return true }
            })

            queryHint = getString(R.string.search_hint)
        }
        return true
    }

    override fun emitSearchQueryIntent(): Observable<String> {
        Log.d(SEARCH_ACTIVITY_LOG_TAG, "emitSearchQueryIntent $searchQuery")
        return Observable.just(searchQuery)
    }

    override fun render(searchScreenViewState: SearchScreenViewState) {
        when (searchScreenViewState) {
            is SearchScreenViewState.ClearState -> renderClearState()
            is SearchScreenViewState.LoadingState -> renderLoadingState()
            is SearchScreenViewState.DataState -> renderDataState(searchScreenViewState.repoList)
            is SearchScreenViewState.ErrorState -> renderErrorState(searchScreenViewState.error)
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
