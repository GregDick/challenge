package com.example.greg.challenge.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.details.DetailFragment
import com.example.greg.challenge.view.details.DetailFragment.Companion.DETAIL_FRAGMENT_TAG
import com.example.greg.challenge.view.results.ResultsFragment
import com.example.greg.challenge.view.results.ResultsFragment.Companion.RESULTS_FRAGMENT_TAG
import com.example.greg.challenge.viewmodel.DetailViewModel
import com.example.greg.challenge.viewmodel.ResultsViewModel
import com.example.greg.challenge.viewmodel.ViewModelFactory
import com.jakewharton.rxbinding3.appcompat.queryTextChangeEvents
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), HasSupportFragmentInjector{

    @Inject
    lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private lateinit var toolbarSearchView : SearchView
    private lateinit var resultsViewModel : ResultsViewModel
    private lateinit var detailViewModel : DetailViewModel

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        resultsViewModel = ViewModelProviders.of(this, viewModelFactory).get(ResultsViewModel::class.java)
        detailViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        detailViewModel.detail().observe(this, Observer<Repo>{
            Log.d(SEARCH_TAG, "SearchActivity onDetailUpdated ${it.name}")
            startDetailFragment()
            if (::toolbarSearchView.isInitialized) toolbarSearchView.hideKeyboard() //only necessary because onCreateOptionsMenu happens after onCreate
        })

        startResultsFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchMenuItem = (menu.findItem(R.id.search))

        toolbarSearchView = (searchMenuItem.actionView as SearchView).apply {

            queryTextChangeEvents()
                .filter { it.queryText.isNotEmpty() } //todo: allow empty string to clear results?
                .distinctUntilChanged()
                .debounce(300, TimeUnit.MILLISECONDS) //waits for user to finish typing before sending api request
                .subscribe({
                    if(it.isSubmitted){
                        runOnUiThread { hideKeyboard() } //pressing enter hides keyboard
                    }
                    resultsViewModel.searchForQuery(it.queryText.toString())
                    supportFragmentManager.popBackStack() //if searching from DetailFragment, this makes sure we're on ResultsFragment
                }, {
                    Log.d(SEARCH_TAG, "searchView error ${it.localizedMessage}")
                })

            queryHint = getString(R.string.search_hint)
        }

        toolbarSearchView.setIconifiedByDefault(false) //starts the app with the search bar expanded and focused
        toolbarSearchView.requestFocus()

        return true
    }

    private fun startResultsFragment() {
        val resultsFragment = supportFragmentManager.findFragmentByTag(RESULTS_FRAGMENT_TAG)

        if (resultsFragment != null) {
            Log.d(SEARCH_TAG, "results fragment already exists")
        } else {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, ResultsFragment.newInstance(), RESULTS_FRAGMENT_TAG)
                .commit()
        }
    }

    private fun startDetailFragment() {
        val detailFragment = supportFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_TAG)

        if (detailFragment != null) {
            Log.d(SEARCH_TAG, "details fragment already exists")
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, DetailFragment.newInstance(), DETAIL_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
        }
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
