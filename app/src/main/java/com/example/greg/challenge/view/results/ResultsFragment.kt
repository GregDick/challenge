package com.example.greg.challenge.view.results

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greg.challenge.R
import com.example.greg.challenge.model.Repo
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
    private lateinit var errorView: TextView
    private lateinit var resultsViewModel: ResultsViewModel
    private lateinit var detailViewModel: DetailViewModel


    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            resultsViewModel = ViewModelProviders.of(it, viewModelFactory).get(ResultsViewModel::class.java)
            detailViewModel = ViewModelProviders.of(it, viewModelFactory).get(DetailViewModel::class.java)
        }

        resultsViewModel.resultsList().observe(viewLifecycleOwner, Observer {
            Log.d(SEARCH_TAG, "ResultsFragment resultsViewModel onUpdate")
            if(it.isNullOrEmpty()){
                displayNoResultsView()
            } else {
                displayNewResults(it)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        bindViews(view)

        setUpRecyclerView()

        return view
    }

    private fun setUpRecyclerView() {
        resultsAdapter = ResultsAdapter(context, detailViewModel)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = resultsAdapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        decoration.setDrawable(ColorDrawable(resources.getColor(R.color.mediumGray, null)))
        recyclerView.addItemDecoration(decoration)
    }

    private fun bindViews(view: View) {
        recyclerView = view.findViewById(R.id.results_recycler_view)
        noResultsView = view.findViewById(R.id.no_results_view)
        errorView = view.findViewById(R.id.error_view)
    }

    private fun displayNoResultsView() {
        noResultsView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    fun displayErrorView(error: String) {
        errorView.visibility = View.VISIBLE
        errorView.text = getString(R.string.error_text, error)
        recyclerView.visibility = View.GONE
    }

    private fun displayNewResults(dataList : List<Repo>) {
        noResultsView.visibility = View.GONE
        errorView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        resultsAdapter.setData(dataList)
    }

    companion object {
        const val RESULTS_FRAGMENT_TAG = "resultsFragmentTAG"

        fun newInstance(): ResultsFragment {
            return ResultsFragment()
        }

    }
}