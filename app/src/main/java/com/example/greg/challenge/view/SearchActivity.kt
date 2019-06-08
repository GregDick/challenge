package com.example.greg.challenge.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.details.DetailFragment
import com.example.greg.challenge.view.details.DetailFragment.Companion.DETAIL_FRAGMENT_TAG
import com.example.greg.challenge.view.results.ResultsFragment
import com.example.greg.challenge.view.results.ResultsFragment.Companion.RESULTS_FRAGMENT_TAG
import com.example.greg.challenge.viewmodel.ResultsViewModel
import com.example.greg.challenge.viewmodel.ViewModelFactory
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), HasSupportFragmentInjector,  ResultsFragment.ResultsFragmentListener {

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private lateinit var toolbarSearchView : SearchView
    private lateinit var viewModel : ResultsViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ResultsViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        startResultsFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchMenuItem = (menu.findItem(R.id.search))

        toolbarSearchView = (searchMenuItem.actionView as SearchView).apply {

            queryTextChanges()
                .filter{ queryString -> queryString.length > 2} //todo: allow empty string to clear results?
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS) //waits for user to finish typing before sending api request
                .subscribe {
                    Log.d(SEARCH_TAG, "$it")
                    viewModel.searchForQuery(it as String)
                    //todo remove detail fragment if it exists?
                }

            queryHint = getString(R.string.search_hint)
        }

        toolbarSearchView.setIconifiedByDefault(false) //starts the app with the search bar expanded and focused
        toolbarSearchView.requestFocus()

        return true
    }

    override fun onResultClicked() {
        Log.d(SEARCH_TAG, "onResultClicked")
        toolbarSearchView.hideKeyboard()
        startDetailFragment()
    }

    private fun startResultsFragment() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, ResultsFragment.newInstance(), RESULTS_FRAGMENT_TAG)
            .commit()
    }

    private fun startDetailFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, DetailFragment.newInstance(), DETAIL_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    private fun SearchView.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
        this.clearFocus()
    }

    private fun SearchView.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        this.requestFocus()
    }

    companion object {
        const val SEARCH_TAG = "[SEARCH]"
    }

}
