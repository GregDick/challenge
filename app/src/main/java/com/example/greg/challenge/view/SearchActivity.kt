package com.example.greg.challenge.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.view.results.ResultsFragment
import com.example.greg.challenge.view.results.ResultsFragment.Companion.RESULTS_FRAGMENT_TAG
import com.example.greg.challenge.viewmodel.ResultsViewModel
import com.example.greg.challenge.viewmodel.ViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), ResultsFragment.ResultsFragmentListener {

    private lateinit var toolbarSearchView : SearchView

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        val model = ViewModelProviders.of(this, viewModelFactory).get(ResultsViewModel::class.java)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        toolbarSearchView = (menu.findItem(R.id.search).actionView as SearchView).apply {

            setOnQueryTextListener( object : SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        if(newText.length > 2) {
                            // todo: pass to viewModel
                        }
                    }
                    return true
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard()
                    return true
                }
            })

            queryHint = getString(R.string.search_hint)
        }

        toolbarSearchView.requestFocus() //todo open the search bar after the menu is created
        toolbarSearchView.showKeyboard()

        return true
    }

    override fun onResultClicked(repo: Repo) {
        Log.d(SEARCH_TAG, "onResultClicked")
        // todo: load details fragment
    }

    private fun startResultsFragment(repoList: ArrayList<Repo>) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ResultsFragment.newInstance(repoList), RESULTS_FRAGMENT_TAG)
            .addToBackStack(null)
            .commit()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    companion object {
        const val SEARCH_TAG = "[SEARCH]"
    }

}
