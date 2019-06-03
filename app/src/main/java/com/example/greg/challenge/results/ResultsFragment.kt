package com.example.greg.challenge.results

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greg.challenge.R
import com.example.greg.challenge.Repo

class ResultsFragment : Fragment() {

    private lateinit var resultsList : ArrayList<Repo>
    private lateinit var recyclerView : RecyclerView
    private lateinit var resultsAdapter: ResultsAdapter
    private lateinit var noResultsView : View
    private lateinit var errorView : TextView

    private lateinit var listener: ResultsFragmentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultsList = arguments?.getSerializable(RESULTS_FRAGMENT_LIST) as ArrayList<Repo>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)
        val context = activity as Context

        listener = context as ResultsFragmentListener

        recyclerView = view.findViewById(R.id.results_recycler_view)
        noResultsView = view.findViewById(R.id.no_results_view)
        errorView = view.findViewById(R.id.error_view)

        resultsAdapter = ResultsAdapter(context, resultsList, listener)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = resultsAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        if (resultsList.isEmpty()) { //if the first search is empty, render empty state
            renderEmptyState()
        }

        return view
    }

    fun renderEmptyState() {
        noResultsView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    fun renderErrorState(error: String) {
        errorView.visibility = View.VISIBLE
        errorView.text = getString(R.string.error_text, error)
        recyclerView.visibility = View.GONE
    }

    fun renderDataView(dataList : ArrayList<Repo>) {
        noResultsView.visibility = View.GONE
        errorView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        resultsAdapter.resultsList = dataList
        resultsAdapter.notifyDataSetChanged()
    }

    interface ResultsFragmentListener {
        fun onResultClicked(repo: Repo)
    }

    companion object {
        const val RESULTS_FRAGMENT_LIST = "resultsFragmentList"
        const val RESULTS_FRAGMENT_TAG = "resultsFragmentTAG"

        fun newInstance(resultsList : ArrayList<Repo>): ResultsFragment {
            val args = Bundle()
            args.putSerializable(RESULTS_FRAGMENT_LIST, resultsList)

            val fragment = ResultsFragment()
            fragment.arguments = args

            return fragment
        }

    }
}