package com.example.greg.challenge.results

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.greg.challenge.R
import com.example.greg.challenge.Repo

class ResultsFragment : Fragment() {

    private lateinit var resultsList : ArrayList<Repo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        resultsList = arguments?.getSerializable(RESULTS_FRAGMENT_LIST) as ArrayList<Repo>
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.results_recycler_view)
        val context = activity as Context

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = ResultsAdapter(context, resultsList)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return view
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