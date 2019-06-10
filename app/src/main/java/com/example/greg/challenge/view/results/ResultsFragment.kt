package com.example.greg.challenge.view.results

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
import com.example.greg.challenge.model.StatusError
import com.example.greg.challenge.model.StatusLoading
import com.example.greg.challenge.model.StatusSuccess
import com.example.greg.challenge.view.SearchActivity.Companion.SEARCH_TAG
import com.example.greg.challenge.viewmodel.DetailViewModel
import com.example.greg.challenge.viewmodel.ResultsViewModel
import com.example.greg.challenge.viewmodel.ViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ResultsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory : ViewModelFactory

    private lateinit var recyclerView : RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var noResultsView : View
    private lateinit var errorView: LinearLayout
    private lateinit var errorText: TextView
    private lateinit var welcomeView: TextView
    private lateinit var searchProgress: ProgressBar

    private lateinit var resultsViewModel: ResultsViewModel
    private lateinit var detailViewModel: DetailViewModel


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(SEARCH_TAG, "ResultsFRagment onCreate")
        super.onCreate(savedInstanceState)

        activity?.let {
            resultsViewModel = ViewModelProviders.of(it, viewModelFactory).get(ResultsViewModel::class.java)
            detailViewModel = ViewModelProviders.of(it, viewModelFactory).get(DetailViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        bindViews(view)

        setUpRecyclerView()

        displayWelcomeView()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        resultsViewModel.results().observe(viewLifecycleOwner, Observer {
            Log.d(SEARCH_TAG, "ResultsFragment resultsViewModel onUpdate")
            when(it){
                is StatusLoading -> displayLoading()
                is StatusSuccess -> displaySuccess(it.resultsList)
                is StatusError -> displayErrorView(it.error)
            }
        })
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.results_recycler_view)
        noResultsView = view.findViewById(R.id.no_results_view)
        errorView = view.findViewById(R.id.error_view)
        errorText = view.findViewById(R.id.error_text)
        welcomeView = view.findViewById(R.id.welcome_message)
        searchProgress = view.findViewById(R.id.search_progress)
    }

    private fun setUpRecyclerView() {
        resultsAdapter = ResultsAdapter(context, detailViewModel)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = resultsAdapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ColorDrawable(resources.getColor(R.color.mediumGray, null)))
        recyclerView.addItemDecoration(decoration)
    }

    private fun displayLoading() {
        noResultsView.visibility = View.GONE
        welcomeView.visibility = View.GONE
        searchProgress.visibility = View.VISIBLE
    }

    private fun displaySuccess(resultsList: List<Repo>) {
        noResultsView.visibility = View.GONE
        searchProgress.visibility = View.GONE
        errorView.visibility = View.GONE
        welcomeView.visibility = View.GONE

        if(resultsList.isNullOrEmpty()){
            displayNoResultsView()
        } else {
            displayNewResults(resultsList)
        }
    }

    private fun displayErrorView(error: String) {
        welcomeView.visibility = View.GONE
        noResultsView.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchProgress.visibility = View.GONE

        errorView.visibility = View.VISIBLE
        errorText.text = getString(R.string.error_text, error)

        //the least we can do is hide the keyboard while we display this awful error handling
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun displayNoResultsView() {
        welcomeView.visibility = View.GONE
        noResultsView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun displayNewResults(dataList : List<Repo>) {
        welcomeView.visibility = View.GONE
        noResultsView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        resultsAdapter.setData(dataList)
        recyclerView.smoothScrollToPosition(0)
    }

    private fun displayWelcomeView() {
        welcomeView.visibility = View.VISIBLE
    }

    companion object {
        const val RESULTS_FRAGMENT_TAG = "resultsFragmentTAG"

        fun newInstance(): ResultsFragment {
            return ResultsFragment()
        }

    }
}